package gui.screens;

import events.ScreenChangeListener;
import events.DataChangeListener;

import javax.swing.*;
import java.awt.*;

import com.developer.data.devDataHelper;
import data.DataModel;
import gui.lists.lstEmployees;
import gui.EADesktop;

/**
 * Displays the panel that shows a list of <b>all</b> the Employees accross all Projects.<br>
 * This differs from <code>pnlProjectEmployeeList</code>.<br>
 *
 * @see pnlProjectEmployeeList
 */
public class pnlEmployeeList
        extends JPanel
{
    private JLabel lblEmployees = new JLabel( "View an employee's projects:" );
    private lstEmployees list = new lstEmployees();

    /**
     * Constructs the panel that shows a list of <b>all</b> the Employees accross all Projects.<br>
     */
    public pnlEmployeeList ()
    {
        super();
        devDataHelper ddh = devDataHelper.getDataHelper();
        DataModel dm = ddh.getDataModel();

        lblEmployees.setFont( new Font( "Arial", Font.BOLD, 18 ) );
        // merely a place holder for future assignment,
        // this behaves strangely if the list is too small at first.
        list.setListData( dm.getEmloyeeNameList() );
        list.setFont( new Font( "Arial", Font.PLAIN, 14 ) );
        list.setFixedCellHeight( 20 );
        list.setFixedCellWidth( 250 );
        this.add( lblEmployees );
        this.add( new JScrollPane( list ) );
        this.setToolTipText( pnlEmployeeList.class.toString() );
    }

}
