package com.developer.data;

import data.DataModel;
import data.ProjectDataSource;
import data.MilestoneDataSource;
import events.DataChangeEvent;
import events.DataChangeListener;
import gui.EADesktop;
import io.Load;

/**
 * Created by IntelliJ IDEA.
 * User: Edy
 * Date: Mar 6, 2007
 * Time: 7:30:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class devDataHelper
{
    private static devDataHelper dataHelper;
    private DataModel dm = new DataModel();


    private void initDefaultDataModel()
    {
        ProjectDataSource pds = new ProjectDataSource();
        pds.setProjectName( "EP Scheduling");
        pds.setDescription(  "Best Boxed Product");
        dm.addProject( pds );

        pds = new ProjectDataSource();
        pds.setProjectName( "Wedding");
        dm.addProject( pds );

        pds = new ProjectDataSource();
        pds.setProjectName( "Cash Flow");
        dm.addProject( pds );

        pds = new ProjectDataSource();
        pds.setProjectName( "Global Vista");
        dm.addProject( pds );

        pds = new ProjectDataSource();
        pds.setProjectName( "EP Budgeting");
        dm.addProject( pds );

        pds = new ProjectDataSource();
        pds.setProjectName( "Project 5");
        dm.addProject( pds );

        pds = new ProjectDataSource();
        pds.setProjectName( "Project 6");
        dm.addProject( pds );

        pds = new ProjectDataSource();
        pds.setProjectName( "Project 7");
        dm.addProject( pds );
      // --------------------------------------------------------
        pds = dm.getProject( "EP Scheduling" );
        MilestoneDataSource mds = new MilestoneDataSource();
        mds.setName( "eps Milestone");
        mds.setDescription( "do this eps stone");
        pds.addMilestone( mds );

        pds = dm.getProject("Wedding");
        mds = new MilestoneDataSource();
        mds.setName( "wed Mileston");
        mds.setDescription( "do this wedding stone");
        pds.addMilestone( mds );

        pds = dm.getProject("Cash Flow");
        mds = new MilestoneDataSource();
        mds.setName( "cash Milestone");
        mds.setDescription( "do this cash stone");
        pds.addMilestone( mds );

        pds = dm.getProject("Global Vista");
        mds = new MilestoneDataSource();
        mds.setName( "gv Milestone");
        mds.setDescription( "do this globalV stone");
        pds.addMilestone( mds );

        pds = dm.getProject("EP Budgeting");
        mds = new MilestoneDataSource();
        mds.setName( "epb Milestone");
        mds.setDescription( "do this epb stone");
        pds.addMilestone( mds );

        pds = dm.getProject("Project 5");
        mds = new MilestoneDataSource();
        mds.setName( "p5 Milestone");
        mds.setDescription( "do this p5 stone");
        pds.addMilestone( mds );

        pds = dm.getProject("Project 6");
        mds = new MilestoneDataSource();
        mds.setName( "p6 Milestone");
        mds.setDescription( "do this p6 stone");
        pds.addMilestone( mds );

    }


    private devDataHelper()
    {
        //initDefaultDataModel();     
    }


    public DataModel loadDefeaultDataModel()
    {
        return dm;
    }


    public DataModel getDataModel()
    {
        return dm;
    }

    public static synchronized devDataHelper getDataHelper()
    {
        if ( dataHelper == null )
            dataHelper = new devDataHelper();
        return dataHelper;
    }



}
