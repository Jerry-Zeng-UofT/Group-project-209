package use_case.meal_planning;

import data_access.MealPlanningDataAccess;
import data_access.SavedRecipesDataAccess;
import entity.MealPlanEntry;
import entity.Recipe;
import java.time.LocalDate;
import java.util.List;

public class MealPlanningInteractor implements MealPlanning {
    private final MealPlanningDataAccess dataAccess;
    private final SavedRecipesDataAccess savedRecipesDataAccess;
    private final MealPlanningOutputBoundary outputBoundary;

    public MealPlanningInteractor(MealPlanningDataAccess dataAccess,
                                  SavedRecipesDataAccess savedRecipesDataAccess,
                                  MealPlanningOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.savedRecipesDataAccess = savedRecipesDataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public List<Recipe> getSavedRecipes(int userId) {
        try {
            List<Recipe> recipes = savedRecipesDataAccess.getSavedRecipes(userId);
            outputBoundary.presentSavedRecipes(recipes);
            return recipes;
        } catch (Exception e) {
            outputBoundary.presentError("Failed to load saved recipes: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public void updateMealStatus(int userId, int entryId, String status) {
        try {
            dataAccess.updateMealStatus(userId, entryId, status);
            outputBoundary.presentStatusUpdateSuccess("Meal status updated");
        } catch (Exception e) {
            outputBoundary.presentError("Failed to update meal status: " + e.getMessage());
        }
    }

    @Override
    public void addToCalendar(int userId, int recipeId, LocalDate date, String mealType) {
        try {
            dataAccess.addMealPlanEntry(userId, recipeId, date, mealType);
            outputBoundary.presentAddSuccess("Recipe added to calendar");
        } catch (Exception e) {
            outputBoundary.presentError("Failed to add recipe to calendar: " + e.getMessage());
        }
    }

    @Override
    public void removeFromCalendar(int userId, int mealPlanEntryId) {
        try {
            dataAccess.removeMealPlanEntry(userId, mealPlanEntryId);
            outputBoundary.presentRemoveSuccess("Recipe removed from calendar");
        }
        catch (Exception e) {
            outputBoundary.presentError("Failed to remove recipe from calendar: " + e.getMessage());
        }
    }

    @Override
    public void getCalendarWeek(int userId, LocalDate weekStart) {
        try {
            List<MealPlanEntry> entries = dataAccess.getWeeklyPlan(userId, weekStart);
            outputBoundary.presentCalendarWeek(entries);
        } catch (Exception e) {
            outputBoundary.presentError("Failed to load calendar: " + e.getMessage());
        }
    }

}
