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
package org.jboss.tools.smooks.xsd.ui;

import org.jboss.tools.smooks.xml.AbstractFileSelectionWizardPage;
import org.jboss.tools.smooks.xml.AbstractStructuredDdataWizard;
import org.jboss.tools.smooks.xml.XmlActivator;

/**
 * @author Dart Peng
 * @Date Aug 18, 2008
 */
public class XSDStructuredDataWizard extends AbstractStructuredDdataWizard {

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.xml.AbstractStructuredDdataWizard#createAbstractFileSelectionWizardPage()
	 */
	@Override
	protected AbstractFileSelectionWizardPage createAbstractFileSelectionWizardPage() {
		return new XSDStructuredDataWizardPage("XSD");
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.ui.IStrucutredDataCreationWizard#getInputDataTypeID()
	 */
	public String getInputDataTypeID() {
		return XmlActivator.TYPE_ID_XSD;
	}

}
