package gui.menuitems;

import events.EnableListener;

import javax.swing.*;

import gui.EADesktop;
import gui.screens.pnlProjectMenu;
import gui.screens.pnlEmployeeList;

import java.awt.*;

/**
 * This class provides dynamic control of a Menu Items <code>enabled/disabled</code> setting.<br>
 * Implements a listener that invokes the method <code>setEnabled()</code>
 *
 * @see javax.swing.JMenuItem#setEnabled(boolean)
 * @see EnableListener
 * @see events.EnableEvent
 */
public class JMenuItemEnabled
        extends JMenuItem
        implements EnableListener
{

    private JPanel cParent = new JPanel();

    /**
     * The name of this JMenuItem used for display and for identification of enabling/disabling.
     *
     * @see EnableListener
     */
    private String strMyName;

    /**
     * A Constructor that sets the Name and the visibility of this Menu Item.<br>
     *
     * @param s This menu item's display name and enable/disable identifier.
     */
    public JMenuItemEnabled ( String s, boolean vis )
    {
        super( s );
        strMyName = s;
        EADesktop.getEventManager().removeListener( EnableListener.class, this );
        EADesktop.getEventManager().addListener( EnableListener.class, this );
        this.setEnabled( vis );
    }

    /**
     * Used to set a parent frame, I think?
     * @param c
     */
    public void setParent ( JPanel c )
    {
        cParent = c;
    }

    /**
     * Verify the display name of this menu item.<br>
     *
     * @param s the String to compare against the name <code>strMyName</code>.
     * @return true if <code>s</code> <code>equals()</code> <code>strMyName</code>.
     * @see #strMyName
     * @see EnableListener
     */
    public boolean isName ( String s )
    {
        return strMyName.equals( s );
    }

    /**
     * Enable or Disable this JMenuItem
     * @param iType the command to ENABLE_YOURSELF, DISABLE_YOURSELF, or DISABLE_ALL.
     * @param objGUIControlName we expect a String, the display name of the menu item to enable/disable.
     * @see EnableListener
     */
    public void setEnabled ( int iType, Object objGUIControlName )
    {
        // Simply obey the command to disable yourself.
        if ( iType == EnableListener.DISABLE_ALL )
        {
            this.setEnabled( false );
            return;
        }

        // We simply use a String to id ourselves,
        // if it's not a String, then just exit this method.
        if ( ! ( objGUIControlName instanceof String ) )
            return;

        // If the name of the requested gui control does not match
        // this, then just exit this method.
        if ( ! (  this.isName(  ( String ) objGUIControlName ) ) )
            return;


        if ( iType == EnableListener.DISABLE_YOURSELF )
        {
            this.setEnabled( false );
        }
        else if ( iType == EnableListener.ENABLE_YOURSELF )
        {
             this.setEnabled( true );

        }
    }
}
