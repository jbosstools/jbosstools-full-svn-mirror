/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.ui.veditor.editors;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.parts.*;
import org.eclipse.gef.*;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.parts.*;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.jboss.tools.hibernate.ui.veditor.editors.model.OrmDiagram;
import org.jboss.tools.hibernate.ui.veditor.editors.parts.TreePartFactory;


public class DiagramContentOutlinePage extends ContentOutlinePage implements
		IAdaptable {
	
	private GraphicalViewer graphicalViewer;
	
	private VisualEditor editor;
	
	private OrmDiagram ormDiagram;

	private SelectionSynchronizer selectionSynchronizer;

	private PageBook pageBook;

	private Control outline;
	/*
	 * surface for drawing 
	 */
	private Canvas overview;

	private IAction showOutlineAction, showOverviewAction;

	static final int ID_OUTLINE = 0;

	static final int ID_OVERVIEW = 1;

	private Thumbnail thumbnail;

	IPageSite pSite;

	/**
	 * The constructor
	 * 
	 * @param viewer
	 */
	public DiagramContentOutlinePage(EditPartViewer viewer) {
		super(viewer);
	}

	/**
	 * Sets graphical viewer
	 * 
	 * @param graphicalViewer
	 */
	public void setGraphicalViewer(GraphicalViewer graphicalViewer) {
		this.graphicalViewer = graphicalViewer;
	}
    
    public void update(GraphicalViewer graphicalViewer){
        if(this.graphicalViewer != null){
            if(this.graphicalViewer != graphicalViewer){
                getSelectionSynchronizer().removeViewer(this.graphicalViewer);
                replaceSelectionChangedListeners(graphicalViewer);
                this.graphicalViewer = graphicalViewer;
                getSelectionSynchronizer().addViewer(graphicalViewer);
                initializeOverview();
            }
        }
        
    }

	
	/**
	 * @return graphical viewer
	 */
	public GraphicalViewer getGraphicalViewer() {
		return graphicalViewer;
	}

	/**
	 * sets selection synchronizer
	 * 
	 * @param selectionSynchronizer
	 */
	public void setSelectionSynchronizer(
			SelectionSynchronizer selectionSynchronizer) {
		this.selectionSynchronizer = selectionSynchronizer;
	}

	/**
	 * @return returns selection synchronizer
	 */
	public SelectionSynchronizer getSelectionSynchronizer() {
		return selectionSynchronizer;
	}

	/**
	 * initializator
	 */
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		this.pSite = pageSite;
	}

	/**
	 * Outline viewer configuration
	 * 
	 */
	protected void configureOutlineViewer() {
		getViewer().setEditDomain(editor.getDefaultEditDomain());
		getViewer().setEditPartFactory(new TreePartFactory());
		IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
		showOutlineAction = new Action() {
			public void run() {
				showPage(ID_OUTLINE);
			}
		};
		showOutlineAction.setImageDescriptor(ImageDescriptor.createFromFile(
				VisualEditor.class,"icons/outline.gif")); //$NON-NLS-1$
		tbm.add(showOutlineAction);
		showOverviewAction = new Action() {
			public void run() {
				showPage(ID_OVERVIEW);
			}
		};
		showOverviewAction.setImageDescriptor(ImageDescriptor.createFromFile(
				VisualEditor.class,"icons/overview.gif")); //$NON-NLS-1$
		tbm.add(showOverviewAction);
		showPage(ID_OVERVIEW);
	}

	
	/**
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		pageBook = new PageBook(parent, SWT.NONE);
		outline = getViewer().createControl(pageBook);
		overview = new Canvas(pageBook, SWT.NONE);
		pageBook.showPage(outline);
		configureOutlineViewer();
		hookOutlineViewer();
		initializeOutlineViewer();
	}

	
	/**
	 * @see org.eclipse.ui.part.IPage#dispose()
	 */
	public void dispose() {
		unhookOutlineViewer();
		if (thumbnail != null) {
			thumbnail.deactivate();
			thumbnail = null;
		}
		super.dispose();
	}

	
	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class type) {
		if (type == ZoomManager.class) {
			return ((ScalableFreeformRootEditPart) getGraphicalViewer()
					.getRootEditPart()).getZoomManager();
		}
		return null;
	}

	
	/**
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
	public Control getControl() {
		return pageBook;
	}

	/**
	 * 
	 */
	protected void hookOutlineViewer() {
		getSelectionSynchronizer().addViewer(getViewer());
	}

	/**
	 * 
	 */
	protected void initializeOutlineViewer() {
		setContents(getOrmDiagram());
	}

	
	private DisposeListener disposeListener;

	/**
	 * 
	 */
	protected void initializeOverview() {
		LightweightSystem lws = new LightweightSystem(overview);
		RootEditPart rep = getGraphicalViewer().getRootEditPart();

		if (rep instanceof ScalableFreeformRootEditPart) {
			ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) rep;
			if(this.thumbnail != null) {
				this.thumbnail.deactivate();
			}
			thumbnail = new ScrollableThumbnail((Viewport) root.getFigure());
			thumbnail.setBorder(new MarginBorder(3));
			thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
			lws.setContents(thumbnail);
		}
	}

	public void setContents(Object contents) {
		getViewer().setContents(contents);
	}

	/**
	 * @param id
	 */
	protected void showPage(int id) {

		if (id == ID_OUTLINE) {
			showOutlineAction.setChecked(true);
			showOverviewAction.setChecked(false);
			pageBook.showPage(outline);
			if (thumbnail != null)
				thumbnail.setVisible(false);
		} else if (id == ID_OVERVIEW) {
			if (thumbnail == null)
				initializeOverview();
			showOutlineAction.setChecked(false);
			showOverviewAction.setChecked(true);
			pageBook.showPage(overview);
			thumbnail.setVisible(true);
		}

	}

	/**
	 * 
	 */
	protected void unhookOutlineViewer() {
		getSelectionSynchronizer().removeViewer(getViewer());
	}
	
	Set listeners = new HashSet();

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		super.addSelectionChangedListener(listener);
		listeners.add(listener);
	}
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		super.removeSelectionChangedListener(listener);
		listeners.remove(listener);
	}
	
	void replaceSelectionChangedListeners(GraphicalViewer graphicalViewer) {
		Iterator it = listeners.iterator();
		while(it.hasNext()) {
			ISelectionChangedListener l = (ISelectionChangedListener)it.next();
			getViewer().removeSelectionChangedListener(l);
			graphicalViewer.addSelectionChangedListener(l);
		}
	}

	/**
	 * @return the ormDiagram
	 */
	public OrmDiagram getOrmDiagram() {
		return ormDiagram;
	}

	/**
	 * @param ormDiagram the ormDiagram to set
	 */
	public void setOrmDiagram(OrmDiagram ormDiagram) {
		this.ormDiagram = ormDiagram;
	}

	public VisualEditor getEditor() {
		return editor;
	}

	public void setEditor(VisualEditor editor) {
		this.editor = editor;
	}

}
