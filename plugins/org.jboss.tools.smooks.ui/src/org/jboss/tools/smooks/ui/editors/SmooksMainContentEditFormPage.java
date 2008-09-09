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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart Peng Date : 2008-9-9
 */
public class SmooksMainContentEditFormPage extends FormPage {

	public SmooksMainContentEditFormPage(FormEditor editor, String id,
			String title) {
		super(editor, id, title);
	}

	public SmooksMainContentEditFormPage(String id, String title) {
		super(id, title);
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit tool = managedForm.getToolkit();
		tool.decorateFormHeading(form.getForm());
		GridLayout gridLayout = UIUtils.createGeneralFormEditorLayout(2);
		form.getBody().setLayout(gridLayout);
		Composite rootMainControl = form.getBody();
		form.setText("Normal Page");
		createResourceConfigGUI(rootMainControl, tool);
		createSmooksTypeGUI(rootMainControl, tool);
		createDataTypeGUI(rootMainControl, tool);
	}

	protected void createDataTypeGUI(Composite rootMainControl, FormToolkit tool) {
		Section section = tool.createSection(rootMainControl, Section.TITLE_BAR
				| Section.DESCRIPTION);
		section.setText("Data Type");

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		section.setLayoutData(gd);
		Composite dataTypeComposite = tool.createComposite(section);
		section.setClient(dataTypeComposite);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		dataTypeComposite.setLayout(layout);

		Composite tableComposite = tool
				.createComposite(dataTypeComposite);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 1;
		fillLayout.marginWidth = 1;
		tableComposite.setLayout(fillLayout);
		TableViewer tableTreeViewer = new TableViewer(tableComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		tableComposite.setLayoutData(gd);
		tableComposite.setBackground(GraphicsConstants.groupBorderColor);
		tool.paintBordersFor(tableComposite);

		Composite buttonComposite = tool
				.createComposite(dataTypeComposite);
		gd = new GridData(GridData.FILL_VERTICAL);
		buttonComposite.setLayoutData(gd);

		GridLayout buttonLayout = new GridLayout();
		buttonComposite.setLayout(buttonLayout);

		Button addButton = tool.createButton(buttonComposite, "New", SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);
		Button removeButton = tool.createButton(buttonComposite, "Delete",
				SWT.NONE);
		removeButton.setLayoutData(gd);

		Button upButton = tool.createButton(buttonComposite, "Up", SWT.NONE);
		upButton.setLayoutData(gd);

		Button downButton = tool
				.createButton(buttonComposite, "Down", SWT.NONE);
		downButton.setLayoutData(gd);
	}

	protected void createResourceConfigGUI(Composite rootMainControl,
			FormToolkit tool) {
		Section section = tool
				.createSection(rootMainControl, Section.TITLE_BAR);
		section.setText("Resource Config");

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 2;
		section.setLayoutData(gd);
		Composite resourceConfigComposite = tool.createComposite(section);
		section.setClient(resourceConfigComposite);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		resourceConfigComposite.setLayout(layout);

		Composite tableComposite = tool
				.createComposite(resourceConfigComposite);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 1;
		fillLayout.marginWidth = 1;
		tableComposite.setLayout(fillLayout);
		TableViewer tableTreeViewer = new TableViewer(tableComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		tableComposite.setLayoutData(gd);
		tableComposite.setBackground(GraphicsConstants.groupBorderColor);
		tool.paintBordersFor(tableComposite);

		Composite buttonComposite = tool
				.createComposite(resourceConfigComposite);
		gd = new GridData(GridData.FILL_VERTICAL);
		buttonComposite.setLayoutData(gd);

		GridLayout buttonLayout = new GridLayout();
		buttonComposite.setLayout(buttonLayout);

		Button addButton = tool.createButton(buttonComposite, "New", SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);
		Button removeButton = tool.createButton(buttonComposite, "Delete",
				SWT.NONE);
		removeButton.setLayoutData(gd);

		Button upButton = tool.createButton(buttonComposite, "Up", SWT.NONE);
		upButton.setLayoutData(gd);

		Button downButton = tool
				.createButton(buttonComposite, "Down", SWT.NONE);
		downButton.setLayoutData(gd);

	}

	protected void createSmooksTypeGUI(Composite mainComposite, FormToolkit tool) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		Section section = tool.createSection(mainComposite, Section.TITLE_BAR);
		section.setLayoutData(gd);
		Composite typeSelectComposite = tool.createComposite(section);
		section.setClient(typeSelectComposite);
		section.setText("Smooks Parse Type");
		GridLayout layout1 = new GridLayout();
		typeSelectComposite.setLayout(layout1);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		typeSelectComposite.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		typeSelectComposite.setLayout(layout);

		createTypeSelectRadioButton(typeSelectComposite, tool, "SAX")
				.setSelection(true);
		createTypeSelectRadioButton(typeSelectComposite, tool, "DOM");
		createTypeSelectRadioButton(typeSelectComposite, tool, "SAX/DOM");

	}

	private Button createTypeSelectRadioButton(Composite parent,
			FormToolkit tool, String labelName) {
		Button button = tool.createButton(parent, labelName, SWT.RADIO);
		return button;
	}

}
