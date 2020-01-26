package events;

/**
 *
 */
public interface LoadEventListener
        extends SimpleEventListener
{
    /**
     *
     * @param o
     */
    public void load( Object o );

    /**
     * 
     * @param o
     */
    public void unload( Object o );
}
