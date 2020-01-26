package events;

import data.MilestoneDataSource;
import java.awt.event.MouseEvent;

/**
 * Provides functionality for selecting different milestones on the same Graph.<br>
 */
public class MilestoneGrabberEvent
        extends SimpleEvent
{
    private boolean waitingForClick = false;
    private MilestoneDataSource mdsSource;
   // private MilestoneDataSource mdsSibling;
    private int iSibling = -1;

    /**
     *
     * @param o
     */
    public MilestoneGrabberEvent( Object o )
    {
        super( o );
    }

    /**
     * The Milestone looking for its sibling.<br>
     *
     * @param mds the Milestone to point to
     * @param i the sibling's position Top(0), Right(1), Bottom(2), Left(3)
     */
    public void setSource( MilestoneDataSource mds , int i )
    {
        if ( mds == null )
            throw new NullPointerException( "! ! !    MilestoneGrabberEvent::setSource()    ! ! !");

        mdsSource = mds;
        iSibling = i;
    }




    /**
     * The Milestone to be attached to <code>mdsSource</code>.
     * @param mds the Milestone to point to
     */
    public void setSibling(  MilestoneDataSource mds )
    {
        mdsSource.setSiblingAtLocation( iSibling, mds );
        mdsSource.setSiblingUniqueID( iSibling, mds.getID() );
    }


    /**
     * Check to see if the Milestone Grabber is waiting for you to notify it.
     * @return
     */
    public boolean isWaiting()
    {
        return waitingForClick;
    }


    /**
     * Notifies the Milestone Grabber to wait or not for another sibling attachment.
     * @param b
     */
    public void setWaiting( boolean b )
    {
        waitingForClick = b;
    }


    /**
     *
     * @param sel
     * @param se
     */
    public void broadcast( SimpleEventListener sel, int i,  SimpleEvent se )
    {

    }

    /**
     * Gets the source (referring) milestone.
     * @return
     */
  public MilestoneDataSource getSourceMilestone()
  {
      return mdsSource;
  }


    /**
     *  the sibling's position Top(0), Right(1), Bottom(2), Left(3)
     * @return
     */
    public int getOppositeConnector()
    {
        int iReturn = 0;

        switch (iSibling)
        {
            case 0:
                iReturn = 2;
                break;
            case 1:
                iReturn = 3;
                break;
            case 2:
                iReturn = 0;
                break;
            case 3:
                iReturn = 1;
                break;
        }
        return iReturn;
    }


    public void mouseClicked(MouseEvent e)
    {
        waitingForClick = true;

    }
    //          Invoked when the mouse button has been clicked (pressed and released) on a component.
    public void 	mouseEntered(MouseEvent e)  {}
            //  Invoked when the mouse enters a component.
    public void 	mouseExited(MouseEvent e) {}
      //        Invoked when the mouse exits a component.
    public void 	mousePressed(MouseEvent e) {}
      //        Invoked when a mouse button has been pressed on a component.
   public  void 	mouseReleased(MouseEvent e){}
      //        Invoked when a mouse button has been released on a component.

}


