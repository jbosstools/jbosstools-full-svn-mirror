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

import java.util.Collection;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 1, 2009
 */
public class SmooksConfigurationFormPage extends FormPage {

	private SmooksMasterDetailBlock masterDetailBlock = null;

	public SmooksConfigurationFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	public SmooksConfigurationFormPage(String id, String title) {
		super(id, title);
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		masterDetailBlock = new SmooksMasterDetailBlock(getEditor(),
			(AdapterFactoryEditingDomain) ((SmooksMultiFormEditor) getEditor()).getEditingDomain());
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());
		form.setText(getTitle());
		masterDetailBlock.createContent(managedForm);

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 13;
		form.getBody().setLayout(gridLayout);
	}
	
	public void setSmooksModel(Object model){
		masterDetailBlock.setSmooksModel(model);
	}
	
	public void setSelectionToViewer(final Collection<?> collection){
		if(masterDetailBlock != null && masterDetailBlock.getSmooksTreeViewer() != null){
			// I don't know if this should be run this deferred
			// because we might have to give the editor a chance to process the
			// viewer update events
			// and hence to update the views first.
			//
			//
			final TreeViewer viewer = masterDetailBlock.getSmooksTreeViewer();
			Runnable runnable = new Runnable() {
				public void run() {
					// Try to select the items in the current content viewer of
					// the editor.
					//
					if (viewer != null) {
						viewer.setSelection(new StructuredSelection(collection.toArray()), true);
					}
				}
			};
			runnable.run();
		}
	}

}
