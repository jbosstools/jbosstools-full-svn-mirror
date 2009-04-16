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
package org.jboss.tools.smooks.configuration.editors.smooks;

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
import org.jboss.tools.smooks.model.smooks.SmooksPackage;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class ImportTypeUICreator extends PropertyUICreator {

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
		IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,SmooksMultiFormEditor formEditor) {
		if (feature == SmooksPackage.eINSTANCE.getImportType_File()) {
			final Object fm = model;
			final Object path = SmooksUIUtils.getEditValue(propertyDescriptor, model);
			IHyperlinkListener listener = new IHyperlinkListener() {
				public void linkActivated(HyperlinkEvent e) {
					String p = null;
					if (path != null && path instanceof String) {
						p = ((String) path).trim();
					}
					try {
						IResource resource = SmooksUIUtils.getResource((EObject) fm);
						SmooksUIUtils.openFile(p, resource.getProject(),null);
					} catch (Exception e1) {

					}
				}

				public void linkEntered(HyperlinkEvent e) {

				}

				public void linkExited(HyperlinkEvent e) {

				}

			};
			SmooksUIUtils.createLinkTextValueFieldEditor(null, (AdapterFactoryEditingDomain) formEditor.getEditingDomain(), propertyDescriptor,
					toolkit, parent, model, false, 0, true, listener);

			return parent;
		}
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor);
	}

}