package gui.lists;

import events.*;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import gui.screens.pnlEmployee;
import gui.EADesktop;
import data.DataModel;
import data.EmployeeDataSource;
import com.developer.data.devDataHelper;

/**
 * Displays a unique list of all employees across all projects.<br>
 */
public class lstEmployees
        extends JList
        implements DataChangeListener
{
    private Object [] objList = {"Employee 1", "Employee 2", "Employee 3", "Employee 4",
            "Employee 5", "Employee 6", "Employee 7"};

    SharedListSelectionHandler slsh = new SharedListSelectionHandler();
    private DefaultListSelectionModel dlsmListModel = new DefaultListSelectionModel();
    private lstEmployees thisList = null;

    /**
     * Constructs a list of employees initialized with meaningless default data.<br>
     */
    public lstEmployees ()
    {
        super();
        EADesktop.getEventManager().removeListener( DataChangeListener.class, this );
        EADesktop.getEventManager().addListener( DataChangeListener.class, this );
        this.setListData( objList );
        this.setSelectionModel( dlsmListModel );
        dlsmListModel.addListSelectionListener( slsh );
        thisList = this;
    }


    /**
     * Set an array of Strings containing the first names of all employees across
     * all projects.<br>
     *
     * @param o
     */
    public void setListData ( Object o [] )
    {
        super.setListData( o );
    }

    /**
     * Respond to a <code>DataChangeEvent</code>.
     * @param iScreen
     * @param iType
     * @param o
     */
    public void change ( int iScreen, int iType, Object o )
    {
        if ( iScreen == ScreenChangeListener.SHOW_EMPLOYEE_LIST )
            if ( iType == DataChangeListener.UPDATE )
                if ( o instanceof DataModel )
                {
                    this.setListData( ( ( DataModel ) o ).getEmployees() );
                }
    }

    /**
     * Removes the default <code>ListSelectionListener</code> and replaces it with <code>lsl</code>.<br>
     * The default original is <code>SharedListSelectionHandler</code>.
     * @param lsl
     * @see SharedListSelectionHandler
     */
    public void changeSelectionBehavior( ListSelectionListener lsl )
    {
        dlsmListModel.removeListSelectionListener( slsh );
        dlsmListModel.addListSelectionListener( lsl );
    }

    /**
     * This class provides list selection handling specific
     * to the class <code>lstEmployees</code>.<br>
     *
     * @see lstEmployees
     */
    class SharedListSelectionHandler
            implements ListSelectionListener
    {
        /**
         * Value Changed
         *
         * @param e
         */
        public void valueChanged ( ListSelectionEvent e )
        {
            EmployeeDataSource eds = ( EmployeeDataSource ) thisList.getSelectedValue();
            if ( eds == null )
                return;

            // Set the Currently Selected Employee ...
            devDataHelper dvh = devDataHelper.getDataHelper();
            DataModel dm = dvh.getDataModel();
            dm.setCurrentEmployee( eds );
            EADesktop.getScreenManager().goToScreen( ScreenChangeListener.EMPLOYEE );
            EADesktop.getEventManager().fireEvent( DataChangeListener.class,
                    DataChangeListener.UPDATE, new DataChangeEvent( ( Object ) eds, ScreenChangeListener.SHOW_EMPLOYEE ) );
            EADesktop.getEventManager().fireEvent( EnableListener.class, EnableListener.ENABLE_YOURSELF,
                        new EnableEvent( "Edit Employee" ) );
            EADesktop.getEventManager().fireEvent( EnableListener.class, EnableListener.ENABLE_YOURSELF,
                        new EnableEvent( "Delete Employee" ) );
            dlsmListModel.clearSelection();
        }
    }
}
