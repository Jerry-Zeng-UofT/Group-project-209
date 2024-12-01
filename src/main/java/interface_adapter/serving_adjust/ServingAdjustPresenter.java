package interface_adapter.serving_adjust;

import entity.Recipe;
import use_case.serving_adjust.ServingAdjustOutputBoundary;

import java.util.List;

public class ServingAdjustPresenter implements ServingAdjustOutputBoundary {
    private final ServingAdjustViewModel viewModel;

    public ServingAdjustPresenter(ServingAdjustViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentUpdatedRecipes(List<Recipe> updatedRecipes) {
        try {
            viewModel.updateRecipes(updatedRecipes);
        }
        catch (Exception e) {
            viewModel.setError("Failed to update recipes: " + e.getMessage());
        }
    }
}