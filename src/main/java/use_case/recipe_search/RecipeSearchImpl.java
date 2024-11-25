package use_case.recipe_search;

import data_access.SavedRecipesDataAccess;
import entity.Recipe;
import entity.Ingredient;
import entity.Nutrition;
import entity.Food;
import data_access.RecipeSearchEdamam;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class RecipeSearchImpl implements RecipeSearch {
    private final RecipeSearchEdamam recipeSearchEdamam;
    private final RecipeSearchOutputBoundary outputBoundary;
    private final SavedRecipesDataAccess savedRecipesDataAccess;

    public RecipeSearchImpl(RecipeSearchEdamam recipeSearchEdamam,
                            SavedRecipesDataAccess savedRecipesDataAccess,
                            RecipeSearchOutputBoundary outputBoundary) {
        this.recipeSearchEdamam = recipeSearchEdamam;
        this.savedRecipesDataAccess = savedRecipesDataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void saveRecipe(int userId, Recipe recipe) throws RecipeSearchException {
        try {
            savedRecipesDataAccess.saveRecipe(userId, recipe);
            outputBoundary.presentSaveSuccess(recipe);
        } catch (Exception e) {
            outputBoundary.presentError("Failed to save recipe: " + e.getMessage());
            throw new RecipeSearchException("Failed to save recipe", e);
        }
    }

    @Override
    public void searchRecipes(List<String> ingredients) throws RecipeSearchException {
        try {
            String searchQuery = String.join(",", ingredients);
            List<Recipe> searchResults = recipeSearchEdamam.searchRecipesByFoodName(searchQuery);
            List<Recipe> recipes = convertToRecipes(searchResults);
            outputBoundary.presentRecipes(recipes);
        } catch (Exception e) {
            outputBoundary.presentError("Failed to search recipes: " + e.getMessage());
            throw new RecipeSearchException("Recipe search failed", e);
        }
    }

    @Override
    public void searchRestrictionRecipes(Map<String, List<String>> restrictions) throws RecipeSearchException {
        try {
            String searchFoodQuery = String.join(",", restrictions.get("Food Name"));
            String searchDietQuery = String.join(",", restrictions.get("Diet Label"));
            String searchHealthQuery = String.join(",", restrictions.get("Health Label"));
            String searchCuisineQuery = String.join(",", restrictions.get("Cuisine Type"));
            List<Recipe> searchResults = recipeSearchEdamam.searchRecipesByRestriction(
                    searchFoodQuery, searchDietQuery, searchHealthQuery, searchCuisineQuery);
            List<Recipe> recipes = convertToRecipes(searchResults);
            outputBoundary.presentRecipes(recipes);
        } catch (Exception e) {
            outputBoundary.presentError("Failed to search recipes: " + e.getMessage());
            throw new RecipeSearchException("Recipe search failed", e);
        }
    }

    @Override
    public void adjustRecipeServings(int newServings, Recipe recipe) {
        Recipe adjustedRecipe = recipe.adjustServings(newServings);
        outputBoundary.presentRecipes(List.of(adjustedRecipe));
    }

    private List<Recipe> convertToRecipes(List<Recipe> searchResults) {
        List<Recipe> recipes = new ArrayList<>();
        int recipeId = 1;

        for (Recipe result : searchResults) {
            String title = result.getTitle();
            String description = result.getDescription() != null ? result.getDescription() : "No description available";
            List<Ingredient> ingredients = result.getIngredients();
            String instructions = result.getInstructions() != null ? result.getInstructions() : "Instructions not available";
            Nutrition nutrition = result.getNutrition() != null ? result.getNutrition() : new Nutrition(0, 0, 0, 0, 0, 0);
            List<Food> food = result.getFood() != null ? result.getFood() : new ArrayList<>();
            int servings = result.getServings() > 0 ? result.getServings() : 1;

            Recipe recipe = new Recipe(
                    recipeId++,
                    title,
                    description,
                    ingredients,
                    instructions,
                    nutrition,
                    food,
                    result.getJsonIngredient(),
                    servings
            );

            recipes.add(recipe);
        }

        return recipes;
    }
}