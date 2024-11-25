package interface_adapter.nutrition_analysis;

import entity.Nutrient;
import use_case.nutrition_analysis.NutritionAnalysis;
import use_case.nutrition_analysis.NutritionAnalysisOutputBoundary;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the Nutrition Analysis functionality.
 */
public class NutritionAnalysisPresenter implements NutritionAnalysisOutputBoundary {
    private final NutritionAnalysisViewModel viewModel;

    public NutritionAnalysisPresenter(NutritionAnalysisViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentNutritionInfo(List<Nutrient> NutritionInfo) {
        NutritionAnalysisState state = new NutritionAnalysisState();
        List<String> nutritionResults = new ArrayList<>();

        for (Nutrient nutrient : NutritionInfo) {
            StringBuilder description = new StringBuilder();
            description.append(nutrient.getNutrients());
            nutritionResults.add(description.toString());
        }
        state.setNutritionResults(nutritionResults);
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
