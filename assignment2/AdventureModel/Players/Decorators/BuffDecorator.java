package AdventureModel.Players.Decorators;

import AdventureModel.Players.Player;

/**
 * Decorator class for buffing attack power
 */
public class BuffDecorator extends PlayerDecorator {

    /**
     * Constructor for BuffDecorator
     *
     * @param defaultPlayer reference to concrete player object
     */
    public BuffDecorator (Player defaultPlayer) {
        super(defaultPlayer);
    }

    /**
     * This method returns the correct calculation for atkPower
     *
     * @return interger value for atkPower multiplied by 2
     */
    @Override
    public int getAttackPower() {
        return (defaultPlayer.getAttackPower() * 2);
    }
}
