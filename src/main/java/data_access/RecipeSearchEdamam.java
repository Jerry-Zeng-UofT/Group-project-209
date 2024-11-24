package data_access;

import entity.*;

import entity.RecipeForSearch;

import java.util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.Nutrient;
import okhttp3.*;
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
                    RecipeForSearch recipeForSearch = new RecipeForSearch(name, description, ingredients, ingredientsArray);
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
                    RecipeForSearch recipeForSearch = new RecipeForSearch(name, description, ingredients, ingredientsArray);
                    recipes.add(recipeForSearch);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return recipes;
    }

    /**
     * Retrieve detailed recipe information by recipe ID.
     * Needed for meal planning to get full recipe details when adding to calendar.
     */

    public Recipe getRecipeById(String recipeId) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("type", "public");
        urlBuilder.addQueryParameter("id", recipeId);
        urlBuilder.addQueryParameter("app_id", APP_ID);
        urlBuilder.addQueryParameter("app_key", APP_KEY);

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject recipeJson = jsonObject.getJSONObject("recipe");

                // Extract basic info
                String name = recipeJson.getString("label");
                String instructions = recipeJson.optString("url", "Instructions not available");

                // Extract ingredients
                List<Ingredient> ingredients = new ArrayList<>();
                JSONArray ingredientsArray = recipeJson.getJSONArray("ingredients");
                for (int i = 0; i < ingredientsArray.length(); i++) {
                    JSONObject ingredientJson = ingredientsArray.getJSONObject(i);
                    int ingredientId = i + 1;
                    String ingredientName = ingredientJson.getString("food");
                    double quantity = ingredientJson.optDouble("quantity", 0.0);
                    String unit = ingredientJson.optString("measure", "");

                    ingredients.add(new Ingredient(ingredientId, ingredientName, quantity, unit));
                }

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
                        new ArrayList<>(),  // Food list
                        new JSONArray()     // created only to fit the attributes in Recipe
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to get recipe details: " + e.getMessage());
        }
        return null;
    }
}
