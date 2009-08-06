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

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.jboss.tools.smooks.model.smooks.AbstractReader;
import org.jboss.tools.smooks.model.smooks.ConditionsType;
import org.jboss.tools.smooks.model.smooks.ParamsType;

/**
 * @author Dart
 *
 */
public class SmooksConfigurationResourceConfigPage extends SmooksConfigurationFormPage {

	public SmooksConfigurationResourceConfigPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	public SmooksConfigurationResourceConfigPage(String id, String title) {
		super(id, title);
	}

	@Override
	protected ViewerFilter[] createViewerFilters() {
		ViewerFilter filter = new ViewerFilter() {
			
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				Object obj = element;
				obj = AdapterFactoryEditingDomain.unwrap(obj);
				if(obj instanceof ParamsType){
					return false;
				}
				if(obj instanceof ConditionsType){
					return false;
				}
				if(obj instanceof AbstractReader){
					return false;
				}
				return true;
			}
		};
		
		ViewerFilter[] filters = super.createViewerFilters();
		ViewerFilter[] newFilters = new ViewerFilter[filters.length + 1];
		newFilters[0] = filter;
		System.arraycopy(filters, 0, newFilters,1, filters.length);
		return newFilters;
	}
	
	

	
	@Override
	protected void setPageTitle(ScrolledForm form) {
//		EObject smooksModel = ((ISmooksModelProvider) getEditor()).getSmooksModel();
//		if (smooksModel != null) {
//			EObject parent = smooksModel;
//			while (parent != null) {
//				EObject old = parent;
//				parent = parent.eContainer();
//				if (parent == null) {
//					parent = old;
//					break;
//				}
//			}
//			if (parent instanceof DocumentRoot) {
//				title = "Smooks 1.1 - " + title;
//			}
//			if (parent instanceof org.jboss.tools.smooks10.model.smooks.DocumentRoot) {
//				title = "Smooks 1.0 - " + title;
//			}
//		}
		form.setText("Desgin");
	}

	@Override
	protected String getNewSmooksElementDescription() {
		return super.getNewSmooksElementDescription();
	}

	@Override
	protected String getNewSmooksElementTitle() {
		return super.getNewSmooksElementTitle();
	}

	@Override
	protected String getMainSectionDescription() {
		return super.getMainSectionDescription();
	}

	@Override
	protected String getMainSectionTitle() {
		return super.getMainSectionTitle();
	}
}
