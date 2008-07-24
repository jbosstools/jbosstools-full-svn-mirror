package org.jboss.tools.birt.oda.ui.impl;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;


/**
 * Text menu manager contains 'undo,redo,cut,copy,paste,select all' menuItem. It
 * displays on textVeiwer.
 * 
 */
class TextMenuManager
{

	private Hashtable htActions = new Hashtable( );
	private MenuManager manager;

	/**
	 * Constructor to specify the textMenuManager for a text viewer.
	 * 
	 * @param viewer
	 */
	TextMenuManager( TextViewer viewer )
	{
		manager = new MenuManager( );
		Separator separator = new Separator( "undo" );//$NON-NLS-1$
		manager.add( separator );
		separator = new Separator( "copy" );//$NON-NLS-1$
		manager.add( separator );
		separator = new Separator( "select" );//$NON-NLS-1$
		manager.add( separator );
		manager.appendToGroup( "undo", getAction( "undo", viewer, "Undo", ITextOperationTarget.UNDO ) );
		manager.appendToGroup( "undo", getAction( "redo", viewer, "Redo", ITextOperationTarget.REDO ) );
		manager.appendToGroup( "copy", getAction( "cut", viewer, "Cut", ITextOperationTarget.CUT ) );
		manager.appendToGroup( "copy", getAction( "copy", viewer, "Copy", ITextOperationTarget.COPY ) );//$NON-NLS-1$
		manager.appendToGroup( "copy", getAction( "paste", viewer, "Paste", ITextOperationTarget.PASTE ) );//$NON-NLS-1$
		manager.appendToGroup( "select", getAction( "selectall", viewer, "Select all", ITextOperationTarget.SELECT_ALL ) );//$NON-NLS-1$

		manager.add( new Separator("hql") );
		manager.appendToGroup( "hql", getAction("contentAssist", viewer, "Content Assist",13));
		manager.appendToGroup( "hql", getAction("contentTip", viewer, "Content Tip",14));
		manager.appendToGroup( "hql", getAction("format", viewer, "Format HQL source",15));
		
		manager.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				Enumeration elements = htActions.elements( );
				while ( elements.hasMoreElements( ) )
				{
					SourceViewerAction action = (SourceViewerAction) elements.nextElement( );
					action.update( );
				}
			}
		} );
	}
	
	/**
	 * 
	 * @param control
	 * @return
	 */
	public Menu getContextMenu( Control control )
	{
		return manager.createContextMenu( control );
	}

	/**
	 * 
	 * @param id
	 * @param viewer
	 * @param name
	 * @param operation
	 * @return
	 */
	private final SourceViewerAction getAction( String id, TextViewer viewer,
			String name, int operation )
	{
		SourceViewerAction action = (SourceViewerAction) htActions.get( id );
		if ( action == null )
		{
			action = new SourceViewerAction( viewer, name, operation );
			htActions.put( id, action );
		}
		return action;
	}

	
	class SourceViewerAction extends Action
	{

		private int operationCode = -1;
		private TextViewer viewer = null;

		public SourceViewerAction( TextViewer viewer, String text,
				int operationCode )
		{
			super( text );
			this.operationCode = operationCode;
			this.viewer = viewer;
		}

		/*
		 * 
		 * @see org.eclipse.jface.action.IAction#run()
		 */
		public void run( )
		{
			viewer.doOperation( operationCode );
		}

		/**
		 * update the operation
		 * 
		 */
		public void update( )
		{
			setEnabled( viewer.canDoOperation( operationCode ) );
		}

	}

}