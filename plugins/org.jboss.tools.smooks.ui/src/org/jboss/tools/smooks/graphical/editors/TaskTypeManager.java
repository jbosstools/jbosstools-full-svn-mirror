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

import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.model.freemarker.Freemarker;
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
public class TaskTypeManager {

	public static final String TASK_ID_INPUT = "input";

	public static final String TASK_ID_JAVA_MAPPING = "java_mapping";

	public static final String TASK_ID_XSL_TEMPLATE = "xsl_template";

	public static final String TASK_ID_FREEMARKER_TEMPLATE = "freemarker_template";

	private static List<TaskTypeDescriptor> allTaskList = null;

	public static String[] getChildTaskIDs(String parentId) {
		if (parentId == null)
			return null;
		if (parentId.equals(TaskTypeManager.TASK_ID_INPUT)) {
			return new String[] { TaskTypeManager.TASK_ID_JAVA_MAPPING, TaskTypeManager.TASK_ID_XSL_TEMPLATE,
					TaskTypeManager.TASK_ID_FREEMARKER_TEMPLATE };
		}
		if (parentId.equals(TaskTypeManager.TASK_ID_JAVA_MAPPING)) {
			return new String[] { TaskTypeManager.TASK_ID_XSL_TEMPLATE, TaskTypeManager.TASK_ID_FREEMARKER_TEMPLATE };
		}
		return null;
	}

	/**
	 * @return the allTaskList
	 */
	public static List<TaskTypeDescriptor> getAllTaskList() {
		if (allTaskList == null) {
			allTaskList = new ArrayList<TaskTypeDescriptor>();

			// init
			allTaskList.add(new TaskTypeDescriptor(TASK_ID_INPUT, "Input", GraphicsConstants.IMAGE_INPUT_TASK));
			allTaskList.add(new TaskTypeDescriptor(TASK_ID_JAVA_MAPPING, "Java Mapping",
					GraphicsConstants.IMAGE_JAVA_AMPPING_TASK));
			allTaskList.add(new TaskTypeDescriptor(TASK_ID_FREEMARKER_TEMPLATE, "Apply Freemaker Template",
					GraphicsConstants.IMAGE_APPLY_FREEMARKER_TASK));
			allTaskList.add(new TaskTypeDescriptor(TASK_ID_XSL_TEMPLATE, "Apply XSL Template",
					GraphicsConstants.IMAGE_APPLY_XSL_TASK));
		}
		return allTaskList;
	}

	public static List<Object> getAssociatedSmooksElementsType(TaskType taskType) {
		return null;
	}

	public static List<Object> getAssociatedSmooksElementsType(String taskID) {
		List<Object> elementsType = new ArrayList<Object>();
		if (taskID == null)
			return null;
		if (TASK_ID_JAVA_MAPPING.equals(taskID)) {
			elementsType.add(BeanType.class);
			elementsType.add(BindingsType.class);
		}
		if (TASK_ID_FREEMARKER_TEMPLATE.equals(taskID)) {
			elementsType.add(Freemarker.class);
		}
		if (TASK_ID_XSL_TEMPLATE.equals(taskID)) {
			elementsType.add(Xsl.class);
		}
		return elementsType;
	}

	public static String getTaskLabel(String taskId) {
		if (taskId != null) {
			if (taskId.equals(TASK_ID_FREEMARKER_TEMPLATE)) {
				return "Apply Freemarker Template";
			}
			if (taskId.equals(TASK_ID_INPUT)) {
				return "Input Task";
			}
			if (taskId.equals(TASK_ID_JAVA_MAPPING)) {
				return "Java Mapping";
			}
			if (taskId.equals(TASK_ID_XSL_TEMPLATE)) {
				return "Apply XSL Template";
			}
		}
		return "";
	}

	/**
	 * 
	 * @param taskID
	 * @param smooksResourceList
	 * @return
	 */
	public static List<Object> getAssociatedSmooksElements(String taskID, SmooksResourceListType smooksResourceList) {
		List<Object> elementTypes = getAssociatedSmooksElementsType(taskID);
		List<AbstractResourceConfig> resourceConfigList = smooksResourceList.getAbstractResourceConfig();
		List<Object> associatedElements = new ArrayList<Object>();
		for (Iterator<?> iterator = resourceConfigList.iterator(); iterator.hasNext();) {
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator.next();
			if (isSameType(abstractResourceConfig, elementTypes)) {
				associatedElements.add(abstractResourceConfig);
			}
		}
		return associatedElements;
	}

	private static boolean isSameType(Object element, List<Object> elementTypes) {
		for (Iterator<?> iterator = elementTypes.iterator(); iterator.hasNext();) {
			Class<?> object = (Class<?>) iterator.next();
			if (object.isInstance(element)) {
				return true;
			}
		}
		return false;
	}

	public static final class TaskTypeDescriptor {

		private String id;

		private String label;

		private String imagePath;

		public TaskTypeDescriptor(String id, String label, String imagePath) {
			this.id = id;
			this.label = label;
			this.imagePath = imagePath;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * @param label
		 *            the label to set
		 */
		public void setLabel(String label) {
			this.label = label;
		}

		/**
		 * @return the imagePath
		 */
		public String getImagePath() {
			return imagePath;
		}

		/**
		 * @param imagePath
		 *            the imagePath to set
		 */
		public void setImagePath(String imagePath) {
			this.imagePath = imagePath;
		}

	}

}
