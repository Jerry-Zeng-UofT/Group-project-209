package interface_adapter.recipe_search;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state for recipe search functionality.
 */
public class RecipeSearchState {
    private List<String> ingredients = new ArrayList<>();
    private List<String> recipeResults = new ArrayList<>();
    private String error;

    /**
     * Copy constructor for RecipeSearchState.
     * @param copy The state to copy from
     */
    public RecipeSearchState(RecipeSearchState copy) {
        ingredients = new ArrayList<>(copy.ingredients);
        recipeResults = new ArrayList<>(copy.recipeResults);
        error = copy.error;
    }

    /**
     * Default constructor for RecipeSearchState.
     */
    public RecipeSearchState() {

    }

    public List<String> getIngredients() {
        return new ArrayList<>(ingredients);
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
    }

    public List<String> getRecipeResults() {
        return new ArrayList<>(recipeResults);
    }

    public void setRecipeResults(List<String> recipeResults) {
        this.recipeResults = new ArrayList<>(recipeResults);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
