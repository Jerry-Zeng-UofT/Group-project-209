package use_caseTest;

import data_access.NutritionAnalysisDataAccess;
import data_access.NutritionAnalysisDataAccessObject;
import entity.*;
import interface_adapter.nutrition_analysis.NutritionAnalysisPresenter;
import interface_adapter.nutrition_analysis.NutritionAnalysisState;
import interface_adapter.nutrition_analysis.NutritionAnalysisViewModel;
import org.junit.Before;
import org.junit.Test;
import use_case.nutrition_analysis.NutritionAnalysisImpl;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NutritionAnalysisImplTest {

    private NutritionAnalysisImpl nutritionAnalysis;
    private NutritionAnalysisDataAccessObject mockDAO;
    private NutritionAnalysisPresenter presenter;
    private NutritionAnalysisViewModel viewModel;
    private Recipe recipe;

    @Before
    public void setUp() {

        // Initialize necessary components
        mockDAO = new NutritionAnalysisDataAccessObject(); // Custom implementation
        viewModel = new NutritionAnalysisViewModel();
        presenter = new NutritionAnalysisPresenter(viewModel);
        nutritionAnalysis = new NutritionAnalysisImpl(mockDAO, presenter);

        // Create real instances of dependencies for testing
        Ingredient ingredient1 = new Ingredient(1, "Sugar", 100, "g");
        Ingredient ingredient2 = new Ingredient(2, "Flour", 200, "g");
        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);

        // Create real instance of Nutrition (assuming Nutrition is simple enough to use directly)
        Nutrition nutrition = new Nutrition(200, 3.0, 5.0, 30.0, 4.0, 10.0);

        // Create real instances of Food
        Food food1 = new Food(1, "Apple", "Fruit", "g", 150, nutrition);
        Food food2 = new Food(2, "Banana", "Fruit", "g", 200, nutrition);
        List<Food> foods = Arrays.asList(food1, food2);

        // Create Recipe instance with real objects
        recipe = new Recipe(1, "Cake", "Delicious cake", ingredients, "Mix ingredients and bake", nutrition, foods, null, 4);
    }

    @Test
    public void testAnalyzeNutrition_Success() throws Exception {

        // Prepare test data
        Nutrient nutrient1 = new Nutrient("Vitamin B");
        Nutrient nutrient2 = new Nutrient("Iron");


        List<Nutrient> nutritionInfo = Arrays.asList(nutrient1, nutrient2);

        // Call the method
        nutritionAnalysis.analyzeNutrition(recipe);

        // Get the state from the viewModel
        Object state = viewModel.getState();

        // Ensure that the state is of type NutritionAnalysisState
        assertTrue("Expected state to be an instance of NutritionAnalysisState", state instanceof NutritionAnalysisState);
        NutritionAnalysisState nutritionState = (NutritionAnalysisState) state;

        // Assert the result
        assertNotNull(nutritionState);
        assertEquals(2, nutritionState.getNutritionResults().size());
        assertTrue(nutritionState.getNutritionResults().contains("Vitamin B"));
        assertTrue(nutritionState.getNutritionResults().contains("Iron"));
    }

    @Test
    public void testAnalyzeNutrition_EmptyList() throws Exception {

        // Call the method
        nutritionAnalysis.analyzeNutrition(recipe);

        // Get the state from the viewModel
        Object state = viewModel.getState();

        // Ensure that the state is of type NutritionAnalysisState
        assertTrue("Expected state to be an instance of NutritionAnalysisState", state instanceof NutritionAnalysisState);
        NutritionAnalysisState nutritionState = (NutritionAnalysisState) state;

        // Assert the result (empty list case)
        assertNotNull(nutritionState);
        assertTrue(nutritionState.getNutritionResults().isEmpty());
    }
}