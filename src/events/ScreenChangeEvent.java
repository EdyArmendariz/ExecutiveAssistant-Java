package events;

import data.DataBean;
import gui.EADesktop;
import gui.screens.pnlProjectMenu;

/**
 * Provides the underlying Event Broadcasting mechanism for screen navigation.<br>
 * Any new screens should be included in this class as well as <code>ScreenNavigator</code>.<br>
 * Some object data may also be passed along with the request for the screen to show itself.<br>
 * @see gui.ScreenNavigator
 */
public class ScreenChangeEvent
        extends SimpleEvent
{
    /**
     * An Event wrapper class.<br>
     * Fires <code>source</code> as an event.
     *
     * @param o the Object being wrapped
     * @see ScreenChangeEvent
     * @see ScreenChangeEvent#broadcast(SimpleEventListener, int, SimpleEvent)
     */
    public ScreenChangeEvent(Object o)
    {
        super(o);
    }

    /**
     * Invokes methods upon all Listener objects that implement the methods defined in <code>ScreenChangeListener</code>.<br>
     * The objects must also be registered with <code>SimpleEventMgr</code> via the <code>addListener( )</code> method.<br>
     *
     * @param listener  the invoking object
     * @param eventType the EventType
     * @param event     the Event to be passed as a parameter to the invoking object
     * @see ScreenChangeListener
     * @see SimpleEventMgr#addListener(Class, SimpleEventListener)
     */
    public void broadcast(SimpleEventListener listener, int eventType, SimpleEvent event)
    {
        if (listener instanceof ScreenChangeListener && event instanceof ScreenChangeEvent)
        {
            final ScreenChangeListener scl = (ScreenChangeListener) listener;
            final ScreenChangeEvent sce = (ScreenChangeEvent) event;

            switch (eventType)
            {
                case ScreenChangeListener.SHOW_MAIN:
                    scl.goToScreen(ScreenChangeListener.SHOW_MAIN, sce);
                    break;
                case ScreenChangeListener.SHOW_EMPLOYEE:
                    scl.goToScreen( ScreenChangeListener.SHOW_EMPLOYEE, sce );
                case ScreenChangeListener.SHOW_EMPLOYEE_LIST:
                    scl.goToScreen(ScreenChangeListener.SHOW_EMPLOYEE_LIST, sce);
                    break;
                case ScreenChangeListener.SHOW_PROJECT_LIST:
                    scl.goToScreen(ScreenChangeListener.SHOW_PROJECT_LIST, sce);
                    break;
                case ScreenChangeListener.SHOW_PROJECT_MENU:
                    scl.goToScreen(ScreenChangeListener.SHOW_PROJECT_MENU, sce);
                    break;
                case ScreenChangeListener.SHOW_PROJECT_EMPLOYEES_LIST:
                    scl.goToScreen(ScreenChangeListener.SHOW_PROJECT_EMPLOYEES_LIST, sce);
                    break;
                case ScreenChangeListener.SHOW_PROJECT_EMPLOYEE:
                    scl.goToScreen(ScreenChangeListener.SHOW_PROJECT_EMPLOYEE, sce);
                    break;
                case ScreenChangeListener.SHOW_MILESTONES_GRAPH:
                    scl.goToScreen(ScreenChangeListener.SHOW_MILESTONES_GRAPH, sce);
                    break;
                case ScreenChangeListener.SHOW_PREVIOUS:
                    if (EADesktop.getScreenManager().isCurrentScreen(ScreenChangeListener.EMPLOYEE))
                    {
                       scl.goToScreen(ScreenChangeListener.SHOW_EMPLOYEE_LIST, sce);
                    } else if (EADesktop.getScreenManager().isCurrentScreen(ScreenChangeListener.PROJECT_EMPLOYEES_LIST))
                    {
                        scl.goToScreen(ScreenChangeListener.SHOW_PROJECT_MENU, sce);

                    } else if (EADesktop.getScreenManager().isCurrentScreen(ScreenChangeListener.EMPLOYEE_LIST))
                    {
                        scl.goToScreen(ScreenChangeListener.SHOW_MAIN, sce);
                    } else if (EADesktop.getScreenManager().isCurrentScreen(ScreenChangeListener.MILESTONES_GRAPH))
                    {
                        scl.goToScreen(ScreenChangeListener.SHOW_PROJECT_MENU, sce);
                    } else if (EADesktop.getScreenManager().isCurrentScreen(ScreenChangeListener.PROJECT_LIST))
                    {
                        scl.goToScreen(ScreenChangeListener.SHOW_MAIN, sce);
                    } else if (EADesktop.getScreenManager().isCurrentScreen(ScreenChangeListener.PROJECT_MENU))
                    {
                        scl.goToScreen(ScreenChangeListener.SHOW_PROJECT_LIST, sce);
                    }
                    else if ( EADesktop.getScreenManager().isCurrentScreen(ScreenChangeListener.PROJECT_EMPLOYEE ))
                    {
                        scl.goToScreen( ScreenChangeListener.SHOW_PROJECT_EMPLOYEES_LIST, sce );
                    }
                    break;

            }
        }
    }

}
