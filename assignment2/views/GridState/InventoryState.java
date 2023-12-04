package views.GridState;

import AdventureModel.AdventureObject;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import views.AdventureGameView;
import views.GridState.GridState;

import java.util.ArrayList;

/**
 * Class InventoryState. The class models the Inventory Screen in the game.
 */
public class InventoryState extends GridStateWithItems
{
    VBox objectsInInventory = new VBox(); //to hold inventory items
    VBox powerUps = new VBox(); //to hold accrued power-ups
    VBox achievements; //to hold any achievements

    Button exitButton; //button to return back to Traversal Screen

    Label objLabel, inventoryLabel, powerUpLabel, achivementLabel;

    public InventoryState(String name, AdventureGameView view)
    {
        super();
        this.name = name;
        this.view = view;

        initUI();
    }

    @Override
    void initUI() {
        //Inventory items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);

        // GridPane, anyone?
        grid.setPadding(new Insets(20));
        grid.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(333);
        ColumnConstraints column2 = new ColumnConstraints(333);
        ColumnConstraints column3 = new ColumnConstraints(333);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column2.setHgrow(Priority.SOMETIMES);
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints(100);
        RowConstraints row2 = new RowConstraints(100);
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        grid.getColumnConstraints().addAll( column1 , column2 , column1 );
        grid.getRowConstraints().addAll( row1 , row2 , row1 );

        //Buttons
        exitButton = new Button("Exit");
        exitButton.setFont(new Font("Arial", textSize));
        exitButton.setId("Exit");
        customizeButton(exitButton, 100, 50);
        AdventureGameView.makeButtonAccessible(exitButton, "Exit Button", "This button exits out of the Inventory.", "This button takes you from inetory back to main screen.");
        addExitEvent();

        //Labels
        inventoryLabel =  new Label("Inventory");
        inventoryLabel.setAlignment(Pos.CENTER);
        inventoryLabel.setStyle("-fx-text-fill: white;");
        inventoryLabel.setFont(new Font("Arial", 30));
        inventoryLabel.setWrapText(true);

        objLabel =  new Label("Objects in Inventory");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Arial", textSize));
        objLabel.setWrapText(true);

        powerUpLabel =  new Label("Power Ups");
        powerUpLabel.setAlignment(Pos.CENTER);
        powerUpLabel.setStyle("-fx-text-fill: white;");
        powerUpLabel.setFont(new Font("Arial", textSize));
        powerUpLabel.setWrapText(true);

        achivementLabel =  new Label("Achievements");
        achivementLabel.setAlignment(Pos.CENTER);
        achivementLabel.setStyle("-fx-text-fill: white;");
        achivementLabel.setFont(new Font("Arial", textSize));
        achivementLabel.setWrapText(true);

        //add all the widgets to the GridPane
        grid.add(inventoryLabel, 0, 0, 1, 1 );  // Add label
        grid.add(exitButton, 2, 0, 1, 1);
        grid.add(objLabel, 0, 1, 1, 1);
        grid.add(powerUpLabel, 1, 1, 1, 1);
        grid.add(achivementLabel, 2, 1, 1, 1);

        updateScene(""); //method displays an image and whatever text is supplied
    }
    @Override
    public void updateScene(String s)
    {
        updateItems();
        updatePowerUps();
        updateAchievements();

        inventoryLabel.setFont(new Font("Arial", textSize));
        objLabel.setFont(new Font("Arial", textSize));
        powerUpLabel.setFont(new Font("Arial", textSize));
        achivementLabel.setFont(new Font("Arial", textSize));
    }

