package views.GridState;

import AdventureModel.AdventureGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import views.AdventureGameView;
import views.GridState.GridState;
import javafx.scene.effect.ColorAdjust;


/**
 * SettingsState. Models the Settings screen game.
 */
public class SettingsState extends GridState {

    public Slider brightnessControl; //Slider to change brightness
    public ComboBox backgroundThemeChanger; //Combo box to change background color

    public Button enlargeButton, minimizeButton, highContrastButton, exitButton, muteButton;
    //Button to enlarge text, minimize text, change into high contrast most, and go back to Traversal Screen
    public Label settingsLabel, brightnessLabel, backgroundLabel, textLabel, contrastLabel;
    public boolean highContrastModeOn = false;

    public SettingsState(String name, AdventureGameView view) {
        super();
        this.name = name;
        this.view = view;
        initUI();
    }

    @Override
    void initUI() {
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
        column2.setHgrow(Priority.SOMETIMES); //let some columns grow to take any extra space
        column1.setHgrow(Priority.SOMETIMES);

        // Row constraints
        RowConstraints row1 = new RowConstraints(100);
        RowConstraints row2 = new RowConstraints(100);
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow(Priority.SOMETIMES);
        row3.setVgrow(Priority.SOMETIMES);

        grid.getColumnConstraints().addAll(column1, column2, column1);
        grid.getRowConstraints().addAll(row1, row2, row3);

        //Widgets
        settingsLabel = new Label("Settings");
        settingsLabel.setFont(new Font("Arial", textSize));
        settingsLabel.setAlignment(Pos.CENTER);
        settingsLabel.setStyle("-fx-text-fill: white;");
        settingsLabel.setFont(new Font("Arial", 30));
        settingsLabel.setWrapText(true);

        exitButton = new Button("Exit");
        exitButton.setId("Exit");
        exitButton.setFont(new Font("Arial", textSize));
        customizeButton(exitButton, 100, 50);
        AdventureGameView.makeButtonAccessible(exitButton, "Exit Button", "This button exits out of the Inventory.", "This button takes you from inetory back to main screen.");
        addExitEvent();

        muteButton = new Button("Mute");
        muteButton.setId("Mute");
        muteButton.setFont(new Font("Arial", textSize));
        customizeButton(muteButton, 100, 50);
        AdventureGameView.makeButtonAccessible(muteButton, "Background Music Mute Button", "This button mutes background music.", "This button mutes background music.");
        muteButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        addMuteEvent();

        HBox buttons = new HBox();
        buttons.getChildren().addAll(exitButton, muteButton);
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);

        brightnessLabel = new Label("Brightness");
        brightnessLabel.setFont(new Font("Arial", textSize));
        brightnessLabel.setAlignment(Pos.CENTER);
        brightnessLabel.setStyle("-fx-text-fill: white;");
        brightnessLabel.setFont(new Font("Arial", 20));
        brightnessLabel.setWrapText(true);

        brightnessControl = new Slider(-1, 1, 0);
        brightnessControl.setMax(0.5);
        brightnessControl.setMin(-0.5);
        addSliderEvent();

        VBox brightness = new VBox();
        brightness.getChildren().addAll(brightnessLabel, brightnessControl);
        brightness.setSpacing(10);
        brightness.setAlignment(Pos.CENTER);

        backgroundLabel = new Label("Background Theme");
        backgroundLabel.setFont(new Font("Arial", textSize));
        backgroundLabel.setAlignment(Pos.CENTER);
        backgroundLabel.setStyle("-fx-text-fill: white;");
        backgroundLabel.setFont(new Font("Arial", 20));
        backgroundLabel.setWrapText(true);

        backgroundThemeChanger = new ComboBox();
        addBackgroundThemeEvent();

        VBox backgroundTheme = new VBox();
        backgroundTheme.getChildren().addAll(backgroundLabel, backgroundThemeChanger);
        backgroundTheme.setSpacing(10);
        backgroundTheme.setAlignment(Pos.CENTER);

        textLabel = new Label("Text Size");
        textLabel.setFont(new Font("Arial", textSize));
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setStyle("-fx-text-fill: white;");
        textLabel.setFont(new Font("Arial", 20));
        textLabel.setWrapText(true);

        enlargeButton = new Button("Enlarge");
        enlargeButton.setId("Enlarge");
        enlargeButton.setFont(new Font("Arial", textSize));
        customizeButton(enlargeButton, 100, 50);
        AdventureGameView.makeButtonAccessible(enlargeButton, "Text Enlarge Button", "This button enlarges text.", "This button increases size of text.");
        addEnlargeEvent();

        minimizeButton = new Button("Minimize");
        minimizeButton.setId("Minimize");
        minimizeButton.setFont(new Font("Arial", textSize));
        customizeButton(minimizeButton, 100, 50);
        AdventureGameView.makeButtonAccessible(minimizeButton, "Text Minimize Button", "This button minimizes text.", "This button decreases size of text.");
        addMinimizeEvent();

        HBox textControls = new HBox();
        textControls.getChildren().addAll(enlargeButton, minimizeButton);
        textControls.setSpacing(10);
        textControls.setAlignment(Pos.CENTER);

        VBox textChange = new VBox();
        textChange.getChildren().addAll(textLabel, textControls);
        textChange.setSpacing(10);
        textChange.setAlignment(Pos.CENTER);

