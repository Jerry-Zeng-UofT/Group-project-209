package use_case.recipe_search;

import java.util.ArrayList;
import java.util.List;

import data_access.RecipeSearchDataAccessObject;
import data_access.SavedRecipesDataAccess;
import entity.Food;
import entity.Ingredient;
import entity.Nutrition;
import entity.Recipe;

public class RecipeSearchInteractor implements RecipeSearchInputBoundary {
    private final RecipeSearchDataAccessObject recipeSearchDataAccessObject;
    private final RecipeSearchOutputBoundary outputBoundary;
    private final SavedRecipesDataAccess savedRecipesDataAccess;

    public RecipeSearchInteractor(RecipeSearchDataAccessObject recipeSearchDataAccessObject,
                                  SavedRecipesDataAccess savedRecipesDataAccess,
                                  RecipeSearchOutputBoundary outputBoundary) {
        this.recipeSearchDataAccessObject = recipeSearchDataAccessObject;
        this.savedRecipesDataAccess = savedRecipesDataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void saveRecipe(int userId, Recipe recipe) throws RecipeSearchException {
        try {
            savedRecipesDataAccess.saveRecipe(userId, recipe);
            outputBoundary.presentSaveSuccess(recipe);
        } catch (Exception exception) {
            outputBoundary.presentError("Failed to save recipe: " + exception.getMessage());
            throw new RecipeSearchException("Failed to save recipe", exception);
        }
    }

    @Override
    public void searchRecipes(List<String> ingredients) throws RecipeSearchException {
        try {
            final String searchQuery = String.join(",", ingredients);
            final List<Recipe> searchResults = recipeSearchDataAccessObject.searchRecipesByFoodName(searchQuery);
            final List<Recipe> recipes = convertToRecipes(searchResults);
            outputBoundary.presentRecipes(recipes);
        }
        catch (Exception exception) {
            outputBoundary.presentError("Failed to search recipes: " + exception.getMessage());
            throw new RecipeSearchException("Recipe search failed", exception);
        }
    }

    @Override
    public void adjustRecipeServings(int newServings, Recipe recipe) {
        if (recipe == null || newServings <= 0) {
            throw new IllegalArgumentException("Invalid recipe or servings");
        }

        final int currentServings = recipe.getServings();
        if (currentServings <= 0) {
            throw new IllegalStateException("Current servings must be greater than zero");
        }

        final double adjustmentFactor = (double) newServings / currentServings;

        for (Ingredient ingredient : recipe.getIngredients()) {
            final double newQuantity = ingredient.getQuantity() * adjustmentFactor;
            ingredient.setQuantity(newQuantity);
        }

        recipe.setServings(newServings);
    }

    private List<Recipe> convertToRecipes(List<Recipe> searchResults) {
        final List<Recipe> recipes = new ArrayList<>();
        int recipeId = 1;

        for (Recipe result : searchResults) {
            final String description = defaultIfNull(result.getDescription(), "No description available");
            final String instructions = defaultIfNull(result.getInstructions(), "Instructions not available");
            final Nutrition nutrition = defaultIfNull(result.getNutrition(), new Nutrition(0, 0, 0, 0, 0, 0));
            final List<Food> food = defaultIfNull(result.getFood(), new ArrayList<>());
            final int servings = Math.max(result.getServings(), 1);

            final Recipe recipe = new Recipe(
                    recipeId++,
                    result.getTitle(),
                    description,
                    result.getIngredients(),
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

    private <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
