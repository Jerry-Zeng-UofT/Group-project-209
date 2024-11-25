package interface_adapter.recipe_search;

import entity.Recipe;

import javax.swing.*;
import java.util.List;

public class ServingAdjustmentHandler {
    private final RecipeSearchController recipeSearchController;
    private final RecipeSearchPresenter recipeSearchPresenter;

    public ServingAdjustmentHandler(RecipeSearchController controller, RecipeSearchPresenter presenter) {
        this.recipeSearchController = controller;
        this.recipeSearchPresenter = presenter;
    }

    public void adjustServings(int servings, RecipeSearchState state) {
        if (recipeSearchController != null && state != null) {
            List<Recipe> recipes = state.getRecipes();

            if (recipes.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "No recipes available to update.",
                        "No Recipes",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (Recipe recipe : recipes) {
                recipeSearchController.adjustServings(servings, recipe);
            }

            recipeSearchPresenter.presentRecipes(recipes);

            JOptionPane.showMessageDialog(null,
                    "All recipes updated to " + servings + " servings.",
                    "Update Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(null,
                    "Controller unavailable. Unable to update servings.",
                    "Update Failed",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}