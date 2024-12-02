package use_case.nutrition_analysis;

import data_access.NutritionAnalysisDataAccess;
import data_access.NutritionAnalysisDataAccessObject;
import entity.Recipe;
import use_case.recipe_search.RecipeSearchException;
import entity.Nutrient;

import java.util.List;

/**
 * Implementation of the Nutrition Analysis use case.
 */
public class NutritionAnalysisImpl implements NutritionAnalysis {

    private final NutritionAnalysisDataAccess NutritionAnalysisDAO;
    private final NutritionAnalysisOutputBoundary outputBoundary;

    public NutritionAnalysisImpl(NutritionAnalysisDataAccess NutritionAnalysisDAO,
                                 NutritionAnalysisOutputBoundary outputBoundary) {
        this.NutritionAnalysisDAO = NutritionAnalysisDAO;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void analyzeNutrition(Recipe recipe) throws NutritionAnalysisException {
        try {
            // Get NutritionInfo from Edamam API by the RecipeName.
            List<Nutrient> nutritionInfo = NutritionAnalysisDAO.analyzeNutrition(recipe);

            // Present success
            outputBoundary.presentNutritionInfo(nutritionInfo);
        }
        catch (Exception e) {
            // Present error
            outputBoundary.presentError("Failed to analyze the recipe: " + e.getMessage());
            throw new RecipeSearchException("Analysis failed", e);
        }
    }
}