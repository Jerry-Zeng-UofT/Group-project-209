package interface_adapter.recipe_search;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Arrays;

/**
 * A ViewModel for managing servings.
 */
public class ServingsViewModel {
    private int servings = 1;
    private final List<Integer> availableServings = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    private final PropertyChangeSupport pcs;

    public ServingsViewModel() {
        this.pcs = new PropertyChangeSupport(this);
    }

    // Getter for current servings
    public int getServings() {
        return servings;
    }

    // Setter for servings with PropertyChangeSupport
    public void setServings(int newServings) {
        int oldServings = this.servings;
        this.servings = newServings;
        pcs.firePropertyChange("servings", oldServings, newServings);
    }

    // Getter for available servings
    public List<Integer> getAvailableServings() {
        return availableServings;
    }

    // Methods to add and remove property change listeners
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
