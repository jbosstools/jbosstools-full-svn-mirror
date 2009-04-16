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
package org.jboss.tools.smooks.configuration.editors.edireader;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.edi.EdiPackage;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class EDIReaderUICreator extends PropertyUICreator {

	public EDIReaderUICreator() {
	}

	private void openFile(IItemPropertyDescriptor propertyDescriptor, Object model) {
		Object path = SmooksUIUtils.getEditValue(propertyDescriptor, model);
		String p = null;
		if (path != null && path instanceof String) {
			p = ((String) path).trim();
		}
		try {
			IResource resource = SmooksUIUtils.getResource((EObject) model);
			SmooksUIUtils.openFile(p, resource.getProject(), SmooksMultiFormEditor.EDITOR_ID);
		} catch (Exception e) {

		}
	}

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
		if (feature == EdiPackage.eINSTANCE.getEDIReader_Encoding()) {
		}
		if (feature == EdiPackage.eINSTANCE.getEDIReader_MappingModel()) {
			final Object fm = model;
			final IItemPropertyDescriptor fpd = propertyDescriptor;
			IHyperlinkListener listener = new IHyperlinkListener() {

				public void linkActivated(HyperlinkEvent e) {
					openFile(fpd, fm);
				}

				public void linkEntered(HyperlinkEvent e) {

				}

				public void linkExited(HyperlinkEvent e) {

				}

			};
			SmooksUIUtils.createLinkTextValueFieldEditor("Mapping Model", (AdapterFactoryEditingDomain) formEditor.getEditingDomain(), propertyDescriptor,
					toolkit, parent, model, false, 0, true, listener);
			return parent;
		}
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor);
	}

	@Override
	public boolean isFileSelectionFeature(EAttribute attribute) {
//		if (attribute == EdiPackage.eINSTANCE.getEDIReader_MappingModel()) {
//			return true;
//		}
		return super.isFileSelectionFeature(attribute);
	}

}