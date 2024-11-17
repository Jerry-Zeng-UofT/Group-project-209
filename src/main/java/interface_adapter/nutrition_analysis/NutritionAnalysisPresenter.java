package interface_adapter.nutrition_analysis;

import entity.Nutrient;
import use_case.nutrition_analysis.NutritionAnalysisOutputBoundary;

/**
 * Presenter for the Nutrition Analysis functionality.
 */
public class NutritionAnalysisPresenter implements NutritionAnalysisOutputBoundary {
    private final NutritionAnalysisViewModel viewModel;

    public NutritionAnalysisPresenter(NutritionAnalysisViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentNutritionInfo(Nutrient NutritionInfo) {
        NutritionAnalysisState state = new NutritionAnalysisState();

        state.setNutritionResults(NutritionInfo.getNutrients());
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentError(String error) {
        final NutritionAnalysisState state = new NutritionAnalysisState();
        state.setError(error);
        viewModel.setState(state);
        viewModel.firePropertyChanged();

    }
}
