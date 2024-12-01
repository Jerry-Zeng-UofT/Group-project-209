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
import java.util.HashMap;
import java.util.Map;

public class NutritionAnalysisView extends JFrame {

    private NutritionAnalysisController controller;
    private NutritionAnalysisViewModel viewModel;
    private final JTextArea resultArea;

    public NutritionAnalysisView(NutritionAnalysisViewModel nutritionAnalysisViewModel) {
        super("Nutrition Analysis result");

        this.viewModel = nutritionAnalysisViewModel;
        this.resultArea = new JTextArea();

        initializeUI();
        observeViewModelState();
    }

    private void initializeUI() {
        // Set size and default close operation for the JFrame
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create and set up the panel with result area
        JPanel panel = createPanelWithResultArea();
        add(panel);
    }

    private JPanel createPanelWithResultArea() {
        JPanel panel = new JPanel(new BorderLayout());

        // Configure the result area (JTextArea)
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void observeViewModelState() {
        // Observe changes to the "state" property of the view model
        viewModel.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())) {
                updateResultArea((NutritionAnalysisState) evt.getNewValue());
            }
        });
    }

    private void updateResultArea(NutritionAnalysisState state) {
        if (state != null) {
            String displayedNutritionInfo = classifyNutrients(state.getNutritionResults());
            resultArea.setText(displayedNutritionInfo);
        }
        else {
            resultArea.setText("State is Null");
        }
    }

    public static String classifyNutrients(List<String> nutrientInfos) {
        // Categorize nutrients
        Map<String, List<String>> categorizedNutrients = new HashMap<>();
        categorizedNutrients.put("Macronutrients", new ArrayList<>());
        categorizedNutrients.put("Micronutrients", new ArrayList<>());
        categorizedNutrients.put("Vitamins", new ArrayList<>());
        categorizedNutrients.put("Minerals", new ArrayList<>());
        categorizedNutrients.put("Others", new ArrayList<>());

        for (String info : nutrientInfos) {
            categorizeNutrient(info, categorizedNutrients);
        }

        // Build result string
        return buildResultString(categorizedNutrients).trim();
    }

    private static void categorizeNutrient(String info, Map<String, List<String>> categorizedNutrients) {
        String lowerInfo = info.trim().toLowerCase();

        if (containsAny(lowerInfo, "carbohydrate", "protein", "fat", "energy", "fiber", "water")) {
            categorizedNutrients.get("Macronutrients").add(info);
        }
        else if (lowerInfo.contains("vitamin")) {
            if (containsAny(lowerInfo, "a", "b", "c", "d", "e", "k")) {
                categorizedNutrients.get("Vitamins").add(info);
            }
            else {
                categorizedNutrients.get("Micronutrients").add(info);
            }
        }
        else if (containsAny(lowerInfo, "calcium", "iron", "magnesium", "phosphorus", "potassium", "sodium", "zinc")) {
            categorizedNutrients.get("Minerals").add(info);
        }
        else {
            categorizedNutrients.get("Others").add(info);
        }
    }

    private static boolean containsAny(String input, String... keywords) {
        boolean contains = false;
        for (String keyword : keywords) {
            if (input.contains(keyword)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    private static String buildResultString(Map<String, List<String>> categorizedNutrients) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, List<String>> entry : categorizedNutrients.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                result.append(entry.getKey()).append(":\n");
                for (String info : entry.getValue()) {
                    result.append("    • ").append(info).append("\n");
                }
            }
        }

        return result.toString();
    }

    public void setController(NutritionAnalysisController controller) {
        this.controller = controller;
    }

    public NutritionAnalysisController getController() {
        return controller;
    }
}


