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
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.jboss.tools.smooks.configuration.editors.wizard.StructuredDataSelectionWizard;
import org.jboss.tools.smooks.configuration.validate.ISmooksModelValidateListener;
import org.jboss.tools.smooks.editor.ISourceSynchronizeListener;
import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 1, 2009
 */
public class SmooksConfigurationFormPage extends FormPage implements ISmooksModelValidateListener,
		ISmooksGraphChangeListener, ISourceSynchronizeListener {

	private SmooksMasterDetailBlock masterDetailBlock = null;

	public SmooksConfigurationFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		((SmooksMultiFormEditor) editor).getSmooksGraphicsExt().addSmooksGraphChangeListener(this);
	}

	public SmooksConfigurationFormPage(String id, String title) {
		super(id, title);
	}

	protected String getMainSectionTitle() {
		return null;
	}

	protected String getMainSectionDescription() {
		return null;
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());
		setPageTitle(form);
		// create master details UI
		createMasterDetailBlock(managedForm);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 13;
		// gridLayout.numColumns = 2;
		// gridLayout.makeColumnsEqualWidth = true;
		form.getBody().setLayout(gridLayout);
	}

	protected void setPageTitle(ScrolledForm form) {
		String title = getTitle();
		form.setText(title);
	}

	protected void createMasterDetailBlock(IManagedForm managedForm) {
		masterDetailBlock = new SmooksMasterDetailBlock(getEditor(),
				(AdapterFactoryEditingDomain) ((SmooksMultiFormEditor) getEditor()).getEditingDomain());
		ViewerFilter[] filters = createViewerFilters();
		masterDetailBlock.setViewerFilters(filters);
		masterDetailBlock.setMainSectionDescription(getMainSectionDescription());
		masterDetailBlock.setMainSectionTitle(getMainSectionTitle());
		masterDetailBlock.setNewSmooksElementTitle(getNewSmooksElementTitle());
		masterDetailBlock.setNewSmooksElementDescription(getNewSmooksElementDescription());
		masterDetailBlock.createContent(managedForm);
	}

	protected String getNewSmooksElementTitle() {
		return null;
	}

	protected String getNewSmooksElementDescription() {
		return null;
	}

	protected ViewerFilter[] createViewerFilters() {
		return new ViewerFilter[] { new TextEObjectModelFilter() };
	}

	protected SmooksGraphicsExtType getSmooksGraphicsExtType() {
		SmooksGraphicsExtType extType = ((SmooksMultiFormEditor) getEditor()).getSmooksGraphicsExt();
		return extType;
	}

	public void setSmooksModel(Object model) {
		if (masterDetailBlock != null) {
			masterDetailBlock.setSmooksModel(model);
		}
	}

	protected void showInputDataWizard() {
		StructuredDataSelectionWizard wizard = new StructuredDataSelectionWizard();
		wizard.setInput(getEditorInput());
		wizard.setSite(getEditorSite());
		wizard.setForcePreviousAndNextButtons(true);
		StructuredDataSelectionWizardDailog dialog = new StructuredDataSelectionWizardDailog(
				getEditorSite().getShell(), wizard, getSmooksGraphicsExtType(), (SmooksMultiFormEditor) getEditor());
		dialog.show();
	}

	public void setSelectionToViewer(final Collection<?> collection) {
		if (masterDetailBlock != null && masterDetailBlock.getSmooksTreeViewer() != null) {
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

	public void validateEnd(List<Diagnostic> diagnosticResult) {
		if (masterDetailBlock != null)
			masterDetailBlock.refreshSmooksTreeViewer();
	}

	public void validateStart() {

	}

	public void saveComplete(SmooksGraphicsExtType extType) {
	}

	public void sourceChange(Object model) {
		this.setSmooksModel(model);
	}

}
