package gui.screens;

import events.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

import data.DataModel;
import data.ProjectDataSource;
import com.developer.data.devDataHelper;
import gui.lists.lstProjects;

/**
 * Displays the list of Projects.<br>
 * Navigate to here from pnlMain.<br>
 */
public class pnlProjectList
        extends JPanel
{

    private JLabel lblProjects = new JLabel( "Select a project:" );
    private lstProjects list = new lstProjects();

    /**
     *
     */
    public pnlProjectList ()
    {
        super();

        devDataHelper ddh = devDataHelper.getDataHelper();
        DataModel dm = ddh.getDataModel();

        list.setListData( dm.getProjectNameList() );
        list.setFont( new Font( "Arial", Font.PLAIN, 14 ) );
        list.setFixedCellHeight( 20 );
        list.setFixedCellWidth( 250 );

        lblProjects.setFont( new Font( "Arial", Font.BOLD, 18 ) );
        this.add( lblProjects );
        this.add( new JScrollPane( list ) );
        this.setToolTipText( pnlProjectList.class.toString() );
    }


}
