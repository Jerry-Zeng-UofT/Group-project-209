package use_case.serving_adjust;

import java.util.List;

import entity.Recipe;

/**
 * Interface for serving adjustment use case.
 */
public interface ServingAdjust {
    /**
     * Adjust the servings for a single recipe.
     *
     * @param newServings New number of servings
     * @param recipe The recipe to adjust
     */
    void adjustServings(int newServings, Recipe recipe);

    /**
     * Adjust the servings for multiple recipes.
     *
     * @param newServings New number of servings
     * @param recipes List of recipes to adjust
     */
    void adjustServingsForMultiple(int newServings, List<Recipe> recipes);
}
