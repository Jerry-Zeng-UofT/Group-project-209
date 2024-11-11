package app;

import javax.swing.*;
import data_access.RecipeSearchEdamam;
import interface_adapter.recipe_search.*;
import use_case.recipe_search.*;
import view.RecipeSearchView;

/**
 * Main application class for the recipe search functionality.
 */
public class MainRecipeApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create components
            RecipeSearchViewModel viewModel = new RecipeSearchViewModel();
            RecipeSearchView view = new RecipeSearchView(viewModel);
            RecipeSearchPresenter presenter = new RecipeSearchPresenter(viewModel);
            RecipeSearchEdamam edamamAPI = new RecipeSearchEdamam();
            RecipeSearch recipeSearchUseCase = new RecipeSearchImpl(edamamAPI, presenter);
            RecipeSearchController controller = new RecipeSearchController(recipeSearchUseCase);

            // Connect view to controller
            view.setRecipeSearchController(controller);

            // Create and show the frame
            JFrame frame = new JFrame("Recipe Search");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}