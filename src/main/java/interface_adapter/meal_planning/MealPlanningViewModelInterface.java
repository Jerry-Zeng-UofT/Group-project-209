package interface_adapter.meal_planning;

public interface MealPlanningViewModelInterface {
    MealPlanningState getState();
    void setState(MealPlanningState state);
    void firePropertyChanged();
    void addPropertyChangeListener(java.beans.PropertyChangeListener listener);
}
