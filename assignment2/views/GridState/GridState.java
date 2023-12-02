package views.GridState;

import javafx.scene.AccessibleRole;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import views.AdventureGameView;

/**
 * Class GridState. Abstract class that is based off of State design pattern.
 * The template for each GridState– a GridState describes teh different screens of the game
 * and has associated with it all methods to update the screen and change the screen.
 */

public abstract class GridState
{
    public String name;
    public GridPane grid = new GridPane();
    public AdventureGameView view;
    public static double brightness = 0.0;
    public static double textSize = 16;

    public static String Backgcolor = "black";

    public static ColorAdjust highContrastEffect = new ColorAdjust();



    /**
     * Initialize the UI
     */
    abstract void initUI();

    /**
     * Checks if name of GridState is correct
     */
    public boolean isName(String name)
    {
        return this.name.equals(name);
    }

    /**
     * Returns the GridPane of the GridState object
     */
    public GridPane getGrid()
    {
        return grid;
    }

    /**
     * Updates the GridPane after any changes
     */
    abstract public void updateScene(String s);


    void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }


}
