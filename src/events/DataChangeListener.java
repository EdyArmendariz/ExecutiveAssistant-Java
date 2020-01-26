package events;

/**
 * This interface allows an implementing object to be accessible to other objects
 * that don't have access to an instance.<br>
 * Used by the applications screens to update themselves with new data
 *
 */
public interface DataChangeListener
        extends SimpleEventListener
{
    /** No change.  */
    public static final int NONE = 0;
    /** Save */
    public static final int SAVE = 1;
    /**  Update */
    public static final int UPDATE = 2;
    /** Delete  */
    public static final int DELETE = 3;
    /** Title  */
    public static final int TITLE = 4;

    /**
     * The Interface for a Data Change Listener.
     * @param iScreen the ID for the screen to be updated.
     * @param type the type of change that is occuring.
     * @param o any object you might want to pass along.
     * @see DataChangeListener#NONE
     * @see DataChangeListener#SAVE
     * @see DataChangeListener#UPDATE
     * @see DataChangeListener#DELETE
     * @see DataChangeListener#TITLE
     */
    public void change( int iScreen,  int type, Object o );
}
