package use_caseTest.serving_adjust;

import entity.Ingredient;
import entity.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.serving_adjust.ServingAdjustInteractor;
import use_case.serving_adjust.ServingAdjustOutputBoundary;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ServingAdjustInteractor without using Mockito.
 */
class ServingAdjustInteractorTest {

    private TestServingAdjustOutputBoundary outputBoundary;
    private ServingAdjustInteractor interactor;

    @BeforeEach
    public void setUp() {

        outputBoundary = new TestServingAdjustOutputBoundary();

        interactor = new ServingAdjustInteractor(outputBoundary);
    }

    @Test
    void testAdjustServings_singleRecipe() {

        Ingredient ingredient1 = new Ingredient(1, "Flour", 200, "grams");
        Ingredient ingredient2 = new Ingredient(2, "Sugar", 100, "grams");
        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);

        Recipe recipe = new Recipe(
                1,
                "Cake",
                "Delicious cake",
                ingredients,
                "Mix ingredients and bake.",
                null,
                null,
                null,
                4
        );

        interactor.adjustServings(8, recipe);

        assertEquals(8, recipe.getServings());
        assertEquals(400, ingredient1.getQuantity());
        assertEquals(200, ingredient2.getQuantity());
    }

    @Test
    void testAdjustServings_invalidServings() {

        Ingredient ingredient = new Ingredient(1, "Salt", 10, "grams");
        List<Ingredient> ingredients = Collections.singletonList(ingredient);

        Recipe recipe = new Recipe(
                2,
                "Soup",
                "Tasty soup",
                ingredients,
                "Boil water and add salt.",
                null,
                null,
                null,
                2
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> interactor.adjustServings(0, recipe)
        );
        assertEquals("Servings must be greater than zero.", exception.getMessage());
    }

    @Test
   void testAdjustServings_multipleRecipes() {

        Ingredient ingredient1 = new Ingredient(1, "Pasta", 100, "grams");
        Recipe recipe1 = new Recipe(
                3,
                "Spaghetti",
                "Classic spaghetti",
                Collections.singletonList(ingredient1),
                "Cook pasta.",
                null,
                null,
                null,
                2
        );

        Ingredient ingredient2 = new Ingredient(2, "Rice", 200, "grams");
        Recipe recipe2 = new Recipe(
                4,
                "Fried Rice",
                "Delicious fried rice",
                Collections.singletonList(ingredient2),
                "Fry rice.",
                null,
                null,
                null,
                4
        );

        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);

        interactor.adjustServingsForMultiple(8, recipes);

        assertEquals(8, recipe1.getServings());
        assertEquals(400, ingredient1.getQuantity());

        assertEquals(8, recipe2.getServings());
        assertEquals(400, ingredient2.getQuantity());

        assertTrue(outputBoundary.wasCalled);
        assertEquals(recipes, outputBoundary.updatedRecipes);
    }

    @Test
    void testAdjustServings_listMethod() {

        Ingredient ingredient1 = new Ingredient(1, "Tomato", 2, "pieces");
        Ingredient ingredient2 = new Ingredient(2, "Lettuce", 1, "head");
        Recipe recipe1 = new Recipe(
                5,
                "Salad",
                "Fresh salad",
                Arrays.asList(ingredient1, ingredient2),
                "Chop and mix.",
                null,
                null,
                null,
                2
        );

        Ingredient ingredient3 = new Ingredient(3, "Bread", 4, "slices");
        Recipe recipe2 = new Recipe(
                6,
                "Sandwich",
                "Ham sandwich",
                Collections.singletonList(ingredient3),
                "Assemble ingredients.",
                null,
                null,
                null,
                2
        );

        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);

        interactor.adjustServings(4, recipes);

        assertEquals(4, recipe1.getServings());
        assertEquals(4, ingredient1.getQuantity()); // 2 * (4/2) = 4
        assertEquals(2, ingredient2.getQuantity()); // 1 * (4/2) = 2

        assertEquals(4, recipe2.getServings());
        assertEquals(8, ingredient3.getQuantity()); // 4 * (4/2) = 8

        assertTrue(outputBoundary.wasCalled);
        assertEquals(recipes, outputBoundary.updatedRecipes);
    }

    @Test
    void testAdjustServings_listMethod_invalidServings() {

        Ingredient ingredient = new Ingredient(1, "Milk", 500, "ml");
        Recipe recipe = new Recipe(
                7,
                "Smoothie",
                "Fruit smoothie",
                Collections.singletonList(ingredient),
                "Blend ingredients.",
                null,
                null,
                null,
                2
        );

        List<Recipe> recipes = Collections.singletonList(recipe);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> interactor.adjustServings(0, recipes)
        );
        assertEquals("Servings must be greater than zero.", exception.getMessage());
    }

    /**
     * Test implementation of ServingAdjustOutputBoundary for capturing method calls.
     */
    private static class TestServingAdjustOutputBoundary implements ServingAdjustOutputBoundary {
        public boolean wasCalled = false;
        public List<Recipe> updatedRecipes = null;

        @Override
        public void presentUpdatedRecipes(List<Recipe> recipes) {
            this.wasCalled = true;
            this.updatedRecipes = recipes;
        }
    }
}