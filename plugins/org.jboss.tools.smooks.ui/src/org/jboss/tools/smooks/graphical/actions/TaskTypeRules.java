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

import org.eclipse.emf.ecore.EObject;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.graphical.editors.TaskTypeManager;
import org.jboss.tools.smooks.model.graphics.ext.ProcessType;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;

/**
 * @author Dart
 * 
 */
public class TaskTypeRules {

	public boolean isNextTask(TaskType currentTask, TaskType testTask) {
		TaskType parentTask = testTask;
		String parentID = parentTask.getId();

		EObject parent = currentTask;
		while (!(parent instanceof ProcessType)) {
			parent = parent.eContainer();
		}

		List<TaskType> currentList = ((ProcessType) parent).getTask();
		List<TaskType> taskList = new ArrayList<TaskType>();
		for (Iterator<?> iterator = currentList.iterator(); iterator.hasNext();) {
			TaskType taskType = (TaskType) iterator.next();
			SmooksUIUtils.fillAllTask(taskType, taskList);
		}

		if (parentID.equals(TaskTypeManager.TASK_ID_INPUT)) {
			// if (!SmooksConstants.TASK_ID_JAVA_MAPPING.equals(taskID))
			return false;
		}
		if (parentID.equals(TaskTypeManager.TASK_ID_FREEMARKER_TEMPLATE)) {
			if (!TaskTypeManager.TASK_ID_JAVA_MAPPING.equals(currentTask.getId())) {
				return false;
			}
			return true;
		}

		if (parentID.equals(TaskTypeManager.TASK_ID_JAVA_MAPPING)) {
			if (!TaskTypeManager.TASK_ID_INPUT.equals(currentTask.getId())) {
				return false;
			} else {
				for (Iterator<?> iterator = taskList.iterator(); iterator.hasNext();) {
					TaskType taskType = (TaskType) iterator.next();
					if (TaskTypeManager.TASK_ID_JAVA_MAPPING.equals(taskType.getId())) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	public boolean isPreTask(TaskType currentTask, TaskType testTask) {
		TaskType parentTask = testTask;
		String currentTaskID = parentTask.getId();
		if (currentTaskID.equals(TaskTypeManager.TASK_ID_INPUT)) {
			if (!TaskTypeManager.TASK_ID_JAVA_MAPPING.equals(currentTask.getId())) {
				return false;
			} else {
				List<?> taskList = parentTask.getTask();
				for (Iterator<?> iterator = taskList.iterator(); iterator.hasNext();) {
					TaskType object = (TaskType) iterator.next();
					if (TaskTypeManager.TASK_ID_JAVA_MAPPING.equals(object.getId())) {
						return false;
					}
				}
			}
		}
		if (currentTaskID.equals(TaskTypeManager.TASK_ID_JAVA_MAPPING)) {
			// if (!SmooksConstants.TASK_ID_JAVA_MAPPING.equals(taskID))
			return false;
		}
		return true;
	}
}
