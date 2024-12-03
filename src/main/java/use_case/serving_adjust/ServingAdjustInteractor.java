package use_case.serving_adjust;

import java.util.List;

import entity.Recipe;

/**
 * Implementation of the serving adjustment use case.
 */
public class ServingAdjustInteractor implements ServingAdjust, ServingAdjustInputBoundary {

    private final ServingAdjustOutputBoundary outputBoundary;

    /**
     * Constructs a ServingAdjustInteractor with the specified output boundary.
     *
     * @param outputBoundary The output boundary to handle the results of the serving adjustment.
     */
    public ServingAdjustInteractor(ServingAdjustOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    /**
     * Adjusts the servings for a single recipe.
     *
     * @param newServings The new number of servings. Must be greater than zero.
     * @param recipe The recipe to adjust. Must not be null.
     * @throws IllegalArgumentException if {@code newServings} is less than or equal to zero.i
     */
    @Override
    public void adjustServings(int newServings, Recipe recipe) {
        if (newServings <= 0) {
            throw new IllegalArgumentException("Servings must be greater than zero.");
        }

        final int currentServings = recipe.getServings();
        final double factor = (double) newServings / currentServings;

        recipe.getIngredients().forEach(ingredient -> {
            ingredient.setQuantity(ingredient.getQuantity() * factor);
        });

        recipe.setServings(newServings);
    }

    /**
     * Adjusts the servings for a list of recipes.
     *
     * @param servings The new number of servings. Must be greater than zero.
     * @param recipes The list of recipes to adjust. Must not be null or empty.
     * @throws IllegalArgumentException if {@code servings} is less than or equal to zero.
     */
    @Override
    public void adjustServings(int servings, List<Recipe> recipes) {
        adjustServingsForMultiple(servings, recipes);
    }

    /**
     * Adjusts the servings for multiple recipes.
     *
     * @param newServings The new number of servings. Must be greater than zero.
     * @param recipes The list of recipes to adjust. Must not be null or empty.
     * @throws IllegalArgumentException if {@code newServings} is less than or equal to zero.
     */
    @Override
    public void adjustServingsForMultiple(int newServings, List<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            adjustServings(newServings, recipe);
        }
        outputBoundary.presentUpdatedRecipes(recipes);
    }
}
