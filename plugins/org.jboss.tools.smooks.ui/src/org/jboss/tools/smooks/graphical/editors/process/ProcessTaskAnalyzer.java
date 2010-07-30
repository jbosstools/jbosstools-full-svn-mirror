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
package org.jboss.tools.smooks.graphical.editors.process;

import java.util.Iterator;
import java.util.List;

import org.jboss.tools.smooks.graphical.editors.TaskTypeManager;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.IComponent;
import org.jboss.tools.smooks.model.javabean.IBean;
import org.milyn.javabean.dynamic.Model;

/**
 * @author Dart
 * 
 */
public class ProcessTaskAnalyzer {

	public void analyzeTaskNode(ProcessType process,
			Model<SmooksModel> rootSmooksModel) {
		process.getTask().clear();
		if (rootSmooksModel == null)
			return;
		// Input task node must be in process:
		TaskType inputTask = ProcessFactory.eINSTANCE.createTaskType();
		inputTask.setId(TaskTypeManager.TASK_ID_INPUT);
		inputTask.setName(TaskTypeManager.getTaskLabel(inputTask));

		process.addTask(inputTask);
		

		List<IComponent> compoenents = rootSmooksModel.getModelRoot()
				.getComponents();

		// the java mapping is the next task node of input task:
		TaskType javaMappingTask = null;
		for (Iterator<?> iterator = compoenents.iterator(); iterator.hasNext();) {
			IComponent component = (IComponent) iterator.next();
			if (component instanceof IBean) {
				javaMappingTask = ProcessFactory.eINSTANCE.createTaskType();
				javaMappingTask.setId(TaskTypeManager.TASK_ID_JAVA_MAPPING);
				javaMappingTask.setName(TaskTypeManager
						.getTaskLabel(javaMappingTask));
				inputTask.addTask(javaMappingTask);
				break;
			}
		}

		// the apply template is the next task node of java mapping task:
		if (javaMappingTask != null) {
//			for (Iterator<?> iterator = compoenents.iterator(); iterator
//					.hasNext();) {
//				Component component = (Component) iterator.next();
//				if (component instanceof FreeMarkerTemplate) {
//					TemplateAppyTaskNode templateTask = (TemplateAppyTaskNode) ProcessFactory.eINSTANCE
//							.createTemplateTask();
//					templateTask.setType("XML");
//					templateTask.addSmooksModel(component);
//					templateTask.setName(TaskTypeManager
//							.getTaskLabel(templateTask));
//					javaMappingTask.addTask(templateTask);
//				}
//			}
//			for (Iterator<?> iterator = resourceConfigList.iterator(); iterator
//					.hasNext();) {
//				AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator
//						.next();
//				if (abstractResourceConfig instanceof Freemarker) {
//					String messageType = SmooksModelUtils.getParamValue(
//							((Freemarker) abstractResourceConfig).getParam(),
//							SmooksModelUtils.KEY_TEMPLATE_TYPE);
//					if (SmooksModelUtils.FREEMARKER_TEMPLATE_TYPE_CSV
//							.equals(messageType)
//							|| SmooksModelUtils.FREEMARKER_TEMPLATE_TYPE_XML
//									.equals(messageType)) {
//						TemplateAppyTaskNode templateTask = (TemplateAppyTaskNode) ProcessFactory.eINSTANCE
//								.createTemplateTask();
//						templateTask.setType(messageType);
//						templateTask.addSmooksModel(abstractResourceConfig);
//						templateTask.setName(TaskTypeManager
//								.getTaskLabel(templateTask));
//						javaMappingTask.addTask(templateTask);
//					}
//				}
//			}
		}
	}
}
