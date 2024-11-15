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

    private final RecipeSearchView mainFrame; // 主窗口引用，用于回传用户选择
    private final JTextArea resultArea; // 用于展示筛选结果

    // 添加筛选选项面板的实例变量
    private final JPanel dietPanel;
    private final JPanel healthPanel;
    private final JPanel cuisinePanel;

    // 筛选选项
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

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // 创建标签
        JLabel label = new JLabel("Select Filters:", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(label, BorderLayout.NORTH);

        // 创建中间面板，放置滚动复选框
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridLayout(3, 1, 10, 10)); // 3行布局

        // 使用类实例变量初始化筛选项面板
        dietPanel = createScrollableCheckBoxPanel("Diet Label", DIET_TYPES);
        healthPanel = createScrollableCheckBoxPanel("Health Label", DIET_RESTRICTIONS);
        cuisinePanel = createScrollableCheckBoxPanel("Cuisine Type", CUISINE_TYPES);

        checkBoxPanel.add(dietPanel);
        checkBoxPanel.add(healthPanel);
        checkBoxPanel.add(cuisinePanel);

        panel.add(checkBoxPanel, BorderLayout.CENTER);

        // 用于展示筛选结果的区域
        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);  // 不可编辑
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        // 添加 Cancel 和 Confirm 按钮
        JPanel buttonPanel = new JPanel();
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancelAction());
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> confirmAction());

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // 将面板添加到窗口
        add(panel);
        setVisible(true);
    }

    // 创建带滚动条的复选框面板
    private JPanel createScrollableCheckBoxPanel(String label, String[] items) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // 垂直排列复选框
        panel.setBorder(BorderFactory.createTitledBorder(label));

        // 为每个选项添加一个 JCheckBox
        for (String item : items) {
            panel.add(new JCheckBox(item));
        }

        // 创建一个滚动面板，包装 JPanel
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(400, 120)); // 设置滚动面板的大小

        JPanel wrappedPanel = new JPanel(new BorderLayout());
        wrappedPanel.add(scrollPane, BorderLayout.CENTER);

        return wrappedPanel;
    }

    // 获取面板中所有的 JCheckBox
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

    // 获取已选择的筛选内容
    public String getSelectedFilters() {
//        StringBuilder resultText = new StringBuilder("Selected Filters:\n");
        StringBuilder resultText = new StringBuilder();

        // Diet Label 选择的项
//        resultText.append("Diet Types: ");
        for (JCheckBox checkBox : getCheckBoxes(dietPanel)) {
            if (checkBox.isSelected()) resultText.append(checkBox.getText()).append(", ");
        }

        // 去掉最后的逗号和空格
        if (resultText.toString().endsWith(", ")) {
            resultText.setLength(resultText.length() - 2);
        }

        resultText.append("\n");

        // Health Label 选择的项
//        resultText.append("Health Labels: ");
        for (JCheckBox checkBox : getCheckBoxes(healthPanel)) {
            if (checkBox.isSelected()) resultText.append(checkBox.getText()).append(", ");
        }

        // 去掉最后的逗号和空格
        if (resultText.toString().endsWith(", ")) {
            resultText.setLength(resultText.length() - 2);
        }

        resultText.append("\n");

        // Cuisine Type 选择的项
//        resultText.append("Cuisine Types: ");
        for (JCheckBox checkBox : getCheckBoxes(cuisinePanel)) {
            if (checkBox.isSelected()) resultText.append(checkBox.getText()).append(", ");
        }

        // 去掉最后的逗号和空格
        if (resultText.toString().endsWith(", ")) {
            resultText.setLength(resultText.length() - 2);
        }

        return resultText.toString();
    }

    public Map<String, List<String>> getSelectedFiltersMap() {
        final Map<String, List<String>> filtersMap = new HashMap<>();

        // Diet Label 选择的项
        for (JCheckBox checkBox : getCheckBoxes(dietPanel)) {
            if (checkBox.isSelected()) {
                addValueToKey(filtersMap, "Diet Types", checkBox.getText());
            }
        }

        // Health Label 选择的项
        for (JCheckBox checkBox : getCheckBoxes(healthPanel)) {
            if (checkBox.isSelected()) {
                addValueToKey(filtersMap, "Health Labels", checkBox.getText());
            }
        }

        // Cuisine Type 选择的项
        for (JCheckBox checkBox : getCheckBoxes(cuisinePanel)) {
            if (checkBox.isSelected()) {
                addValueToKey(filtersMap, "Cuisine Types", checkBox.getText());
            }
        }

        return filtersMap;
    }

    // 添加值到指定的 key 中，如果 key 不存在则新建一个 List
    private static void addValueToKey(Map<String, List<String>> map, String key, String value) {
        // 如果 map 中已经存在这个 key，直接添加值到 List
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    // 点击 Cancel 按钮
    private void cancelAction() {
        // 直接回到主页面
        mainFrame.setVisible(true);
        this.setVisible(false);
    }

    // 点击 Confirm 按钮
    private void confirmAction() {
        String selectedFilters = getSelectedFilters();
        mainFrame.updateSelection(selectedFilters);
        this.setVisible(false);
    }
}

