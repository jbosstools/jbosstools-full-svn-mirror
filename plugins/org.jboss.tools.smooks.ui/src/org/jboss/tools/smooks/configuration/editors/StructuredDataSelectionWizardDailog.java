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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard;
import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.ParamType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtFactory;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

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


	private List<ParamType> generateExtParams(String type, String path, Properties properties) {
		List<ParamType> lists = new ArrayList<ParamType>();
		if (properties != null) {
			Enumeration<?> enumerations = properties.keys();
			while (enumerations.hasMoreElements()) {
				Object key = (Object) enumerations.nextElement();
				ParamType param = SmooksGraphicsExtFactory.eINSTANCE.createParamType();
				param.setValue(properties.getProperty(key.toString()));
				param.setName(key.toString());
				lists.add(param);
			}
		}
		return lists;
	}

	
	public int show() {
		int openResult = this.open();
		if (openResult == WizardDialog.OK) {
			IStructuredDataSelectionWizard wizard1 = this.getCurrentCreationWizard();
			String type = wizard1.getInputDataTypeID();
			String path = wizard1.getStructuredDataSourcePath();
			
			wizard1.complate(this.getFormEditor());
			
			SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
			if (type != null && path != null && extType != null) {
				String[] values = path.split(";");
				if(values == null || values.length == 0){
					values = new String[]{path};
				}
				for (int i = 0; i < values.length; i++) {
					String value = values[i];
					value = value.trim();
					if (value.length() == 0)
						continue;
					InputType input = SmooksGraphicsExtFactory.eINSTANCE.createInputType();
					input.setType(type);
					ParamType param = SmooksGraphicsExtFactory.eINSTANCE.createParamType();
					param.setValue(value);
					param.setName(SmooksModelUtils.PARAM_NAME_PATH);
					input.getParam().add(param);
					List<ParamType> params = generateExtParams(type, path, wizard1.getProperties());
					input.getParam().addAll(params);
					extType.getInput().add(input);
				}
				try {
					extType.eResource().save(Collections.emptyMap());
					List<ISmooksGraphChangeListener> listeners = extType.getChangeListeners();
					for (Iterator<?> iterator = listeners.iterator(); iterator.hasNext();) {
						ISmooksGraphChangeListener smooksGraphChangeListener = (ISmooksGraphChangeListener) iterator
								.next();
						smooksGraphChangeListener.saveComplete(extType);
					}
				} catch (IOException e) {
					SmooksConfigurationActivator.getDefault().log(e);
				}
			}
		}
		return openResult;
	}

}
