package gui.lists;

import data.EmployeeDataSource;
import data.DataModel;
import data.ProjectDataSource;
import data.MilestoneDataSource;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import gui.EADesktop;
import events.DataChangeListener;
import events.ScreenChangeListener;
import com.developer.data.devDataHelper;

import java.util.List;
import java.util.ArrayList;


/**
 * This class encapsulates a JList that contains the Projects for the Employee.<br>
 */
public class lstEmployeeProjects
        extends JList
        implements DataChangeListener
{
    lstEmployeeProjects.MySelectionHandler slsh = new lstEmployeeProjects.MySelectionHandler();
    private DefaultListSelectionModel dlsmListModel = new DefaultListSelectionModel();
    private lstEmployeeProjects thisList = null;
    private lstEmployeeMilestones  thisMilestoneList = null;

    /**
     *
     */
    public lstEmployeeProjects ()
    {
        super();
        EADesktop.getEventManager().removeListener( DataChangeListener.class, this );
        EADesktop.getEventManager().addListener( DataChangeListener.class, this );
        this.setSelectionModel( dlsmListModel );
        dlsmListModel.addListSelectionListener( slsh );
        thisList = this;
    }


    /**
     * A quick-and-dirty way to connect the Project list with the milestone list.<br>
     * @param lst
     */
    public void linkTo ( JList lst )
    {
        thisMilestoneList = ( lstEmployeeMilestones ) lst;
    }

     /**
      * 
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
                    this.setListData( dm.getListOfProjectsForEmployee( dm.getCurrentEmployee().id ) );
                }
    }

    /**
     * Handles the user's selection of an item from this employee's Project's list.<br>
     * Displays a list of Milestones that are assigned to this employee.<br>
     */
    class MySelectionHandler
            implements ListSelectionListener
    {
        public void valueChanged ( ListSelectionEvent e )
        {
            ProjectDataSource m = ( ProjectDataSource ) thisList.getSelectedValue();
            if ( m == null )
                return;
            List <MilestoneDataSource> l =  m.getMilestoneList();

            devDataHelper ddh = devDataHelper.getDataHelper();
            DataModel dm = ddh.getDataModel();
            dm.setCurrentProject( m );
            EmployeeDataSource eds = dm.getCurrentEmployee();

            ArrayList alEmpsMiles = new ArrayList();
            for ( int i = 0; i < l.size(); i++ )
            {
                MilestoneDataSource mds =  l.get( i );
                if ( mds.isEmployeeAssigned( eds.id ) )
                    alEmpsMiles.add( mds );

            }

            thisMilestoneList.setListData( alEmpsMiles.toArray() );

            dlsmListModel.clearSelection();
        }
    }
}



