package data_access;

import entity.MealPlanEntry;
import entity.Recipe;
import java.time.LocalDate;
import java.util.*;

public class MealPlanningDataAccessObject implements MealPlanningDataAccess {

    private final Map<Integer, MealPlanEntry> mealPlanEntries = new HashMap<>();
    private final SavedRecipesDataAccess savedRecipesDataAccess;
    private int nextEntryId = 1;

    public MealPlanningDataAccessObject(SavedRecipesDataAccess savedRecipesDataAccess) {
        this.savedRecipesDataAccess = savedRecipesDataAccess;
    }

    @Override
    public MealPlanEntry getMealPlanEntry(int userId, int entryId) {
        MealPlanEntry entry = mealPlanEntries.get(entryId);
        if (entry != null && entry.getUserId() == userId) {
            return entry;
        }
        return null;
    }

    @Override
    public void updateMealStatus(int userId, int entryId, String status) {
        MealPlanEntry entry = mealPlanEntries.get(entryId);
        if (entry != null && entry.getUserId() == userId) {
            entry.setStatus(status);
        }
        else {
            throw new IllegalArgumentException("Entry not found or unauthorized access");
        }
    }

    @Override
    public void addMealPlanEntry(int userId, int recipeId, LocalDate date, String mealType) {
        Recipe recipe = savedRecipesDataAccess.getSavedRecipe(userId, recipeId);
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe not found in saved recipes");
        }

        MealPlanEntry entry = new MealPlanEntry(nextEntryId++, recipe, date, userId, mealType);
        mealPlanEntries.put(entry.getEntryId(), entry);
    }

    @Override
    public void removeMealPlanEntry(int userId, int mealPlanEntryId) {
        MealPlanEntry entry = mealPlanEntries.get(mealPlanEntryId);
        if (entry != null && entry.getUserId() == userId) {
            mealPlanEntries.remove(mealPlanEntryId);
        }
    }

    @Override
    public List<MealPlanEntry> getWeeklyPlan(int userId, LocalDate weekStart) {
        return mealPlanEntries.values().stream()
                .filter(entry -> entry.getUserId() == userId)
                .filter(entry -> {
                    LocalDate date = entry.getDate();
                    return !date.isBefore(weekStart) && date.isBefore(weekStart.plusDays(7));
                })
                .toList();
    }
}
