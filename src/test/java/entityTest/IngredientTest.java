package entityTest;

import entity.Ingredient;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IngredientTest {

    private Ingredient ingredient;

    // Set up a default Ingredient object before each test
    @Before
    public void setUp() {
        ingredient = new Ingredient(1, "Sugar", 100.0, "grams");
    }

    @Test
    public void testConstructor_validInput() {
        // Check that the object is created successfully with valid parameters
        assertNotNull(ingredient);
        assertEquals(1, ingredient.getIngredientId());
        assertEquals("Sugar", ingredient.getName());
        assertEquals(100.0, ingredient.getQuantity(), 0.01);
        assertEquals("grams", ingredient.getUnit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_invalidQuantity() {
        // Test invalid quantity (negative)
        new Ingredient(2, "Salt", -50.0, "grams");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullName() {
        // Test null name
        new Ingredient(3, null, 100.0, "grams");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_emptyName() {
        // Test empty name
        new Ingredient(4, "", 100.0, "grams");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullUnit() {
        // Test null unit
        new Ingredient(5, "Flour", 100.0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_emptyUnit() {
        // Test empty unit
        new Ingredient(6, "Flour", 100.0, "");
    }

    @Test
    public void testGetIngredientId() {
        assertEquals(1, ingredient.getIngredientId());
    }

    @Test
    public void testGetName() {
        assertEquals("Sugar", ingredient.getName());
    }

    @Test
    public void testGetQuantity() {
        assertEquals(100.0, ingredient.getQuantity(), 0.01);
    }

    @Test
    public void testGetUnit() {
        assertEquals("grams", ingredient.getUnit());
    }

    @Test
    public void testSetQuantity() {
        // Test setting the quantity to a new value
        ingredient.setQuantity(200.0);
        assertEquals(200.0, ingredient.getQuantity(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScaleQuantity_invalidFactor() {
        // Test scaling with a negative factor
        ingredient.scaleQuantity(-2.0);
    }

    @Test
    public void testScaleQuantity_validFactor() {
        // Test scaling with a valid factor
        Ingredient scaledIngredient = ingredient.scaleQuantity(2.0);
        assertEquals(200.0, scaledIngredient.getQuantity(), 0.01);
    }

    @Test
    public void testScaleQuantity_noChange() {
        // Test scaling with a factor of 1.0 (no change)
        Ingredient scaledIngredient = ingredient.scaleQuantity(1.0);
        assertEquals(100.0, scaledIngredient.getQuantity(), 0.01);
    }

    @Test
    public void testScaleQuantity_sameIngredient() {
        // Ensure that the scaled ingredient is a new object, not the same instance
        Ingredient scaledIngredient = ingredient.scaleQuantity(2.0);
        assertNotSame(ingredient, scaledIngredient);  // Verifying they are different objects
    }

    @Test
    public void testScaleQuantity_zeroFactor() {
        Ingredient scaledIngredient = ingredient.scaleQuantity(0.0);
        assertEquals(0.0, scaledIngredient.getQuantity(), 0.01);
    }

    @Test
    public void testLargeQuantity() {
        Ingredient largeIngredient = new Ingredient(1000000, "Large Ingredient", 1e6, "grams");
        assertEquals(1e6, largeIngredient.getQuantity(), 0.01);
    }

    @Test
    public void testScaleQuantity_largeFactor() {
        Ingredient scaledIngredient = ingredient.scaleQuantity(1e6);
        assertEquals(100.0 * 1e6, scaledIngredient.getQuantity(), 0.01);
    }
}