        contrastLabel = new Label("Set High Constrast Mode");
        contrastLabel.setFont(new Font("Arial", textSize));
        contrastLabel.setAlignment(Pos.CENTER);
        contrastLabel.setStyle("-fx-text-fill: white;");
        contrastLabel.setFont(new Font("Arial", 20));
        contrastLabel.setWrapText(true);

        highContrastButton = new Button("Change Contrast");
        highContrastButton.setId("Contrast");
        highContrastButton.setFont(new Font("Arial", textSize));
        highContrastButton.setStyle("-fx-background-color: white; -fx-text-fill: white");
        customizeButton(highContrastButton, 200, 50);
        AdventureGameView.makeButtonAccessible(highContrastButton, "High Contrast Change Toggle", "This button changes high contrast mode.", "This button changes high contrast mode.");
        addContrastEvent();

        VBox highContrast = new VBox();
        highContrast.getChildren().addAll(contrastLabel, highContrastButton);
        highContrast.setSpacing(10);
        highContrast.setAlignment(Pos.CENTER);

        //add all the widgets to the GridPane
        grid.add(settingsLabel, 0, 0, 1, 1);  // Add label
        grid.add(buttons, 1, 0, 1, 1);  // Add buttons

        grid.add(textChange, 0, 1, 1, 1);
        grid.add(brightness, 1, 1, 1, 1);
        grid.add(backgroundTheme, 0, 2, 1, 1);
        grid.add(highContrast, 1, 2, 1, 1);

        updateScene("");

        highContrastButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
    }

    @Override
    public void updateScene(String s) {
        settingsLabel.setFont(new Font("Arial", textSize));
        brightnessLabel.setFont(new Font("Arial", textSize));
        backgroundLabel.setFont(new Font("Arial", textSize));
        contrastLabel.setFont(new Font("Arial", textSize));
        textLabel.setFont(new Font("Arial", textSize));
        ColorAdjust bright = new ColorAdjust();
        bright.setBrightness(brightness);
        bright.setContrast(Contrast);
        grid.setEffect(bright);
        if (!mute)
            this.view.backgroundMusic.adjustVolume(0.8);
        else
            this.view.backgroundMusic.adjustVolume(0.0);

        switch (Backgcolor) {
            case "Black":
                grid.setBackground(new Background(new BackgroundFill(
                        Color.BLACK,
                        new CornerRadii(0),
                        new Insets(0)
                )));
                break;
            case "Grey":
                grid.setBackground(new Background(new BackgroundFill(
                        Color.GREY,
                        new CornerRadii(0),
                        new Insets(0)
                )));
                break;
            case "Pink":
                grid.setBackground(new Background(new BackgroundFill(
                        Color.PINK,
                        new CornerRadii(0),
                        new Insets(0)
                )));
                break;
            case "Orange":
                grid.setBackground(new Background(new BackgroundFill(
                        Color.ORANGE,
                        new CornerRadii(0),
                        new Insets(0)
                )));
                break;
            default:
                // Handle unknown color
                break;
        }
        // To be added to
    }

    /**
     * Adds a mouse event to exitButton
     */
    public void addExitEvent() {
        exitButton.setOnAction(e -> {
            grid.requestFocus();

            if(this.view.inTrollGame)
                this.view.changeState("Troll");
            else
                this.view.changeState("Traversal");
        });
    }

    /**
     * Adds a slider event to brightnessControl.
     * Min value is -0.5 and max value is 0.5
     */
    public void addSliderEvent()
    {
        brightnessControl.setOnMouseDragged(e -> {
            brightness = brightnessControl.getValue();
            updateScene("");
        });
        brightnessControl.setOnMouseClicked(e -> {
            brightness = brightnessControl.getValue();
            updateScene("");
        });
    }

    /**
     * Adds a mouse event to the backgroundThemeChanger
     */
    public void addBackgroundThemeEvent() {
        backgroundThemeChanger.getItems().addAll("Black", "Grey", "Pink", "Orange");
        backgroundThemeChanger.setValue("Black"); // Set default background color

        backgroundThemeChanger.setOnAction(e -> {
            Backgcolor = (String) backgroundThemeChanger.getValue();
            updateScene("");
        });
    }

    /**
     * Adds a mouse event to the enlargeButton
     */
    public void addEnlargeEvent() {
        enlargeButton.setOnAction(e -> {
            grid.requestFocus();
            textSize += 5;
            updateScene("");
        });
    }
    /**
     * Adds a mouse event to the minimizeButton
     */
    public void addMinimizeEvent() {
        minimizeButton.setOnAction(e -> {
            grid.requestFocus();
            textSize -= 5;
            updateScene("");
        });
    }

    /**
     * Adds a mouse event to the highContrastButton
     */
    public void addContrastEvent() {
        highContrastButton.setOnAction(e -> {
            grid.requestFocus();
            toggleHighContrastMode();
        });

    }

    private void toggleHighContrastMode() {
        if (HighContrastMode) {
            // If high contrast mode is on, turn it off
            HighContrastMode = false;
            Contrast = 0.0;
            updateScene("");
            highContrastButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        } else {
            // If high contrast mode is off, turn it on
            HighContrastMode = true;
            Contrast = 1.0;
            updateScene("");
            highContrastButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");

        }

    }

    public void addMuteEvent() {
        muteButton.setOnAction(e -> {
            grid.requestFocus();
            if(this.mute) {
                this.mute = !this.mute;
                muteButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            }
            else {
                this.mute = !this.mute;
                muteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            }

            updateScene("");

        });
    }

}
