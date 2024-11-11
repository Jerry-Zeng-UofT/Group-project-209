package use_case.recipe_search;

import java.util.List;
import entity.Recipe;
import repository.RecipeRepository;
import exception.RecipeSearchException;

/**
 * Implementation of the RecipeSearchUseCase interface. This class is responsible
 * for searching recipes using external APIs or data sources. It delegates the actual
 * search operation to the RecipeApiGateway.
 */
public class RecipeSearchImpl implements RecipeSearch {

    private final RecipeRepository recipeRepository;

    /**
     * Constructor for the RecipeSearchUseCaseImpl.
     * Initializes the RecipeApiGateway which is used to fetch recipe data.
     *
     * @param recipeRepository The gateway for accessing the external API or service.
     */
    public RecipeSearchImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Searches for recipes based on the provided search parameters.
     * This method calls the RecipeApiGateway to fetch recipe data and returns
     * the result as a list of Recipe objects.
     *
     * @param query The search query (e.g., recipe name, dish type).
     * @param maxFat The maximum amount of fat in grams the recipe can have per serving.
     * @param number The number of results to return.
     * @return A list of recipes that match the search criteria.
     * @throws RecipeSearchException if an error occurs while searching for recipes.
     */
    public List<Recipe> searchRecipes(String query, int maxFat, int number) throws RecipeSearchException {
        try {
            // Use the recipeApiGateway to fetch recipes based on the parameters
            return recipeRepository.fetchRecipes(query, maxFat, number);
        } catch (Exception e) {
            // Wrap any errors into a custom exception
            throw new RecipeSearchException("Failed to search recipes", e);
        }
    }
}
