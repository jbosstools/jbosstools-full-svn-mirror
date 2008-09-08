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
package org.jboss.tools.smooks.ui.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * @author Dart Peng
 * @Date Jul 28, 2008
 */
public class SmooksFormEditor extends FormEditor implements
		ITabbedPropertySheetPageContributor {
	SmooksGraphicalFormPage graphicalPage = null;
	private TabbedPropertySheetPage tabbedPropertySheetPage;
	public static final String EDITOR_ID = "org.jboss.tools.smooks.ui.editors.SmooksFormEditor";

	@Override
	protected void addPages() {
		graphicalPage = new SmooksGraphicalFormPage(this, "graph", "Mapping");
		try {
			int index = this.addPage(this.graphicalPage);
			this.setPageText(index, "Graph");

		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		graphicalPage.doSave(monitor);
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySheetPage.class){
			tabbedPropertySheetPage = new TabbedPropertySheetPage(this);
			return tabbedPropertySheetPage;
		}
		return super.getAdapter(adapter);
	}

	public String getContributorId() {
		return getSite().getId();
	}

	public void setTabbedPropertySheetPage(
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		this.tabbedPropertySheetPage = tabbedPropertySheetPage;
	}

}
