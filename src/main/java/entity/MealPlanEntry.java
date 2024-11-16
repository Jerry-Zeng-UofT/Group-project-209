package entity;

import java.time.LocalDate;

public class MealPlanEntry {
    private int entryId;
    private Recipe recipe;
    private LocalDate date;
    private int userId;
    // e.g., "Breakfast", "Lunch", "Dinner"
    private String mealType;

    public MealPlanEntry(int entryId, Recipe recipe, LocalDate date, int userId, String mealType) {
        this.entryId = entryId;
        this.recipe = recipe;
        this.date = date;
        this.userId = userId;
        this.mealType = mealType;
    }

    // Getters and setters
    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
}
