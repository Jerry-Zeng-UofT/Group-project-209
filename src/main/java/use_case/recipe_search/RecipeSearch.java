package use_case.recipe_search;

import entity.Recipe;

import java.util.List;
import java.util.Map;

/**
 * Interface for the recipe search use case.
 */
public interface RecipeSearch {
    /**
     * Searches for recipes based on a list of ingredients.
     * @param ingredients List of ingredients to search with
     * @throws RecipeSearchException if search fails
     */
    void searchRecipes(List<String> ingredients) throws RecipeSearchException;

    /**
     * Searches for recipes based on a map of restrictions.
     * @param restrictions Map of restrictions to search with
     * @throws RecipeSearchException if search fails
     */
    void searchRestrictionRecipes(Map<String, List<String>> restrictions) throws RecipeSearchException;

    void saveRecipe(int userId, Recipe recipe) throws RecipeSearchException;

    void adjustRecipeServings(int newServings, Recipe recipe);
}