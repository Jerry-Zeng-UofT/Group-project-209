package interface_adapter.serving_adjust;

import use_case.serving_adjust.ServingAdjustInputBoundary;
import entity.Recipe;

import java.util.List;

public class ServingAdjustController {
    private final ServingAdjustInputBoundary servingAdjust;

    public ServingAdjustController(ServingAdjustInputBoundary servingAdjust) {
        this.servingAdjust = servingAdjust;
    }

    /**
     * Updates the servings for a list of recipes.
     *
     * @param newServings The new number of servings.
     * @param recipes     The list of recipes to update.
     */
    public void updateServingsForAll(int newServings, List<Recipe> recipes) {
        servingAdjust.adjustServings(newServings, recipes);
    }
}
