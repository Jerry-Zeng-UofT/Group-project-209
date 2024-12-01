package use_case.recipe_search;

import entity.Recipe;
import java.util.List;

/**
 * Interface for the recipe search use case.
 */
public interface RecipeSearchInputBoundary {
    /**
     * Searches for recipes based on a list of ingredients.
     * @param ingredients List of ingredients to search with
     * @throws RecipeSearchException if search fails
     */
    void searchRecipes(List<String> ingredients) throws RecipeSearchException;

    void saveRecipe(int userId, Recipe recipe) throws RecipeSearchException;

    void adjustRecipeServings(int newServings, Recipe recipe);
}