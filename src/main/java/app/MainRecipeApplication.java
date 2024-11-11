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
     * The main entry point of the application.
     * <p>
     * The program will show a search interface where users can:
     * - Add ingredients to their search
     * - Remove ingredients from their search
     * - Search for recipes matching their ingredients
     * - View recipe results including ingredients and basic instructions
     * </p>
     * @param args commandline arguments are ignored
     */
    public static void main(String[] args) {
        // Create the data access object for recipe search
        final RecipeSearchEdamam recipeSearchEdamam = new RecipeSearchEdamam();

        // Create and configure the application using the builder
        final RecipeAppBuilder builder = new RecipeAppBuilder();
        builder.addRecipeSearchAPI(recipeSearchEdamam)
                .addRecipeSearchView()
                .addRecipeSearchUseCase()
                .build()
                .setVisible(true);
    }
}