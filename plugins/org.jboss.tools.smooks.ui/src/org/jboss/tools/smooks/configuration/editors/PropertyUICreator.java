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
package org.jboss.tools.smooks.configuration.editors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.uitls.IFieldDialog;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart (dpeng@redhat.com)
 *         <p>
 *         Apr 9, 2009
 */
public class PropertyUICreator implements IPropertyUICreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.IPropertyUICreator#
	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite,
	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute)
	 */
	public Composite createPropertyUI(FormToolkit toolkit, Composite parent,
		IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
		SmooksMultiFormEditor formEditor) {
		if (isBeanIDRefFieldFeature(feature)) {
			return createBeanIDRefFieldEditor(toolkit, parent, propertyDescriptor, model, feature,
				formEditor);
		}
		if (isSelectorFeature(feature)) {
			return createSelectorFieldEditor(toolkit, parent, propertyDescriptor, model, feature,
				formEditor);
		}
		if (isJavaTypeFeature(feature)) {
			return createJavaTypeSearchEditor(toolkit, parent, propertyDescriptor, model, feature,
				formEditor);
		}
		if (isFileSelectionFeature(feature)) {
			return createFileSelectionFieldEditor(toolkit, parent, propertyDescriptor, model,
				feature, formEditor);
		}
		if (feature == SmooksPackage.eINSTANCE.getAbstractReader_TargetProfile()) {

		}
		return null;
	}

	public IResource getResource(EObject model) {
		return SmooksUIUtils.getResource(model);
	}

	public IJavaProject getJavaProject(EObject model) {
		return SmooksUIUtils.getJavaProject(model);
	}

	public void createExtendUI(AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit,
		Composite parent, Object model, SmooksMultiFormEditor formEditor) {

	}

	public boolean isFileSelectionFeature(EAttribute attribute) {
		return false;
	}

	public Composite createFileSelectionFieldEditor(FormToolkit toolkit, Composite parent,
		IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
		SmooksMultiFormEditor formEditor) {
		IFieldDialog dialog = new IFieldDialog() {
			public Object open(Shell shell) {
				FileSelectionWizard wizard = new FileSelectionWizard();
				WizardDialog dialog = new WizardDialog(shell, wizard);
				if (dialog.open() == Dialog.OK) {
					return wizard.getFilePath();
				}
				return null;
			}
		};
		final IItemPropertyDescriptor fp = propertyDescriptor;
		final Object fm = model;
		IHyperlinkListener listener = new IHyperlinkListener() {

			public void linkActivated(HyperlinkEvent e) {
				Object value = SmooksUIUtils.getEditValue(fp, fm);
				System.out.println(value);
			}

			public void linkEntered(HyperlinkEvent e) {

			}

			public void linkExited(HyperlinkEvent e) {

			}
		};

		return SmooksUIUtils.createDialogFieldEditor(parent, toolkit, propertyDescriptor, "Browse",
			dialog, (EObject) model, true, listener);
	}

	public boolean isSelectorFeature(EAttribute attribute) {
		return false;
	}

	public Composite createSelectorFieldEditor(FormToolkit toolkit, Composite parent,
		IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
		SmooksMultiFormEditor formEditor) {
		SmooksGraphicsExtType ext = formEditor.getSmooksGraphicsExt();
		if (ext != null) {
			return SmooksUIUtils.createSelectorFieldEditor(toolkit, parent, propertyDescriptor,
				model, ext);
		}
		return null;
	}

	public boolean isJavaTypeFeature(EAttribute attribute) {
		return false;
	}

	public Composite createJavaTypeSearchEditor(FormToolkit toolkit, Composite parent,
		IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
		SmooksMultiFormEditor formEditor) {
		if (model instanceof EObject) return SmooksUIUtils.createJavaTypeSearchFieldEditor(parent,
			toolkit, propertyDescriptor, (EObject) model);
		return null;
	}

	public boolean isBeanIDRefFieldFeature(EAttribute attribute) {
		return false;
	}

	public Composite createBeanIDRefFieldEditor(FormToolkit toolkit, Composite parent,
		IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
		SmooksMultiFormEditor formEditor) {
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
		return null;
	}

	protected SmooksResourceListType getSmooksResourceList(EObject model) {
		EObject parent = model.eContainer();
		while (parent != null && !(parent instanceof SmooksResourceListType)) {
			parent = parent.eContainer();
		}
		if (parent instanceof SmooksResourceListType) {
			return (SmooksResourceListType) parent;
		}
		return null;
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

	public boolean ignoreProperty(EAttribute feature) {
		return false;
	}

}
