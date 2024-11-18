package view;

import java.awt.*;
import javax.swing.*;

/**
 * A view for the starting page.
 */
public class StartPageView extends JFrame {

    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;
    private static final int TITLE_FONT_SIZE = 20;
    private static final int TITLE_PADDING_TOP = 80;
    private static final int TITLE_BUTTON_SPACING = 50;
    private static final int BUTTON_HORIZONTAL_SPACING = 90;
    private static final int BUTTON_VERTICAL_SPACING = 20;

    public StartPageView() {
        super("Login to Start");

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Welcome to Recipe & Meal Planner!", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(TITLE_PADDING_TOP));

        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(TITLE_BUTTON_SPACING));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, BUTTON_HORIZONTAL_SPACING, BUTTON_VERTICAL_SPACING));
        JButton signUpButton = new JButton("Sign Up");
        JButton logInButton = new JButton("Log In");
        buttonPanel.add(signUpButton);
        buttonPanel.add(logInButton);

        panel.add(buttonPanel);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }
}
