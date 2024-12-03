package use_caseTest;

import data_access.SavedRecipesDataAccessInterface;
import entity.MealPlanEntry;
import entity.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import use_case.meal_planning.MealPlanningDataAccessInterface;
import use_case.meal_planning.MealPlanningException;
import use_case.meal_planning.MealPlanningInteractor;
import use_case.meal_planning.MealPlanningOutputBoundary;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MealPlanningInteractorTest {
    private MealPlanningDataAccessInterface mockDataAccess;
    private SavedRecipesDataAccessInterface mockSavedRecipesAccess;
    private MealPlanningOutputBoundary mockOutputBoundary;
    private MealPlanningInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDataAccess = Mockito.mock(MealPlanningDataAccessInterface.class);
        mockSavedRecipesAccess = Mockito.mock(SavedRecipesDataAccessInterface.class);
        mockOutputBoundary = Mockito.mock(MealPlanningOutputBoundary.class);
        interactor = new MealPlanningInteractor(mockDataAccess, mockSavedRecipesAccess, mockOutputBoundary);
    }

    @Test
    void testGetSavedRecipesSuccess() {
        int userId = 1;
        Recipe mockRecipe = mock(Recipe.class);
        List<Recipe> recipes = List.of(mockRecipe);

        when(mockSavedRecipesAccess.getSavedRecipes(userId)).thenReturn(recipes);

        List<Recipe> result = interactor.getSavedRecipes(userId);

        assertEquals(recipes, result);
        verify(mockOutputBoundary).presentSavedRecipes(recipes);
    }

    @Test
    void testGetSavedRecipesInvalidUser() {
        int userId = -1;

        MealPlanningException exception = assertThrows(MealPlanningException.class, () -> interactor.getSavedRecipes(userId));

        assertEquals("Invalid user ID", exception.getMessage());
        verify(mockOutputBoundary).presentError("Invalid user ID");
    }

    @Test
    void testUpdateMealStatusSuccess() {
        int userId = 1;
        int entryId = 10;
        String status = "Completed";

        interactor.updateMealStatus(userId, entryId, status);

        verify(mockDataAccess).updateMealStatus(userId, entryId, status);
        verify(mockOutputBoundary).presentStatusUpdateSuccess("Meal status updated");
    }

    @Test
    void testUpdateMealStatusInvalidStatus() {
        int userId = 1;
        int entryId = 10;
        String status = "";

        MealPlanningException exception = assertThrows(MealPlanningException.class, () -> interactor.updateMealStatus(userId, entryId, status));

        assertEquals("Status cannot be empty", exception.getMessage());
        verify(mockOutputBoundary).presentError("Status cannot be empty");
    }

    @Test
    void testAddToCalendarSuccess() {
        int userId = 1;
        int recipeId = 2;
        LocalDate date = LocalDate.now();
        String mealType = "Lunch";

        interactor.addToCalendar(userId, recipeId, date, mealType);

        verify(mockDataAccess).addMealPlanEntry(userId, recipeId, date, mealType);
        verify(mockOutputBoundary).presentAddSuccess("Recipe added to calendar");
    }

    @Test
    void testAddToCalendarInvalidDate() {
        int userId = 1;
        int recipeId = 2;
        LocalDate date = null;
        String mealType = "Lunch";

        MealPlanningException exception = assertThrows(MealPlanningException.class, () -> interactor.addToCalendar(userId, recipeId, date, mealType));

        assertEquals("Date cannot be null", exception.getMessage());
        verify(mockOutputBoundary).presentError("Date cannot be null");
    }

    @Test
    void testAddToCalendarInvalidMealType() {
        int userId = 1;
        int recipeId = 2;
        LocalDate date = LocalDate.now();
        String mealType = ""; // Invalid meal type

        MealPlanningException exception = assertThrows(MealPlanningException.class, () -> interactor.addToCalendar(userId, recipeId, date, mealType));

        assertEquals("Meal type cannot be empty", exception.getMessage());
        verify(mockOutputBoundary).presentError("Meal type cannot be empty");
    }

    @Test
    void testRemoveFromCalendarSuccess() {
        int userId = 1;
        int mealPlanEntryId = 5;

        interactor.removeFromCalendar(userId, mealPlanEntryId);

        verify(mockDataAccess).removeMealPlanEntry(userId, mealPlanEntryId);
        verify(mockOutputBoundary).presentRemoveSuccess("Recipe removed from calendar");
    }

    @Test
    void testGetCalendarWeekSuccess() {
        int userId = 1;
        LocalDate weekStart = LocalDate.now();
        List<MealPlanEntry> entries = Collections.emptyList();

        when(mockDataAccess.getWeeklyPlan(userId, weekStart)).thenReturn(entries);

        interactor.getCalendarWeek(userId, weekStart);

        verify(mockDataAccess).getWeeklyPlan(userId, weekStart);
        verify(mockOutputBoundary).presentCalendarWeek(entries);
    }

    @Test
    void testInitializeMealPlanningSuccess() {
        int userId = 1;
        LocalDate currentWeekStart = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        List<MealPlanEntry> entries = Collections.emptyList();

        when(mockDataAccess.getWeeklyPlan(userId, currentWeekStart)).thenReturn(entries);

        interactor.initializeMealPlanning(userId);

        verify(mockDataAccess).getWeeklyPlan(userId, currentWeekStart);
        verify(mockOutputBoundary).presentCalendarWeek(entries);
    }

    @Test
    void testInitializeMealPlanningInvalidUser() {
        int userId = -1;

        MealPlanningException exception = assertThrows(MealPlanningException.class, () -> interactor.initializeMealPlanning(userId));

        assertEquals("Invalid user ID", exception.getMessage());
        verify(mockOutputBoundary).presentError("Invalid user ID");
    }
}
