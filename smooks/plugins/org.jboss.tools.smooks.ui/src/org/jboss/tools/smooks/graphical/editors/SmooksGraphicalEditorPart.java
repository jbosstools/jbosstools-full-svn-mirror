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
package org.jboss.tools.smooks.graphical.editors;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.configuration.editors.SelectoreSelectionDialog;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaBeanList;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaBeanModel;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanContentProvider;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanlabelProvider;
import org.jboss.tools.smooks.configuration.editors.xml.TagList;
import org.jboss.tools.smooks.configuration.editors.xml.TagObject;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataContentProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataLabelProvider;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.common.RootModel;
import org.jboss.tools.smooks.gef.tree.model.TreeContainerModel;
import org.jboss.tools.smooks.graphical.editors.model.InputDataContianerModel;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;

/**
 * @author Dart
 * 
 */
public class SmooksGraphicalEditorPart extends GraphicalEditor {

	private DefaultEditDomain editDomain = null;

	private SmooksMultiFormEditor formEditor = null;

	public SmooksGraphicalEditorPart(SmooksMultiFormEditor formEditor) {
		super();
		this.editDomain = new DefaultEditDomain(this);
		this.editDomain.getCommandStack().addCommandStackEventListener(new CommandStackEventListener() {
			
			public void stackChanged(CommandStackEvent event) {
				firePropertyChange(PROP_DIRTY);
			}
		});
		this.formEditor = formEditor;
		this.setEditDomain(editDomain);
	}

	public DefaultEditDomain getEditDomain() {
		return editDomain;
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getGraphicalViewer().setEditDomain(editDomain);
		getGraphicalViewer().setEditPartFactory(new SmooksEditFactory());

		getGraphicalViewer().setRootEditPart(new FreeformGraphicalRootEditPart());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	@Override
	protected void initializeGraphicalViewer() {
		Object obj = formEditor.getSmooksModel();
		if (obj instanceof DocumentRoot) {
			List<Object> inputDatas = SelectoreSelectionDialog.generateInputData(formEditor.getSmooksGraphicsExt(),
					((DocumentRoot) obj).getSmooksResourceList());
			RootModel root = new RootModel();
			for (Iterator<?> iterator = inputDatas.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				ITreeContentProvider contentProvider = new XMLStructuredDataContentProvider();
				ILabelProvider labelProvider = new XMLStructuredDataLabelProvider();
				Object containerModel = null;

				if (object instanceof JavaBeanModel) {
					contentProvider = new JavabeanContentProvider();
					labelProvider = new JavabeanlabelProvider();
					containerModel = new JavaBeanList();
					((JavaBeanList) containerModel).addJavaBean((JavaBeanModel) object);
				}
				if (object instanceof TagObject) {
					containerModel = new TagList();
					((TagList) containerModel).addRootTag((TagObject) object);
				}
				if (containerModel != null) {
					TreeContainerModel container = new InputDataContianerModel(containerModel, contentProvider,
							labelProvider);
					root.addTreeNode(container);
				}
			}
			getGraphicalViewer().setContents(root);
		}
	}
	
	

	@Override
	public Object getAdapter(Class type) {
		if(type == ISmooksModelProvider.class){
			return this.formEditor;
		}
		return super.getAdapter(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

}
