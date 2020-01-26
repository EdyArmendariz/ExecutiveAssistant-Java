package gui.screens;

import events.DataChangeListener;
import events.DataChangeEvent;
import events.ScreenChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import data.EmployeeDataSource;
import data.DataModel;
import gui.cmbProject;
import gui.EADesktop;
import com.developer.data.devDataHelper;

/**
 * Provides a GUI for editing an Employee's information.
 */
public class pnlEditEmployee
        extends JFrame
        implements DataChangeListener
{
    private GridBagLayout lyt = new GridBagLayout();
    private GridBagConstraints con = new GridBagConstraints();
    JButton btnOK = new JButton( "OK" );
    JButton btnCancel = new JButton( "Cancel" );
    private JLabel lblfname = new JLabel( "First Name:" );
    private JLabel lbllname = new JLabel( "Last Name:" );
    JTextField fname = new JTextField( 20 );
    JTextField lname = new JTextField( 30 );
    cmbProject jproject = new cmbProject();
    Box.Filler bf = new Box.Filler( new Dimension( 20, 10 ), new Dimension( 40, 20 ), new Dimension( 50, 30 ) );

    EmployeeDataSource myEmployee;

    /**
     * Default constructor for this <code>pnlEditEmployee</code> object.
     */
    public pnlEditEmployee ()
    {
        super();
        myEmployee = new EmployeeDataSource();
        initGUI();
        this.setupButtonActions();
       
    }

    /**
     * Construct this <code>pnlEditEmployee</code> object initialized with an
     * <code>EmployeeDataSource</code> object
     * @param eds
     */
    public pnlEditEmployee ( EmployeeDataSource eds )
    {
        super();
        myEmployee =  eds;
        initGUI();
        this.setupButtonActions();
    }


    /**
     * Initialize the GUI for this <code>pnlEditEmployee</code>.<br>
     *
     */
    public void initGUI ()
    {
        this.setTitle( "Edit Employee" );
        this.setLocation( 300, 300 );
        this.setBounds( new Rectangle( 300, 300 ) );
        this.setLocationRelativeTo( null );
        this.setLayout( lyt );

        con.fill = GridBagConstraints.HORIZONTAL;

        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 1;
        this.add( lblfname, con );

        con.gridx = 1;
        con.gridy = 0;
        con.gridwidth = 2;
        fname.setMinimumSize( new Dimension( 60, 15 ) );
        fname.setBorder( BorderFactory.createLoweredBevelBorder() );
        fname.setText( myEmployee.firstname );
        this.add( fname, con );

        con.gridx = 0;
        con.gridy = 1;
        con.gridwidth = 2;
        this.add( bf, con );

        bf = new Box.Filler( new Dimension( 20, 10 ), new Dimension( 40, 20 ), new Dimension( 50, 30 ) );
        con.gridx = 1;
        con.gridy = 1;
        con.gridwidth = 2;
        this.add( bf, con );

        con.gridx = 0;
        con.gridy = 2;
        con.gridwidth = 1;
        this.add( lbllname, con );

        con.gridx = 1;
        con.gridy = 2;
        con.gridwidth = 2;
        lname.setBorder( BorderFactory.createLoweredBevelBorder() );
        lname.setText( myEmployee.lastname );
        this.add( lname, con );

        bf = new Box.Filler( new Dimension( 20, 10 ), new Dimension( 40, 20 ), new Dimension( 50, 30 ) );
        con.gridx = 1;
        con.gridy = 3;
        con.gridwidth = 2;
        this.add( bf, con );

        con.gridx = 1;
        con.gridy = 4;
        con.gridwidth = 2;
        this.add( jproject, con );


        bf = new Box.Filler( new Dimension( 20, 80 ), new Dimension( 40, 120 ), new Dimension( 50, 200 ) );
        con.gridx = 1;
        con.gridy = 5;
        con.gridheight = 4;
        con.gridwidth = 2;
        this.add( bf, con );

        con.gridx = 1;
        con.gridy = 10;
        con.gridwidth = 1;
        this.add( btnCancel, con );

        con.gridx = 2;
        con.gridy = 10;
        con.gridwidth = 1;
        this.add( btnOK, con );

    }

    /**
     * Hides the pulldown menu for selecting a project name.<br>
     * When invoking <code>pnlNewEmployee</code> we should use the currently open project.<br>
     */
    public void hideProjectPulldown()
    {
        this.jproject.setVisible( false );
    }

    /**
     *
     */
    public void setupButtonActions ()
    {

        btnOK.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                myEmployee.setFname( fname.getText() );
                myEmployee.setLname( lname.getText() );

                // Insert DataChangeEvent code here ...
                devDataHelper ddh = devDataHelper.getDataHelper();
                DataModel dm = ddh.getDataModel();
                EADesktop.getEventManager().fireEvent( DataChangeListener.class,
                                        DataChangeListener.UPDATE,
                                        new DataChangeEvent( myEmployee, ScreenChangeListener.SHOW_EMPLOYEE ) );
                dm.setCurrentEmployee( myEmployee );
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
     * @param a
     * @param b
     * @param o
     */
    public void change ( int a, int b, Object o )
    {

    }
}
