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

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.smooks.graphical.editors.TaskTypeManager;
import org.jboss.tools.smooks.graphical.editors.process.ProcessFactory;
import org.jboss.tools.smooks.graphical.editors.process.TaskType;
import org.jboss.tools.smooks.model.ISmooksModelProvider;
import org.jboss.tools.smooks.model.SmooksModel;
import org.milyn.javabean.dynamic.Model;

/**
 * @author Dart
 * 
 */
public class AddNextTaskNodeAction extends AddTaskNodeAction {

	public AddNextTaskNodeAction(String taskID, String text, ISmooksModelProvider provider, IEditorPart editor) {
		super(taskID, text, provider, editor);
	}

	@Override
	protected void init() {
		super.init();
	}

	protected void addFreemarkerTemplateTask(IWizard currentWizard, Model<SmooksModel> resourceList) {
//		if (currentWizard instanceof FreemarkerTemplateParametersProvider) {
//
//			Freemarker freemarker = FreemarkerFactory.eINSTANCE.createFreemarker();
//
//			Map<String, String> parameters = ((FreemarkerTemplateParametersProvider) currentWizard).getParameters();
//			Iterator<String> keys = parameters.keySet().iterator();
//			while (keys.hasNext()) {
//				String key = keys.next();
//				String value = parameters.get(key);
//
//				ParamType param = SmooksFactory.eINSTANCE.createParamType();
//				param.setName(key);
//				param.setStringValue(value);
//
//				freemarker.getParam().add(param);
//			}
//			
//			String type = ((FreemarkerTemplateParametersProvider) currentWizard).getTemplateType();
//			if (type != null) {
//				ParamType param = SmooksFactory.eINSTANCE.createParamType();
//				param.setName(SmooksModelUtils.KEY_TEMPLATE_TYPE);
//				param.setStringValue(type);
//				freemarker.getParam().add(param);
//			}
//
//			Command addFreemarkerCommand = AddCommand.create(this.provider.getEditingDomain(), resourceList,
//					SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
//							.createEntry(FreemarkerPackage.Literals.DOCUMENT_ROOT__FREEMARKER, freemarker));
//
//			if (addFreemarkerCommand.canExecute()) {
//				provider.getEditingDomain().getCommandStack().execute(addFreemarkerCommand);
//				TaskType parentTask = this.getCurrentSelectedTask().get(0);
//				TaskType childTask = ProcessFactory.eINSTANCE.createTemplateTask();
//				childTask.addSmooksModel(freemarker);
//				childTask.setType(type);
//				parentTask.addTask(childTask);
//			}
//		}
	}

	@Override
	public void run() {
		super.run();
		if (this.provider != null) {
			Model<SmooksModel> root = provider.getSmooksModel();
//			SmooksResourceListType resourceList = SmooksUIUtils.getSmooks11ResourceListType(provider.getSmooksModel());
			if (taskID.equals(TaskTypeManager.TASK_ID_FREEMARKER_CSV_TEMPLATE)) {
				// open wizard
//				TemplateMessageTypeWizard wizard = new TemplateMessageTypeWizard();
//				WizardDialog dialog = new WizardDialog(editorPart.getSite().getShell(), wizard);
//				if (dialog.open() == Dialog.OK) {
//					// init freemarker model
//					IWizard currentWizard = wizard.getWizard();
//					addFreemarkerTemplateTask(currentWizard, root);
//				} else {
//					return;
//				}
			} else {
				TaskType parentTask = this.getCurrentSelectedTask().get(0);
				TaskType childTask = ProcessFactory.eINSTANCE.createTaskType();
				childTask.setId(taskID);
				childTask.setName(TaskTypeManager.getTaskLabel(childTask));
				parentTask.addTask(childTask);
			}
		}
	}

	protected TaskType createTaskType() {
		return ProcessFactory.eINSTANCE.createTaskType();
	}

	@Override
	public void update() {
		try{
		super.update();
		if (this.isEnabled()) {
			TaskType testTaskType = ProcessFactory.eINSTANCE.createTaskType();
			testTaskType.setId(taskID);
			setEnabled(rules.isNextTask(this.getCurrentSelectedTask().get(0), testTaskType));
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
