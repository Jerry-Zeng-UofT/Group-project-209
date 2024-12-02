package use_case.meal_planning;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import data_access.MealPlanningDataAccess;
import data_access.SavedRecipesDataAccess;
import entity.MealPlanEntry;
import entity.Recipe;

/**
 * Implements the MealPlanning interface to handle meal planning business logic.
 * Coordinates between data access layer and output boundary for meal planning operations.
 */
public class MealPlanningInteractor implements MealPlanning {
    private final MealPlanningDataAccess dataAccess;
    private final SavedRecipesDataAccess savedRecipesDataAccess;
    private final MealPlanningOutputBoundary outputBoundary;

    /**
     * Constructs a new MealPlanningInteractor.
     *
     * @param dataAccess data access for meal planning operations
     * @param savedRecipesDataAccess data access for saved recipes
     * @param outputBoundary output boundary for presenting results
     */
    public MealPlanningInteractor(MealPlanningDataAccess dataAccess,
                                  SavedRecipesDataAccess savedRecipesDataAccess,
                                  MealPlanningOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.savedRecipesDataAccess = savedRecipesDataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public List<Recipe> getSavedRecipes(int userId) {
        List<Recipe> recipes = List.of();
        try {
            recipes = savedRecipesDataAccess.getSavedRecipes(userId);
            outputBoundary.presentSavedRecipes(recipes);
        }
        catch (IllegalArgumentException exception) {
            outputBoundary.presentError("Failed to load saved recipes: " + exception.getMessage());
        }
        return recipes;
    }

    @Override
    public void updateMealStatus(int userId, int entryId, String status) {
        try {
            dataAccess.updateMealStatus(userId, entryId, status);
            outputBoundary.presentStatusUpdateSuccess("Meal status updated");
        }
        catch (IllegalArgumentException exception) {
            outputBoundary.presentError("Failed to update meal status: " + exception.getMessage());
        }
    }

    @Override
    public void addToCalendar(int userId, int recipeId, LocalDate date, String mealType) {
        try {
            dataAccess.addMealPlanEntry(userId, recipeId, date, mealType);
            outputBoundary.presentAddSuccess("Recipe added to calendar");
        }
        catch (IllegalArgumentException exception) {
            outputBoundary.presentError("Failed to add recipe to calendar: " + exception.getMessage());
        }
    }

    @Override
    public void removeFromCalendar(int userId, int mealPlanEntryId) {
        try {
            dataAccess.removeMealPlanEntry(userId, mealPlanEntryId);
            outputBoundary.presentRemoveSuccess("Recipe removed from calendar");
        }
        catch (IllegalArgumentException exception) {
            outputBoundary.presentError("Failed to remove recipe from calendar: " + exception.getMessage());
        }
    }

    @Override
    public void getCalendarWeek(int userId, LocalDate weekStart) {
        try {
            final List<MealPlanEntry> entries = dataAccess.getWeeklyPlan(userId, weekStart);
            outputBoundary.presentCalendarWeek(entries);
        }
        catch (IllegalArgumentException exception) {
            outputBoundary.presentError("Failed to load calendar: " + exception.getMessage());
        }
    }

    @Override
    public void initializeMealPlanning(int userId) {
        try {
            final LocalDate currentWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);
            final List<MealPlanEntry> entries = dataAccess.getWeeklyPlan(userId, currentWeekStart);
            outputBoundary.presentCalendarWeek(entries);
        }
        catch (IllegalArgumentException exception) {
            outputBoundary.presentError("Failed to initialize meal planning: " + exception.getMessage());
        }
    }
}
