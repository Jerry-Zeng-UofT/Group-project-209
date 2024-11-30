package view;

import interface_adapter.meal_planning.*;
import entity.MealPlanEntry;
import entity.Recipe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * View for the meal planning use case.
 */
public class MealPlanningView extends JPanel implements ActionListener, PropertyChangeListener {
    // Constants for layout
    private static final int VERTICAL_GAP = 10;
    private static final int HORIZONTAL_GAP = 10;
    private static final int CALENDAR_COLUMNS = 7;
    private static final int TITLE_FONT_SIZE = 16;
    private static final int SAVED_RECIPES_WIDTH = 250;
    private static final String[] MEAL_TYPES = {"Breakfast", "Lunch", "Dinner", "Snack"};
    private static final String[] DAY_NAMES = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static final String[] MEAL_STATUSES = {"Planned", "In Progress", "Completed"};

    // UI Components
    private final MealPlanningViewModel viewModel;
    private MealPlanningController controller;
    private final JPanel calendarPanel;
    private final JButton prevWeekButton;
    private final JButton nextWeekButton;
    private final JLabel weekLabel;
    private final JComboBox<String> mealTypeCombo;
    private final DefaultListModel<Recipe> savedRecipesModel;
    private final JList<Recipe> savedRecipesList;
    private final JLabel titleLabel;
    private final JPanel contentPanel;
    private final JPanel savedRecipesPanel;

