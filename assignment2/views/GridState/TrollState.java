package views.GridState;

import AdventureModel.Players.Player;
import views.AdventureGameView;

public abstract class TrollState extends GridState {
    public Player player;

    /**
     * playGame
     * _________________________
     * Play the Trolls game
     *
     * @return true if player wins the game, else false
     */
    public abstract boolean playGame();


    /**
     * This method changes the player attribute if it changes due to
     * the PlayerDecorator
     */
    public void changePlayer(Player newPlayer) {this.player = newPlayer;}

    /**
     * This method returns the player attribute of the game
     */
    public Player getPlayer() {return this.player;}
}
