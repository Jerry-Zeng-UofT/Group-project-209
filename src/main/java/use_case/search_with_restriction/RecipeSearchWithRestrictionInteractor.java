package use_case.search_with_restriction;

import data_access.SearchWithRestrictionDataAccessObject;
import entity.Food;
import entity.Ingredient;
import entity.Nutrition;
import entity.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeSearchWithRestrictionInteractor implements SearchWithRestrictionInputBoundary {
    private final SearchWithRestrictionDataAccessObject searchWithRestrictionDataAccessObject;
    private final SearchWithRestrictionOutputBoundry outputBoundary;

    public RecipeSearchWithRestrictionInteractor(SearchWithRestrictionDataAccessObject searchWithRestrictionDataAccessObject,
                                                 SearchWithRestrictionOutputBoundry outputBoundary) {
        this.searchWithRestrictionDataAccessObject = searchWithRestrictionDataAccessObject;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void searchRestrictionRecipes(Map<String, List<String>> restrictions) throws SearchWithRestrictionException {
        try {
            String searchFoodQuery = String.join(",", restrictions.get("Food Name"));

            String searchDietQuery = null;
            if (restrictions.get("Diet Label") != null && !restrictions.get("Diet Label").isEmpty()) {
                searchDietQuery = String.join(",", restrictions.get("Diet Label"));
            }

            String searchHealthQuery = null;
            if (restrictions.get("Health Label") != null && !restrictions.get("Health Label").isEmpty()) {
                searchHealthQuery = String.join(",", restrictions.get("Health Label"));
            }

            String searchCuisineQuery = null;
            if (restrictions.get("Cuisine Type") != null && !restrictions.get("Cuisine Type").isEmpty()) {
                searchCuisineQuery = String.join(",", restrictions.get("Cuisine Type"));
            }
            List<Recipe> searchResults = searchWithRestrictionDataAccessObject.searchRecipesByRestriction(
                    searchFoodQuery, searchDietQuery, searchHealthQuery, searchCuisineQuery);
            List<Recipe> recipes = convertToRecipes(searchResults);
            outputBoundary.presentRecipes(recipes);
        }
        catch (Exception e) {
            outputBoundary.presentError("Failed to search recipes: " + e.getMessage());
            throw new SearchWithRestrictionException("Recipe search failed", e);
        }
    }

    private List<Recipe> convertToRecipes(List<Recipe> searchResults) {
        List<Recipe> recipes = new ArrayList<>();
        int recipeId = 1;
        for (Recipe result : searchResults) {
            String title = result.getTitle();
            String description;
            if (result.getDescription() != null) {
                description = result.getDescription();
            }
            else {
                description = "No description available";
            }

            List<Ingredient> ingredients = result.getIngredients();

            String instructions;
            if (result.getInstructions() != null) {
                instructions = result.getInstructions();
            }
            else {
                instructions = "Instructions not available";
            }

            Nutrition nutrition;
            if (result.getNutrition() != null) {
                nutrition = result.getNutrition();
            }
            else {
                nutrition = new Nutrition(0, 0, 0, 0, 0, 0);
            }

            List<Food> food;
            if (result.getFood() != null) {
                food = result.getFood();
            }
            else {
                food = new ArrayList<>();
            }

            int servings;
            if (result.getServings() > 0) {
                servings = result.getServings();
            }
            else {
                servings = 1;
            }

            Recipe recipe = new Recipe(
                    recipeId++,
                    title,
                    description,
                    ingredients,
                    instructions,
                    nutrition,
                    food,
                    result.getJsonIngredient(),
                    servings
            );

            recipes.add(recipe);
        }

        return recipes;
    }
}
