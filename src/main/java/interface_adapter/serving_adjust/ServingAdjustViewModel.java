package interface_adapter.serving_adjust;

import entity.Recipe;
import interface_adapter.ViewModel;

import java.util.List;

/**
 * ViewModel for serving adjustment functionality.
 */
public class ServingAdjustViewModel extends ViewModel<ServingAdjustState> {

    public static final String UPDATED_RECIPES_PROPERTY = "updatedRecipes";
    public static final String UPDATED_SERVINGS_PROPERTY = "updatedServings";

    public ServingAdjustViewModel() {
        super("Serving Adjustment");
        setState(new ServingAdjustState());
    }

    /**
     * Update the recipes in the state and notify listeners.
     *
     * @param recipes The list of updated recipes.
     */
    public void updateRecipes(List<Recipe> recipes) {
        ServingAdjustState currentState = getState();
        if (currentState != null) {
            currentState.setRecipes(recipes);
            firePropertyChanged(UPDATED_RECIPES_PROPERTY);
        }
    }

    /**
     * Update the servings in the state and notify listeners.
     *
     * @param servings The updated servings count.
     */
    public void updateServings(int servings) {
        ServingAdjustState currentState = getState();
        if (currentState != null) {
            currentState.setCurrentServings(servings);
            firePropertyChanged(UPDATED_SERVINGS_PROPERTY);
        }
    }

    /**
     * Set an error message in the state and notify listeners.
     *
     * @param error Error message to set.
     */
    public void setError(String error) {
        ServingAdjustState currentState = getState();
        if (currentState != null) {
            currentState.setError(error);
        }
    }
}