package interface_adapter.nutrition_analysis;

import entity.Recipe;
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
     * @param recipe the recipe the user wants to analyze.
     */
    public void executeAnalysis(Recipe recipe) {
        nutritionAnalysisUseCase.analyzeNutrition(recipe);
    }
}

