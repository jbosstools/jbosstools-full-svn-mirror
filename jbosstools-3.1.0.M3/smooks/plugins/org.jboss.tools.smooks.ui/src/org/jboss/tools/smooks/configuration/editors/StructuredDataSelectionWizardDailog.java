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
package org.jboss.tools.smooks.configuration.editors;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;

/**
 * @author Dart Peng
 * @Date Aug 6, 2008
 */
public class StructuredDataSelectionWizardDailog extends WizardDialog {

	protected SmooksGraphicsExtType smooksGraphicsExtType;
	
	private SmooksMultiFormEditor formEditor;
	
	public StructuredDataSelectionWizardDailog(Shell parentShell,
			IWizard newWizard,SmooksGraphicsExtType extType , SmooksMultiFormEditor formEditor) {
		super(parentShell, newWizard);
		this.setSmooksGraphicsExtType(extType);
		this.formEditor = formEditor;
	}
	
	public IStructuredDataSelectionWizard getCurrentCreationWizard(){
		IWizard w = getWizard();
		if(w != null && w instanceof IStructuredDataSelectionWizard){
			return (IStructuredDataSelectionWizard)w;
		}
		return null;
	}
	
	
	public SmooksMultiFormEditor getFormEditor() {
		return formEditor;
	}

	public void setFormEditor(SmooksMultiFormEditor formEditor) {
		this.formEditor = formEditor;
	}

	/**
	 * @return the smooksGraphicsExtType
	 */
	public SmooksGraphicsExtType getSmooksGraphicsExtType() {
		return smooksGraphicsExtType;
	}

	/**
	 * @param smooksGraphicsExtType the smooksGraphicsExtType to set
	 */
	public void setSmooksGraphicsExtType(SmooksGraphicsExtType smooksGraphicsExtType) {
		this.smooksGraphicsExtType = smooksGraphicsExtType;
	}

	
	public int show() {
		int openResult = this.open();
		if (openResult == WizardDialog.OK) {
			IStructuredDataSelectionWizard wizard1 = this.getCurrentCreationWizard();
			String type = wizard1.getInputDataTypeID();
			String path = wizard1.getStructuredDataSourcePath();
			
//			wizard1.complate(this.getFormEditor());
			
			SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
			SmooksUIUtils.recordInputDataInfomation(null,extType, type, path, wizard1.getProperties());
		}
		return openResult;
	}

}
