package interface_adapter.nutrition_analysis;

import use_case.nutrition_analysis.NutritionAnalysis;

/**
 * Controller for the Nutrition Analysis functionality.
 */
public class NutritionAnalysisController {
    private final NutritionAnalysis nutritionAnalysisUseCase;

    public NutritionAnalysisController(NutritionAnalysis nutritionAnalysisUsecase) {
        this.nutritionAnalysisUseCase = nutritionAnalysisUsecase;
    }

    /**
     * Execute a nutrition analysis with the given recipe name.
     * @param RecipeName the name of the recipe the user wants to analyze.
     */
    public void executeAnalysis(String RecipeName) {
        try {
            nutritionAnalysisUseCase.analyzeNutrition(RecipeName);
        }
        catch (Exception e) {
            // Error will be handled by the presenter through output boundary
        }
    }
}
