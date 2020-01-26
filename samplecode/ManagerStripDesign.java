package com.bulloch.scheduler.manager;

import com.bulloch.scheduler.EPIO.StripDesignIO;
import com.bulloch.scheduler.EPIO.StripDesignIOLayouts;
import com.bulloch.scheduler.PrintGUI.Design;
import com.bulloch.scheduler.PrintGUI.GUIDesigner;
import com.bulloch.scheduler.PrintGUI.GUIDesignerDialog;
import com.bulloch.scheduler.PrintGUI.GUIDesignerDialogListener;
import com.bulloch.scheduler.StripDemo.StripDesign;
import com.bulloch.scheduler.StripDemo.StripToolBar;
import com.bulloch.scheduler.StripDemo.StripsPanel;
import com.bulloch.scheduler.category.SimpleFileFilter;
import com.bulloch.scheduler.category.ToolbarState;
import com.bulloch.scheduler.sched.ScheduleFileHelper;
import com.bulloch.scheduler.sched.SchedulerApp;
import com.bulloch.scheduler.sched.SchedulerConstants;
import com.bulloch.scheduler.util.GenerateObjID;
import com.bulloch.scheduler.util.Global;
import com.epservices.apps.Core.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.tree.TreePath;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/*
 ****************************************************************************************
 *  File Name       : ManagerStripDesign.java
 *  Author          : ksomasundaram
 *  Project         : E P - S C H E D U L I N G
 *  Created Date    : Oct 16, 2003 Time: 11:42:16 AM
 *
 *  Revisions History:
 *  -------------------------------------------------------------------------------------
 *    Version  Build#  Date          Author        Issue#     Comments/Issue Description
 *  -------------------------------------------------------------------------------------
 *    4.00     154     Nov 9, 2004   mnoohu        NONE       Code Reformatted.
 *
 *
 *  -------------------------------------------------------------------------------------
 *  Copyright (c) 2004 Entertainment Partners. All Rights Reserved.
 *
 ****************************************************************************************
 */


