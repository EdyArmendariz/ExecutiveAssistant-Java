package events;

/**
 * Allows the menu items on the menubar to dance.<br>
 * Some options should only be available on certain screens.<br>
 */
public interface EnableListener
       extends SimpleEventListener
{

    public static final int ENABLE_YOURSELF = 1;
    public static final int DISABLE_YOURSELF = 2;
    public static final int DISABLE_ALL = 3;

    /**
     * Enable or Disable the GUI control that implements this method.
     * @param iType the command to enable or disable.
     * @param objGUIControllName the display name of the menu item to enable/disable.
     */
    public void setEnabled( int iType, Object objGUIControllName );
}
