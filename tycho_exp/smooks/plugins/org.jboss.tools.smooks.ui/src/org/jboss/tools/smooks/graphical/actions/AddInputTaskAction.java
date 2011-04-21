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
import org.jboss.tools.smooks.graphical.editors.TaskTypeManager;
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.GraphPackage;
import org.jboss.tools.smooks.model.graphics.ext.ProcessType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;

/**
 * @author Dart
 *
 */
public class AddInputTaskAction extends AddNextTaskNodeAction {
	
	
	
	public AddInputTaskAction(ISmooksModelProvider provider) {
		super(TaskTypeManager.TASK_ID_INPUT, "Add Input Task", provider);
	}

	@Override
	public void run() {
		if (this.provider != null) {
			SmooksGraphicsExtType graph = provider.getSmooksGraphicsExt();
			ProcessType process = graph.getProcesses().getProcess();
			if (process != null && process.getTask().isEmpty()) {
				TaskType childTask = GraphFactory.eINSTANCE.createTaskType();
				childTask.setId(taskID);
				childTask.setName("Input Task");
				Command command = AddCommand.create(provider.getEditingDomain(), process,
						GraphPackage.Literals.PROCESS_TYPE__TASK, childTask);
				provider.getEditingDomain().getCommandStack().execute(command);
			}
		}
	}

	@Override
	public void update() {
		this.setEnabled(false);
		SmooksGraphicsExtType graph = provider.getSmooksGraphicsExt();
		ProcessType process = graph.getProcesses().getProcess();
		if (process != null && process.getTask().isEmpty()) {
			this.setEnabled(true);
		}
	}
}
