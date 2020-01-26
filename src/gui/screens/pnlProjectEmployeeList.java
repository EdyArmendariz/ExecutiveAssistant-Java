package gui.screens;

import events.*;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import com.developer.data.devDataHelper;
import data.DataModel;
import data.ProjectDataSource;
import data.EmployeeDataSource;
import gui.lists.lstProjectEmployees;
import gui.lists.lstEmployees;
import gui.EADesktop;

/**
 * Displays the lstAssignedEmployees of employees for the current Project.<br>
 * This differs from <code>pnlEmployeeList</code>.
 * @see pnlEmployeeList
 */
public class pnlProjectEmployeeList
        extends JPanel
        implements DataChangeListener
{
    private JLabel lblSelectEmployee = new JLabel( " Employees working on :" );
    /** The JList of employees assigned to this project. */
    private lstProjectEmployees lstAssignedEmployees = new lstProjectEmployees();
    /** The JList of all employees stored in the database.<br>
     * They may or may not already be assigned to this project. */
    private lstEmployees lstAllEmployees = new lstEmployees();
    private DefaultListSelectionModel dlsmListModel = new DefaultListSelectionModel();
    private DinkySelector ds = new DinkySelector();
    /** This button adds employees to this project. */
    private JButton jbtnAddEmployee = new JButton( "Add" );
    /** This button removes employees from this project. */
    private JButton jbRemoveEmployee = new JButton( "Remove" );
    private JButton jbViewEmployee = new JButton ( "View" );
    private GridBagLayout lyt = new GridBagLayout();
    private GridBagConstraints con = new GridBagConstraints();

    /**
     * Construct the Screen to show the current project's <code>lstAssignedEmployees</code>
     *  of employees.<br>
     * Populated with default data.<br>
     * To populate the <code>lstAssignedEmployees</code> with data, fire  <code>DataChangeEvent::change()</code> .<br>
     *
     * @see DataChangeListener
     */
    public pnlProjectEmployeeList ()
    {
        super();
        this.setToolTipText( "pnlProjectEmployeeList" );
        EADesktop.getEventManager().removeListener( DataChangeListener.class, this );
        EADesktop.getEventManager().addListener( DataChangeListener.class, this );
        lblSelectEmployee.setFont( new Font( "Arial", Font.BOLD, 18 ) );
        lstAssignedEmployees.setSelectionModel( dlsmListModel );
        lstAssignedEmployees.setFont( new Font( "Arial", Font.PLAIN, 14 ) );
        lstAssignedEmployees.setFixedCellHeight( 20 );
        lstAssignedEmployees.setFixedCellWidth( 250 );
        lstAssignedEmployees.setSize( 100, 100 );
        lstAssignedEmployees.setToolTipText( "lstAssignedEmployees" );
        dlsmListModel.addListSelectionListener( new pnlProjectEmployeeList.AssignedEmployeeListSelectionHandler() );

        lstAllEmployees.setFont( new Font( "Arial", Font.PLAIN, 14 ) );
        //lstAllEmployees.setSelectionModel( dlsmListModel );
        lstAllEmployees.setFixedCellHeight( 20 );
        lstAllEmployees.setFixedCellWidth( 250 );
        lstAllEmployees.setSize( 100, 100 );
        this.initGUI();
        this.setToolTipText ( pnlProjectEmployeeList.class.toString() );
    }

    /**
     * Initialize this internal panel's GUI.<br>
     * The panel will be assigned into a CardLayout, so a title isn't needed.<br>
     */
    private void initGUI()
    {
        this.setLayout( lyt );
        this.setBackground( Color.lightGray );
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 4;
        con.gridheight = 1;
        this.add( lblSelectEmployee, con );
        con.gridx = 0;
        con.gridy = 1;

        this.add( new JScrollPane( lstAssignedEmployees ), con );
        setActionsForFullList( lstAllEmployees );


        this.initButtons();

        con.gridx = 1;
        con.gridy = 2;
        con.gridwidth = 1;
        this.add(jbtnAddEmployee, con );
        con.gridx = 2;
        con.gridy = 2;
        this.add( jbRemoveEmployee, con );
        con.gridx = 3;
        con.gridy = 2;
        this.add ( jbViewEmployee, con );
        con.gridx = 0;
        con.gridy = 3;
        con.gridwidth = 4;
        this.add ( new JLabel( "Available Employees") , con );
        con.gridx = 0;
        con.gridy = 4;
        con.gridheight = 1;
        this.add( lstAllEmployees , con );

    }

    /**
     * Updates this screen's lstAssignedEmployees of Project Employees.<br>
     *
     * @param iScreen
     * @param iType         None(0), Save(1), Update(2), Delete(3), Title(4)
     * @param objDataSource
     */
    public void change ( int iScreen, int iType, Object objDataSource )
    {
        if ( iType == DataChangeListener.UPDATE )
        {
            if ( iScreen == ScreenChangeListener.SHOW_PROJECT_EMPLOYEES_LIST )
            {
                ProjectDataSource pds = new ProjectDataSource();

                if ( objDataSource instanceof ProjectDataSource )
                    pds = ( ProjectDataSource ) objDataSource;

                this.lstAssignedEmployees.setListData( pds.getProjectEmployees() );
               //this.lstAllEmployees.setListData( new Object [] { "hammer"});

        lblSelectEmployee.setText(  pds.getProjectName() + " Employees");
            }
        }
    }


    /**
     *
     */
    class AssignedEmployeeListSelectionHandler
            implements ListSelectionListener
    {
        public void valueChanged ( ListSelectionEvent e )
        {
            if ( true )
                return;
            
            EmployeeDataSource eds = null;
            if ( lstAssignedEmployees.getSelectedValue() instanceof EmployeeDataSource )
                eds = ( EmployeeDataSource ) lstAssignedEmployees.getSelectedValue();

            ScreenChangeEvent sce = new ScreenChangeEvent( lstAssignedEmployees );
            sce.setData( lstAssignedEmployees.getSelectedValue() );

            if ( eds != null )
            {
                // Navigate the Screen
                EADesktop.getEventManager().fireEvent( ScreenChangeListener.class,
                        ScreenChangeListener.SHOW_PROJECT_EMPLOYEE,
                        new ScreenChangeEvent( eds ) );
                // If a selection was made, pass some data to the new Form
                EADesktop.getEventManager().fireEvent( DataChangeListener.class,
                        DataChangeListener.UPDATE,
                        new DataChangeEvent( eds, ScreenChangeListener.SHOW_PROJECT_EMPLOYEE ) );
            }

            // Always clear the selection before continuing, otherwise the select gets stuck
            dlsmListModel.clearSelection();
        }
    }

    /**
     * Over write the default selection handler for class <code>lstEmployees</code>.<br>
     * We only need it to select the Employee name without taking any automatic actions.<br>
     * We are using JButton <code>jbtnAddEmployee</code> to handle the actions.
     * @param l
     */
    private void setActionsForFullList( lstEmployees l )
    {
        l.changeSelectionBehavior( ds );
    }


    /**
     * Initialize the Employee Add Button and the Employee Remove Button.<br>
     */
    public void initButtons()
    {
        jbtnAddEmployee.addActionListener( new ActionListener()
        {
            /**
             * Add 1 or more employees to the Project's list and the Project Datasource.
             * @param ae
             */
            public void actionPerformed( ActionEvent ae )
            {
                // Sets the data in the JList
                Object [] objArrayNewlySelectedEmployees = lstAllEmployees.getSelectedValues();
                ListModel lmAssignedEmployees = lstAssignedEmployees.getModel();
                int lmSize = lmAssignedEmployees.getSize();
                ArrayList alNewList = new ArrayList();

                for ( int i = 0; i < lmSize; i++ )
                {
                    alNewList.add( lmAssignedEmployees.getElementAt( i ) );
                }

                devDataHelper ddh = devDataHelper.getDataHelper();
                DataModel dm = ddh.getDataModel();
                ProjectDataSource pds = dm.getCurrentProject();

                int oSize = objArrayNewlySelectedEmployees.length;
                for ( int i = 0; i < oSize; i++ )
                {
                    if ( !alNewList.contains( objArrayNewlySelectedEmployees[i] ) )
                    {
                        alNewList.add( objArrayNewlySelectedEmployees[i] );
                        // TODO save this data to the XML file
                        EmployeeDataSource eds = (EmployeeDataSource) objArrayNewlySelectedEmployees[i];
                        if ( eds.hasAvailableProject() )
                            eds.setNextAvailableProject( pds.getProjectID() );
                        pds.addEmployee( eds );
                    }
                }

                lstAssignedEmployees.setListData( alNewList.toArray() );

                // Add data to the datasource here...

            }

        });

        this.jbRemoveEmployee.addActionListener( new ActionListener()
        {
            /**
             * Remove 1 or more employees from this Project.<br>
             * Removes from the list and from the datasource.<br>
             * @param ae
             */
            public void actionPerformed( ActionEvent ae )
            {
                // Get the ListModel before removing the selected employee(s)
                ListModel lm = lstAssignedEmployees.getModel();

                if ( lm.getSize() == 0 )
                    return;

                ArrayList alTemp = new ArrayList( lm.getSize() );

                // Get the list of employees to be removed
                Object [] objRemovableEmployees = lstAssignedEmployees.getSelectedValues();

                // Copy all of the original data to alTemp
                for ( int i = 0 ; i < lm.getSize(); i++ )
                {
                    alTemp.add( lm.getElementAt( i ) );    
                }

                // Remove the Employees to be deleted alTemp
                for ( int i = 0; i < objRemovableEmployees.length; i++ )
                {
                    alTemp.remove( objRemovableEmployees[i] );
                }

                // The employee list is now complete
                lstAssignedEmployees.setListData( alTemp.toArray() );

                devDataHelper ddh = devDataHelper.getDataHelper();
                DataModel dm = ddh.getDataModel();
                ProjectDataSource pds = dm.getCurrentProject();

                // Get the size of the completed list
                int oSize = alTemp.size();

                // Iterate through the list of employees to be removed and remove them from
                // the datasource as well
                for (  int i = 0; i < objRemovableEmployees.length; i++ )
                {
                    EmployeeDataSource eds = (EmployeeDataSource) objRemovableEmployees[i];
                    pds.deleteEmployee( eds );
                }
                

            }
        });
    }


     /**
     * This class provides list selection handling specific
     * to the class <code>lstEmployees</code>.<br>
     *
     * @see lstEmployees
     */
    class DinkySelector
            implements ListSelectionListener
    {
        /**
         * Value Changed
         *
         * @param e
         */
        public void valueChanged ( ListSelectionEvent e )
        {
            System.out.println( "Dinky Selector: valueChanged()" );
        }
    }
}
