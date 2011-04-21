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
package org.jboss.tools.smooks.graphical.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.graphical.editors.TaskTypeManager;
import org.jboss.tools.smooks.model.graphics.ext.ProcessesType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart
 * 
 */
public class DeleteTaskNodeAction extends AbstractProcessGraphAction {

	public DeleteTaskNodeAction(ISmooksModelProvider provider) {
		super("Delete", provider);
	}

	@Override
	protected void init() {
		super.init();
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
	}

	@Override
	public void update() {
		List<TaskType> taskList = this.getCurrentSelectedTask();
		if(!taskList.isEmpty() && taskList.size() == 1){
			TaskType task = taskList.get(0);
			if(TaskTypeManager.TASK_ID_INPUT.equals(task.getId())){
				this.setEnabled(false);
				return;
			}
		}
		this.setEnabled((taskList != null && !taskList.isEmpty() && taskList.size() == 1));
	}

	@Override
	public void run() {
		super.run();
		if (getProvider() != null) {
			ISmooksModelProvider p = getProvider();
			SmooksGraphicsExtType graph = p.getSmooksGraphicsExt();
			if (graph != null) {
				ProcessesType processes = graph.getProcesses();
				if (processes != null) {
					List<TaskType> currentTasks = getCurrentSelectedTask();
					TaskType currentTask = currentTasks.get(0);
					List<TaskType> allTask = new ArrayList<TaskType>();
					SmooksUIUtils.fillAllTask(currentTask, allTask);
					List<Object> associatedElements = new ArrayList<Object>();
					SmooksResourceListType listType = SmooksUIUtils.getSmooks11ResourceListType(p.getSmooksModel());
					if (listType != null) {
						for (Iterator<?> iterator = allTask.iterator(); iterator.hasNext();) {
							TaskType taskType = (TaskType) iterator.next();
							List<Object> elements = TaskTypeManager.getAssociatedSmooksElements(taskType.getId(),
									listType);
							if (elements != null && !elements.isEmpty()) {
								associatedElements.addAll(elements);
							}
						}
					}
					Command remove = null;
					if (associatedElements.isEmpty()) {
						remove = RemoveCommand.create(p.getEditingDomain(), currentTask);
						p.getEditingDomain().getCommandStack().execute(remove);
					} else {
						associatedElements = getDeletedObjects(associatedElements);
						CompoundCommand ccommand = new CompoundCommand();
						ccommand.append(RemoveCommand.create(p.getEditingDomain(), currentTask));
						remove = RemoveCommand.create(p.getEditingDomain(), listType,
								SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP,
								associatedElements);
						if (remove.canExecute()) {
							ccommand.append(remove);
						}
						p.getEditingDomain().getCommandStack().execute(ccommand);
					}
				}
			}
		}
	}

	private List<Object> getDeletedObjects(List<Object> deletingObjects) {
		List<Object> objs = new ArrayList<Object>();
		for (Iterator<?> iterator = deletingObjects.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			EStructuralFeature feature = SmooksUIUtils.getFeature(object);
			if (feature != null) {
				object = FeatureMapUtil.createEntry(feature, object);
			}
			objs.add(object);
		}
		return objs;
	}
}
