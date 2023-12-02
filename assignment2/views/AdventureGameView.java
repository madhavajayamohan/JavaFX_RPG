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
import marytts.exceptions.SynthesisException;
import org.junit.platform.commons.util.StringUtils;
import views.GridState.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.signalproc.effects.*;
import marytts.util.data.audio.AudioPlayer;
import javax.sound.sampled.AudioInputStream;


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

    public GridState[] allStates = new GridState[4]; //Contains all possible gridStates
    public Scene[] allScenes = new Scene[4]; //Contains all possible scenes
    public GridState currState; //This is the current GridState of the game
    public GridPane currGrid; //This is the currently displayed GridPane

    public boolean inTrollGame = false;

    public MaryInterface marytts;

    private MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing

    String trollSpeak = "You, puny human, dare to come on this path?\n" +
            "These chambers are only meant for the strong, and no human is strong.\n" +
            "Oho? I see that you can use some magic. Very well, then.\n" +
            "Let us see how your magic matches up to my strength.\n\n" +
            "LET US DO BATTLE!!!!\n\n" +
            "===================\n" +
            "QUEST: DEFEAT TROLL\n" +
            "===================\n";

    String instructionText = "[You have two choices: select A, for Attack, or D, for Defense.]\n" +
            "[Then, to activate your magic, guess an integer from 0 to 100]\n" +
            "[The system will generate a random integer\n" +
            "the closer to that number, the more effective your attack or defense]\n" +
            "[If you happen to select the random integer your spell will gain immense power!!!]\n" +
            "[Defensive spells will perfectly shield you, and attacking spells will greatly damage your opponent!]\n" +
            "[If you want to do an Attack and guess 50 as a number between 0-100, enter A 50 in the textbox]\n" +
            "[If you want to defend instead, with a guess of 50, enter D 50 in the textbox.]\n";

    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;

        try {
            marytts = new LocalMaryInterface();
        } catch (MaryConfigurationException e) {
            System.out.println("MaryTTS error.");
            //throw new RuntimeException();
        }

//        for(String voice : this.marytts.getAvailableVoices()) {
//            System.out.println(voice);
//        }
//
//        this.marytts.setVoice("dfki-spike-hsmm");


        intiUI();
    }

    /**
     * say
     * @param text
     *
     * Reads out text using MaryTTS library
     */
    public void say(String text)
    {
        try {
            AudioPlayer ap = new AudioPlayer();
            AudioInputStream audio = this.marytts.generateAudio(text);
            ap.setAudio(audio);
            ap.start();
            ap.join();
        } catch (SynthesisException | InterruptedException e) {
            System.out.println("MaryTTS error.");
        }
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

        allStates[3] = new GameTrollState("Troll", this, this.model.getPlayer());
        allScenes[3] = new Scene(allStates[3].grid, 1000, 800);
        allScenes[3].setFill(Color.BLACK);

        this.stage.setScene(allScenes[0]);
        this.stage.setResizable(false);
        this.stage.show();

        autoSave();
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
//        String roomDescription = this.model.getPlayer().getCurrentRoom().getRoomDescription();
//        String objectDescription = this.model.getPlayer().getCurrentRoom().getObjectString();
//
////        say(marytts, roomDescription);
//        say(marytts, "Objects in this room");
//        say(marytts, objectDescription);

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

    /**
     * Updates the GridPane after any changes
     */
    public void updateScene(String textToDisplay)
    {
        currState.updateScene(textToDisplay);
    }

    /**
     * Updates the items of the GridPane if it is the
     * TraversalState or the InventoryState
     */
    public void updateItems()
    {
        if(currState instanceof GridStateWithItems)
            ((GridStateWithItems) currState).updateItems();
    }

    /**
     * @param s: Name of the changeState
     * Changes the value of currState and currGrid, and changes
     * the current Scene to the scene based on currGrid.
     * The state to change to is determined by the paramter s
     */
    public void changeState(String s)
    {
        stopArticulation();
        int index = 0;

        if(s.equals("Inventory"))
            index = 1;
        else if(s.equals("Settings"))
            index = 2;
        else if(s.equals("Troll"))
            index = 3;

        currState = allStates[index];
        currGrid = currState.grid;

        if(index == 3)
            updateScene(trollSpeak + instructionText + "\nAre you ready to play? (Enter B to start playing)");
        updateItems();
        this.stage.setScene(allScenes[index]);
        this.stage.setResizable(false);
        this.stage.show();
    }

    /**
     * This method handles the event related to the
     * player pressing the exit program button on the top right corner.
     */
    public void autoSave() {
        stage.setOnCloseRequest(e -> {
            String gameName = "Autosave " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".ser";
            String separator = File.separator;
            File save = new File("Games" + separator + "AutoSaves" + separator + gameName);
            this.model.saveModel(save);


            File[] autoSaves = new File("Games" + separator + "AutoSaves").listFiles();
            if (autoSaves != null) {
                Arrays.sort(autoSaves);
                if (autoSaves.length > 10) {
                    File temp = new File("Games" + separator + "AutoSaves" + separator + autoSaves[0].getName());
                    System.out.println(temp.delete());
                }
            }
        });
    }

}
