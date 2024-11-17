package data_access;

import entity.Nutrient;
import entity.RecipeForSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class RecipeSearchEdamam {
    private static final String APP_ID = "35f28703";
    private static final String APP_KEY = "acb2a3e8e5cd69c1e0bcacefd85ea880";
    private static final String BASE_URL = "https://api.edamam.com/api/recipes/v2";
    private static final String NA_URL = "https://api.edamam.com/api/nutrition-details";
    private final OkHttpClient httpClient = new OkHttpClient();

    /**
     * Get recipes by filling only food name.
     */
    public List<RecipeForSearch> searchRecipesByFoodName(String foodName) {
        List<RecipeForSearch> recipes = new ArrayList<>();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("type", "public");
        urlBuilder.addQueryParameter("q", foodName);
        urlBuilder.addQueryParameter("app_id", APP_ID);
        urlBuilder.addQueryParameter("app_key", APP_KEY);

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString()).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray hitsArray = jsonObject.getJSONArray("hits");

                for (int i = 0; i < hitsArray.length(); i++) {
                    JSONObject recipeJson = hitsArray.getJSONObject(i).getJSONObject("recipe");

                    // Extract recipe details
                    String name = recipeJson.getString("label");
                    String description = recipeJson.optString("source", "No description available");

                    // Extract ingredients
                    List<String> ingredients = new ArrayList<>();
                    JSONArray ingredientsArray = recipeJson.getJSONArray("ingredientLines");
                    for (int j = 0; j < ingredientsArray.length(); j++) {
                        ingredients.add(ingredientsArray.getString(j));
                    }

                    // Create and add Recipe entity
                    RecipeForSearch recipeForSearch = new RecipeForSearch(name, description, ingredients);
                    recipes.add(recipeForSearch);
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return recipes;
    }

    /**
     * Get recipes by selecting restrictions.
     */
    public List<RecipeForSearch> searchRecipesByRestriction(String foodName, String diet, String health, String cuisineType) {
        List<RecipeForSearch> recipes = new ArrayList<>();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("type", "public");
        urlBuilder.addQueryParameter("q", foodName);
        urlBuilder.addQueryParameter("app_id", APP_ID);
        urlBuilder.addQueryParameter("app_key", APP_KEY);

        // Add optional filters if provided
        if (diet != null && !diet.isEmpty()) {
            urlBuilder.addQueryParameter("diet", diet);
        }
        if (health != null && !health.isEmpty()) {
            urlBuilder.addQueryParameter("health", health);
        }
        if (cuisineType != null && !cuisineType.isEmpty()) {
            urlBuilder.addQueryParameter("cuisineType", cuisineType);
        }

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString()).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray hitsArray = jsonObject.getJSONArray("hits");

                for (int i = 0; i < hitsArray.length(); i++) {
                    JSONObject recipeJson = hitsArray.getJSONObject(i).getJSONObject("recipe");

                    // Extract recipe details
                    String name = recipeJson.getString("label");
                    String description = recipeJson.optString("source", "No description available");

                    // Extract ingredients
                    List<String> ingredients = new ArrayList<>();
                    JSONArray ingredientsArray = recipeJson.getJSONArray("ingredientLines");
                    for (int j = 0; j < ingredientsArray.length(); j++) {
                        ingredients.add(ingredientsArray.getString(j));
                    }

                    // Create and add Recipe entity
                    RecipeForSearch recipeForSearch = new RecipeForSearch(name, description, ingredients);
                    recipes.add(recipeForSearch);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return recipes;
    }

    /**
     * Get totalNutrients from a POST request from the API.
     * @param recipeName   The recipe name.
     * @param ingredients  A list of ingredients.
     * @return A Nutrient entity
     * @throws IOException If an I/O error occurs.
     */
    public List<Nutrient> analyzeNutrition(String recipeName, List<String> ingredients) throws IOException {
        List<Nutrient> nutrientsList = new ArrayList<>();

        // Build the URL using HttpUrl.Builder
        HttpUrl urlBuilder = HttpUrl.parse(NA_URL).newBuilder().addQueryParameter("app_id", APP_ID).addQueryParameter("app_key", APP_KEY).build();

        // Build the request body.
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", recipeName);
        requestBody.put("ingr", ingredients);

        Request request = new Request.Builder()
                .url(urlBuilder)
                .post(RequestBody.create(
                        requestBody.toString(),
                        MediaType.get("application/json; charset=utf-8")
                ))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject totalNutrients = jsonObject.getJSONObject("totalNutrients");

                // Iterate over each nutrient in the JSON object totalNutrients.
                for (String key : totalNutrients.keySet()) {
                    // Extract
                    JSONObject nutrient = totalNutrients.getJSONObject(key);

                    // Combine the values into a string and add them to the list
                    String aNutrient = "Label: " + nutrient.getString("label")
                            + ", Quantity: " + nutrient.getInt("quantity")
                            + ", Unit: " + nutrient.getString("unit");

                    nutrientsList.add(new Nutrient(aNutrient));
                }
            }
        }
        catch (IOException e) {
            throw new IOException(e);
        }
        return nutrientsList;
    }
}
