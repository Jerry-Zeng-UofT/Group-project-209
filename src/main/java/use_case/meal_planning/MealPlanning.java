package use_case.meal_planning;

import java.time.LocalDate;
import java.util.List;
import entity.Recipe;

public interface MealPlanning {
    void addToCalendar(int userId, int recipeId, LocalDate date, String mealType);
    void removeFromCalendar(int userId, int mealPlanEntryId);
    void getCalendarWeek(int userId, LocalDate weekStart);
    List<Recipe> getSavedRecipes(int userId);  // Added to get user's saved recipes
    void updateMealStatus(int userId, int entryId, String status);  // Added to update meal status
}