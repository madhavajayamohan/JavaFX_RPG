package views;

import AdventureModel.AdventureGame;
import AdventureModel.AdventureObject;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent; //you will need these!
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.EventHandler; //you will need this too!
import javafx.scene.AccessibleRole;
import org.junit.platform.commons.util.StringUtils;
import views.GridState.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 * You are asked to demo your visualization via a Zoom
 * recording. Place a link to your recording below.
 *
 * ZOOM LINK: <https://youtu.be/XLVlwlUviNM>
 * PASSWORD: <PASSWORD HERE>
 */
public class AdventureGameView {

    public AdventureGame model; //model of the game
    public Stage stage; //stage on which all is rendered

    public GridState[] allStates = new GridState[3];
    public Scene[] allScenes = new Scene[3];
    public GridState currState;
    public GridPane currGrid;

    private MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing

    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        intiUI();
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {

        // setting up the stage
        this.stage.setTitle("tiowille's Adventure Game"); //Replace <YOUR UTORID> with your UtorID

        allStates[0] = new TraversalState("Traversal", this);
        currState = allStates[0];
        currGrid = currState.grid;
        allScenes[0] = new Scene(currGrid, 1000, 800);
        allScenes[0].setFill(Color.BLACK);

        allStates[1] = new InventoryState("Inventory", this);
        allScenes[1] = new Scene(allStates[1].grid, 1000, 800);
        allScenes[1].setFill(Color.BLACK);

        allStates[2] = new SettingsState("Settings", this);
        allScenes[2] = new Scene(allStates[2].grid, 1000, 800);
        allScenes[2].setFill(Color.BLACK);

        this.stage.setScene(allScenes[0]);
        this.stage.setResizable(false);
        this.stage.show();

    }

    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * This method articulates Room Descriptions
     */
    public void articulateRoomDescription() {
        String musicFile;
        String adventureName = this.model.getDirectoryName();
        String roomName = this.model.getPlayer().getCurrentRoom().getRoomName();

        if (!this.model.getPlayer().getCurrentRoom().getVisited()) musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-long.mp3" ;
        else musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-short.mp3" ;
        musicFile = musicFile.replace(" ","-");

        Media sound = new Media(new File(musicFile).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlaying = true;

    }

    /**
     * This method stops articulations
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        if (mediaPlaying) {
            mediaPlayer.stop(); //shush!
            mediaPlaying = false;
        }
    }

    public void updateScene(String textToDisplay)
    {
        currState.updateScene(textToDisplay);
    }

    public void updateItems()
    {
        if(currState instanceof GridStateWithItems)
            ((GridStateWithItems) currState).updateItems();
    }

    public void changeState(String s)
    {
        stopArticulation();
        int index = 0;

        if(s.equals("Inventory"))
            index = 1;
        else if(s.equals("Settings"))
            index = 2;

        currState = allStates[index];
        currGrid = currState.grid;
        updateScene("");
        updateItems();
        this.stage.setScene(allScenes[index]);
        this.stage.setResizable(false);
        this.stage.show();
    }

}
