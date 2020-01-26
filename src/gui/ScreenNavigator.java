package gui;

import events.ScreenChangeEvent;
import events.ScreenChangeListener;
import events.EnableListener;
import events.EnableEvent;

import javax.swing.*;
import java.awt.*;

import data.DataModel;
import gui.screens.*;
import com.developer.data.devDataHelper;

/**
 * Provides screen navigation functionality.<br>
 * Any new screens should be included in this class.<br>
 *
 * @see CardLayout
 */
public class ScreenNavigator
        extends JPanel
        implements events.ScreenChangeListener
{
    DataModel myDataModel;
    private pnlMain pMain = new pnlMain();
    private pnlProjectList pProjectList = new pnlProjectList();
    private pnlEmployee pEmployee = new pnlEmployee();
    private pnlEmployeeList  pEmployeeList = new pnlEmployeeList();
    private pnlProjectMenu pProjectMenu = new pnlProjectMenu();
    private pnlProjectEmployee pProjectEmployee = new pnlProjectEmployee();
    /**
     *
     */
    private pnlProjectEmployeeList pProjectEmployeeList = new pnlProjectEmployeeList();
    private String strCurrentPanel = new String( ScreenChangeListener.MAIN );
    private CardLayout crdLayout = new CardLayout();

    /**
     * Constructs the screen navigator that displays screens on the request of the user.<br>
     * Uses <code>ScreenChangeEvents</code> to control its navigation.<br>
     *
     * @param dm The data model of this application.
     * @see ScreenChangeEvent
     * @see ScreenChangeListener
     */
    public ScreenNavigator ( DataModel dm )
    {
        super();
        EADesktop.getEventManager().removeListener( ScreenChangeListener.class, this );
        EADesktop.getEventManager().addListener( ScreenChangeListener.class, this );
        myDataModel = dm;

        listOfScreensToNavigate();
        this.setLayout( crdLayout );
        crdLayout.show( this, ScreenChangeListener.MAIN );
    }


    /**
     * This method adds a Screen to this CardLayout. <br>
     * All panels that should be navigated <b>must</b> be added here.<br>
     */
    private void listOfScreensToNavigate ()
    {
        this.add( pMain, ScreenChangeListener.MAIN );
        crdLayout.addLayoutComponent( pMain, ScreenChangeListener.MAIN );

        this.add( pProjectList, ScreenChangeListener.PROJECT_LIST );
        crdLayout.addLayoutComponent( pProjectList, ScreenChangeListener.PROJECT_LIST );

        this.add( pEmployeeList, ScreenChangeListener.EMPLOYEE_LIST );
        crdLayout.addLayoutComponent( pEmployeeList, ScreenChangeListener.EMPLOYEE_LIST );

        this.add( pProjectMenu, ScreenChangeListener.PROJECT_MENU );
        crdLayout.addLayoutComponent( pProjectMenu, ScreenChangeListener.PROJECT_MENU );

        this.add( pProjectEmployeeList, ScreenChangeListener.PROJECT_EMPLOYEES_LIST );
        crdLayout.addLayoutComponent( pProjectEmployeeList, ScreenChangeListener.PROJECT_EMPLOYEES_LIST );

        // this.add( pMilestonesGraph, ScreenChangeListener.MILESTONES_GRAPH );
        // crdLayout.addLayoutComponent( pMilestonesGraph, ScreenChangeListener.MILESTONES_GRAPH );

        this.add( pProjectEmployee, ScreenChangeListener.PROJECT_EMPLOYEE );
        crdLayout.addLayoutComponent( pProjectEmployee, ScreenChangeListener.PROJECT_EMPLOYEE );

        this.add( pEmployee, ScreenChangeListener.EMPLOYEE_LIST );
        crdLayout.addLayoutComponent( pEmployee, ScreenChangeListener.EMPLOYEE );
    }


    /**
     * Implements the Interface that allows this App to 'flip' through the Panels in the CardLayout.<br>
     *
     * @param screenID The ID number of the requested screen.
     * @param sce      holds any data to be passed to the requested screen.
     */
    public boolean goToScreen ( int screenID, ScreenChangeEvent sce )
    {
        String s = new String();
        boolean bHandled = true;

        // Disable all menu bar items, then enable only the necessary menu items.
        EADesktop.getEventManager().fireEvent(
                        EnableListener.class,
                        EnableListener.DISABLE_ALL ,
                        new EnableEvent( new Object() ) );

        // These two menu options should always be available.
        EADesktop.getEventManager().fireEvent( EnableListener.class, EnableListener.ENABLE_YOURSELF,
                        new EnableEvent( "New Project" ) );
        EADesktop.getEventManager().fireEvent( EnableListener.class, EnableListener.ENABLE_YOURSELF,
                        new EnableEvent( "New Employee" ) );

        switch ( screenID )
        {
            case ScreenChangeListener.SHOW_MAIN:
                crdLayout.show( this, ScreenChangeListener.MAIN );
                s = ScreenChangeListener.MAIN;
                break;
            case ScreenChangeListener.SHOW_PROJECT_LIST:
                crdLayout.show( this, ScreenChangeListener.PROJECT_LIST );
                s = ScreenChangeListener.PROJECT_LIST;
                break;
            case ScreenChangeListener.SHOW_EMPLOYEE_LIST:
                crdLayout.show( this, ScreenChangeListener.EMPLOYEE_LIST );
                s = ScreenChangeListener.EMPLOYEE_LIST;
                //EADesktop.getEventManager().fireEvent(
                //        EnableListener.class,
                //        EnableListener.ENABLE_YOURSELF,
                //        new EnableEvent( new pnlEmployeeList_delme() ) );
                break;
            case ScreenChangeListener.SHOW_PROJECT_MENU :
                crdLayout.show( this, ScreenChangeListener.PROJECT_MENU );
                s = ScreenChangeListener.PROJECT_MENU;
                EADesktop.getEventManager().fireEvent( EnableListener.class, EnableListener.ENABLE_YOURSELF,
                        new EnableEvent( "Edit Project" ) );
                EADesktop.getEventManager().fireEvent( EnableListener.class, EnableListener.ENABLE_YOURSELF,
                        new EnableEvent( "New Milestone" ) );
                break;
            case ScreenChangeListener.SHOW_PROJECT_EMPLOYEES_LIST:
                crdLayout.show( this, ScreenChangeListener.PROJECT_EMPLOYEES_LIST );
                s = ScreenChangeListener.PROJECT_EMPLOYEES_LIST;
                EnableEvent ee = new EnableEvent( "New Employee" );
                ee.setData( devDataHelper.getDataHelper().getDataModel().getCurrentProject() );
                EADesktop.getEventManager().fireEvent( EnableListener.class, EnableListener.ENABLE_YOURSELF,
                        ee );
                break;
            case ScreenChangeListener.SHOW_MILESTONES_GRAPH:
                crdLayout.show( this, ScreenChangeListener.MILESTONES_GRAPH );
                s = ScreenChangeListener.PROJECT_EMPLOYEES_LIST;
                break;
            case ScreenChangeListener.SHOW_PROJECT_EMPLOYEE:
                crdLayout.show( this, ScreenChangeListener.PROJECT_EMPLOYEE );
                s = ScreenChangeListener.PROJECT_EMPLOYEE;
                break;
            case ScreenChangeListener.SHOW_EMPLOYEE:
                crdLayout.show( this, ScreenChangeListener.EMPLOYEE );
                s = ScreenChangeListener.EMPLOYEE;
                break;
        }

        strCurrentPanel = s;
        return bHandled;
    }

    /**
     * @param s
     */
    public void goToScreen ( String s )
    {
        crdLayout.show( this, s );
        strCurrentPanel = s;
    }

    /**
     * @param s
     * @return
     */
    public boolean isCurrentScreen ( String s )
    {
        return strCurrentPanel.equalsIgnoreCase( s );
    }

    /**
     * @return
     */
    public String getCurrentScreen ()
    {
        return strCurrentPanel;
    }
}
