package gui.screens;

import events.ScreenChangeListener;
import events.ScreenChangeEvent;
import events.EnableListener;
import events.EnableEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.developer.data.devDataHelper;
import data.DataModel;
import gui.EADesktop;

/**
 *
 */
public class pnlMain
        extends JPanel

{
    private GridLayout lytGrid = new GridLayout( 5, 2 );
    private JButton btnProjects = new JButton( "View Projects" );
    private JButton btnEmployees = new JButton( "View Employees" );

    /**
     *
     */
    public pnlMain ()
    {
        super();
        initializeButtonActions();
        btnProjects.setAlignmentX( Component.CENTER_ALIGNMENT );
        btnEmployees.setAlignmentX( Component.CENTER_ALIGNMENT );
        lytGrid.setHgap( 30 );
        lytGrid.setVgap( 30 );
        this.setLayout( lytGrid );
        btnProjects.setPreferredSize( new Dimension( 10, 20 ) );
        this.add( btnProjects );
        this.add( btnEmployees );
        this.setToolTipText( pnlMain.class.toString() );
    }

    /**
     *
     */
    private void initializeButtonActions ()
    {
        btnProjects.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                EADesktop.getEventManager().fireEvent(
                        ScreenChangeListener.class,
                        ScreenChangeListener.SHOW_PROJECT_LIST,
                        new ScreenChangeEvent( new Object() ) );
            }
        } );

        btnEmployees.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                EADesktop.getEventManager().fireEvent(
                        ScreenChangeListener.class,
                        ScreenChangeListener.SHOW_EMPLOYEE_LIST,
                        new ScreenChangeEvent( new Object() ) );
            }
        } );

    }

}
