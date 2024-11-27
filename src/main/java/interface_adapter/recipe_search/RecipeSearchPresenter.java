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
        state.setRecipes(recipes);

        for (Recipe recipe : recipes) {
            StringBuilder description = new StringBuilder();

            // Recipe title in bold
            description.append("----\n")
                    .append("✦ ").append(recipe.getTitle().toUpperCase()).append("\n")
                    .append("----\n\n");

            // Servings info
            description.append("👥 Servings: ").append(recipe.getServings()).append("\n\n");

            // Ingredients section
            description.append("📝 INGREDIENTS:\n");
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (ingredient.getQuantity() == 0) {
                    description.append("  • ").append(ingredient.getName()).append("\n");
                } else {
                    String quantityString = ingredient.getQuantity() == Math.floor(ingredient.getQuantity())
                            ? String.valueOf((int) ingredient.getQuantity())
                            : String.format("%.2f", ingredient.getQuantity());

                    if (ingredient.getUnit() == null || ingredient.getUnit().trim().isEmpty() ||
                            ingredient.getUnit().equalsIgnoreCase("<unit>")) {
                        description.append("  • ")
                                .append(quantityString)
                                .append(" ")
                                .append(ingredient.getName())
                                .append("\n");
                    } else {
                        description.append("  • ")
                                .append(quantityString)
                                .append(" ")
                                .append(ingredient.getUnit())
                                .append(" ")
                                .append(ingredient.getName())
                                .append("\n");
                    }
                }
            }

            description.append("\n"); // Add extra spacing between recipes
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
        // Instead of creating a new state, get the current state and update it
        RecipeSearchState currentState = (RecipeSearchState) viewModel.getState();
        RecipeSearchState newState = new RecipeSearchState(currentState); // Copy the current state
        newState.setMessage("Recipe '" + recipe.getTitle() + "' saved successfully!");
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }
}