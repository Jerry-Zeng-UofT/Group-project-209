package use_case.serving_adjust;

import entity.Recipe;

import java.util.List;

/**
 * Interface for serving adjustment use case input.
 */
public interface ServingAdjustInputBoundary {
    /**
     * Adjust the servings for a list of recipes.
     *
     * @param servings The number of servings to set.
     * @param recipes  The list of recipes to update.
     */
    void adjustServings(int servings, List<Recipe> recipes);
}