package use_caseTest;

import data_access.*;
import entity.MealPlanEntry;
import entity.Recipe;
import interface_adapter.recipe_search.RecipeSearchPresenter;
import interface_adapter.recipe_search.RecipeSearchViewModel;
import use_case.meal_planning.MealPlanningInteractor;
import use_case.meal_planning.MealPlanningOutputBoundary;
import org.junit.Before;
import org.junit.Test;
import use_case.recipe_search.RecipeSearchInteractor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class MealPlanningInteractorTest {

    private MealPlanningDataAccess dataAccess;
    private SavedRecipesDataAccess savedRecipesDataAccess;
    private MealPlanningOutputBoundary outputBoundary;
    private MealPlanningInteractor interactor;
    private Recipe recipe1;
    private Recipe recipe2;

    @Before
    public void setUp() {
        // Simple in-memory implementations of dependencies
        dataAccess = new MealPlanningDataAccessObject(new SavedRecipesDataAccessObject());
        savedRecipesDataAccess = new SavedRecipesDataAccessObject();
        outputBoundary = new MealPlanningOutputBoundary() {
            @Override
            public void presentSavedRecipes(List<Recipe> recipes) { }

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
            public void presentCalendarWeek(List<MealPlanEntry> mealPlanEntries) { }
        };

        // Create the interactor
        interactor = new MealPlanningInteractor(dataAccess, savedRecipesDataAccess, outputBoundary);

        // Establishment of a system that produces valid Recipe Object
        SavedRecipesDataAccessObject savedRecipesDataAccess = new SavedRecipesDataAccessObject();
        RecipeSearchViewModel recipeSearchViewModel = new RecipeSearchViewModel();
        RecipeSearchPresenter recipeSearchPresenter = new RecipeSearchPresenter(recipeSearchViewModel);
        RecipeSearchDataAccessObject recipeSearchDataAccessObject = new RecipeSearchDataAccessObject();

        RecipeSearchInteractor recipeSearchInputBoundaryUseCase = new RecipeSearchInteractor(
                recipeSearchDataAccessObject,
                savedRecipesDataAccess,
                recipeSearchPresenter
        );

        // Assume RecipeSearch use case (RecipeSearchInteractor) is well tested (independence principle).
        final String exampleFoodName = "egg";
        List<String> exampleIngredients = List.of(exampleFoodName);
        recipeSearchInputBoundaryUseCase.searchRecipes(exampleIngredients);
        recipe1 = recipeSearchViewModel.getState().getRecipes().get(1);
        recipe2 = recipeSearchViewModel.getState().getRecipes().get(2);
    }

    @Test
    public void testAddToCalendar() {
        savedRecipesDataAccess.saveRecipe(1, recipe1);

        // Add recipe to calendar for user 1
        LocalDate date = LocalDate.now();
        interactor.addToCalendar(1, 1, date, "Dinner");

        // Verify that the recipe was added (check for its existence in the meal plan entries)
        List<MealPlanEntry> weeklyPlan = dataAccess.getWeeklyPlan(1, date.withDayOfMonth(1));  // Using the start of the current month as an example
        assertTrue("The recipe should be added to the meal plan", weeklyPlan.size() > 0);
        assertEquals("Spaghetti Bolognese", weeklyPlan.get(0).getRecipe().getTitle());
    }

    @Test
    public void testUpdateMealStatus() {
        savedRecipesDataAccess.saveRecipe(1, recipe1);

        // Add recipe to calendar
        LocalDate date = LocalDate.now();
        interactor.addToCalendar(1, 1, date, "Lunch");

        // Update meal status
        List<MealPlanEntry> weeklyPlan = dataAccess.getWeeklyPlan(1, date.withDayOfMonth(1));
        int entryId = weeklyPlan.get(0).getEntryId();
        interactor.updateMealStatus(1, entryId, "Completed");

        // Verify the status is updated
        MealPlanEntry updatedEntry = dataAccess.getMealPlanEntry(1, entryId);
        assertNotNull("Updated entry should not be null", updatedEntry);
        assertEquals("Completed", updatedEntry.getStatus());
    }

    @Test
    public void testRemoveFromCalendar() {
        savedRecipesDataAccess.saveRecipe(1, recipe1);

        // Add recipe to calendar
        LocalDate date = LocalDate.now();
        interactor.addToCalendar(1, 1, date, "Snack");

        // Remove the recipe
        List<MealPlanEntry> weeklyPlan = dataAccess.getWeeklyPlan(1, date.withDayOfMonth(1));
        int entryId = weeklyPlan.get(1).getEntryId();
        interactor.removeFromCalendar(1, entryId);

        // Verify the recipe is removed
        MealPlanEntry removedEntry = dataAccess.getMealPlanEntry(1, entryId);
        assertNull("The meal plan entry should be removed", removedEntry);
    }

    @Test
    public void testGetCalendarWeek() {
        savedRecipesDataAccess.saveRecipe(1, recipe1);
        savedRecipesDataAccess.saveRecipe(1, recipe2);

        LocalDate weekStart = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        interactor.addToCalendar(1, 1, weekStart, "Breakfast");
        interactor.addToCalendar(1, 2, weekStart.plusDays(1), "Lunch");

        // Get the weekly plan
        List<MealPlanEntry> weeklyPlan = dataAccess.getWeeklyPlan(1, weekStart);

        // Verify that the correct recipes are in the weekly plan
        assertEquals("There should be two meal plan entries", 2, weeklyPlan.size());
        assertTrue("Weekly plan should contain Spaghetti Bolognese", weeklyPlan.stream().anyMatch(entry -> entry.getRecipe().getTitle().equals("Spaghetti Bolognese")));
        assertTrue("Weekly plan should contain Omelette", weeklyPlan.stream().anyMatch(entry -> entry.getRecipe().getTitle().equals("Omelette")));
    }

    @Test
    public void testInitializeMealPlanning() {
        // Test the initialization of the meal planning for a user
        savedRecipesDataAccess.saveRecipe(1, recipe2);

        LocalDate weekStart = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        interactor.addToCalendar(1, 1, weekStart, "Lunch");

        // Initialize meal planning
        interactor.initializeMealPlanning(1);

        // Verify that the meal plan for the current week is loaded
        List<MealPlanEntry> weeklyPlan = dataAccess.getWeeklyPlan(1, weekStart);
        assertFalse("The weekly meal plan should not be empty", weeklyPlan.isEmpty());
        assertEquals("The meal plan should contain the correct recipe", "Omelette", weeklyPlan.get(0).getRecipe().getTitle());
    }
}