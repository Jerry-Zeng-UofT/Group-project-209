package use_case.serving_adjust;

import java.util.List;

import entity.Recipe;

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
