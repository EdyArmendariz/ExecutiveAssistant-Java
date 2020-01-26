package gui.lists;

import gui.ScreenNavigator;
import gui.EADesktop;
import gui.screens.pnlProjectMenu;
import gui.screens.pnlEditMilestone;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import events.*;
import data.DataModel;
import data.EmployeeDataSource;
import data.MilestoneDataSource;

import java.util.ArrayList;

import com.developer.data.devDataHelper;

/**
 * This class encapsulates a JList that contains the Milestones for the Employee.<br>
 */
public class lstEmployeeMilestones
        extends JList
        implements DataChangeListener
{
    lstEmployeeMilestones.MySelectionHandler slsh = new lstEmployeeMilestones.MySelectionHandler();
    private DefaultListSelectionModel dlsmListModel = new DefaultListSelectionModel();
    private lstEmployeeMilestones thisList = null;

    /**
     *
     */
    public lstEmployeeMilestones ()
    {
        super();
        EADesktop.getEventManager().removeListener( DataChangeListener.class, this );
        EADesktop.getEventManager().addListener( DataChangeListener.class, this );
        this.setSelectionModel( dlsmListModel );
        dlsmListModel.addListSelectionListener( slsh );
        thisList = this;
    }

    /**
     * @param iScreen
     * @param iType
     * @param o
     */
    public void change ( int iScreen, int iType, Object o )
    {
        if ( iScreen == ScreenChangeListener.SHOW_EMPLOYEE )
            if ( iType == DataChangeListener.UPDATE )
                if ( o instanceof EmployeeDataSource )
                {
                    devDataHelper ddh = devDataHelper.getDataHelper();
                    DataModel dm = ddh.getDataModel();
                    this.setListData( dm.getListOfMilestonesForEmployee( ( ( EmployeeDataSource ) o ).id ) );
                }
    }


    /**
     * Handles the user's selection of an item from this list.<br>
     * Displays a list of Milestones that are assigned to this employee.<br>
     */
    class MySelectionHandler
            implements ListSelectionListener
    {
        public void valueChanged ( ListSelectionEvent e )
        {
            MilestoneDataSource m = ( MilestoneDataSource ) thisList.getSelectedValue();
            if ( m == null )
                return;

            pnlEditMilestone pem = new pnlEditMilestone( m );
            pem.addKeyListener( pem );
            pem.setVisible( true );

            dlsmListModel.clearSelection();
        }
    }
}
