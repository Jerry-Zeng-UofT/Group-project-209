package entity;

/**
 * The class represents the NutritionInfo for food, in form of the details of each Nutrient.
 */
public class Nutrient {

    private String nutrientInfo;

    public Nutrient(String nutrients) {
        this.nutrientInfo = nutrients;
    }

    public String getNutrients() {
        return nutrientInfo;
    }

    public void setNutrients(String nutrients) {
        this.nutrientInfo = nutrients;
    }
}
