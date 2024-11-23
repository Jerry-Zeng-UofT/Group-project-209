package view;


import interface_adapter.nutrition_analysis.NutritionAnalysisController;

import java.awt.*;
import javax.swing.*;

public class NutritionAnalysisView extends JFrame {

    private NutritionAnalysisController controller;

    public NutritionAnalysisView() {
        // Set the title of the JFrame
        super("Nutrition Analysis result");

        // Set size and default close operation
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add some content to display results
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel resultLabel = new JLabel("Nutrition Analysis result", SwingConstants.CENTER);
        panel.add(resultLabel, BorderLayout.CENTER);

        // Add the panel to the frame
        add(panel);
    }

    public void setController(NutritionAnalysisController controller) {
        this.controller = controller;
    }
}

