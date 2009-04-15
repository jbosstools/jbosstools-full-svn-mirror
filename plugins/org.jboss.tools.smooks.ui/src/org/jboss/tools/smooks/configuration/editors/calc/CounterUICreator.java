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
package org.jboss.tools.smooks.configuration.editors.calc;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.model.calc.CalcPackage;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class CounterUICreator extends PropertyUICreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.IPropertyUICreator#
	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite,
	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute)
	 */
	public Composite createPropertyUI(FormToolkit toolkit, Composite parent, IItemPropertyDescriptor propertyDescriptor, Object model,
			EAttribute feature, SmooksMultiFormEditor formEditor) {
		
		if (feature == CalcPackage.eINSTANCE.getCounter_StartExpression()) {
		}
		if (feature == CalcPackage.eINSTANCE.getCounter_AmountExpression()) {
		}
		if (feature == CalcPackage.eINSTANCE.getCounter_ResetCondition()) {
		}
		if (feature == CalcPackage.eINSTANCE.getCounter_Amount()) {
		}
		if (feature == CalcPackage.eINSTANCE.getCounter_BeanId()) {
		}
		if (feature == CalcPackage.eINSTANCE.getCounter_CountOnElementNS()) {
		}
		if (feature == CalcPackage.eINSTANCE.getCounter_Direction()) {
		}
		if (feature == CalcPackage.eINSTANCE.getCounter_ExecuteAfter()) {
		}
		if (feature == CalcPackage.eINSTANCE.getCounter_Start()) {
		}
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor);
	}

	@Override
	public boolean isSelectorFeature(EAttribute attribute) {
		if (attribute == CalcPackage.eINSTANCE.getCounter_CountOnElement()) {
			return true;
		}
		return super.isSelectorFeature(attribute);
	}
	
	

}