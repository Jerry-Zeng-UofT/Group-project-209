package view;

import interface_adapter.nutrition_analysis.NutritionAnalysisController;
import interface_adapter.recipe_search.RecipeSearchController;

import java.awt.*;
import java.util.Arrays;
import javax.swing.*;

public class NutritionAnalysisView extends JFrame {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    private final RecipeSearchView mainFrame;

    private JTextField textField1;
    private JTextField textField2;
    private JLabel resultLabel;

    private NutritionAnalysisController nutritionAnalysisController;

    public NutritionAnalysisView(RecipeSearchView mainFrame) {
        this.mainFrame = mainFrame;
        // Set the title of the window
        setTitle("Nutrition Analysis");

        // Set the layout manager for the frame
        setLayout(new BorderLayout(10, 10));
        // Add gaps between components

        // Create a JPanel for the input fields and button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 5, 5));
        // 3 rows, 2 columns, with gaps

        // Labels and text fields
        JLabel label1 = new JLabel("Enter The recipe Name:");
        textField1 = new JTextField(15);

        JLabel label2 = new JLabel("Enter The Ingredients:");
        textField2 = new JTextField(15);

        JButton analyzeButton = new JButton("Analyze");



        // Add components to the input panel
        inputPanel.add(label1);
        inputPanel.add(textField1);
        inputPanel.add(label2);
        inputPanel.add(textField2);
        inputPanel.add(new JLabel());
        // Placeholder for spacing
        inputPanel.add(analyzeButton);

        // Create a JLabel for displaying results
        resultLabel = new JLabel("Analysis Result", SwingConstants.CENTER);

        // Add components to the this window.
        add(inputPanel, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);

        // Setting of the window.
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Action of Analysis.
        analyzeButton.addActionListener(e -> handleAnalyzeAction());

    }

    /**
     * Sets the controller for this view.
     *
     * @param controller The controller to set
     */
    public void setNutritionAnalysisController(NutritionAnalysisController controller) {
        this.nutritionAnalysisController = controller;
    }

    // Method of the "Analyze" button action.
    private void handleAnalyzeAction() {
        String input1 = textField1.getText();
        String input2 = textField2.getText();

        // Perform analysis and update the result label
        if (input1.isEmpty() || input2.isEmpty()) {
            resultLabel.setText("Please enter values in both fields.");
        }
        else {
            java.util.List<String> convertedInput2 = inputConverter(input2);
            nutritionAnalysisController.executeAnalysis(input1, convertedInput2);
        }

    }

    private java.util.List<String> inputConverter(String input2) {
        // Split the string by "\n"
        String[] parts = input2.split("\n");
        java.util.List<String> list = Arrays.asList(parts);
        return list;
    }

}

