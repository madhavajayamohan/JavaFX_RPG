package AdventureModel.Players.Decorators;

import AdventureModel.Players.Player;

/**
 * Decorator class for increasing defense power
 */
public class DefenseUpDecorator extends PlayerDecorator {

    /**
     * Constructor for DefenseUpDecorator
     *
     * @param defaultPlayer reference to concrete player object
     */
    public DefenseUpDecorator (Player defaultPlayer) {
        super(defaultPlayer);
    }

    /**
     * This method returns the correct calculation for defPower
     *
     * @return interger value for defPower multiplied by 2
     */
    @Override
    public int getDefensePower() {
        return (defaultPlayer.getDefensePower() * 2);
    }
}