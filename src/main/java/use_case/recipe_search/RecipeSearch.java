package use_case.recipe_search;

import java.util.List;

import entity.Recipe;

/**
 * Interface for the use case that handles searching for recipes based on
 * various filters such as ingredients, cuisines, and nutritional information.
 * This use case allows the application to fetch recipes by making calls to
 * external APIs or services.
 */
public interface RecipeSearch {

    /**
     * Searches for recipes based on the provided query and filters.
     *
     * @param query The search query (e.g., recipe name, dish type).
     * @param maxFat The maximum amount of fat in grams the recipe can have per serving.
     * @param number The number of results to return.
     * @return A list of recipes that match the search criteria.
     * @throws RecipeSearchException if an error occurs while fetching the recipes.
     */
    List<Recipe> searchRecipes(String query, int maxFat, int number) throws RecipeSearchException;
}
