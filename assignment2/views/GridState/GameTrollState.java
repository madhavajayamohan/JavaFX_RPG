package views.GridState;

import AdventureModel.Players.Player;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import views.AdventureGameView;
import views.LoadView;
import views.SaveView;

import java.util.ArrayList;
import java.util.Stack;

import static javafx.scene.paint.Paint.valueOf;

public class GameTrollState extends TrollState implements Runnable {

    Button saveButton, loadButton, replayButton, settingsButton; //buttons
    Boolean helpToggle = false; //is help on display?

    Label trollStatusLabel, playerStatusLabel, trollHPLabel, playerHPLabel, trollBaseAttack, playerBaseAttack, playerBaseDefense, playerLivesLabel, commandLabel;

    String trollSpeak = "You, puny human, dare to come on this path?\n" +
                        "These chambers are only meant for the strong– and no human is strong.\n" +
                        "These chambers are only meant for the strong– and no human is strong.\n" +
                        "Oho? I see that you can use some magic. Very well, then.\n" +
                        "Let us see how your magic matches up to my strength.\n\n" +
                        "LET US DO BATTLE!!!!\n\n" +
                        "===================\n" +
                        "QUEST: DEFEAT TROLL\n" +
                        "===================\n";

    String instructionText = "[You have two choices: select A, for Attack, or D, for Defense.]\n" +
                             "[Then, to activate your magic, guess an integer from 0 to 100]\n" +
                             "[The system will generate a random integer–\n" +
                             "the closer to that number, the more effective your attack or defense]\n" +
                             "[If you happen to select the random integer– your spell will gain immense power!!!]\n" +
                             "[Defensive spells will perfectly shield you, and attacking spells wilL greatly damage your opponent!]\n" +
                             "[If you want to do an Attack and guess 50 as a number between 0-100, enter A 50 in the textbox]\n" +
                             "[If you want to defend instead, with a guess of 50, enter D 50 in the textbox.]";

    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    VBox objectsInRoom = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox(); //to hold inventory items
    ImageView roomImageView; //to hold room image
    TextField inputTextField; //for user input

    ScrollPane mainScroll = new ScrollPane();

    boolean gameStart = false;

    int turnCounter = 1;
    int trollHP = 1500;
    int playerHP = 900;
    int trollAttack = 200;
    int trollDefense = 200;

    ArrayList<String> commandList = new ArrayList<>();

    Thread commandThread = new Thread(this);

    public GameTrollState(String name, AdventureGameView view, Player player)
    {
        super();
        this.player = player;
        this.name = name;
        this.view = view;

        initUI();
    }

