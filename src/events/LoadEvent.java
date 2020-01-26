package events;

/**
 * Notify the GUI components that fresh data has been read into the App.<br>
 * The GUI components must implement <code>LoadEventListener</code>.<br>
 */
public class LoadEvent
        extends SimpleEvent
{
    public final static int LOAD = 1;
    public final static int UNLOAD = 2;

    /**
     *
     * @param o
     */
    public LoadEvent( Object o )
    {
        super( o );
    }

    /**
     *
     * @param listener
     * @param eventType
     * @param event
     */
     public void broadcast(SimpleEventListener listener, int eventType, SimpleEvent event)
    {
        if (listener instanceof LoadEventListener && event instanceof LoadEvent)
        {
            final LoadEventListener lel = (LoadEventListener) listener;

            switch ( eventType )
            {
                case LOAD:
                    lel.load( this.getSource() );
                break;
                case UNLOAD:
                    lel.unload( this.getSource() );
                break;
            }
        }
    }
}
