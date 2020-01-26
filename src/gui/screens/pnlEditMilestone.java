package gui.screens;

import events.DataChangeListener;
import events.ScreenChangeListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import gui.EADesktop;
import gui.lists.lstProjectEmployees;
import gui.lists.lstMilestoneEmployees;
import data.MilestoneDataSource;
import data.DataModel;
import data.ProjectDataSource;
import data.EmployeeDataSource;
import com.developer.data.devDataHelper;
import io.XMLTag;

/**
 * This window is called 'Milestone Info'.<br>
 * It's invoked via a right-click on a milestone displayed on the Milestone Graph.<br>
 * This class provides a JFrame for displaying GUI components used for editing a Milestone.<br>
 * Also serves as the base class for <code>pnlNewMilestone</code>.<br>
 *
 * @see pnlNewMilestone
 * @see MilestoneDataSource
 */
public class pnlEditMilestone
        extends JFrame
        implements DataChangeListener, KeyListener
{

    JButton btnOK = new JButton("OK");
    JButton btnCancel = new JButton("Cancel");
    JButton btnAdd = new JButton( "Add" );
    JButton btnRemove = new JButton( "Remove" );
    private JLabel lblName = new JLabel("Name:");
    private JLabel lblDescription = new JLabel("Description:");
    JTextField name = new JTextField(20);
    JTextArea description = new JTextArea(5, 20);
    JPanel pnlRadioPanel = new JPanel();
    JPanel pnlTest = new JPanel();
    ButtonGroup bgStatus = new ButtonGroup();
    JRadioButton rbNotStarted = new JRadioButton("Not Started");
    JRadioButton rbInProgress = new JRadioButton("In Progress");
    JRadioButton rbCompleted = new JRadioButton("Completed");
    JRadioButton rbLate = new JRadioButton("Late");
    private GridBagLayout lyt = new GridBagLayout();
    private GridBagConstraints con = new GridBagConstraints();
    private MilestoneDataSource myMilestone;
    private pnlEditMilestone pem;
    /**
     * List of employees assigned to this specific Milestone.
     */
    private lstMilestoneEmployees lstAssignedEmployees = new lstMilestoneEmployees(  );
    /**
     * List of all employees whose time is available for this project.
     */
    private lstProjectEmployees lstProjectEmployees = new lstProjectEmployees();


    /**
     * Constructs a JFrame for displaying GUI components used for editing a Milestone.<br>
     *
     * @see pnlNewMilestone
     */
    public pnlEditMilestone()
    {
        super();
        myMilestone = new MilestoneDataSource();
        init();
    }

    /**
     * Constructs a JFrame for displaying GUI components used for editing a Milestone.<br>
     *
     * @param mds The Milestone used to populate the data displayed by the GUI components.<br>
     * @see pnlNewMilestone
     */
    public pnlEditMilestone(MilestoneDataSource mds)
    {
        super();
        myMilestone = mds;
        init();
    }

    /**
     *
     * @return
     */
    private EmployeeDataSource [] getEmployees()
    {
       return myMilestone.getEmployees();
    }

    /**
     *
     */
    private void init()
    {
        EADesktop.getEventManager().removeListener(DataChangeListener.class, this);
        EADesktop.getEventManager().addListener(DataChangeListener.class, this);
        initGUI();
        initButtons();
        devDataHelper ddh = devDataHelper.getDataHelper();
        DataModel dm = ddh.getDataModel();
        ProjectDataSource pds = dm.getCurrentProject();
        lstAssignedEmployees.setListData( this.getEmployees() );
        lstProjectEmployees.setListData( pds.getProjectEmployees() );

        setupButtonActions();
        setStatusButtons();
        this.addKeyListener( this );
        this.setupActionMap();

        pem = this;
    }

    /**
     * Update this panel with the correct data.
     *
     * @param iScreen the ID of this screen
     * @param iType   the type of data change event
     * @param o       the object to update with (DataModel)
     */
    public void change(int iScreen, int iType, Object o)
    {
        if (iScreen == ScreenChangeListener.SHOW_EDIT_MILESTONE)
        {

        }

    }



    /**
     *
     */
    private void initButtons()
    {

        btnAdd.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                Object objSelected = lstProjectEmployees.getSelectedValue();
                if ( objSelected == null )
                    return;

                EmployeeDataSource eds = ( EmployeeDataSource ) objSelected;
                if ( myMilestone.getNumberOfEmployees() == 4 )
                {
                    // TODO : Display some message indicating max employees reached
                }
                else
                if ( ! myMilestone.isEmployeeAssigned( eds.id ) )
                    myMilestone.addEmployee( eds.id );

                ListModel lmAssignedEmployees =  lstAssignedEmployees.getModel();
                int lmSize = lmAssignedEmployees.getSize();
                ArrayList alNewList = new ArrayList();

                for ( int i = 0; i < lmSize; i++ )
                {
                    alNewList.add( lmAssignedEmployees.getElementAt( i ) );
                }

                if ( !alNewList.contains( objSelected ) )
                {
                    alNewList.add( objSelected );
                }

                 lstAssignedEmployees.setListData( alNewList.toArray() );
            }
        });

        btnRemove.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                Object objSelected = lstAssignedEmployees.getSelectedValue();
                if ( objSelected == null )
                    return;
                EmployeeDataSource eds = ( EmployeeDataSource ) objSelected;
                myMilestone.removeEmployee( eds.id );
                ListModel lmAssignedEmployees =  lstAssignedEmployees.getModel();
                int lmSize = lmAssignedEmployees.getSize();
                ArrayList alNewList = new ArrayList();

                for ( int i = 0; i < lmSize; i++ )
                {
                    alNewList.add( lmAssignedEmployees.getElementAt( i ) );
                }

                if ( alNewList.contains( objSelected ) )
                {
                    alNewList.remove( objSelected );
                }

                 lstAssignedEmployees.setListData( alNewList.toArray() );
            }
        });
    }

    /**
     *
     */
    public void initGUI()
    {
        this.setTitle("Milestone Info");
        this.setLocation(300, 300);
        this.setBounds(new Rectangle(600, 500));
        this.setLocationRelativeTo(null);
        this.setLayout(lyt);

        lstProjectEmployees.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        lstAssignedEmployees.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

        con.fill = GridBagConstraints.NORTHWEST;
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 2;
        //  * * * * * * * *  B O R D E R  * * * * * * *
        JPanel pnlName = new JPanel();
        pnlName.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Name") );
        pnlName.add( name )  ;
        name.setMinimumSize(new Dimension(10, 15));
        name.setBorder(BorderFactory.createLoweredBevelBorder());
        name.setText(myMilestone.getName());

        this.add( pnlName, con );

        JPanel pnlDescription = new JPanel();
        pnlDescription.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Description") );

        con.gridx = 0;
        con.gridy = 1;
        con.gridwidth = 2;
        description.setBorder(BorderFactory.createLoweredBevelBorder());
        description.setText(myMilestone.getDescription());
        description.setSize( 50, 10 );
        description.setLineWrap ( true );
        description.setWrapStyleWord ( true );

        pnlDescription.setSize( 55, 15 );
        pnlDescription.add( description );
        this.add( pnlDescription , con );

        con.fill = GridBagConstraints.SOUTH;
        con.gridx = 0;
        con.gridy = 8;
        con.gridwidth = 1;

        JPanel pnlButtons = new JPanel();
        pnlButtons.add( btnCancel );
        // this.add(btnCancel, con);
        pnlButtons.add( btnOK );
        this.add( pnlButtons, con );

        con.gridx = 1;
        con.gridy = 8;
        con.gridwidth = 1;
        // this.add(btnOK, con);

        // - - - - - - - - - - - - - - - - - - -
        //  B U T T O N    G R O U P
        bgStatus.add(rbNotStarted);
        bgStatus.add(rbInProgress);
        bgStatus.add(rbCompleted);
        bgStatus.add(rbLate);

        //  * * * * * * * *  B O R D E R  * * * * * * *
        pnlRadioPanel.setBorder( BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Progress") ) ;

        con.fill = GridBagConstraints.WEST;
        con.gridx = 0;
        con.gridy = 2;
        con.gridwidth = 1;
        con.gridheight = 3;

        pnlRadioPanel.setLayout(new GridLayout(0, 1));
        pnlRadioPanel.add(rbNotStarted);
        pnlRadioPanel.add(rbInProgress);
        pnlRadioPanel.add(rbCompleted);
        pnlRadioPanel.add(rbLate);
        this.add(pnlRadioPanel, con);


        //  * * * * * * * *  B O R D E R  * * * * * * *
        pnlTest.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Employees" ));
        pnlTest.setPreferredSize( new Dimension( 200, 200 ));


        lstAssignedEmployees.setToolTipText( "Employees Assigned to this Milestone");
        lstAssignedEmployees.setSize( new Dimension ( 100, 100 ) );
        JScrollPane jspAss = new JScrollPane( lstAssignedEmployees );    lstAssignedEmployees.getModel().getSize();
        JScrollPane jspAll = new JScrollPane( lstProjectEmployees );     lstProjectEmployees.getModel().getSize();

        GridBagLayout gbl = new GridBagLayout();
        pnlTest.setLayout( gbl );
        GridBagConstraints _con = new GridBagConstraints();
        _con.gridx = 0;
        _con.gridy = 0;
        _con.gridheight = 2;
        _con.gridwidth = 3;
        _con.weightx = 2;
        _con.weighty = 2;
        _con.fill = GridBagConstraints.BOTH;
        pnlTest.add(  jspAss , _con );


        _con.gridx = 0;
        _con.gridy = 2;
        _con.gridheight = 1;
        _con.gridwidth = 1;
        _con.weightx = .05;
        _con.weighty = .05;
        pnlTest.add( btnAdd, _con );

        _con.gridx = 1;
        _con.gridy = 2;
        _con.gridheight = 1;
        _con.gridwidth = 1;
        _con.weightx = .05;
        _con.weighty = .05;
        pnlTest.add( btnRemove, _con );

        _con.gridx = 0;
        _con.gridy = 3;
        _con.gridheight = 2;
        _con.gridwidth = 3;
        _con.weightx = 2;
        _con.weighty = 2;
        pnlTest.add( jspAll, _con );

        con.fill = GridBagConstraints.BOTH;
        con.gridx = 2;
        con.gridy = 0;
        con.gridwidth = 3;
        con.gridheight = 5;

        this.add( pnlTest, con );

       // - - - - - - - - - - - - - - - - - - - -

    }

    /**
     *
     */
    public void setMyFocus()
    {
        name.requestFocusInWindow();
        if ( name != null )
            if ( name.getText() != null )
                name.setCaretPosition( name.getText().length() );
    }

    /**
     *
     */
    private void setStatusButtons()
    {
        switch (myMilestone.getStatus())
        {
            case 1200:
                rbNotStarted.setSelected(true);
                break;
            case 1201:
                rbInProgress.setSelected(true);
                break;
            case 1202:
                rbCompleted.setSelected(true);
                break;
            case 1203:
                rbLate.setSelected(true);
                break;
        }
    }

    /**
     *
     */
    public void setupButtonActions()
    {
        btnOK.setMnemonic(KeyEvent.VK_O);
        btnCancel.setMnemonic(KeyEvent.VK_C);

        btnOK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                myMilestone.setName(name.getText());
                myMilestone.setDescription(description.getText());
                int iSelected = 0;
                if (rbNotStarted.isSelected())
                    iSelected = 1200;
                if (rbInProgress.isSelected())
                    iSelected = 1201;
                if (rbCompleted.isSelected())
                    iSelected = 1202;
                if (rbLate.isSelected())
                    iSelected = 1203;

                myMilestone.setStatus(iSelected);
                myMilestone.setAttribute ( XMLTag.HH_ATTR_MODIFIED ); 
                Container c = btnOK.getRootPane().getParent();
                c.setVisible(false);
                ((JFrame) c).dispose();
            }
        });

        btnCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                Container c = btnCancel.getRootPane().getParent();
                c.setVisible(false);
                ((JFrame) c).dispose();
            }
        });
    }


    public void keyPressed(KeyEvent e)
    {

    }

    public void keyReleased(KeyEvent e)
    {

    }

    /**
     * Handle the user's escape keyboard command to close this window.
     * @param e
     */
    public void keyTyped(KeyEvent e)
    {
           if ( e.getKeyCode()  == 0 )
           {

           }
    }

     /**
     * Set the Action Map to respond to the <b>escape</b> key.<br>
     */
    private void setupActionMap()
    {
        Action actCloseMe =
                new AbstractAction()
                {
                    public void actionPerformed( ActionEvent e )
                    {
                        Container c = pem;
                        c.setVisible( false );
                        ( ( JFrame ) c ).dispose();

                        System.out.println("Escape Pressed");
                    }
                };

        this.getRootPane().getInputMap().put( KeyStroke.getKeyStroke( "ESCAPE" ), "pressed" );
        this.getRootPane().getActionMap().put( "pressed", actCloseMe );
    }

}
