package io;

import data.DataModel;
import data.EmployeeDataSource;
import data.MilestoneDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;



/**
 *
 */
public class Save
{
   // private String DEFAULTPATH = "C:\\Program Files\\Mendari\\Executive Assistant\\system\\data";
   //  private String FILE = "dummy.xml";
    private String DEFAULTPATH = "C:\\Program Files\\Palm\\ArmendE\\ExecAsst";
    private String FILE = "execasst.dat";

    private DataModel objSaveMe;

    /**
     *
     */
    public Save()
    {

    }

    /**
     *
     * @param o
     */
    public void objectToSave(DataModel o)
    {
        objSaveMe = o;
    }

    /**
     *
     * @param f
     */
    public void save(File f)
    {
        int i = 0;
    }

    /**
     * Save to the default directory and file.<br>
     */
    public void save()
    {
        File f = new File(DEFAULTPATH + "\\" + FILE );
        try
        {
            XMLTag xml = new XMLTag();
            Document d = DocumentHelper.createDocument();
            Element root = d.addElement(xml.MENDARI);
            Element appname = root.addElement(xml.EXEXASSISTDATA);
            appname.addAttribute("version", "1.0");
            Element appid = appname.addElement(xml.APPID);
            appid.addText("MND0");
            Element rootprojectdb = appname.addElement(xml.PROJECTS_DB);
            Element database_name = rootprojectdb.addElement(xml.DATABASE_NAME);
            database_name.addText("projectMND0");

            for (int i = 0; i < objSaveMe.getNumberOfProjects(); i++)
            {
                try
                {
                    Element project = rootprojectdb.addElement(xml.PROJECT);
                    Element projectname = project.addElement(xml.NAME);
                    Element projectdesc = project.addElement(xml.DESCRIPTION);
                    Element pid = project.addElement(xml.PID);
                    Element uid = project.addElement(xml.UID);
                    Element attr = project.addElement(xml.ATTR);
                    projectname.addText(objSaveMe.getProjectByIndex(i).getProjectName());
                    projectdesc.addText(objSaveMe.getProjectByIndex(i).getProjectDescription());
                    pid.addText( new Integer ( objSaveMe.getProjectByIndex ( i ).getProjectID()).toString());
                    uid.addText( new Integer ( objSaveMe.getProjectByIndex ( i ).getUniqueID()).toString());
                    attr.addText( new Integer(objSaveMe.getProjectByIndex(i).getAttribute()).toString());
                   // uid.addText("10747905");
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }

            Element rootmilestones_db = appname.addElement(xml.MILESTONES_DB);
            Element databasename_milestone = rootmilestones_db.addElement(xml.DATABASE_NAME);
            databasename_milestone.addText("milestoneMND0");
            for (int i = 0; i < objSaveMe.getNumberOfProjects(); i++)
            {
                List<MilestoneDataSource> l = objSaveMe.getProjectByIndex(i).getMilestoneList();
                for (Iterator<MilestoneDataSource> iter = l.iterator(); iter.hasNext();)
                {
                    try
                    {
                        Element milestone = rootmilestones_db.addElement(xml.MILESTONE);
                        MilestoneDataSource mds = iter.next();
                        Element projectuid = milestone.addElement(xml.ID);
                        projectuid.addText(new Integer(mds.getID()).toString());

                        Element projectid = milestone.addElement(xml.PID );
                        projectid.addText(new Integer(mds.getProjectID()).toString());

                        Element milestonename = milestone.addElement(xml.NAME);
                        milestonename.addText(mds.getName());
                        Element milestonedesc = milestone.addElement( "desc" );
                        milestonedesc.addText(mds.getDescription());
                        Element milestonexpos = milestone.addElement(xml.XPOS);
                        milestonexpos.addText(new Integer((mds.getPosition().x/3)).toString());
                        Element milestoneypos = milestone.addElement(xml.YPOS);
                        milestoneypos.addText(new Integer((mds.getPosition().y/3)).toString());

                        Element milestonestatus = milestone.addElement(xml.STATUS);
                        milestonestatus.addText(new Integer(mds.getStatus()).toString());

                        Element milestonetopsib = milestone.addElement(xml.TOP_SIB);
                        milestonetopsib.addText(new Integer(mds.getSiblingUniqueID(MilestoneDataSource.TOP_SIBLING)).toString());
                        Element milestonerightsib = milestone.addElement(xml.RIGHT_SIB);
                        milestonerightsib.addText(new Integer(mds.getSiblingUniqueID(MilestoneDataSource.RIGHT_SIBLING)).toString());
                        Element milestonebottomsib = milestone.addElement(xml.BOTTOM_SIB);
                        milestonebottomsib.addText(new Integer(mds.getSiblingUniqueID(MilestoneDataSource.BOTTOM_SIBLING)).toString());
                        Element milestoneleftsib = milestone.addElement(xml.LEFT_SIB);
                        milestoneleftsib.addText(new Integer(mds.getSiblingUniqueID(MilestoneDataSource.LEFT_SIBLING)).toString());

                        Element milestoneEmp0 = milestone.addElement(xml.EMP1);
                        milestoneEmp0.addText(new Integer(mds.getEmpID(0)).toString());
                        Element milestoneEmp1 = milestone.addElement(xml.EMP2);
                        milestoneEmp1.addText(new Integer(mds.getEmpID(1)).toString());
                        Element milestoneEmp2 = milestone.addElement(xml.EMP3);
                        milestoneEmp2.addText(new Integer(mds.getEmpID(2)).toString());
                         Element milestoneEmp3 = milestone.addElement( xml.EMP4 );
                        milestoneEmp3.addText(new Integer(mds.getEmpID(3)).toString());
                        Element milestoneAttr = milestone.addElement( xml.ATTR );
                        milestoneAttr.addText( new Integer ( mds.getAttribute() ).toString() );
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                }
            }

            Element rootemployees_db = appname.addElement( xml.EMPLOYEES_DB );
            Element databasename_employee = rootemployees_db.addElement(xml.DATABASE_NAME);
            databasename_employee.addText( "employeeMND0");

            List<EmployeeDataSource> l = objSaveMe.getEmployeeList();
            for (Iterator<EmployeeDataSource> iter = l.iterator(); iter.hasNext();)
            {
                try
                {
                    Element employee = rootemployees_db.addElement(xml.EMPLOYEE);
                    EmployeeDataSource eds = iter.next();
                    Element empid = employee.addElement(xml.ID);
                    empid.addText(new Integer(eds.id).toString());
                    Element empPID0 = employee.addElement( xml.PID0 );
                    empPID0.addText( new Integer( eds.getProjectID0() ).toString() );
                    Element empPID1 = employee.addElement( xml.PID1 );
                    empPID1.addText( new Integer( eds.getProjectID1() ).toString() );
                    Element empPID2 = employee.addElement( xml.PID2 );
                    empPID2.addText( new Integer( eds.getProjectID2() ).toString() );
                    Element empPID3 = employee.addElement( xml.PID3 );
                    empPID3.addText( new Integer( eds.getProjectID3() ).toString() );

                    Element empfname = employee.addElement(xml.FNAME);
                    empfname.addText(eds.firstname);
                    Element emplname = employee.addElement(xml.LNAME);
                    emplname.addText(eds.lastname);
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }

            FileOutputStream fos = new FileOutputStream(f);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(d.asXML().getBytes());
            bos.flush();
            bos.close();
            fos.close();
        } catch (java.io.FileNotFoundException fnfe)
        {

        } catch (java.io.IOException ioe)
        {

        }
        catch ( java.lang.NullPointerException npe )
        {
            System.out.println( npe.getMessage() ); 
        }
        finally
        {

        }
    }
}

/*
XMLTag xml = new XMLTag();
        Document d = DocumentHelper.createDocument();
        Element root = d.addElement( xml.MENDARI );
        Element appname = root.addElement( xml.EXEXASSISTDATA );
                appname.addAttribute("version", "1.0");
        Element appid =  appname.addElement( xml.APPID );
        appid.addText( "MND0");
        Element  rootprojectdb = appname.addElement( xml.PROJECTS_DB );
        Element database_name = rootprojectdb.addElement( xml.DATABASE_NAME );
        database_name.addText( "projectMND0" );
        Element project = rootprojectdb.addElement( xml.PROJECT );
        Element projectname = project.addElement( xml.PROJECT_NAME );
        Element pid = project.addElement( xml.PID0 );
        Element uid = project.addElement( xml.UID );
        projectname.addText( "EP Scheduling");
        pid.addText( "1" );
        uid.addText( "10747905" );

        Element rootmilestones_db = appname.addElement( xml.MILESTONES_DB );
        Element databasename_milestone = rootmilestones_db.addElement( xml.DATABASE_NAME );
        databasename_milestone.addText( "milestoneMND0");
        Element milestone = rootmilestones_db.addElement( xml.MILESTONE );
        Element milestonename = milestone.addElement(xml.NAME );
        milestonename.addText( "JDK 1.5");
*/