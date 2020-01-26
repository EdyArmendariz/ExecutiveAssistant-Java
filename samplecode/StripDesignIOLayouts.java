package com.bulloch.scheduler.EPIO;

import com.bulloch.scheduler.PrintGUI.GUILine;
import com.bulloch.scheduler.PrintGUI.GUIRectangle;
import com.bulloch.scheduler.PrintGUI.GUIShape;
import com.bulloch.scheduler.PrintGUI.GUIToolBar;
import com.bulloch.scheduler.StripDemo.StripDesign;
import com.bulloch.scheduler.StripDemo.StripDesigns;
import com.bulloch.scheduler.StripDemo.StripsPanel;
import com.bulloch.scheduler.util.MathUtility;
import com.bulloch.scheduler.util.ResourceID;
import com.bulloch.scheduler.sched.SchedulerApp;
import com.epservices.apps.Core.*;
import org.dom4j.Element;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

/*
 ****************************************************************************************
 *  File Name       : StripDesignIOLayouts.java
 *  Author          : pstorli
 *  Project         : E P - S C H E D U L I N G
 *  Created Date    : Mar 5, 2003 Time: 11:04:27 AM
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


public class StripDesignIOLayouts
{
	public static StripDesignIOXmlIDs xmlIDs = null;


	//-----------------------------------------------------------------
	//  getTextAlignmentString()
	//-----------------------------------------------------------------
	private static String getTextAlignmentString( int alignment )
	    throws EPException
	{
		String strAlign = xmlIDs.ID_LEFT;
		try
		{
			switch ( alignment )
			{
				case GUIToolBar.LEFT_JUSTIFIED:
					strAlign = xmlIDs.ID_LEFT;
					break;
				case GUIToolBar.RIGHT_JUSTIFIED:
					strAlign = xmlIDs.ID_RIGHT;
					break;
				case GUIToolBar.CENTER_JUSTIFIED:
					strAlign = xmlIDs.ID_CENTER;
					break;
				default:
					strAlign = xmlIDs.ID_LEFT;
					break;
			}
		}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject( "Strip Design error in saving Text alignment data! \n"
			                                                          + e.getMessage() ) );
		}

		return strAlign;
	}


	//-----------------------------------------------------------------
	//  saveStripBoardDefault()
	//-----------------------------------------------------------------
	private static void saveStripBoardDefault( Element sblElement, StripDesign sd )
	    throws EPException
	{
		try
		{
			Element stripBoardDefaultElement = XMLHelper.createElement( xmlIDs.ID_StripBoardDefault, sblElement );
			//
			// Text Justification attribute
			//
			String strAlignment = getTextAlignmentString( sd.areaAlignment );
			XMLHelper.createAttribute( xmlIDs.ID_TextJustification, stripBoardDefaultElement, strAlignment );

			StripDesignIOUtil.addFontElement( xmlIDs.ID_Font, stripBoardDefaultElement,
			                                  sd.areaFont,
			                                  xmlIDs, className );
		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject( "Strip Design error in saving StripBoardDefault [" + sd.name
			                                                          + "]! \n"
			                                                          + e.getMessage() ) );
		}
	}


	//-----------------------------------------------------------------
	//  saveBannerStripLayout()
	//-----------------------------------------------------------------
	private static void saveBannerStripLayout( Element sblElement, StripDesign sd )
	    throws EPException
	{
		try
		{
			Element bannerStripLayoutElement = XMLHelper.createElement( xmlIDs.ID_BannerStripLayout, sblElement );
			//
			// Text Justification attribute
			//
			String strAlignment =
			    getTextAlignmentString( sd.bannerAlignment );
			XMLHelper.createAttribute( xmlIDs.ID_TextJustification, bannerStripLayoutElement, strAlignment );
			StripDesignIOUtil.addFontElement( xmlIDs.ID_Font, bannerStripLayoutElement,
			                                  sd.bannerFont,
			                                  xmlIDs, className );

		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject(
			                                           "Strip Design error in saving Banner Strip Layout [" + sd.name + "]\n"
			                                           + e.getMessage() ) );
		}
	}


	//-----------------------------------------------------------------
	//  saveDayBreakStripLayout()
	//-----------------------------------------------------------------
	private static void saveDayBreakStripLayout( Element sblElement, StripDesign sd )
	    throws EPException
	{
		try
		{
			Element dayBreakStripLayoutElement = XMLHelper.createElement( xmlIDs.ID_DayBreakStripLayout, sblElement );
			//
			// Text Justification attribute
			//
			String strAlignment =
			    getTextAlignmentString( sd.dayBreakAlignment );
			XMLHelper.createAttribute( xmlIDs.ID_TextJustification, dayBreakStripLayoutElement, strAlignment );

			StripDesignIOUtil.addFontElement( xmlIDs.ID_Font, dayBreakStripLayoutElement,
			                                  sd.dayBreakFont,
			                                  xmlIDs, className );

		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject(
			                                           "Strip Design error in saving DayBreakStripLayout [" + sd.name + "]! \n"
			                                           + e.getMessage() ) );
		}
	}


	//-----------------------------------------------------------------
	//  saveStripBoardHeaderLayout()
	//-----------------------------------------------------------------
	private static void saveStripBoardHeaderLayout( Element sblElement, StripDesign sd )
	    throws EPException
	{
		try
		{
			Element sbHeaderLayoutElement = XMLHelper.createElement( xmlIDs.ID_StripBoardHeaderLayout, sblElement );
			//
			// Width attribtue
			//
			double fWidth = sd.getHeaderWidth();
			fWidth = MathUtility.roundOff( fWidth, 2 );
			XMLHelper.createAttribute( xmlIDs.ID_Width, sbHeaderLayoutElement,
			                           XMLMappings.doubleToXML( fWidth ) );
			//
			// Add Layout and shapes
			//
			List shapes = sd.headerBoardShapes;
			saveLayout( sbHeaderLayoutElement, sd, shapes );

		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject(
			                                           "Strip Design error in saving StripBoard Header Layout [" + sd.name + "]! \n"
			                                           + e.getMessage() ) );
		}
	}


	//-----------------------------------------------------------------
	//  saveLayout()
	//-----------------------------------------------------------------
	private static void saveLayout( Element parent,
	                                StripDesign sd, List shapes )
	    throws EPException
	{
		try
		{
			Element layout = XMLHelper.createElement( xmlIDs.ID_Layout, parent );

			Element shapesElement = XMLHelper.createElement( xmlIDs.ID_Shapes, layout );


			if ( shapes == null )
			{
				return;
			}

			for ( int i = 0; i < shapes.size(); i++ )
			{
				GUIShape shape = ( GUIShape ) shapes.get( i );
				boolean bRectangle = shape instanceof GUIRectangle;
				boolean bLine = shape instanceof GUILine;

				if ( bRectangle || bLine )
				{
					StripDesignIOLines.save( shapesElement, shape, xmlIDs );
				}
				else
				{
					StripDesignIOFields.save( shapesElement, shape, xmlIDs );
				}

			}
		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject(
			                                           "Strip Design error in saving Breakdown Sheet Strip Layout [" + sd.name + "]! \n"
			                                           + e.getMessage() ) );
		}
		return;
	}


	//-----------------------------------------------------------------
	//  saveBDSStripLayout()
	//-----------------------------------------------------------------
	private static void saveBDSStripLayout( Element sblElement, StripDesign sd )
	    throws EPException
	{
		try
		{
			Element bdsLayoutElement = XMLHelper.createElement( xmlIDs.ID_BDSStripLayout, sblElement );
			List shapes = sd.shapes;
			saveLayout( bdsLayoutElement, sd, shapes );

		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject(
			                                           "Strip Design error in saving Breakdown Sheet Layout [" + sd.name + "]! \n"
			                                           + e.getMessage() ) );
		}
	}


	//-----------------------------------------------------------------
	//  getStripOrientationString()
	//-----------------------------------------------------------------
	private static String getStripOrientationString( int orientation )
	    throws EPException
	{
		String strOrientation = "HORIZONTAL";
		try
		{
			switch ( orientation )
			{
				case StripsPanel.HORIZONTAL:
					strOrientation = "HORIZONTAL";
					break;
				case StripsPanel.VERTICAL:
					strOrientation = "VERTICAL";
					break;
				default:
					break;
			}
		}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject(
			                                           "Strip Design error in saving Strip Orienation data! \n" + e.getMessage() ) );
		}

		return strOrientation;
	}


	//-----------------------------------------------------------------
	//  saveStripBoardLayoutAttributes()
	//-----------------------------------------------------------------
	private static void saveStripBoardLayoutAttributes( Element sblElement, StripDesign sd )
	    throws EPException
	{
		try
		{
			XMLHelper.createAttribute( xmlIDs.ID_StripBoardLayout_Name, sblElement, sd.name );
			XMLHelper.createAttribute( xmlIDs.ID_StripBoardLayout_StripOrientation, sblElement,
			                           getStripOrientationString( sd.orientation ) );

			double value = MathUtility.roundOff( sd.getStripWidth() );
			XMLHelper.createAttribute( xmlIDs.ID_StripBoardLayout_StripWidth, sblElement,
			                           XMLMappings.doubleToXML( value ) );

			value = MathUtility.roundOff( sd.getStripLength() );
			XMLHelper.createAttribute( xmlIDs.ID_StripBoardLayout_StripLength, sblElement,
			                           XMLMappings.doubleToXML( value ) );
		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject(
			                                           "Strip Design error in saving Stripboard Layout Attributes! \n" + e.getMessage() ) );
		}
	}


	/**
	 * A trampoline method that allows access to a private method.<br>
	 *
	 * @param sblsElement  the XML Element to be modified.
	 * @param sd the StripDesign object that will modify <code>sblsElement</code>
	 * @see StripDesignIOLayouts#saveStripBoardLayout(org.dom4j.Element, com.bulloch.scheduler.StripDemo.StripDesign)
	 */
	public static void saveStripBoardLayoutPublic( Element sblsElement, StripDesign sd )
	{
		try
		{
			saveStripBoardLayout( sblsElement, sd );
		}
		catch( EPException epe )
		{

		}
	}

	//-----------------------------------------------------------------
	//  saveStripBoardLayout()
	//-----------------------------------------------------------------
	private static void saveStripBoardLayout( Element sblsElement, StripDesign sd )
	    throws EPException
	{
		try
		{
			Element sblElement = XMLHelper.createElement( xmlIDs.ID_StripBoardLayout, sblsElement );

			saveStripBoardLayoutAttributes( sblElement, sd );

			StripDesignIOPageFormat.save( sblElement, sd );

			saveBDSStripLayout( sblElement, sd );

			saveStripBoardHeaderLayout( sblElement, sd );

			saveStripBoardDefault( sblElement, sd );

			saveDayBreakStripLayout( sblElement, sd );

			saveBannerStripLayout( sblElement, sd );
		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject( "Strip Design error in saving Stripboard layout! \n" + e.getMessage() ) );
		}
	}


	//-----------------------------------------------------------------
	//  save()
	//-----------------------------------------------------------------
	public static void save( Element sblMgr, StripDesigns stripDesigns )
	    throws EPException
	{
		try
		{
			xmlIDs = StripDesignIOXmlIDs.instance();
			Element sblsElement =
			    XMLHelper.createElement( xmlIDs.ID_StripBoardLayouts, sblMgr );

			StripDesign[] stripDesignsArray = stripDesigns.getStripDesigns();
			int size = stripDesignsArray.length;

			for ( int i = 0; i < size; i++ )
			{
				StripDesign stripDesign = ( StripDesign ) stripDesignsArray[i];
				if ( null == stripDesign )
				{
					continue;
				}
				saveStripBoardLayout( sblsElement, stripDesign );
			}

			StripDesignIOXmlIDs.RemoveInstance();
		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject(
			                                           "Strip Design error in saving Strip design layouts! \n" + e.getMessage() ) );
		}
	}

	//-----------------------------------------------------------------
	//
	//              L O A D I N G METHODS ....
	//
	//-----------------------------------------------------------------


	//-----------------------------------------------------------------
	//  loadBannerStripLayout()
	//-----------------------------------------------------------------
	private static void loadStripBoardDefault( Element sblElement, StripDesign sd )
	    throws EPException
	{
		try
		{
			Element stripBoardDefaultElement = sblElement.element( xmlIDs.ID_StripBoardDefault );
			if ( stripBoardDefaultElement == null )
			{
				sd.areaAlignment = GUIToolBar.LEFT_JUSTIFIED;
				sd.areaFont = new Font( SchedulerApp.appFont.getName(), SchedulerApp.appFont.getStyle(), SchedulerApp.appFont.getSize() );
				return;
			}

			int nAlign = parseTextJustification( stripBoardDefaultElement, xmlIDs.ID_TextJustification );
			sd.areaAlignment = nAlign;

			Font font = StripDesignIOUtil.parseFont( stripBoardDefaultElement, xmlIDs.ID_Font, xmlIDs, className );
			sd.areaFont = font;
		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject( "Strip Design error in loading Strip Board Default! \n"
			                                                          + e.getMessage() ) );
		}
	}


	//-----------------------------------------------------------------
	//  loadBannerStripLayout()
	//-----------------------------------------------------------------
	private static void loadBannerStripLayout( Element sblElement, StripDesign sd )
	    throws EPException
	{
		try
		{
			Element bannerStripLayoutElement =
			    sblElement.element( xmlIDs.ID_BannerStripLayout );
			if ( bannerStripLayoutElement == null )
			{
				return;
			}

			int nAlign = parseTextJustification( bannerStripLayoutElement,
			                                     xmlIDs.ID_TextJustification );
			sd.bannerAlignment = nAlign;

			Font font = StripDesignIOUtil.parseFont( bannerStripLayoutElement, xmlIDs.ID_Font,
			                                         xmlIDs, className );
			sd.bannerFont = font;
		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject(
			                                           "Strip Design error in loading Banner Strip Layout! \n" + e.getMessage() ) );
		}
	}


	//-----------------------------------------------------------------
	// parseTextJustification()
	//-----------------------------------------------------------------
	private static int parseTextJustification( Element elem, String attName )
	    throws EPException
	{
		int nAlign = GUIToolBar.LEFT_JUSTIFIED;
		try
		{
			String value = elem.attributeValue( attName );
			if ( value.equals( xmlIDs.ID_LEFT ) )
			{
				nAlign = GUIToolBar.LEFT_JUSTIFIED;
			}

			else if ( value.equals( xmlIDs.ID_RIGHT ) )
			{
				nAlign = GUIToolBar.RIGHT_JUSTIFIED;
			}

			else if ( value.equals( xmlIDs.ID_CENTER ) )
			{
				nAlign = GUIToolBar.CENTER_JUSTIFIED;
			}
		}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject(
			                                           "Strip Design error in parsing Text alignment data! \n" + e.getMessage() ) );
		}
		return nAlign;

	}


	//-----------------------------------------------------------------
	//  loadDayBreakLayout()
	//-----------------------------------------------------------------
	private static void loadDayBreakLayout( Element sblElement, StripDesign sd )
	    throws EPException
	{
		try
		{
			Element dayBreakLayoutElement =
			    sblElement.element( xmlIDs.ID_DayBreakStripLayout );
			if ( dayBreakLayoutElement == null )
			{
				return;
			}

			int nAlign = parseTextJustification( dayBreakLayoutElement,
			                                     xmlIDs.ID_TextJustification );
			sd.dayBreakAlignment = nAlign;

			Font font = StripDesignIOUtil.parseFont( dayBreakLayoutElement, xmlIDs.ID_Font,
			                                         xmlIDs, className );
			sd.dayBreakFont = font;

		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject(
			                                           "Strip Design error in loading Day break Strip Layout! \n" + e.getMessage() ) );
		}
	}


	//-----------------------------------------------------------------
	//  loadSBHeaderLayout()
	//-----------------------------------------------------------------
	private static void loadSBHeaderLayout( Element sblElement, StripDesign sd )
	    throws EPException
	{
		try
		{
			Element sbHeaderLayoutElement =
			    sblElement.element( xmlIDs.ID_StripBoardHeaderLayout );
			if ( sbHeaderLayoutElement == null )
			{
				return;
			}

			double fWidth = XMLMappings.xmlToDouble( sbHeaderLayoutElement.attributeValue( xmlIDs.ID_Width ) );
			sd.setHeaderWidth( fWidth );

			loadLayout( sbHeaderLayoutElement, sd, xmlIDs.ID_StripBoardHeaderLayoutContainer );

		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject(
			                                           "Strip Design error in loading Breakdown sheet Strip Layout! \n" + e.getMessage() ) );
		}
	}


	//-----------------------------------------------------------------
	//  loadBDSLayout()
	//-----------------------------------------------------------------
	private static void loadLayout( Element bdsElement, StripDesign sd, int shapesContainerType )
	    throws EPException
	{
		try
		{
			Element layoutElement =
			    bdsElement.element( xmlIDs.ID_Layout );

			if ( layoutElement == null )
			{
				return;
			}

			Element shapesElement =
			    layoutElement.element( xmlIDs.ID_Shapes );

			if ( shapesElement == null )
			{
				return;
			}

			for ( Iterator i = shapesElement.elementIterator(); i.hasNext(); )
			{
				Element shapeElement = ( Element ) i.next();
				boolean bHorzLine =
				    shapeElement.getName().equals( xmlIDs.ID_HorizontalLine );
				boolean bVertLine =
				    shapeElement.getName().equals( xmlIDs.ID_VerticalLine );
				boolean bRectangle =
				    shapeElement.getName().equals( xmlIDs.ID_Rectangle );

				if ( bHorzLine || bVertLine || bRectangle )
				{
					StripDesignIOLines.load( shapeElement, sd, shapesContainerType, xmlIDs );
				}
				else
				{
					StripDesignIOFields.load( shapeElement, sd, shapesContainerType, xmlIDs );
				}
			}
			StripDesignIOXmlIDs.RemoveInstance();


		}
		catch ( EPException e )
		{throw e;}
		catch ( Exception e )
		{
			EPExceptionHelper.generateEPException( ResourceID.ID_MSG_ALL,
			                                       Resource.toObject(
			                                           "Strip Design error in loading Breakdown sheet Strip Layout! \n" + e.getMessage() ) );
		}
	}


	//-----------------------------------------------------------------
	//  loadBDSLayout()
	//-----------------------------------------------------------------
	private static void loadBDSLayout( Element sblElement, StripDesign sd )
	{
		try
		{
			Element bdsElement =
			    sblElement.element( xmlIDs.ID_BDSStripLayout );
			if ( bdsElement == null )
			{
				return;
			}
			loadLayout( bdsElement, sd, xmlIDs.ID_BreakdownSheetLayoutContainer );
		}
		catch ( Exception e )
		{
			Trace.writeln( className,
			               "Error in loading Breakdown sheet Strip Layout!" );
		}

	}


	//-----------------------------------------------------------------
	//  parseStripWidth()
	//-----------------------------------------------------------------
	private static double parseStripLength( Element sblElement )
	{
		double fLength = xmlIDs.ID_SBL_DefaultLength;
		try
		{
			double fValue =
			    XMLMappings.xmlToDouble( sblElement.attributeValue( xmlIDs.ID_StripBoardLayout_StripLength ) );
			if ( fValue <= 0.0 )
			{
				return fLength; // return default Value
			}
			fLength = fValue;
		}
		catch ( Exception e )
		{
			Trace.writeln( className,
			               "Error in parsing Strip Length " );
		}
		return fLength;

	}


	//-----------------------------------------------------------------
	//  parseStripWidth()
	//-----------------------------------------------------------------
	private static double parseStripWidth( Element sblElement )
	{
		double fWidth = xmlIDs.ID_SBL_DefaultWidth;
		try
		{
			double fValue =
			    XMLMappings.xmlToDouble( sblElement.attributeValue( xmlIDs.ID_StripBoardLayout_StripWidth ) );
			if ( fValue <= 0.0 )
			{
				return fWidth; // return default Value
			}
			fWidth = fValue;
		}
		catch ( Exception e )
		{
			Trace.writeln( className,
			               "Error in parsing Strip Width " );
		}

		return fWidth;

	}


	//-----------------------------------------------------------------
	//  parseStripOrientation()
	//-----------------------------------------------------------------
	private static int parseStripOrientation( Element sblElement )
	{
		int nOrientation = StripsPanel.HORIZONTAL;
		String strOrientation =
		    sblElement.attributeValue( xmlIDs.ID_StripBoardLayout_StripOrientation );

		if ( strOrientation.equals( xmlIDs.ID_SBL_Orientation_HORIZONTAL ) )
		{
			nOrientation = StripsPanel.HORIZONTAL;
		}
		else if ( strOrientation.equals( xmlIDs.ID_SBL_Orientation_VERTICAL ) )
		{
			nOrientation = StripsPanel.VERTICAL;
		}
		return nOrientation;

	}


	//-----------------------------------------------------------------
	//  loadSBLAttributes()
	//-----------------------------------------------------------------
	private static void loadSBLAttributes( Element sblElement, StripDesign sd )
	{
		try
		{
			sd.name = sblElement.attributeValue( xmlIDs.ID_StripBoardLayout_Name );
			sd.orientation = parseStripOrientation( sblElement );
			double fWidth = parseStripWidth( sblElement );
			double fHeight = parseStripLength( sblElement );
			sd.setStripDimension( ( float ) fWidth, ( float ) fHeight );
		}
		catch ( Exception e )
		{
			Trace.writeln( className, "Error in loading Strip Layout attributes!" );
		}
		return;
	}


	/**
	 * Parses an XML Element defining a Strip Board Layout.<br>
	 * Overloads the private access modifier. <br>
	 * @see StripDesignIOLayouts#loadStripBoardLayout(org.dom4j.Element, com.bulloch.scheduler.StripDemo.StripDesigns) 
	 * @param sblElement
	 * @param sds
	 */
	public static void loadStripBoardLayoutPublic( Element sblElement, StripDesigns sds )
	{
		 loadStripBoardLayout(  sblElement,  sds );
	}


	/**
	 * Parses an XML Element defining a Strip Board Layout.<br>
	 * Loads an individual <code>stripBoardLayout</code> into the <code>stripDesigns</code>.<br>
	 * Executes once for each strip layout in the schedule.<br>
	 * @param stripBoardLayout an XML Element defining a Strip Board Layout.
	 * @param stripDesigns StripDesigns object.
	 */
	private static void loadStripBoardLayout( Element stripBoardLayout, StripDesigns stripDesigns )
	{
		StripDesign stripDesign = new StripDesign();
		try
		{
			if ( stripDesign == null )
			{
				return;
			}

			loadSBLAttributes( stripBoardLayout, stripDesign );

			StripDesignIOPageFormat.load( stripBoardLayout, stripDesign );

			loadBDSLayout( stripBoardLayout, stripDesign );

			loadSBHeaderLayout( stripBoardLayout, stripDesign );

			loadStripBoardDefault( stripBoardLayout, stripDesign );

			loadDayBreakLayout( stripBoardLayout, stripDesign );

			loadBannerStripLayout( stripBoardLayout, stripDesign );

		}
		catch ( Exception e )
		{
			Trace.writeln( className, "Error in loading Strip Design!" );
		}

		//
		// Now add the strip design object
		//
		try
		{
			String s = stripDesign.getName();
			String postfix = "_1";
			if ( stripDesigns.hasStripDesignNamed( s ) )
			{
				while ( stripDesigns.hasStripDesignNamed( s ) )
				{
					if (s.endsWith( postfix ))
					{
						int len = s.length();
						String temp = s.substring( 0, len - 2 );
						s = temp + "_2";
					}
					else if (s.endsWith("_2"))
					{
						int len = s.length();
						String temp = s.substring( 0, len - 2 );
						s = temp + "_3";
					}
					else if (s.endsWith("_3"))
					{
						int len = s.length();
						String temp = s.substring( 0, len - 2 );
						s = temp + "_4";
					}
					else if (s.endsWith("_4"))
					{
						int len = s.length();
						String temp = s.substring( 0, len - 2 );
						s = temp + "_5";
					}
					else if (s.endsWith("_5"))
					{
						int len = s.length();
						String temp = s.substring( 0, len - 2 );
						s = temp + "_6";
					}
					else if (s.endsWith("_6"))
					{
						int len = s.length();
						String temp = s.substring( 0, len - 2 );
						s = temp + "_7";
					}
					else if (s.endsWith("_7"))
					{
						int len = s.length();
						String temp = s.substring( 0, len - 2 );
						s = temp + "_8";
					}
					else if (s.endsWith("_8"))
					{
						int len = s.length();
						String temp = s.substring( 0, len - 2 );
						s = temp + "_9";
					}
					else
						s += "_1";
				}

				stripDesign.setName( s );
				stripDesigns.addStripDesign( stripDesign );
			}
			else
			{
				stripDesigns.addStripDesign( stripDesign );
			}
		}
		catch ( Exception e )
		{
			Trace.writeln( className, "Error in adding Strip Design!" );
		}


	}


	/**
	 * A trampoline method that allows access to a private method.<br>
	 *
	 * @param e an XML Element object that contains the source data.
	 * @param s a StripDesigns object that gets modified.
	 * @see StripDesignIOLayouts#loadStripBoardLayouts(org.dom4j.Element, com.bulloch.scheduler.StripDemo.StripDesigns) 
	 */
	public static void loadStripBoardLayoutsPublic( Element e, StripDesigns s )
	{
		loadStripBoardLayouts( e ,  s );
	}

	/**
	 * Iterates through the StripBoardLayouts and invokes <i>loadStripBoardLayout()</i>
	 * multiple times.<br>
	 *
	 * @param stripBoardLayouts The 'StripBoardLayout' parent element that contains all of the
	 * Strip Design Layouts.
	 * @param sds The StripDesigns object that gets modified.
	 * @see StripDesignIOLayouts#loadStripBoardLayout(org.dom4j.Element, com.bulloch.scheduler.StripDemo.StripDesigns)
	 */
	private static void loadStripBoardLayouts( Element stripBoardLayouts, StripDesigns sds )
	{
		try
		{
			// for each Strip Layout in the Strip Board Layouts
			for ( Iterator i = stripBoardLayouts.elementIterator(); i.hasNext(); )
			{
				Element stripBoardLayout = ( Element ) i.next();
				if ( !stripBoardLayout.getName().equals( xmlIDs.ID_StripBoardLayout ) )
				{
					continue;
				}
				loadStripBoardLayout( stripBoardLayout, sds );
			}
		}
		catch ( Exception e )
		{
			Trace.writeln( className, "Error in loading Strip Designs!" );
		}


	}


	/**
	 * Loads the StripBoardLayoutMgr into the schedule.<br>
	 * Invokes <i>loadStripBoardLayouts()</i>.<br>
	 * @param parentElement An Element object of root type 'StripBoardLayoutMgr'
	 * @param sds
	 * @see StripDesignIOLayouts#loadStripBoardLayouts(org.dom4j.Element, com.bulloch.scheduler.StripDemo.StripDesigns)
	 */
	public static void load( Element parentElement, StripDesigns sds )
	{
		try
		{
			xmlIDs = StripDesignIOXmlIDs.instance();

			// for each child Element in the parentElement Element
			for ( Iterator i = parentElement.elementIterator(); i.hasNext(); )
			{
			    // get the child Element of StripBoardLayouts
				Element childElement = ( Element ) i.next();
				if ( !childElement.getName().equals( xmlIDs.ID_StripBoardLayouts ) )
				{
					continue;
				}
				loadStripBoardLayouts( childElement, sds );
				break;
			}
			StripDesignIOXmlIDs.RemoveInstance();
		}
		catch ( Exception e )
		{
			Trace.writeln( className, "Error in loading Stripboard Layout Manager!" );
		}

	}


	private static String className = "StripDesignIOLayouts";

}

