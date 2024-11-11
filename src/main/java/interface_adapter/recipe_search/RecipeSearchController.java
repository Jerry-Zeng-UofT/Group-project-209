package interface_adapter.recipe_search;

import use_case.recipe_search.RecipeSearch;
import java.util.List;

/**
 * Controller for the recipe search functionality.
 */
public class RecipeSearchController {
    private final RecipeSearch recipeSearchUseCase;

    public RecipeSearchController(RecipeSearch recipeSearchUseCase) {
        this.recipeSearchUseCase = recipeSearchUseCase;
    }

    /**
     * Execute a recipe search with the given ingredients.
     * @param ingredients List of ingredients to search for
     */
    public void executeSearch(List<String> ingredients) {
        try {
            recipeSearchUseCase.searchRecipes(ingredients);
        }
        catch (Exception e) {
            // Error will be handled by the presenter through output boundary
        }
    }
}

