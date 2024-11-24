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
    private String wantToShow;

    public NutritionAnalysisView(NutritionAnalysisViewModel nutritionAnalysisViewModel) {
        // Set the title of the JFrame
        super("Nutrition Analysis result");

        this.viewModel = nutritionAnalysisViewModel;

        // Set size and default close operation
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add some content to display results
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // result area
        JLabel resultLabel = new JLabel("State is Null", SwingConstants.CENTER);
        panel.add(resultLabel, BorderLayout.CENTER);

        // Add the panel to the frame
        add(panel);

        viewModel.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())) {
                NutritionAnalysisState state = (NutritionAnalysisState) evt.getNewValue();
                if (state != null) {
                    resultLabel.setText(state.getNutritionResults().toString());
                }
                else {
                    resultLabel.setText("State is Null");
                }
                panel.revalidate();  // Refresh the panel
                panel.repaint();     // Ensure UI updates
            }
        }
        );
    }

    public void setController(NutritionAnalysisController controller) {
        this.controller = controller;
    }

    public NutritionAnalysisController getController() {
        return controller;
    }
}


