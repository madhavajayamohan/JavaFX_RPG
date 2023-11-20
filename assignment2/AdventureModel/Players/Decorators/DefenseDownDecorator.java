package AdventureModel.Players.Decorators;

import AdventureModel.Players.Player;

/**
 * Decorator class for decreasing defense power
 */
public class DefenseDownDecorator extends PlayerDecorator {

    /**
     * Constructor for DefenseDownDecorator
     *
     * @param defaultPlayer reference to concrete player object
     */
    public DefenseDownDecorator (Player defaultPlayer) {
        super(defaultPlayer);
    }

    /**
     * This method returns the correct calculation for defPower
     *
     * @return interger value for defPower halved
     */
    @Override
    public int getDefensePower() {
        double val = defaultPlayer.getDefensePower() / 2;
        return (int)Math.floor(val);
    }
}
