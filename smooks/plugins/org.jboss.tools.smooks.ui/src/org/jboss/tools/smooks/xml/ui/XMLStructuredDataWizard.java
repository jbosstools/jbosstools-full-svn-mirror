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
package org.jboss.tools.smooks.xml.ui;

import java.util.Properties;

import org.jboss.tools.smooks.ui.SmooksUIActivator;
import org.jboss.tools.smooks.xml.AbstractFileSelectionWizardPage;
import org.jboss.tools.smooks.xml.AbstractStructuredDdataWizard;

/**
 * @author Dart Peng
 * @Date Aug 18, 2008
 */
public class XMLStructuredDataWizard extends AbstractStructuredDdataWizard {
	
	
	Properties properties = new Properties();
	String filePath = null;

	public static final String XML_FILE = "xmlFile";

	@Override
	protected AbstractFileSelectionWizardPage createAbstractFileSelectionWizardPage() {
		return new XMLStructuredDataWizardPage("XML");
	}

	public boolean performFinish() {
		filePath = page.getFilePath();
//		properties.put(XML_FILE, filePath);
		return super.performFinish();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.ui.IStrucutredDataCreationWizard#getInputDataTypeID()
	 */
	public String getInputDataTypeID() {
		return SmooksUIActivator.TYPE_ID_XML;
	}

	public Properties getProperties() {
		return properties;
	}
}
