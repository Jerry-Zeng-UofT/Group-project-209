package data_access;

import entity.RecipeForSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class RecipeSearchEdamam {
    private static final String APP_ID = "35f28703";
    private static final String APP_KEY = "acb2a3e8e5cd69c1e0bcacefd85ea880";
    private static final String BASE_URL = "https://api.edamam.com/api/recipes/v2";
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
    public List<RecipeForSearch> searchRecipesByRestriction(String foodName, String[] diet, String[] health, String[] cuisineType) {
        List<RecipeForSearch> recipes = new ArrayList<>();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("type", "public");
        urlBuilder.addQueryParameter("q", foodName);
        urlBuilder.addQueryParameter("app_id", APP_ID);
        urlBuilder.addQueryParameter("app_key", APP_KEY);

        // Add optional filters if provided
        if (diet != null) {
            for (String d : diet) {
                urlBuilder.addQueryParameter("diet", d);
            }
        }
        if (health != null) {
            for (String h : health) {
                urlBuilder.addQueryParameter("health", h);
            }
        }
        if (cuisineType != null && cuisineType.length != 0) {
            for (String c : cuisineType) {
                urlBuilder.addQueryParameter("cuisineType", c);
            }
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
}
