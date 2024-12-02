package interface_adapter.serving_adjust;

import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state for serving adjustment functionality.
 */
public class ServingAdjustState {
    private int currentServings = 1; // Default servings
    private List<Recipe> recipes = new ArrayList<>();
    private String error;
    private String message;

    /**
     * Copy constructor for ServingAdjustState.
     * @param copy The state to copy from
     */
    public ServingAdjustState(ServingAdjustState copy) {
        if (copy != null) {
            currentServings = copy.currentServings;
            recipes = new ArrayList<>(copy.recipes);
            error = copy.error;
            message = copy.message;
        }
    }

    /**
     * Default constructor for ServingAdjustState.
     */
    public ServingAdjustState() {

    }

    /**
     * Getter for currentServings.
     * @return The current servings
     */
    public int getCurrentServings() {
        return currentServings;
    }

    /**
     * Setter for currentServings.
     * @param currentServings The current servings to set
     */
    public void setCurrentServings(int currentServings) {
        if (currentServings > 0) {
            this.currentServings = currentServings;
        }
        else {
            throw new IllegalArgumentException("Servings must be greater than zero.");
        }
    }

    /**
     * Getter for recipes.
     * @return A copy of the recipes list
     */
    public List<Recipe> getRecipes() {
        return new ArrayList<>(recipes);
    }

    /**
     * Setter for recipes.
     * @param recipes The recipes to set
     */
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
    }

    /**
     * Getter for error.
     * @return The error message
     */
    public String getError() {
        return error;
    }

    /**
     * Setter for error.
     * @param error The error message to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Getter for message.
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for message.
     * @param message The message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}