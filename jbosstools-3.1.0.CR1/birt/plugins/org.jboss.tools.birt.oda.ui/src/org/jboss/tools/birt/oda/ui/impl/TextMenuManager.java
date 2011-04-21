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
import org.jboss.tools.birt.oda.ui.Messages;


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
		manager.appendToGroup( "undo", getAction( "undo", viewer, Messages.TextMenuManager_Undo, ITextOperationTarget.UNDO ) ); //$NON-NLS-1$ //$NON-NLS-2$
		manager.appendToGroup( "undo", getAction( "redo", viewer, Messages.TextMenuManager_Redo, ITextOperationTarget.REDO ) ); //$NON-NLS-1$ //$NON-NLS-2$
		manager.appendToGroup( "copy", getAction( "cut", viewer, Messages.TextMenuManager_Cut, ITextOperationTarget.CUT ) ); //$NON-NLS-1$ //$NON-NLS-2$
		manager.appendToGroup( "copy", getAction( "copy", viewer, Messages.TextMenuManager_Copy, ITextOperationTarget.COPY ) );//$NON-NLS-1$ //$NON-NLS-2$
		manager.appendToGroup( "copy", getAction( "paste", viewer, Messages.TextMenuManager_Paste, ITextOperationTarget.PASTE ) );//$NON-NLS-1$ //$NON-NLS-2$
		manager.appendToGroup( "select", getAction( "selectall", viewer, Messages.TextMenuManager_Select_all, ITextOperationTarget.SELECT_ALL ) );//$NON-NLS-1$ //$NON-NLS-2$

		manager.add( new Separator("hql") ); //$NON-NLS-1$
		manager.appendToGroup( "hql", getAction("contentAssist", viewer, Messages.TextMenuManager_Content_Assist,13)); //$NON-NLS-1$ //$NON-NLS-2$
		manager.appendToGroup( "hql", getAction("contentTip", viewer, Messages.TextMenuManager_Content_Tip,14)); //$NON-NLS-1$ //$NON-NLS-2$
		manager.appendToGroup( "hql", getAction("format", viewer, Messages.TextMenuManager_Format_HQL_source,15)); //$NON-NLS-1$ //$NON-NLS-2$
		
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