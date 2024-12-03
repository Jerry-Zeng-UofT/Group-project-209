package use_case.nutrition_analysis;

import java.util.List;

import data_access.NutritionAnalysisDataAccess;
import entity.Nutrient;
import entity.Recipe;
import use_case.recipe_search.RecipeSearchException;

/**
 * Implementation of the Nutrition Analysis use case.
 */
public class NutritionAnalysisImpl implements NutritionAnalysis {

    private final NutritionAnalysisDataAccess nutritionAnalysisDAO;
    private final NutritionAnalysisOutputBoundary outputBoundary;

    public NutritionAnalysisImpl(NutritionAnalysisDataAccess NutritionAnalysisDAO,
                                 NutritionAnalysisOutputBoundary outputBoundary) {
        this.nutritionAnalysisDAO = NutritionAnalysisDAO;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void analyzeNutrition(Recipe recipe) throws NutritionAnalysisException {
        try {
            // Get NutritionInfo from Edamam API by the RecipeName.
            final List<Nutrient> nutritionInfo = nutritionAnalysisDAO.analyzeNutrition(recipe);

            // Present success
            outputBoundary.presentNutritionInfo(nutritionInfo);
        }
        catch (Exception exception) {
            // Present error
            outputBoundary.presentError("Failed to analyze the recipe: " + exception.getMessage());
            throw new RecipeSearchException("Analysis failed", exception);
        }
    }
}
