package data;

import java.util.ArrayList;

/**
 * This class encapsulates all data related to an employee.<br>
 */
public class EmployeeDataSource
{
    /**  Marks this object as being modified by the user.  */
    private int isDirty = 0;
    public int id;

    /** The first of four ids of the project that this employee is working on.  */
    private int pid0;
    /** The second of four ids of the project that this employee is working on.  */
    private int pid1;
    /** The third of four ids of the project that this employee is working on.  */
    private int pid2;
    /** The fourth of four ids of the project that this employee is working on.  */
    private int pid3;

    public String firstname;
    public String lastname;
    public String description;

    /**
     * Default constructor without initialization. <br>
     */
    public EmployeeDataSource()
    {
        
    }

    /**
     * Constructor that initializes with the first name and last name.<br>
     * @param f A String representing the first name.
     * @param l A String representing the last name.
     */
    public EmployeeDataSource( String f, String l )
    {
        firstname = f;
        lastname = l;
    }

    /**
     * Copy constructor.<br>
     * @param eds
     */
    public  EmployeeDataSource ( EmployeeDataSource eds )
    {
        this.firstname = eds.firstname;
        this.lastname = eds.lastname;
        this.description = eds.description;
    }

    /**
     *
     * @param s
     */
    public void setFname( String s )
    {
        firstname = s;
    }


    /**
     *
     * @param s
     */
    public void setLname( String s )
    {
        lastname = s;
    }

    /**
     * Returns the String representation of this Object.
     * @return
     * @see Object#toString()
     */
    public String toString()
    {
        if ( firstname == null )
            return new String();

        return firstname + " " + lastname;
    }

    /**
     * Get the ID for the first Project to which this employee is assigned.
     * @return An int indicating the first Project's ID.
     * @see ProjectDataSource#getProjectID()
     * @see EmployeeDataSource#getProjectID1()
     * @see EmployeeDataSource#getProjectID2()
     * @see EmployeeDataSource#getProjectID3()
     */
    public int getProjectID0()
    {
        return pid0;
    }

    /**
     * Get the ID for the second Project to which this employee is assigned.
     * @return  An int indicating the second Project's ID.
     * @see EmployeeDataSource#getProjectID0()
     * @see EmployeeDataSource#getProjectID2()
     * @see EmployeeDataSource#getProjectID3()
     */
    public int getProjectID1()
    {
        return pid1;
    }

    /**
     * Get the ID for the third Project to which this employee is assigned.
     * @return An int indicating the third Project's ID.
     * @see EmployeeDataSource#getProjectID0()
     * @see EmployeeDataSource#getProjectID1()
     * @see EmployeeDataSource#getProjectID3()
     */
    public int getProjectID2()
    {
        return pid2;
    }

    /**
     * Get the ID for the fourth Project to which this employee is assigned.
     * @return An int indicating the fourth Project's ID.
     * @see EmployeeDataSource#getProjectID0()
     * @see EmployeeDataSource#getProjectID1()
     * @see EmployeeDataSource#getProjectID2()
     */
    public int getProjectID3()
    {
        return pid3;
    }


    /**
     * Retrieve an <code>ArrayList</code> of Project IDs to which this employee is assigned.<br>
     * The size of the <code>ArrayList</code> indicates the quantity of projects.<br>
     * The size of the ArrayList is zero if this employee has no assigned projects.<br>
     * @return an <code>ArrayList</code> of Project IDs.
     */
    public ArrayList <Integer> getProjectIDs()
    {
        ArrayList alReturn = new ArrayList(0);
        if ( getProjectID0() > 0 )
            alReturn.add( getProjectID0() );
        if ( getProjectID1() > 0 )
            alReturn.add( getProjectID1() );
        if ( getProjectID2() > 0 )
            alReturn.add( getProjectID2() );
        if ( getProjectID3() > 0 )
            alReturn.add( getProjectID3() );
        return alReturn;
    }

    /**
     * Determine if this employee has enough room to be assigned another project.<br>
     * @return true if this employee has at least one unassigned space for a project,
     * false otherwise.
     */
    public boolean hasAvailableProject()
    {
        if ( this.getProjectID0() == 0 )
            return true;
        if ( this.getProjectID1() == 0 )
            return true;
        if ( this.getProjectID2() == 0 )
            return true;
        if ( this.getProjectID3() == 0 )
            return true;
        return false;
    }

    /**
     * Search for a Project assigned to this Employee identified by <code>id</code>.<br>
     *
     * @param id The id of the Project for which to search.
     * @return Returns true if the Project is found, false otherwise.
     */
    public boolean isAssignedToProject( int id )
    {
        if ( this.getProjectID0() == id )
            return true;
        if ( this.getProjectID1() == id )
            return true;
        if ( this.getProjectID2() == id )
            return true;
        if ( this.getProjectID3() == id )
            return true;
        return false;
    }

    /**
     * Deletes the Project identified by <code>i</code> from this employee's assignments.
     * @param i
     */
    public void deleteFromProject( int i )
    {
        if ( pid0 == i )
            pid0 = 0;
        if ( pid1 == i )
            pid1 = 0;
        if ( pid2 == i )
            pid2 = 0;
        if ( pid3 == i )
            pid3 = 0;
    }

    /**
     * Finds an available slot and then sets that slot with the new Project ID value.<br>
     * Only allows a maximum of 4 Project ID assignments.
     * @param i the Project ID
     */
    public void setNextAvailableProject( int i )
    {
         if ( this.getProjectID0() == 0 )
            this.setProjectID0( i );
        else  if ( this.getProjectID1() == 0 )
            this.setProjectID1( i );
        else  if ( this.getProjectID2() == 0 )
            this.setProjectID2( i );
        else  if ( this.getProjectID3() == 0 )
            this.setProjectID3( i );
    }

    /**
     * Set the ID of the first project to which this Employee is assigned.<br>
     * @param i
     */
    public void setProjectID0( int i )
    {
        pid0 = i;
    }

    /**
     * Set the ID of the second project to which this Employee is assigned.<br>
     * @param i
     */
    public void setProjectID1( int i )
    {
        pid1 = i;
    }
    /**
     * Set the ID of the third project to which this Employee is assigned.<br>
     * @param i
     */
    public void setProjectID2( int i )
    {
        pid2 = i;
    }
    /**
     * Set the ID of the fourth project to which this Employee is assigned.<br>
     * @param i
     */
    public void setProjectID3( int i )
    {
        pid3 = i;
    }
}
