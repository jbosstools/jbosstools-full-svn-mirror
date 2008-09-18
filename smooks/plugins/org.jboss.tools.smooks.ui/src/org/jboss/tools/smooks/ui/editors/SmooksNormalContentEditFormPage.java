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

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
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
import org.jboss.tools.smooks.analyzer.NormalSmooksModelPackage;
import org.jboss.tools.smooks.model.DocumentRoot;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart Peng Date : 2008-9-9
 */
public class SmooksNormalContentEditFormPage extends FormPage {

	protected NormalSmooksModelPackage modelPackage = null;

	protected SmooksResourceConfigFormBlock resourceBlock = null;

	public SmooksNormalContentEditFormPage(FormEditor editor, String id,
			String title, NormalSmooksModelPackage modelPacakge) {
		super(editor, id, title);
		this.setModelPackage(modelPacakge);
		this.createResourceConfigFormBlock();
	}

	public SmooksNormalContentEditFormPage(String id, String title,
			NormalSmooksModelPackage modelPacakge) {
		super(id, title);
		this.setModelPackage(modelPackage);
		this.createResourceConfigFormBlock();
	}
	
	protected void createResourceConfigFormBlock(){
		resourceBlock = new SmooksResourceConfigFormBlock();
		resourceBlock.setDomain(getEditingDomain());
		resourceBlock.setParentEditor((SmooksFormEditor)this.getEditor());
	}
	
	protected EditingDomain getEditingDomain(){
		return ((SmooksFormEditor)getEditor()).getEditingDomain();
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit tool = managedForm.getToolkit();
		tool.decorateFormHeading(form.getForm());
		GridLayout gridLayout = UIUtils.createGeneralFormEditorLayout(1);
		resourceBlock.createContent(managedForm);
		Composite rootMainControl = form.getBody();
		form.setText("Normal Page");
		createSmooksTypeGUI(rootMainControl, tool);
		form.getBody().setLayout(gridLayout);
		form.pack();
		
		resourceBlock.initViewers();
	}

	protected void createSmooksTypeGUI(Composite mainComposite, FormToolkit tool) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		Section section = tool.createSection(mainComposite, Section.TITLE_BAR
				| Section.DESCRIPTION | Section.TWISTIE);
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

	/**
	 * @return the modelPackage
	 */
	public NormalSmooksModelPackage getModelPackage() {
		return modelPackage;
	}

	/**
	 * @param modelPackage
	 *            the modelPackage to set
	 */
	public void setModelPackage(NormalSmooksModelPackage modelPackage) {
		if (modelPackage == this.modelPackage)
			return;
		this.modelPackage = modelPackage;
		this.resourceBlock.setModelPackage(this.modelPackage);
	}

}
