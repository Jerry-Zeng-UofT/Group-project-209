package entity;

import java.util.List;

/**
 * The class represents the NutritionInfo for food, in form of the details of each Nutrient.
 */
public class Nutrient {

    private List<String> nutrients;

    public Nutrient(List<String> nutrients) {
        this.nutrients = nutrients;
    }

    public List<String> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<String> nutrients) {
        this.nutrients = nutrients;
    }
}
