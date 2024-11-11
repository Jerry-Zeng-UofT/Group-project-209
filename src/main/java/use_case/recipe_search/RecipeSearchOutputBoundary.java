package use_case.recipe_search;

import entity.Recipe;
import java.util.List;

/**
 * Output boundary for the recipe search use case.
 */
public interface RecipeSearchOutputBoundary {
    /**
     * Present the recipes to the user.
     * @param recipes List of recipes to present
     */
    void presentRecipes(List<Recipe> recipes);

    /**
     * Present an error message to the user.
     * @param error Error message to present
     */
    void presentError(String error);
}
