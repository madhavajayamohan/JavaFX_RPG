package AdventureModel.Players.Decorators;

import AdventureModel.Players.Player;

/**
 * Decorator class for default attack power
 */
public class NormalDecorator extends PlayerDecorator {

    /**
     * Constructor for NormalDecorator
     *
     * @param defaultPlayer reference to concrete player object
     */
    public NormalDecorator (Player defaultPlayer) {
        super(defaultPlayer);
    }

    /**
     * This method returns the correct calculation for atkPower
     *
     * @return interger value for atkPower
     */
    @Override
    public int getAttackPower() {
        return defaultPlayer.getAttackPower();
    }
}
