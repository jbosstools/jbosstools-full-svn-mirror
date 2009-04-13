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
package org.jboss.tools.smooks.configuration.editors.filerouting;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.model.fileRouting.FileRoutingPackage;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class OutputStreamUICreator extends PropertyUICreator {

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
		if (feature == FileRoutingPackage.eINSTANCE.getOutputStream_FileNamePattern()) {
		}
		if (feature == FileRoutingPackage.eINSTANCE.getOutputStream_DestinationDirectoryPattern()) {
		}
		if (feature == FileRoutingPackage.eINSTANCE.getOutputStream_ListFileNamePattern()) {
		}
		if (feature == FileRoutingPackage.eINSTANCE.getOutputStream_CloseOnCondition()) {
		}
		if (feature == FileRoutingPackage.eINSTANCE.getOutputStream_Encoding()) {
		}
		if (feature == FileRoutingPackage.eINSTANCE.getOutputStream_OpenOnElement()) {
		}
		if (feature == FileRoutingPackage.eINSTANCE.getOutputStream_OpenOnElementNS()) {
		}
		if (feature == FileRoutingPackage.eINSTANCE.getOutputStream_ResourceName()) {
		}

		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature,
			formEditor);
	}

}