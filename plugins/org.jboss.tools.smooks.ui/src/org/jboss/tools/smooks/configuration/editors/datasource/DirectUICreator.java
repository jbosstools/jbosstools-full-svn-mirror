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
package org.jboss.tools.smooks.configuration.editors.datasource;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.model.datasource.DatasourcePackage;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class DirectUICreator extends PropertyUICreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.IPropertyUICreator#
	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite,
	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute)
	 */
	public AttributeFieldEditPart createPropertyUI(FormToolkit toolkit, Composite parent,
			IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
			SmooksMultiFormEditor formEditor) {
		if (feature == DatasourcePackage.eINSTANCE.getDirect_AutoCommit()) {
		}
		if (feature == DatasourcePackage.eINSTANCE.getDirect_BindOnElementNS()) {
		}
		if (feature == DatasourcePackage.eINSTANCE.getDirect_Datasource()) {
		}
		if (feature == DatasourcePackage.eINSTANCE.getDirect_Password()) {
		}
		if (feature == DatasourcePackage.eINSTANCE.getDirect_Url()) {
		}
		if (feature == DatasourcePackage.eINSTANCE.getDirect_Username()) {
		}
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.PropertyUICreator#ignoreProperty
	 * (org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	public boolean ignoreProperty(EAttribute feature) {
		if (feature == DatasourcePackage.eINSTANCE.getDirect_BindOnElementNS()) {
			return true;
		}
		if (feature == DatasourcePackage.eINSTANCE.getDirect_BindOnElement()) {
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
	public List<AttributeFieldEditPart> createExtendUI(AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit,
			Composite parent, Object model, SmooksMultiFormEditor formEditor) {
		IItemPropertySource itemPropertySource = (IItemPropertySource) editingdomain.getAdapterFactory().adapt(model,
				IItemPropertySource.class);
		List<IItemPropertyDescriptor> propertyDes = itemPropertySource.getPropertyDescriptors(model);
		IItemPropertyDescriptor createOnElementFeature = null;
		IItemPropertyDescriptor createOnElementFeatureNS = null;
		for (Iterator<?> iterator = propertyDes.iterator(); iterator.hasNext();) {
			IItemPropertyDescriptor itemPropertyDescriptor = (IItemPropertyDescriptor) iterator.next();
			if (itemPropertyDescriptor.getFeature(model) == DatasourcePackage.eINSTANCE.getDirect_BindOnElement()) {
				createOnElementFeature = itemPropertyDescriptor;
			}
			if (itemPropertyDescriptor.getFeature(model) == DatasourcePackage.eINSTANCE.getDirect_BindOnElementNS()) {
				createOnElementFeatureNS = itemPropertyDescriptor;
			}
		}
		if(createOnElementFeature == null || createOnElementFeatureNS == null){
			return Collections.emptyList();
		}
		return this.createElementSelectionSection("Binding On Element", editingdomain, toolkit, parent, model, formEditor,
				createOnElementFeature, createOnElementFeatureNS);
	}

	@Override
	public boolean isJavaTypeFeature(EAttribute attribute) {
		if (attribute == DatasourcePackage.eINSTANCE.getDirect_Driver()) {
			return true;
		}
		return super.isJavaTypeFeature(attribute);
	}

	@Override
	public boolean isSelectorFeature(EAttribute attribute) {
		// if (attribute ==
		// DatasourcePackage.eINSTANCE.getDirect_BindOnElement()) {
		// return true;
		// }
		return super.isSelectorFeature(attribute);
	}

}