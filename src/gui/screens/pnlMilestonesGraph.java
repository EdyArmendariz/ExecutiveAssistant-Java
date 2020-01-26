package gui.screens;

import events.LoadEventListener;
import events.MilestoneGrabberEvent;
import gui.movies.myCanvas;
import gui.screens.pnlNewMilestone;
import gui.EADesktop;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.net.MalformedURLException;

import data.MilestoneDataSource;


/**
 * The JFrame that contains the panel that renders the Graph, Milestones and Connector Lines.<br>
 * New Version
 */
public class pnlMilestonesGraph
        extends JFrame
        implements LoadEventListener, WindowListener, MouseListener
{
    private JLabel lblMilestonesGraph = new JLabel( "pnlMilestonesGraph" );
    private myCanvas can = new myCanvas();
    public static pnlMilestonesGraph pmg;
    private JMenuBar mnuMainBar = new JMenuBar();
    private JToolBar jtbToolBar = new JToolBar ( "My Toolbar" );
    private JMenu mnuEdit = new JMenu( "Edit" );
    private JMenuItem mniNewMilestone = new JMenuItem( "New Milestone" );
    private JMenuItem mniDeleteMilestone = new JMenuItem( "Delete Milestone" );
    private JMenuItem mniStraightenMilestones = new JMenuItem ( "Straighten Milestones" );
    private JButton jbInvokingButton = null;


    /**
     * Constructs the JFrame for the Milestones Graph.<br>
     * Menus are initialized and Milestones are rendered.<br>
     */
    public pnlMilestonesGraph ()
    {
        super();
        this.setName( "I Am pnlMilestonesGraph");
        this.setTitle( "Milestones Graph" );
        this.setLayout( new BorderLayout ( ) );
        EADesktop.getEventManager().removeListener( LoadEventListener.class, this );
        EADesktop.getEventManager().addListener( LoadEventListener.class, this );
        pmg = this;
        lblMilestonesGraph.setFont( new Font( "Arial", 0, 18 ) );
        can.setBorder ( BorderFactory.createLineBorder(Color.black) );
        this.add( can, BorderLayout.CENTER );
        can.setupActionMap();
        this.setLocation( 4, 2 );
        this.setMinimumSize( new Dimension( 100, 200 ) );
        this.setPreferredSize( new Dimension( 400, 600 ) );
        this.setMaximumSize( new Dimension( 600, 800 ) );
        this.pack();
        initMenu();
        this.addWindowListener( this );
        this.addMouseListener( this );
        this.setupActionMap();
    }


    /**
     * Initialize the menu bar for this Milestones Graph.<br>
     */
    private void initMenu ()
    {
        jtbToolBar.setBorder( BorderFactory.createLineBorder(Color.GREEN ));
        this.addButtons( jtbToolBar );
        this.add( jtbToolBar, BorderLayout.NORTH );
        this.setJMenuBar( mnuMainBar );
        mnuMainBar.add( mnuEdit );
        mnuEdit.add( mniNewMilestone );
        mnuEdit.add( mniDeleteMilestone );
        mnuEdit.add ( mniStraightenMilestones );

        mniNewMilestone.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                pnlNewMilestone pnm = new pnlNewMilestone();
                pnm.setVisible( true );
            }
        } );

        mniDeleteMilestone.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                MilestoneGrabberEvent mg = EADesktop.getMilestoneGrabber();
                MilestoneDataSource mds = mg.getSourceMilestone();
                pnlNewMilestone pnm = new pnlNewMilestone();
                pnm.setVisible( true );
            }
        } );

        mniStraightenMilestones.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                MilestoneGrabberEvent mg = EADesktop.getMilestoneGrabber();
                MilestoneDataSource mds = mg.getSourceMilestone();
                can.straightenMilestones();
            }
        } );

    }

    /**
     * Anything rendered here is overwritten by milestones or connector lines.<br><br>
     * Renders this Panel in <b>only two</b> circumstances.<br>
     * Once after <code>setVisible( true )</code> and the other after a
     * <code>WINDOW_DEICONIFIED</code> event.<br>
     *
     * @param g
     */
    public void paint ( Graphics g )
    {
        super.paint( g );
    }

    /**
     * Loads the canvas contained by the Milestones Graph.<br>
     * Multiple invocations will load duplicate Milestones.<br>
     *
     * @see myCanvas
     */
    public void loadMilestones ()
    {
        can.loadMilestones();
    }

    /**
     * A sloppy way to hook the invoking parent's button to this Frame.<br>
     * This method disables the parent's button so that only one pnlMilestonesGraph
     * will be displayed at a time.
     * @param b
     */
    public void attachInvokingButton( JButton b )
    {
        jbInvokingButton = b;
    }

    /**
     *
     * @param toolBar
     */
    protected void addButtons(JToolBar toolBar) {
        JButton button = null;

        //first button
        button = makeNavigationButton("Back24", "",
                                      "Back to previous something-or-other",
                                      "Previous");
        toolBar.add(button);

        //second button
        button = makeNavigationButton("Up24", "",
                                      "Up to something-or-other",
                                      "Up");
        toolBar.add(button);

    }


    /**
     * Allows the Milestone Graph to load fresh data on the fly.<br>
     *
     * @param o
     */
    public void load ( Object o )
    {
        if ( o instanceof data.MilestoneDataSource )
            can.loadMilestone( o );
    }

    /**
     * Allows the Miletone Graph to remove data on the fly.<br>
     * @param o
     */
    public void unload ( Object o )
    {
        if ( o instanceof data.MilestoneDataSource )
            can.removeMilestone( o );
    }


    public void windowClosed( WindowEvent we)
    {}

    public void windowDeiconified( WindowEvent we )
    {}

    /**
     * Executes immediately before this JFrame closes.
     * Re-enables the invoking parent's button so that another pnlMilestonesGraph
     * instance can be displayed <b><i>after</i></b> this JFrame is closed.<br>
     * Currently, multiple pnlMilestonesGraph should not be displayed at the same time.
     * @param we The window event that invokes this method.
     * @see pnlProjectMenu#initializeButtonActions()
     */
    public void windowClosing( WindowEvent we )
    {
        if ( jbInvokingButton != null )
            jbInvokingButton.setEnabled( true );
    }

    public void windowIconified( WindowEvent we )
    {}
    public void windowDeactivated( WindowEvent we )
    {}
    public void windowOpened( WindowEvent we )
    {}
    public void windowActivated( WindowEvent we )
    {}

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
                        Container c = pmg.getRootPane().getParent();
                        c.setVisible( false );
                        ( ( JFrame ) c ).dispose();

                        if ( jbInvokingButton != null )
                            jbInvokingButton.setEnabled( true );
                        System.out.println("Escape Pressed");
                    }
                };

        this.getRootPane().getInputMap().put( KeyStroke.getKeyStroke( "ESCAPE" ), "pressed" );
        this.getRootPane().getActionMap().put( "pressed", actCloseMe );
    }

    /**
     *
     * @param imageName
     * @param actionCommand
     * @param toolTipText
     * @param altText
     * @return
     */
    protected JButton makeNavigationButton(String imageName,
                                           String actionCommand,
                                           String toolTipText,
                                           String altText)
    {

        //Look for the image.
        String imgLocation = "C:\\MendariDevelopment\\ExecutiveAssistant\\lib\\icons\\"
                             + imageName
                             + ".gif";

        //Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        //button.addActionListener(this);

        if ( imgLocation != null)
        {                      //image found
            button.setIcon(new ImageIcon(imgLocation, altText));
        }
        else
        {                                     //no image found
            button.setText(altText);
            System.err.println("Resource not found: " + imgLocation);
        }

        return button;
    }

    /**
     *
     * @param me
     */
    public void mouseEntered( MouseEvent me )
    {

    }

    /**
     *
     * @param me
     */
    public void mouseExited( MouseEvent me )
    {

    }

    /**
     *
     * @param me
     */
    public void mouseReleased( MouseEvent me )
    {

    }

    /**
     *
     * @param me
     */
    public void mouseClicked( MouseEvent me )
    {
        System.out.println( "Milestone Graph Clicked");
    }


    /**
     *
     * @param me
     */
    public void mousePressed( MouseEvent me )
    {

    }
}

