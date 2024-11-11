package view;

import interface_adapter.recipe_search.RecipeSearchController;
import interface_adapter.recipe_search.RecipeSearchState;
import interface_adapter.recipe_search.RecipeSearchViewModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * A view for searching recipes by ingredients.
 */
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

    private final RecipeSearchViewModel recipeSearchViewModel;
    private RecipeSearchController recipeSearchController;

    /**
     * Constructs a new RecipeSearchView.
     * @param viewModel The view model for recipe search
     */
    public RecipeSearchView(RecipeSearchViewModel viewModel) {
        this.recipeSearchViewModel = viewModel;
        this.recipeSearchViewModel.addPropertyChangeListener(this);

        // Initialize components
        title = new JLabel(RecipeSearchViewModel.TITLE_LABEL);
        inputPanel = new JPanel();
        ingredientField = new JTextField(TEXTFIELD_WIDTH);
        addIngredientButton = new JButton(RecipeSearchViewModel.ADD_INGREDIENT_BUTTON_LABEL);
        ingredientListModel = new DefaultListModel<>();
        ingredientList = new JList<>(ingredientListModel);
        removeIngredientButton = new JButton(RecipeSearchViewModel.REMOVE_INGREDIENT_BUTTON_LABEL);
        searchButton = new JButton(RecipeSearchViewModel.SEARCH_BUTTON_LABEL);
        resultsPanel = new JPanel();
        recipeListModel = new DefaultListModel<>();
        recipeResults = new JList<>(recipeListModel);
        ingredients = new ArrayList<>();

        // Set up the main layout
        this.setLayout(new BorderLayout());

        // Configure the title
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, TITLE_FONT_SIZE));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set up panels
        setupInputPanel();
        setupResultsPanel();

        // Add action listeners
        addIngredientButton.addActionListener(this);
        removeIngredientButton.addActionListener(this);
        searchButton.addActionListener(this);

        // Main panel assembly
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        mainPanel.add(searchButton);
        mainPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        mainPanel.add(resultsPanel);

        this.add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Sets the controller for this view.
     * @param controller The controller to set
     */
    public void setRecipeSearchController(RecipeSearchController controller) {
        this.recipeSearchController = controller;
    }

    // ... (keep existing panel setup methods the same) ...

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(addIngredientButton)) {
            final String ingredient = ingredientField.getText().trim();
            if (!ingredient.isEmpty()) {
                ingredients.add(ingredient);
                ingredientListModel.addElement(ingredient);
                ingredientField.setText("");
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
                recipeSearchController.executeSearch(new ArrayList<>(ingredients));
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RecipeSearchState state = (RecipeSearchState) evt.getNewValue();
        if (state != null) {
            if (state.getError() != null) {
                JOptionPane.showMessageDialog(this, state.getError());
            } else {
                updateRecipeResults(state.getRecipeResults());
            }
        }
    }

    private void setupInputPanel() {
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        final JPanel addIngredientPanel = new JPanel();
        addIngredientPanel.add(new JLabel("Enter Ingredient:"));
        addIngredientPanel.add(ingredientField);
        addIngredientPanel.add(addIngredientButton);

        final JScrollPane ingredientScrollPane = new JScrollPane(ingredientList);
        ingredientScrollPane.setPreferredSize(
                new Dimension(INGREDIENT_LIST_WIDTH, INGREDIENT_LIST_HEIGHT));

        final JPanel removeButtonPanel = new JPanel();
        removeButtonPanel.add(removeIngredientButton);

        inputPanel.add(addIngredientPanel);
        inputPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        inputPanel.add(ingredientScrollPane);
        inputPanel.add(removeButtonPanel);
    }

    private void setupResultsPanel() {
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        final JLabel resultsLabel = new JLabel("Recipe Results:");
        resultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JScrollPane resultsScrollPane = new JScrollPane(recipeResults);
        resultsScrollPane.setPreferredSize(
                new Dimension(RESULTS_LIST_WIDTH, RESULTS_LIST_HEIGHT));

        resultsPanel.add(resultsLabel);
        resultsPanel.add(Box.createVerticalStrut(VERTICAL_SPACING));
        resultsPanel.add(resultsScrollPane);
    }

    private void updateRecipeResults(List<String> recipes) {
        recipeListModel.clear();
        for (String recipe : recipes) {
            recipeListModel.addElement(recipe);
        }
    }
}