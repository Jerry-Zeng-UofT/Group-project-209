package use_case.nutrition_analysis;

import entity.Recipe;

import java.util.List;

/**
 * Interface for the Nutrition Analysis use case.
 */
public interface NutritionAnalysis {

    /**
     * Searches for recipes based on a list of ingredients.
     *
     * @param RecipeName the title of recipe to analyze.
     * @param ingredients the list of ingredients of this recipe.
     * @throws NutritionAnalysisException if search fails
     */
    void analyzeNutrition(Recipe recipe) throws NutritionAnalysisException;
}
