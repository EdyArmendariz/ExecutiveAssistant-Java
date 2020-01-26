package gui;

import events.ScreenChangeListener;
import events.ScreenChangeEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 *
 */
public class pnlNavigationButtons
        extends JPanel
        implements KeyListener
{
    JButton btnHome = new JButton( "Home" );
    JButton btnBack = new JButton( "Back" );
    JPanel pnlCurrentPanel = new JPanel();

    /**
     *
     */
    public pnlNavigationButtons ()
    {
        super();
        initializeButtonActions();
        this.setLayout( new FlowLayout( FlowLayout.LEFT ) );
        this.add( btnHome );
        this.add( btnBack );
        btnBack.addKeyListener( this );
    }

    /**
     * 
     */
    private void initializeButtonActions ()
    {
        btnHome.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                EADesktop.getEventManager().fireEvent(
                        ScreenChangeListener.class,
                        ScreenChangeListener.SHOW_MAIN,
                        new ScreenChangeEvent( this ) );
            }
        } );

        btnBack.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                EADesktop.getEventManager().fireEvent(
                        ScreenChangeListener.class,
                        ScreenChangeListener.SHOW_PREVIOUS,
                        new ScreenChangeEvent( this ) );
            }
        } );

    }

    public void keyTyped( KeyEvent ke )
    {
        if ( ke.getKeyCode() == 0 )
        {

        }
    }

    public void keyReleased( KeyEvent ke )
    {

    }

    public void keyPressed( KeyEvent ke )
    {
        
    }
}
