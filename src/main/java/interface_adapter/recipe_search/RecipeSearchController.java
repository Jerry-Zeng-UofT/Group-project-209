package interface_adapter.recipe_search;

import entity.Recipe;
import use_case.recipe_search.RecipeSearch;
import java.util.List;
import java.util.Map;

/**
 * Controller for the recipe search functionality.
 */
public class RecipeSearchController {
    private final RecipeSearch recipeSearchUseCase;

    public RecipeSearchController(RecipeSearch recipeSearchUseCase) {
        this.recipeSearchUseCase = recipeSearchUseCase;
    }

    /**
     * Execute a recipe search with the given ingredients.
     * @param ingredients List of ingredients to search for
     */
    public void executeSearch(List<String> ingredients) {
        try {
            recipeSearchUseCase.searchRecipes(ingredients);
        }
        catch (Exception e) {
            // Error will be handled by the presenter through output boundary
        }
    }

    /**
     * Execute a recipe search with the given restrictions.
     * @param ingredients Map of ingredients to search for
     */
    public void executeRestrictionSearch(Map<String, List<String>> ingredients) {
        try {
            recipeSearchUseCase.searchRestrictionRecipes(ingredients);
        }
        catch (Exception e) {
            // Error will be handled by the presenter through output boundary
        }
    }

    public void saveRecipe(int userId, Recipe recipe) {
        try {
            recipeSearchUseCase.saveRecipe(userId, recipe);
        }
        catch (Exception e) {
            // Error will be handled by the presenter
        }
    }

    /**
     * Adjust servings for a recipe.
     * @param newServings The new serving size.
     * @param selectedRecipe The selected recipe to adjust.
     */
    public void adjustServings(int newServings, Recipe selectedRecipe) {
        try {
            recipeSearchUseCase.adjustRecipeServings(newServings, selectedRecipe);
        } catch (Exception e) {
            // Error will be handled by the presenter through output boundary
        }
    }
}