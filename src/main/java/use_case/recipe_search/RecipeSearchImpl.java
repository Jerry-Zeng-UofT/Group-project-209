package use_case.recipe_search;

import entity.Recipe;
import entity.RecipeForSearch;
import entity.Ingredient;
import entity.Nutrition;
import data_access.RecipeSearchEdamam;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of the recipe search use case.
 */
public class RecipeSearchImpl implements RecipeSearch {
    private final RecipeSearchEdamam recipeSearchEdamam;
    private final RecipeSearchOutputBoundary outputBoundary;

    public RecipeSearchImpl(RecipeSearchEdamam recipeSearchEdamam,
                            RecipeSearchOutputBoundary outputBoundary) {
        this.recipeSearchEdamam = recipeSearchEdamam;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void searchRecipes(List<String> ingredients) throws RecipeSearchException {
        try {
            // Join ingredients with commas for the API search
            String searchQuery = String.join(",", ingredients);

            // Get recipes from Edamam API
            List<RecipeForSearch> searchResults = recipeSearchEdamam.searchRecipesByFoodName(searchQuery);

            // Convert API results to Recipe entities
            List<Recipe> recipes = convertToRecipes(searchResults);

            // Present success
            outputBoundary.presentRecipes(recipes);
        }
        catch (Exception e) {
            // Present error
            outputBoundary.presentError("Failed to search recipes: " + e.getMessage());
            throw new RecipeSearchException("Recipe search failed", e);
        }
    }

    private List<Recipe> convertToRecipes(List<RecipeForSearch> searchResults) {
        List<Recipe> recipes = new ArrayList<>();
        int recipeId = 1;

        for (RecipeForSearch result : searchResults) {
            // Convert ingredients to Ingredient entities
            List<Ingredient> ingredients = new ArrayList<>();
            int ingredientId = 1;
            for (String ingredientStr : result.getIngredients()) {
                ingredients.add(new Ingredient(
                        ingredientId++,
                        ingredientStr,
                        0.0,  // Default quantity
                        ""    // Default unit
                ));
            }

            // Create Recipe entity
            Recipe recipe = new Recipe(
                    recipeId++,
                    result.getTitle(),
                    ingredients,
                    result.getInstructions(),
                    new Nutrition(0, 0, 0, 0, 0, 0),  // Default nutrition
                    new ArrayList<>()  // Empty food list
            );

            recipes.add(recipe);
        }

        return recipes;
    }
}