package interface_adapter.search_with_restriction;

import entity.Recipe;
import entity.Ingredient;
import use_case.search_with_restriction.SearchWithRestrictionOutputBoundry;

import java.util.List;

/**
 * Presenter for the recipe search with restriction use case.
 */
public class RestrictionPresenter implements SearchWithRestrictionOutputBoundry {
    private final RestrictionViewModel restrictionViewModel;

    public RestrictionPresenter(RestrictionViewModel restrictionViewModel) {
        this.restrictionViewModel = restrictionViewModel;
    }

    @Override
    public void presentRecipes(List<Recipe> recipes) {
        RestrictionState state = new RestrictionState();
        state.setRecipes(recipes);

        // Format each recipe manually without streams
        List<String> recipeResults = new java.util.ArrayList<>();
        for (Recipe recipe : recipes) {
            recipeResults.add(formatRecipe(recipe));
        }

        state.setRecipeResults(recipeResults);
        restrictionViewModel.setState(state);
        restrictionViewModel.firePropertyChanged();
    }

    /**
     * Formats a recipe into an HTML string.
     */
    private String formatRecipe(Recipe recipe) {
        StringBuilder description = new StringBuilder();
        description.append("<html>");
        description.append(formatTitleAndServings(recipe)).append("<br>");
        description.append(formatIngredients(recipe.getIngredients())).append("<br>");
        description.append("</html>");
        return description.toString();
    }

    /**
     * Formats the recipe title and servings information.
     */
    private String formatTitleAndServings(Recipe recipe) {
        return String.format("<b>✦ %s</b><br><i>👥 Servings:</i> %d",
                recipe.getTitle().toUpperCase(),
                recipe.getServings());
    }

    /**
     * Formats the ingredients list as a single line.
     */
    private String formatIngredients(List<Ingredient> ingredients) {
        StringBuilder ingredientsLine = new StringBuilder("<u>📝 INGREDIENTS:</u> ");

        // Iterate through ingredients and format each one
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            ingredientsLine.append(formatIngredient(ingredient));

            // Add a comma if not the last ingredient
            if (i < ingredients.size() - 1) {
                ingredientsLine.append(", ");
            }
        }

        return ingredientsLine.toString();
    }

    /**
     * Formats an individual ingredient.
     */
    private String formatIngredient(Ingredient ingredient) {
        if (ingredient.getQuantity() == 0) {
            return ingredient.getName();
        }

        String quantity = formatQuantity(ingredient.getQuantity());
        String unit = (ingredient.getUnit() == null || ingredient.getUnit().trim().isEmpty() ||
                ingredient.getUnit().equalsIgnoreCase("<unit>"))
                ? ""
                : ingredient.getUnit() + " ";

        return quantity + unit + ingredient.getName();
    }

    /**
     * Formats the quantity of an ingredient to show up to 2 decimal places if necessary.
     */
    private String formatQuantity(double quantity) {
        return quantity == Math.floor(quantity)
                ? String.valueOf((int) quantity)
                : String.format("%.2f", quantity);
    }

    @Override
    public void presentError(String error) {
        RestrictionState state = new RestrictionState();
        state.setError(error);
        restrictionViewModel.setState(state);
        restrictionViewModel.firePropertyChanged();
    }
}
