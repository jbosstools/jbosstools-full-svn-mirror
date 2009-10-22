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
import java.util.List;

import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;

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
		}
		return allTaskList;
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
