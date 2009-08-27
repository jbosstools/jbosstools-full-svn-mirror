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

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.smooks.AbstractReader;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart
 * 
 */
public class SmooksConfigurationReaderPage extends SmooksConfigurationFormPage {

	private TableViewer inputDataViewer;

	public SmooksConfigurationReaderPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	public SmooksConfigurationReaderPage(String id, String title) {
		super(id, title);
	}

	@Override
	protected ViewerFilter[] createViewerFilters() {
		return new ViewerFilter[] { new OnlyReaderViewerFilter() };
	}

	@Override
	protected SmooksMasterDetailBlock createSmooksMasterDetailsBlock() {
		return new SmooksMasterDetailBlock(getEditor(),
				(AdapterFactoryEditingDomain) ((SmooksMultiFormEditor) getEditor()).getEditingDomain()) {
			@Override
			protected Object getEmptyDefaultSelection(EObject smooksTreeViewerInput) {
				List<EObject> contents = smooksTreeViewerInput.eContents();
				for (Iterator<?> iterator = contents.iterator(); iterator.hasNext();) {
					EObject eObject = (EObject) iterator.next();
					if (smooksTreeViewerInput instanceof SmooksResourceListType && eObject instanceof AbstractReader) {
						return eObject;
					}
				}
				return super.getEmptyDefaultSelection(smooksTreeViewerInput);
			}
		};
	}

	@Override
	protected String getNewSmooksElementDescription() {
		return "create new smooks reader";
	}

	@Override
	protected String getNewSmooksElementTitle() {
		return "New Smooks Reader";
	}

	@Override
	protected String getMainSectionDescription() {
		return "Edit the smooks readers";
	}

	@Override
	protected String getMainSectionTitle() {
		return "Reader";
	}

	@Override
	protected void setPageTitle(ScrolledForm form) {
		form.setText("Reader/Input");
	}

	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		// create extention UI for add/remove extention data
		createExtentionArea(managedForm);
	}

	protected void createExtentionArea(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		final ScrolledForm form = managedForm.getForm();
		Section section = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		section.setLayoutData(gd);
		section.setText("Input Data");
		FillLayout flayout = new FillLayout();
		section.setLayout(flayout);

		Composite mainComposite = toolkit.createComposite(section, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		mainComposite.setLayout(gl);
		section.setClient(mainComposite);

		Composite tableComposite = toolkit.createComposite(mainComposite, SWT.NONE);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 1;
		fillLayout.marginWidth = 1;
		gd = new GridData(GridData.FILL_BOTH);
		tableComposite.setLayoutData(gd);
		tableComposite.setBackground(GraphicsConstants.BORDER_CORLOER);
		tableComposite.setLayout(fillLayout);

		inputDataViewer = new TableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		TableColumn header = new TableColumn(inputDataViewer.getTable(), SWT.NONE);
		header.setText("Type");
		header.setWidth(100);
		TableColumn pathColumn = new TableColumn(inputDataViewer.getTable(), SWT.NONE);
		pathColumn.setText("Path");
		pathColumn.setWidth(300);

		TableColumn extColumn = new TableColumn(inputDataViewer.getTable(), SWT.NONE);
		extColumn.setText("Extension Paramers");
		extColumn.setWidth(400);

		inputDataViewer.setContentProvider(new ExtentionInputContentProvider());
		inputDataViewer.setLabelProvider(new ExtentionInputLabelProvider());
		inputDataViewer.getTable().setHeaderVisible(true);
		inputDataViewer.getTable().setLinesVisible(true);
		SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
		if (extType != null) {
			inputDataViewer.setInput(extType);
		}
		Composite buttonComposite = toolkit.createComposite(mainComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_VERTICAL);
		buttonComposite.setLayoutData(gd);
		GridLayout l = new GridLayout();
		buttonComposite.setLayout(l);

		Button addButton = toolkit.createButton(buttonComposite, "Add", SWT.FLAT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);
		addButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				showInputDataWizard();
			}

		});

		Button removeButton = toolkit.createButton(buttonComposite, "Delete", SWT.FLAT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		removeButton.setLayoutData(gd);
		removeButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) inputDataViewer.getSelection();
				if (selection != null) {
					SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
					if (extType != null) {
						boolean canSave = false;
						Object[] objs = selection.toArray();
						for (int i = 0; i < objs.length; i++) {
							Object obj = objs[i];
							if (obj instanceof InputType) {
								extType.getInput().remove(obj);
								canSave = true;
							}
						}
						if (!canSave)
							return;
						try {
							extType.eResource().save(Collections.emptyMap());
							inputDataViewer.refresh();
						} catch (IOException t) {
							SmooksConfigurationActivator.getDefault().log(t);
						}
					}
				}
			}
		});
	}

	public void saveComplete(SmooksGraphicsExtType extType) {
		inputDataViewer.refresh();
	}

}
