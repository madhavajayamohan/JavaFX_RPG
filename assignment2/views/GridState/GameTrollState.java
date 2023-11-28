package views.GridState;

import AdventureModel.Players.Player;
import views.AdventureGameView;

public class GameTrollState extends TrollState {

    public GameTrollState(String name, AdventureGameView view, Player player)
    {
        super();
        this.name = name;
        this.view = view;

        initUI();
    }

    @Override
    void initUI() {

    }

    @Override
    public void updateScene(String s) {

    }

    @Override
    public void giveInstructions() {

    }

    @Override
    public boolean playGame() {
        return false;
    }
}
