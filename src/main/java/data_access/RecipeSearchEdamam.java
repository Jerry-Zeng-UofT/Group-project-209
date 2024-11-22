package data_access;

import entity.*;

import entity.RecipeForSearch;

import java.util.*;

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
     * Get recipes by food name.
     */
    public List<RecipeForSearch> searchRecipesByFoodName(String foodName) {
        return fetchRecipes(foodName, null, null, null);
    }

    /**
     * Get recipes by restrictions.
     */
    public List<RecipeForSearch> searchRecipesByRestriction(String foodName, String diet, String health, String
            cuisineType) {
        return fetchRecipes(foodName, diet, health, cuisineType);
    }

    /**
     * Fetch recipes from Edamam API.
     */
    private List<RecipeForSearch> fetchRecipes(String foodName, String diet, String health, String cuisineType) {
        List<RecipeForSearch> recipes = new ArrayList<>();

        // Build API URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("type", "public");
        urlBuilder.addQueryParameter("q", foodName);
        urlBuilder.addQueryParameter("app_id", APP_ID);
        urlBuilder.addQueryParameter("app_key", APP_KEY);

        // Add optional filters
        if (diet != null && !diet.isEmpty()) urlBuilder.addQueryParameter("diet", diet);
        if (health != null && !health.isEmpty()) urlBuilder.addQueryParameter("health", health);
        if (cuisineType != null && !cuisineType.isEmpty()) urlBuilder.addQueryParameter("cuisineType", cuisineType);

        Request request = new Request.Builder().url(urlBuilder.build().toString()).build();

        // API Call
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray hitsArray = jsonObject.getJSONArray("hits");

                for (int i = 0; i < hitsArray.length(); i++) {
                    JSONObject recipeJson = hitsArray.getJSONObject(i).getJSONObject("recipe");

                    // Extract details
                    String name = recipeJson.getString("label");
                    String description = recipeJson.optString("source", "No description available");
                    List<Ingredient> ingredients = extractIngredients(recipeJson.getJSONArray("ingredients"));

                    // Create RecipeForSearch
                    RecipeForSearch recipeForSearch = new RecipeForSearch(name, description, ingredients);
                    recipes.add(recipeForSearch);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch recipes: " + e.getMessage());
        }
        return recipes;
    }

    /**
     * Extract detailed ingredients from a JSON array.
     */
    private List<Ingredient> extractIngredients(JSONArray ingredientsArray) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < ingredientsArray.length(); i++) {
            JSONObject ingredientJson = ingredientsArray.getJSONObject(i);

            String food = ingredientJson.getString("food");
            double quantity = ingredientJson.optDouble("quantity", 0.0);
            String unit = ingredientJson.optString("measure", "");

            ingredients.add(new Ingredient(i + 1, food, quantity, unit));
        }
        return ingredients;
    }

    /**
     * Retrieve detailed recipe information by recipe ID.
     */
    public Recipe getRecipeById(String recipeId) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("type", "public");
        urlBuilder.addQueryParameter("id", recipeId);
        urlBuilder.addQueryParameter("app_id", APP_ID);
        urlBuilder.addQueryParameter("app_key", APP_KEY);

        Request request = new Request.Builder().url(urlBuilder.build().toString()).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject recipeJson = jsonObject.getJSONObject("recipe");

                // Extract details
                String name = recipeJson.getString("label");
                String instructions = recipeJson.optString("url", "Instructions not available");
                List<Ingredient> ingredients = extractIngredients(recipeJson.getJSONArray("ingredients"));

                // Extract nutrition
                JSONObject nutritionJson = recipeJson.getJSONObject("totalNutrients");
                Nutrition nutrition = new Nutrition(
                        nutritionJson.getJSONObject("ENERC_KCAL").getDouble("quantity"),
                        nutritionJson.getJSONObject("PROCNT").getDouble("quantity"),
                        nutritionJson.getJSONObject("FAT").getDouble("quantity"),
                        nutritionJson.getJSONObject("CHOCDF").getDouble("quantity"),
                        nutritionJson.getJSONObject("FIBTG").getDouble("quantity"),
                        nutritionJson.getJSONObject("SUGAR").getDouble("quantity")
                );

                return new Recipe(
                        Integer.parseInt(recipeId),
                        name,
                        ingredients,
                        instructions,
                        nutrition,
                        new ArrayList<>()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return null;
    }
}
