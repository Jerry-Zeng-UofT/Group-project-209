package app;

import javax.swing.*;
import data_access.*;
import interface_adapter.recipe_search.*;
import interface_adapter.meal_planning.*;
import use_case.recipe_search.*;
import use_case.meal_planning.*;
import view.*;

/**
 * Builder class for the RecipeApp.
 */
public class RecipeAppBuilder {
    public static final int HEIGHT = 600;
    public static final int WIDTH = 800;

    private RecipeSearchEdamam recipeSearchEdamam;
    private SavedRecipesDataAccess savedRecipesDataAccess;
    private MealPlanningDataAccess mealPlanningDataAccess;

    private RecipeSearchViewModel recipeSearchViewModel;
    private MealPlanningViewModel mealPlanningViewModel;

    private RecipeSearchView recipeSearchView;
    private MealPlanningView mealPlanningView;

    private RecipeSearch recipeSearchUseCase;
    private MealPlanning mealPlanningUseCase;

    /**
     * Add the recipe search API.
     * @param recipeSearchEdamam The recipe search API
     * @return The builder instance
     */
    public RecipeAppBuilder addRecipeSearchAPI(RecipeSearchEdamam recipeSearchEdamam) {
        this.recipeSearchEdamam = recipeSearchEdamam;
        this.savedRecipesDataAccess = new SavedRecipesDataAccessObject();
        this.mealPlanningDataAccess = new MealPlanningDataAccessObject(this.savedRecipesDataAccess);
        return this;
    }

    /**
     * Add the meal planning view.
     * @return The builder instance
     */
    public RecipeAppBuilder addMealPlanningView() {
        mealPlanningViewModel = new MealPlanningViewModel();
        mealPlanningView = new MealPlanningView(mealPlanningViewModel);
        return this;
    }

    /**
     * Add the recipe search view.
     * @return The builder instance
     */
    public RecipeAppBuilder addRecipeSearchView() {
        // Make sure mealPlanningView is created before this
        if (mealPlanningView == null) {
            throw new RuntimeException("addMealPlanningView must be called before addRecipeSearchView");
        }

        recipeSearchViewModel = new RecipeSearchViewModel();
        recipeSearchView = new RecipeSearchView(recipeSearchViewModel);
        recipeSearchView.setMealPlanningView(mealPlanningView);
        return this;
    }

    /**
     * Add the meal planning use case.
     * @return The builder instance
     */
    public RecipeAppBuilder addMealPlanningUseCase() {
        if (mealPlanningView == null) {
            throw new RuntimeException("addMealPlanningView must be called before addMealPlanningUseCase");
        }

        MealPlanningPresenter presenter = new MealPlanningPresenter(mealPlanningViewModel);
        mealPlanningUseCase = new MealPlanningInteractor(
                mealPlanningDataAccess,
                savedRecipesDataAccess,
                presenter
        );
        MealPlanningController controller = new MealPlanningController(mealPlanningUseCase);
        mealPlanningView.setController(controller);

        return this;
    }

    /**
     * Add the recipe search use case.
     * @return The builder instance
     */
    public RecipeAppBuilder addRecipeSearchUseCase() {
        if (recipeSearchView == null) {
            throw new RuntimeException("addRecipeSearchView must be called before addRecipeSearchUseCase");
        }

        RecipeSearchPresenter presenter = new RecipeSearchPresenter(recipeSearchViewModel);
        recipeSearchUseCase = new RecipeSearchImpl(
                recipeSearchEdamam,
                savedRecipesDataAccess,
                presenter
        );
        RecipeSearchController controller = new RecipeSearchController(recipeSearchUseCase);
        recipeSearchView.setRecipeSearchController(controller);

        return this;
    }

    /**
     * Get the saved recipes data access.
     * @return The saved recipes data access
     */
    public SavedRecipesDataAccess getSavedRecipesDataAccess() {
        return savedRecipesDataAccess;
    }

    /**
     * Get the meal planning data access.
     * @return The meal planning data access
     */
    public MealPlanningDataAccess getMealPlanningDataAccess() {
        return mealPlanningDataAccess;
    }

    /**
     * Build the main frame.
     * @return The main frame
     */
    public JFrame build() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Recipe Wiz");
        frame.setSize(WIDTH, HEIGHT);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Recipe Search", recipeSearchView);
        tabbedPane.addTab("Meal Planning", mealPlanningView);

        frame.add(tabbedPane);
        frame.setLocationRelativeTo(null);
        return frame;
    }
}