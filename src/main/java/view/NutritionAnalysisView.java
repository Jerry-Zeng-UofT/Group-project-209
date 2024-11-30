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

public class NutritionAnalysisView extends JFrame {

    private NutritionAnalysisController controller;
    private NutritionAnalysisViewModel viewModel;

    public NutritionAnalysisView(NutritionAnalysisViewModel nutritionAnalysisViewModel) {
        // Set the title of the JFrame
        super("Nutrition Analysis result");

        this.viewModel = nutritionAnalysisViewModel;

        // Set size and default close operation
        setSize(400, 600);
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
                    StringBuilder displayedNutrionInfo = new StringBuilder();
                    for (String nutrient : state.getNutritionResults()) {
                        displayedNutrionInfo.append(nutrient).append("\n");
                    }
                    resultArea.setText(displayedNutrionInfo.toString());
                }
                else {
                    resultArea.setText("State is Null");
                }
            }
        });
    }

    public void setController(NutritionAnalysisController controller) {
        this.controller = controller;
    }

    public NutritionAnalysisController getController() {
        return controller;
    }
}


