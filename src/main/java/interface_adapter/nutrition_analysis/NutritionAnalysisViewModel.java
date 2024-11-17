package interface_adapter.nutrition_analysis;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the NutritionAnalysis View.
 */
public class NutritionAnalysisViewModel extends ViewModel {
    // Attributes are created base on the components in RecipeSearchView class
    private static final String TITLE_LABEL = "Nutrition Analysis";
    private static final String REMOVE_INGREDIENT_BUTTON_LABEL = "Remove All";

    public NutritionAnalysisViewModel(String viewName) {
        super(viewName);
    }
}
