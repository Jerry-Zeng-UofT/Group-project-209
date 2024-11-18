package data_access;

import entity.MealPlanEntry;
import java.time.LocalDate;
import java.util.List;

public interface MealPlanningDataAccess {
    void addMealPlanEntry(int userId, int recipeId, LocalDate date, String mealType);
    void removeMealPlanEntry(int userId, int mealPlanEntryId);
    void updateMealStatus(int userId, int entryId, String status);

    MealPlanEntry getMealPlanEntry(int userId, int entryId);
    List<MealPlanEntry> getWeeklyPlan(int userId, LocalDate weekStart);
}
