package gui.menuitems;

import data.MilestoneDataSource;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import events.MilestoneGrabberEvent;
import events.RenderListener;
import events.RenderEvent;
import gui.EADesktop;

/**
 * This class provides functionality for attaching and detaching siblings from a milestone.<br>
 */
public class pmnuAddRemoveSiblings
        extends JPopupMenu
{
    private String strConnect = "Connect ";
    private String strDisconnect = "Disconnect ";
    private String strNext = "Next Milestone";
    private String strRight = "Right Milestone";
    private String strPrevious = "Previous Milestone";
    private String strLeft = "Left Milestone";
    private String strDelete = "Delete";
    private JMenuItem jmiNext = new JMenuItem( strNext );
    private JMenuItem jmiRight = new JMenuItem( strRight );
    private JMenuItem jmiBottom = new JMenuItem( strPrevious );
    private JMenuItem jmiLeft = new JMenuItem( strLeft );
    private JMenuItem jmiDelete = new JMenuItem( strDelete );
    private boolean isConnectedNext = false;
    private boolean isConnectedRight = false;
    private boolean isConnectedBottom = false;
    private boolean isConnectedLeft = false;
    private MilestoneDataSource myMilestone = new MilestoneDataSource();


    /**
     * Constructs the object that handles connecting and disconnecting of milestones.
     *
     * @param mds
     */
    public pmnuAddRemoveSiblings ( MilestoneDataSource mds )
    {
        myMilestone = mds;
        // todo study setLightWeightPopupEnabled(false)
        this.setLightWeightPopupEnabled( false );
        this.setOpaque( true );
        initMenuLabels();
        initMenuActions();
    }

    /**
     * Initializes the available actions for the currently selected milestone.<br>
     * Properly dances the labels according to the current status of this milestones
     * siblings.<br>
     */
    private void initMenuActions ()
    {
        jmiNext.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                MilestoneGrabberEvent mg = EADesktop.getMilestoneGrabber();

                if ( isConnectedNext == false )
                {
                    mg.setWaiting( true );
                    mg.setSource( myMilestone, 0 );
                }
                else
                    if ( isConnectedNext == true )
                    {
                        mg.setWaiting( false );
                        myMilestone.detachSibling( 0 );
                    }
                EADesktop.getEventManager().fireEvent(
                        RenderListener.class,
                        RenderListener.STUFF,
                        new RenderEvent( new Object() ) );
            }
        } );

        jmiRight.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                System.out.println( "Right" );
                MilestoneGrabberEvent mg = EADesktop.getMilestoneGrabber();

                if ( isConnectedRight == false )
                {
                    mg.setWaiting( true );
                    mg.setSource( myMilestone, 1 );
                }
                else
                    if ( isConnectedRight == true )
                    {
                        mg.setWaiting( false );
                        myMilestone.detachSibling( 1 );
                    }

                EADesktop.getEventManager().fireEvent(
                        RenderListener.class,
                        RenderListener.STUFF,
                        new RenderEvent( new Object() ) );
            }
        } );

        jmiBottom.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                System.out.println( "Previous" );

                MilestoneGrabberEvent mg = EADesktop.getMilestoneGrabber();
                if ( isConnectedBottom == false )
                {
                    mg.setWaiting( true );
                    mg.setSource( myMilestone, 2 );
                }
                else
                    if ( isConnectedBottom == true )
                    {
                        mg.setWaiting( false );
                        myMilestone.detachSibling( 2 );
                    }

                EADesktop.getEventManager().fireEvent(
                        RenderListener.class,
                        RenderListener.STUFF,
                        new RenderEvent( new Object() ) );
            }
        } );

        jmiLeft.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                System.out.println( "Left" );
                MilestoneGrabberEvent mg = EADesktop.getMilestoneGrabber();
                if ( isConnectedLeft == false )
                {
                    mg.setWaiting( true );
                    mg.setSource( myMilestone, 3 );
                }
                else
                    if ( isConnectedLeft == true )
                    {
                        mg.setWaiting( false );
                        myMilestone.detachSibling( 3 );
                    }

                EADesktop.getEventManager().fireEvent(
                        RenderListener.class,
                        RenderListener.STUFF,
                        new RenderEvent( new Object() ) );
            }
        } );

        jmiDelete.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                System.out.println("Delete");
                MilestoneGrabberEvent mg = EADesktop.getMilestoneGrabber();

                if (isConnectedNext == true)
                {
                    myMilestone.detachSibling(0);
                }

                if (isConnectedRight == true)
                {
                    myMilestone.detachSibling(1);
                }

                if (isConnectedBottom == true)
                {
                    myMilestone.detachSibling(2);
                }

                if (isConnectedLeft == true)
                {
                    myMilestone.detachSibling(3);
                }

                mg.setWaiting(false);

                com.developer.data.devDataHelper dvh = com.developer.data.devDataHelper.getDataHelper();
                data.DataModel dm = dvh.getDataModel();
                data.ProjectDataSource pds = dm.getCurrentProject();
                boolean b = pds.deleteMilestone( myMilestone );

                EADesktop.getEventManager().fireEvent(
                        events.LoadEventListener.class,
                        events.LoadEvent.UNLOAD,
                        new events.LoadEvent( myMilestone ) );

                EADesktop.getEventManager().fireEvent(RenderListener.class,
                        RenderListener.STUFF,
                        new RenderEvent(new Object()));
            }
        });

    }

    /**
     * Properly dances this milestone's labels on its status in regards
     * to its currently connected/disconnected siblings.
     */
    private void initMenuLabels ()
    {

        if ( myMilestone.hasSibling( MilestoneDataSource.TOP_SIBLING ) )
        {
            isConnectedNext = true;
            jmiNext.setText( strDisconnect + strNext );
            this.add( jmiNext );
        }
        else
        {
            isConnectedNext = false;
            jmiNext.setText( strConnect + strNext );
            this.add( jmiNext );
        }

        if ( myMilestone.hasSibling( MilestoneDataSource.RIGHT_SIBLING ) )
        {
            isConnectedRight = true;
            jmiRight.setText( strDisconnect + strRight );
            this.add( jmiRight );
        }
        else
        {
            isConnectedRight = false;
            jmiRight.setText( strConnect + strRight );
            this.add( jmiRight );
        }

        if ( myMilestone.hasSibling( MilestoneDataSource.BOTTOM_SIBLING ) )
        {
            isConnectedBottom = true;
            jmiBottom.setText( strDisconnect + strPrevious );
            this.add( jmiBottom );
        }
        else
        {
            isConnectedBottom = false;
            jmiBottom.setText( strConnect + strPrevious );
            this.add( jmiBottom );
        }

        if ( myMilestone.hasSibling( MilestoneDataSource.LEFT_SIBLING ) )
        {
            isConnectedLeft = true;
            jmiLeft.setText( strDisconnect + strLeft );
            this.add( jmiLeft );
        }
        else
        {
            isConnectedLeft = false;
            jmiLeft.setText( strConnect + strLeft );
            this.add( jmiLeft );
        }

        // Always add the delete option.
         this.add( jmiDelete );
    }

}
