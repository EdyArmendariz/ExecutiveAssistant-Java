package gui;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Edy
 * Date: Mar 18, 2007
 * Time: 6:56:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class myRectangle
        extends Rectangle
        implements MouseListener, MouseMotionListener
{
    public myRectangle ()
    {
        super();
        this.x = 30;
        this.y = 30;
        this.height = 20;
        this.width = 20;


    }


    public void mouseEntered ( MouseEvent me )
    {
        System.out.println( "<Mouse Entered>" );
    }

    public void mouseExited ( MouseEvent me )
    {
        System.out.println( "<Mouse Exited>" );
    }

    public void mouseClicked ( MouseEvent me )
    {
        System.out.println( "<Mouse Clicked>" );
    }

    public void mousePressed ( MouseEvent me )
    {
        System.out.println( "<Mouse Pressed>" );
    }

    public void mouseReleased ( MouseEvent me )
    {
        System.out.println( "<Mouse Released>" );
    }

    public void mouseDragged ( MouseEvent me )
    {
        System.out.println( "<Mouse Dragged>" );
    }


    public void mouseMoved ( MouseEvent me )
    {

    }
}
