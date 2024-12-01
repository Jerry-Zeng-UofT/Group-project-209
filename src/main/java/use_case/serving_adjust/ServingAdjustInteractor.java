package use_case.serving_adjust;

import entity.Recipe;

import java.util.List;

public class ServingAdjustInteractor implements ServingAdjust, ServingAdjustInputBoundary {

    private final ServingAdjustOutputBoundary outputBoundary;

    public ServingAdjustInteractor(ServingAdjustOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void adjustServings(int newServings, Recipe recipe) {
        if (newServings <= 0) {
            throw new IllegalArgumentException("Servings must be greater than zero.");
        }

        int currentServings = recipe.getServings();
        double factor = (double) newServings / currentServings;

        recipe.getIngredients().forEach(ingredient -> {
            ingredient.setQuantity(ingredient.getQuantity() * factor);
        });

        recipe.setServings(newServings);
    }

    @Override
    public void adjustServingsForMultiple(int newServings, List<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            adjustServings(newServings, recipe);
        }
        outputBoundary.presentUpdatedRecipes(recipes);
    }

    @Override
    public void adjustServings(int servings, List<Recipe> recipes) {
        adjustServingsForMultiple(servings, recipes);
    }
}