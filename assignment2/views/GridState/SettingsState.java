package views.GridState;

import views.AdventureGameView;
import views.GridState.GridState;

public class SettingsState extends GridState
{

    public SettingslState(String name, AdventureGameView view)
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
