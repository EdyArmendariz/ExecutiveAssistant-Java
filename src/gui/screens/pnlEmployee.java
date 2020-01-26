package gui.screens;

import com.developer.data.devDataHelper;
import data.DataModel;
import data.EmployeeDataSource;
import data.MilestoneDataSource;
import events.DataChangeListener;
import events.ScreenChangeListener;
import gui.EADesktop;
import gui.lists.lstEmployeeMilestones;
import gui.lists.lstEmployeeProjects;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Shows an Employee's information.<br>
 * This screen is shown from the Employee List component on pnlEmployeeList_delme.<br>
 *
 * @see gui.lists.lstEmployees#slsh
 * @see gui.screens.pnlEmployeeList
 */
public class pnlEmployee
        extends JPanel
        implements DataChangeListener
{
    private GridBagLayout lyt = new GridBagLayout();
    private GridBagConstraints con = new GridBagConstraints();
    private JLabel lblEmployeeFname = new JLabel( "pnlEmployee" );
    private JLabel lblEmployeeLname = new JLabel( "Last Name" );
    private JLabel lblProjectName = new JLabel( "Project" );
    private JButton btnAddEmployeeToMilestone = new JButton ( "Add Milestone" );
    

    /**
     *
     */
    private lstEmployeeProjects lstProjects = new lstEmployeeProjects();
    /**
     *
     */
    private lstEmployeeMilestones listEmployeeMilestones = new lstEmployeeMilestones();
    /**
     *
     */
    private EmployeeDataSource myEmployee = new EmployeeDataSource();

    /**
     * Constructs the Employee Screen.
     */
    public pnlEmployee ()
    {
        // Invoke the JPanel default constructor
        super();

        // De-register then re-register this class to listen for Broadcasted Events
        EADesktop.getEventManager().removeListener( DataChangeListener.class, this );
        EADesktop.getEventManager().addListener( DataChangeListener.class, this );

        // Initialize this screen's gui.
        initGUI();
        linkProjectListToMilestoneList();
        this.setToolTipText ( pnlEmployee.class.toString() );
    }


    private void initGUI ()
    {
        this.setLayout( lyt );
        this.setMinimumSize( new Dimension( 280, 300 ) );

        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 1;
        con.gridheight = 1;
        con.anchor = GridBagConstraints.NORTH;
        lblEmployeeFname.setFont( new Font( "Arial", Font.BOLD, 18 ) );
        this.add( lblEmployeeFname, con );

        // The filler between the first name and the last name.
        Box.Filler bf = new Box.Filler( new Dimension( 10, 10 ), new Dimension( 20, 20 ), new Dimension( 30, 30 ) );
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 1;
        con.gridy = 0;
        this.add( bf, con );

        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 2;
        con.gridy = 0;
        con.gridwidth = 2;
        con.gridheight = 1;
        lblEmployeeLname.setFont( new Font( "Arial", Font.BOLD, 18 ) );
        this.add( lblEmployeeLname, con );

        // The filler between the name labels and the pull down menu.
        bf = new Box.Filler( new Dimension( 20, 10 ), new Dimension( 40, 20 ), new Dimension( 50, 30 ) );
        con.gridx = 0;
        con.gridy = 1;
        this.add( bf, con );

        con.weighty = 2;
        con.weightx = 2;
        con.fill = GridBagConstraints.BOTH;
        con.gridx = 0;
        con.gridy = 3;
        con.gridwidth = 4;
        con.gridheight = 3;
        lstProjects.setFont( new Font( "Arial", Font.PLAIN, 14 ) );
        lstProjects.setFixedCellHeight( 20 );
        lstProjects.setFixedCellWidth( 250 );
        JPanel jpProjectsPanel = new JPanel( new BorderLayout() );
        jpProjectsPanel.add( new JScrollPane( lstProjects ) );
        TitledBorder tblProjects = BorderFactory.createTitledBorder( "Project" );
        jpProjectsPanel.setBorder( tblProjects );
        this.add( jpProjectsPanel, con );

        con.fill = GridBagConstraints.BOTH;
        con.gridx = 0;
        con.gridy = 6;
        con.gridwidth = 4;
        con.gridheight = 5;
        listEmployeeMilestones.setFont( new Font( "Arial", Font.PLAIN, 14 ) );
        listEmployeeMilestones.setFixedCellHeight( 20 );
        listEmployeeMilestones.setFixedCellWidth( 250 );
        JPanel jpMilestonePanel = new JPanel( new BorderLayout() );
        jpMilestonePanel.add( new JScrollPane ( listEmployeeMilestones ) );
        TitledBorder tbMilestone = BorderFactory.createTitledBorder( "Milestones" );
        jpMilestonePanel.setBorder( tbMilestone );
        this.add( jpMilestonePanel, con );
    }

    /**
     * A quick and dirty way to connect the actions received from the Project listEmployeeMilestones to
     * the Milestones listEmployeeMilestones.<br>
     * Clicking on the Project listEmployeeMilestones will load all of this employee's milestone into the
     * Milestone JList.<br>
     */
    private void linkProjectListToMilestoneList()
    {
        lstProjects.linkTo( listEmployeeMilestones );
    }

    /**
     * Populates this screen with data.<br>
     * Implement the response to a request to show this screen.<br>
     *
     * @param iScreen the screen to be displayed
     * @param iEvent the Event being handled
     * @param o
     */
    public void change ( int iScreen, int iEvent, Object o )
    {
        if ( iScreen == ScreenChangeListener.SHOW_EMPLOYEE )
            if ( iEvent == UPDATE && o instanceof EmployeeDataSource )
            {
                // Capture the name of the employee to display on this screen.
                EmployeeDataSource eds = ( EmployeeDataSource ) o;
                myEmployee = eds;
                lblEmployeeFname.setText( eds.firstname );
                lblEmployeeLname.setText( eds.lastname + " is working on");

                devDataHelper ddh = devDataHelper.getDataHelper();
                DataModel dm = ddh.getDataModel();

                // Get the projects to which this employee is currently assigned.
                ArrayList alProjIDs = eds.getProjectIDs();
                ArrayList alProjects = new ArrayList();
                for ( int i = 0; i < alProjIDs.size(); i ++ )
                {
                    Integer ii = (Integer)alProjIDs.get( i );
                    alProjects.add( dm.getProject( ii.intValue() ) );
                }
                lstProjects.setListData( alProjects.toArray() );

                // If this employee isn't assigned to a Project, then just exit.
                if ( alProjects.size() <= 0 )
                {
                    listEmployeeMilestones.setListData( alProjects.toArray() );
                    return;
                }

                // Otherwise, get all of the Milestones for the first Project in the list
                // that this employee is assigned to...
                Object objMiles [] = dm.getListOfMilestonesForEmployee( myEmployee.id );

                int iSize = objMiles.length;
                ArrayList alList  = new ArrayList();

                for ( int j = 0; j < iSize; j++ )
                {
                    MilestoneDataSource mds = (MilestoneDataSource) objMiles[j];
                    Integer ii = (Integer)alProjIDs.get( 0 );

                    if ( mds.getProjectID() == ii.intValue() )
                        alList.add( mds );
                }

                listEmployeeMilestones.setListData( alList.toArray() );

            }
    }

}
