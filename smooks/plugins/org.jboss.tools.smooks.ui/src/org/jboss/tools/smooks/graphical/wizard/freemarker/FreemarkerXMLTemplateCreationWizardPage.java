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
package org.jboss.tools.smooks.graphical.wizard.freemarker;

import java.util.List;

import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jboss.tools.smooks.configuration.editors.xml.XSDStructuredDataWizardPage;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart
 * 
 */
public class FreemarkerXMLTemplateCreationWizardPage extends XSDStructuredDataWizardPage {

	private Combo combo = null;

	public FreemarkerXMLTemplateCreationWizardPage(String pageName, boolean multiSelect, Object[] initSelections,
			List<ViewerFilter> filters) {
		super(pageName, multiSelect, initSelections, filters);
	}

	public FreemarkerXMLTemplateCreationWizardPage(String pageName) {
		super(pageName);
		this.setTitle("Create XML template model");
		this.setDescription("Create XML template model.");
//		this.fileExtensionNames = new String[]{"xml"};
	}

	public String getInputType(){
		if(combo.getSelectionIndex() == 0){
			return SmooksModelUtils.KEY_XML_FILE_TYPE_XSD;
		}
		if(combo.getSelectionIndex() == 1){
			return SmooksModelUtils.KEY_XML_FILE_TYPE_XML;
		}
		return null;
	}
	
	/**
	 * @return the combo
	 */
	public Combo getCombo() {
		return combo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		mainComposite.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_BOTH);
		mainComposite.setLayoutData(gd);

		Label fileTypeLabel = new Label(mainComposite, SWT.NONE);
		fileTypeLabel.setText("XML Template File Type:");

		combo = new Combo(mainComposite, SWT.READ_ONLY | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		initCombo(combo);
		combo.setLayoutData(gd);
		combo.select(0);

		combo.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateWizardPage();
			}
		});
		
		Label separator = new Label(mainComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(gd);
		
		super.createControl(mainComposite);

		this.setControl(mainComposite);
	}

	protected void updateWizardPage() {
		int index = combo.getSelectionIndex();
		if(index == 0){
			loadXSDButton.setEnabled(true);
			this.tableViewer.getTable().setEnabled(true);
		}
		if(index == 1){
			loadXSDButton.setEnabled(false);
			this.tableViewer.getTable().setEnabled(false);
		}
	}

	private void initCombo(Combo combo2) {
		combo2.add("Load from XSD file");
//		combo2.add("Load from XML Simple File");
	}

}
