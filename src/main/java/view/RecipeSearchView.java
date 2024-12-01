package view;

import entity.Recipe;
import interface_adapter.nutrition_analysis.NutritionAnalysisController;
import interface_adapter.recipe_search.*;
import interface_adapter.search_with_restriction.*;
import interface_adapter.search_with_restriction.RestrictionController;
import interface_adapter.search_with_restriction.RestrictionViewModel;
import interface_adapter.serving_adjust.ServingAdjustController;
import interface_adapter.serving_adjust.ServingAdjustViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

public class RecipeSearchView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final int TEXTFIELD_WIDTH = 20;
    private static final int VERTICAL_SPACING = 10;
    private static final int INGREDIENT_LIST_HEIGHT = 100;
    private static final int INGREDIENT_LIST_WIDTH = 300;
    private static final int RESULTS_LIST_HEIGHT = 200;
    private static final int RESULTS_LIST_WIDTH = 300;
    private static final int TITLE_FONT_SIZE = 16;

    // UI Components
    // Labels
    private final JLabel title;

    // Panels
    private final JPanel inputPanel;
    private final JPanel resultsPanel;
    private final JPanel restrictionPanel;

    // Text Fields
    private final JTextField ingredientField;

    // Buttons
    private final JButton addIngredientButton;
    private final JButton removeIngredientButton;
    private final JButton searchButton;
    private final JButton addRestrictionButton;
    private final JButton saveRecipeButton;
    private final JButton analyzeNutritionButton;
    private final JButton removeRestrictionsButton;

    // Lists
    private final JList<String> ingredientList;
    private final JList<String> recipeResults;
    private final JList<String> restrictionList;

    // Data Models
    private final DefaultListModel<String> ingredientListModel;
    private final DefaultListModel<String> recipeListModel;
    private final DefaultListModel<String> restrictionListModel;

    // Data Collections
    private final List<String> ingredients;
    private final List<String> restrictions;
    private Map<String, List<String>> restrictionMap;

    // Controllers and ViewModels
    private final RecipeSearchViewModel recipeSearchViewModel;
    private final RestrictionViewModel restrictionViewModel;
    private RecipeSearchController recipeSearchController;
    private RestrictionController restrictionController;
    private final ServingAdjustView servingAdjustView;
    private ServingAdjustController servingAdjustController;

    // Optional reference to meal planning view for updates
    private MealPlanningView mealPlanningView;

    // the nutrition analysis view and controller.
    private NutritionAnalysisView nutritionAnalysisView;
    private NutritionAnalysisController nutritionAnalysisController;
    private ServingAdjustViewModel servingAdjustViewModel;

    public RecipeSearchView(RecipeSearchViewModel viewModel, RestrictionViewModel restrictionModel,
                            RecipeSearchController controller, RestrictionController restrictionController,
                            ServingAdjustController servingAdjustController) {
        this.recipeSearchViewModel = viewModel;
        this.recipeSearchViewModel.addPropertyChangeListener(this);
        this.restrictionViewModel = restrictionModel;
        this.restrictionViewModel.addPropertyChangeListener(this);
        this.recipeSearchController = controller;
        this.restrictionController = restrictionController;
        RecipeSearchPresenter presenter = new RecipeSearchPresenter(viewModel);
        this.servingAdjustController = servingAdjustController;

        // Initialize components
        // UI Components - Labels
        title = new JLabel(RecipeSearchViewModel.TITLE_LABEL);

        // Panels
        inputPanel = new JPanel();
        restrictionPanel = new JPanel();
        resultsPanel = new JPanel();

        // Text Fields
        ingredientField = new JTextField(TEXTFIELD_WIDTH);

        // Buttons
        addIngredientButton = new JButton(RecipeSearchViewModel.ADD_INGREDIENT_BUTTON_LABEL);
        removeIngredientButton = new JButton(RecipeSearchViewModel.REMOVE_INGREDIENT_BUTTON_LABEL);
        searchButton = new JButton(RecipeSearchViewModel.SEARCH_BUTTON_LABEL);
        addRestrictionButton = new JButton(RestrictionViewModel.ADD_RESTRICTION_LABEL);
        removeRestrictionsButton = new JButton(RestrictionViewModel.REMOVE_RESTRICTION_LABEL);
        saveRecipeButton = new JButton(RecipeSearchViewModel.SAVE_RECIPE_LABEL);
        analyzeNutritionButton = new JButton(RecipeSearchViewModel.ANALYZE_NUTRITION_LABEL);

        // Lists
        ingredientListModel = new DefaultListModel<>();
        ingredientList = new JList<>(ingredientListModel);
        restrictionListModel = new DefaultListModel<>();
        restrictionList = new JList<>(restrictionListModel);
        recipeListModel = new DefaultListModel<>();
        recipeResults = new JList<>(recipeListModel);

        // Data Collections
        ingredients = new ArrayList<>();
        restrictions = new ArrayList<>();
        restrictionMap = new HashMap<>();

        // Controllers and ViewModels
        servingAdjustView = new ServingAdjustView(evt -> handleServingAdjustment());

        this.setLayout(new BorderLayout());

        title.setFont(new Font(title.getFont().getName(), Font.BOLD, TITLE_FONT_SIZE));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        setupInputPanel();
        setupResultsPanel();

        addIngredientButton.addActionListener(this);
        removeIngredientButton.addActionListener(this);
        searchButton.addActionListener(this);
        addRestrictionButton.addActionListener(this);
        saveRecipeButton.addActionListener(this);
        analyzeNutritionButton.addActionListener(this);
        removeRestrictionsButton.addActionListener(this);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        mainPanel.add(restrictionPanel);
        mainPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(searchButton);
        mainPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        mainPanel.add(resultsPanel);

        this.add(mainPanel, BorderLayout.CENTER);
    }

    public Map<String, List<String>> getRestrictionMap() {
        return restrictionMap;
    }

    public void setRecipeSearchController(RecipeSearchController controller) {
        this.recipeSearchController = controller;
    }

    public void setRestrictionController(RestrictionController restrictionController) {
        this.restrictionController = restrictionController;
    }

    // Method to set reference to meal planning view
    public void setMealPlanningView(MealPlanningView mealPlanningView) {
        this.mealPlanningView = mealPlanningView;
    }

    public void setNutritionAnalysisController(NutritionAnalysisController nutritionAnalysisController) {
        this.nutritionAnalysisController = nutritionAnalysisController;
    }

    public void setNutritionAnalysisView(NutritionAnalysisView nutritionAnalysisView) {
        this.nutritionAnalysisView = nutritionAnalysisView;
    }

    public void setServingAdjustController(ServingAdjustController controller) {
        this.servingAdjustController = controller;
    }

    public void setServingAdjustViewModel(ServingAdjustViewModel viewModel) {
        this.servingAdjustViewModel = viewModel;
    }

    private void handleServingAdjustment() {
        int servings = servingAdjustView.getServings();
        if (recipeSearchController != null && servingAdjustController != null) {
            RecipeSearchState state = (RecipeSearchState) recipeSearchViewModel.getState();
            if (state != null) {
                servingAdjustController.updateServingsForAll(servings, state.getRecipes());

                servingAdjustViewModel.updateRecipes(state.getRecipes());

                RecipeSearchPresenter presenter = new RecipeSearchPresenter(recipeSearchViewModel);
                presenter.presentRecipes(state.getRecipes());

                JOptionPane.showMessageDialog(this,
                        "All recipes updated to " + servings + " servings.",
                        "Update Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Serving adjustment is not configured properly.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();

        if (source.equals(addIngredientButton)) {
            handleAddIngredient();
        } else if (source.equals(addRestrictionButton)) {
            handleAddRestriction();
        } else if (source.equals(saveRecipeButton)) {
            handleSaveRecipe();
        } else if (source.equals(removeIngredientButton)) {
            handleRemoveIngredient();
        } else if (source.equals(removeRestrictionsButton)) {
            handleRemoveRestriction();
        } else if (source.equals(searchButton)) {
            handleSearch();
        } else if (source.equals(analyzeNutritionButton)) {
            handleAnalyzeNutrition();
        }
    }

    // Extracted methods for each action
    private void handleAddIngredient() {
        final String ingredient = ingredientField.getText().trim();
        if (!ingredient.isEmpty()) {
            ingredients.add(ingredient);
            addValueToKey(restrictionMap, "Food Name", ingredient);
            ingredientListModel.addElement(ingredient);
            ingredientField.setText("");
        }
    }

    private void handleAddRestriction() {
        new FilterFrameView(this);
    }

    private void handleSaveRecipe() {
        int selectedIndex = recipeResults.getSelectedIndex();
        if (selectedIndex == -1) {
            showMessage("Please select a recipe to save first.", "No Recipe Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (recipeSearchController != null) {
            Object state = recipeSearchViewModel.getState();
            if (state == null) {
                state = restrictionViewModel.getState();
            }

            if (state instanceof RecipeSearchState recipeState) {
                List<Recipe> recipes = recipeState.getRecipes();
                if (selectedIndex < recipes.size()) {
                    Recipe selectedRecipe = recipes.get(selectedIndex);
                    recipeSearchController.saveRecipe(getCurrentUserId(), selectedRecipe);

                    if (mealPlanningView != null && mealPlanningView.getController() != null) {
                        mealPlanningView.refreshSavedRecipes();
                    }
                }
            } else if (state instanceof RestrictionState restrictionState) {
                List<Recipe> recipes = restrictionState.getRecipes();
                if (selectedIndex < recipes.size()) {
                    Recipe selectedRecipe = recipes.get(selectedIndex);
                    recipeSearchController.saveRecipe(getCurrentUserId(), selectedRecipe);

                    if (mealPlanningView != null && mealPlanningView.getController() != null) {
                        mealPlanningView.refreshSavedRecipes();
                    }
                }
            } else {
                showMessage("Unknown state type.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRemoveIngredient() {
        final int selectedIndex = ingredientList.getSelectedIndex();
        if (selectedIndex != -1) {
            ingredients.remove(selectedIndex);
            ingredientListModel.remove(selectedIndex);
        }
    }

    private void handleRemoveRestriction() {
        final int selectedIndex = restrictionList.getSelectedIndex();
        if (selectedIndex != -1) {
            restrictions.remove(selectedIndex);
            restrictionListModel.remove(selectedIndex);
            removeByJList(restrictionList.getModel().getElementAt(selectedIndex));
        } else {
            showMessage("Please select restrictions to remove.", "No Restrictions Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleSearch() {
        if (recipeSearchController != null && restrictionController != null) {
            if (restrictions.isEmpty()) {
                recipeSearchController.executeSearch(new ArrayList<>(ingredients));
            } else {
                restrictionController.executeRestrictionSearch(restrictionMap);
            }
        }
    }

    private void handleAnalyzeNutrition() {
        final int selectedIndex = recipeResults.getSelectedIndex();
        if (selectedIndex == -1) {
            showMessage("Please select a recipe to analyze first.", "No Recipe Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (nutritionAnalysisController != null) {
            final RecipeSearchState state = (RecipeSearchState) recipeSearchViewModel.getState();
            if (state != null) {
                final List<Recipe> recipes = state.getRecipes();
                if (selectedIndex < recipes.size()) {
                    final Recipe selectedRecipe = recipes.get(selectedIndex);
                    nutritionAnalysisController.executeAnalysis(selectedRecipe);
                    nutritionAnalysisView.setVisible(true);
                }
            }
        }
    }

    // Utility method for showing messages
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }


    // Helper method for remove restrictions
    private void removeByJList(String valueToRemove) {
        final List<String> valuesToRemove = Arrays.asList(valueToRemove.split(" "));

        restrictionMap.values().removeIf(valuesToRemove::contains);
    }

    private int getCurrentUserId() {
        return 1; // For testing purposes
    }

    private void setupInputPanel() {
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        final JPanel addIngredientPanel = createAddIngredientPanel();
        final JPanel labelPanel = createLabelPanel();
        final JPanel buttonPanel = createButtonPanel();
        final JPanel displayPanel = createDisplayPanel();

        inputPanel.add(addIngredientPanel);
        inputPanel.add(labelPanel);
        inputPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        inputPanel.add(displayPanel);
        inputPanel.add(buttonPanel);
    }

    // Extracted helper methods
    private JPanel createAddIngredientPanel() {
        JPanel addIngredientPanel = new JPanel();
        addIngredientPanel.setLayout(new BoxLayout(addIngredientPanel, BoxLayout.Y_AXIS));
        addIngredientPanel.add(new JLabel(RecipeSearchViewModel.ENTER_INGREDIENT_LABEL));
        addIngredientPanel.add(ingredientField);
        return addIngredientPanel;
    }

    private JPanel createLabelPanel() {
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));

        JLabel ingredientLabel = new JLabel(RecipeSearchViewModel.INGREDIENT_TITLE_LABEL);
        ingredientLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel restrictionLabelPanel = new JPanel();
        restrictionLabelPanel.add(new JLabel(RestrictionViewModel.RESTRICTION_TITLE_LABEL));

        labelPanel.add(ingredientLabel);
        labelPanel.add(restrictionLabelPanel);

        return labelPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(addIngredientButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(removeIngredientButton);
        buttonPanel.add(Box.createHorizontalStrut(80));
        buttonPanel.add(addRestrictionButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(removeRestrictionsButton);

        return buttonPanel;
    }

    private JPanel createDisplayPanel() {
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.X_AXIS));

        JScrollPane ingredientScrollPane = new JScrollPane(ingredientList);
        ingredientScrollPane.setPreferredSize(
                new Dimension(INGREDIENT_LIST_WIDTH, INGREDIENT_LIST_HEIGHT));

        JScrollPane restrictionScrollPane = new JScrollPane(restrictionList);
        restrictionScrollPane.setPreferredSize(
                new Dimension(INGREDIENT_LIST_WIDTH, INGREDIENT_LIST_HEIGHT));

        displayPanel.add(ingredientScrollPane);
        displayPanel.add(restrictionScrollPane);

        return displayPanel;
    }


    private void setupResultsPanel() {
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        final JLabel resultsLabel = new JLabel(RecipeSearchViewModel.RECIPE_RESULT_LABEL);
        resultsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Configure the recipe results list
        recipeResults.setFont(new Font("Monospaced", Font.PLAIN, 12));
        recipeResults.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        recipeResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create a custom cell renderer for better formatting
        recipeResults.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                // Set background colors
                if (isSelected) {
                    label.setBackground(new Color(200, 220, 240));
                    label.setForeground(Color.BLACK);
                } else {
                    label.setBackground(Color.WHITE);
                }

                // Add padding
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return label;
            }
        });

        final JScrollPane resultsScrollPane = new JScrollPane(recipeResults);
        resultsScrollPane.setPreferredSize(new Dimension(RESULTS_LIST_WIDTH, RESULTS_LIST_HEIGHT));
        resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultsScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(Color.GRAY)
        ));

        // Create button panel with better spacing and alignment
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        analyzeNutritionButton.setBackground(new Color(240, 240, 240));
        saveRecipeButton.setBackground(new Color(240, 240, 240));

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(analyzeNutritionButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(saveRecipeButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(servingAdjustView);
        buttonPanel.add(Box.createHorizontalGlue());

        resultsPanel.add(resultsLabel);
        resultsPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        resultsPanel.add(resultsScrollPane);
        resultsPanel.add(buttonPanel);

        // Add padding around the results panel
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newState = evt.getNewValue();

        if (newState instanceof RecipeSearchState) {
            RecipeSearchState recipeState = (RecipeSearchState) newState;

            if (recipeState.getError() != null) {
                JOptionPane.showMessageDialog(this,
                        recipeState.getError(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (recipeState.getMessage() != null) {
                JOptionPane.showMessageDialog(this,
                        recipeState.getMessage(),
                        "Message",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                updateRecipeResults(recipeState.getRecipeResults());
            }

        } else if (newState instanceof RestrictionState) {
            RestrictionState restrictionState = (RestrictionState) newState;

            if (restrictionState.getError() != null) {
                JOptionPane.showMessageDialog(this,
                        restrictionState.getError(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (restrictionState.getMessage() != null) {
                JOptionPane.showMessageDialog(this,
                        restrictionState.getMessage(),
                        "Message",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                updateRecipeResults(restrictionState.getRecipeResults());
            }

        } else {
            System.out.println("Unexpected state type: " + newState.getClass().getName());
        }
    }

    public void updateSelection(String selectionText) {
        restrictionListModel.addElement(selectionText);
        restrictions.add(selectionText);
    }

    private void updateRecipeResults(List<String> recipes) {
        recipeListModel.clear();
        for (String recipe : recipes) {
            recipeListModel.addElement(recipe);
        }
    }

    private static void addValueToKey(Map<String, List<String>> map, String key, String value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }
}
