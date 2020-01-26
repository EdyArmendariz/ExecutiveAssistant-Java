package gui.screens;

import events.ScreenChangeListener;
import events.ScreenChangeEvent;
import events.DataChangeListener;
import events.DataChangeEvent;

import javax.swing.*;

import data.EmployeeDataSource;
import gui.EADesktop;

/**
 * A screen that displays an employee's information specific to his project.<br>
 */
public class pnlProjectEmployee
        extends JPanel
        implements DataChangeListener
{
    private JLabel lblFirstName = new JLabel( "first name" );
    private JLabel lblLastName = new JLabel( "last name" );

    /**
     *
     */
    public pnlProjectEmployee ()
    {
        super();

        EADesktop.getEventManager().removeListener( DataChangeListener.class, this );
        EADesktop.getEventManager().addListener( DataChangeListener.class, this );

        this.add( lblFirstName );
        this.add( lblLastName );
        this.setToolTipText ( pnlProjectEmployee.class.toString() );
    }

    /**
     * Allows objects without an instance variable to update/modify the text (names, numbers, etc.)
     * displayed on this screen.<br>
     *
     * @param iScreen
     * @param iType
     * @param o       We are expecting an <code>EmployeeDataSource</code> object.
     */
    public void change ( int iScreen, int iType, Object o )
    {
        if ( iScreen == ScreenChangeListener.SHOW_PROJECT_EMPLOYEE )
        {
            if ( iType == DataChangeListener.UPDATE )
            {
                if ( o instanceof EmployeeDataSource )
                {
                    EmployeeDataSource eds = ( EmployeeDataSource ) o;
                    lblFirstName.setText( eds.firstname );
                    lblLastName.setText( eds.lastname );
                }
            }
        }

    }

}
