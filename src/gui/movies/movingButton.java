package gui.movies;

import javax.swing.*;
import javax.swing.text.Position;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Edy
 * Date: Mar 17, 2007
 * Time: 11:11:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class movingButton
        extends JButton
        implements MouseListener, MouseMotionListener

{
    public movingButton ()
    {
        super();
        this.setSize( 20, 20 );
        this.setMaximumSize( new Dimension( 20, 20 ) );
    }

    public movingButton ( String s )
    {
        super( s );
        this.setSize( 20, 20 );
        this.setMaximumSize( new Dimension( 20, 20 ) );
    }

    public void mouseEntered ( MouseEvent me )
    {
        //System.out.println( "entered");
    }

    public void mouseExited ( MouseEvent me )
    {
        //  System.out.println( "exited");
    }

    public void mouseClicked ( MouseEvent me )
    {
        Dimension d = this.getPreferredSize();
        System.out.println( "Prefered size: (" + d.getWidth() + ", " + d.getHeight() + ")" );
        System.out.println( "Size: " + this.getSize() );
    }

    public void mousePressed ( MouseEvent me )
    {
        Point p = this.getParent().getMousePosition();
        this.setText( "(" + p.getX() + ", " + p.getY() + ")" );
    }

    public void mouseReleased ( MouseEvent me )
    {
        Point p = this.getParent().getMousePosition();
        this.setLocation( p );
    }

    public void mouseDragged ( MouseEvent me )
    {

    }


    public void mouseMoved ( MouseEvent me )
    {

    }
}
