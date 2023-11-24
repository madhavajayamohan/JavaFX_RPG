package AdventureModel.Players.Decorators;

import AdventureModel.Players.Player;

/**
 * Decorator class for debuffing attack power
 */
public class DebuffDecorator extends PlayerDecorator {

    /**
     * Constructor for DebuffDecorator
     *
     * @param defaultPlayer reference to concrete player object
     */
    public DebuffDecorator (Player defaultPlayer) {
        super(defaultPlayer);
    }

    /**
     * This method returns the correct calculation for atkPower
     *
     * @return interger value for atkPower halved
     */
    @Override
    public int getAttackPower() {
        double val = defaultPlayer.getAttackPower() / 2;
        return (int)Math.floor(val);
    }
}
