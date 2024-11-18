package interface_adapter.recipe_search;

import entity.Recipe;
import entity.Ingredient;
import use_case.recipe_search.RecipeSearchOutputBoundary;
import java.util.List;
import java.util.ArrayList;

/**
 * Presenter for the recipe search use case.
 */
public class RecipeSearchPresenter implements RecipeSearchOutputBoundary {
    private final RecipeSearchViewModel viewModel;

    public RecipeSearchPresenter(RecipeSearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentRecipes(List<Recipe> recipes) {
        RecipeSearchState state = new RecipeSearchState();
        List<String> recipeResults = new ArrayList<>();

        // Store the actual Recipe objects
        state.setRecipes(recipes);

        // Create string representations for display
        for (Recipe recipe : recipes) {
            StringBuilder description = new StringBuilder();
            description.append(recipe.getTitle()).append("\n");
            description.append("Ingredients:\n");
            for (Ingredient ingredient : recipe.getIngredients()) {
                description.append("- ").append(ingredient.getName()).append("\n");
            }
            recipeResults.add(description.toString());
        }

        state.setRecipeResults(recipeResults);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentError(String error) {
        RecipeSearchState state = new RecipeSearchState();
        state.setError(error);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentSaveSuccess(Recipe recipe) {
        RecipeSearchState state = new RecipeSearchState();
        state.setMessage("Recipe '" + recipe.getTitle() + "' saved successfully!");
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}