package app;

import javax.swing.*;
import data_access.*;

import interface_adapter.nutrition_analysis.NutritionAnalysisController;
import interface_adapter.nutrition_analysis.NutritionAnalysisPresenter;
import interface_adapter.nutrition_analysis.NutritionAnalysisViewModel;
import interface_adapter.recipe_search.*;
import interface_adapter.meal_planning.*;
import interface_adapter.search_with_restriction.RestrictionController;
import interface_adapter.search_with_restriction.RestrictionPresenter;
import interface_adapter.search_with_restriction.RestrictionViewModel;
import interface_adapter.serving_adjust.ServingAdjustController;
import interface_adapter.serving_adjust.ServingAdjustPresenter;
import interface_adapter.serving_adjust.ServingAdjustViewModel;
import use_case.nutrition_analysis.NutritionAnalysis;
import use_case.nutrition_analysis.NutritionAnalysisInteractor;
import use_case.recipe_search.*;
import use_case.meal_planning.*;
import use_case.search_with_restriction.RecipeSearchWithRestrictionInteractor;
import use_case.search_with_restriction.SearchWithRestrictionInputBoundary;
import use_case.serving_adjust.ServingAdjustInputBoundary;
import use_case.serving_adjust.ServingAdjustInteractor;
import use_case.serving_adjust.ServingAdjustOutputBoundary;
import view.*;

/**
 * Builder class for the RecipeApp.
 */
public class RecipeAppBuilder {
    public static final int HEIGHT = 600;
    public static final int WIDTH = 800;

    private RecipeSearchDataAccessObject recipeSearchDataAccessObject;
    private SearchWithRestrictionDataAccessObject searchWithRestrictionDataAccessObject;
    private SavedRecipesDataAccess savedRecipesDataAccess;
    private MealPlanningDataAccess mealPlanningDataAccess;
    private NutritionAnalysisDataAccessObject nutritionAnalysisDataAccess;

    private RecipeSearchViewModel recipeSearchViewModel;
    private RestrictionViewModel restrictionViewModel;
    private MealPlanningViewModel mealPlanningViewModel;
    private NutritionAnalysisViewModel nutritionAnalysisViewModel;

    private FrontPageView frontPageView;
    private RecipeSearchView recipeSearchView;
    private MealPlanningView mealPlanningView;
    private NutritionAnalysisView nutritionAnalysisView;

    private RecipeSearchInputBoundary recipeSearchInputBoundaryUseCase;
    private SearchWithRestrictionInputBoundary searchWithRestrictionInputBoundaryUseCase;
    private MealPlanningInputBoundary mealPlanningInputBoundaryUseCase;
    private NutritionAnalysis nutritionAnalysisUseCase;

    /**
     * Add the recipe search API.
     * @param recipeSearchDataAccessObject The recipe search API
     * @param searchWithRestrictionDataAccessObject The restriction search API
     * @return The builder instance
     */
    public RecipeAppBuilder addRecipeSearchAPI(RecipeSearchDataAccessObject recipeSearchDataAccessObject, SearchWithRestrictionDataAccessObject searchWithRestrictionDataAccessObject) {
        this.recipeSearchDataAccessObject = recipeSearchDataAccessObject;
        this.searchWithRestrictionDataAccessObject = searchWithRestrictionDataAccessObject;
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
        restrictionViewModel = new RestrictionViewModel();

        RecipeSearchPresenter presenter = new RecipeSearchPresenter(recipeSearchViewModel);
        recipeSearchInputBoundaryUseCase = new RecipeSearchInteractor(recipeSearchDataAccessObject, savedRecipesDataAccess, presenter);
        RecipeSearchController controller = new RecipeSearchController(recipeSearchInputBoundaryUseCase);

        RestrictionPresenter restrictionPresenter = new RestrictionPresenter(restrictionViewModel);
        searchWithRestrictionInputBoundaryUseCase = new RecipeSearchWithRestrictionInteractor(searchWithRestrictionDataAccessObject, restrictionPresenter);
        RestrictionController restrictionController = new RestrictionController(searchWithRestrictionInputBoundaryUseCase);

        ServingAdjustViewModel servingAdjustViewModel = new ServingAdjustViewModel();
        ServingAdjustPresenter servingAdjustPresenter = new ServingAdjustPresenter(servingAdjustViewModel);
        ServingAdjustInputBoundary servingAdjustUseCase = new ServingAdjustInteractor(servingAdjustPresenter);
        ServingAdjustController servingAdjustController = new ServingAdjustController(servingAdjustUseCase);

        recipeSearchView = new RecipeSearchView(recipeSearchViewModel, restrictionViewModel, controller,
                restrictionController, servingAdjustController);
        recipeSearchView.setMealPlanningView(mealPlanningView);
        recipeSearchView.setNutritionAnalysisView(nutritionAnalysisView);

        return this;
    }

    /**
     * Add the front page view.
     */
    public void addFrontpageView() {
        frontPageView = new FrontPageView(recipeSearchView, mealPlanningView);
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
        mealPlanningInputBoundaryUseCase = new MealPlanningInteractor(
                mealPlanningDataAccess,
                savedRecipesDataAccess,
                presenter
        );
        MealPlanningController controller = new MealPlanningController(mealPlanningInputBoundaryUseCase);
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
        nutritionAnalysisUseCase = new NutritionAnalysisInteractor(
                nutritionAnalysisDataAccess,
                presenter
        );
        NutritionAnalysisController controller = new NutritionAnalysisController(nutritionAnalysisUseCase);
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
        recipeSearchInputBoundaryUseCase = new RecipeSearchInteractor(
                recipeSearchDataAccessObject,
                savedRecipesDataAccess,
                presenter
        );
        RecipeSearchController controller = new RecipeSearchController(recipeSearchInputBoundaryUseCase);
        recipeSearchView.setRecipeSearchController(controller);

        return this;
    }

    /**
     * Add the recipe search with restriction use case.
     * @return The builder instance
     */
    public RecipeAppBuilder addRestrictionSearchUseCase() {
        if (recipeSearchView == null) {
            throw new RuntimeException("addRecipeSearchView must be called before addRecipeSearchUseCase");
        }

        RestrictionPresenter restrictionPresenter = new RestrictionPresenter(restrictionViewModel);
        searchWithRestrictionInputBoundaryUseCase = new RecipeSearchWithRestrictionInteractor(
                searchWithRestrictionDataAccessObject,
                restrictionPresenter
        );
        RestrictionController controller = new RestrictionController(searchWithRestrictionInputBoundaryUseCase);
        recipeSearchView.setRestrictionController(controller);

        return this;
    }

    /**
     * Add the serving adjust use case.
     * @return The builder instance
     */
    public RecipeAppBuilder addServingAdjustUseCase() {
        ServingAdjustViewModel servingAdjustViewModel = new ServingAdjustViewModel();

        ServingAdjustOutputBoundary presenter = new ServingAdjustPresenter(servingAdjustViewModel);
        ServingAdjustInputBoundary servingAdjustUseCase = new ServingAdjustInteractor(presenter);

        ServingAdjustController controller = new ServingAdjustController(servingAdjustUseCase);

        if (recipeSearchView == null) {
            throw new RuntimeException("addRecipeSearchView must be called before addServingAdjustUseCase");
        }

        recipeSearchView.setServingAdjustController(controller);
        recipeSearchView.setServingAdjustViewModel(servingAdjustViewModel);

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
        return frontPageView;
    }
}