package views.GridState;

import AdventureModel.Players.Player;
import views.AdventureGameView;

public abstract class TrollState extends GridState {
    public Player player;

    /**
     * giveInstructions
     * _________________________
     * All Trolls should explain how their game is played
     */
    public abstract void giveInstructions();

    /**
     * playGame
     * _________________________
     * Play the Trolls game
     *
     * @return true if player wins the game, else false
     */
    public abstract boolean playGame();
}
