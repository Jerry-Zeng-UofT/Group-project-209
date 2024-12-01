package data_access;

import java.io.IOException;
import java.util.List;

import entity.Recipe;

public interface RecipeSearchDataAccess {
    /**
     * Get searchedRecipe name from a POST request from the API.
     *
     * @param foodName the recipe name we want to display.
     * @return A list of Recipe entity
     * @throws IOException If an I/O error occurs.
     */
    List<Recipe> searchRecipesByFoodName(String foodName);
}
