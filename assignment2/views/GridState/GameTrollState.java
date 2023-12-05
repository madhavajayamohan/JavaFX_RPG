package views.GridState;

import AdventureModel.Passage;
import AdventureModel.PassageTable;
import AdventureModel.Players.Player;
import AdventureModel.Room;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import views.AdventureGameView;
import views.LoadView;
import views.SaveView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static javafx.scene.paint.Paint.valueOf;

public class GameTrollState extends TrollState {

    Button settingsButton; //buttons
    Boolean helpToggle = false; //is help on display?

    Label trollStatusLabel, playerStatusLabel, trollHPLabel, playerHPLabel, trollBaseAttack, playerBaseAttack, playerBaseDefense, playerLivesLabel, commandLabel;
    String trollSpeak = "You, puny human, dare to come on this path?\n" +
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
                             "[If you want to defend instead, with a guess of 50, enter D 50 in the textbox.]\n";

    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    VBox objectsInRoom = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox(); //to hold inventory items
    ImageView roomImageView; //to hold room image
    TextField inputTextField; //for user input

    Label mainText = new Label();

    boolean gameStart = false;

    int turnCounter = 1;
    int trollHP = 750 + (int) (250 * Math.random()) + 1;

    final int playerStartHP = 900;
    int playerHP = playerStartHP;
    int trollAttack = 50 + (int) (100 * Math.random()) + 1;

    String[] commandList = new String[2];

    VBox textEntry = new VBox();

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

        grid.getColumnConstraints().addAll( column1 , column2 , column3 );
        grid.getRowConstraints().addAll( row1 , row2 , row3 );

        // Top Buttons

        settingsButton = new Button("Settings");
        settingsButton.setId("Settings");
        settingsButton.setFont(new Font("Arial", textSize));
        customizeButton(settingsButton, 200, 50);
        AdventureGameView.makeButtonAccessible(settingsButton, "Settings Button", "This button takes you to settings.", "This button takes you to settings. Click it to change features like brightness, text size, and color contrast.");
        addSettingsEvent();

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(settingsButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

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
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        grid.add( textEntry, 0, 2, 3, 1 );

        updateScene(""); //method displays an image and whatever text is supplied
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
     * Process the command represented by inputText
     *
     * @param inputText the command that needs to be processed
     */
    public void submitEvent(String inputText)
    {
        if(inputText.equalsIgnoreCase("B") && !gameStart)
        {
            gameStart = true;
            updateScene(instructionText + "\nTurn " + turnCounter + "\n" + "Please enter your move: \n");
        }
        else if(gameStart)
        {
            String[] input = inputText.split(" ");

            if(input.length == 2)
            {
                boolean firstIsAorD = input[0].equalsIgnoreCase("A") || input[0].equalsIgnoreCase("D");

                if(firstIsAorD && isNumeric(input[1]))
                {
                    commandList[0] = input[0];
                    commandList[1] = input[1];

                    runTurn();
                    playGame();
                }
            }
        }
    }

    /**
     * isNumeric
     * __________________________
     * Returns wether or not text represents an integer
     *
     * @param text the command that needs to be processed
     */
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

        playerBaseAttack.setText("Player Base Attack: " + player.getAttackPower());
        playerBaseDefense.setText("Player Base Defense: " + player.getDefensePower());
        playerLivesLabel.setText("Player Lives: " + player.getLives());

        mainText.setText(s);
        mainText.setStyle("-fx-text-fill: white;");
        mainText.setFont(new Font("Arial", textSize));
        mainText.setAlignment(Pos.TOP_LEFT);
        mainText.setPrefWidth(700);
        mainText.setPrefHeight(700);
        mainText.setTextOverrun(OverrunStyle.CLIP);
        mainText.setWrapText(true);

        ScrollPane toScroll = new ScrollPane(mainText);
        toScroll.setStyle("-fx-background: rgb(0,0,0)");
        grid.add(toScroll, 1, 1, 1,1);
        switch (Backgcolor) {
            case "Black":
                grid.setBackground(new Background(new BackgroundFill(
                        Color.BLACK,
                        new CornerRadii(0),
                        new Insets(0)
                )));
                commandLabel.setTextFill(Color.BLACK);
                textEntry.setStyle("-fx-background-color: black;");
                mainText.setStyle("-fx-background: black; -fx-background-color:black;");
                toScroll.setStyle("-fx-background: black");

                break;
            case "Grey":
                grid.setBackground(new Background(new BackgroundFill(
                        Color.GREY,
                        new CornerRadii(0),
                        new Insets(0)
                )));
                commandLabel.setTextFill(Color.GREY);
                textEntry.setStyle("-fx-background-color: grey;");
                mainText.setStyle("-fx-background: grey; -fx-background-color:grey;");
                toScroll.setStyle("-fx-background: grey");

                break;
            case "Pink":
                grid.setBackground(new Background(new BackgroundFill(
                        Color.PINK,
                        new CornerRadii(0),
                        new Insets(0)
                )));
                commandLabel.setTextFill(Color.PINK);
                textEntry.setStyle("-fx-background-color: pink;");
                mainText.setStyle("-fx-background: pink; -fx-background-color:pink;");
                toScroll.setStyle("-fx-background: pink");

                break;
            case "Orange":
                grid.setBackground(new Background(new BackgroundFill(
                        Color.ORANGE,
                        new CornerRadii(0),
                        new Insets(0)
                )));
                commandLabel.setTextFill(Color.ORANGE);
                textEntry.setStyle("-fx-background-color: orange;");
                mainText.setStyle("-fx-background: orange; -fx-background-color:orange;");
                toScroll.setStyle("-fx-background: orange");
                break;
            default:
                // Handle unknown color
                break;
        }




        ColorAdjust bright = new ColorAdjust();
        bright.setBrightness(brightness);
        bright.setContrast(Contrast);
        grid.setEffect(bright);

        this.view.stage.sizeToScene();
    }

