package interface_adapter.meal_planning;

import interface_adapter.ViewModel;

public class MealPlanningViewModel extends ViewModel {
    public static final String TITLE_LABEL = "Meal Planning Calendar";
    public static final String ADD_BUTTON_LABEL = "Add to Calendar";
    public static final String REMOVE_BUTTON_LABEL = "Remove from Calendar";
    public static final String NEXT_WEEK_BUTTON_LABEL = "Next Week";
    public static final String PREV_WEEK_BUTTON_LABEL = "Previous Week";

    public MealPlanningViewModel() {
        super("Meal Planning Calendar");
    }
}
