package interface_adapter.meal_planning;

import entity.MealPlanEntry;
import entity.Recipe;
import use_case.meal_planning.MealPlanningOutputBoundary;
import java.util.List;

public class MealPlanningPresenter implements MealPlanningOutputBoundary {
    private final MealPlanningViewModel viewModel;

    public MealPlanningPresenter(MealPlanningViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentCalendarWeek(List<MealPlanEntry> entries) {
        MealPlanningState currentState = viewModel.getState();
        MealPlanningState newState = new MealPlanningState(currentState); // Copy constructor
        newState.setMealPlanEntries(entries);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentAddSuccess(String message) {
        MealPlanningState state = viewModel.getState();
        MealPlanningState newState = new MealPlanningState(state);
        newState.setMessage(message);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentRemoveSuccess(String message) {
        MealPlanningState state = viewModel.getState();
        MealPlanningState newState = new MealPlanningState(state);
        newState.setMessage(message);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentStatusUpdateSuccess(String message) {
        MealPlanningState state = viewModel.getState();
        MealPlanningState newState = new MealPlanningState(state);
        newState.setMessage(message);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentError(String error) {
        MealPlanningState state = viewModel.getState();
        MealPlanningState newState = new MealPlanningState(state);
        newState.setError(error);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentSavedRecipes(List<Recipe> recipes) {
        MealPlanningState currentState = viewModel.getState();
        MealPlanningState newState = new MealPlanningState(currentState);
        newState.setSavedRecipes(recipes);
        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }
}