package io;

import com.developer.data.devDataHelper;
import data.DataModel;
import data.EmployeeDataSource;
import data.MilestoneDataSource;
import data.ProjectDataSource;
import events.DataChangeEvent;
import events.DataChangeListener;
import events.ScreenChangeListener;
import exceptions.DataModelException;
import gui.EADesktop;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Loads data from the XML file into RAM memory.<br>
 */
public class Load
{
    // Path to the Mendari Desktop installation.
    // private String DEFAULTPATH = "C:\\Program Files\\Mendari\\Executive Assistant\\system\\data";

    // Path to the Mendari Palm installation.
    private String DEFAULTPATH = "C:\\Program Files\\Palm\\ArmendE\\ExecAsst";

    // Name of the development test file.
    // private String DEFAULTFILE = "dummy.xml";

    private String DEFAULTFILE = "execasst.dat";
    private Object objData;

    /**
     *
     */
    public Load()
    {

    }

    /**
     * Loads data from the XML file into the <code>DataModel</code> object.
     * @return <code>DataModel</code> cast as an <code>Object</code> data type.
     */
    public Object load()
    {
        XMLTag xml = new XMLTag();
        devDataHelper ddh = devDataHelper.getDataHelper();
        DataModel dm = ddh.getDataModel();
        try
        {
            File f = new File(DEFAULTPATH + "\\" + DEFAULTFILE);
            FileInputStream fis = new FileInputStream(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bReadByte = new byte[8];
            int iReadByte = -1;
            iReadByte = fis.read(bReadByte);

            while (iReadByte != -1)
            {
                baos.write(bReadByte, 0, iReadByte);
                iReadByte = fis.read(bReadByte);
            }

            String strContents = baos.toString();
            Document doc = DocumentHelper.parseText(strContents);
            Element elmRoot = doc.getRootElement();

            if (elmRoot.getName().compareTo(xml.MENDARI) != 0)
                throw new DataModelException();

            elmRoot.getName();
            Element elExecAsst = elmRoot.element("execassistdata");
            Element elProjects = elExecAsst.element("projects_db");
            List<Element> lel = elProjects.content();
            for (int ii = 0; ii < lel.size(); ii++)
            {
                Element eee = lel.get(ii);
                if (eee.getName().compareTo(xml.PROJECT) == 0)
                {
                    try
                    {
                        ProjectDataSource pds = new ProjectDataSource();
                        pds.setProjectID(new Integer(eee.element(xml.PID).getText()).intValue());
                        pds.setUniqueID(new Integer(eee.element(xml.UID).getText()).intValue());
                        pds.setProjectName(eee.element(xml.NAME).getText());
                        pds.setDescription(eee.element(xml.DESCRIPTION).getText());
                       try {  pds.setAttribute( new Integer( eee.element(xml.ATTR).getText()).intValue());   }
                       catch( Exception e ) { pds.setAttribute( XMLTag.HH_ATTR_NEW ); }
                        dm.addProject(pds);
                    } catch (Exception e)
                    {
                        System.out.println( "Error in reading ProjectDataSource" + e.getMessage());
                    }
                }
            }


            Element elMilestones = elExecAsst.element("milestones_db");
            Element elEmployees = elExecAsst.element("employees_db");


            // -------------------------------------------------------------------
            // Old iteration code.
            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            for (Iterator<Element> it = elmRoot.elementIterator(); it.hasNext();)
            {
                // I now have  'appid',  'projects_db', 'milestones_db'.
                // Iteration is not required, simply invoke  elmChild.element('somename')
                //String strName = null;
                Element elmChild = it.next();
                List<Element> l = elmChild.content();

                for (int i = 0; i < l.size(); i++)
                {
                    Element ee = l.get(i);

                    //  if (ee.getName().compareTo(xml.APPID) == 0)
                    //  {

                    //  }

                    // 'projects_db'
                    /* if (ee.getName().compareTo(xml.PROJECTS_DB) == 0)
                     {
                         List<Element> ll = ee.content();
                         for (int ii = 0; ii < ll.size(); ii++)
                         {
                             Element eee = ll.get(ii);
                             if (eee.getName().compareTo(xml.PROJECT) == 0)
                             {
                                 try
                                 {
                                     ProjectDataSource pds = new ProjectDataSource();
                                     pds.setProjectID(new Integer(eee.element(xml.PID).getText()).intValue());
                                     pds.setProjectName(eee.element(xml.NAME).getText());
                                     pds.setDescription(eee.element(xml.DESCRIPTION).getText());
                                     dm.addProject(pds);
                                 } catch (Exception e)
                                 {
                                     System.out.println(e.getMessage());
                                 }
                             }
                         }
                     } */

                    if (ee.getName().compareTo(xml.MILESTONES_DB) == 0)
                    {
                        List<Element> ll = ee.content();

                        for (int ii = 0; ii < ll.size(); ii++)
                        {
                            Element eee = ll.get(ii);
                            if (eee.getName().compareTo(xml.MILESTONE) == 0)
                            {
                                try
                                {
                                    MilestoneDataSource mds = new MilestoneDataSource();
                                    mds.setProjectID(new Integer(eee.element(xml.PID).getText()).intValue());
                                    Integer iBuff = new Integer(eee.element(xml.ID).getText()).intValue();

                                    if (iBuff.intValue() == 0)
                                        iBuff = new Integer(dm.getProject(mds.getProjectID()).getNextHighestMilestoneID());

                                    mds.setID(iBuff);
                                    mds.setName(eee.element(xml.NAME).getText());
                                    mds.setDescription(eee.element("desc").getText());
                                    Point ptPoint = new Point();
                                    ptPoint.x = new Integer(eee.element(xml.XPOS).getText());
                                    ptPoint.y = new Integer(eee.element(xml.YPOS).getText());

                                    // Scale up from mini-screen
                                    ptPoint.x = 3 * ptPoint.x;
                                    ptPoint.y = 3 * ptPoint.y;

                                    mds.setPosition(ptPoint);
                                    mds.setStatus(new Integer(eee.element(xml.STATUS).getText()).intValue());
                                    mds.setSiblingUniqueID(MilestoneDataSource.TOP_SIBLING, new Integer(eee.element(xml.TOP_SIB).getText()));
                                    mds.setSiblingUniqueID(MilestoneDataSource.RIGHT_SIBLING, new Integer(eee.element(xml.RIGHT_SIB).getText()));
                                    mds.setSiblingUniqueID(MilestoneDataSource.BOTTOM_SIBLING, new Integer(eee.element(xml.BOTTOM_SIB).getText()));
                                    mds.setSiblingUniqueID(MilestoneDataSource.LEFT_SIBLING, new Integer(eee.element(xml.LEFT_SIB).getText()));
                                    mds.setEmployeeID(0, new Integer(eee.element(xml.EMP1).getText()).intValue());
                                    mds.setEmployeeID(1, new Integer(eee.element(xml.EMP2).getText()).intValue());
                                    mds.setEmployeeID(2, new Integer(eee.element(xml.EMP3).getText()).intValue());
                                    mds.setEmployeeID(3, new Integer(eee.element(xml.EMP4).getText()).intValue());

                                    //System.out.println("adding milestone " + mds.getName());
                                    // add the Milestone to the Project
                                    dm.getProject(mds.getProjectID()).addMilestone(mds);
                                } catch (Exception e)
                                {
                                    System.out.println( "Error in reading MilestoneDataSource" + e.getMessage());
                                }

                            }
                        }

                    }
                    if (ee.getName().compareTo(xml.EMPLOYEES_DB) == 0)
                    {
                        List<Element> ll = ee.content();
                        for (int ii = 0; ii < ll.size(); ii++)
                        {
                            Element eee = ll.get(ii);
                            if (eee.getName().compareTo(xml.EMPLOYEE) == 0)
                            {
                                try
                                {
                                    EmployeeDataSource eds = new EmployeeDataSource();
                                    try
                                    {
                                        eds.id = new Integer(eee.element(xml.ID).getText()).intValue();
                                    } catch (Exception e)
                                    {
                                        System.out.println( "Error in reading EmployeeDataSource" + e.toString());
                                    }
                                    try
                                    {
                                        eds.setProjectID0(new Integer(eee.element(xml.PID0).getText()).intValue());
                                    } catch (Exception e)
                                    {
                                        System.out.println( "Error in reading EmployeeDataSource" + e.toString());
                                    }
                                    try
                                    {
                                        eds.setProjectID1(new Integer(eee.element(xml.PID1).getText()).intValue());
                                    } catch (Exception e)
                                    {
                                        System.out.println( "Error in reading EmployeeDataSource" + e.toString());
                                    }
                                    try
                                    {
                                        eds.setProjectID2(new Integer(eee.element(xml.PID2).getText()).intValue());
                                    } catch (Exception e)
                                    {
                                        System.out.println( "Error in reading EmployeeDataSource" + e.toString());
                                    }
                                    try
                                    {
                                        eds.setProjectID3(new Integer(eee.element(xml.PID3).getText()).intValue());
                                    } catch (Exception e)
                                    {
                                        System.out.println( "Error in reading EmployeeDataSource" + e.toString());
                                    }
                                    try
                                    {
                                        eds.firstname = eee.element(xml.FNAME).getText();
                                    } catch (Exception e)
                                    {
                                        System.out.println( "Error in reading EmployeeDataSource" + e.toString());
                                    }
                                    try
                                    {
                                        eds.lastname = eee.element(xml.LNAME).getText();
                                    } catch (Exception e)
                                    {
                                        System.out.println( "Error in reading EmployeeDataSource" + e.toString());
                                    }
                                    try
                                    {
                                        dm.addEmployee(eds);
                                    } catch (Exception e)
                                    {
                                        System.out.println( "Error in reading EmployeeDataSource" + e.toString());
                                    }
                                    /*try
                                    {
                                        eee.element( "NotFound" );
                                    }
                                    catch( Exception e )
                                    {
                                        System.out.println( e.toString() );
                                    }  */
                                } catch (Exception e)
                                {
                                    System.out.println( "Error in reading EmployeeDataSource" + e.toString());
                                }
                            }
                        }
                    }
                }

            }

            fis.close();
            baos.close();

            // For each project, attach the siblings.
            for (int idx = 0; idx < dm.getNumberOfProjects(); idx++)
            {
                ProjectDataSource pds = dm.getProjectByIndex(idx);
                pds.processSiblings();
            }

            // For each project, assign it's employees.
            for (int idx = 0; idx < dm.getNumberOfProjects(); idx++)
            {
                ProjectDataSource pds = dm.getProjectByIndex(idx);

                for (int jdx = 0; jdx < dm.getNumberOfEmployees(); jdx++)
                {
                    EmployeeDataSource eds = dm.getEmployeeByIndex(jdx);
                    /** TODO find the employee's project id numbers and add them to the Project's list
                     if they match up.   **/
                    if (eds.isAssignedToProject(pds.getProjectID()))
                        pds.addEmployee(eds);

                }
            }


        } catch (exceptions.DataModelException dme)
        {

        } catch (java.io.FileNotFoundException fnfe)
        {

        } catch (java.io.IOException ioe)
        {

        } catch (org.dom4j.DocumentException de)
        {

        }

        EADesktop.getEventManager().fireEvent(DataChangeListener.class,
                DataChangeListener.UPDATE,
                new DataChangeEvent(dm, ScreenChangeListener.SHOW_PROJECT_LIST));

        EADesktop.getEventManager().fireEvent(DataChangeListener.class,
                DataChangeListener.UPDATE,
                new DataChangeEvent(dm, ScreenChangeListener.SHOW_EMPLOYEE_LIST));
        EADesktop.myData = dm;
        return objData = dm;
    }

    /**
     * @return
     */
    public Object getData()
    {
        return objData;
    }


}
