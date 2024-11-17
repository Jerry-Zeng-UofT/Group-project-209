package interface_adapter.nutrition_analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state for Nutrition Analysis functionality.
 */
public class NutritionAnalysisState {
    private String recipeName;
    private List<String> nutritionResults = new ArrayList<>();
    private String error;

    /**
     * Copy constructor for NutritionAnalysisState.
     * @param copy The state to copy from
     */
    public NutritionAnalysisState(NutritionAnalysisState copy) {
        this.recipeName = copy.getRecipeName();
        this.nutritionResults = copy.getNutritionResults();
        this.error = copy.getError();
    }

    public String getRecipeName() {
        return recipeName;
    }

    public List<String> getNutritionResults() {
        return nutritionResults;
    }

    public String getError() {
        return error;
    }

    public void setNutritionResults(List<String> nutritionResults) {
        this.nutritionResults = nutritionResults;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setError(String error) {
        this.error = error;
    }
}
