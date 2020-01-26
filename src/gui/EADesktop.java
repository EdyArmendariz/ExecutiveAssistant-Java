package gui;

import events.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerListener;
import java.io.File;

import io.Save;
import io.Load;
import io.OpenDat;
import data.DataModel;
import data.EmployeeDataSource;
import gui.menuitems.JMenuItemEnabled;
import gui.screens.*;
import com.developer.data.devDataHelper;

/**
 * This application's main window.
 */
public class EADesktop
        extends JFrame
{
    private EADesktop thisEADesktop = null;
    public static SimpleEventMgr eventManager = new SimpleEventMgr();
    public static MilestoneGrabberEvent myGrabber = new MilestoneGrabberEvent( new Object() );
    public static Save saveMgr = new Save();
    public static Load loadMgr = new Load();
    public static DataModel myData;
    public static ScreenNavigator screenMgr = new ScreenNavigator( myData );
    private pnlNorth pNorth = new pnlNorth();
    private pnlWest pWest = new pnlWest();
    private pnlEast pEast = new pnlEast();
    private JMenuBar mnuMainBar = new JMenuBar();
    private JMenu mnuFile = new JMenu( "File" );
    private JMenu mnuNewStuff = new JMenu( "New ..." );

    private JMenuItem mnuiLoad = new JMenuItem( "Load" );
    private JMenuItem mnuiOpenDat = new JMenuItem( "Open .dat");
    private JMenuItem mnuiSave = new JMenuItem( "Save" );
    private JMenu mnuEdit = new JMenu( "Edit" );
    private JMenuItemEnabled mnuiNewProject = new JMenuItemEnabled( "New Project", true );
    private JMenuItemEnabled mnuiNewMilestone = new JMenuItemEnabled( "New Milestone", false );
    private JMenuItemEnabled mnuiNewEmployee = new JMenuItemEnabled( "New Employee", true );
    private JMenuItemEnabled mnuiEditProject = new JMenuItemEnabled( "Edit Project", false );

    private JMenuItemEnabled mnuiEditEmployee = new JMenuItemEnabled( "Edit Employee", false );
    private JMenuItemEnabled mnuiDeleteEmployee = new JMenuItemEnabled( "Delete Employee", false );
    private pnlNavigationButtons pnlNavButtons = new pnlNavigationButtons();

    /**
     *  The constructor for the primary JFrame.
     */
    public EADesktop ()
    {
        super();
        thisEADesktop = this;
        this.getContentPane().add( pNorth, BorderLayout.NORTH );
        this.getContentPane().add( pWest, BorderLayout.WEST );
        this.getContentPane().add( pEast, BorderLayout.EAST );
        this.getContentPane().add( screenMgr, BorderLayout.CENTER );
        this.getContentPane().add( pnlNavButtons, BorderLayout.SOUTH );
        this.setTitle( "Executive Assistant" );
        this.setLocation( 300, 300 );
        this.setBounds( new Rectangle( 500, 700 ) );
        this.setLocationRelativeTo( null );
        initMenu();
    }

    /**
     *
     */
    private void initMenu ()
    {
        mnuMainBar.add( mnuFile );
        mnuFile.add( mnuNewStuff );
        mnuNewStuff.add( mnuiNewProject );
        mnuNewStuff.add( mnuiNewMilestone );
        mnuNewStuff.add( mnuiNewEmployee );
        mnuFile.add( mnuiLoad );
        mnuFile.add( mnuiOpenDat );
        mnuFile.add( mnuiSave );

        mnuMainBar.add( mnuEdit );

        mnuEdit.add( mnuiEditProject );
        mnuiEditProject.setParent( new pnlProjectMenu() );
        mnuEdit.add( mnuiEditEmployee );
        mnuEdit.add( mnuiDeleteEmployee );

        this.setJMenuBar( mnuMainBar );

        mnuiLoad.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                myData = ( DataModel ) loadMgr.load();
            }
        } );

        mnuiOpenDat.addActionListener( new ActionListener()
        {
           public void actionPerformed ( ActionEvent ae )
            {
                // Process the .dat file here ...
                OpenDat od = new OpenDat();
                od.load();
                od.getString();
            }
        });

        mnuiSave.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                saveMgr.objectToSave( myData );
                saveMgr.save();
            }
        } );

        mnuiEditProject.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                devDataHelper dvh = devDataHelper.getDataHelper();
                DataModel dm = dvh.getDataModel();
                pnlEditProject pepm = new pnlEditProject();
                pepm.setProjectDataSource( dm.getCurrentProject() );
                pepm.setVisible( true );
            }
        } );

       mnuiEditEmployee.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                devDataHelper dvh = devDataHelper.getDataHelper();
                DataModel dm = dvh.getDataModel(  );
                pnlEditEmployee pee = new pnlEditEmployee( dm.getCurrentEmployee() );
                pee.setVisible( true );
            }
        } );

        mnuiDeleteEmployee.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                devDataHelper dvh = devDataHelper.getDataHelper();
                DataModel dm = dvh.getDataModel( );
                EmployeeDataSource eds = dm.getCurrentEmployee();
                Object[] options = {"Yes",
                    "No"};
                int iResponse = JOptionPane.showOptionDialog( thisEADesktop, "Are you sure you want to delete\n" + eds.firstname + " " + eds.lastname  + " ?",
                       "Delete Employee", JOptionPane.YES_NO_OPTION ,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[1]);

                if ( iResponse == 0 )
                {
                    // Delete the Employee and switch back to the Employee List.
                    dm.deleteCurrentEmployee();
                    EADesktop.getEventManager().fireEvent( DataChangeListener.class,
                        DataChangeListener.UPDATE,
                        new DataChangeEvent( dm, ScreenChangeListener.SHOW_EMPLOYEE_LIST ) );
                    EADesktop.getEventManager().fireEvent(
                        ScreenChangeListener.class,
                        ScreenChangeListener.SHOW_EMPLOYEE_LIST,
                        new ScreenChangeEvent( this ) );
                }

            }
        } );

        mnuiNewProject.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                pnlNewProject pepm = new pnlNewProject();
                pepm.setVisible( true );
            }
        } );

        mnuiNewMilestone.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                pnlNewMilestone pnm = new pnlNewMilestone();
                pnm.setVisible( true );
            }
        } );

        mnuiNewEmployee.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                devDataHelper dvh = devDataHelper.getDataHelper();
                DataModel dm = dvh.getDataModel();

                pnlNewEmployee pne = new pnlNewEmployee();

                if ( dm.hasCurrentlySelectedProject() )
                    pne.setCurrentProject( dm.getCurrentProject() );
                
                pne.setVisible( true );

                EADesktop.getEventManager().fireEvent( DataChangeListener.class,
                        DataChangeListener.UPDATE,
                        new DataChangeEvent( dm, ScreenChangeListener.SHOW_NEW_EMPLOYEE ) );
            }
        } );
    }

    /**
     * Provides Application-wide (a.k.a. global ) access to the Event Handling mechanism.<br>
     *
     * @return the Application's internal Event Handler.<br>
     * @see SimpleEventMgr
     */
    public static SimpleEventMgr getEventManager ()
    {
        return eventManager;
    }

    /**
     * Provides Application-wide (a.k.a. global ) access to the Screen flipping mechanism.<br>
     *
     * @return the Application's internal Screen Handler
     */
    public static ScreenNavigator getScreenManager ()
    {
        return screenMgr;
    }

    /**
     * Return the Application wide <code>MilestoneGrabber</code>.<br>
     *
     * @return
     */
    public static MilestoneGrabberEvent getMilestoneGrabber ()
    {
        return myGrabber;
    }

    /**
     * @param ae
     */
    public void actionPerformed ( ActionEvent ae )
    {
        ae.getActionCommand();
    }
}
