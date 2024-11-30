package view;

import interface_adapter.start.FrontPageController;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrontPageView extends JFrame implements ActionListener {

    private final JButton buttonA;
    private final JButton buttonB;

    private final RecipeSearchView recipeSearchView;
    private final MealPlanningView mealPlanningView;

    public FrontPageView(RecipeSearchView recipeSearchview, MealPlanningView mealPlanningView) {
        super("Start Page");

        this.recipeSearchView = recipeSearchview;
        this.mealPlanningView = mealPlanningView;

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new ImagePanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Recipe Wiz", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextPane textPane = getjTextPane();

        Style style = textPane.addStyle("center", null);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);

        StyledDocument doc = textPane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), style, false);

        panel.add(Box.createVerticalStrut(40));

        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonA = new JButton("Recipe Search");
        buttonB = new JButton("Meal Planner");

        buttonA.addActionListener(this);
        buttonB.addActionListener(this);

        buttonPanel.add(buttonA);
        buttonPanel.add(buttonB);

        panel.add(buttonPanel);

        panel.add(textPane);

        panel.add(Box.createVerticalStrut(20));

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    @NotNull
    private JTextPane getjTextPane() {
        JTextPane textPane = new JTextPane();
        textPane.setText("Search recipe allows you to edit servings sizes, add custom dietary restrictions and analyze nutrition's values");
        textPane.setEditable(false);
        textPane.setFont(new Font("Roboto", Font.BOLD, 16));
        textPane.setBackground(new Color(0, 0, 0, 0));
        textPane.setBackground(getBackground());
        textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPane.setPreferredSize(new Dimension(350, 100));
        return textPane;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(buttonA)) {
            setVisible(false);
            FrontPageController.goToRecipeSearch(recipeSearchView, mealPlanningView);
        }

        else if (evt.getSource().equals(buttonB)) {
            setVisible(false);
            FrontPageController.goToMealPlanning(recipeSearchView, mealPlanningView);
        }
    }
}