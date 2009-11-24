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
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.graphical.editors.TaskTypeManager;
import org.jboss.tools.smooks.graphical.wizard.TemplateMessageTypeWizard;
import org.jboss.tools.smooks.graphical.wizard.freemarker.FreemarkerCSVTemplateCreationWizard;
import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks.model.freemarker.FreemarkerFactory;
import org.jboss.tools.smooks.model.freemarker.FreemarkerPackage;
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.GraphPackage;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;
import org.jboss.tools.smooks.model.smooks.ParamType;
import org.jboss.tools.smooks.model.smooks.SmooksFactory;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

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

	@Override
	public void run() {
		super.run();
		if (this.provider != null) {
			SmooksResourceListType resourceList = SmooksUIUtils.getSmooks11ResourceListType(provider.getSmooksModel());
			if (taskID.equals(TaskTypeManager.TASK_ID_FREEMARKER_TEMPLATE)) {
				// open wizard
				TemplateMessageTypeWizard wizard = new TemplateMessageTypeWizard();
				WizardDialog dialog = new WizardDialog(editorPart.getSite().getShell(), wizard);
				if (dialog.open() == Dialog.OK) {
					// init freemarker model
					IWizard currentWizard = wizard.getWizard();
					if (currentWizard instanceof FreemarkerCSVTemplateCreationWizard) {

						List<String> fields = ((FreemarkerCSVTemplateCreationWizard) currentWizard).getFields();
						String fieldsString = "";
						for (Iterator<?> iterator = fields.iterator(); iterator.hasNext();) {
							String string = (String) iterator.next();
							fieldsString += string + ",";
						}
						if (fieldsString.length() > 0) {
							fieldsString = fieldsString.substring(0, fieldsString.length() - 1);
						}
						String quote = ((FreemarkerCSVTemplateCreationWizard) currentWizard).getQuote();
						String seperator = ((FreemarkerCSVTemplateCreationWizard) currentWizard).getSeprator();

						Freemarker freemarker = FreemarkerFactory.eINSTANCE.createFreemarker();

						String idref = SmooksModelUtils.generateTaskID(resourceList, Freemarker.class, "freemarker");

						ParamType idParam = SmooksFactory.eINSTANCE.createParamType();
						idParam.setName(SmooksModelUtils.KEY_OBJECT_ID);
						idParam.setStringValue(idref);

						ParamType messageTypeParam = SmooksFactory.eINSTANCE.createParamType();
						messageTypeParam.setName(SmooksModelUtils.KEY_TEMPLATE_TYPE);
						messageTypeParam.setStringValue(SmooksModelUtils.FREEMARKER_TEMPLATE_TYPE_CSV);

						ParamType quoteParam = SmooksFactory.eINSTANCE.createParamType();
						quoteParam.setName(SmooksModelUtils.KEY_CSV_QUOTE);
						quoteParam.setStringValue(quote);

						ParamType speratorParam = SmooksFactory.eINSTANCE.createParamType();
						speratorParam.setName(SmooksModelUtils.KEY_CSV_SEPERATOR);
						speratorParam.setStringValue(seperator);

						ParamType fieldsParam = SmooksFactory.eINSTANCE.createParamType();
						fieldsParam.setName(SmooksModelUtils.KEY_CSV_FIELDS);
						fieldsParam.setStringValue(fieldsString);

						SmooksModelUtils.addParam(freemarker.getTemplate(), messageTypeParam);
						SmooksModelUtils.addParam(freemarker.getTemplate(), quoteParam);
						SmooksModelUtils.addParam(freemarker.getTemplate(), speratorParam);
						SmooksModelUtils.addParam(freemarker.getTemplate(), fieldsParam);
						freemarker.getParam().add(idParam);

						Command addFreemarkerCommand = AddCommand.create(this.provider.getEditingDomain(),
								resourceList,
								SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP,
								FeatureMapUtil.createEntry(FreemarkerPackage.Literals.DOCUMENT_ROOT__FREEMARKER,
										freemarker));
						TaskType parentTask = this.getCurrentSelectedTask().get(0);
						TaskType childTask = GraphFactory.eINSTANCE.createTaskType();
						childTask.setId(taskID);
						childTask.setName(this.getText());
						Command addTaskCommand = AddCommand.create(provider.getEditingDomain(), parentTask,
								GraphPackage.Literals.TASK_TYPE__TASK, childTask);

						ParamType idrefParam = SmooksFactory.eINSTANCE.createParamType();
						idrefParam.setName(SmooksModelUtils.KEY_TASK_ID_REF);

						idrefParam.setStringValue(idref);
						SmooksModelUtils.addParam(childTask, idrefParam);

						CompoundCommand cc = new CompoundCommand();
						cc.append(addFreemarkerCommand);
						cc.append(addTaskCommand);

						provider.getEditingDomain().getCommandStack().execute(cc);
					}
				} else {
					return;
				}
			} else {
				Command command = createAddTaskCommand();
				provider.getEditingDomain().getCommandStack().execute(command);
			}
		}
	}

	private Command createAddTaskCommand() {
		TaskType parentTask = this.getCurrentSelectedTask().get(0);
		TaskType childTask = GraphFactory.eINSTANCE.createTaskType();
		childTask.setId(taskID);
		childTask.setName(this.getText());
		Command command = AddCommand.create(provider.getEditingDomain(), parentTask,
				GraphPackage.Literals.TASK_TYPE__TASK, childTask);
		return command;
	}

	protected TaskType createTaskType() {
		return GraphFactory.eINSTANCE.createTaskType();
	}

	@Override
	public void update() {
		super.update();
		if (this.isEnabled()) {
			TaskType testTaskType = GraphFactory.eINSTANCE.createTaskType();
			testTaskType.setId(taskID);
			setEnabled(rules.isNextTask(this.getCurrentSelectedTask().get(0), testTaskType));
		}
	}

}
