package use_case.nutrition_analysis;

import data_access.RecipeSearchEdamam;
import use_case.recipe_search.RecipeSearchException;
import entity.Nutrient;

import java.util.List;

/**
 * Implementation of the Nutrition Analysis use case.
 */
public class NutritionAnalysisImpl implements NutritionAnalysis {

    private final RecipeSearchEdamam recipeSearchEdamam;
    private final NutritionAnalysisOutputBoundary outputBoundary;

    public NutritionAnalysisImpl(RecipeSearchEdamam recipeSearchEdamam,
                                 NutritionAnalysisOutputBoundary outputBoundary) {
        this.recipeSearchEdamam = recipeSearchEdamam;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void analyzeNutrition(String RecipeName, List<String> ingredients) throws NutritionAnalysisException {
        try {
            // Get NutritionInfo from Edamam API by the RecipeName.
            List<Nutrient> nutritionInfo = recipeSearchEdamam.analyzeNutrition(RecipeName, ingredients);

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