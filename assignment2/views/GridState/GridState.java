package views.GridState;

import javafx.scene.AccessibleRole;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import views.AdventureGameView;

public abstract class GridState
{
    public String name;
    public GridPane grid = new GridPane();
    public AdventureGameView view;
    public static double brightness = 0.0;
    public static double textSize = 15;

    /**
     * Initialize the UI
     */
    abstract void initUI();

    public boolean isName(String name)
    {
        return this.name.equals(name);
    }

    public GridPane getGrid()
    {
        return grid;
    }

    public void updateScene(String s)
    {

    }

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
