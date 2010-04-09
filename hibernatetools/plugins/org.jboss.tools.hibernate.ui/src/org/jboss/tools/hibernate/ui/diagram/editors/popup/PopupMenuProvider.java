/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.ui.diagram.editors.popup;

import java.util.Iterator;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.actions.ActionFactory;
import org.hibernate.mediator.x.mapping.Column;
import org.hibernate.mediator.x.mapping.PersistentClass;
import org.hibernate.mediator.x.mapping.Property;
import org.hibernate.mediator.x.mapping.TableStub;
import org.jboss.tools.hibernate.ui.diagram.editors.actions.AutoLayoutAction;
import org.jboss.tools.hibernate.ui.diagram.editors.actions.ExportImageAction;
import org.jboss.tools.hibernate.ui.diagram.editors.actions.OpenMappingAction;
import org.jboss.tools.hibernate.ui.diagram.editors.actions.OpenSourceAction;
import org.jboss.tools.hibernate.ui.diagram.editors.actions.ToggleConnectionsAction;
import org.jboss.tools.hibernate.ui.diagram.editors.actions.ToggleShapeExpandStateAction;
import org.jboss.tools.hibernate.ui.diagram.editors.actions.ToggleShapeVisibleStateAction;
import org.jboss.tools.hibernate.ui.diagram.editors.model.ExpandableShape;
import org.jboss.tools.hibernate.ui.diagram.editors.model.OrmShape;
import org.jboss.tools.hibernate.ui.diagram.editors.model.Shape;
import org.jboss.tools.hibernate.ui.diagram.editors.parts.OrmEditPart;

/**
 * Context menu provider for Diagram Viewer and Diagram Outline.
 * 
 * @author ?
 * @author Vitali Yemialyanchyk
 */
public class PopupMenuProvider extends ContextMenuProvider {

    public static final String GROUP_OPEN_SOURCE = "open_source"; //$NON-NLS-1$
    public static final String GROUP_EDIT = "edit"; //$NON-NLS-1$
    public static final String GROUP_ADDITIONAL_ACTIONS = "additional_actions"; //$NON-NLS-1$

    private ActionRegistry actionRegistry;

	public PopupMenuProvider(EditPartViewer viewer, ActionRegistry actionRegistry) {
		super(viewer);
		this.actionRegistry = actionRegistry;
	}
    		
	@SuppressWarnings("unchecked")
	public void buildContextMenu(IMenuManager menu) {
		
		menu.add(new Separator(GROUP_OPEN_SOURCE));
		menu.add(new Separator(GROUP_EDIT));
		menu.add(new Separator(GROUP_ADDITIONAL_ACTIONS));
		// Add standard action groups to the menu
		//GEFActionConstants.addStandardActionGroups(menu);
		IAction action = null;
		if (getViewer().getSelection() instanceof StructuredSelection) {
			Shape selectedShape = null;
			IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();
			if (selection != null) {
				Object firstElement = selection.getFirstElement();
				Object obj = null;
				if (firstElement instanceof OrmEditPart) {
					obj = ((OrmEditPart)firstElement).getModel();
				} else if (firstElement instanceof AbstractTreeEditPart) {
					obj = ((AbstractTreeEditPart)firstElement).getModel();
				}
				if (null != obj && obj instanceof Shape) {
					selectedShape = (Shape)obj;
				} 
			}			
			if (selectedShape != null && selection.size() == 1) {
				Object first = selectedShape.getOrmElement();
				if (first instanceof PersistentClass
						|| first.getClass() == Property.class
						|| first instanceof TableStub
						|| first instanceof Column) {
					action = getActionRegistry().getAction(OpenSourceAction.ACTION_ID);
					appendToGroup(GROUP_OPEN_SOURCE, action);
					createMenuItem(getMenu(), action);
					
					action = getActionRegistry().getAction(OpenMappingAction.ACTION_ID);
					appendToGroup(GROUP_OPEN_SOURCE, action);
					createMenuItem(getMenu(), action);					
				}
			}
			boolean addToggleVisibleStateMenu = false;
			boolean addToggleExpandStateMenu = false;
		    Iterator it = selection.iterator();
		    while (it.hasNext() && (!addToggleVisibleStateMenu || !addToggleExpandStateMenu)) {
		    	Object element = it.next();
				Object obj = null;
				if (element instanceof OrmEditPart) {
					obj = ((OrmEditPart)element).getModel();
				} else if (element instanceof AbstractTreeEditPart) {
					obj = ((AbstractTreeEditPart)element).getModel();
				}
				if (null != obj && obj instanceof OrmShape) {
					selectedShape = (Shape)obj;
					Object first = selectedShape.getOrmElement();
					if (first instanceof PersistentClass || first instanceof TableStub) {
						addToggleVisibleStateMenu = true;
					}
				}
				if (null != obj && obj instanceof ExpandableShape) {
					addToggleExpandStateMenu = true;
				} 
		    }
			if (addToggleVisibleStateMenu) {
				action = getActionRegistry().getAction(ToggleShapeVisibleStateAction.ACTION_ID);
				appendToGroup(GROUP_EDIT, action);
				createMenuItem(getMenu(), action);
			}
			if (addToggleExpandStateMenu) {
				action = getActionRegistry().getAction(ToggleShapeExpandStateAction.ACTION_ID);
				appendToGroup(GROUP_EDIT, action);
				createMenuItem(getMenu(), action);
			}
		}

		action = getActionRegistry().getAction(ToggleConnectionsAction.ACTION_ID);
		appendToGroup(GROUP_EDIT, action);
		createMenuItem(getMenu(), action);
		
		action = getActionRegistry().getAction(AutoLayoutAction.ACTION_ID);
		appendToGroup(GROUP_ADDITIONAL_ACTIONS, action);
		createMenuItem(getMenu(), action);
		
		//action = getActionRegistry().getAction(CollapseAllAction.ACTION_ID);
		//appendToGroup(GROUP_EDIT, action);
		//createMenuItem(getMenu(), action);
		
		//action = getActionRegistry().getAction(ExpandAllAction.ACTION_ID);
		//appendToGroup(GROUP_EDIT, action);
		//createMenuItem(getMenu(), action);
		
		action = getActionRegistry().getAction(ExportImageAction.ACTION_ID);
		appendToGroup(GROUP_ADDITIONAL_ACTIONS, action);
		createMenuItem(getMenu(), action);

		// Add actions to the menu
		/** /
		// is undo/redo operation so necessary for popup menu?
		menu.appendToGroup(
				GEFActionConstants.GROUP_UNDO, // target group id
				getAction(ActionFactory.UNDO.getId())); // action to add
		menu.appendToGroup(
				GEFActionConstants.GROUP_UNDO, 
				getAction(ActionFactory.REDO.getId()));
		/**/
		menu.appendToGroup(
				GROUP_EDIT, // target group id
				getAction(ActionFactory.SELECT_ALL.getId())); // action to add
	}

	private IAction getAction(String actionId) {
		return actionRegistry.getAction(actionId);
	}

	public void createMenuItem(Menu menu, IAction action) {
		boolean enabled = action.isEnabled();
		boolean hidden = false;
		if (hidden) {
			return;
		}
		MenuItem item = new MenuItem(menu, SWT.CASCADE | SWT.RADIO | SWT.CHECK);
		String displayName = action.getText();
		item.addSelectionListener(new AL(action));
		item.setText(displayName);
		item.setEnabled(enabled);
	}

	static class AL implements SelectionListener {
		IAction action;

		public AL(IAction action) {
			this.action = action;
		}

		public void widgetSelected(SelectionEvent e) {
			action.run();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}
	}

	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}
}
