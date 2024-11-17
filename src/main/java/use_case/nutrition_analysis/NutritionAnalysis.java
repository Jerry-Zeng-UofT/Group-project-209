package use_case.nutrition_analysis;

/**
 * Interface for the Nutrition Analysis use case.
 */
public interface NutritionAnalysis {

    /**
     * Searches for recipes based on a list of ingredients.
     *
     * @param RecipeName the title of recipe to analyze.
     * @throws NutritionAnalysisException if search fails
     */
    void analyzeNutrition(String RecipeName) throws NutritionAnalysisException;
}
