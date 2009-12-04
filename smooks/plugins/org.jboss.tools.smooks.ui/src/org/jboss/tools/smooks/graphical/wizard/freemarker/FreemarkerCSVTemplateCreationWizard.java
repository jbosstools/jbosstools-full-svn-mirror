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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author Dart
 * 
 */
public class FreemarkerCSVTemplateCreationWizard extends Wizard {

	private FreemarkerCSVCreationWizardPage page;
	private String seprator;
	private String quote;

	private List<String> fields = new ArrayList<String>();
	private String fieldsString;

	public FreemarkerCSVTemplateCreationWizard() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the seprator
	 */
	public String getSeprator() {
		return seprator;
	}

	/**
	 * @return the quote
	 */
	public String getQuote() {
		return quote;
	}

	/**
	 * @return the fields
	 */
//	public List<String> getFields() {
//		return fields;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		if (page == null) {
			page = new FreemarkerCSVCreationWizardPage("CSV");
		}
		this.addPage(page);
		super.addPages();
	}
	
	

	public String getFieldsString() {
		return fieldsString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		if (page != null) {
			seprator = page.getSeperatorText().getText();
			quote = page.getQuoteText().getText();
			fieldsString = page.getFieldsText().getText();
//			List<FieldText> fieldList = page.getFieldsList();
//			for (Iterator<?> iterator = fieldList.iterator(); iterator.hasNext();) {
//				FieldText fieldText = (FieldText) iterator.next();
//				fields.add(fieldText.getText());
//			}
			return true;
		}
		return true;
	}

}
