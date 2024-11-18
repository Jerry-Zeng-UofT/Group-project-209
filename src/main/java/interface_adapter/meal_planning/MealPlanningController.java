package interface_adapter.meal_planning;

import use_case.meal_planning.MealPlanning;
import entity.Recipe;
import java.time.LocalDate;
import java.util.List;

public class MealPlanningController {
    private final MealPlanning mealPlanningUseCase;

    public MealPlanningController(MealPlanning mealPlanningUseCase) {
        this.mealPlanningUseCase = mealPlanningUseCase;
    }

    public List<Recipe> getSavedRecipes(int userId) {
        return mealPlanningUseCase.getSavedRecipes(userId);
    }

    public void updateMealStatus(int userId, int entryId, String status) {
        mealPlanningUseCase.updateMealStatus(userId, entryId, status);
    }

    public void addToCalendar(int userId, int recipeId, LocalDate date, String mealType) {
        mealPlanningUseCase.addToCalendar(userId, recipeId, date, mealType);
    }

    public void removeFromCalendar(int userId, int mealPlanEntryId) {
        mealPlanningUseCase.removeFromCalendar(userId, mealPlanEntryId);
    }

    public void viewCalendarWeek(int userId, LocalDate weekStart) {
        mealPlanningUseCase.getCalendarWeek(userId, weekStart);
    }
}
