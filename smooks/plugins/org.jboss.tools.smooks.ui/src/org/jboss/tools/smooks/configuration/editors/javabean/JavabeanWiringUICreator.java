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
package org.jboss.tools.smooks.configuration.editors.javabean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 9, 2009
 */
public class JavabeanWiringUICreator extends PropertiesAndSetterMethodSearchFieldEditorCreator {

	@Override
	protected boolean canCreatePropertiesSearchFieldEditor(EAttribute feature) {
		if (feature == JavabeanPackage.eINSTANCE.getWiringType_Property()) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean canCreateMethodsSearchFieldEditor(EAttribute feature) {
		if (feature == JavabeanPackage.eINSTANCE.getWiringType_SetterMethod()) {
			return true;
		}
		return super.canCreateMethodsSearchFieldEditor(feature);
	}

	@Override
	public Composite createPropertyUI(FormToolkit toolkit, Composite parent,
		IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
		SmooksMultiFormEditor formEditor) {
		if (feature == JavabeanPackage.eINSTANCE.getWiringType_WireOnElement()) {
			SmooksGraphicsExtType ext = formEditor.getSmooksGraphicsExt();
			if (ext != null) {
				return SmooksUIUtils.createSelectorFieldEditor(toolkit, parent, propertyDescriptor,
					model, ext);
			}
		}
		if (feature == JavabeanPackage.eINSTANCE.getWiringType_BeanIdRef()) {
			if (model instanceof EObject) {
				SmooksResourceListType smooksResourceList = getSmooksResourceList((EObject) model);
				if (smooksResourceList != null) {
					String displayName = propertyDescriptor.getDisplayName(model);
					toolkit.createLabel(parent, displayName + " :");
					final CCombo combo = new CCombo(parent, SWT.BORDER);
					GridData gd = new GridData(GridData.FILL_HORIZONTAL);
					combo.setLayoutData(gd);
					Object editValue = SmooksUIUtils.getEditValue(propertyDescriptor, model);
					if (editValue != null) {
						combo.setText(editValue.toString());
					}
					List<String> list = getBeanIdList(smooksResourceList);
					for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
						String beanId = (String) iterator.next();
						combo.add(beanId);
					}
					int selectIndex = list.indexOf(editValue);
					if (selectIndex != -1) {
						combo.select(selectIndex);
					}
					final IItemPropertyDescriptor ip = propertyDescriptor;
					final Object cmodel = model;
					combo.addModifyListener(new ModifyListener() {
						public void modifyText(ModifyEvent e) {
							Object editValue = SmooksUIUtils.getEditValue(ip, cmodel);
							if (combo.getText().equals(editValue)) {
								return;
							}
							ip.setPropertyValue(cmodel, combo.getText());
						}
					});
					return combo;
				}
			}
		}
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature,
			formEditor);
	}

	protected List<String> getBeanIdList(SmooksResourceListType resourceList) {
		List<AbstractResourceConfig> rlist = resourceList.getAbstractResourceConfig();
		List<String> beanIdList = new ArrayList<String>();
		for (Iterator<?> iterator = rlist.iterator(); iterator.hasNext();) {
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator
				.next();
			if (abstractResourceConfig instanceof BindingsType) {
				String beanId = ((BindingsType) abstractResourceConfig).getBeanId();
				if (beanId == null) continue;
				beanIdList.add(beanId);
			}
		}
		return beanIdList;
	}

	private SmooksResourceListType getSmooksResourceList(EObject model) {
		EObject parent = model.eContainer();
		while (parent != null && !(parent instanceof SmooksResourceListType)) {
			parent = parent.eContainer();
		}
		if (parent instanceof SmooksResourceListType) {
			return (SmooksResourceListType) parent;
		}
		return null;
	}

}
