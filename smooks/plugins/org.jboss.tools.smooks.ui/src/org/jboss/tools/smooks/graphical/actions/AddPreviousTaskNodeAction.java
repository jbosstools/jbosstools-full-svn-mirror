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
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.GraphPackage;
import org.jboss.tools.smooks.model.graphics.ext.ProcessType;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;

/**
 * @author Dart
 * 
 */
public class AddPreviousTaskNodeAction extends AddTaskNodeAction {

	public AddPreviousTaskNodeAction(String taskID, String text, ISmooksModelProvider provider) {
		super(taskID, text, provider);
		// TODO Auto-generated constructor stub
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
			EObject pp = parentTask.eContainer();
			TaskType childTask = GraphFactory.eINSTANCE.createTaskType();
			childTask.setId(taskID);
			childTask.setName(this.getText());
			CompoundCommand cc = new CompoundCommand();
			Command command = DeleteCommand.create(provider.getEditingDomain(), parentTask);
			cc.append(command);
			childTask.getTask().add((TaskType) EcoreUtil.copy(parentTask));
			EStructuralFeature feature = null;
			if (pp instanceof ProcessType) {
				feature = GraphPackage.Literals.PROCESS_TYPE__TASK;
			}
			if (pp instanceof TaskType) {
				feature = GraphPackage.Literals.TASK_TYPE__TASK;
			}
			if (feature != null) {
				Command command1 = AddCommand.create(provider.getEditingDomain(), pp, feature, childTask);
				cc.append(command1);
				try {
					provider.getEditingDomain().getCommandStack().execute(cc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void update() {
		super.update();
		if (this.isEnabled()) {
			TaskType parentTask = this.getCurrentSelectedTask().get(0);
			String parentID = parentTask.getId();

			if (parentID.equals(SmooksConstants.TASK_ID_INPUT)) {
				// if (!SmooksConstants.TASK_ID_JAVA_MAPPING.equals(taskID))
				this.setEnabled(false);
			}

			if (parentID.equals(SmooksConstants.TASK_ID_JAVA_MAPPING)) {
				if (!SmooksConstants.TASK_ID_INPUT.equals(taskID)) {
					this.setEnabled(false);
				} else {
					EObject obj = parentTask.eContainer();
					if(obj instanceof TaskType){
						if (SmooksConstants.TASK_ID_INPUT.equals(((TaskType)obj).getId())) {
							this.setEnabled(false);
						}
					}
				}
			}
		}
	}

}
