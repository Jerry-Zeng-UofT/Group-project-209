package data_access;

import entity.Ingredient;
import entity.Nutrient;
import entity.Recipe;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NutritionAnalysisDataAccessObject {
    private static final String APP_ID = "6ec3d1f4";
    private static final String APP_KEY = "0bae38670d4f3777d686730b59f0e707";
    private static final String NA_URL = "https://api.edamam.com/api/nutrition-details";
    private static final String BASE_URL = "https://api.edamam.com/api/recipes/v2";
    private final OkHttpClient httpClient = new OkHttpClient();

    public NutritionAnalysisDataAccessObject() {
        // Empty constructor
    }

    /**
     * Get totalNutrients from a POST request from the API.
     * @param recipe the recipe we want to analyze the nutrition of.
     * @return A Nutrient entity
     * @throws IOException If an I/O error occurs.
     */
    public List<Nutrient> analyzeNutrition(Recipe recipe) throws IOException {
        String recipeName = recipe.getTitle();
        JSONArray ingredients = getIngredientsById(recipeName);
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
        System.out.println("Request has been sent");

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
        System.out.println("nutrientsList has been returned");
        return nutrientsList;
    }

    public JSONArray getIngredientsById(String recipeId) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("type", "public");
        urlBuilder.addQueryParameter("id", recipeId);
        urlBuilder.addQueryParameter("app_id", APP_ID);
        urlBuilder.addQueryParameter("app_key", APP_KEY);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject recipeJson = jsonObject.getJSONObject("recipe");

                // Extract ingredients
                List<String> ingredients = new ArrayList<>();
                JSONArray ingredientsArray = recipeJson.getJSONArray("ingredientLines");

                return ingredientsArray;
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to get recipe details: " + e.getMessage());
        }
        return null;
    }
}