    /**
     * playGame
     * __________________________
     * If the player wins the game, then prints winning message,
     * and moves player to 'trophy room' to get new item.
     *
     * If the player loses the game, decreases the players life by one.
     * If the player has zero lives now, ends game. If player has lives left,
     * redirects player back to room from which he entered.
     */
    @Override
    public boolean playGame() {
        if(trollHP <= 0) {
            this.trollHP = 0;

            if(this.playerHP <= 0) {
                this.playerHP = 1;
            }

            updateScene("YOU HAVE WON!!!!");
            mainText.setFont(new Font("Arial", 70));

            PauseTransition pause = new PauseTransition(Duration.seconds(3));

            pause.setOnFinished(event -> {
                this.view.model.movePlayer("FORWARD");
                this.view.changeState("Traversal");
            });
            pause.play();
        }
        else if(playerHP <= 0) {
            this.playerHP = 0;

            if(this.trollHP <= 0) {
                this.trollHP = 1;
            }

            this.player.decreaseLives();
            updateScene("YOU HAVE LOST!!!!");
            mainText.setFont(new Font("Arial", 70));

            PauseTransition pause = new PauseTransition(Duration.seconds(3));

            if(this.player.getLives() == 0) {
                updateScene("GAME OVER!!!");
                mainText.setFont(new Font("Arial", 70));

                pause.setOnFinished(event -> {
                    Platform.exit();
                });
                pause.play();
            }
            else {
                PassageTable passages = this.player.getCurrentRoom().getMotionTable();

                pause.setOnFinished(event -> {
                    this.view.model.movePlayer("BACK");
                    this.view.changeState("Traversal");
                });
                pause.play();
            }
        }

        if(trollHP <= 0 || playerHP <= 0)
        {
            gameStart = false;
            turnCounter = 1;
            trollHP = 750 + (int) (250 * Math.random()) + 1;
            playerHP = playerStartHP;
            trollAttack = 50 + (int) (100 * Math.random()) + 1;
            this.view.inTrollGame = false;
        }

        return true;
    }

    /**
     * runTurn()
     * __________________________
     * Represents a turn of the game.
     */
    public void runTurn() {
        String newText = instructionText + "\nTurn " + turnCounter + "\n";

        String attackChoice = commandList[0];
        int guess = Integer.parseInt(commandList[1]);

        int tAttk = (trollAttack - 50) + (int) (100 * Math.random()) + 1; //Troll Attack
        int randNumber = (int) (100 * Math.random()) + 1;
        int difference = Math.abs(randNumber - guess);

        if(guess < 0 && guess > 100)
            newText += "As you guessed an out of range number, your spell failed!\n";
        else if(attackChoice.equals("A"))
        {
            int pAttk = 0;
            if(difference == 0) {
                pAttk = (int) (this.player.getAttackPower() * 3);
                newText += "Incredible! Your guess matched the random number!\n";
                newText += "Your spell gains incredible power!\n";
                newText += "Your spell does " + pAttk + " damage!\n";
            }
            else {
                pAttk = (int) (this.player.getAttackPower() * 2 * ((100 - difference) / 100.0));
                newText += "The random number was " + randNumber + "\n";
                newText += "Your spell does " + pAttk + " damage!\n\n";
            }

            trollHP -= pAttk;
            playerHP -= tAttk;
            newText += "The troll's attack does + " + tAttk + " damage!\n\n";
        }
        else if(attackChoice.equals("D")) {
            int newTAttk = 0;
            if(difference == 0) {
                newText += "Incredible! Your guess matched the random number!\n";
                newText += "Your spell gains incredible power!\n";
                newText += "You have nullified the troll's attack\n";
            }
            else {
                double damageNullified = (double) (this.player.getDefensePower()) / (difference + this.player.getDefensePower());
                newTAttk = (int) (tAttk * damageNullified);
                newText += "The random number was " + randNumber + "\n";
                newText += "Your spell reduced troll attack  damage from " + tAttk + " to " + newTAttk + " points!\n\n";
            }

            playerHP -= newTAttk;
            newText += "The troll's attack does " + newTAttk + " damage!\n\n";
        }

        newText += "Enter your next move: ";
        updateScene(newText);
        turnCounter += 1;
    }

    /**
     * This method handles the event related to the
     * settings button.
     */
    public void addSettingsEvent() {
        settingsButton.setOnAction(e -> {
            grid.requestFocus();
            this.view.changeState("Settings");
        });
    }
}
