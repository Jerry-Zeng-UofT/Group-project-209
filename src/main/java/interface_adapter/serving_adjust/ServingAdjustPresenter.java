package interface_adapter.serving_adjust;

import java.util.List;

import entity.Recipe;
import use_case.serving_adjust.ServingAdjustOutputBoundary;

/**
 * Presenter class for serving adjustment functionality.
 */
public class ServingAdjustPresenter implements ServingAdjustOutputBoundary {

    private final ServingAdjustViewModel viewModel;

    /**
     * Constructs ServingAdjustPresenter with the specified view model.
     *
     * @param viewModel The view model to update with the serving adjustment data
     */
    public ServingAdjustPresenter(ServingAdjustViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Updates the view model with the list of recipes that have been adjusted for servings.
     *
     * @param updatedRecipes The list of recipes with adjusted servings
     */
    @Override
    public void presentUpdatedRecipes(List<Recipe> updatedRecipes) {
        try {
            viewModel.updateRecipes(updatedRecipes);
        }
        catch (IllegalArgumentException exIllegalArgument) {
            viewModel.setError("Invalid argument while updating recipes: " + exIllegalArgument.getMessage());
        }
        catch (IllegalStateException exIllegalState) {
            viewModel.setError("Illegal state encountered while updating recipes: " + exIllegalState.getMessage());
        }
    }
}
