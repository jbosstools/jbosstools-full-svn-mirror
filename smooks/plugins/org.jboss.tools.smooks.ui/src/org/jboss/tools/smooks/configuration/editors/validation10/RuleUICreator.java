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
package org.jboss.tools.smooks.configuration.editors.validation10;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.rules10.RuleBase;
import org.jboss.tools.smooks.model.rules10.RuleBasesType;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks.model.validation10.Validation10Package;

/**
 * @author Dart (dpeng@redhat.com)
 *         <p>
 *         Apr 8, 2009
 */
public class RuleUICreator extends PropertyUICreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.PropertyUICreator#ignoreProperty
	 * (org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	public boolean ignoreProperty(EAttribute feature) {
		if (feature == Validation10Package.Literals.RULE_TYPE__EXECUTE_ON) {
			return true;
		}
		if (feature == Validation10Package.Literals.RULE_TYPE__EXECUTE_ON_NS) {
			return true;
		}
		return super.ignoreProperty(feature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.PropertyUICreator#createExtendUI
	 * (org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain,
	 * org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite, java.lang.Object,
	 * org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor)
	 */
	@Override
	public List<AttributeFieldEditPart> createExtendUIOnTop(AdapterFactoryEditingDomain editingdomain,
			FormToolkit toolkit, Composite parent, Object model, ISmooksModelProvider formEditor, IEditorPart part) {
		return super.createExtendUIOnTop(editingdomain, toolkit, parent, model, formEditor, part);
		// return createElementSelectionSection("Execute On Element",
		// editingdomain, toolkit, parent, model, formEditor,
		// part, Validation10Package.Literals.RULE_TYPE__EXECUTE_ON,
		// Validation10Package.Literals.RULE_TYPE__EXECUTE_ON_NS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.PropertyUICreator#
	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite,
	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute,
	 * org.jboss.tools.smooks.editor.ISmooksModelProvider,
	 * org.eclipse.ui.IEditorPart)
	 */
	@Override
	public AttributeFieldEditPart createPropertyUI(FormToolkit toolkit, Composite parent,
			IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
			ISmooksModelProvider formEditor, IEditorPart editorPart) {
		if (feature == Validation10Package.Literals.RULE_TYPE__NAME) {
			return SmooksUIUtils.createChoiceFieldEditor(parent, toolkit, propertyDescriptor, model,
					getRuleNames(model), null, false);
		}
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor, editorPart);
	}

	private String[] getRuleNames(Object model) {
		SmooksResourceListType listType = SmooksUIUtils.getSmooks11ResourceListType((EObject) model);
		List<String> namesString = new ArrayList<String>();
		List<?> arList = listType.getAbstractResourceConfig();
		for (Iterator<?> iterator = arList.iterator(); iterator.hasNext();) {
			AbstractResourceConfig config = (AbstractResourceConfig) iterator.next();
			if (config instanceof RuleBasesType) {
				fillNamesList(namesString, (RuleBasesType) config);
			}
		}

		return namesString.toArray(new String[] {});
	}

	private void fillNamesList(List<String> namesString, RuleBasesType ruleBases) {
		EList<?> ruleBaseList = ruleBases.getRuleBase();
		IResource resource = SmooksUIUtils.getResource(ruleBases);
		if (resource == null)
			return;
		IProject project = resource.getProject();
		if (project == null)
			return;

		for (Iterator<?> iterator = ruleBaseList.iterator(); iterator.hasNext();) {
			RuleBase rulebase = (RuleBase) iterator.next();
			String src = rulebase.getSrc();
			IFile file = SmooksUIUtils.getFile(src, project);
			if (file == null || !file.exists())
				continue;
			Properties properties = new Properties();
			String ruleBasesName = rulebase.getName();
			if (ruleBasesName == null)
				continue;
			try {
				properties.load(file.getContents());
			} catch (Throwable t) {
				continue;
			}
			Enumeration<?> keys = properties.keys();
			while (keys.hasMoreElements()) {
				String keyName = keys.nextElement().toString();

				namesString.add(ruleBasesName + "." + keyName);
			}
			properties.clear();
		}
	}
}