public class ManagerStripDesign
    extends Manager
    implements GUIDesignerDialogListener, SchedulerConstants, PropertyChangeListener,
    TableModelListener
{
	private static final String VERTICAL_NODE = "Vertical";
	private static final String HORIZONTAL_NODE = "Horizontal";
	private static final String STRIP_DESIGNS_NODE = "Strip Designs";

	public static final String NEW_STRIP_DESIGN = "New Strip Layout";
	public static final String OPEN_STRIP_DESIGN = "Edit Strip Layout";
	public static final String COPY_STRIP_DESIGN = "Duplicate Strip Layout";
	public static final String DELETE_STRIP_DESIGN = "Delete Strip Layout";

	public static final String NO_STRIP_SELECTED = "ManagerStripDesign:NoStripSelected";
	public static final String STRIP_SELECTED = "ManagerStripDesign:StripSelected";
	public static final String EVT_SELECT_STRIP_DESIGN = "ManagerStripDesign:SelectStripDesign";

	private static final String STRIP_LAYOUT_DESIGNER_WINDOW = "StripLayoutDesignerWindow";

	private ManagerNode rootNode = null;


	public ManagerStripDesign( final ManagerFrame parent )
	{
		super( parent );
		init();
	}


	//---------------------------------------------------------------------------
	private void init()
	{
		// root node
		rootNode = addNode( STRIP_DESIGNS_NODE, null, null );
		addNode( VERTICAL_NODE, null, rootNode );
		addNode( HORIZONTAL_NODE, null, rootNode );

		// set up the table model
		final String[] colNames = {"Name", "Orientation", "Height", "Width", ACTIVE_COL_NAME};
		tableModel = new ManagerStripDesignTableModel( colNames );

        // Add Fonts for Multi-Language Character Support.
        if( SchedulerApp.EnableAppFont)
            table.setFont( SchedulerApp.appFont );
        
		table.setModel( tableModel );

		nodeSelected( rootNode );
		tree.setSelectionPath( new TreePath( rootNode ) );
		Global.hideColumn( table, 0 );

		tablePopupMenu.add( new JMenuItem( new ManagerActions( ManagerActions.RENAME,
		                                                       "Rename Strip Layout", this ) ) );
		initTablePopupMenuText();

		// Allow the tree to be visible in the spliter window.
		enableTree( true );

		setUpListeners();
	}


	/**
	 * Refreshes the rows seen in the Strips Layout window.<br>
	 * This method should be invoked after importing or deleting
	 * a Strips Layout report.<br> <br>
	 *
	 *
	 * <font size=-1>
	 * To view the Strips Layout window ... <br>
	 * Go to the menu bar.<br>
	 * Then  <b>Design > Strips</b> Layout.<br>
	 * Or hit [ctrl]+M on the keybord.<br>
	 * </font>
	 * @see com.bulloch.scheduler.manager.ManagerStripDesign#init()
	 */
	private void initQuickie()
	{
		// root node
		rootNode = addNode( STRIP_DESIGNS_NODE, null, null );
		addNode( VERTICAL_NODE, null, rootNode );
		addNode( HORIZONTAL_NODE, null, rootNode );

		// set up the table model
		final String[] colNames = {"Name", "Orientation", "Height", "Width", ACTIVE_COL_NAME};
		tableModel = new ManagerStripDesignTableModel( colNames );

        // Add Fonts for Multi-Language Character Support.
        if( SchedulerApp.EnableAppFont)
            table.setFont( SchedulerApp.appFont );

		table.setModel( tableModel );

		nodeSelected( rootNode );
		tree.setSelectionPath( new TreePath( rootNode ) );
		Global.hideColumn( table, 0 );

		// Allow the tree to be visible in the spliter window.
		enableTree( true );

		setUpListeners();
	}

	public void setUpListeners()
	{
		// Remove first so that we don't add another
		// one if this has been called before.
		tableModel.removeTableModelListener( this );
		tableModel.addTableModelListener( this );
		setUpStripBoardListeners();
	}


	//---------------------------------------------------------------------------
	private void initTablePopupMenuText()
	{
		// Tree Popup menu items
		miNew.setText( NEW_STRIP_DESIGN );

		// Table Popup menu items
		miEdit.setText( OPEN_STRIP_DESIGN );
		miDuplicate.setText( COPY_STRIP_DESIGN );
		miDelete.setText( DELETE_STRIP_DESIGN );
	}


	private static String getReportDesignSavedName( final String name )
	{
		return STRIP_LAYOUT_DESIGNER_WINDOW + ":" + name;
	}


	/**
	 * If there is a multi-selection, then the first id is first row selected and last id is the last row selected.
	 * <p/>
	 * If only one row is selected, then the first and last rows will be the same.
	 *
	 * @param firstIndex
	 * @param lastIndex
	 */
	public void tableRowSelected( final int firstIndex, final int lastIndex )
	{
		firePropertyChange( ManagerStripDesign.STRIP_SELECTED, null, configureButtonStates() );
	}


	public void nodeSelected( final ManagerNode node )
	{
		final ManagerStripDesignTableModel model = ( ManagerStripDesignTableModel ) tableModel;
		ArrayList data = null;

		selectedNode = node;

		// If they select the root node, disable the currently selected row.
		table.clearSelection();

		final ToolbarState toolbarButtons = configureButtonStates();

		// Did they pick the root category node,
		// the normal custom category node,
		// the special built-in category node?
		if ( null == selectedNode )
		{
			model.setData( null );

			// The old and new values must be different, or we don't fire.
			firePropertyChange( ManagerStripDesign.NO_STRIP_SELECTED, null, toolbarButtons );
		}
		else
		{
			if ( node.nodeName.equals( VERTICAL_NODE ) )
			{
				data = model.getStripDesignsByOrientation( StripsPanel.VERTICAL );
			}
			else if ( node.nodeName.equals( HORIZONTAL_NODE ) )
			{
				data = model.getStripDesignsByOrientation( StripsPanel.HORIZONTAL );
			}
			else if ( node.nodeName.equals( STRIP_DESIGNS_NODE ) )
			{
				data = model.getStripDesignsByOrientation( -1 );
			}

			model.setData( data );

			// The old and new values must be different, or we don't fire.
			firePropertyChange( ManagerStripDesign.STRIP_SELECTED, null, toolbarButtons );
		}
	}


	/**
	 * Based on what we know, determine the toolbarbuttons state.
	 *
	 * @return
	 */
	private ToolbarState configureButtonStates()
	{
		final ToolbarState toolbarButtons = new ToolbarState();
		toolbarButtons.setEnableNewButton( true );

		if ( null != selectedNode )
		{
			final int selectedRow = table.getSelectedRow();
			final int numSelectedRows = table.getSelectedRows().length;

			toolbarButtons.setEnableOpenButton( 1 == numSelectedRows && selectedRow >= 0 );
			toolbarButtons.setEnableCopyButton( 1 == numSelectedRows && selectedRow >= 0 );
			toolbarButtons.setEnableDeleteButton( 1 == numSelectedRows && selectedRow >= 0 );
			toolbarButtons.setEnableExportButton( numSelectedRows >= 1 && selectedRow >= 0 );
		}

		return toolbarButtons;
	}


	public void addTableRow()
	{
		if ( selectedNode != null )
		{
			StripDesign stripDesign = null;
			if ( selectedNode.nodeName.equals( VERTICAL_NODE ) )
			{
				stripDesign = new StripDesign( StripsPanel.VERTICAL );
			}
			else if ( selectedNode.nodeName.equals( HORIZONTAL_NODE ) ||
			    selectedNode.nodeName.equals( STRIP_DESIGNS_NODE ) )
			{
				stripDesign = new StripDesign( StripsPanel.HORIZONTAL );
			}

			final String result =
			    showNewDesignDialog( GUIDesigner.STRIP_DESIGNER, "New Strip Design" );
			if ( result == null )
			{
				return; // OK button
			}
			stripDesign.name = result;

			final GUIDesignerDialog guiDesignerDialog = new GUIDesignerDialog( stripDesign, this, getReportDesignSavedName( result ) );
			guiDesignerDialog.setDirty( true );
			guiDesignerDialog.addListener( this );

			// always call this after obtaining the internalFrame
			addFrame( stripDesign.getID(), guiDesignerDialog );
		}
	}


	public void editTableRow()
	{
		// always call this first
		if ( isTableRowOpened() )
		{
			return;
		}

		final int selectedRowIndex = table.getSelectedRow();
		if ( selectedRowIndex != -1 )
		{
			final StripDesign stripDesign = ( StripDesign ) tableModel.getDataObject( selectedRowIndex );
			final StripDesign stripDesignCopy = ( StripDesign ) stripDesign.clone();
			stripDesignCopy.setID( stripDesign.getID() );
			final GUIDesignerDialog guiDesignerDialog = new GUIDesignerDialog( stripDesignCopy, this,
			                                                                   getReportDesignSavedName( stripDesignCopy.getName() ) );
			guiDesignerDialog.addListener( this );

			// always call this after obtaining the internalFrame
			addFrame( stripDesign.getID(), guiDesignerDialog );
		}
	}


	public void copyTableRow()
	{
		final int selectedRowIndex = table.getSelectedRow();
		if ( selectedRowIndex != -1 )
		{
			final StripDesign stripDesign = ( StripDesign ) tableModel.getDataObject( selectedRowIndex );
			final StripDesign stripDesignCopy = ( StripDesign ) stripDesign.clone();
			stripDesignCopy.setID( GenerateObjID.generateObjID() );
			stripDesignCopy.name = "Copy of " + stripDesign.name;

			final String result =
			    showNewDesignDialog( GUIDesigner.STRIP_DESIGNER, stripDesignCopy.name );
			if ( result == null )
			{
				return; // OK button
			}
			stripDesignCopy.name = result;

			final GUIDesignerDialog guiDesignerDialog = new GUIDesignerDialog( stripDesignCopy, this, getReportDesignSavedName( result ) );
			guiDesignerDialog.setDirty( true );
			guiDesignerDialog.addListener( this );

			// always call this after obtaining the internalFrame
			addFrame( stripDesignCopy.getID(), guiDesignerDialog );

			updateCurrentStripDesign();
		}
	}


	/**
	 * Exports a Strip Layout to a file. <br>
	 * The file has a .epsdL extension. <br>
	 * Includes encryption to protect the output file.<br>
	 *
	 * <br>
	 * <font size=-1>
	 * MMS *.stp files are <b>not</b> supported.
	 * </font>
	 * <br>
	 * @see PBEKeySpec
	 * @see PBEParameterSpec
	 * @see SecretKeyFactory
	 * @see Cipher
	 */
	final public void exportTableRow()
		throws java.io.FileNotFoundException
	{
		Document doc = null;
		StripDesignIO sdio = new StripDesignIO();
		Cipher pbeCipher = sdio.getEncryptionCipher();
		ScheduleFileHelper sfh = SchedulerApp.getScheduleFileHelper();
		File fDirectory = sfh.getRecentReportLayoutDirectory();
		List listReadOnly = new ArrayList();
		List listOverWrite = new ArrayList();
		int counter = table.getSelectedRows().length;
		int [] arr = table.getSelectedRows();

		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Select a destination Folder ...");
		jfc.setApproveButtonText("Select");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setMultiSelectionEnabled(true);
		String[] strExt = {""};
		SimpleFileFilter sff = new SimpleFileFilter(strExt, "Folder");
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.addChoosableFileFilter(sff);
		jfc.setFileFilter(sff);
		jfc.setCurrentDirectory(fDirectory);

		if (jfc.showOpenDialog(this) == JFileChooser.CANCEL_OPTION)
		{
			return;
		}

		fDirectory = jfc.getSelectedFile();         // get the user's selected directory
		sfh.setRecentLayoutDirectory(fDirectory);  // reset as the current selected directory

		// Test for Read-Only Files or naming conflicts
		for (int i = 0; i < counter; i++)
		{
			int selectedRowIndex = arr[i];
			StripDesign stripDesign = (StripDesign) tableModel.getDataObject(selectedRowIndex);
			String strFileName = stripDesign.getName();
			File f = new File(fDirectory.getPath()+"\\"+stripDesign.getName()+".epsdL");

			if (f.exists())
			{
				listOverWrite.add(strFileName);
				if (!f.canWrite())
				{
					listReadOnly.add(strFileName);
				}
			}

		}

		if (listReadOnly.size() > 0)
		{
			String strrof = new String();   // string of read only files
			for (int _i = 0; _i < listReadOnly.size(); _i++)
			{
				strrof += "\n"+(String) listReadOnly.get(_i);
			}

			JOptionPane.showMessageDialog(this,
			                              "The following file(s) are marked \"Read Only\". Please rename or choose a different destination:\n"
				                              +strrof);
			return;
		}


		if (listOverWrite.size() > 0)
		{
			String strow = new String();   // string of read only files
			for (int _i = 0; _i < listOverWrite.size(); _i++)
			{
				strow += "\n"+(String) listOverWrite.get(_i);
			}

			Object [] options = {"OK", "Cancel"};
			int n = JOptionPane.showOptionDialog(this,
			                                     "File(s) with this name(s) already exist. Click OK to overwrite the existing file(s):\n"
				                                     +strow,
			                                     "",
			                                     JOptionPane.YES_NO_OPTION,
			                                     JOptionPane.QUESTION_MESSAGE,
			                                     null,
			                                     options,
			                                     options[1]);
			if (n == 1)
			{
				return;
			}
			else if (n == JOptionPane.CLOSED_OPTION)
			{
				return;
			}
		}

		// Loop to Save
		for (int i = 0; i < counter; i++)
		{
			final int selectedRowIndex = arr[i];
			final StripDesign stripDesign = (StripDesign) tableModel.getDataObject(selectedRowIndex);
			Element sblMgr = XMLHelper.createElement("SingleStripBoardLayout", null);
			StripDesignIOLayouts.saveStripBoardLayoutPublic(sblMgr, stripDesign);
			doc = DocumentHelper.createDocument(sblMgr);

			try
			{
				File f = new File(fDirectory.getPath()+"\\"+stripDesign.getName()+".epsdL");
				FileOutputStream fos = new FileOutputStream(f);
				CipherOutputStream cos = new CipherOutputStream(fos, pbeCipher);
				cos.write(doc.asXML().getBytes());
				cos.flush();
				fos.flush();
				cos.close();
				fos.close();
			}
			catch (java.lang.IllegalStateException ise)
			{ System.out.println("IllegalStateException"); }
			catch (java.lang.NullPointerException npe)
			{ System.out.println("NullPointerException"); }
			catch (IOException ioe)
			{ System.out.println("IOException"); }
		}
	}


	/**
	 *  New code to import native EP Scheduling files.<br>
	 * @see ManagerStripDesign#exportTableRow()
	 * @see SimpleFileFilter
	 *
	 */
	final public void importTableRow()
		throws java.io.FileNotFoundException
	{
		// initialize some variables
		boolean bFileImported = false;
		List lNotImported = new ArrayList();
		File file[];
		//MMSToEPS importXML = new MMSToEPS();
		StripDesignIO sdio = new StripDesignIO();

		// retrieve the current directory
		ScheduleFileHelper sfh = SchedulerApp.getScheduleFileHelper();
		File dir = sfh.getRecentReportLayoutDirectory();

		// display the file chooser
		JFileChooser jfc = new JFileChooser();
		jfc.setMultiSelectionEnabled(true);
		String[] strExt = {"epsdL"};    // add stp  extension here
		SimpleFileFilter sff = new SimpleFileFilter(strExt, "Strip Layout File (*.epsdL)");
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.addChoosableFileFilter(sff);
		jfc.setFileFilter(sff);
		jfc.setCurrentDirectory(dir);
		if (jfc.showOpenDialog(this) == JFileChooser.CANCEL_OPTION)
		{
			return;
		}

		file = jfc.getSelectedFiles();

		// iterate through the selected files and add them to the current schedule
		int numSelectedFiles = file.length;
		for (int i = 0; i < numSelectedFiles; i++)
		{
			// Do not accept MMS *.stp files at this time
			// Test for an MMS, else import the EPS file
			//if (importXML.canImport(file[i].getParent(), file[i].getName()))
			//{
			//
			//	bFileImported = sdio.loadStripLayoutMMSFile( file[i] );
			//	if ( !bFileImported )
			//		lNotImported.add( file[i].getName() );
			//}
			//else
			{
				bFileImported = sdio.loadStripLayoutEPS( file[i] );
				if ( !bFileImported )
					lNotImported.add( file[i].getName() );
			}
		}

		if (lNotImported.size() > 0)
		{
			String strrof = new String();   // string of read only files
			for (int _i = 0; _i < lNotImported.size(); _i++)
			{
				strrof += "\n"+(String) lNotImported.get(_i);
			}

			JOptionPane.showMessageDialog(this,
			                              "An error occurred loading the following layout(s):\n"
				                              +strrof);
		}

		// if any files were imported, refresh the gui and send a notification to
		// the app that it now needs to be saved.
		if( bFileImported )
		{
			sfh.setRecentLayoutDirectory( jfc.getCurrentDirectory() );
			this.initQuickie();
			SchedulerApp.setDocumentModified( bFileImported );
		}
	}

	// TODO:  As soon as a more elegant way can be found to initialize this frame
	// TODO:  correctly under the Windows Look And Feel, this method should be
	// TODO:  entirely removed! It serves temporarily only to perform a corrective
	// TODO:  external resizing after the frame has been added to the desktop.
	private void addFrame( final String key, final GUIDesignerDialog frame )
	{
		super.addFrame( key, frame );

		// **************************************************************
		// This will force the Strip Designer frame to repaint itself on
		// initial display and allow the design area panel to appear
		if ( Platform.isWindowsLookAndFeel() )
		{
			frame.frameResized();
		}
		// **************************************************************
	}


	public void deleteTableRow()
	{
		final int[] selectedRowsIndex = table.getSelectedRows();
		final int size = selectedRowsIndex.length;
		if ( size == 0 )
		{
			return;
		}

		final int result =
		    Message.showYesNoDialog( "Are you sure you want to delete the selected strip layout?",
		                             "Delete Strip Layout - Confirm" );

		if ( result != JOptionPane.YES_OPTION )
		{
			return;
		}

		final int colIndex = ( tableModel ).findColumn( ACTIVE_COL_NAME );

		for ( int i = size - 1; i >= 0; i-- )
		{
			tableModel.removeDataObject( selectedRowsIndex[i] );
		}

		// By dafult the active row will be the first row.
		int activeRow = 0;

		// Look for any active rows.
		for ( int row = table.getRowCount() - 1; row >= 0; row-- )
		{
			if ( ( ( ManagerStripDesignTableModel ) tableModel ).isActive( row, colIndex ) )
			{
				activeRow = row;
				break;
			}
		}

		nodeSelected( selectedNode );
		SchedulerApp.setDocumentModified( true );

		if ( !setRowActive( activeRow, colIndex ) )
		{
			updateCurrentStripDesign();
		}
	}


	public void tableChanged( final TableModelEvent event )
	{
		final int colIndex = event.getColumn();
		int rowIndex = event.getFirstRow();

		if ( colIndex != ManagerBoardTableModel.INVALID_ROW_COL )
		{
			tableModel.rowData = ( Vector ) tableModel.getDataVector().elementAt( rowIndex );
			final boolean checked =
			    ( ( Boolean ) tableModel.rowData.get( ManagerBoardTableModel.ACTIVE_COL ) ).booleanValue();
			if ( !checked )
			{
				// Try to find the first row.
				rowIndex = ManagerBoardTableModel.INVALID_ROW_COL;
			}
			setRowActive( rowIndex, colIndex );
		}
	}


	private void setStripDesignIDActive( final String stripDesignID )
	{
		if ( !Utility.isEmpty( stripDesignID ) )
		{
			for ( int rowIndex = 0; rowIndex < ( tableModel ).getDataVector().size(); rowIndex++ )
			{
				final String rowID = ( ( ManagerStripDesignTableModel ) tableModel ).getStripDesignID( rowIndex );
				if ( stripDesignID.equals( rowID ) )
				{
					if ( !setRowActive( rowIndex, ManagerBoardTableModel.ACTIVE_COL ) )
					{
						updateCurrentStripDesign();
					}
					break;
				}
			}
		}
	}


	/**
	 * Set a row active.
	 *
	 * @param rowIndex
	 * @param colIndex
	 * @return true if the current Strip Design was updated.
	 */
	private boolean setRowActive( int rowIndex, final int colIndex )
	{
		boolean currentStripDesignUpdated = false;

		if ( rowIndex >= ( tableModel ).getDataVector().size() )
		{
			rowIndex = 0;
			if ( rowIndex < ( tableModel ).getDataVector().size() )
			{
				// Make someone else active.
				tableModel.rowData = ( Vector ) tableModel.getDataVector().elementAt( rowIndex );
				final String stripDesignName = ( String ) tableModel.rowData.get( ManagerBoardTableModel.NAME_COL );
				if ( !Utility.isEmpty( stripDesignName ) )
				{
					updateCurrentStripDesign( stripDesignName );
					currentStripDesignUpdated = true;
				}
				else
				{
					( ( ManagerStripDesignTableModel ) tableModel ).getStripDesigns().setActiveStripDesignNamed( null );
					rowIndex = ( ( ManagerStripDesignTableModel ) tableModel ).INVALID_ROW_COL;
				}
			}
		}

		boolean setActive = false;

		for ( int i = ( tableModel ).getDataVector().size() - 1; i >= 0; i-- )
		{
			( ( ManagerStripDesignTableModel ) tableModel ).rowData = ( Vector ) ( tableModel ).getDataVector().elementAt( i );

			//What is the current active row?
			if ( rowIndex == i && tableModel.isCellEditable( i, ManagerBoardTableModel.ACTIVE_COL ) )
			{
				SchedulerApp.setDocumentModified( true );
			}

			( ( ManagerStripDesignTableModel ) tableModel ).rowData.setElementAt( Boolean.valueOf( rowIndex == i ), colIndex );
			if ( rowIndex == i )
			{
				tableModel.rowData = ( Vector ) tableModel.getDataVector().elementAt( rowIndex );
				final String stripDesignName = ( String ) tableModel.rowData.get( ManagerBoardTableModel.NAME_COL );
				updateCurrentStripDesign( stripDesignName );
				currentStripDesignUpdated = true;
				setActive = true;
			}
		}

		if ( !setActive )
		{
			( ( ManagerStripDesignTableModel ) tableModel ).getStripDesigns().setActiveStripDesignNamed( null );
		}
		( tableModel ).fireTableDataChanged();

		// Make sure the active row is selected.
		if ( setActive )
		{
			table.getSelectionModel().setSelectionInterval( rowIndex, rowIndex );
		}

		return currentStripDesignUpdated;
	}


	public void rename()
	{
		final int[] selectedRowsIndex = table.getSelectedRows();
		if ( selectedRowsIndex.length != 1 )
		{
			return;
		}

		final int selectedRowIndex = selectedRowsIndex[0];
		final StripDesign stripDesign = ( StripDesign ) tableModel.getDataObject( selectedRowIndex );
		String name;

		// until the user gets it right or cancels...
		while ( true )
		{
			name = ( String ) Message.showInputDialog( "Enter strip layout name:",
			                                           "Rename Strip Layout", JOptionPane.QUESTION_MESSAGE, null, null,
			                                           stripDesign.name );

			// the user cancelled
			if ( name == null )
			{
				return;
			}
			else
			{
				name = name.trim();
				if ( name.length() == 0 )
				{
					ErrorMessage.showWarningMessage( "No strip layout name specified.\nPlease re-enter.",
					                                 "Rename Error" );
				}

				else if ( SchedulerApp.getSchedule().getStripDesigns().hasStripDesignNamed( name ) )
				{
					ErrorMessage.showWarningMessage( "Duplicate strip layout name specified.\nPlease re-enter.",
					                                 "Rename Error" );
				}

				// the user got it right
				else
				{
					break;
				}
			}
		}

		tableModel.renameDataObject( stripDesign, selectedRowIndex, name );
		tableModel.refreshDataObject( stripDesign, selectedRowIndex );
		nodeSelected( selectedNode );
		updateCurrentStripDesign();
		SchedulerApp.setDocumentModified( true );
	}


	public void save( final Design design )
	{
		final StripDesign stripDesign = ( StripDesign ) ( ( StripDesign ) design ).clone();
		final int rowIndex = tableModel.getRowIndex( stripDesign.getID() );

		// if we're saving an existing design...
		if ( rowIndex != -1 )
		{
			tableModel.refreshDataObject( stripDesign, rowIndex );
			updateCurrentStripDesign();
		}

		// if we're saving a new design...
		else
		{
			tableModel.addDataObject( stripDesign );
			updateCurrentStripDesign();

			// If we've just added the first design to this schedule, we need to
			// force a table refresh to display the Active flag that was set by
			// the updateCurrentStripDesign() method, after the original table
			// refresh
			if ( ( ( ManagerStripDesignTableModel ) tableModel ).getStripDesigns().getStripDesignCount() == 1 )
			{
				tableModel.refreshDataObject( stripDesign, tableModel.getRowIndex( stripDesign.getID() ) );
			}
		}
	}


	public void propertyChange( final PropertyChangeEvent event )
	{
		if ( SchedulerApp.noSchedule() )
		{
			return;
		}

		final String propertyName = event.getPropertyName();
		final Object newValue = event.getNewValue();

		if ( propertyName.equals( StripToolBar.EVT_SELECT_STRIP_DESIGN ) )
		{
			if ( newValue instanceof StripDesign )
			{
				final StripDesign stripDesign = ( StripDesign ) newValue;
				setStripDesignIDActive( stripDesign.getID() );
			}
		}

		super.propertyChange( event );
	}


	/**
	 * This routine updates with the curretn active strip board.
	 */
	private void updateCurrentStripDesign()
	{
		final String stripDesignName =
		    ( ( ManagerStripDesignTableModel ) tableModel ).getStripDesigns().getActiveStripDesignName();
		updateCurrentStripDesign( stripDesignName );
	}


	/**
	 * This routine allows you to set the current strip board as well as refresh the toolbar.
	 *
	 * @param stripDesignName
	 */
	private void updateCurrentStripDesign( final String stripDesignName )
	{
		( ( ManagerStripDesignTableModel ) tableModel ).getStripDesigns().setActiveStripDesignNamed( stripDesignName );
		firePropertyChange( ManagerStripDesign.EVT_SELECT_STRIP_DESIGN, null,
		                    ( ( ManagerStripDesignTableModel ) tableModel ).getStripDesigns().getActiveStripDesign() );
	}


	//-----------------------------------------------------------------------
	public void refreshTree()
	{
		init();
		super.refreshTree();
	}


	public void refresh() {}  // data to screen


	public void update() {}   // screen to data
}

