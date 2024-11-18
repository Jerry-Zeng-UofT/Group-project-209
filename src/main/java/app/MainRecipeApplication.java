package app;

import data_access.RecipeSearchEdamam;

/**
 * An application where users can search for recipes based on ingredients.
 * <p>
 * This application allows users to input ingredients and search for matching recipes
 * using the Edamam Recipe Search API. Users can view recipe details including
 * ingredients and instructions.
 * </p>
 */
public class MainRecipeApplication {
    /**
     * Main method for the Recipe Application.
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        // Create the data access object for recipe search
        final RecipeSearchEdamam recipeSearchEdamam = new RecipeSearchEdamam();

        // Create and configure the application using the builder
        final RecipeAppBuilder builder = new RecipeAppBuilder();

        // Build the application with correct order
        builder.addRecipeSearchAPI(recipeSearchEdamam)
                .addMealPlanningView()
                .addMealPlanningUseCase()
                .addRecipeSearchView()
                .addRecipeSearchUseCase();

        // Build and show the frame
        builder.build().setVisible(true);
    }
}