package interface_adapter.meal_planning;

import entity.MealPlanEntry;
import entity.Recipe;
import java.time.LocalDate;
import java.util.List;

public interface MealPlanningStateInterface {
    List<MealPlanEntry> getMealPlanEntries();
    void setMealPlanEntries(List<MealPlanEntry> entries);
    List<Recipe> getSavedRecipes();
    void setSavedRecipes(List<Recipe> recipes);
    LocalDate getCurrentWeekStart();
    void setCurrentWeekStart(LocalDate date);
    String getMessage();
    void setMessage(String message);
    String getError();
    void setError(String error);
}
