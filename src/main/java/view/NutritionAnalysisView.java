package view;


import interface_adapter.nutrition_analysis.NutritionAnalysisController;
import interface_adapter.nutrition_analysis.NutritionAnalysisState;
import interface_adapter.nutrition_analysis.NutritionAnalysisViewModel;

import java.awt.*;
import javax.swing.*;

public class NutritionAnalysisView extends JFrame {

    private NutritionAnalysisController controller;
    private NutritionAnalysisViewModel viewModel;

    public NutritionAnalysisView(NutritionAnalysisViewModel viewModel) {
        // Set the title of the JFrame
        super("Nutrition Analysis result");

        this.viewModel = viewModel;

        // Set size and default close operation
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add some content to display results
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        NutritionAnalysisState state = (NutritionAnalysisState) viewModel.getState();
        if (state != null) {
            String wantToShow = state.getNutritionResults().toString();
            JLabel resultLabel = new JLabel(wantToShow, SwingConstants.CENTER);
            panel.add(resultLabel, BorderLayout.CENTER);
        }
        else {
            JLabel resultLabel = new JLabel("State is Null", SwingConstants.CENTER);
            panel.add(resultLabel, BorderLayout.CENTER);
        }

        // Add the panel to the frame
        add(panel);
    }

    public void setController(NutritionAnalysisController controller) {
        this.controller = controller;
    }

    public NutritionAnalysisController getController() {
        return controller;
    }
}


