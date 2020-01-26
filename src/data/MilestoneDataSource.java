package data;

import com.developer.data.devDataHelper;

import java.io.Serializable;
import java.awt.*;
import java.util.ArrayList;

import io.XMLTag;

/**
 * This class provides all data related to a Milestone.<br>
 */
public class MilestoneDataSource
        implements Serializable
{
    private static int NOT_STARTED = 1;
    private static int IN_PROGRESS = 2;
    private static int COMPLETED = 3;
    private static int LATE = 4;
    public static int TOP_SIBLING = 0;
    public static int RIGHT_SIBLING = 1;
    public static int BOTTOM_SIBLING = 2;
    public static int LEFT_SIBLING = 3;

    /**  Marks this object as being modified by the user.  */
    private int isDirty = 0;
    /** The Unique ID from the Palm OS Database unique record id.   */

    private int id = 0;
    private String name;
    private String description;
    private Point ptPosition;
    private int status = NOT_STARTED;
    private int projectID = 0;
    private MilestoneDataSource arSiblings[] = new MilestoneDataSource[4];

    /**
     * The Unique IDs of sibling Milestones as stored within the Palm OS database.<br>
     * Stored as a UInt32 in Palm.<br>
     */
    private int intTopSib = 0;
    /** @see MilestoneDataSource#intTopSib   */
    private int intRightSib = 0;
    /**  @see MilestoneDataSource#intTopSib  */
    private int intBottomSib = 0;
    /**  @see MilestoneDataSource#intTopSib  */
    private int intLeftSib = 0;

    // EMLOYEE FOREIGN KEYS
    private int emp0 = 0;
    private int emp1 = 0;
    private int emp2 = 0;
    private int emp3 = 0;

    /** Hotsync record status */
    private int attr = XMLTag.HH_ATTR_NONE;

    /**
     * Constructor for a MilestoneDataSource object.<br>
     * Maintains all data related to a Milestone.<br>
     */
    public MilestoneDataSource()
    {
        name = new String();
        description = new String();
        ptPosition = new Point();
    }

    /**
     * Compares the data members of this MilestoneDataSource against <code>o</code>.
     * Only tests the <code>id</code>, <code>projectID</code> and <code>name</code>.
     * @param o we expect a MilestoneDataSource object.
     * @return true if they have the same id, projectid and name, false otherwise.
     */
    public boolean equals( Object o )
    {
        assert ( o != null );
        assert ( o instanceof MilestoneDataSource );

        MilestoneDataSource that = ( MilestoneDataSource ) o;

        if ( this.id != that.id )
            return false;

        if ( this.projectID != that.projectID )
            return false;

        if ( !( this.name.equals( that.name ) ) )
            return false;

        return true;
    }


    /**
     * Retrieve the name of this Milestone.<br>
     * @return A String representing this Milestone's name.
     */
    public String getName()
    {
        return name;
    }


    /**
     *
     * @return
     */
    public int getNumberOfEmployees()
    {
        int iCount = 0;
        if ( emp0 != 0 )
            iCount ++;
        if ( emp1 != 0 )
            iCount ++;
        if ( emp2 != 0 )
            iCount ++;
        if ( emp3 != 0 )
            iCount ++;
        return iCount;
    }

    /**
     * Retrieve the description of this Milestone.<br>
     * @return A String representing this Milestone's description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     *
     * @param i
     * @return
     */
    public boolean addEmployee( int i )
    {
        boolean bSuccess = false;
        if ( this.isEmployeeAssigned( i ) )
        {
            bSuccess = true;
        }

        else if ( this.getNumberOfEmployees() == 4 )
        {
            bSuccess = false;
        }
        else
        {
            if ( emp0 == 0 )
            {
                emp0 = i;
                bSuccess = true;
            }
            else if ( emp1 == 0 )
            {
                emp1 = i;
                bSuccess = true;
            }
            else if ( emp2 == 0 )
            {
                emp2 = i;
                bSuccess = true;
            }
            else if ( emp3 == 0 )
            {
                emp3 = i;
                bSuccess = false;
            }
            else
            {
                bSuccess = false;
            }
        }


        return bSuccess;
    }

    /**
     * Detach the sibling at position <code>pos</code> .
     * @param pos the sibling's position ( Top(0), Right(1), Bottom(2), Left(3) ).
     */
    public void detachSibling( int pos )
    {
        if ( this.hasSibling( pos ) )
        {
            // get the opposing sibling
            MilestoneDataSource mdsSibling = this.getSiblingAtLocation( pos );
            // dettach yourself from it
            mdsSibling.setSiblingAtLocation( this.getOppositeConnector(pos ),  null );
            // detach your id from its ref array
            mdsSibling.setSiblingUniqueID( this.getOppositeConnector( pos ), 0 );
            // detach the sibling from yourself
            this.setSiblingAtLocation( pos, null );
            // detach the sibling's id from yourself
            this.setSiblingUniqueID( pos, 0 );

        }
    }

    /**
     *
     * @return
     */
    public EmployeeDataSource [] getEmployees()
    {
        devDataHelper ddh = devDataHelper.getDataHelper();
        DataModel dm = ddh.getDataModel();
        int iEmployeeCount = this.getNumberOfEmployees();
        ArrayList alTemp = new ArrayList();
        EmployeeDataSource  eds [] = new EmployeeDataSource[iEmployeeCount];

        if( emp0 > 0 )
            alTemp.add( dm.getEmployeeByID( emp0 ) );
        if ( emp1 > 0 )
            alTemp.add( dm.getEmployeeByID( emp1 ) );
        if ( emp2 > 0 )
            alTemp.add( dm.getEmployeeByID( emp2 ) );
        if ( emp3 > 0 )
            alTemp.add( dm.getEmployeeByID( emp3 ) );

        alTemp.trimToSize();

        for ( int i = 0; i < alTemp.size(); i ++ )
        {
            eds[i] = ( EmployeeDataSource) alTemp.get( i );
        }

        return eds;
    }

    /**
     *
     * @param pos
     * @return
     */
    public int getEmpID( int pos )
    {
        int rValue = 0;
        switch( pos )
        {
            case 0:
                rValue = emp0;
            break;
            case 1:
                rValue = emp1;
            break;
            case 2:
                rValue = emp2;
            break;
            case 3:
                rValue = emp3;
            break;
            default:
                rValue = 0;
        }
        return rValue;
    }


    public int getAttribute ( )
    {
        return attr;
    }
    /**
     *
     * @param s
     */
    public void setName( String s )
    {
        name = s;
    }

    /**
     * Removes the Employee identified by <code>id</code> from this Milestone.<br>
     * @param id The id of the employee to be removed.
     */
    public void removeEmployee( int id )
    {
        if ( emp0 == id )
            emp0 = 0;
        else if ( emp1 == id )
            emp1 = 0;
        else if ( emp2 == id )
            emp2 = 0;
        else if ( emp3 == id )
            emp3 = 0;

    }

    public void setAttribute ( int a )
    {
        attr = attr | a;
    }

    /**
     *
     * @param s
     */
    public void setDescription( String s )
    {
        description = s;
    }

    /**
     * Assign an employee to work on this Milestone.<br>
     *
     * @param pos the index to be set, [ 0, 1, 2, 3]
     * @param id  the id of the employee
     */
    public void setEmployeeID( int pos, int id )
    {
        switch( pos )
        {
            case 0:
                emp0 = id;
            break;
            case 1:
                emp1 = id;
            break;
            case 2:
                emp2 = id;
            break;
            case 3:
                emp3 = id;
            break;
            default:
        }
    }




    /**
     * Sets this Milestone's sibling located at position <p>.<br>
     * 
     * @param p
     * @param mds
     */
    public void setSiblingAtLocation( int p, MilestoneDataSource mds )
    {
        //assert ( p > MilestoneDataSource.TOP_SIBLING );
        //assert ( p < MilestoneDataSource.LEFT_SIBLING );

        arSiblings[p] = mds;

        // clear out the sibling.
        if ( mds == null )
            this.setSiblingUniqueID( p, 0 );
    }

    /**
     * Sets the unique id as retrieved from the Palm OS internal database UInt32.<br>
     *
     * @param iWhichSibling the sibling to set ( Top(0), Right(1), Bottom(2), Left(3) ).
     * @param intUniqueID the UInt32 value retrieved from the underlying Palm OS database record
     * id.
     */
    public void setSiblingUniqueID( int iWhichSibling, int intUniqueID )
    {
        //assert ( iWhichSibling > MilestoneDataSource.TOP_SIBLING );
       // assert ( iWhichSibling < MilestoneDataSource.LEFT_SIBLING );

        switch( iWhichSibling )
        {
            case 0:
                this.intTopSib = intUniqueID;
            break;
            case 1:
                this.intRightSib = intUniqueID;
            break;
            case 2:
                this.intBottomSib = intUniqueID;
            break;
            case 3:
                this.intLeftSib = intUniqueID;
            break;
        }
    }



    /**
     * Sets the status of this milestone ( 0 ... 4).<br>
     * A value of 0(zero) is unknown/undefined.<br>
     * <br>
     * <font size=-1>
     * NOT_STARTED = 1<br> IN_PROGRESS = 2<br>  COMPLETED = 3<br>  LATE = 4<br>
     * </font>
     *
     * @param i the status number.
     * @see MilestoneDataSource#NOT_STARTED
     * @see MilestoneDataSource#IN_PROGRESS
     * @see MilestoneDataSource#COMPLETED
     * @see MilestoneDataSource#LATE
     */
    public void setStatus( int i )
    {
         status = i;
    }


    /**
     * Returns the sibling of this milestone.<br>
     * Siblings may exits at Top(0), Right(1), Bottom(2) or Left(3).<br>
     * First use <code>hasSibling()</code> to check for the existence of a sibling.<br>
     *
     * <br>
     * <font size=-1>
     * TOP_SIBLING = 0<br> RIGHT_SIBLING = 1<br>  BOTTOM_SIBLING = 2<br>  LEFT_SIBLING = 3<br>
     * </font>
     *
     * @param iWhichSibling the position of sibling to return( 0, 1, 2, 3 ).
     * @return a MilestoneDataSource object if it exists, otherwise null.
     * @see MilestoneDataSource#hasSibling(int)
     */
    public MilestoneDataSource getSiblingAtLocation( int iWhichSibling )
    {
        assert ( iWhichSibling >= 0);
        assert ( iWhichSibling <= 3);
        return arSiblings[iWhichSibling];
    }

    /**
     *
     * @param iWhichSibling
     * @return
     */
    public int getSiblingUniqueID( int iWhichSibling )
    {
        assert ( iWhichSibling >= 0);
        assert ( iWhichSibling <= 3);
        int rValue = 0;
        switch( iWhichSibling )
        {
            case 0:
                rValue = this.intTopSib;
            break;
            case 1:
                rValue = this.intRightSib;
            break;
            case 2:
                rValue = this.intBottomSib;
            break;
            case 3:
                rValue = this.intLeftSib;
            break;

        }
        return rValue;
    }

    /**
     * Returns the status of this milestone.<br>
     * <br>
     * <font size=-1>
     * NOT_STARTED = 1<br> IN_PROGRESS = 2<br>  COMPLETED = 3<br>  LATE = 4<br>
     * </font>
     *
     *
     * @return an integer ( 0 ... 4 )
     * @see MilestoneDataSource#setStatus(int)
     */
    public int getStatus()
    {
        return status;
    }

    /**
     *
     * @return
     */
    public int getProjectID()
    {
        return projectID;
    }

    /**
     *
     * @return
     */
    public int getOppositeConnector( int i)
    {
        int iReturn = 0;

        switch( i )
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

    /**
     *
     * @return
     */
    public Point getPosition()
    {
        return ptPosition;
    }

    /**
     * Set the ID of the project to which this milestone is associated.<br>
     * @param i
     */
    public void setProjectID( int i )
    {
        projectID = i;
    }

    /**
     * Moves the location of the top-left corner of the Milestone.<br>
     * @param p the x, y point to relocate to.
     */
    public void setPosition( Point p )
    {
        ptPosition = p;
    }

    public void setIntTopSib( int i )
    {
        intTopSib = i;
    }

    /**
     * Returns the Top Sibling.<br>
     * The top sibling is also the preceding milestone.
     * @return
     */
    public int getIntTopSib( )
    {
        return intTopSib;
    }


    /**
     * Use to check for an exiting sibling before invoking <code>getSiblingAtLocation()</code>.<br>
     * Siblings may exits at Top(0), Right(1), Bottom(2) or Left(3).<br>
     *
     *  <br>
     * <font size=-1>
     * TOP_SIBLING = 0<br> RIGHT_SIBLING = 1<br>  BOTTOM_SIBLING = 2<br>  LEFT_SIBLING = 3<br>
     * </font>
     *
     * @param iWhichSibling the position to check, ( 0, 1, 2, 3 ).
     * @return true if a sibling exists, false otherwise.
     * @see MilestoneDataSource#getSiblingAtLocation(int)
     */
    public boolean hasSibling( int iWhichSibling )
    {
        if( arSiblings[ iWhichSibling] == null )
            return false;
        else
            return true;
    }

    /**
     * Determine if this MilestoneDataSource is not connected to any other MilestoneDataSource.<br>
     *
     * @return
     */
    public boolean hasNoSiblings(  )
    {
        if ( this.intTopSib == 0 && this.intRightSib  == 0 &&
             this.intBottomSib  == 0 &&  this.intLeftSib == 0 )
        {
            return true;
        }

        return false;
    }


    /**
     * Checks if the employee identified by <code>id</code> is assigned to this milestone.<br>
     * @param id the Record ID of this employee from the Palm Database (UInt32)
     * @return true if this employee is assigned, false otherwise
     */
    public boolean isEmployeeAssigned( int id )
    {
        boolean bReturn = false;

        if ( emp0 == id )
            bReturn = true;
        else if ( emp1 == id )
            bReturn = true;
        else if ( emp2 == id )
            bReturn = true;
        else if ( emp3 == id )
            bReturn = true;

        return bReturn;
    }

    /**
     * Sets the unique id of this Milestone.<br>
     * The unique id comes from the Palm OS database unique record id (UInt32).<br>
     * @param i the ID to be used for this Milestone.
     */
    public void setID( int i )
    {
        assert( i >= 0 );
        this.id = i;
    }

    /**
     * Sets the unique id of the Bottom sibling Milestone.<br>
     * The unique id comes from the Palm OS database unique record id (UInt32).<br>
     * @param i
     */
    public void setIntBottomSib( int i )
    {
        intBottomSib = i;
    }


    /**
     * Get the unique id of this Milestone.<br>
     * The unique id comes from the Palm OS database unique record id (UInt32).<br>
     * @return an int or 0 if it has not been set yet.
     */
    public int getID()
    {
        return id;
    }

    /**
     *
     * @return
     */
    public int getIntBottomSib()
    {
        return intBottomSib;
    }

    /**
     * Sets the unique id of the Left sibling milestone.<br>
     * The unique id comes from the Palm OS database unique record id (UInt32).<br>
     * @param i
     */
    public void setIntLeftSib( int i )
    {
        intLeftSib = i;
    }

    public int getIntLeftSib()
    {
        return intLeftSib;
    }

    /**
     * Sets the unique id of the Right sibling milestone.<br>
     * The unique id comes from the Palm OS database unique record id (UInt32).<br>
     * @param i
     */
    public void setIntRightSib( int i )
    {
        intRightSib = i;
    }

    /**
     * Gets the unique id of the Right sibling milestone.<br>
     * The unique id comes from the Palm OS database unique record id (UInt32).<br>
     *
     * @return the Unique ID as an int
     */
    public int getIntRightSib()
    {
        return intRightSib;
    }

    /**
     * Provides a String representation of this Object.<br>
     * Used for populating <code>lstEmployeeMilestones</code>
     * @return the name of this Milestone as a String
     * @see MilestoneDataSource#name
     * @see gui.lists.lstEmployeeMilestones
     */
    public String toString()
    {
        if ( name == null )
            return new String("Milestone");
        else
            return name;

    }
}
