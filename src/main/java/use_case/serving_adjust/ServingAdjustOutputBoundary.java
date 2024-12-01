package use_case.serving_adjust;

import entity.Recipe;

import java.util.List;

/**
 * Interface for presenting the output of serving adjustments.
 */
public interface ServingAdjustOutputBoundary {
    /**
     * Present the updated recipes after servings are adjusted.
     *
     * @param updatedRecipes The list of updated recipes.
     */
    void presentUpdatedRecipes(List<Recipe> updatedRecipes);
}