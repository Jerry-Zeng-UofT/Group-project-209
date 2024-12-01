package data_access;

import entity.Nutrient;
import entity.Recipe;
import okhttp3.Request;

import java.io.IOException;
import java.util.List;

public interface NutritionAnalysisDataAccess {
    /**
     * Get totalNutrients from a POST request from the API.
     *
     * @param recipe the recipe we want to analyze the nutrition of.
     * @return A list of Nutrient entity
     * @throws IOException If an I/O error occurs.
     */
    List<Nutrient> analyzeNutrition(Recipe recipe) throws IOException;
    // Helper methods are private and are not in the interface according to
    // the encapsulation principle and interface Segregation (from SOLID).
}
