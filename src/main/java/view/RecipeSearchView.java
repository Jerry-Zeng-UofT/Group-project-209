package view;

import entity.Recipe;
import interface_adapter.nutrition_analysis.NutritionAnalysisController;
import interface_adapter.nutrition_analysis.NutritionAnalysisViewModel;
import interface_adapter.recipe_search.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeSearchView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final int TEXTFIELD_WIDTH = 20;
    private static final int VERTICAL_SPACING = 10;
    private static final int INGREDIENT_LIST_HEIGHT = 100;
    private static final int INGREDIENT_LIST_WIDTH = 300;
    private static final int RESULTS_LIST_HEIGHT = 200;
    private static final int RESULTS_LIST_WIDTH = 300;
    private static final int TITLE_FONT_SIZE = 16;

    private final JLabel title;
    private final JPanel inputPanel;
    private final JTextField ingredientField;
    private final JButton addIngredientButton;
    private final DefaultListModel<String> ingredientListModel;
    private final JList<String> ingredientList;
    private final JButton removeIngredientButton;
    private final JButton searchButton;
    private final JPanel resultsPanel;
    private final DefaultListModel<String> recipeListModel;
    private final JList<String> recipeResults;
    private final List<String> ingredients;
    private final JPanel restrictionPanel;
    private final JButton addRestrictionButton;
    private final DefaultListModel<String> restrictionListModel;
    private final JList<String> restrictionList;
    private final List<String> restrictions;
    private Map<String, List<String>> restrictionMap;
    private final JButton saveRecipeButton;
    private final JButton analyzeNutritionButton;

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
        title = new JLabel(RecipeSearchViewModel.TITLE_LABEL);
        inputPanel = new JPanel();
        restrictionPanel = new JPanel();
        resultsPanel = new JPanel();
        ingredientField = new JTextField(TEXTFIELD_WIDTH);
        addIngredientButton = new JButton(RecipeSearchViewModel.ADD_INGREDIENT_BUTTON_LABEL);
        removeIngredientButton = new JButton(RecipeSearchViewModel.REMOVE_INGREDIENT_BUTTON_LABEL);
        searchButton = new JButton(RecipeSearchViewModel.SEARCH_BUTTON_LABEL);
        addRestrictionButton = new JButton(RecipeSearchViewModel.ADD_RESTRICTION_LABEL);
        saveRecipeButton = new JButton("Save Recipe");
        analyzeNutritionButton = new JButton("Analyze Nutrition");

        ingredientListModel = new DefaultListModel<>();
        ingredientList = new JList<>(ingredientListModel);
        restrictionListModel = new DefaultListModel<>();
        restrictionList = new JList<>(restrictionListModel);
        recipeListModel = new DefaultListModel<>();
        recipeResults = new JList<>(recipeListModel);

        ingredients = new ArrayList<>();
        restrictions = new ArrayList<>();
        restrictionMap = new HashMap<>();

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
            final String restriction = filterFrame.getSelectedFilters().trim();
            restrictionMap.put("Diet Types", filterFrame.getDietType());
            restrictionMap.put("Health Types", filterFrame.getHealthType());
            restrictionMap.put("Cuisine Types", filterFrame.getCuisineType());

            if (!restriction.isEmpty()) {
                restrictions.add(restriction);
                restrictionListModel.addElement(restriction);
            }
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

    private int getCurrentUserId() {
        return 1; // For testing purposes
    }

    private void setupInputPanel() {
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        final JPanel addIngredientPanel = new JPanel();
        addIngredientPanel.add(new JLabel("Enter Ingredient:"));
        addIngredientPanel.add(ingredientField);
        addIngredientPanel.add(addIngredientButton);
        addIngredientPanel.add(addRestrictionButton);

        final JScrollPane ingredientScrollPane = new JScrollPane(ingredientList);
        ingredientScrollPane.setPreferredSize(
                new Dimension(INGREDIENT_LIST_WIDTH, INGREDIENT_LIST_HEIGHT));

        final JScrollPane restrictionScrollPane = new JScrollPane(restrictionList);
        restrictionScrollPane.setPreferredSize(
                new Dimension(INGREDIENT_LIST_WIDTH, INGREDIENT_LIST_HEIGHT));

        final JPanel removeButtonPanel = new JPanel();
        removeButtonPanel.add(removeIngredientButton);

        inputPanel.add(addIngredientPanel);
        inputPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        inputPanel.add(ingredientScrollPane);
        inputPanel.add(restrictionScrollPane);
        inputPanel.add(removeButtonPanel);
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

    public void updateSelection(String selectionText) {
        restrictionListModel.addElement(selectionText);
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
