package gui.lists;

import events.*;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import data.DataModel;
import gui.EADesktop;


/**
 * 
 */
public class lstProjects
        extends JList
        implements DataChangeListener
{
    MySelectionHandler slsh = new MySelectionHandler();
    private DefaultListSelectionModel dlsmListModel = new DefaultListSelectionModel();
    private lstProjects thisList = null;

    /**
     *
     */
    public lstProjects ()
    {
        EADesktop.getEventManager().removeListener( DataChangeListener.class, this );
        EADesktop.getEventManager().addListener( DataChangeListener.class, this );
        this.setSelectionModel( dlsmListModel );
        dlsmListModel.addListSelectionListener( slsh );
        thisList = this;
    }


    /**
     * Constructs a list from an Object array.<br>
     *
     * @param o
     * @see JList
     */
    public lstProjects ( Object[] o )
    {
        super( o );
        EADesktop.getEventManager().removeListener( DataChangeListener.class, this );
        EADesktop.getEventManager().addListener( DataChangeListener.class, this );
        this.setSelectionModel( dlsmListModel );
        dlsmListModel.addListSelectionListener( slsh );
        thisList = this;
    }

    /**
     * @param iType
     * @param o
     */
    public void change ( int iScreen, int iType, Object o )
    {
        if ( iScreen == ScreenChangeListener.SHOW_PROJECT_LIST )
            if ( iType == DataChangeListener.UPDATE )
                if ( o instanceof DataModel )
                {
                    this.setListData( ( ( DataModel ) o ).getProjectNameList() );
                }
    }


    class MySelectionHandler
            implements ListSelectionListener
    {
        public void valueChanged ( ListSelectionEvent e )
        {
            String s = ( String ) thisList.getSelectedValue();
            if ( s == null )
                return;

            EADesktop.getEventManager().fireEvent( DataChangeListener.class,
                    DataChangeListener.TITLE, new DataChangeEvent( s, ScreenChangeListener.SHOW_PROJECT_MENU ) );

            EADesktop.getEventManager().fireEvent( ScreenChangeListener.class,
                        ScreenChangeListener.SHOW_PROJECT_MENU, new ScreenChangeEvent( new Object() ) );

            dlsmListModel.clearSelection();
        }
    }

}
