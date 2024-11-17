package use_case.nutrition_analysis;

import entity.Nutrient;

import java.util.List;

public interface NutritionAnalysisoutputBoundary {
    /**
     * Present the Nutrition information to the user.
     * @param NutritionInfo List of recipes to present
     */
    void presentNutritionInfo(List<Nutrients> NutritionInfo);

    /**
     * Present an error message to the user.
     * @param error Error message to present
     */
    void presentError(String error);
}