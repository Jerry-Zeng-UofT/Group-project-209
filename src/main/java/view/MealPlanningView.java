package view;

import interface_adapter.meal_planning.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MealPlanningView extends JPanel implements ActionListener, PropertyChangeListener {
    private static final int CALENDAR_COLUMNS = 7;
    private static final int VERTICAL_GAP = 10;
    private static final int HORIZONTAL_GAP = 10;

    private final MealPlanningViewModel viewModel;
    private MealPlanningController controller;

    private final JPanel calendarPanel;
    private final JButton prevWeekButton;
    private final JButton nextWeekButton;
    private final JLabel weekLabel;

    public MealPlanningView(MealPlanningViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(HORIZONTAL_GAP, VERTICAL_GAP));

        // Title panel
        JLabel titleLabel = new JLabel(viewModel.TITLE_LABEL);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Navigation panel
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        prevWeekButton = new JButton(MealPlanningViewModel.PREV_WEEK_BUTTON_LABEL);
        nextWeekButton = new JButton(MealPlanningViewModel.NEXT_WEEK_BUTTON_LABEL);
        weekLabel = new JLabel();

        navigationPanel.add(prevWeekButton);
        navigationPanel.add(weekLabel);
        navigationPanel.add(nextWeekButton);

        // Calendar panel
        calendarPanel = new JPanel(new GridLayout(0, CALENDAR_COLUMNS, 5, 5));

        // Assembly
        add(titleLabel, BorderLayout.NORTH);
        add(navigationPanel, BorderLayout.CENTER);
        add(new JScrollPane(calendarPanel), BorderLayout.SOUTH);

        // Add listeners
        prevWeekButton.addActionListener(this);
        nextWeekButton.addActionListener(this);

        // Initial update
        updateWeekLabel();
        refreshCalendar();
    }

    private void updateWeekLabel() {
        MealPlanningState state = viewModel.getState();
        if (state != null) {
            LocalDate weekStart = state.getCurrentWeekStart();
            LocalDate weekEnd = weekStart.plusDays(6);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
            weekLabel.setText(String.format("%s - %s",
                    weekStart.format(formatter),
                    weekEnd.format(formatter)));
        }
    }
}
