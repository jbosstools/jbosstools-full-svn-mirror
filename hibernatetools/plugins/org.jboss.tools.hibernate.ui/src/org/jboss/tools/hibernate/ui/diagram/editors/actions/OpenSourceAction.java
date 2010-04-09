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
package org.jboss.tools.hibernate.ui.diagram.editors.actions;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.mediator.x.mapping.ColumnStub;
import org.hibernate.mediator.x.mapping.PersistentClassStub;
import org.hibernate.mediator.x.mapping.PropertyStub;
import org.hibernate.mediator.x.mapping.TableStub;
import org.jboss.tools.hibernate.ui.diagram.DiagramViewerMessages;
import org.jboss.tools.hibernate.ui.diagram.UiPlugin;
import org.jboss.tools.hibernate.ui.diagram.editors.DiagramViewer;
import org.jboss.tools.hibernate.ui.diagram.editors.model.Connection;
import org.jboss.tools.hibernate.ui.diagram.editors.model.Shape;
import org.jboss.tools.hibernate.ui.view.DiagramEditorInput;

/**
 * @author Dmitry Geraskov
 *
 */
public class OpenSourceAction extends SelectionAction {
	public static final String ACTION_ID = "org.jboss.tools.hibernate.ui.diagram.editors.actions.open.source"; //$NON-NLS-1$
	public static final ImageDescriptor img = 
		UiPlugin.getImageDescriptor("icons/java.gif"); //$NON-NLS-1$

	public OpenSourceAction(IWorkbenchPart part) {
		super(part);
		setId(ACTION_ID);
		setText(DiagramViewerMessages.OpenSourceAction_open_source_file);
		setImageDescriptor(img);
	}

	public void run() {
		DiagramEditorInput objectEditorInput = (DiagramEditorInput)((DiagramViewer)getWorkbenchPart()).getEditorInput();
		ConsoleConfiguration consoleConfig = objectEditorInput.getConsoleConfig();

		DiagramViewer part = (DiagramViewer)getWorkbenchPart();
		Set<Shape> selectedElements = part.getSelectedElements();

		IEditorPart editorPart = null;
		Iterator<Shape> iterator = selectedElements.iterator();
		// open only first editor - no sense to open all of them
		while (iterator.hasNext() && editorPart == null) {
			Shape shape = iterator.next();
			Object selection = shape.getOrmElement();
			if (selection instanceof ColumnStub || selection instanceof TableStub) {
				Iterator<Connection> targetConnections = shape.getTargetConnections().iterator();
				while (targetConnections.hasNext()) {
					Connection connection = targetConnections.next();
					Shape sh1 = connection.getSource();
					Shape sh2 = connection.getTarget();
					if (shape == sh1 && sh2 != null) {
						shape = sh2;
						break;
					} else if (shape == sh2 && sh1 != null) {
						shape = sh1;
						break;
					}
				}
				selection = shape.getOrmElement();
			}
			PersistentClassStub rootClass = null;
			if (selection instanceof PersistentClassStub) {
				rootClass = (PersistentClassStub) selection;
			} else if (selection instanceof PropertyStub) {
				rootClass = ((PropertyStub) selection).getPersistentClass();
			} else {
				continue;
			}

			String fullyQualifiedName = rootClass.getClassName();//HibernateUtils.getPersistentClassName(rootClass);
			/*if (fullyQualifiedName.indexOf("$") > 0) {
				fullyQualifiedName = fullyQualifiedName.substring(0, fullyQualifiedName.indexOf("$"));
			}*/
			try {
				editorPart = org.hibernate.eclipse.console.actions.OpenSourceAction.run(consoleConfig, selection, fullyQualifiedName);
			} catch (CoreException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage(DiagramViewerMessages.OpenSourceAction_canot_open_source_file, e);
			} catch (FileNotFoundException e) {
				HibernateConsolePlugin.getDefault().logErrorMessage(DiagramViewerMessages.OpenSourceAction_canot_find_source_file, e);
			}
		}
	}

	protected boolean calculateEnabled() {
		DiagramViewer part = (DiagramViewer)getWorkbenchPart();
		return part.getSelectedElements().size() > 0;
	}
}
