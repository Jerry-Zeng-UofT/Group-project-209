package use_case.serving_adjust;

import entity.Recipe;
import java.util.List;

/**
 * Interactor class for the serving adjustment use case.
 */
public class ServingAdjustInteractor implements ServingAdjustInputBoundary {

    private final ServingAdjustOutputBoundary outputBoundary;
    private final ServingAdjustDataAccessInterface dataAccess;

    /**
     * Constructs a ServingAdjustInteractor with the specified output boundary and data access interface.
     *
     * @param outputBoundary The output boundary to handle the presentation of results.
     * @param dataAccess     The data access interface for saving updated recipes.
     */
    public ServingAdjustInteractor(ServingAdjustOutputBoundary outputBoundary,
                                   ServingAdjustDataAccessInterface dataAccess) {
        this.outputBoundary = outputBoundary;
        this.dataAccess = dataAccess;
    }

    /**
     * Adjusts the servings for the given input data.
     *
     * @param inputData The input data containing new servings and recipes.
     * @throws ServingAdjustException if an error occurs during serving adjustment.
     */
    @Override
    public void adjustServings(ServingAdjustInputData inputData) throws ServingAdjustException {
        int newServings = inputData.getNewServings();
        List<Recipe> recipes = inputData.getRecipes();

        for (Recipe recipe : recipes) {
            adjustServingsForRecipe(newServings, recipe);
        }

        // Save the updated recipes
        dataAccess.saveUpdatedRecipes(recipes);

        // Prepare output data and present it
        ServingAdjustOutputData outputData = new ServingAdjustOutputData(recipes);
        outputBoundary.presentUpdatedRecipes(outputData);
    }

    /**
     * Adjusts the servings for a single recipe.
     *
     * @param newServings The new number of servings.
     * @param recipe      The recipe to adjust.
     * @throws ServingAdjustException if the recipe is null or current servings are invalid.
     */
    private void adjustServingsForRecipe(int newServings, Recipe recipe) throws ServingAdjustException {
        if (recipe == null) {
            throw new ServingAdjustException("Recipe cannot be null.");
        }

        int currentServings = recipe.getServings();
        if (currentServings <= 0) {
            throw new ServingAdjustException("Current servings must be greater than zero.");
        }

        double factor = (double) newServings / currentServings;

        recipe.getIngredients().forEach(ingredient -> {
            ingredient.setQuantity(ingredient.getQuantity() * factor);
        });

        recipe.setServings(newServings);
    }
}
