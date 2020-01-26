package events;
import java.util.EventObject;

/**
 * Provides a mechanism for getting and setting data between objects that do not
 * have access to each other's instance variables.
 */
abstract public class SimpleEvent
    extends EventObject
    {
        /**
         * This is called to broadcast the event. (Is obscufaction wasn't needed we could do this with reflection.)
         * <p/>
         * When new events are added, update the broadcast list.
         *
         * @param listener
         * @param eventName
         * @param event
         */
        abstract public void broadcast( SimpleEventListener listener, int eventName, SimpleEvent event);


        /**
         * @param source the source of the event.
         */
        public SimpleEvent(Object source)
        {
            super(source);
        }


        /**
         * @return Object
         */
        public Object getData()
        {
            return data;
        }


        /**
         * Inserts some data into this <code>SimpleEvent</code> object to be broadcasted.<br>
         * @param data
         */
        public void setData(Object data)
        {
            this.data = data;
        }


        /**  Hide our privates. */
        private Object data;
    }

