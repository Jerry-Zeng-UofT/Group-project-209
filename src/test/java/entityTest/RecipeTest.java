package entityTest;

import entity.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RecipeTest {

    private Recipe recipe;
    private Ingredient ingredient1;
    private Ingredient ingredient2;
    private Nutrition nutrition;
    private Food food1;
    private Food food2;

    @Before
    public void setUp() {
        // Create real instances of dependencies for testing
        ingredient1 = new Ingredient(1, "Sugar", 100, "g");
        ingredient2 = new Ingredient(2, "Flour", 200, "g");
        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);

        // Create real instance of Nutrition (assuming Nutrition is simple enough to use directly)
        nutrition = new Nutrition(200, 3.0, 5.0, 30.0, 4.0, 10.0);

        // Create real instances of Food
        food1 = new Food(1, "Apple", "Fruit", "g", 150, nutrition);
        food2 = new Food(2, "Banana", "Fruit", "g", 200, nutrition);
        List<Food> foods = Arrays.asList(food1, food2);

        // Create Recipe instance with real objects
        recipe = new Recipe(1, "Cake", "Delicious cake", ingredients, "Mix ingredients and bake", nutrition, foods, null, 4);
    }

    @Test
    public void testConstructor() {
        assertEquals(1, recipe.getRecipeId());
        assertEquals("Cake", recipe.getTitle());
        assertEquals("Delicious cake", recipe.getDescription());
        assertEquals(4, recipe.getServings());
        assertEquals(2, recipe.getIngredients().size());  // Ensure both ingredients are set
        assertEquals("Mix ingredients and bake", recipe.getInstructions());
        assertNotNull(recipe.getNutrition());  // Ensure nutrition is correctly injected
        assertEquals(2, recipe.getFood().size());  // Ensure foods are correctly injected
    }

    @Test
    public void testGettersAndSetters() {
        recipe.setRecipeId(2);
        recipe.setTitle("Chocolate Cake");
        recipe.setDescription("Rich chocolate cake");
        recipe.setServings(6);
        recipe.setInstructions("Mix and bake at 350 degrees");
        recipe.setIngredients(Arrays.asList(new Ingredient(3, "Cocoa", 50, "g")));
        recipe.setFood(Arrays.asList(new Food(3, "Cocoa", "Baking", "g", 50, nutrition)));
        recipe.setNutrition(new Nutrition(300, 4.0, 10.0, 40.0, 5.0, 15.0));

        assertEquals(2, recipe.getRecipeId());
        assertEquals("Chocolate Cake", recipe.getTitle());
        assertEquals("Rich chocolate cake", recipe.getDescription());
        assertEquals(6, recipe.getServings());
        assertEquals("Mix and bake at 350 degrees", recipe.getInstructions());
        assertEquals(1, recipe.getIngredients().size());  // Ensure new ingredient is set
        assertEquals(1, recipe.getFood().size());  // Ensure new food is set
        assertNotNull(recipe.getNutrition());  // Ensure nutrition is set
    }

    @Test
    public void testToString() {
        String expectedString = "Recipe: Cake (Servings: 4)\nDescription: Delicious cake\nIngredients: [Ingredient{id=1, name='Sugar', quantity=100.0, unit='g'}, Ingredient{id=2, name='Flour', quantity=200.0, unit='g'}]\nInstructions: Mix ingredients and bake";
        assertEquals(expectedString, recipe.toString());
    }

    @Test
    public void testNullDescription() {
        // Test if the description defaults correctly when null is passed
        Recipe recipeWithNullDescription = new Recipe(2, "Pancakes", null, Arrays.asList(ingredient1, ingredient2), "Mix and fry", nutrition, Arrays.asList(food1, food2), null, 2);
        assertEquals("Instructions not available", recipeWithNullDescription.getDescription());
    }

    @Test
    public void testEmptyDescription() {
        // Test if the description defaults correctly when an empty string is passed
        Recipe recipeWithEmptyDescription = new Recipe(2, "Pancakes", "", Arrays.asList(ingredient1, ingredient2), "Mix and fry", nutrition, Arrays.asList(food1, food2), null, 2);
        assertEquals("Instructions not available", recipeWithEmptyDescription.getDescription());
    }

    @Test
    public void testSetValidInstructions() {
        // Setting valid instructions
        recipe.setInstructions("Bake at 180 degrees");
        assertEquals("Bake at 180 degrees", recipe.getInstructions());
    }

    @Test
    public void testSetInvalidInstructions() {
        // Test that setting null instructions is allowed (based on current implementation)
        recipe.setInstructions(null);
        assertNull(recipe.getInstructions());
    }

}