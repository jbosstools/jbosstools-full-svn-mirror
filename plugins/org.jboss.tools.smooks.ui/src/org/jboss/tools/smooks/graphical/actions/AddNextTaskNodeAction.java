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

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.GraphPackage;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;

/**
 * @author Dart
 * 
 */
public class AddNextTaskNodeAction extends AddTaskNodeAction {

	public AddNextTaskNodeAction(String taskID, String text, ISmooksModelProvider provider) {
		super(taskID, text, provider);
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void run() {
		super.run();
		if (this.provider != null) {
			TaskType parentTask = this.getCurrentSelectedTask().get(0);
			TaskType childTask = GraphFactory.eINSTANCE.createTaskType();
			childTask.setId(taskID);
			childTask.setName(this.getText());
			Command command = AddCommand.create(provider.getEditingDomain(), parentTask,
					GraphPackage.Literals.TASK_TYPE__TASK, childTask);
			provider.getEditingDomain().getCommandStack().execute(command);
		}
	}

	@Override
	public void update() {
		super.update();
		if (this.isEnabled()) {
			TaskType parentTask = this.getCurrentSelectedTask().get(0);
			String currentTaskID = parentTask.getId();
			if (currentTaskID.equals(SmooksConstants.TASK_ID_INPUT)) {
				if (!SmooksConstants.TASK_ID_JAVA_MAPPING.equals(taskID)){
					this.setEnabled(false);
				}else{
					List<?> taskList = parentTask.getTask();
					for (Iterator<?> iterator = taskList.iterator(); iterator.hasNext();) {
						TaskType object = (TaskType) iterator.next();
						if(SmooksConstants.TASK_ID_JAVA_MAPPING.equals(object.getId())){
							this.setEnabled(false);
						}
					}
				}
			}
			if (currentTaskID.equals(SmooksConstants.TASK_ID_JAVA_MAPPING)) {
//				if (!SmooksConstants.TASK_ID_JAVA_MAPPING.equals(taskID))
					this.setEnabled(false);
			}
		}
	}

}
