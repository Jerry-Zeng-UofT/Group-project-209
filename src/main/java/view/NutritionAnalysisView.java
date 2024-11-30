package view;


import interface_adapter.nutrition_analysis.NutritionAnalysisController;
import interface_adapter.nutrition_analysis.NutritionAnalysisState;
import interface_adapter.nutrition_analysis.NutritionAnalysisViewModel;
import interface_adapter.recipe_search.RecipeSearchState;

import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.*;
import java.util.ArrayList;

public class NutritionAnalysisView extends JFrame {

    private NutritionAnalysisController controller;
    private NutritionAnalysisViewModel viewModel;

    public NutritionAnalysisView(NutritionAnalysisViewModel nutritionAnalysisViewModel) {
        // Set the title of the JFrame
        super("Nutrition Analysis result");

        this.viewModel = nutritionAnalysisViewModel;

        // Set size and default close operation
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add some content to display results
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // result area
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);  // Make it non-editable
        resultArea.setLineWrap(true);  // Enable line wrapping
        resultArea.setWrapStyleWord(true);  // Wrap by words

        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add the panel to the frame
        add(panel);

        // Observe changes to the state
        viewModel.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())) {
                NutritionAnalysisState state = (NutritionAnalysisState) evt.getNewValue();
                if (state != null) {
                    String displayedNutritionInfo = classifyNutrients(state.getNutritionResults());

                    resultArea.setText(displayedNutritionInfo);
                }
                else {
                    resultArea.setText("State is Null");
                }
            }
        });
    }

    public static String classifyNutrients(List<String> nutrientInfos) {
        // Create categorized lists
        List<String> macronutrients = new ArrayList<>();
        List<String> micronutrients = new ArrayList<>();
        List<String> vitamins = new ArrayList<>();
        List<String> minerals = new ArrayList<>();
        List<String> others = new ArrayList<>();

        // Classification logic
        for (String info : nutrientInfos) {
            String lowerInfo = info.trim().toLowerCase();

            if (lowerInfo.contains("carbohydrate") || lowerInfo.contains("protein") || lowerInfo.contains("fat") || lowerInfo.contains("energy") || lowerInfo.contains("fiber") || lowerInfo.contains("water")) {
                macronutrients.add(info);
            }
            else if (lowerInfo.contains("vitamin")) {
                if (lowerInfo.contains("a") || lowerInfo.contains("b") || lowerInfo.contains("c") || lowerInfo.contains("d") || lowerInfo.contains("e") || lowerInfo.contains("k")) {
                    vitamins.add(info);
                }
                else {
                    micronutrients.add(info);
                }
            }
            else if (lowerInfo.contains("calcium") || lowerInfo.contains("iron") || lowerInfo.contains("magnesium") || lowerInfo.contains("phosphorus") || lowerInfo.contains("potassium") || lowerInfo.contains("sodium") || lowerInfo.contains("zinc")) {
                minerals.add(info);
            }
            else {
                others.add(info);
            }
        }

        // Build the result string
        StringBuilder result = new StringBuilder();

        if (!macronutrients.isEmpty()) {
            result.append("Macronutrients:\n");
            macronutrients.forEach(info -> result.append("    • ").append(info).append("\n"));
        }

        if (!micronutrients.isEmpty()) {
            result.append("Micronutrients:\n");
            micronutrients.forEach(info -> result.append("    • ").append(info).append("\n"));
        }

        if (!vitamins.isEmpty()) {
            result.append("  Vitamins:\n");
            vitamins.forEach(info -> result.append("    • ").append(info).append("\n"));
        }

        if (!minerals.isEmpty()) {
            result.append("  Minerals:\n");
            minerals.forEach(info -> result.append("    • ").append(info).append("\n"));
        }

        if (!others.isEmpty()) {
            result.append("Others:\n");
            others.forEach(info -> result.append("    • ").append(info).append("\n"));
        }

        return result.toString().trim();
    }

    public void setController(NutritionAnalysisController controller) {
        this.controller = controller;
    }

    public NutritionAnalysisController getController() {
        return controller;
    }
}


