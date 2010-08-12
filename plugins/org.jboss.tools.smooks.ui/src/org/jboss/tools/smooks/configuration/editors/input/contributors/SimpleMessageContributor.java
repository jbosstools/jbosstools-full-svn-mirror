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
package org.jboss.tools.smooks.configuration.editors.input.contributors;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.editor.FormPage;
import org.jboss.tools.smooks.configuration.editors.input.InputReaderConfigurationContributor;

/**
 * Simple contributor used to add a "No type selected" warning message to the composite.
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class SimpleMessageContributor implements InputReaderConfigurationContributor {

	private String message;

	public SimpleMessageContributor(String message) {
		this.message = message;
	}
	
	public void addInputConfigControls(FormPage formPage, Composite configComposite) {
		Label formText = formPage.getManagedForm().getToolkit().createLabel(configComposite, ""); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_BOTH);
		
		gd.heightHint = 50;
		gd.horizontalSpan = 2;
		formText.setLayoutData(gd);
		formText.setText(message);
	}
}
