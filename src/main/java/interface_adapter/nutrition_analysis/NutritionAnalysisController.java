package interface_adapter.nutrition_analysis;

import entity.Recipe;
import use_case.nutrition_analysis.NutritionAnalysis;

import java.util.List;

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
     * @param ingredient the list of ingredients of this recipe.
     */
    public void executeAnalysis(Recipe recipe) {
        try {
            nutritionAnalysisUseCase.analyzeNutrition(recipe);
        }
        catch (Exception e) {
            // Error will be handled by the presenter through output boundary
        }
    }
}
