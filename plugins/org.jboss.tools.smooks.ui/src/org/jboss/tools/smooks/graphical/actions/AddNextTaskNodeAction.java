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

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
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
			TaskType testTaskType = GraphFactory.eINSTANCE.createTaskType();
			testTaskType.setId(taskID);
			setEnabled(rules.isNextTask(this.getCurrentSelectedTask().get(0),testTaskType));
		}
	}

}
