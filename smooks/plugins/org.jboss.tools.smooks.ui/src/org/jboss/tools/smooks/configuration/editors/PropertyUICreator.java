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
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.uitls.FieldMarkerWrapper;
import org.jboss.tools.smooks.configuration.editors.uitls.IFieldDialog;
import org.jboss.tools.smooks.configuration.editors.uitls.IModelProcsser;
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

	protected IModelProcsser fileFiledEditorModelProcess;

	protected IHyperlinkListener fileFiledEditorLinkListener;

	protected List<ViewerFilter> viewerFilters = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.IPropertyUICreator#
	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite,
	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute)
	 */
	public AttributeFieldEditPart createPropertyUI(FormToolkit toolkit, Composite parent, IItemPropertyDescriptor propertyDescriptor, Object model,
			EAttribute feature, SmooksMultiFormEditor formEditor) {
		if (isBeanIDRefFieldFeature(feature)) {
			return createBeanIDRefFieldEditor(toolkit, parent, propertyDescriptor, model, feature, formEditor);
		}
		if (isSelectorFeature(feature)) {
			return createSelectorFieldEditor(toolkit, parent, propertyDescriptor, model, feature, formEditor);
		}
		if (isJavaTypeFeature(feature)) {
			return createJavaTypeSearchEditor(toolkit, parent, propertyDescriptor, model, feature, formEditor);
		}
		if (isFileSelectionFeature(feature)) {
			return createFileSelectionFieldEditor(toolkit, parent, propertyDescriptor, model, feature, formEditor);
		}
		if(isConditionSelectionFeature(feature)){
			return SmooksUIUtils.createConditionsChoiceFieldEditor(parent, toolkit, propertyDescriptor, model);
//			return parent;
		}
		if (feature == SmooksPackage.eINSTANCE.getAbstractReader_TargetProfile()) {

		}
		return null;
	}

	protected boolean isConditionSelectionFeature(EAttribute feature) {
		return false;
	}

	public IHyperlinkListener getFileFiledEditorLinkListener() {
		return fileFiledEditorLinkListener;
	}

	public void setFileFiledEditorLinkListener(IHyperlinkListener fileFiledEditorLinkListener) {
		this.fileFiledEditorLinkListener = fileFiledEditorLinkListener;
	}

	public IModelProcsser getFileFiledEditorModelProcess() {
		return fileFiledEditorModelProcess;
	}

	public void setFileFiledEditorModelProcess(IModelProcsser fileFiledEditorModelProcess) {
		this.fileFiledEditorModelProcess = fileFiledEditorModelProcess;
	}

	public List<ViewerFilter> getFileDialogViewerFilters() {
		return viewerFilters;
	}

	public void setDialogViewerFilters(List<ViewerFilter> viewerFilters) {
		this.viewerFilters = viewerFilters;
	}

	public IResource getResource(EObject model) {
		return SmooksUIUtils.getResource(model);
	}

	public IJavaProject getJavaProject(EObject model) {
		return SmooksUIUtils.getJavaProject(model);
	}

	public void createExtendUI(AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent, Object model,
			SmooksMultiFormEditor formEditor) {
	}

	protected boolean isFileSelectionFeature(EAttribute attribute) {
		return false;
	}

	public AttributeFieldEditPart createFileSelectionFieldEditor(FormToolkit toolkit, Composite parent, IItemPropertyDescriptor propertyDescriptor, Object model,
			EAttribute feature, SmooksMultiFormEditor formEditor) {
		IFieldDialog dialog = new IFieldDialog() {
			public Object open(Shell shell) {
				FileSelectionWizard wizard = new FileSelectionWizard();
				wizard.setViewerFilters(getFileDialogViewerFilters());
				WizardDialog dialog = new WizardDialog(shell, wizard);
				if (dialog.open() == Dialog.OK) {
					IModelProcsser p = getModelProcesser();
					String path = wizard.getFilePath();
					if (p != null) {
						path = p.unwrapValue(path).toString();
					}
					return path;
				}
				return null;
			}

			public IModelProcsser getModelProcesser() {
				return getFileFiledEditorModelProcess();
			}

			public void setModelProcesser(IModelProcsser processer) {

			}

		};
		return SmooksUIUtils.createDialogFieldEditor(parent, toolkit, propertyDescriptor, "Browse", dialog, (EObject) model, true,
				getFileFiledEditorLinkListener());
	}

	protected boolean isSelectorFeature(EAttribute attribute) {
		return false;
	}

	public AttributeFieldEditPart createSelectorFieldEditor(FormToolkit toolkit, Composite parent, IItemPropertyDescriptor propertyDescriptor, Object model,
			EAttribute feature, SmooksMultiFormEditor formEditor) {
		SmooksGraphicsExtType ext = formEditor.getSmooksGraphicsExt();
		if (ext != null) {
			return SmooksUIUtils.createSelectorFieldEditor(toolkit, parent, propertyDescriptor, model, ext);
		}
		return null;
	}

	public boolean isJavaTypeFeature(EAttribute attribute) {
		return false;
	}

	public AttributeFieldEditPart createJavaTypeSearchEditor(FormToolkit toolkit, Composite parent, IItemPropertyDescriptor propertyDescriptor, Object model,
			EAttribute feature, SmooksMultiFormEditor formEditor) {
		if (model instanceof EObject)
			return SmooksUIUtils.createJavaTypeSearchFieldEditor(parent, toolkit, propertyDescriptor, (EObject) model);
		return null;
	}

	protected boolean isBeanIDRefFieldFeature(EAttribute attribute) {
		return false;
	}

	public AttributeFieldEditPart createBeanIDRefFieldEditor(FormToolkit toolkit, Composite parent, IItemPropertyDescriptor propertyDescriptor, Object model,
			EAttribute feature, SmooksMultiFormEditor formEditor) {
		if (model instanceof EObject) {
			AttributeFieldEditPart editPart = new AttributeFieldEditPart();
			SmooksResourceListType smooksResourceList = getSmooksResourceList((EObject) model);
			if (smooksResourceList != null) {
				FieldMarkerWrapper wrapper = SmooksUIUtils.createFieldEditorLabel(null,parent, toolkit, propertyDescriptor, model, false);
				editPart.setFieldMarker(wrapper.getMarker());
				
				Composite tcom = toolkit.createComposite(parent);
				GridLayout layout = new GridLayout();
				layout.numColumns = 2;
				layout.marginLeft = 0;
				layout.marginRight = 0;
				layout.horizontalSpacing = 0;
				tcom.setLayout(layout);
				
				FieldMarkerComposite notificationComposite = new FieldMarkerComposite(tcom, SWT.NONE);
				GridData gd = new GridData();
				gd.heightHint = 8;
				gd.widthHint = 8;
				gd.horizontalAlignment = GridData.BEGINNING;
				gd.verticalAlignment = GridData.BEGINNING;
				notificationComposite.setLayoutData(gd);
				editPart.setFieldMarker(notificationComposite);
				
				final Combo combo = new Combo(tcom, SWT.BORDER);
				editPart.setContentControl(combo);
				gd = new GridData(GridData.FILL_HORIZONTAL);
				combo.setLayoutData(gd);
				tcom.setLayoutData(gd);
				
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
				return editPart;
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
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator.next();
			if (abstractResourceConfig instanceof BindingsType) {
				String beanId = ((BindingsType) abstractResourceConfig).getBeanId();
				if (beanId == null)
					continue;
				beanIdList.add(beanId);
			}
		}
		return beanIdList;
	}

	public boolean ignoreProperty(EAttribute feature) {
		return false;
	}

}
