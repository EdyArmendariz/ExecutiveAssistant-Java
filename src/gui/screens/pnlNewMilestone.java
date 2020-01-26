
package gui.screens;

import com.developer.data.devDataHelper;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;
import data.DataModel;
import data.ProjectDataSource;
import data.MilestoneDataSource;
import javax.swing.*;
import gui.EADesktop;
import events.LoadEventListener;
import events.LoadEvent;

/**
 * This class provides a JFrame that displays GUI components for adding a new milestone into the current project.<br>
 * @see data.DataModel#getCurrentProject()
 */
public class pnlNewMilestone
        extends pnlEditMilestone
{
    public pnlNewMilestone ()
    {
        super();
        this.setTitle( "New Milestone ..." );



        btnOK.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                devDataHelper ddh = devDataHelper.getDataHelper();
                DataModel dm = ddh.getDataModel();
                ProjectDataSource pds = dm.getCurrentProject();

                MilestoneDataSource mds = new MilestoneDataSource();
                mds.setProjectID( pds.getProjectID() );
                mds.setID( 0 );
                mds.setName( name.getText() );
                mds.setDescription( description.getText() );
                int iSelected = 0;
                if (rbNotStarted.isSelected())
                    iSelected = 1200;
                if (rbInProgress.isSelected())
                    iSelected = 1201;
                if (rbCompleted.isSelected())
                    iSelected = 1202;
                if (rbLate.isSelected())
                    iSelected = 1203;
                mds.setStatus( iSelected );
                pds.addMilestone( mds );

                EADesktop.getEventManager().fireEvent(
                        LoadEventListener.class,
                        LoadEvent.LOAD,
                        new LoadEvent( mds ) );
            }
        } );

        btnCancel.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                Container c = btnCancel.getRootPane().getParent();
                c.setVisible( false );
                ( ( JFrame ) c ).dispose();
            }
        } );
    }
}
