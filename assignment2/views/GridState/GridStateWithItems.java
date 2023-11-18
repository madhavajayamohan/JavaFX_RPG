package views.GridState;

public abstract class GridStateWithItems extends GridState{

    /**
     * Class GridStateWithItems. Abstract class that is a subclass of GridState.
     * No difference, except it has the abstract method updateItems()
     */
    public GridStateWithItems()
    {
        super();
    }

    abstract public void updateItems();


}
