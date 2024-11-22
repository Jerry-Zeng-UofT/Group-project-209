package entity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The Recipe entity which contains ingredients and other details.
 */
public final class RecipeForSearch {

    private final String title;
    private final String description;
    private final List<Ingredient> ingredients;
    private final String instructions;

    /**
     * Constructor for RecipeForSearch.
     *
     * @param title       The title of the recipe.
     * @param description The description of the recipe.
     * @param ingredients The list of ingredients for the recipe.
     */
    public RecipeForSearch(String title, String description, List<Ingredient> ingredients) {
        this.title = title;
        this.description = description;
        this.ingredients = Collections.unmodifiableList(ingredients);
        this.instructions = "Instructions not available";
    }

    /**
     * Overloaded constructor for RecipeForSearch with instructions.
     */
    public RecipeForSearch(String title, String description, List<Ingredient> ingredients, String instructions) {
        this(title, description, ingredients);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    /**
     * Scale the quantities of all ingredients by the given factor.
     *
     * @param factor The scaling factor (e.g., 2.0 for doubling).
     * @return A new RecipeForSearch with scaled ingredient quantities.
     * @throws IllegalArgumentException if the scaling factor is negative.
     */
    public RecipeForSearch scaleIngredients(double factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("Scaling factor cannot be negative.");
        }

        List<Ingredient> scaledIngredients = ingredients.stream()
                .map(ingredient -> ingredient.scaleQuantity(factor))
                .toList();

        return new RecipeForSearch(title, description, scaledIngredients, instructions);
    }
}