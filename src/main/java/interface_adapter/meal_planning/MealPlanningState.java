package interface_adapter.meal_planning;

import entity.MealPlanEntry;
import entity.Recipe;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MealPlanningState {
    private List<MealPlanEntry> mealPlanEntries = new ArrayList<>();
    private List<Recipe> savedRecipes = new ArrayList<>();  // Added to store saved recipes
    private LocalDate currentWeekStart = LocalDate.now();
    private String message;
    private String error;
    private boolean isLoading = false;

    public MealPlanningState(MealPlanningState copy) {
        if (copy != null) {
            this.mealPlanEntries = new ArrayList<>(copy.mealPlanEntries);
            this.savedRecipes = new ArrayList<>(copy.savedRecipes);
            this.currentWeekStart = copy.currentWeekStart;
            this.message = copy.message;
            this.error = copy.error;
            this.isLoading = copy.isLoading;
        }
    }

    // Default constructor
    public MealPlanningState() {

    }

    // Getters and setters

    public List<Recipe> getSavedRecipes() {
        return new ArrayList<>(savedRecipes);
    }

    public void setSavedRecipes(List<Recipe> recipes) {
        this.savedRecipes = new ArrayList<>(recipes);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public List<MealPlanEntry> getMealPlanEntries() {
        return new ArrayList<>(mealPlanEntries);
    }

    public void setMealPlanEntries(List<MealPlanEntry> entries) {
        this.mealPlanEntries = new ArrayList<>(entries);
    }

    public LocalDate getCurrentWeekStart() {
        return currentWeekStart;
    }

    public void setCurrentWeekStart(LocalDate date) {
        this.currentWeekStart = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
