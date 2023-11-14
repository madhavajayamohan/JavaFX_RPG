package views.GridState;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import views.AdventureGameView;
import views.GridState.GridState;
import views.LoadView;
import views.SaveView;

import java.util.Arrays;
import java.util.HashSet;


public class TraversalState extends GridState
{
    Button saveButton, loadButton, helpButton, inventButton, replayButton, settingsButton; //buttons
    Boolean helpToggle = false; //is help on display?

    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    VBox objectsInRoom = new VBox(); //to hold room items
    ImageView roomImageView; //to hold room image
    TextField inputTextField; //for user input

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
        // setting up the stage
        this.view.stage.setTitle("tiowille's Adventure Game"); //Replace <YOUR UTORID> with your UtorID

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

        // Buttons
        saveButton = new Button("Save");
        saveButton.setId("Save");
        customizeButton(saveButton, 100, 50);
        makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();

        loadButton = new Button("Load");
        loadButton.setId("Load");
        customizeButton(loadButton, 100, 50);
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();

        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        customizeButton(helpButton, 200, 50);
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(saveButton, helpButton, loadButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        inventButton = new Button("Inventory");
        saveButton.setId("Inventory");
        customizeButton(inventButton, 100, 50);
        makeButtonAccessible(inventButton, "Inventory Button", "This button gives access to inventory.", "This button gives access to inventory. Click it in order to access your items, power ups and achievements.");
        addInventEvent();

        loadButton = new Button("Load");
        loadButton.setId("Load");
        customizeButton(loadButton, 100, 50);
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();

        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        customizeButton(helpButton, 200, 50);
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();

        inputTextField = new TextField();
        inputTextField.setFont(new Font("Arial", 16));
        inputTextField.setFocusTraversable(true);

        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field

        //labels for inventory and room items
        Label objLabel =  new Label("Objects in Room");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Arial", 16));

        Label invLabel =  new Label("Your Inventory");
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", 16));

        //add all the widgets to the GridPane
        grid.add( objLabel, 0, 0, 1, 1 );  // Add label
        grid.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        grid.add( invLabel, 2, 0, 1, 1 );  // Add label

        Label commandLabel = new Label("What would you like to do?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Arial", 16));

        updateScene(""); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms

        // adding the text area and submit button to a VBox
        VBox textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 0, 2, 3, 1 );

        // Render everything
        var scene = new Scene( gridPane ,  1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
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
        stopArticulation(); //if speaking, stop

        if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription();
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (!objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            articulateRoomDescription(); //all we want, if we are looking, is to repeat description.
            return;
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            showInstructions();
            return;
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            showCommands(); //this is new!  We did not have this command in A1
            return;
        }

        //try to move!
        String output = this.model.interpretAction(text); //process the command!

        if (output == null || (!output.equals("GAME OVER") && !output.equals("FORCED") && !output.equals("HELP"))) {
            updateScene(output);
            updateItems();
            for (Node x : gridPane.getChildren()) {
                if (gridPane.getRowIndex(x) == 2 && gridPane.getColumnIndex(x) == 0) {
                    x.setDisable(false);
                }
                if (gridPane.getRowIndex(x) == 1 && gridPane.getColumnIndex(x) == 0) {
                    x.setDisable(false);
                }
                if (gridPane.getRowIndex(x) == 1 && gridPane.getColumnIndex(x) == 2) {
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
        } else if (output.equals("FORCED")) {
            //write code here to handle "FORCED" events!
            //Your code will need to display the image in the
            //current room and pause, then transition to
            //the forced room.
            updateScene("");
            updateItems();
            for (Node x : gridPane.getChildren()) {
                if (gridPane.getRowIndex(x) == 2 && gridPane.getColumnIndex(x) == 0) {
                    x.setDisable(true);
                }
                if (gridPane.getRowIndex(x) == 1 && gridPane.getColumnIndex(x) == 0) {
                    x.setDisable(true);
                }
                if (gridPane.getRowIndex(x) == 1 && gridPane.getColumnIndex(x) == 2) {
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
        String roomCommands = model.getPlayer().getCurrentRoom().getCommands();
        HashSet<String> commands = new HashSet<String>(Arrays.asList(roomCommands.split(",")));
        String outputCommands = commands.toString();
        updateScene("Movable Directions:\n" + outputCommands.substring(1, outputCommands.length()-1));
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
            //stopArticulation();
            helpToggle = false;
        } else {
            //stopArticulation();
            grid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == 1 && GridPane.getRowIndex(node) == 1);
            Label help = new Label(model.getInstructions());
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
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            //stopArticulation(); //if speaking, stop
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
            SaveView saveView = new SaveView(this);
        });
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            grid.requestFocus();
            LoadView loadView = new LoadView(this);
        });
    }
}