    @Override
    public void updateItems() {
        objectsInInventory.getChildren().clear();
        //write some code here to add images of objects in a given room to the objectsInRoom Vbox
        grid.setPadding(new Insets(20));
        grid.setBackground(new Background(new BackgroundFill(
                Color.valueOf(Backgcolor),
                new CornerRadii(0),
                new Insets(0)
        )));

        ArrayList<String> playerInv = this.view.model.player.getInventory();
        for (String x : playerInv) {
            Image currImg = new Image(this.view.model.getDirectoryName() + "/objectImages/" + x + ".jpg");
            Image img = new Image(this.view.model.getDirectoryName() + "/objectImages/" + x + ".jpg");
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(100);
            imgView.setPreserveRatio(true);
            Button button = new Button(x, imgView);
            button.setContentDisplay(ContentDisplay.TOP);
            makeButtonAccessible(button, x + " Object Button", "This button represents the " + x + " object.", "This button represents the object " + x + ". Click it to pick up the object.");
            button.setOnAction(e -> {
                this.view.model.interpretAction("drop " + x);
                updateScene("");
            });
            objectsInInventory.getChildren().add(button);
        }

        ScrollPane scO = new ScrollPane(objectsInInventory);
        scO.setPadding(new Insets(10));

        switch (Backgcolor) {
            case "Black":
                grid.setBackground(new Background(new BackgroundFill(
                        Color.BLACK,
                        new CornerRadii(0),
                        new Insets(0)
                )));
                scO.setStyle("-fx-background: "+ Backgcolor + " ; -fx-background-color:" + Backgcolor +" ;");
                break;
            case "Grey":
                grid.setBackground(new Background(new BackgroundFill(
                        Color.GREY,
                        new CornerRadii(0),
                        new Insets(0)
                )));
                scO.setStyle("-fx-background: "+ Backgcolor + " ; -fx-background-color:" + Backgcolor +" ;");
                break;
            case "Pink":
                grid.setBackground(new Background(new BackgroundFill(
                        Color.PINK,
                        new CornerRadii(0),
                        new Insets(0)
                )));
                scO.setStyle("-fx-background: "+ Backgcolor + " ; -fx-background-color:" + Backgcolor +" ;");
                break;
            case "Orange":
                grid.setBackground(new Background(new BackgroundFill(
                        Color.ORANGE,
                        new CornerRadii(0),
                        new Insets(0)
                )));
                scO.setStyle("-fx-background: "+ Backgcolor + " ; -fx-background-color:" + Backgcolor +" ;");
                break;
            case "Rosy Orange":
                grid.setBackground(new Background(new BackgroundFill(
                        Color.ROSYBROWN,
                        new CornerRadii(0),
                        new Insets(0)
                )));
                scO.setStyle("-fx-background: "+ Backgcolor + " ; -fx-background-color:" + Backgcolor +" ;");
                break;

            default:
                // Handle unknown color
                break;
        }

        scO.setStyle("-fx-background: "+ Backgcolor + " ; -fx-background-color:" + Backgcolor +" ;");
        scO.setFitToWidth(true);
        scO.setFitToHeight(true);
        grid.add(scO,0,2, 1, 1);

        ColorAdjust bright = new ColorAdjust();
        bright.setBrightness(brightness);
        bright.setContrast(Contrast);
        grid.setEffect(bright);
    }

    /**
     * Updates PowerUps
     */
    public void updatePowerUps()
    {
        powerUps.getChildren().clear();
        ArrayList<String> playerInv = this.view.model.player.getPowerInventory();
        for (String x : playerInv) {
            Image img = new Image(this.view.model.getDirectoryName() + "/objectImages/" + x + ".jpg");
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(100);
            imgView.setPreserveRatio(true);
            Button button = new Button(x, imgView);
            button.setContentDisplay(ContentDisplay.TOP);
            makeButtonAccessible(button, x + " Object Button", "This button represents the " + x + " object.", "This button represents the object " + x + ". Click it to pick up the object.");
            button.setOnAction(e -> {
                this.view.model.interpretAction("drop " + x);
                updateScene("");
            });
            powerUps.getChildren().add(button);
        }

        ScrollPane scO = new ScrollPane(powerUps);
        scO.setPadding(new Insets(10));
        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setFitToWidth(true);
        scO.setFitToHeight(true);
        grid.add(scO,1,2, 1, 1);

        ColorAdjust bright = new ColorAdjust();
        bright.setBrightness(brightness);
        bright.setContrast(Contrast);
        grid.setEffect(bright);
    }

    /**
     * Updates Acehievements
     */
    public void updateAchievements()
    {
        // To be implemented
    }

    /**
     * Adds a mouse event to the Exit Button
     */
    public void addExitEvent() {
        exitButton.setOnAction(e -> {
            grid.requestFocus();
            this.view.changeState("Traversal");
        });
    }
}
