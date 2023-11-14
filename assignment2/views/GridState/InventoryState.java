package views.GridState;

import views.AdventureGameView;
import views.GridState.GridState;

public class InventoryState extends GridState
{

    public InventoryState(String name, AdventureGameView view)
    {
        super();
        this.name = name;
        this.view = view;

        initUI();
    }

    @Override
    void initUI() {

    }
}
