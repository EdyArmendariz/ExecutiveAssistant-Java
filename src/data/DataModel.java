package data;

import events.DataChangeListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;


import gui.EADesktop;
import gui.lists.lstProjects;
import gui.lists.lstProjects;

import javax.swing.*;

import io.XMLTag;
import exceptions.ProjectNotFoundException;

/**
 *
 */
public class DataModel
        implements DataChangeListener, Serializable
{
    public List <EmployeeDataSource> employees = new ArrayList();
    public List <ProjectDataSource> projects = new ArrayList();
    private ProjectDataSource pdsCurrent = null;
    private EmployeeDataSource edsCurrent = null;

    /**
     *
     */
    public DataModel()
    {
        EADesktop.getEventManager().removeListener( DataChangeListener.class, this );
        EADesktop.getEventManager().addListener( DataChangeListener.class, this );

    }

    /**
     *
     * @param dm
     */
    public DataModel( DataModel dm )
    {
        this.projects.retainAll( dm.projects );
    }



     /**
      *
      * @param dm
      */
     public void setData( DataModel dm )
     {
         this.projects.retainAll( dm.projects );
     }

    /**
     * Return the <code>ProjectDataSource</code> identified by its unique <code>id</code>.
     * @param id the unique identification number from the underlying Palm OS database.
     * @return a ProjectDataSource object if it is found, otherwise throws an exception.
     * @throws ProjectNotFoundException A runtime exception.
     * @see #hasProjectByID(int)
     */
    public ProjectDataSource getProject( int id )
    {

        for( int i = 0; i < projects.size(); i ++ )
        {
            if( projects.get(i).getProjectID() == id )
                return projects.get( i  );
        }
        throw new ProjectNotFoundException();
    }

    /**
     * Return the <code>ProjectDataSource</code> at location <code>index</code> in the List.<br>
     * @param index the index location of the List, 0-based.
     * @return a ProjectDataSourceObject
     * @see ProjectDataSource
     * @see java.util.List
     */
    public ProjectDataSource getProjectByIndex( int index )
    {
       assert( ( index >= 0 ) && (index < projects.size()) );
       return this.projects.get( index );
    }


    /**
     * Retrieves the Employee identified by <code>id</code>.<br>
     * @param id The id to use as a search key.
     * @return the EmployeeDataSource if it is found.
     */
    public EmployeeDataSource getEmployeeByID( int id )
    {
        EmployeeDataSource eds = new EmployeeDataSource();

        for (int i = 0; i < employees.size() ; i ++ )
        {
            if ( employees.get( i ).id == id )
                eds = employees.get( i );
        }

        return eds;
    }

    /**
     *
     * @param index
     * @return
     */
    public EmployeeDataSource getEmployeeByIndex ( int index )
    {
        assert( ( index >= 0 ) && (index < employees.size()) );
        return this.employees.get( index );
    }

    /**
     *
     * @return
     */
    public List<EmployeeDataSource> getEmployeeList()
    {
        return this.employees;
    }

    /**
     * Get an array of Strings containing the first names of all
     * employees across all projects.<br>
     * @return Object [] of Strings with all employees first names.
     */
    public Object [] getEmloyeeNameList()
    {
        List<String> l = new ArrayList();

        for ( int i = 0; i < employees.size(); i++ )
        {
            l.add( employees.get( i ).firstname  );
        }
        return l.toArray();
    }

    /**
     * Return all of this DataModel's <code>EmployeeDataSource</code> objects.<br>
     * They are returned as an Object [].<br>
     * @return Object []
     * @see EmployeeDataSource
     */
    public Object [] getEmployees()
    {
        return employees.toArray();
    }

    /**
     *
     * @param i
     * @param pds
     */
    public void setProject( int i, ProjectDataSource pds)
    {
        this.projects.set( i, pds );
    }

    /**
     * Returns the number of projects stored in the List of ProjectDataSource.<br>
     * @return
     */
    public int getNumberOfProjects()
    {
        if ( this.projects == null )
            return 0;

        return this.projects.size();
    }

    /**
     *
     */
    public void change( int iScreen, int iType, Object o )
    {
        if (( iType == SAVE ) && ( o instanceof lstProjects) )
        {
            ListModel lm = ((lstProjects)o).getModel();
            for( int i = 0; i < lm.getSize() ; i++ )
            {
                Object oo = lm.getElementAt( i );
                projects.add(new ProjectDataSource( (String) oo));
            }

        }
    }


    /**
     *
     * @return
     */
    public String toString()
    {
        String ot = "<";
        String ct = ">";
        String s = new String();
        XMLTag x = new XMLTag();


       s += ot + x.MENDARI + ct +
            ot + x.EXEXASSISTDATA + ct +
            ot + x.APPID + ct + "MND0";

       return s;
    }

    /**
     * Add a ProjectDataSource object to the data structure.<br>
     * @param pds the  ProjectDataSource to be added. Not modified.
     */
    public void addProject( ProjectDataSource pds )
    {

        projects.add( pds );
    }

    /**
     *
     * @param s
     * @return
     * @throws ProjectNotFoundException
     */
    public ProjectDataSource getProject(String s)
        throws ProjectNotFoundException
    {
        if ( s == null )
            throw new ProjectNotFoundException();

        for ( Iterator <ProjectDataSource> i = projects.iterator(); i.hasNext(); )
        {
            ProjectDataSource _p = i.next();
            if ( _p.getProjectName().compareTo(s) == 0  )
            {
                return _p;
            }
        }

        throw new ProjectNotFoundException();
    }

    /**
     * Iterates through and returns the entire list of Projects' names.<br>
     * @return
     */
    public Object [] getProjectNameList()
    {
        List<String> l = new ArrayList();

        for ( int i = 0; i < projects.size(); i++ )
        {
            l.add( projects.get( i ).getProjectName() );   
        }
        return l.toArray();
    }

    

    /**
     *
     * @return
     */
    public List <ProjectDataSource> getProjectDataSource()
    {
        return projects;
    }

    /**
     * Sets the currently selected Project.<br>
     * Used for saving and loading in-memory data.<br>
     * @param pds
     */
    public void setCurrentProject(ProjectDataSource pds)
    {
         pdsCurrent = pds;
    }

    /**
     * Sets the currently selected Employee.<br>
     * Used for saving and loading in-memory data.<br>
     * @param eds
     */
    public void setCurrentEmployee ( EmployeeDataSource eds)
    {
         edsCurrent = eds;
    }

    /**
     * Gets the currently selected Project.<br>
     * If no current project is selected, then null is returned.<br>
     * Used for saving and loading in-memory data.<br>
     * @return the current <code>ProjectDataSource</code>, otherwise null.
     */
    public ProjectDataSource getCurrentProject()
    {
         return pdsCurrent;
    }

    /**
     * Checks for the current <code>ProjectDataSource</code>.<br>
     * @return true if <code>ProjectDataSource</code> exists, false otherwise.
     */
    public boolean hasCurrentlySelectedProject()
    {
        return pdsCurrent != null;
    }

    /**
     * Gets the currently selected Employee.<br>
     * Used for saving and loading in-memory data.<br>
     * @return
     */
    public EmployeeDataSource getCurrentEmployee()
    {
         return edsCurrent;
    }


    /**
     * Deletes the current Employee from the data source.
     */
    public void deleteCurrentEmployee()
    {
        if ( edsCurrent != null  && employees != null )
        {
            if ( employees.contains( edsCurrent )  )
            {
                employees.remove( edsCurrent );
            }
        }
    }

    /**
     * Gets
     * @return
     */
    public Object [] getFilteredEmployees()
    {
        List<String> l = new ArrayList();

        for ( int i = 0; i < projects.size(); i++ )
        {
            //l.add( projects.get( i ));
        }

        return l.toArray();
    }

    /**
     * Returns an Object array of all of the employee's projects.<br>
     * Used to populate a JList.<br>
     * @param id
     * @return
     */
    public Object [] getListOfProjectsForEmployee( int id )
    {
        List <ProjectDataSource> lReturn = new ArrayList();

        if (  projects == null )
            return lReturn.toArray();

        for (  int i = 0; i < projects.size(); i ++ )
        {
            ProjectDataSource pds = projects.get( i );
            if ( pds.hasEmployee( id ) )
                lReturn.add( pds );
        }
        return lReturn.toArray();
    }

    /**
     * Returns an Object array of all of the employee's milestones.<br>
     * Used to populate a JList.<br>
     * @param id
     * @return  an Object array Milestone objects.
     * @see MilestoneDataSource
     * @see gui.lists.lstEmployeeMilestones
     *
     */
    public Object [] getListOfMilestonesForEmployee( int id )
    {
        List <MilestoneDataSource> lReturn = new ArrayList();
        if ( projects == null )
            return lReturn.toArray();

        for ( int i = 0; i < projects.size(); i ++ )
        {
            ProjectDataSource pds = projects.get( i );
            List <MilestoneDataSource> listMDS = pds.getMilestoneList();

            if ( listMDS == null )
                return lReturn.toArray();

            for ( int j = 0; j < listMDS.size(); j++ )
            {
                MilestoneDataSource mds = listMDS.get( j );
                if ( mds.isEmployeeAssigned( id ) )
                {
                    lReturn.add( mds );
                }
            }

        }

        return lReturn.toArray();
    }

    /**
     * Provides the next Employee ID for use within this Java App.<br>
     * This value is replaced/lost after being sent to the Palm database.<br>
     * When it returns back after synchronization from Palm, it will have a large
     * UInt32 value.<br>
     * The returned UInt32 value will be correct and permanent.<br>
     * @return an <code>int</code> between 0 thru 999.
     * @see DataModel#getNextProjectID_Java()
     */
    public int getNextEmployeeID_Java()
    {
        int iReturn = 0;
        for( int i = 0; i < employees.size(); i ++ )
        {
            int iBuffID = employees.get( i ).id;
            // a Palm record UInt32 is usually a very high number,
            // this desktop Java app will only deal with much smaller numbers.
            if( iBuffID  < 1000 )
            {
                if ( iReturn <= iBuffID )
                    iReturn = iBuffID + 1;
            }
        }

        return iReturn;
    }


    /**
     * Provides the next Project ID for use within this Java App.<br>
     * This value is replaced/lost after being sent to the Palm database.<br>
     * When it returns back after synchronization from Palm, it will have a large
     * UInt32 value.<br>
     * The returned UInt32 value will be correct and permanent.<br>
     * @return an <code>int</code> between 0 thru 999.
     */
    public int getNextProjectID_Java()
    {
        int iReturn = 0;
        int iHighNumber = 0;
        for( int i = 0; i < projects.size(); i ++ )
        {
            int iBuffID = projects.get(i).getProjectID();
            // a Palm record UInt32 is usually a very high number,
            // this desktop Java app will only deal with much smaller numbers.
            if( iBuffID  < 1000 )
            {
                if ( iHighNumber <= iBuffID )
                    iHighNumber = iBuffID + 1;
            }
        }

        iReturn = iHighNumber;
        return iReturn;
    }

    /**
     * Returns the total number of Employees across all projects.
     * @return  the number of distinct employees, 0 if none or the datastructure resolves to <code>null</code>.
     */
    public int getNumberOfEmployees()
    {
        if ( this.employees == null )
            return 0;

        return this.employees.size();
    }

    /**
     * Determines if a project identified by <code>id</code> exists in the Data Model.<br>
     *
     * @param id The unique ID of the project to search.
     * @return true if a project with <code>id</code> exists, false otherwise.
     * @see this#getProject(int)
     */
    public boolean hasProjectByID( int id )
    {
        boolean bReturn = false;
        for( int i = 0; i < projects.size(); i ++ )
        {
            if( projects.get(i).getProjectID() == id )
            {
                bReturn = true;
            }
        }
        return bReturn;
    }

    /**
     * Adds the <code>EmployeeDataSource</code> to the full list of employees.
     * Also adds it to the current project.  If there is no current project then the employee
     * will not be added to any of the projects.<br>
     * @param eds
     */
    public void addEmployee(EmployeeDataSource eds)
    {
        this.employees.add( eds );
        if ( this.getCurrentProject() != null )
            this.getCurrentProject().addEmployee( eds );

    }
}
