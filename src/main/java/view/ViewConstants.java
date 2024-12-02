package view;

import java.awt.Color;

/**
 * Constants used throughout the view layer.
 * Contains layout measurements, colors, and standard options.
 */

public final class ViewConstants {

    public static final int VERTICAL_GAP = 10;
    public static final int HORIZONTAL_GAP = 10;
    public static final int BORDER_PADDING = 10;
    public static final int TITLE_SIZE = 16;
    public static final int PANEL_WIDTH = 250;

    public static final int DAYS_IN_WEEK = 7;
    public static final int BUTTON_WIDTH = 40;
    public static final int BUTTON_HEIGHT = 25;
    public static final Color COMPLETED_COLOR = new Color(200, 255, 200);
    public static final Color IN_PROGRESS_COLOR = new Color(255, 255, 200);
    public static final Color DEFAULT_COLOR = Color.WHITE;

    public static final String[] MEAL_TYPES = {"Breakfast", "Lunch", "Dinner", "Snack"};
    public static final String[] MEAL_STATUSES = {"Planned", "In Progress", "Completed"};

    private ViewConstants() {

    }
}
