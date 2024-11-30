package view;

import entity.Recipe;
import interface_adapter.nutrition_analysis.NutritionAnalysisController;
import interface_adapter.recipe_search.*;

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
    private RecipeSearchController recipeSearchController;
    private final ServingAdjustmentHandler servingAdjustmentHandler;

    // Optional reference to meal planning view for updates
    private MealPlanningView mealPlanningView;
    private final ServingAdjustView servingAdjustView;

    // the nutrition analysis view and controller.
    private NutritionAnalysisView nutritionAnalysisView;
    private NutritionAnalysisController nutritionAnalysisController;

    public RecipeSearchView(RecipeSearchViewModel viewModel, RecipeSearchController controller) {
        this.recipeSearchViewModel = viewModel;
        this.recipeSearchViewModel.addPropertyChangeListener(this);
        this.recipeSearchController = controller;
        RecipeSearchPresenter presenter = new RecipeSearchPresenter(viewModel);
        servingAdjustmentHandler = new ServingAdjustmentHandler(recipeSearchController, presenter);

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
        addRestrictionButton = new JButton(RecipeSearchViewModel.ADD_RESTRICTION_LABEL);
        removeRestrictionsButton = new JButton(RecipeSearchViewModel.REMOVE_RESTRICTION_LABEL);
        saveRecipeButton = new JButton("Save Recipe");
        analyzeNutritionButton = new JButton("Analyze Nutrition");

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

    public void setRecipeSearchController(RecipeSearchController controller) {
        this.recipeSearchController = controller;
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

    private void handleServingAdjustment() {
        final int servings = servingAdjustView.getServings();
        final RecipeSearchState state = (RecipeSearchState) recipeSearchViewModel.getState();

        servingAdjustmentHandler.adjustServings(servings, state);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(addIngredientButton)) {
            final String ingredient = ingredientField.getText().trim();
            if (!ingredient.isEmpty()) {
                ingredients.add(ingredient);
                addValueToKey(restrictionMap, "Food Name", ingredient);
                ingredientListModel.addElement(ingredient);
                ingredientField.setText("");
            }
        }
        else if (evt.getSource().equals(addRestrictionButton)) {
            FilterFrameView filterFrame = new FilterFrameView(this);
        }
        else if (evt.getSource().equals(saveRecipeButton)) {
            int selectedIndex = recipeResults.getSelectedIndex();
            if (selectedIndex != -1 && recipeSearchController != null) {
                RecipeSearchState state = (RecipeSearchState) recipeSearchViewModel.getState();
                if (state != null) {
                    List<Recipe> recipes = state.getRecipes();
                    if (selectedIndex < recipes.size()) {
                        Recipe selectedRecipe = recipes.get(selectedIndex);
                        recipeSearchController.saveRecipe(getCurrentUserId(), selectedRecipe);

                        // Refresh meal planning view if available
                        if (mealPlanningView != null && mealPlanningView.getController() != null) {
                            mealPlanningView.refreshSavedRecipes();
                        }
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(this,
                        "Please select a recipe to save first.",
                        "No Recipe Selected",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        else if (evt.getSource().equals(removeIngredientButton)) {
            final int selectedIndex = ingredientList.getSelectedIndex();
            if (selectedIndex != -1) {
                ingredients.remove(selectedIndex);
                ingredientListModel.remove(selectedIndex);
            }
        }
        else if (evt.getSource().equals(removeRestrictionsButton)) {
            final int selectedIndex = restrictionList.getSelectedIndex();
            if (selectedIndex != -1) {
                restrictions.remove(selectedIndex);
                restrictionListModel.remove(selectedIndex);
                removeByJList(restrictionList.getModel().getElementAt(selectedIndex));
            }
            else {
                JOptionPane.showMessageDialog(this,
                        "Please select restrictions to remove.",
                        "No Restrictions Selected",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        else if (evt.getSource().equals(searchButton)) {
            if (recipeSearchController != null) {
                if (restrictions.isEmpty()) {
                    recipeSearchController.executeSearch(new ArrayList<>(ingredients));
                }
                else {
                    recipeSearchController.executeRestrictionSearch(restrictionMap);
                }
            }
        }
        else if (evt.getSource().equals(analyzeNutritionButton)) {
            int selectedIndex = recipeResults.getSelectedIndex();
            if (selectedIndex != -1 && nutritionAnalysisController != null) {
                RecipeSearchState state = (RecipeSearchState) recipeSearchViewModel.getState();
                if (state != null) {
                    List<Recipe> recipes = state.getRecipes();
                    if (selectedIndex < recipes.size()) {
                        Recipe selectedRecipe = recipes.get(selectedIndex);
                        nutritionAnalysisController.executeAnalysis(selectedRecipe);

                        nutritionAnalysisView.setVisible(true);
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(this,
                        "Please select a recipe to analyze first.",
                        "No Recipe Selected",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

    }

    // Helper method for remove restrictions
    private void removeByJList(String valueToRemove) {
        List<String> valuesToRemove = Arrays.asList(valueToRemove.split(" "));

        restrictionMap.values().removeIf(valuesToRemove::contains);
    }

    private int getCurrentUserId() {
        return 1; // For testing purposes
    }

    private void setupInputPanel() {
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        final JPanel addIngredientPanel = new JPanel();
        addIngredientPanel.setLayout(new BoxLayout(addIngredientPanel, BoxLayout.Y_AXIS));

        final JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        final JPanel ingredientLabelPanel = new JPanel();
        final JPanel restrictionLabelPanel = new JPanel();

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        final JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.X_AXIS));

        final JScrollPane ingredientScrollPane = new JScrollPane(ingredientList);
        ingredientScrollPane.setPreferredSize(
                new Dimension(INGREDIENT_LIST_WIDTH, INGREDIENT_LIST_HEIGHT));

        final JScrollPane restrictionScrollPane = new JScrollPane(restrictionList);
        restrictionScrollPane.setPreferredSize(
                new Dimension(INGREDIENT_LIST_WIDTH, INGREDIENT_LIST_HEIGHT));

        addIngredientPanel.add(new JLabel("Enter Ingredient:"));
        addIngredientPanel.add(ingredientField);

        ingredientLabelPanel.add(new JLabel("Ingredient:"));

        JLabel label = new JLabel("Ingredient:");
        label.setHorizontalAlignment(SwingConstants.LEFT);

        restrictionLabelPanel.add(new JLabel("Restriction:"));
        labelPanel.add(label);
        labelPanel.add(restrictionLabelPanel);

        buttonPanel.add(addIngredientButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(removeIngredientButton);
        buttonPanel.add(Box.createHorizontalStrut(80));
        buttonPanel.add(addRestrictionButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(removeRestrictionsButton);

        displayPanel.add(ingredientScrollPane);
        displayPanel.add(restrictionScrollPane);

        inputPanel.add(addIngredientPanel);
        inputPanel.add(labelPanel);
        inputPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        inputPanel.add(displayPanel);
        inputPanel.add(buttonPanel);
    }

    private void setupResultsPanel() {
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        final JLabel resultsLabel = new JLabel("Recipe Results:");
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
        RecipeSearchState state = (RecipeSearchState) evt.getNewValue();
        if (state != null) {
            if (state.getError() != null) {
                JOptionPane.showMessageDialog(this,
                        state.getError(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (state.getMessage() != null) {
                JOptionPane.showMessageDialog(this,
                        state.getMessage(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                updateRecipeResults(state.getRecipeResults());
            }
        }
    }

    public void updateSelection(String selectionText, Map<String, List<String>> selectionResults) {
        restrictionListModel.addElement(selectionText);
        restrictions.add(selectionText);
        restrictionMap = selectionResults;
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
