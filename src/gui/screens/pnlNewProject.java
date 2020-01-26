package gui.screens;

import events.DataChangeListener;
import events.DataChangeEvent;
import events.ScreenChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.developer.data.devDataHelper;
import data.DataModel;
import data.ProjectDataSource;
import gui.EADesktop;
import io.XMLTag;

/**
 *
 */
public class pnlNewProject
        extends pnlEditProject
        implements DataChangeListener
{

    public pnlNewProject ()
    {
        super();
        this.setTitle( "New Project ..." );


        btnOK.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                devDataHelper ddh = devDataHelper.getDataHelper();
                DataModel dm = ddh.getDataModel();
                ProjectDataSource myp = new ProjectDataSource();
                myp.setProjectID( dm.getNextProjectID_Java() );
                myp.setProjectName( name.getText() );
                myp.setDescription( description.getText() );
                dm.addProject( myp );
                dm.setCurrentProject( myp );
                EADesktop.getEventManager().fireEvent( DataChangeListener.class,
                        DataChangeListener.UPDATE,
                        new DataChangeEvent( dm, ScreenChangeListener.SHOW_PROJECT_LIST ) );
                EADesktop.getEventManager().fireEvent( DataChangeListener.class,
                        DataChangeListener.TITLE,
                        new DataChangeEvent( dm.getCurrentProject().getProjectName(), ScreenChangeListener.SHOW_PROJECT_MENU ) );
                myp.setAttribute ( XMLTag.HH_ATTR_NEW );
                Container c = btnOK.getRootPane().getParent();
                c.setVisible( false );
                ( ( JFrame ) c ).dispose();
            }
        } );
    }

    public void change ( int iScreen, int iType, Object o )
    {
    }


}
