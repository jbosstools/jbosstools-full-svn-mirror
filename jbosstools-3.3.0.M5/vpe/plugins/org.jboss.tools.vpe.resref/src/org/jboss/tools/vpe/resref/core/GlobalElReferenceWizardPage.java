/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.resref.core;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;
import org.jboss.tools.common.resref.core.ResourceReference;

public class GlobalElReferenceWizardPage extends ELReferenceWizardPage {

	public GlobalElReferenceWizardPage(String pageName, String title,
			ImageDescriptor titleImage, Object fileLocation) {
		super(pageName, title, titleImage, fileLocation);
	}

	@Override
	protected Group createScopeGroup(Composite parent) {
		Group groupControl = new Group(parent, SWT.SHADOW_ETCHED_IN);
		groupControl.setText(Messages.SCOPE_GROUP_NAME);
		Layout layout = new GridLayout(1, false);
		groupControl.setLayout(layout);
		
		Button globalRadioButton = new Button(groupControl, SWT.RADIO);
		globalRadioButton.setText(Messages.SCOPE_GLOBAL);
		globalRadioButton.setSelection(true);
		globalRadioButton.addSelectionListener(this);
		
		return groupControl;
	}

	@Override
	protected int getSelectedScope() {
		return ResourceReference.GLOBAL_SCOPE;
	}

	
}