    @Override
    void initUI() {
        grid.setPadding(new Insets(20));
        grid.setBackground(new Background(new BackgroundFill(
                Paint.valueOf("#000000"),
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

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(saveButton, loadButton, replayButton, settingsButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        //Scroll Pane for main Text
        mainScroll.setStyle("-fx-background: rgb(0,0,0)");
        grid.add(mainScroll, 1, 1, 1, 1);

        //labels for room items
        trollStatusLabel =  new Label("Troll Status");
        trollStatusLabel.setAlignment(Pos.CENTER);
        trollStatusLabel.setStyle("-fx-text-fill: white;");
        trollStatusLabel.setFont(new Font("Arial", textSize + 16));
        trollStatusLabel.setWrapText(true);

        playerStatusLabel =  new Label("Player Status");
        playerStatusLabel.setAlignment(Pos.CENTER);
        playerStatusLabel.setStyle("-fx-text-fill: white;");
        playerStatusLabel.setFont(new Font("Arial", textSize + 16));
        playerStatusLabel.setWrapText(true);

        trollHPLabel =  new Label("Troll HP: " + trollHP);
        trollHPLabel.setAlignment(Pos.CENTER);
        trollHPLabel.setStyle("-fx-text-fill: white;");
        trollHPLabel.setFont(new Font("Arial", textSize + 8));
        trollHPLabel.setWrapText(true);

        trollBaseAttack =  new Label("Troll Base Attack: " + trollAttack);
        trollBaseAttack.setAlignment(Pos.CENTER);
        trollBaseAttack.setStyle("-fx-text-fill: white;");
        trollBaseAttack.setFont(new Font("Arial", textSize + 8));
        trollBaseAttack.setWrapText(true);

        VBox trollStats = new VBox();
        trollStats.getChildren().addAll(trollHPLabel, trollBaseAttack);
        trollStats.setSpacing(30);
        trollStats.setAlignment(Pos.CENTER);

        playerHPLabel =  new Label("Player HP: " + playerHP);
        playerHPLabel.setAlignment(Pos.CENTER);
        playerHPLabel.setStyle("-fx-text-fill: white;");
        playerHPLabel.setFont(new Font("Arial", textSize + 8));
        playerHPLabel.setWrapText(true);

        playerBaseAttack =  new Label("Player Base Attack: " + player.getAttackPower());
        playerBaseAttack.setAlignment(Pos.CENTER);
        playerBaseAttack.setStyle("-fx-text-fill: white;");
        playerBaseAttack.setFont(new Font("Arial", textSize + 8));
        playerBaseAttack.setWrapText(true);

        playerBaseDefense =  new Label("Player Base Defense: " + player.getDefensePower());
        playerBaseDefense.setAlignment(Pos.CENTER);
        playerBaseDefense.setStyle("-fx-text-fill: white;");
        playerBaseDefense.setFont(new Font("Arial", textSize + 8));
        playerBaseDefense.setWrapText(true);

        playerLivesLabel =  new Label("Player Lives: " + player.getLives());
        playerLivesLabel.setAlignment(Pos.CENTER);
        playerLivesLabel.setStyle("-fx-text-fill: white;");
        playerLivesLabel.setFont(new Font("Arial", textSize + 8));
        playerLivesLabel.setWrapText(true);

        VBox playerStats = new VBox();
        playerStats.getChildren().addAll(playerHPLabel, playerBaseAttack, playerBaseDefense, playerLivesLabel);
        playerStats.setSpacing(30);
        playerStats.setAlignment(Pos.CENTER);

        //add all the widgets to the GridPane
        grid.add( trollStatusLabel, 0, 0, 1, 1 );  // Add label
        grid.add(trollStats, 0, 1, 1, 1);
        grid.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        grid.add( playerStatusLabel, 2, 0, 1, 1 );  // Add label
        grid.add(playerStats, 2, 1, 1, 1);

        commandLabel = new Label("What would you like to do?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Arial", textSize));

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

        updateScene(trollSpeak + instructionText + "Are you ready? (Press 'B' to Battle the Troll)\n"); //method displays an image and whatever text is supplied
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

    public void submitEvent(String inputText)
    {
        if(inputText.equals("B") && !gameStart)
        {
            gameStart = true;
            playGame();
        }
        else if(gameStart)
        {
            String[] input = inputText.split(" ");
            boolean firstIsAorD = input[0].equals("A") || input[1].equals("D");

            if(input.length == 2 && firstIsAorD && isNumeric(input[1]))
            {
                commandList.add(input[0]);
                commandList.add(input[1]);
            }
        }
    }

    private boolean isNumeric(String text)
    {
        if(text == null)
            return false;

        try {
            int x = Integer.parseInt(text);
        } catch(NumberFormatException e) {
            return false;
        }

        return true;
    }

    @Override
    public void updateScene(String s)
    {
        //Text update from Settings
        trollStatusLabel.setFont(new Font("Arial", textSize + 16));
        playerStatusLabel.setFont(new Font("Arial", textSize + 16));
        trollHPLabel.setFont(new Font("Arial", textSize + 8));
        playerHPLabel.setFont(new Font("Arial", textSize + 8));
        trollBaseAttack.setFont(new Font("Arial", textSize + 8));
        playerBaseAttack.setFont(new Font("Arial", textSize + 8));
        playerBaseDefense.setFont(new Font("Arial", textSize + 8));
        playerLivesLabel.setFont(new Font("Arial", textSize + 8));
        commandLabel.setFont(new Font("Arial", textSize));
        inputTextField.setFont(new Font("Arial", textSize));
        playerHPLabel.setText("Player HP: " + playerHP);
        trollHPLabel.setText("Troll HP: " + trollHP);

        Text mainText = new Text();
        mainText.setText(s);
        mainText.setFont(new Font("Arial", textSize));
        mainText.setFill(Color.WHITE);
        mainText.setWrappingWidth(700);
        mainScroll.setContent(mainText);


        ColorAdjust bright = new ColorAdjust();
        bright.setBrightness(brightness);
        grid.setEffect(bright);
        this.view.stage.sizeToScene();
    }

    @Override
    public boolean playGame() {
        updateScene(instructionText);
        commandThread.start();


        while(trollHP > 0 && playerHP > 0)
        {
            commandList = new ArrayList<>();
            updateScene(instructionText);

        }

        return false;
    }

    @Override
    public void run()
    {

        while(commandList.size() < 2)
        {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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

    public void addReplayEvent() {
        replayButton.setOnAction(e -> {
            grid.requestFocus();
            // Needs to be implemented
        });
    }

    public void addSettingsEvent() {
        settingsButton.setOnAction(e -> {
            grid.requestFocus();
            this.view.changeState("Settings");
        });
    }
}
