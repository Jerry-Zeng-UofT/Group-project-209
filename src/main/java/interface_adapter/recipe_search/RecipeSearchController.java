package interface_adapter.recipe_search;

import use_case.recipe_search.RecipeSearch;

/**
 * Controller for the RecipeSearch system.
 */
public class RecipeSearchController {
    private final RecipeSearch RecipeSearchInteractor;

    public RecipeSearchController(RecipeSearch RecipeSearchInteractor) {
        this.RecipeSearchInteractor = RecipeSearchInteractor;
    }

    /**
     * Executes the RecipeSearch Use Case.
     * @param query The search query (e.g., recipe name, dish type).
     * @param maxFat The maximum amount of fat in grams the recipe can have per serving.
     * @param number The number of results to return.
     */
    public void execute(String query, int maxFat, int number) {

        RecipeSearchInteractor.searchRecipes(query, maxFat, number);
    }
}

