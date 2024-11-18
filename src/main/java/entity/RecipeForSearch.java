package entity;

import java.util.List;

/**
 * The Recipe entity which contains ingredients and other details.
 */
public class RecipeForSearch {

    private String title;
    private String description;
    private List<String> ingredients;
    private String instructions;

    public RecipeForSearch(String title, String description, List<String> ingredients) {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = "Instructions not available";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
