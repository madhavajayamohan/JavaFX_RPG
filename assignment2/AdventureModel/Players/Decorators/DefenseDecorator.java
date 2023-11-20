package AdventureModel.Players.Decorators;

import AdventureModel.Players.Player;

/**
 * Decorator class for default defense power
 */
public class DefenseDecorator extends PlayerDecorator {

    /**
     * Constructor for DefenseDecorator
     *
     * @param defaultPlayer reference to concrete player object
     */
    public DefenseDecorator (Player defaultPlayer) {
        super(defaultPlayer);
    }

    /**
     * This method returns the correct calculation for defPower
     *
     * @return interger value for defPower
     */
    @Override
    public int getDefensePower() {
        return defaultPlayer.getDefensePower();
    }
}
