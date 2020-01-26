package data;

import exceptions.MilestoneNotFoundException;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import io.XMLTag;

/**
 * This class contains the data contained in a Project.<br>
 */
public class ProjectDataSource
{
    /**  Marks this object as being modified by the user.  */
    private int isDirty = 0;
    private int uniqueID = 0;
    private int projectID = 1;
    private int iHighMilestoneID = 1;
    private String name = new String("Default Project");
    private String description = new String( "Project Description");
    private List <MilestoneDataSource> lstMilestones = new LinkedList();
    private List <EmployeeDataSource> lstEmployees = new LinkedList();
    /** HotSync record status. */
    private int attr = XMLTag.HH_ATTR_NONE;

    /**
     *
     */
    public ProjectDataSource(){}

    /**
     *
     * @param s
     */
    public ProjectDataSource( String s )
    {
        name = s;
    }

    /**
     *
     * @return
     */
    public String getProjectName()
    {
        return name;
    }

    /**
     * Iterate through all of the milestones and assign their sibling relationships.<br>
     * This data comes from the underlying Palm OS database (UInt32) unique record id.<br>
     */
    public void processSiblings()
    {
        MilestoneDataSource toBeAssigned = new MilestoneDataSource();
        Iterator <MilestoneDataSource> itOurter = lstMilestones.iterator();
        for( ; itOurter.hasNext(); )
        {
            toBeAssigned = itOurter.next();

            MilestoneDataSource possibleSibling = new MilestoneDataSource();
            Iterator <MilestoneDataSource> itInner = lstMilestones.iterator();
            for ( ; itInner.hasNext(); )
            {
                 possibleSibling = itInner.next();

                // test for top sibling
                if ( toBeAssigned.getSiblingUniqueID(MilestoneDataSource.TOP_SIBLING )
                    == possibleSibling.getID() )
                {
                    toBeAssigned.setSiblingAtLocation( MilestoneDataSource.TOP_SIBLING ,
                                            possibleSibling );
                }
                else if ( toBeAssigned.getSiblingUniqueID(MilestoneDataSource.RIGHT_SIBLING )
                    == possibleSibling.getID() )
                {
                    toBeAssigned.setSiblingAtLocation( MilestoneDataSource.RIGHT_SIBLING ,
                                            possibleSibling );
                }
                else if ( toBeAssigned.getSiblingUniqueID(MilestoneDataSource.LEFT_SIBLING )
                    == possibleSibling.getID() )
                {
                    toBeAssigned.setSiblingAtLocation( MilestoneDataSource.LEFT_SIBLING ,
                                            possibleSibling );
                }
                else if ( toBeAssigned.getSiblingUniqueID(MilestoneDataSource.BOTTOM_SIBLING )
                    == possibleSibling.getID() )
                {
                    toBeAssigned.setSiblingAtLocation( MilestoneDataSource.BOTTOM_SIBLING ,
                                            possibleSibling );
                }

            }

        }


    }

    /**
     * Get the EmployeeDataSource object if the Employee is assigned to this Project.<br>
     * @param f
     * @param l
     * @return
     */
    EmployeeDataSource getEmployeeByName( String f, String l )
    throws Exception
    {
        EmployeeDataSource eds = new EmployeeDataSource();
        Iterator <EmployeeDataSource> it = lstEmployees.iterator();

        for ( ; it.hasNext(); )
        {
            eds = it.next();
            if ( eds.firstname.equalsIgnoreCase( f )
                    && eds.lastname.equalsIgnoreCase( l ) )
            {
                return eds;
            }
        }

        throw new Exception();
    }

    public int getAttribute()
    {
        return attr;
    }

