/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.input.contributors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.part.EditorPart;
import org.jboss.tools.smooks.configuration.editors.input.InputTaskPanelContributor;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.ICorePackage;
import org.jboss.tools.smooks.model.core.IParam;
import org.milyn.javabean.dynamic.Model;

/**
 * Abstract Contributor.
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public abstract class AbstractContributor implements InputTaskPanelContributor {

	private EditorPart editorPart;
	private Model<SmooksModel> configModel;
	private SmooksModel modelRoot;
	private EditingDomain editingDomain;

	public AbstractContributor(EditorPart editorPart, Model<SmooksModel> configModel) {
		this.editorPart = editorPart;
		this.configModel = configModel;
		this.modelRoot = configModel.getModelRoot();
		this.editingDomain = modelRoot.getModelProvider().getEditingDomain();
	}

	public EditorPart getEditorPart() {
		return editorPart;
	}

	public Model<SmooksModel> getConfigModel() {
		return configModel;
	}
	
	public SmooksModel getModelRoot() {
		return modelRoot;
	}

	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	protected IParam setInputParam(String inputType, String value, CompoundCommand compoundCommand) {
		IParam inputParam = getInputParam(inputType);

		if(inputParam == null) {
			inputParam = SmooksUIUtils.recordInputDataInfomation(editingDomain, modelRoot.getParams(), inputType, value, null, compoundCommand);
		} else {
			setParamValue(inputParam, value, compoundCommand);
		}
		
		return inputParam;
	}
	
	protected void removeInputParam(String inputType, CompoundCommand compoundCommand) {
		IParam inputParam = getInputParam(inputType);		
		removeParam(inputParam, compoundCommand);
	}

	protected void removeParam(IParam param, CompoundCommand compoundCommand) {
		if(param != null) {
			modelRoot.getParams().getParams().remove(param);
			Command command = DeleteCommand.create(editingDomain, param);
			addOrExecute(compoundCommand, command);
		}
	}

	protected IParam getInputParam(String inputType) {
		return modelRoot.getParams().getParam(inputType);
	}

	protected void setParamValue(IParam param, String value, CompoundCommand compoundCommand) {
		Command command = SetCommand.create(editingDomain, param, ICorePackage.Literals.PARAM__VALUE, value);
		addOrExecute(compoundCommand, command);
	}

	protected void setParamType(IParam param, String typeValue, CompoundCommand compoundCommand) {
		Command command = SetCommand.create(editingDomain, param, ICorePackage.Literals.PARAM__TYPE, typeValue);
		addOrExecute(compoundCommand, command);
	}

	private void addOrExecute(CompoundCommand compoundCommand, Command command) {
		if (command.canExecute()) {
			if(compoundCommand == null) {
				editingDomain.getCommandStack().execute(command);
			} else {
				compoundCommand.append(command);
			}
		}
	}	
}
