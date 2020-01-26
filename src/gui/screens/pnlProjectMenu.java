package gui.screens;

import events.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import data.DataModel;
import data.ProjectDataSource;
import com.developer.data.devDataHelper;
import gui.EADesktop;


/**
 *
 */
public class pnlProjectMenu
        extends JPanel
        implements DataChangeListener
{
    private GridLayout lytGrid = new GridLayout( 7, 3 );
    private JLabel lblProjectName = new JLabel( "Project" );
    private JButton btnMilestones = new JButton( "View Milestones" );
    private JButton btnEmployees = new JButton( "View Employees" );

    /**
     *
     */
    public pnlProjectMenu ()
    {
        super();
        EADesktop.getEventManager().removeListener( ScreenChangeListener.class, this );
        EADesktop.getEventManager().addListener( ScreenChangeListener.class, this );
        EADesktop.getEventManager().removeListener( DataChangeListener.class, this );
        EADesktop.getEventManager().addListener( DataChangeListener.class, this );
        initializeButtonActions();

        lblProjectName.setFont( new Font( "Arial", Font.BOLD, 22 ) );
        lblProjectName.setHorizontalAlignment( SwingConstants.CENTER );
        lblProjectName.setAlignmentX( Component.TOP_ALIGNMENT );
        btnMilestones.setAlignmentX( Component.CENTER_ALIGNMENT );
        btnMilestones.setToolTipText( "Show Milestones Graph" );
        btnEmployees.setAlignmentX( Component.CENTER_ALIGNMENT );
        lytGrid.setHgap( 30 );
        lytGrid.setVgap( 10 );
        this.setLayout( lytGrid );
        this.add( lblProjectName );
        this.add( btnMilestones );
        this.add( btnEmployees );
        this.setToolTipText( pnlProjectMenu.class.toString() );
    }

    /**
     * Allows objects without an instance variable to change/modify the display of this screen.<br>
     *
     * @param iScreen
     * @param i
     * @param o
     */
    public void change ( int iScreen, int i, Object o )
    {
        if ( iScreen == ScreenChangeListener.SHOW_PROJECT_MENU )
            if ( i == TITLE && o instanceof String )
            {
                devDataHelper dvh = devDataHelper.getDataHelper();
                DataModel dm = dvh.getDataModel();
                ProjectDataSource pds = dm.getProject( ( String ) o );
                dm.setCurrentProject( pds );
                lblProjectName.setText( ( String ) o );
            }
    }

    /**
     * Initializes the buttons on this JPanel.
     * <code>btnMilestones</code> opens <code>pnlMilestonesGraph</code> and then
     * disables itself so that only one <code>pnlMilestonesGraph</code> can be displayed
     * at a time.  This button is re-enabled by the <code>windowClosing()</code> method
     * in <code>pnlMilestonesGraph</code>.
     */
    private void initializeButtonActions ()
    {
        btnMilestones.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                pnlMilestonesGraph pmg = new pnlMilestonesGraph();
                pmg.loadMilestones();
                pmg.attachInvokingButton( btnMilestones );
                pmg.setVisible( true );
                btnMilestones.setEnabled( false );
            }
        } );

        btnEmployees.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                devDataHelper dvh = devDataHelper.getDataHelper();
                DataModel dm = dvh.getDataModel();
                ProjectDataSource pds = dm.getCurrentProject();

                // Notify the screen displaying the Project Employee List to show itself.
                EADesktop.getEventManager().fireEvent(
                        ScreenChangeListener.class,
                        ScreenChangeListener.SHOW_PROJECT_EMPLOYEES_LIST,
                        new ScreenChangeEvent( new Object() ) );

                // Notify the Project Employee List to update its data
                EADesktop.getEventManager().fireEvent( DataChangeListener.class,
                        DataChangeListener.UPDATE,
                        new DataChangeEvent( pds, ScreenChangeListener.SHOW_PROJECT_EMPLOYEES_LIST ) );
            }
        } );
    }
}
