package use_case.search_with_restriction;

import entity.Recipe;
import java.util.List;

public interface SearchWithRestrictionOutputBoundry {
    /**
     * Present the recipes to the user.
     * @param recipes List of recipes to present
     */
    void presentRecipes(List<Recipe> recipes);

    /**
     * Present an error message to the user.
     * @param error Error message to present
     */
    void presentError(String error);
}