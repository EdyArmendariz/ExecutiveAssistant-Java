package events;


import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.ref.WeakReference;

/**
 * This class provides rudimentary Event Handling functionality.<br>
 * It adds & removes Listeners and fires Events.<br>
 * @see SimpleEventMgr#addListener(Class, SimpleEventListener)
 * @see SimpleEventMgr#removeListener(Class, SimpleEventListener)
 * @see SimpleEventMgr#fireEvent(Class, int, SimpleEvent)
 */
public class SimpleEventMgr
{
    /**
     * This class provides rudimentary Event Handling functionality.<br>
     * It adds & removes Listeners and fires Events.<br>
     *
     * @see SimpleEventMgr#addListener(Class, SimpleEventListener)
     * @see SimpleEventMgr#removeListener(Class, SimpleEventListener)
     * @see SimpleEventMgr#fireEvent(Class, int, SimpleEvent)
     */
    public SimpleEventMgr()
    {
        disabledEvents = new HashMap();
        eventListeners = new HashMap();
    }

    /**
     * Registers a Class and its Listener with the Event Manager.
     *
     * @param classListener
     * @param sel
     */
    public void addListener(Class classListener, SimpleEventListener sel)
    {
        List l = (List) eventListeners.get(classListener);

        // if there are no listeners...
        if (l == null)
        {
            // ... make a new list
            l = new ArrayList();
        }

        l.add(new WeakReference(sel));
        eventListeners.put(classListener, l);
    }

    /**
     * De-registers a Class and its Listener with the Event Manager.
     *
     * @param classListener
     * @param sel
     */
    public void removeListener(Class classListener, SimpleEventListener sel)
    {
        // First get the list of listeners.
        List l = (List) eventListeners.get(classListener);

        // Is there already a list?
        if (null != l)
        {
            // Look for the sel to remove.
            for (int pos = 0; pos < l.size(); pos++)
            {
                WeakReference wr = (WeakReference) l.get(pos);
                SimpleEventListener oldListener = (SimpleEventListener) wr.get();
                if (oldListener == sel)
                {
                    // No longer needed.
                    l.remove(pos);
                    break;
                }
            }
        }
    }

    /**
     * Route Events to the appropriate Listener.<br>
     *
     * @param iListen   the class that implements a Listener
     * @param eventType the type of fireMe as specified by
     *                  <code>fireMe</code>, (i.e. <code>CHANGE_DATA</code>)
     * @param fireMe    the Event being fired.

     */
    public void fireEvent(Class iListen, int eventType, SimpleEvent fireMe)
    {
        // Are we currently allowing events to be processed?
        if (disableEventProcessing > 0)
            return;


        // Is this specific fireMe allowed?
        if (disabledEvents.containsKey(iListen))
            return;

        ArrayList listenerList = (ArrayList) eventListeners.get(iListen);

        // Is there already a list?
        if (null != listenerList)
        {
            // Broadcast the fireMe.
            int pos = 0;
            while (pos < listenerList.size())
            {
                WeakReference wr = (WeakReference) listenerList.get(pos);
                SimpleEventListener listener = (SimpleEventListener) wr.get();
                if (null != listener)
                {
                    // Broadcast.
                    fireMe.broadcast(listener, eventType, fireMe);
                    pos++;
                } else
                {
                    // No longer needed.
                    listenerList.remove(pos);
                }
            }
        }
    }

    private Map disabledEvents = null;
    private Map eventListeners = null;
    private int disableEventProcessing = 0;
}
