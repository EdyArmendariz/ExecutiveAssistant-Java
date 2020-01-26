package gui.screens;

import com.developer.data.devDataHelper;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

import data.DataModel;
import data.EmployeeDataSource;
import data.ProjectDataSource;
import gui.EADesktop;
import events.DataChangeListener;
import events.DataChangeEvent;
import events.ScreenChangeListener;

/**
 * A JPanel that displays the GUI for adding a new employee.<br>
 */
public class pnlNewEmployee
        extends pnlEditEmployee
{
    ProjectDataSource myProjectDataSource = new ProjectDataSource ();

    /**
     * Constructor for the New Employee Panel.<br>
     */
    public pnlNewEmployee ()
    {
        super();
        this.setTitle( "New Employee" );
        this.hideProjectPulldown();
        btnOK.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                if ( fname.getText().equals("") )
                {
                    fname.setToolTipText( "This field cannot be blank.");
                    btnOK.setToolTipText( "The employee must have a name.");
                    return;
                }

                devDataHelper ddh = devDataHelper.getDataHelper();
                DataModel dm = ddh.getDataModel();

                EmployeeDataSource eds = new EmployeeDataSource();
                eds.id = dm.getNextEmployeeID_Java();
                eds.setProjectID0( myProjectDataSource.getProjectID() );
                eds.setFname( fname.getText() );
                eds.setLname( lname.getText() );

                dm.addEmployee( eds );

                // Update the Employee Lists
                EADesktop.getEventManager().fireEvent( DataChangeListener.class,
                        DataChangeListener.UPDATE,
                        new DataChangeEvent( dm, ScreenChangeListener.SHOW_PROJECT_EMPLOYEES_LIST ) );
                 EADesktop.getEventManager().fireEvent( DataChangeListener.class,
                        DataChangeListener.UPDATE,
                        new DataChangeEvent( dm, ScreenChangeListener.SHOW_EMPLOYEE_LIST ) );
                Container c = btnOK.getRootPane().getParent();
                c.setVisible( false );
                ( ( JFrame ) c ).dispose();

            }
        } );

        btnCancel.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                Container c = btnCancel.getRootPane().getParent();
                c.setVisible( false );
                ( ( JFrame ) c ).dispose();
            }
        } );

    }

    /**
     *
     * @param pds
     */
    public void setCurrentProject( ProjectDataSource pds )
    {
        myProjectDataSource = pds.clone();
    }

}
