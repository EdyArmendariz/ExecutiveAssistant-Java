package gui.movies;

import gui.EADesktop;
import gui.menuitems.pmnuAddRemoveSiblings;
import gui.screens.pnlMilestonePopUp;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import data.MilestoneDataSource;
import events.RenderListener;
import events.RenderEvent;
import events.MilestoneGrabberEvent;

import javax.swing.*;


/**
 * Provides rendering of a Milestone on the Graph.<br>
 *
 * @see gui.screens.pnlMilestonesGraph
 */
public class cnvMilestone
        extends JPanel
        implements MouseListener, MouseMotionListener
{
    private boolean PRINT_MOUSE_EVENTS = false;
    private Point ptClick = new Point( 0, 0 );
    private MilestoneDataSource myMilestone = new MilestoneDataSource();
    private Graphics myGraphicsContext;
    private boolean bDisplayName = false;

    /**
     * Constructs a renderable Milestone using an object reference to <code>mds</code>.
     *
     * @param mds a <code>MilestoneDataSource</code> used as an object reference, not a clone.<br>
     * @see gui.screens.pnlMilestonesGraph
     */
    public cnvMilestone ( MilestoneDataSource mds )
    {
        super();
        this.addMouseListener( this );
        this.addMouseMotionListener( this );
        this.setSize( 60, 30 );
        this.setLocation( mds.getPosition() );
        this.myMilestone = mds;     // attach a reference, not a clone.
    }

    /**
     * A primitive equals that only tests its <code>MilestoneDataSource</code> data member.<br>
     *
     * @param o
     * @return true if they're equal, false otherwise.
     * @see MilestoneDataSource#equals(Object) 
     */
    public boolean equals( Object o )
    {
        boolean bReturn = false;

        if ( ( o == null ) || ( ! ( o instanceof cnvMilestone ) )  )
            return bReturn;
        cnvMilestone that = ( cnvMilestone ) o;

        if ( ! ( this.myMilestone.equals( that.myMilestone ) ) )
            return bReturn;


        return bReturn = true;
    }

    /**
     * @param me
     */
    public void mouseEntered ( MouseEvent me )
    {
        if ( PRINT_MOUSE_EVENTS )
            System.out.println( "<cnvMilestone Mouse Entered>" );

         bDisplayName = true;
         this.repaint();
    }

    /**
     * Executes when the mouse cursor exits the drawn bounds of this cnvMilestone.<br>
     * @param me
     */
    public void mouseExited ( MouseEvent me )
    {
        if ( PRINT_MOUSE_EVENTS )
            System.out.println( "<cnvMilestone Mouse Exited>" );

       bDisplayName = false;
       this.repaint();
    }

    /**
     * Handles the event of a user clicking on the rendered Milestone.<br>
     * Currently handles double-click events only.<br>
     *
     * @param me the MouseEvent to be handled.
     */
    public void mouseClicked ( MouseEvent me )
    {
        MilestoneGrabberEvent mge = EADesktop.getMilestoneGrabber();

        if ( PRINT_MOUSE_EVENTS )
            System.out.println( "<cnvMilestone Mouse Clicked>" );
        // Handle the left Button (primary)

        if ( me.getButton() == MouseEvent.BUTTON1 )
        {
            // you are selected as a sibling by Milestone Grabber
            if ( mge.isWaiting() )
            {
                mge.setSibling( this.myMilestone );
                mge.setWaiting( false );
                MilestoneDataSource mdsSource = mge.getSourceMilestone();
                this.myMilestone.setSiblingAtLocation( mge.getOppositeConnector(), mdsSource );
                this.myMilestone.setSiblingUniqueID( mge.getOppositeConnector(), mdsSource.getID() );

                //EADesktop.getEventManager().fireEvent(
                //        RenderListener.class,
                //        RenderListener.STUFF,
                 //       new RenderEvent( new Object() ) );
            }
        }

        if ( me.getButton() == MouseEvent.BUTTON1 )
        {
            if ( me.getClickCount() == 2 )
            {
                pnlMilestonePopUp pmpu = new pnlMilestonePopUp( myMilestone );
                pmpu.setLocation( ( int ) this.getLocationOnScreen().getX(), ( int ) this.getLocationOnScreen().getY() - 100 );
                pmpu.setVisible( true );
            }
        }

        // Handle the right button ( right-click )
        else
            if ( me.getButton() == MouseEvent.BUTTON3 )
            {
                pmnuAddRemoveSiblings jpm = new pmnuAddRemoveSiblings( myMilestone );
                jpm.show( this.getParent(), ( int ) this.getX() + 60, ( int ) this.getY() );
            }
            else
                if ( me.getButton() == MouseEvent.BUTTON2 )
                {
                    // Handle the center button ( scroll )
                }

    }

    /**
     * @param me the MouseEvent to be handled.
     */
    public void mousePressed ( MouseEvent me )
    {
        if ( PRINT_MOUSE_EVENTS )
            System.out.println( "<cnvMilestone Mouse Pressed>" );
        ptClick.setLocation( me.getX(), me.getY() );
    }

    /**
     *
     * @param me
     */
    public void mouseReleased ( MouseEvent me )
    {
        if ( PRINT_MOUSE_EVENTS )
            System.out.println( "<cnvMilestone Mouse Released>" );

        Point ptPoint = new Point();
        // if the mouse is released within the painting region
        if ( this.contains( me.getPoint() ) )
        {
            ptPoint.x = this.getX() + me.getX() - ( int ) ptClick.getX();
            ptPoint.y = this.getY() + me.getY() - ( int ) ptClick.getY();

            this.setLocation( this.getX() + me.getX() - ( int ) ptClick.getX(),
                    this.getY() + me.getY() - ( int ) ptClick.getY() );
        }
        // the mouse cursor was outside the painted region
        // and hence, the User moved the cnvMilestone's position.
        else
        {
            ptPoint.x = me.getX() + this.getX() - ( int ) ptClick.getX();
            ptPoint.y = me.getY() + this.getY() - ( int ) ptClick.getY();
            this.setLocation( me.getX() + this.getX() - ( int ) ptClick.getX(),
                    me.getY() + this.getY() - ( int ) ptClick.getY() );
        }

        // update the corresponding MilestoneDataSource's position
        this.myMilestone.setPosition( ptPoint );

        EADesktop.getEventManager().fireEvent(
                RenderListener.class,
                RenderListener.STUFF,
                new RenderEvent( new Object() ) );

    }

    /**
     * @param me
     */
    public void mouseDragged ( MouseEvent me )
    {
        if ( PRINT_MOUSE_EVENTS )
            System.out.println( "<cnvMilestone Mouse Dragged>" );

       // pnlMilestonesGraph.pmg.setTitle( "Mouse Source (" + me.getX() + ", " + me.getY() + ")" );

    }

    /**
     * @param me
     */
    public void mouseMoved ( MouseEvent me )
    {
        if ( PRINT_MOUSE_EVENTS )
            System.out.println( "<cnvMilestone Mouse Moved>" );
    }

    /**
     * @param s
     */
    public void setName ( String s )
    {
        myMilestone.setName( s );
    }

    /**
     * @return
     */
    public String getName ()
    {
        return myMilestone.getName();
    }

    /**
     * @param s
     */
    public void setDescription ( String s )
    {
        myMilestone.setDescription( s );
    }

    /**
     * @return
     */
    public String getDescription ()
    {
        return myMilestone.getDescription();
    }

    /**
     * Paints this milestone onto the Milestone Graph.<br>
     * Any drawing rendered here is done within the confines of Milestone Only.<br>
     *
     * @param g
     */
    public void paint ( Graphics g )
    {
        super.paint( g );
        this.setLocation (  this.myMilestone.getPosition().x,  this.myMilestone.getPosition().y ); // translate( this.myMilestone.getPosition().x,  this.myMilestone.getPosition().y );
        myGraphicsContext = g;
        Dimension d = this.getSize();
        //g.setClip( this.myMilestone.getPosition().x,
        //        this.myMilestone.getPosition().y,( int ) d.getWidth(), ( int ) d.getHeight());
        g.setColor( Color.BLUE );
        g.fillRect( 0, 0, ( int ) d.getWidth(), ( int ) d.getHeight() );
        if ( bDisplayName )
        {
              myGraphicsContext.setColor( Color.WHITE );
              myGraphicsContext.drawString( this.myMilestone.getName(), 0, 11 );
        }
    }
}


