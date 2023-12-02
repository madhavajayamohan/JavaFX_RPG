package views.GridState;

import AdventureModel.AdventureObject;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import views.AdventureGameView;
import views.GridState.GridState;
import views.LoadView;
import views.SaveView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class TraversalState extends GridStateWithItems
{
    Button saveButton, loadButton, helpButton, inventButton, replayButton, settingsButton; //buttons
    Boolean helpToggle = false; //is help on display?

    Label objLabel, commandLabel;

    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    VBox objectsInRoom = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox(); //to hold inventory items
    ImageView roomImageView; //to hold room image
    TextField inputTextField; //for user input

    /**
     * Boolean toggle for hint display
     */
    Boolean hintToggle = false;

    public TraversalState(String name, AdventureGameView view)
    {
        super();
        this.name = name;
        this.view = view;

        initUI();
    }

    @Override
    public void initUI()
    {
        //Room items
        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);

        // GridPane, anyone?
        grid.setPadding(new Insets(20));
        grid.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(650);
        ColumnConstraints column3 = new ColumnConstraints(150);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints( 550 );
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        grid.getColumnConstraints().addAll( column1 , column2 , column1 );
        grid.getRowConstraints().addAll( row1 , row2 , row1 );

        // Top Buttons
        saveButton = new Button("Save");
        saveButton.setId("Save");
        saveButton.setFont(new Font("Arial", textSize));
        customizeButton(saveButton, 100, 50);
        AdventureGameView.makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();

        loadButton = new Button("Load");
        loadButton.setId("Load");
        loadButton.setFont(new Font("Arial", textSize));
        customizeButton(loadButton, 100, 50);
        AdventureGameView.makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();

        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        helpButton.setFont(new Font("Arial", textSize));
        customizeButton(helpButton, 200, 50);
        AdventureGameView.makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(saveButton, helpButton, loadButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        // Side Buttons
        inventButton = new Button("Inventory");
        inventButton.setId("Inventory");
        inventButton.setFont(new Font("Arial", textSize));
        customizeButton(inventButton, 100, 50);
        AdventureGameView.makeButtonAccessible(saveButton, "Inventory Button", "This button takes you to your inventory.", "This button takes you to your inventory. Click it to look at your items, achievement, and power-ups.");
        addInventEvent();

        replayButton = new Button("Replay");
        replayButton.setId("Replay");
        replayButton.setFont(new Font("Arial", textSize));
        customizeButton(replayButton, 100, 50);
        AdventureGameView.makeButtonAccessible(loadButton, "Replay Button", "This button replays audio.", "This button replays the room description audio.");
        addReplayEvent();

        settingsButton = new Button("Settings");
        settingsButton.setId("Settings");
        settingsButton.setFont(new Font("Arial", textSize));
        customizeButton(settingsButton, 200, 50);
        AdventureGameView.makeButtonAccessible(settingsButton, "Settings Button", "This button takes you to settings.", "This button takes you to settings. Click it to change features like brightness, text size, and color contrast.");
        addSettingsEvent();

        VBox sideButtons = new VBox();
        sideButtons.getChildren().addAll(inventButton, replayButton, settingsButton);
        sideButtons.setSpacing(30);
        sideButtons.setAlignment(Pos.CENTER);

        //labels for room items
        objLabel =  new Label("Objects in Room");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Arial", textSize));
        objLabel.setWrapText(true);

        //add all the widgets to the GridPane
        grid.add( objLabel, 0, 0, 1, 1 );  // Add label
        grid.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        grid.add(sideButtons, 2, 1, 1, 1);

        commandLabel = new Label("What would you like to do?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Arial", textSize));
        commandLabel.setWrapText(true);

        inputTextField = new TextField();
        inputTextField.setFont(new Font("Arial", textSize));
        inputTextField.setFocusTraversable(true);

        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field

        // adding the text area and submit button to a VBox
        VBox textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        grid.add( textEntry, 0, 2, 3, 1 );

        updateScene(""); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    public void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the myTextField attribute
     *
     * Your event handler should respond when users
     * hits the ENTER or TAB KEY. If the user hits
     * the ENTER Key, strip white space from the
     * input to myTextField and pass the stripped
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus
     * of the scene onto any other node in the scene
     * graph by invoking requestFocus method.
     */
    private void addTextHandlingEvent() {
        inputTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    String inputText = inputTextField.getText().strip();
                    submitEvent(inputText);
                    inputTextField.setText("");
                }
                if (event.getCode().equals(KeyCode.TAB)) {
                    objectsInRoom.requestFocus();
                    inputTextField.setText("");
                }
            }
        });
    }


    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
     */
    private void submitEvent(String text) {

        text = text.strip(); //get rid of white space
        view.stopArticulation(); //if speaking, stop

        if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
            String roomDesc = this.view.model.getPlayer().getCurrentRoom().getRoomDescription();
            String objectString = this.view.model.getPlayer().getCurrentRoom().getObjectString();
            if (!objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            view.articulateRoomDescription(); //all we want, if we are looking, is to repeat description.
            return;
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            showInstructions();
            return;
        } else if (text.equalsIgnoreCase("HINT")) {
            showHint();
            return;
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            showCommands(); //this is new!  We did not have this command in A1
            return;
        }

        //try to move!
        String output = this.view.model.interpretAction(text); //process the command!

        if (output == null || (!output.equals("GAME OVER") && !output.equals("FORCED") && !output.equals("HELP") && !output.equals("TROLL"))) {
            updateScene(output);
            updateItems();
            for (Node x : grid.getChildren()) {
                if (GridPane.getRowIndex(x) == 2 && GridPane.getColumnIndex(x) == 0) {
                    x.setDisable(false);
                }
                if (GridPane.getRowIndex(x) == 1 && GridPane.getColumnIndex(x) == 0) {
                    x.setDisable(false);
                }
                if (GridPane.getRowIndex(x) == 1 && GridPane.getColumnIndex(x) == 2) {
                    x.setDisable(false);
                }

            }
        } else if (output.equals("GAME OVER")) {
            updateScene("");
            updateItems();
            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event -> {
                Platform.exit();
            });
            pause.play();
        } else if (output.equals("TROLL")) {
            this.view.inTrollGame = true;
            this.view.changeState("Troll");
        } else if (output.equals("FORCED")) {
            //write code here to handle "FORCED" events!
            //Your code will need to display the image in the
            //current room and pause, then transition to
            //the forced room.
            updateScene("");
            updateItems();
            for (Node x : grid.getChildren()) {
                if (GridPane.getRowIndex(x) == 2 && GridPane.getColumnIndex(x) == 0) {
                    x.setDisable(true);
                }
                if (GridPane.getRowIndex(x) == 1 && grid.getColumnIndex(x) == 0) {
                    x.setDisable(true);
                }
                if (GridPane.getRowIndex(x) == 1 && GridPane.getColumnIndex(x) == 2) {
                    x.setDisable(true);
                }
            }
            PauseTransition pause = new PauseTransition(Duration.seconds(6));
            pause.setOnFinished(event -> {
                submitEvent("FORCED");
            });
            pause.play();
        }
    }

    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the
     * current room.
     */
    private void showCommands() {
        String roomCommands = view.model.getPlayer().getCurrentRoom().getCommands();
        HashSet<String> commands = new HashSet<String>(Arrays.asList(roomCommands.split(",")));
        String outputCommands = commands.toString();
        updateScene("Movable Directions:\n" + outputCommands.substring(1, outputCommands.length()-1));
    }


    /**
     * updateScene
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be dispplayed
     * below the image.
     *
     * @param textToDisplay the text to display below the image.
     */
    public void updateScene(String textToDisplay) {

        getRoomImage(); //get the image of the current room
        formatText(textToDisplay); //format the text to display
        roomDescLabel.setPrefWidth(500);
        roomDescLabel.setPrefHeight(500);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);
        ScrollPane toScroll = new ScrollPane(roomDescLabel);
        toScroll.setStyle("-fx-background: rgb(0,0,0)");

        VBox roomPane = new VBox(roomImageView,toScroll);
        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);
        roomPane.setStyle("-fx-background-color: #000000;");

        inputTextField.setFont(new Font("Arial", textSize));
        objLabel.setFont(new Font("Arial", textSize));
        commandLabel.setFont(new Font("Arial", textSize));

        grid.add(roomPane, 1, 1);
        ColorAdjust bright = new ColorAdjust();
        bright.setBrightness(brightness);
        grid.setEffect(bright);
        this.view.stage.sizeToScene();

        //finally, articulate the description
        if (textToDisplay == null || textToDisplay.isBlank()) view.articulateRoomDescription();
    }
    /**
     * formatText
     * __________________________
     *
     * Format text for display.
     *
     * @param textToDisplay the text to be formatted for display.
     */
    private void formatText(String textToDisplay) {
        if (textToDisplay == null || textToDisplay.isBlank()) {
            String roomDesc = this.view.model.getPlayer().getCurrentRoom().getRoomDescription() + "\n";
            String objectString = this.view.model.getPlayer().getCurrentRoom().getObjectString();
            if (objectString != null && !objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            else roomDescLabel.setText(roomDesc);
        } else roomDescLabel.setText(textToDisplay);
        roomDescLabel.setStyle("-fx-text-fill: white;");
        roomDescLabel.setFont(new Font("Arial", textSize));
        roomDescLabel.setAlignment(Pos.TOP_CENTER);
    }

    /**
     * getRoomImage
     * __________________________
     *
     * Get the image for the current room and place
     * it in the roomImageView
     */
    private void getRoomImage() {

        int roomNumber = this.view.model.getPlayer().getCurrentRoom().getRoomNumber();
        String roomImage = this.view.model.getDirectoryName() + "/room-images/" + roomNumber + ".png";

        Image roomImageFile = new Image(new File(roomImage).toURI().toString());
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(400);

        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(this.view.model.getPlayer().getCurrentRoom().getRoomDescription());
        roomImageView.setFocusTraversable(true);
    }

    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the objectsInRoom and objectsInInventory Vboxes.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     *
     * Images of each object are in the assets
     * folders of the given adventure game.
     */
    public void updateItems() {
        objectsInRoom.getChildren().clear();
        //write some code here to add images of objects in a given room to the objectsInRoom Vbox
        ArrayList<AdventureObject> roomObj = this.view.model.player.getCurrentRoom().objectsInRoom;
        for (AdventureObject x : roomObj) {
            Image currImg = new Image(new File(this.view.model.getDirectoryName() + "/objectImages/" + x.getName() + ".jpg").toURI().toString());
            ImageView currImgView = new ImageView(currImg);
            currImgView.setFitWidth(100);
            currImgView.setPreserveRatio(true);
            Button currButton = new Button(x.getName(), currImgView);
            currButton.setContentDisplay(ContentDisplay.TOP);
            currButton.setFont(new Font(textSize));
            makeButtonAccessible(currButton, x.getName() + " Object Button", "This button represents the " + x.getName() + " object.", "This button represents the object " + x.getName() +". Click it to pick up the object.");
            currButton.setOnAction(e -> {
                submitEvent("take " + x.getName());
            });
            currButton.hoverProperty().addListener((e, notHovered, hovered) -> {
                if (hovered) {
                    currButton.setText(x.getDescription());
                } else {
                    currButton.setText(x.getName());
                }
                currButton.setWrapText(true);
            });
            objectsInRoom.getChildren().add(currButton);
        }

        //write some code here to add images of objects in a player's inventory room to the objectsInInventory Vbox

        //please use setAccessibleText to add "alt" descriptions to your images!
        //the path to the image of any is as follows:
        //this.model.getDirectoryName() + "/objectImages/" + objectName + ".jpg";

        ScrollPane scO = new ScrollPane(objectsInRoom);
        scO.setPadding(new Insets(10));
        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setFitToWidth(true);
        grid.add(scO,0,1);
    }

    /*
     * Show the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {

        if (helpToggle) {
            grid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == 1 && GridPane.getRowIndex(node) == 1);
            updateScene("");
            view.stopArticulation();
            helpToggle = false;
        } else {
            view.stopArticulation();
            grid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == 1 && GridPane.getRowIndex(node) == 1);
            Label help = new Label(this.view.model.getInstructions());
            help.setPrefWidth(500);
            help.setPrefHeight(500);
            help.setTextOverrun(OverrunStyle.CLIP);
            help.setWrapText(true);
            help.setStyle("-fx-text-fill: white;");
            help.setFont(new Font("Arial", 16));
            help.setAlignment(Pos.CENTER);
            VBox helpPane = new VBox(help);
            helpPane.setPadding(new Insets(10));
            helpPane.setAlignment(Pos.TOP_CENTER);
            helpPane.setStyle("-fx-background-color: #000000;");
            grid.add(helpPane, 1, 1);
            helpToggle = true;
        }
    }

    /**
     * Shows the hint for the given room
     */
    public void showHint() {

        if (hintToggle) {
            updateScene("");
            view.stopArticulation();
            hintToggle = false;
        } else {
            updateScene(this.view.model.player.getCurrentRoom().getHint());
            view.stopArticulation();
            hintToggle = true;
        }
    }

    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            view.stopArticulation(); //if speaking, stop
            showInstructions();
        });
    }

    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            grid.requestFocus();
            SaveView saveView = new SaveView(this.view);
        });
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            grid.requestFocus();
            LoadView loadView = new LoadView(this.view);
        });
    }

    public void addInventEvent() {
        inventButton.setOnAction(e -> {
            grid.requestFocus();
            this.view.changeState("Inventory");
        });
    }
    /**
     * This method handles the event relayed to the replay audio button.
     * If audio is already playing, stop it first then proceed to play the current room description audio.
    * */
    public void addReplayEvent() {
        replayButton.setOnAction(e -> {
            grid.requestFocus();
            this.view.stopArticulation();
            this.view.articulateRoomDescription();
        });
    }

    public void addSettingsEvent() {
        settingsButton.setOnAction(e -> {
            grid.requestFocus();
            this.view.changeState("Settings");
        });
    }
}
