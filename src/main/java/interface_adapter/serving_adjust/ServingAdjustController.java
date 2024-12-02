package interface_adapter.serving_adjust;

import java.util.List;

import entity.Recipe;
import use_case.serving_adjust.ServingAdjustInputBoundary;

/**
 * Controller class for managing serving adjustments for recipes.
 */
public class ServingAdjustController {

    private final ServingAdjustInputBoundary servingAdjust;

    /**
     * Constructs with the specified serving adjustment use case.
     * @param servingAdjust The input boundary for serving adjustment use case
     */
    public ServingAdjustController(ServingAdjustInputBoundary servingAdjust) {
        this.servingAdjust = servingAdjust;
    }

    /**
     * Updates the servings for a list of recipes by delegating the operation to the serving adjustment use case.
     * @param newServings The new number of servings to apply
     * @param recipes     The list of recipes to update
     */
    public void updateServingsForAll(int newServings, List<Recipe> recipes) {
        servingAdjust.adjustServings(newServings, recipes);
    }
}
