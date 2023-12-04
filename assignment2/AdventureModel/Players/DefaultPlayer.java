package AdventureModel.Players;


import AdventureModel.AdventureObject;
import AdventureModel.Players.Player;
import AdventureModel.Room;

import java.util.ArrayList;

/**
 * Class for concrete player implmentation
 */
public class DefaultPlayer extends Player {

    /**
     * Constructor for DefaultPlayer
     *
     * @param currentRoom room that the player is currently in
     */
    public DefaultPlayer(Room currentRoom) {
        this.inventory = new ArrayList<AdventureObject>();
        this.powerInv = new ArrayList<AdventureObject>();
        this.currentRoom = currentRoom;
        this.atkPower = 100;
        this.defPower = 100;
        this.lives = 3;
        this.immunity = false;
    }

    public DefaultPlayer(Room currentRoom, ArrayList inventory, ArrayList powerInv, int lives, boolean immunity) {
        this.inventory = inventory;
        this.powerInv = powerInv;
        this.currentRoom = currentRoom;
        this.atkPower = 100;
        this.defPower = 100;
        this.lives = lives;
        this.immunity = immunity;
    }
}
