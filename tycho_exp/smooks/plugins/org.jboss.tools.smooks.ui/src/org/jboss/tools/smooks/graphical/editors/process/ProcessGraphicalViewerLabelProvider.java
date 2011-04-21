/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.graphical.editors.process;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.viewers.IFigureProvider;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.CGraphNode;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.graphical.editors.SmooksProcessGraphicalEditor;
import org.jboss.tools.smooks.graphical.editors.TaskTypeManager;
import org.jboss.tools.smooks.graphical.editors.TaskTypeManager.TaskTypeDescriptor;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;

/**
 * @author Dart
 * 
 */
public class ProcessGraphicalViewerLabelProvider extends LabelProvider implements IFigureProvider, ISelfStyleProvider {

	private SmooksProcessGraphicalEditor processEditor;

	public ProcessGraphicalViewerLabelProvider(SmooksProcessGraphicalEditor graph) {
		this.processEditor = graph;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof TaskType) {
			String id = ((TaskType) element).getId();
			List<TaskTypeDescriptor> des = TaskTypeManager.getAllTaskList();
			for (Iterator<?> iterator = des.iterator(); iterator.hasNext();) {
				TaskTypeDescriptor taskTypeDescriptor = (TaskTypeDescriptor) iterator.next();
				if (taskTypeDescriptor.getId().equals(id)) {
					return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
							taskTypeDescriptor.getImagePath());
				}
			}
		}
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof TaskType) {
			String id = ((TaskType) element).getId();
			String name = ((TaskType) element).getName();
			if (name == null) {
				name = id;
			}
			if (name == null) {
				name = "null";
			}
			return name;
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.zest.core.viewers.IFigureProvider#getFigure(java.lang.Object)
	 */
	public IFigure getFigure(Object element) {
		if (element instanceof TaskType) {
			// if(TaskTypeManager.TASK_ID_INPUT.equals(((TaskType)element).getId())){
			Image image = getImage(element);
			String text = getText(element);
			return new TaskNodeFigure(processEditor, image, text);
			// }
		}
		return null;
	}

	public void selfStyleConnection(Object element, GraphConnection connection) {
		connection.setLineColor(GraphicsConstants.BORDER_CORLOR);
	}

	public void selfStyleNode(Object element, GraphNode node) {
		if (node instanceof CGraphNode) {
			IFigure figure = ((CGraphNode) node).getFigure();
			Dimension size = figure.getLayoutManager().getPreferredSize(figure, -1, -1);
			figure.setSize(size);
		}
	}

}
