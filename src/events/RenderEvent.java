package events;

/**
 * Allows an object without an instance variable to a <code>myCanvas</code> object
 * to send a <code>repaint()</code> request.<br>
 * Examples, adding, removing or moving a milestone would require that <code>myCanvas</code>
 * repaint itself.<br>
 */
public class RenderEvent
        extends SimpleEvent
{
    /**
     * Let implementers of interface <code>RenderListener</code> respond to
     * <code>repaint()</code> requests.<br>
     * @param o
     */
    public RenderEvent( Object o )
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
         if (listener instanceof RenderListener && event instanceof RenderEvent)
        {
            final RenderListener rl = (RenderListener )  listener;

            switch( eventType )
            {
                case 0:
                    rl.reRender();
                break;
            }
        }
    }
}
