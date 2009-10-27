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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.ProcessType;
import org.jboss.tools.smooks.model.graphics.ext.ProcessesType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks.model.xsl.Xsl;

/**
 * @author Dart
 * 
 */
public class ProcessAnalyzer {

	public List<String> analyzeTaskID(SmooksResourceListType resourceList) {
		List<String> taskIDs = new ArrayList<String>();
		List<AbstractResourceConfig> resourceConfigList = resourceList.getAbstractResourceConfig();
		for (Iterator<?> iterator = resourceConfigList.iterator(); iterator.hasNext();) {
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator.next();

			// for java-mapping
			if (abstractResourceConfig instanceof BeanType || abstractResourceConfig instanceof BindingsType) {
				if (taskIDs.contains(TaskTypeManager.TASK_ID_JAVA_MAPPING)) {
					continue;
				}
				taskIDs.add(TaskTypeManager.TASK_ID_JAVA_MAPPING);
			}
			// for freemarker template
			if (abstractResourceConfig instanceof Freemarker) {
				if (taskIDs.contains(TaskTypeManager.TASK_ID_FREEMARKER_TEMPLATE)) {
					continue;
				}
				taskIDs.add(TaskTypeManager.TASK_ID_FREEMARKER_TEMPLATE);
			}
			// for xsl template
			if (abstractResourceConfig instanceof Xsl) {
				// if (taskIDs.contains(TaskTypeManager.TASK_ID_JAVA_MAPPING)) {
				// continue;
				// }
				// taskIDs.add(TaskTypeManager.TASK_ID_JAVA_MAPPING);
			}
		}
		return taskIDs;
	}

	private SmooksGraphicsExtType getSmooksGraphicsType(SmooksResourceListType resouceList) {
		List<AbstractResourceConfig> resourceConfigList = resouceList.getAbstractResourceConfig();
		for (Iterator<?> iterator = resourceConfigList.iterator(); iterator.hasNext();) {
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator.next();
			if (abstractResourceConfig instanceof SmooksGraphicsExtType) {
				return (SmooksGraphicsExtType) abstractResourceConfig;
			}
		}
		return null;
	}

	private void fillAllTask(TaskType task, List<TaskType> taskList) {
		taskList.add(task);
		List<TaskType> children = task.getTask();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			TaskType taskType = (TaskType) iterator.next();
			fillAllTask(taskType, taskList);
		}
	}

	/**
	 * 
	 * @param resourceList
	 * @return If the smooks-resource-list was changed in this method return
	 *         <code>true</code>
	 */
	public boolean analyzeSmooksModels(SmooksResourceListType resourceList) {
		boolean modelWasChanged = false;
		SmooksGraphicsExtType ext = getSmooksGraphicsType(resourceList);
		if (ext == null)
			throw new RuntimeException("Can't find the smooks-graph-ext element");

		ProcessesType processes = ext.getProcesses();
		if (processes == null) {
			processes = GraphFactory.eINSTANCE.createProcessesType();
			ext.setProcesses(processes);
			modelWasChanged = true;
		}
		ProcessType process = null;
		if (processes != null) {
			process = processes.getProcess();
		}

		if (process == null) {
			process = GraphFactory.eINSTANCE.createProcessType();
			processes.setProcess(process);
			modelWasChanged = true;
		}

		List<TaskType> currentList = process.getTask();
		List<TaskType> taskList = new ArrayList<TaskType>();
		for (Iterator<?> iterator = currentList.iterator(); iterator.hasNext();) {
			TaskType taskType = (TaskType) iterator.next();
			this.fillAllTask(taskType, taskList);
		}

		if (taskList.isEmpty()) {
			TaskType inputTask = GraphFactory.eINSTANCE.createTaskType();
			inputTask.setId(TaskTypeManager.TASK_ID_INPUT);
			inputTask.setName(TaskTypeManager.getTaskLabel(TaskTypeManager.TASK_ID_INPUT));
			process.getTask().add(inputTask);
			taskList.add(inputTask);
			modelWasChanged = true;
		}

		List<String> taskIDs = analyzeTaskID(resourceList);

		for (Iterator<String> iterator = taskIDs.iterator(); iterator.hasNext();) {
			String taskId = (String) iterator.next();
			if (!taskIDIsExist(taskId, taskList)) {
				TaskType task = GraphFactory.eINSTANCE.createTaskType();
				task.setId(taskId);
				task.setName(TaskTypeManager.getTaskLabel(taskId));
				taskList.add(task);
			}
		}
		modelWasChanged = linkTask(taskList);
		return modelWasChanged;
	}

	private TaskType getTaskType(String id, List<TaskType> taskList) {
		for (Iterator<?> iterator = taskList.iterator(); iterator.hasNext();) {
			TaskType taskType = (TaskType) iterator.next();
			if (id.equals(taskType.getId())) {
				return taskType;
			}
		}
		return null;
	}

	private boolean linkTask(List<TaskType> taskList) {
		TaskType first = getTaskType(TaskTypeManager.TASK_ID_INPUT, taskList);
		if (first != null) {
			return linkTask(first, taskList);
		}
		return false;
	}

	private boolean linkTask(TaskType taskType, List<TaskType> taskList) {
		String id = taskType.getId();
		String[] childrenIds = TaskTypeManager.getChildTaskIDs(id);
		boolean changed = false;
		if (childrenIds != null) {
			for (int i = 0; i < childrenIds.length; i++) {
				String childId = childrenIds[i];
				TaskType childTask = getTaskType(childId, taskList);
				if (childTask != null) {
					if (!taskType.getTask().contains(childTask)
							&& ((childTask.eContainer() == null) || (childTask.eContainer() instanceof ProcessType))) {
						taskType.getTask().add(childTask);
						changed = true;
					}
					taskList.remove(childTask);
					boolean cchange = linkTask(childTask, taskList);
					changed = (changed || cchange);
				}
			}
		}
		return changed;
	}

	private boolean taskIDIsExist(String taskId, List<TaskType> taskList) {
		for (Iterator<?> iterator = taskList.iterator(); iterator.hasNext();) {
			TaskType task = (TaskType) iterator.next();
			if (task.getId().equals(taskId)) {
				return true;
			}
		}
		return false;
	}

	// private boolean taskIsExist(TaskType task , List<String> taskIDs){
	// for (Iterator<?> iterator = taskIDs.iterator(); iterator.hasNext();) {
	// String id = (String) iterator.next();
	// if(task.getId().equals(id)){
	// return true;
	// }
	// }
	// return false;
	// }
}
