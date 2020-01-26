package gui.lists;

import events.ScreenChangeListener;
import events.DataChangeListener;

import javax.swing.*;

import data.DataModel;
import data.ProjectDataSource;
import gui.EADesktop;

/**
 * Displays the list of all employees associated with a specific project.<br>
 * This differs from <code>lstEmployees</code>.<br>
 *
 * @see gui.lists.lstEmployees
 */
public class lstProjectEmployees
        extends JList
        implements DataChangeListener
{
    private Object [] objList = {"Employee1"};

    /**
     * This constructor displays the list of all employees associated with a specific project.<br>
     * It differs from <code>lstEmployees</code>.<br>
     * @see lstEmployees
     */
    public lstProjectEmployees ()
    {
        super();
        EADesktop.getEventManager().removeListener( DataChangeListener.class, this );
        EADesktop.getEventManager().addListener( DataChangeListener.class, this );
        this.setListData( objList );
    }

     /**
      * Modifies/Updates this list.<br>
      * Responds to  <code>DataChangeEvent</code>s.<br>
     * @param iType
     * @param o
     */
    public void change ( int iScreen, int iType, Object o )
    {
            if ( iType == DataChangeListener.UPDATE )
                if ( o instanceof DataModel )
                {
                    ProjectDataSource pds = ( (DataModel)o).getCurrentProject();
                    if ( pds != null )
                        this.setListData( pds.getProjectEmployees());
                }
    }
}
