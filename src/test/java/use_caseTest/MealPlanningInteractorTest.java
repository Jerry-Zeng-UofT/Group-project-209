package use_caseTest;

import data_access.*;
import data_access.SavedRecipesDataAccessObject;
import entity.MealPlanEntry;
import entity.Recipe;
import use_case.meal_planning.MealPlanningDataAccessInterface;
import use_case.meal_planning.MealPlanningInteractor;
import use_case.meal_planning.MealPlanningOutputBoundary;
import data_access.SavedRecipesDataAccessInterface;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MealPlanningInteractorTest {

    private MealPlanningDataAccessInterface dataAccess;
    private SavedRecipesDataAccessInterface savedRecipesDataAccessInterface;
    private MealPlanningOutputBoundary outputBoundary;
    private MealPlanningInteractor interactor;

    @Before
    public void setUp() {
        // Simple in-memory implementations of dependencies
        dataAccess = new MealPlanningDataAccessObject(new SavedRecipesDataAccessObject());
        savedRecipesDataAccessInterface = new SavedRecipesDataAccessObject();
        outputBoundary = new MealPlanningOutputBoundary() {
            @Override
            public void presentSavedRecipes(List<Recipe> recipes) {
                // Do nothing (or log, if needed)
            }

            @Override
            public void presentError(String message) {
                System.out.println("Error: " + message);
            }

            @Override
            public void presentStatusUpdateSuccess(String message) {
                System.out.println("Status Update: " + message);
            }

            @Override
            public void presentAddSuccess(String message) {
                System.out.println("Add Success: " + message);
            }

            @Override
            public void presentRemoveSuccess(String message) {
                System.out.println("Remove Success: " + message);
            }

            @Override
            public void presentCalendarWeek(List<MealPlanEntry> mealPlanEntries) {
                // Do nothing (or log, if needed)
            }
        };

        // Create the interactor
        interactor = new MealPlanningInteractor(dataAccess, savedRecipesDataAccessInterface, outputBoundary);
    }

    @Test
    public void testAddToCalendar() {
        // Create a dummy recipe
        Recipe recipe = new Recipe(1, "Spaghetti Bolognese", "Delicious pasta with meat sauce", new ArrayList<>(), "Boil pasta, cook meat sauce", null, new ArrayList<>(), null, 4);
        savedRecipesDataAccessInterface.saveRecipe(1, recipe);

        // Add recipe to calendar for user 1
        LocalDate date = LocalDate.now();
        interactor.addToCalendar(1, 1, date, "Dinner");

        // Verify that the recipe was added (check for its existence in the meal plan entries)
        List<MealPlanEntry> weeklyPlan = dataAccess.getWeeklyPlan(1, date.withDayOfMonth(1));  // Using the start of the current month as an example
        assertTrue(weeklyPlan.size() > 0);
        assertEquals("Spaghetti Bolognese", weeklyPlan.get(0).getRecipe().getTitle());
    }

    @Test
    public void testUpdateMealStatus() {
        // Create a dummy recipe
        Recipe recipe = new Recipe(1, "Chicken Salad", "Healthy salad with chicken", new ArrayList<>(), "Mix ingredients", null, new ArrayList<>(), null, 2);
        savedRecipesDataAccessInterface.saveRecipe(1, recipe);

        // Add recipe to calendar
        LocalDate date = LocalDate.now();
        interactor.addToCalendar(1, 1, date, "Lunch");

        // Update meal status
        List<MealPlanEntry> weeklyPlan = dataAccess.getWeeklyPlan(1, date.withDayOfMonth(1));
        int entryId = weeklyPlan.get(0).getEntryId();
        interactor.updateMealStatus(1, entryId, "Completed");

        // Verify the status is updated
        MealPlanEntry updatedEntry = dataAccess.getMealPlanEntry(1, entryId);
        assertNotNull(updatedEntry);
        assertEquals("Completed", updatedEntry.getStatus());
    }

    @Test
    public void testRemoveFromCalendar() {
        // Create a dummy recipe
        Recipe recipe = new Recipe(1, "Grilled Cheese", "Classic grilled cheese sandwich", new ArrayList<>(), "Grill bread and cheese", null, new ArrayList<>(), null, 1);
        savedRecipesDataAccessInterface.saveRecipe(1, recipe);

        // Add recipe to calendar
        LocalDate date = LocalDate.now();
        interactor.addToCalendar(1, 1, date, "Snack");

        // Remove the recipe
        List<MealPlanEntry> weeklyPlan = dataAccess.getWeeklyPlan(1, date.withDayOfMonth(1));
        int entryId = weeklyPlan.get(0).getEntryId();
        interactor.removeFromCalendar(1, entryId);

        // Verify the recipe is removed
        MealPlanEntry removedEntry = dataAccess.getMealPlanEntry(1, entryId);
        assertNull(removedEntry);
    }

    @Test
    public void testGetCalendarWeek() {
        // Create and add multiple recipes
        Recipe recipe1 = new Recipe(1, "Pancakes", "Fluffy pancakes", new ArrayList<>(), "Cook pancakes", null, new ArrayList<>(), null, 2);
        savedRecipesDataAccessInterface.saveRecipe(1, recipe1);

        Recipe recipe2 = new Recipe(2, "Omelette", "Cheese omelette", new ArrayList<>(), "Whisk eggs and cook", null, new ArrayList<>(), null, 1);
        savedRecipesDataAccessInterface.saveRecipe(1, recipe2);

        LocalDate weekStart = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        interactor.addToCalendar(1, 1, weekStart, "Breakfast");
        interactor.addToCalendar(1, 2, weekStart.plusDays(1), "Breakfast");

        // Get the weekly plan
        List<MealPlanEntry> weeklyPlan = dataAccess.getWeeklyPlan(1, weekStart);

        // Verify that the correct recipes are in the weekly plan
        assertEquals(2, weeklyPlan.size());
        assertTrue(weeklyPlan.stream().anyMatch(entry -> entry.getRecipe().getTitle().equals("Pancakes")));
        assertTrue(weeklyPlan.stream().anyMatch(entry -> entry.getRecipe().getTitle().equals("Omelette")));
    }

    @Test
    public void testInitializeMealPlanning() {
        // Test the initialization of the meal planning for a user
        Recipe recipe = new Recipe(1, "Salad", "Fresh salad", new ArrayList<>(), "Mix ingredients", null, new ArrayList<>(), null, 1);
        savedRecipesDataAccessInterface.saveRecipe(1, recipe);

        LocalDate weekStart = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        interactor.addToCalendar(1, 1, weekStart, "Lunch");

        // Initialize meal planning
        interactor.initializeMealPlanning(1);

        // Verify that the meal plan for the current week is loaded
        List<MealPlanEntry> weeklyPlan = dataAccess.getWeeklyPlan(1, weekStart);
        assertFalse(weeklyPlan.isEmpty());
        assertEquals("Salad", weeklyPlan.get(0).getRecipe().getTitle());
    }
}