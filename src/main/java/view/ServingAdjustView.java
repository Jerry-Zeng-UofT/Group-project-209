package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ServingAdjustView extends JPanel {
    private static final int DEFAULT_SERVINGS = 1;
    private final JTextField servingInputField;
    private final JButton updateButton;

    public ServingAdjustView(ActionListener updateAction) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(new JLabel("Servings:"));
        servingInputField = new JTextField(String.valueOf(DEFAULT_SERVINGS), 5);
        servingInputField.setMaximumSize(new Dimension(50, 25));
        add(servingInputField);

        updateButton = new JButton("Update");
        updateButton.addActionListener(updateAction);
        add(Box.createHorizontalStrut(10));
        add(updateButton);
    }

    /**
     * Gets the servings value entered by the user.
     * @return The servings value as an integer.
     */
    public int getServings() {
        try {
            int servings = Integer.parseInt(servingInputField.getText().trim());
            if (servings <= 0) {
                throw new NumberFormatException("Servings must be a positive number.");
            }
            return servings;
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive number for servings.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            servingInputField.setText(String.valueOf(DEFAULT_SERVINGS));
            return DEFAULT_SERVINGS;
        }
    }

    /**
     * Sets the servings value in the input field.
     * @param servings The servings value to set.
     */
    public void setServings(int servings) {
        servingInputField.setText(String.valueOf(servings));
    }
}