    /**
     * Constructor for MealPlanningView.
     * @param viewModel The view model for the meal planning use case
     */
    public MealPlanningView(MealPlanningViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        // Initialize all final components
        this.calendarPanel = new JPanel(new GridLayout(0, CALENDAR_COLUMNS, 5, 5));
        this.prevWeekButton = new JButton("Previous Week");
        this.nextWeekButton = new JButton("Next Week");
        this.weekLabel = new JLabel();
        this.mealTypeCombo = new JComboBox<>(MEAL_TYPES);
        this.savedRecipesModel = new DefaultListModel<>();
        this.savedRecipesList = new JList<>(savedRecipesModel);
        this.contentPanel = new JPanel(new BorderLayout(HORIZONTAL_GAP, VERTICAL_GAP));
        this.titleLabel = createTitleLabel();
        this.savedRecipesPanel = createSavedRecipesPanel();

        // Set main layout
        setLayout(new BorderLayout(HORIZONTAL_GAP, VERTICAL_GAP));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create navigation panel
        JPanel navigationPanel = createNavigationPanel();

        // Configure calendar panel
        calendarPanel.setBorder(BorderFactory.createEtchedBorder());

        // Setup main layout
        contentPanel.add(navigationPanel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(calendarPanel), BorderLayout.CENTER);

        // Add all components to main panel
        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(savedRecipesPanel, BorderLayout.EAST);

        // Initialize calendar with current week
        MealPlanningState initialState = new MealPlanningState();
        initialState.setCurrentWeekStart(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1));
        viewModel.setState(initialState);
        refreshCalendar();
    }

    /**
     * Create the title label for the view.
     * @return The title label
     */
    private JLabel createTitleLabel() {
        JLabel label = new JLabel(viewModel.getViewName());
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, TITLE_FONT_SIZE));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /**
     * Create the navigation panel for the view.
     * @return The navigation panel
     */
    private JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        prevWeekButton.addActionListener(this);
        nextWeekButton.addActionListener(this);

        navigationPanel.add(prevWeekButton);
        navigationPanel.add(weekLabel);
        navigationPanel.add(nextWeekButton);

        return navigationPanel;
    }

    /**
     * Create the saved recipes panel for the view.
     * @return The saved recipes panel
     */
    private JPanel createSavedRecipesPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Saved Recipes"));
        panel.setPreferredSize(new Dimension(SAVED_RECIPES_WIDTH, 0));

        savedRecipesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        savedRecipesList.setCellRenderer(new RecipeListCellRenderer(controller));

        // Create panel for controls
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.add(new JLabel("Meal Type:"));
        controlsPanel.add(mealTypeCombo);

        // Add components
        panel.add(controlsPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(savedRecipesList), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Create the meal panel for the view.
     * @param entry The meal plan entry
     * @return The meal panel
     */
    private JPanel createMealPanel(MealPlanEntry entry) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Recipe name
        JLabel nameLabel = new JLabel(entry.getRecipe().getTitle());
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Meal type
        JLabel typeLabel = new JLabel(entry.getMealType());
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Status combo box
        JComboBox<String> statusCombo = new JComboBox<>(MEAL_STATUSES);
        statusCombo.setSelectedItem(capitalizeFirstLetter(entry.getStatus()));
        statusCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusCombo.addActionListener(e -> {
            if (controller != null) {
                String newStatus = ((String) statusCombo.getSelectedItem()).toLowerCase();
                controller.updateMealStatus(getCurrentUserId(), entry.getEntryId(), newStatus);
            }
        });

        // Remove button
        JButton removeButton = new JButton("×");
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.setActionCommand("REMOVE_" + entry.getEntryId());
        removeButton.addActionListener(this);

        // Add components
        panel.add(nameLabel);
        panel.add(typeLabel);
        panel.add(statusCombo);
        panel.add(removeButton);

        // Set background color based on status
        panel.setBackground(getStatusColor(entry.getStatus()));

        return panel;
    }

    /**
     * Get the color based on the status.
     * @param status The status
     * @return The color
     */
    private Color getStatusColor(String status) {
        String statusLower = status.toLowerCase();
        if (statusLower.equals("completed")) {
            return new Color(200, 255, 200); // Light green
        } else if (statusLower.equals("in progress")) {
            return new Color(255, 255, 200); // Light yellow
        } else {
            return new Color(255, 255, 255); // White for planned
        }
    }

    /**
     * Capitalize the first letter of a string.
     * @param input The input string
     * @return The capitalized string
     */
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    /**
     * Create the day panel for the view.
     * @param date The date
     * @return The day panel
     */
    private JPanel createDayPanel(LocalDate date) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEtchedBorder());

        // Add date label with current date highlight
        JLabel dateLabel = new JLabel(date.format(DateTimeFormatter.ofPattern("MM/dd")));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (date.equals(LocalDate.now())) {
            dateLabel.setForeground(Color.BLUE);
            dateLabel.setFont(dateLabel.getFont().deriveFont(Font.BOLD));
        }
        panel.add(dateLabel);

        // Add meals for this day
        MealPlanningState state = viewModel.getState();
        if (state != null) {
            for (MealPlanEntry entry : state.getMealPlanEntries()) {
                if (entry.getDate().equals(date)) {
                    panel.add(createMealPanel(entry));
                }
            }
        }

        // Create "Add Meal" button
        JButton addButton = new JButton("+");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Make the button more visible and clickable
        addButton.setPreferredSize(new Dimension(40, 25));
        addButton.setMinimumSize(new Dimension(40, 25));
        addButton.setMaximumSize(new Dimension(40, 25));
        addButton.setFocusable(true);
        addButton.setEnabled(true);

        addButton.addActionListener(e -> {
            Recipe selectedRecipe = savedRecipesList.getSelectedValue();
            if (selectedRecipe != null && controller != null) {
                String selectedMealType = (String) mealTypeCombo.getSelectedItem();
                try {
                    // Debug output
                    System.out.println("Adding recipe: " + selectedRecipe.getTitle());
                    System.out.println("Date: " + date);
                    System.out.println("Meal type: " + selectedMealType);

                    controller.addToCalendar(getCurrentUserId(), selectedRecipe.getRecipeId(), date, selectedMealType);
                    controller.viewCalendarWeek(getCurrentUserId(), viewModel.getState().getCurrentWeekStart());

                    JOptionPane.showMessageDialog(this,
                            "Recipe added successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error adding recipe: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please select a recipe from the saved recipes list first.",
                        "No Recipe Selected",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        // Add some padding around the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(5));  // Add some spacing
        panel.add(buttonPanel);
        panel.add(Box.createVerticalStrut(5));  // Add some spacing

        return panel;
    }

    /**
     * Refresh the calendar view.
     */
    private void refreshCalendar() {
        calendarPanel.removeAll();

        // Add day headers
        for (String dayName : DAY_NAMES) {
            JLabel dayLabel = new JLabel(dayName, SwingConstants.CENTER);
            dayLabel.setBorder(BorderFactory.createEtchedBorder());
            calendarPanel.add(dayLabel);
        }

        // Get current state
        MealPlanningState state = viewModel.getState();
        if (state == null) return;

        // Add day panels
        LocalDate currentDate = state.getCurrentWeekStart();
        for (int i = 0; i < 7; i++) {
            JPanel dayPanel = createDayPanel(currentDate);
            calendarPanel.add(dayPanel);
            currentDate = currentDate.plusDays(1);
        }

        updateWeekLabel();
        calendarPanel.revalidate();
        calendarPanel.repaint();

        // Ensure the parent containers are also updated
        contentPanel.revalidate();
        contentPanel.repaint();
        this.revalidate();
        this.repaint();
    }

    /**
     * Update the week label.
     */
    private void updateWeekLabel() {
        MealPlanningState state = viewModel.getState();
        if (state != null) {
            LocalDate weekStart = state.getCurrentWeekStart();
            LocalDate weekEnd = weekStart.plusDays(6);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
            weekLabel.setText(String.format("%s - %s",
                    weekStart.format(formatter),
                    weekEnd.format(formatter)));
        }
    }

    /**
     * Get the controller for the view.
     * @return The controller
     */
    public MealPlanningController getController() {
        return this.controller;
    }

    /**
     * Set the controller for the view.
     * @param controller The controller
     */
    public void setController(MealPlanningController controller) {
        this.controller = controller;
        if (controller != null) {
            savedRecipesList.setCellRenderer(new RecipeListCellRenderer(controller));

            int userId = getCurrentUserId();
            controller.initializeMealPlanning(userId);

            LocalDate currentWeekStart = viewModel.getState().getCurrentWeekStart();
            controller.viewCalendarWeek(userId, currentWeekStart);
        }
    }

    /**
     * Refresh the saved recipes list.
     */
    public void refreshSavedRecipes() {
        if (controller != null) {
            controller.getSavedRecipes(getCurrentUserId());
        }
    }

    /**
     * Handle action events.
     * @param evt The action event
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (controller == null) return;

        String command = evt.getActionCommand();
        MealPlanningState state = viewModel.getState();

        if (state == null) return;

        try {
            if (command.equals("Previous Week")) {
                LocalDate newStart = state.getCurrentWeekStart().minusWeeks(1);
                state.setCurrentWeekStart(newStart);
                controller.viewCalendarWeek(getCurrentUserId(), newStart);
            } else if (command.equals("Next Week")) {
                LocalDate newStart = state.getCurrentWeekStart().plusWeeks(1);
                state.setCurrentWeekStart(newStart);
                controller.viewCalendarWeek(getCurrentUserId(), newStart);
            } else if (command.startsWith("ADD_")) {
                Recipe selectedRecipe = savedRecipesList.getSelectedValue();
                if (selectedRecipe != null) {
                    String dateStr = command.substring(4);
                    LocalDate date = LocalDate.parse(dateStr);
                    String mealType = (String) mealTypeCombo.getSelectedItem();
                    controller.addToCalendar(getCurrentUserId(), selectedRecipe.getRecipeId(), date, mealType);
                }
            } else if (command.startsWith("REMOVE_")) {
                int entryId = Integer.parseInt(command.substring(7));
                controller.removeFromCalendar(getCurrentUserId(), entryId);
                controller.viewCalendarWeek(getCurrentUserId(), state.getCurrentWeekStart());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    "An error occurred: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handle property change events.
     * @param evt The property change event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        MealPlanningState state = (MealPlanningState) evt.getNewValue();
        if (state != null) {
            if (state.getError() != null) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                        state.getError(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                state.setError(null);
            }
            refreshCalendar();
            updateSavedRecipesList(state.getSavedRecipes());
            if (state.getMessage() != null) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                        state.getMessage(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                state.setMessage(null);
            }
        }
    }

    /**
     * Update the saved recipes list.
     * @param recipes The recipes
     */
    private void updateSavedRecipesList(List<Recipe> recipes) {
        savedRecipesModel.clear();
        if (recipes != null) {
            for (Recipe recipe : recipes) {
                savedRecipesModel.addElement(recipe);
            }
        }
    }

    /**
     * Get the current user ID.
     * @return The current user ID
     */
    private int getCurrentUserId() {
        // TODO: Implement proper user management
        return 1;
    }

    /**
     * Recipe list cell renderer for the saved recipes list.
     */
    private class RecipeListCellRenderer extends DefaultListCellRenderer {
        private final MealPlanningController controller;

        public RecipeListCellRenderer(MealPlanningController controller) {
            this.controller = controller;
        }

        /**
         * Get the list cell renderer component.
         * @param list The list
         * @param value The value
         * @param index The index
         * @param isSelected Whether the cell is selected
         * @param cellHasFocus Whether the cell has focus
         * @return The component
         */
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Recipe) {
                Recipe recipe = (Recipe) value;
                setText(recipe.getTitle());
                setToolTipText(createRecipeTooltip(recipe));
            }
            return this;
        }

        /**
         * Create the recipe tooltip.
         * @param recipe The recipe
         * @return The recipe tooltip
         */
        private String createRecipeTooltip(Recipe recipe) {
            StringBuilder tooltip = new StringBuilder("<html>");
            tooltip.append("<b>").append(recipe.getTitle()).append("</b><br>");
            tooltip.append("Calories: ").append(recipe.getNutrition().getCalories()).append("<br>");
            tooltip.append("Protein: ").append(recipe.getNutrition().getProtein()).append("g<br>");
            tooltip.append("Carbs: ").append(recipe.getNutrition().getCarbohydrates()).append("g<br>");
            tooltip.append("Fat: ").append(recipe.getNutrition().getFat()).append("g");
            tooltip.append("</html>");
            return tooltip.toString();
        }
    }
}