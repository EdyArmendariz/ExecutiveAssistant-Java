package gui.screens;

import events.DataChangeListener;
import events.DataChangeEvent;
import events.ScreenChangeListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import data.ProjectDataSource;
import data.DataModel;
import gui.EADesktop;
import com.developer.data.devDataHelper;

/**
 *
 */
public class pnlEditProject
        extends JFrame
        implements DataChangeListener
{
    ProjectDataSource myProject = new ProjectDataSource();

    JButton btnOK = new JButton( "OK" );
    JButton btnCancel = new JButton( "Cancel" );
    private JLabel lblName = new JLabel( "Name:" );
    private JLabel lblDescription = new JLabel( "Description:" );
    JTextField name = new JTextField( 20 );
    JTextArea description = new JTextArea( 5, 20 );
    private GridBagLayout lyt = new GridBagLayout();
    private GridBagConstraints con = new GridBagConstraints();

    /**
     * 
     */
    public pnlEditProject ()
    {
        super();
        EADesktop.getEventManager().removeListener( DataChangeListener.class, this );
        EADesktop.getEventManager().addListener( DataChangeListener.class, this );

        this.setTitle( "Edit Project" );
        this.setLocation( 300, 300 );
        this.setBounds( new Rectangle( 370, 200 ) );
        this.setLocationRelativeTo( null );
        this.setLayout( lyt );

        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 1;
        this.add( lblName, con );

        con.gridx = 1;
        con.gridy = 0;
        con.gridwidth = 2;
        name.setMinimumSize( new Dimension( 60, 15 ) );
        name.setBorder( BorderFactory.createLoweredBevelBorder() );
        this.add( name, con );

        con.gridx = 0;
        con.gridy = 1;
        con.gridwidth = 1;
        this.add( lblDescription, con );

        con.gridx = 1;
        con.gridy = 1;
        con.gridwidth = 2;
        description.setBorder ( BorderFactory.createLoweredBevelBorder() );
        description.setLineWrap ( true );
        this.add( description, con );

        con.gridx = 1;
        con.gridy = 3;
        con.gridwidth = 1;
        this.add( btnCancel, con );

        con.gridx = 2;
        con.gridy = 3;
        con.gridwidth = 1;
        this.add( btnOK, con );


        btnOK.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {

                devDataHelper ddh = devDataHelper.getDataHelper();
                DataModel dm = ddh.getDataModel();
                myProject.setProjectName( name.getText() );
                myProject.setDescription( description.getText() );
                EADesktop.getEventManager().fireEvent( DataChangeListener.class,
                        DataChangeListener.UPDATE,
                        new DataChangeEvent( dm, ScreenChangeListener.SHOW_PROJECT_LIST ) );
                EADesktop.getEventManager().fireEvent( DataChangeListener.class,
                        DataChangeListener.TITLE,
                        new DataChangeEvent( dm.getCurrentProject().getProjectName(), ScreenChangeListener.SHOW_PROJECT_MENU ) );

                Container c = btnOK.getRootPane().getParent();
                c.setVisible( false );
                ( ( JFrame ) c ).dispose();
            }
        } );

        btnCancel.addActionListener( new ActionListener()
        {
            public void actionPerformed ( ActionEvent ae )
            {
                Container c = btnCancel.getRootPane().getParent();
                c.setVisible( false );
                ( ( JFrame ) c ).dispose();
            }
        } );
    }

    public void change ( int iScreen, int iType, Object o )
    {
        if ( iType == DataChangeListener.UPDATE )
        {
            if ( o instanceof ProjectDataSource )
            {
                name.setText( ( ( ProjectDataSource ) o ).getProjectName() );
            }
        }
    }

    public void setProjectDataSource ( ProjectDataSource currentProject )
    {
        myProject = currentProject;
        name.setText( myProject.getProjectName() );
        description.setText( myProject.getProjectDescription() );
    }
}
