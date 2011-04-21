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

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.graphics.ext.ProcessType;
import org.jboss.tools.smooks.model.graphics.ext.ProcessesType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;

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
		this.setEnabled((taskList != null && !taskList.isEmpty()));
	}

	@Override
	public void run() {
		super.run();
		if(getProvider() != null){
			ISmooksModelProvider p = getProvider();
			SmooksGraphicsExtType graph = p.getSmooksGraphicsExt();
			if(graph != null){
				ProcessesType processes = graph.getProcesses();
				if(processes != null){
					ProcessType process = processes.getProcess();
					Command remove = RemoveCommand.create(p.getEditingDomain(), getCurrentSelectedTask());
					p.getEditingDomain().getCommandStack().execute(remove);
				}
			}
		}
	}
}
