package app;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import data_access.RecipeSearchEdamam;
import interface_adapter.recipe_search.*;
import use_case.recipe_search.*;
import view.RecipeSearchView;

/**
 * Builder for the Recipe Search Application.
 */
public class RecipeAppBuilder {
    public static final int HEIGHT = 600;  // Increased for better recipe display
    public static final int WIDTH = 800;   // Increased for better recipe display

    private RecipeSearchEdamam recipeSearchEdamam;
    private RecipeSearchViewModel recipeSearchViewModel;
    private RecipeSearchView recipeSearchView;
    private RecipeSearch recipeSearchUseCase;

    /**
     * Sets the Recipe Search API to be used in this application.
     * @param recipeSearchEdamam the API client to use
     * @return this builder
     */
    public RecipeAppBuilder addRecipeSearchAPI(RecipeSearchEdamam recipeSearchEdamam) {
        this.recipeSearchEdamam = recipeSearchEdamam;
        return this;
    }

    /**
     * Creates the objects for the Recipe Search Use Case and connects the RecipeSearchView to its
     * controller.
     * @return this builder
     * @throws RuntimeException if this method is called before addRecipeSearchView
     */
    public RecipeAppBuilder addRecipeSearchUseCase() {
        if (recipeSearchView == null) {
            throw new RuntimeException("addRecipeSearchView must be called before addRecipeSearchUseCase");
        }

        RecipeSearchPresenter presenter = new RecipeSearchPresenter(recipeSearchViewModel);
        recipeSearchUseCase = new RecipeSearchImpl(recipeSearchEdamam, presenter);
        RecipeSearchController controller = new RecipeSearchController(recipeSearchUseCase);
        recipeSearchView.setRecipeSearchController(controller);

        return this;
    }

    /**
     * Creates the RecipeSearchView and underlying ViewModel.
     * @return this builder
     */
    public RecipeAppBuilder addRecipeSearchView() {
        recipeSearchViewModel = new RecipeSearchViewModel();
        recipeSearchView = new RecipeSearchView(recipeSearchViewModel);
        return this;
    }

    /**
     * Builds the application.
     * @return the JFrame for the application
     */
    public JFrame build() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Recipe Search Application");
        frame.setSize(WIDTH, HEIGHT);

        frame.add(recipeSearchView);
        frame.setLocationRelativeTo(null); // Center on screen

        return frame;
    }
}