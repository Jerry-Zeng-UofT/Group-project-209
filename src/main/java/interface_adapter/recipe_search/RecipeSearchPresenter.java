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

            description.append("<html>");
            description.append("<b>✦ ").append(recipe.getTitle().toUpperCase()).append("</b><br>");

            description.append("<i>👥 Servings:</i> ").append(recipe.getServings()).append("<br><br>");

            description.append("<u>📝 INGREDIENTS:</u> ");

            // Combine ingredients into a single line
            List<String> ingredientStrings = new ArrayList<>();
            for (Ingredient ingredient : recipe.getIngredients()) {
                String quantityString = ingredient.getQuantity() == Math.floor(ingredient.getQuantity())
                        ? String.valueOf((int) ingredient.getQuantity())
                        : String.format("%.2f", ingredient.getQuantity());

                if (ingredient.getQuantity() == 0) {
                    ingredientStrings.add(ingredient.getName());
                } else if (ingredient.getUnit() == null || ingredient.getUnit().trim().isEmpty()
                        || ingredient.getUnit().equalsIgnoreCase("<unit>")) {
                    ingredientStrings.add(quantityString + " " + ingredient.getName());
                } else {
                    ingredientStrings.add(quantityString + " " + ingredient.getUnit() + " " + ingredient.getName());
                }
            }

            // Join all ingredients with commas
            description.append(String.join(", ", ingredientStrings)).append("<br>");

            description.append("</html>");
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