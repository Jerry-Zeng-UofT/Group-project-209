package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterFrameView extends JFrame {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    private final RecipeSearchView mainFrame;
    private final JTextArea resultArea;

    private final JPanel dietPanel;
    private final JPanel healthPanel;
    private final JPanel cuisinePanel;

    private static final String[] DIET_TYPES = {"balanced", "high-fiber", "high-protein", "low-carb", "low-fat", "low-sodium"};
    private static final String[] DIET_RESTRICTIONS = {"alcohol-cocktail", "alcohol-free", "celery-free", "crustacean-free",
            "dairy-free", "DASH", "egg-free", "fish-free", "fodmap-free", "gluten-free", "immuno-supportive", "keto-friendly",
            "kidney-friendly", "kosher", "low-fat-abs", "low-potassium", "low-sugar", "lupine-free", "Mediterranean", "mollusk-free",
            "mustard-free", "no-oil-added", "paleo", "peanut-free", "pescatarian", "pork-free", "red-meat-free", "sesame-free",
            "shellfish-free", "soy-free", "sugar-conscious", "sulfite-free", "tree-nut-free", "vegan", "vegetarian", "wheat-free"};
    private static final String[] CUISINE_TYPES = {"American", "Asian", "British", "Caribbean", "Central Europe", "Chinese",
            "Eastern Europe", "French", "Indian", "Italian", "Japanese", "Kosher", "Mediterranean", "Mexican", "Middle Eastern",
            "Nordic", "South American", "South East Asian"};

    public FilterFrameView(RecipeSearchView mainFrame) {
        this.mainFrame = mainFrame;

        setTitle("Filter Page");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        JLabel label = new JLabel("Select Filters:", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(label, BorderLayout.NORTH);

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridLayout(3, 1, 10, 10));

        dietPanel = createScrollableCheckBoxPanel("Diet Label", DIET_TYPES);
        healthPanel = createScrollableCheckBoxPanel("Health Label", DIET_RESTRICTIONS);
        cuisinePanel = createScrollableCheckBoxPanel("Cuisine Type", CUISINE_TYPES);

        checkBoxPanel.add(dietPanel);
        checkBoxPanel.add(healthPanel);
        checkBoxPanel.add(cuisinePanel);

        panel.add(checkBoxPanel, BorderLayout.CENTER);

        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancelAction());
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> confirmAction());

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private JPanel createScrollableCheckBoxPanel(String label, String[] items) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(label));

        for (String item : items) {
            panel.add(new JCheckBox(item));
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(400, 120));

        JPanel wrappedPanel = new JPanel(new BorderLayout());
        wrappedPanel.add(scrollPane, BorderLayout.CENTER);

        return wrappedPanel;
    }

    private JCheckBox[] getCheckBoxes(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JScrollPane) {
                JPanel innerPanel = (JPanel) ((JScrollPane) component).getViewport().getView();
                Component[] components = innerPanel.getComponents();
                JCheckBox[] checkBoxes = new JCheckBox[components.length];
                for (int i = 0; i < components.length; i++) {
                    checkBoxes[i] = (JCheckBox) components[i];
                }
                return checkBoxes;
            }
        }
        return new JCheckBox[0];
    }

    public String getSelectedFilters() {
        StringBuilder resultText = new StringBuilder();

        // Diet Label
        for (JCheckBox checkBox : getCheckBoxes(dietPanel)) {
            if (checkBox.isSelected()) resultText.append(checkBox.getText()).append(", ");
        }

        if (resultText.toString().endsWith(", ")) {
            resultText.setLength(resultText.length() - 2);
        }

        resultText.append("\n");

        // Health Label
        for (JCheckBox checkBox : getCheckBoxes(healthPanel)) {
            if (checkBox.isSelected()) resultText.append(checkBox.getText()).append(", ");
        }

        if (resultText.toString().endsWith(", ")) {
            resultText.setLength(resultText.length() - 2);
        }

        resultText.append("\n");

        // Cuisine Type
        for (JCheckBox checkBox : getCheckBoxes(cuisinePanel)) {
            if (checkBox.isSelected()) resultText.append(checkBox.getText()).append(", ");
        }

        if (resultText.toString().endsWith(", ")) {
            resultText.setLength(resultText.length() - 2);
        }

        return resultText.toString();
    }

    public Map<String, List<String>> getSelectedFiltersMap() {
        final Map<String, List<String>> filtersMap = new HashMap<>();

        // Diet Label
        for (JCheckBox checkBox : getCheckBoxes(dietPanel)) {
            if (checkBox.isSelected()) {
                addValueToKey(filtersMap, "Diet Types", checkBox.getText());
            }
        }

        // Health Label
        for (JCheckBox checkBox : getCheckBoxes(healthPanel)) {
            if (checkBox.isSelected()) {
                addValueToKey(filtersMap, "Health Labels", checkBox.getText());
            }
        }

        // Cuisine Type
        for (JCheckBox checkBox : getCheckBoxes(cuisinePanel)) {
            if (checkBox.isSelected()) {
                addValueToKey(filtersMap, "Cuisine Types", checkBox.getText());
            }
        }

        return filtersMap;
    }

    private static void addValueToKey(Map<String, List<String>> map, String key, String value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    private void cancelAction() {
        mainFrame.setVisible(true);
        this.setVisible(false);
    }

    private void confirmAction() {
        String selectedFilters = getSelectedFilters();
        mainFrame.updateSelection(selectedFilters);
        this.setVisible(false);
    }
}

