package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ServingAdjustView extends JPanel {
    private final JTextField servingInputField;
    private final JButton updateButton;

    public ServingAdjustView(ActionListener updateAction) {
        setLayout(new FlowLayout());

        add(new JLabel("Servings:"));
        servingInputField = new JTextField("1", 5);
        add(servingInputField);

        updateButton = new JButton("Update");
        updateButton.addActionListener(updateAction);
        add(updateButton);
    }

    public int getServings() {
        try {
            return Integer.parseInt(servingInputField.getText().trim());
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for servings.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return 1;
        }
    }
}