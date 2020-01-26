package gui.screens;

import com.developer.data.devDataHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import data.DataModel;
import data.ProjectDataSource;
import data.MilestoneDataSource;

/**
 * This is the Pop Up dialog box that is shown to the user when he double-clicks
 * the Node on the Milestone Graph.<br>
 *
 * @see MilestoneDataSource
 */
public class pnlMilestonePopUp
        extends JFrame
{
    private MilestoneDataSource myMilestone;
    private GridBagLayout lyt = new GridBagLayout();
    private GridBagConstraints con = new GridBagConstraints();
    JButton btnOK = new JButton( "OK" );
    JButton btnEdit = new JButton( "Edit" );
    private JLabel lblName = new JLabel( "Name:" );

    public pnlMilestonePopUp ()
    {
        super();
        myMilestone = new MilestoneDataSource();
        initGUI();
        setupButtonActions();
        setupActionMap();
    }

    public pnlMilestonePopUp ( MilestoneDataSource mds )
    {
        super();
        myMilestone = mds;
        initGUI();
        setupButtonActions();
        setupActionMap();
    }

    public void setLabel ( String s )
    {
        lblName.setText( s );
    }

    /**
     * Initialize this Milestone's Pop Up GUI.<br>
     */
    public void initGUI ()
    {
        this.setBounds( new Rectangle( 200, 100 ) );
        this.setLayout( lyt );
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 2;
        lblName.setText( myMilestone.getName() );
        this.add( lblName, con );

        con.gridx = 0;
        con.gridy = 1;
        con.gridwidth = 1;
        this.add( btnEdit, con );

        con.gridx = 1;
        con.gridy = 1;
        con.gridwidth = 1;
        this.add( btnOK, con );
    }

    /**
     * Adds action handling functionality to this Pop Up dialog's buttons.<br>
     */
    public void setupButtonActions ()
    {

        btnOK.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                Container c = btnOK.getRootPane().getParent();
                c.setVisible( false );
                ( ( JFrame ) c ).dispose();
            }
        } );

        btnEdit.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                Container c = btnOK.getRootPane().getParent();
                c.setVisible( false );
                ( ( JFrame ) c ).dispose();

                devDataHelper ddh = devDataHelper.getDataHelper();
                DataModel dm = ddh.getDataModel();
                ProjectDataSource pds = dm.getCurrentProject();

                pnlEditMilestone pem = new pnlEditMilestone( myMilestone );
                pem.addKeyListener( pem );
                pem.setMyFocus();
                pem.setVisible( true );
            }
        } );
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
                        Container c = btnOK.getRootPane().getParent();
                        c.setVisible( false );
                        ( ( JFrame ) c ).dispose();
                    }
                };

        this.getRootPane().getInputMap().put( KeyStroke.getKeyStroke( "ESCAPE" ), "pressed" );
        this.getRootPane().getActionMap().put( "pressed", actCloseMe );
    }
}
