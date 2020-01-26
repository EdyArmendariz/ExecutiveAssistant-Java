package gui.movies;

import gui.EADesktop;
import java.awt.*;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import com.developer.data.devDataHelper;
import data.DataModel;
import data.ProjectDataSource;
import data.MilestoneDataSource;
import events.RenderListener;

import javax.swing.*;

/**
 * This class provides the canvas onto which Milestones and Connector Lines are drawn.<br>
 */
public class myCanvas
        extends JLayeredPane
        implements MouseListener, MouseMotionListener, RenderListener
{
    private boolean PRINT_MOUSE_EVENTS = false;
    MilestoneDataSource tester = new MilestoneDataSource();
    java.util.List<MilestoneDataSource> myMilestones = new java.util.ArrayList();
    private myCanvas thisMyCanvas;

    /**
     * Holds the coordinates of the user's last mousePressed event.
     */
    int last_x, last_y;

    /**
     * Constructs the drawing area for Milestones and their connector lines.<br>
     */
    public myCanvas ()
    {
        super();
        this.setName( "I am a myCanvas");
        EADesktop.getEventManager().removeListener( RenderListener.class, this );
        EADesktop.getEventManager().addListener( RenderListener.class, this );
        this.addMouseListener( this );
        this.addMouseMotionListener( this );
        thisMyCanvas = this;
        this.setBackground(Color.yellow );
    }



    public void drawThickLine(
     Graphics g, int x1, int y1, int x2, int y2, int thickness, Color c) {
     // The thick line is in fact a filled polygon
     g.setColor(c);
     int dX = x2 - x1;
     int dY = y2 - y1;
     // line length
     double lineLength = Math.sqrt(dX * dX + dY * dY);

     double scale = (double)(thickness) / (2 * lineLength);

     // The x,y increments from an endpoint needed to create a rectangle...
     double ddx = -scale * (double)dY;
     double ddy = scale * (double)dX;
     ddx += (ddx > 0) ? 0.5 : -0.5;
     ddy += (ddy > 0) ? 0.5 : -0.5;
     int dx = (int)ddx;
     int dy = (int)ddy;

     // Now we can compute the corner points...
     int xPoints[] = new int[4];
     int yPoints[] = new int[4];

     xPoints[0] = x1 + dx; yPoints[0] = y1 + dy;
     xPoints[1] = x1 - dx; yPoints[1] = y1 - dy;
     xPoints[2] = x2 - dx; yPoints[2] = y2 - dy;
     xPoints[3] = x2 + dx; yPoints[3] = y2 + dy;

     g.fillPolygon(xPoints, yPoints, 4);
     }

    /**
     * Set the Action Map to respond to the <b>escape</b> key.<br>
     */
    public void setupActionMap()
    {
        Action actCloseMe =
                new AbstractAction()
                {
                    public void actionPerformed( ActionEvent e )
                    {                                //can.getParent().getParent().getParent().getParent()
                        Container c = thisMyCanvas.getParent();
                        c.setVisible( false );
                        ( ( JFrame ) c ).dispose();

                        //if ( jbInvokingButton != null )
                         //   jbInvokingButton.setEnabled( true );
                        System.out.println("Escape Pressed");
                    }
                };


        // this.getInputMap().put( KeyStroke.getKeyStroke( "ESCAPE" ), "pressed" );
       // this.getRootPane().getActionMap().put( "pressed", actCloseMe );
    }



    /**
     * Straighten up the milestones.
     */
    public void straightenMilestones()
    {
        for ( Iterator<MilestoneDataSource> iter = myMilestones.iterator(); iter.hasNext(); )
        {
            MilestoneDataSource mds = iter.next();
            if ( !mds.hasSibling( MilestoneDataSource.LEFT_SIBLING ) )
            {
                mds.setPosition( new Point ( 10, mds.getPosition().y ) );
            }
        }
        this.reRender();
    }
    /**
     *
     * @param me
     */
    public void mouseEntered ( MouseEvent me )
    {
        if ( PRINT_MOUSE_EVENTS )
            System.out.println( "<Canvas Mouse Entered>" );
    }

    public void mouseExited ( MouseEvent me )
    {
        if ( PRINT_MOUSE_EVENTS )
            System.out.println( "<Canvas Mouse Exited>" );
    }

    public void mouseClicked ( MouseEvent me )
    {
        if ( PRINT_MOUSE_EVENTS )
            System.out.println( "<Canvas Mouse Clicked>" );
        //pnlMilestonesGraph.pmg.setTitle( "Mouse Clicked Canvas (" + me.getX() +   ", " +  me.getY() + ")" ); 
    }

    public void mousePressed ( MouseEvent me )
    {
        if ( PRINT_MOUSE_EVENTS )
            System.out.println( "<Canvas Mouse Pressed>" );
    }

    public void mouseReleased ( MouseEvent me )
    {
        if ( PRINT_MOUSE_EVENTS )
            System.out.println( "<Canvas Mouse Released>" );
        this.getParent();
    }

    public void mouseDragged ( MouseEvent me )
    {
        if ( PRINT_MOUSE_EVENTS )
            System.out.println( "<Canvas Mouse Dragged>" );
        //pnlMilestonesGraph.pmg.setTitle( "myCanvas (" + me.getX() +   ", " +  me.getY() + ")" );

    }


    public void mouseMoved ( MouseEvent me )
    {
        if ( PRINT_MOUSE_EVENTS )
            if ( ( me.getX() % 5 == 0 ) || ( me.getY() % 5 == 0 ) )
                System.out.println( "<Canvas Mouse Moved>" );

        //pnlMilestonesGraph.pmg.setTitle( "myCanvas (" + me.getX() +   ", " +  me.getY() + ")" );
    }

    /**
     * Loads the Milestones from the DataModel onto the rendering canvas.<br>
     * Multiple invocations loads multiple duplicates.<br>
     */
    public void loadMilestones ()
    {
        devDataHelper ddh = devDataHelper.getDataHelper();
        DataModel dm = ddh.getDataModel();
        ProjectDataSource pds = dm.getCurrentProject();
        myMilestones = pds.getMilestoneList();

        for ( Iterator<MilestoneDataSource> iter = myMilestones.iterator(); iter.hasNext(); )
        {
            MilestoneDataSource mds = iter.next();
            cnvMilestone myCan = new cnvMilestone( mds );
            this.add( myCan );
        }
    }

    /**
     * Loads a single MilestoneDataSource object into a <code>cnvMilestone</code>
     * object, then into <code>myCanvas</code>.<br>
     * @param o we expect a <code>MilestoneDataSource</code> object.
     */
    public void loadMilestone ( Object o )
    {
        if ( o instanceof MilestoneDataSource )
        {
            cnvMilestone myCan = new cnvMilestone( ( MilestoneDataSource ) o );
            this.add( myCan );
        }
    }

    /**
     * Repaints this <code>myCanvas</code> component.<br>
     * Invoked through the Broadcast/Listener event handler methods.<br>
     * @see java.awt.Component#repaint()
     */
    public void reRender ()
    {        
        this.repaint();
    }

    /**
     * Removes/erases the milestone <code>o</code> from this canvas.<br>
     * If the milestone is found, it will no longer be rendered.<br>
     * @param o we expect a <code>MilestoneDataSource</code>
     * @return true if the corresponding <code>cnvMilestone</code> was found and removed,
     * false otherwise.
     * @see cnvMilestone
     * @see MilestoneDataSource
     */
    public boolean removeMilestone( Object o )
    {
        boolean bReturn = false;

        if ( o instanceof MilestoneDataSource )
        {
            cnvMilestone myCan = new cnvMilestone( ( MilestoneDataSource ) o );
            for ( int i = 0; i < this.getComponentCount(); i++ )
            {
                Component c = this.getComponent( i );
                if ( c instanceof cnvMilestone )
                {
                    cnvMilestone cnvCurrent = ( cnvMilestone )c;
                    if ( cnvCurrent.equals( myCan) )
                    {
                        this.remove( c );
                        bReturn = true;
                    }
                }
            }
        }
        return bReturn;
    }


    /**
     * Anything rendered here is not overwritten by milestones or their connector lines.<br>
     * <p/>
     * Renders this Panel in <b>only two</b> circumstances.<br>
     * Once after <code>setVisible( true )</code> and the other after a
     * <code>WINDOW_DEICONIFIED</code> event.<br>
     *
     * @param g
     */
    public void paint ( Graphics g )
    {
        super.paint( g );

        // Paint the connector lines.
        for ( Iterator<MilestoneDataSource> iter = myMilestones.iterator(); iter.hasNext(); )
        {
            MilestoneDataSource mds = iter.next();
            for ( int i = 0; i < 4; i ++ )
            {
                if ( mds.hasSibling( i ) )
                {
                    if ( i == MilestoneDataSource.TOP_SIBLING )
                        drawThickLine( g, mds.getPosition().x + 30, mds.getPosition().y,
                                mds.getSiblingAtLocation( i ).getPosition().x + 30,
                                mds.getSiblingAtLocation( i ).getPosition().y + 30, 5, Color.BLACK );

                    if ( i == MilestoneDataSource.RIGHT_SIBLING )
                        drawThickLine( g, mds.getPosition().x + 60, mds.getPosition().y + 15,
                                mds.getSiblingAtLocation( i ).getPosition().x,
                                mds.getSiblingAtLocation( i ).getPosition().y + 15, 5, Color.BLACK );

                    if ( i == MilestoneDataSource.BOTTOM_SIBLING )
                        drawThickLine( g,mds.getPosition().x + 30, mds.getPosition().y + 30,
                                mds.getSiblingAtLocation( i ).getPosition().x + 30,
                                mds.getSiblingAtLocation( i ).getPosition().y, 5, Color.BLACK );

                    if ( i == MilestoneDataSource.LEFT_SIBLING )
                        drawThickLine( g, mds.getPosition().x, mds.getPosition().y + 15,
                                mds.getSiblingAtLocation( i ).getPosition().x + 60,
                                mds.getSiblingAtLocation( i ).getPosition().y + 15, 5, Color.BLACK );
                }
            }
        }
    }
}
