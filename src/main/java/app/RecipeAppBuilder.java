package app;

import javax.swing.*;
import data_access.*;

import interface_adapter.nutrition_analysis.NutritionAnalysisController;
import interface_adapter.nutrition_analysis.NutritionAnalysisPresenter;
import interface_adapter.nutrition_analysis.NutritionAnalysisViewModel;
import interface_adapter.recipe_search.*;
import interface_adapter.meal_planning.*;
import use_case.nutrition_analysis.NutritionAnalysis;
import use_case.nutrition_analysis.NutritionAnalysisImpl;
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
    private NutritionAnalysisDataAccessObject nutritionAnalysisDataAccess;

    private RecipeSearchViewModel recipeSearchViewModel;
    private MealPlanningViewModel mealPlanningViewModel;
    private NutritionAnalysisViewModel nutritionAnalysisViewModel;

    private RecipeSearchView recipeSearchView;
    private MealPlanningView mealPlanningView;
    private NutritionAnalysisView nutritionAnalysisView;

    private RecipeSearch recipeSearchUseCase;
    private MealPlanning mealPlanningUseCase;
    private NutritionAnalysis nutritionAnalysisUseCase;

    /**
     * Add the recipe search API.
     * @param recipeSearchEdamam The recipe search API
     * @return The builder instance
     */
    public RecipeAppBuilder addRecipeSearchAPI(RecipeSearchEdamam recipeSearchEdamam) {
        this.recipeSearchEdamam = recipeSearchEdamam;
        this.savedRecipesDataAccess = new SavedRecipesDataAccessObject();
        this.mealPlanningDataAccess = new MealPlanningDataAccessObject(this.savedRecipesDataAccess);
        this.nutritionAnalysisDataAccess = new NutritionAnalysisDataAccessObject();
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
     * Add the meal planning view.
     * @return The builder instance
     */
    public RecipeAppBuilder addNutritionAnalysisView() {
        nutritionAnalysisViewModel = new NutritionAnalysisViewModel();
        nutritionAnalysisView = new NutritionAnalysisView(nutritionAnalysisViewModel);
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
        if (nutritionAnalysisView == null) {
            throw new RuntimeException("addNutritionAnalysisView must be called before addRecipeSearchView");
        }

        recipeSearchViewModel = new RecipeSearchViewModel();
        recipeSearchView = new RecipeSearchView(recipeSearchViewModel);
        recipeSearchView.setMealPlanningView(mealPlanningView);
        recipeSearchView.setNutritionAnalysisView(nutritionAnalysisView);

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
     * Add the Nutrition Analysis Use Case.
     * @return The builder instance
     */
    public RecipeAppBuilder addNutritionAnalysisUseCase() {
        if (nutritionAnalysisView == null) {
            throw new RuntimeException("addNutritionAnalysisView must be called before addNutritionAnalysisUseCase");
        }

        NutritionAnalysisPresenter presenter = new NutritionAnalysisPresenter(nutritionAnalysisViewModel);
        nutritionAnalysisUseCase = new NutritionAnalysisImpl(
                nutritionAnalysisDataAccess,
                presenter
        );
        NutritionAnalysisController controller = new NutritionAnalysisController(nutritionAnalysisUseCase);
        nutritionAnalysisView.setController(controller);
        recipeSearchView.setNutritionAnalysisController(controller);

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
     * Get the Nutrition Analysis data access.
     * @return The Nutrition Analysis data access
     */
    public NutritionAnalysisDataAccessObject getNutritionAnalysisDataAccess() {
        return nutritionAnalysisDataAccess;
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