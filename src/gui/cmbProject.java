package gui;

import events.DataChangeListener;
import events.ScreenChangeListener;

import javax.swing.*;

import data.DataModel;
import data.ProjectDataSource;

/**
 * Created by IntelliJ IDEA.
 * User: Edy
 * Date: Apr 22, 2007
 * Time: 8:18:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class cmbProject
        extends JComboBox
        implements DataChangeListener
{

    /**
     *
     */
    public cmbProject ()
    {
        EADesktop.getEventManager().removeListener( DataChangeListener.class, this );
        EADesktop.getEventManager().addListener( DataChangeListener.class, this );
    }


    /**
     * @param iScreen
     * @param iType
     * @param o
     */
    public void change ( int iScreen, int iType, Object o )
    {
        // clear out the pull down list
        this.removeAllItems();
        if ( iScreen == ScreenChangeListener.SHOW_NEW_EMPLOYEE )
            if ( iType == DataChangeListener.UPDATE )
                if ( o instanceof DataModel )
                {
                    DataModel dm = ( DataModel ) o;
                    int count = dm.getNumberOfProjects();

                    for ( int i = 0; i < count; i ++ )
                    {
                        ProjectDataSource pds = dm.getProjectByIndex( i );
                        this.addItem( pds );

                    }
                }

    }

}
