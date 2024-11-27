package use_case.meal_planning;

import entity.MealPlanEntry;
import entity.Recipe;
import java.util.List;

public interface MealPlanningOutputBoundary {
    void presentCalendarWeek(List<MealPlanEntry> entries);
    void presentAddSuccess(String message);
    void presentRemoveSuccess(String message);
    void presentStatusUpdateSuccess(String message);
    void presentError(String error);
    void presentSavedRecipes(List<Recipe> recipes);
}
