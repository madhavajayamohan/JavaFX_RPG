package AdventureModel.Players.Decorators;

import AdventureModel.Players.Player;

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
}
