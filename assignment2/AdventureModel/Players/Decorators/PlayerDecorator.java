package AdventureModel.Players.Decorators;

import AdventureModel.AdventureObject;
import AdventureModel.Players.Player;
import AdventureModel.Room;

import java.util.ArrayList;

/**
 * Base class for decorators
 */
public class PlayerDecorator extends Player {

    /**
     * Reference to concrete player object
     */
    protected Player defaultPlayer;

    /**
     * Constructor for PlayerDecorator
     *
     * @param defaultPlayer the player object being decorated
     */
    public PlayerDecorator (Player defaultPlayer) {
        this.defaultPlayer = defaultPlayer;
    }

    public boolean takeObject(String object) {
        return defaultPlayer.takeObject(object);
    }

    public boolean takePowerUp(String object) {
        return defaultPlayer.takePowerUp(object);
    }

    public boolean checkIfObjectInInventory(String object) {
        return defaultPlayer.checkIfObjectInInventory(object);
    }

    public boolean checkIfObjectInPowerInventory(String object) {
        return defaultPlayer.checkIfObjectInPowerInventory(object);
    }

    public void dropObject(String s) {
        defaultPlayer.dropObject(s);
    }

    public void dropPowerUp(String s) {
        defaultPlayer.dropPowerUp(s);
    }

    public void setCurrentRoom(Room currentRoom) {
        defaultPlayer.setCurrentRoom(currentRoom);
    }

    public void addToInventory(AdventureObject object) {
        defaultPlayer.addToInventory(object);
    }

    public void addToPowerInventory(AdventureObject object) {
        defaultPlayer.addToPowerInventory(object);
    }

    public Room getCurrentRoom() {
        return defaultPlayer.getCurrentRoom();
    }
    /**
     * This method returns the correct calculation for atkPower
     *
     * @return integer value for atkPower
     */
    public int getAttackPower() {
        return defaultPlayer.getAttackPower();
    }

    /**
     * This method returns the correct calculation for defPower
     *
     * @return integer value for defPower
     */
    public int getDefensePower() { return defaultPlayer.getDefensePower(); }

    public Player getDefaultPlayer() {
        return defaultPlayer;
    }

    public ArrayList<String> getInventory() {
        return defaultPlayer.getInventory();
    }

    public ArrayList<String> getPowerInventory() {
        return defaultPlayer.getPowerInventory();
    }

    public int getLives() {
        return defaultPlayer.getLives();
    }

    public void increaseLives() {
        defaultPlayer.increaseLives();
    }

    public void decreaseLives() {
        defaultPlayer.decreaseLives();
    }

    public boolean getImmunity() {
        return defaultPlayer.getImmunity();
    }

    public void setImmunity(boolean status) {
        defaultPlayer.setImmunity(status);
    }
}