    /**
     * Get the EmployeeDataSource object if the Employee identified
     * by ID is found in this project.<br>
     * @param id  The Employee's ID
     * @return
     */
    EmployeeDataSource getEmployeeByID( int id )
    throws Exception
    {
        EmployeeDataSource eds = new EmployeeDataSource();
        Iterator <EmployeeDataSource> it = lstEmployees.iterator();

        for ( ; it.hasNext(); )
        {
            eds = it.next();
            if ( eds.id == id )
            {
                return eds;
            }
        }

        throw new Exception();
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean hasEmployee( int id )
    {
        EmployeeDataSource eds = new EmployeeDataSource();
        Iterator <EmployeeDataSource> it = lstEmployees.iterator();

        for ( ; it.hasNext(); )
        {
                    eds = it.next();
                    if ( eds.id == id )
                    {
                        return true;
                    }
                }
        return false;
    }

    /**
     *
     * @param s
     * @return
     * @throws MilestoneNotFoundException
     */
    MilestoneDataSource getMilestoneByName( String s )
    throws MilestoneNotFoundException
    {
        MilestoneDataSource mds = new MilestoneDataSource();
        Iterator <MilestoneDataSource> it = lstMilestones.iterator();

        for( ; it.hasNext(); )
        {
            mds = it.next();
             if( mds.getName().equalsIgnoreCase( s ))
                return mds;
        }

        MilestoneNotFoundException mnfe = new MilestoneNotFoundException();
        mnfe.appendToMessage( s );
        throw  mnfe;
    }


    /**
     * Insert an Employee into the data structure <code>lstEmployees</code>.<br>
     * @param eds
     * @return
     */
    public boolean addEmployee( EmployeeDataSource eds )
    {
        boolean bSuccess = false;

        try
        {
            bSuccess = lstEmployees.add( eds );
        }
        catch( java.lang.UnsupportedOperationException uoe)
        {
            bSuccess = false;
        }
        catch( java.lang.ClassCastException cce )
        {
            bSuccess = false;
        }
        catch ( java.lang.NullPointerException npe )
        {
            bSuccess = false;
        }
        catch ( java.lang.IllegalArgumentException iae)
        {
            bSuccess = false;
        }
        catch( Exception e )
        {
            bSuccess = false;
        }

        return bSuccess;
    }

    /**
     * Insert a Milestone into the data structure <code>lstMilestones</code>.<br>
     * @param mds
     * @return <code>true</code> if the Milestone was added successfully, <code>false</code> otherwise.<br>
     */
    public boolean addMilestone( MilestoneDataSource mds )
    {
        boolean bSuccess = false;
        try
        {
            bSuccess = lstMilestones.add( mds );
        }
        catch( java.lang.UnsupportedOperationException uoe)
        {
            bSuccess = false;
        }
        catch( java.lang.ClassCastException cce )
        {
            bSuccess = false;
        }
        catch ( java.lang.NullPointerException npe )
        {
            bSuccess = false;
        }
        catch ( java.lang.IllegalArgumentException iae)
        {
            bSuccess = false;
        }
        catch( Exception e )
        {
            bSuccess = false;
         }


        return bSuccess;
    }


    public void deleteEmployee( EmployeeDataSource eds )
    {
        eds.deleteFromProject( this.projectID );
        lstEmployees.remove( eds );
    }

    public boolean deleteMilestone( MilestoneDataSource mds )
    {
        boolean bReturn = false;
        try
        {
            bReturn = lstMilestones.remove( mds );
        }
        catch( java.lang.UnsupportedOperationException uoe)
        {
            bReturn = false;
        }
        catch( java.lang.ClassCastException cce )
        {
            bReturn = false;
        }
        catch ( java.lang.NullPointerException npe )
        {
            bReturn = false;
        }
        return bReturn;
    }

    /**
     *
     * @return
     */
    public List<MilestoneDataSource> getMilestoneList()
    {
        return lstMilestones;
    }

    /**
     * Adds the value of <code>a</code> to the existing bitwise value.<br>
     * Any existing bits are not modified.<br>
     * @param a
     */
    public void addAttribute ( int a )
    {
       attr = attr | a;
    }


    /**
     * Overwrites all bitwise attributes with the value of <code>a</code>.<br>
     * All other previous attributes are lost.<br>
     * @param a an XMLTag defined value
     * @see XMLTag#HH_ATTR_MODIFIED
     * @see XMLTag#HH_ATTR_NEW
     * @see XMLTag#HH_ATTR_NONE
     */
    public void setAttribute ( int a )
    {
        attr = a;
    }

    /**
     *
     * @param s
     */
    public void setProjectName(String s)
    {
        name = s;

    }

    /**
     *
     * @param s
     */
    public void setDescription(String s)
    {
        description = s;
    }

    public String toString()
    {
        if ( name == null )
            return new String();
        return name;
    }

    /**
     *
     * @return
     */
    public String getProjectDescription()
    {
        return description;
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
     * @param i
     */
    public void setProjectID( int i )
    {
        projectID = i;
    }


    public int getUniqueID()
    {
        return uniqueID;
    }

    public void setUniqueID ( int i )
    {
        uniqueID = i;
    }
    /**
     * Search for the highest Milestone ID, then return a value that is 1
     * integer value higher.
     */
    public int getNextHighestMilestoneID( )
    {
        Iterator <MilestoneDataSource> i = lstMilestones.iterator();
        for( ; i.hasNext(); )
        {
            MilestoneDataSource mds = i.next();
            int iCurr = mds.getID();
            if (  iHighMilestoneID <= iCurr )
            {
                 iHighMilestoneID = iCurr + 1;
            }
        }

        return iHighMilestoneID;
    }


    /**
     * Returns the Number of Employees assigned to this project.<br>
     * Duplicates may appear because the same employee may work on 2 or more projects.<br>
     * @return
     */
    public int getEmployeeCount()
    {
       return this.lstMilestones.size();
    }

    /**
     *
     * @return
     */
    public Object[] getProjectEmployees()
    {
        return this.lstEmployees.toArray();
    }


    public ProjectDataSource clone()
    {
        ProjectDataSource pds = new ProjectDataSource();
        pds.setProjectName( this.getProjectName() );
        pds.setProjectID( this.getProjectID() );
        // pds.lstEmployees =
        // pds.lstMilestones 
        return pds;
    }
}
