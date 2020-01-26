package events;

/**
 *
 */
public class DataChangeEvent
        extends SimpleEvent
{
    private int iScreen = 0;

    /**
     *
     * A simple constructor that only satisfies the inheritance requirements.<br>
     * You should always use <code> DataChangeEvent( Object o, int iS )</code> because it provides
     * all of the needed functionality.<br>
     * @param o
     * @see #DataChangeEvent(Object, int)
     */
    public DataChangeEvent( Object o )
    {
        super( o );
    }

    /**
     * Construct a new DataChangeEvent object.
     * @param o The data to send.
     * @param iS The screen that should receive this event.
     */
    public DataChangeEvent( Object o, int iS )
    {
        super ( o );
        iScreen = iS;
    }

    /**
     *
     * @param listener
     * @param eventType
     * @param event
     */
     public void broadcast(SimpleEventListener listener, int eventType, SimpleEvent event)
    {
        if (listener instanceof DataChangeListener && event instanceof DataChangeEvent)
        {
            final DataChangeListener dcel = (DataChangeListener) listener;
            final DataChangeEvent dce = (DataChangeEvent) event;

            switch (eventType )
            {
                case DataChangeListener.SAVE:
                    dcel.change( dce.iScreen, DataChangeListener.SAVE, dce.source );
                break;
                case DataChangeListener.UPDATE:
                    dcel.change( dce.iScreen, DataChangeListener.UPDATE, dce.source );
                break;
                case DataChangeListener.TITLE:
                    dcel.change( dce.iScreen, DataChangeListener.TITLE,  dce.source );
                break;
            }
        }
    }
}
