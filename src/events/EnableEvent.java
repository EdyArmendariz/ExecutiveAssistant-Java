package events;

/**
 * 
 */
public class EnableEvent
        extends SimpleEvent
{
    /**
     *
     * @param o
     */
    public EnableEvent( Object o )
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
          if (listener instanceof EnableListener && event instanceof EnableEvent)
        {
            final EnableListener vl = (EnableListener) listener;
            final EnableEvent ve = (EnableEvent) event;

            switch (eventType)
            {
                  case EnableListener.ENABLE_YOURSELF:
                        vl.setEnabled( EnableListener.ENABLE_YOURSELF, ve.getSource() );
                    break;
                  case EnableListener.DISABLE_YOURSELF:
                        vl.setEnabled( EnableListener.DISABLE_YOURSELF , ve.getSource() );
                    break;
                  case EnableListener.DISABLE_ALL:
                        vl.setEnabled( EnableListener.DISABLE_ALL, ve.getSource() );
                    break;
            }

        }

    }


}
