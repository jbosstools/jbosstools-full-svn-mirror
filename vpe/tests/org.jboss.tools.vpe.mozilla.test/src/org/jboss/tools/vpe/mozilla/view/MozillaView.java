/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.mozilla.view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import org.jboss.tools.vpe.mozilla.browser.MozillaBrowser;


/**
 * This class wes developed for test corectly starting of Mozilla.
 * Mozilla is used as part of Visual Pade Editor. 
 */

public class MozillaView extends ViewPart {
	private static final String INIT_URL = "about:buildconfig";
	private MozillaBrowser browser;

	/**
	 * The constructor.
	 */
	public MozillaView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		browser = new MozillaBrowser(parent, SWT.NONE);
		browser.setUrl(INIT_URL);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		browser.setFocus();
	}

	/**
	 * This method is used by other test plugins for testing mozilla's functionality
	 * @return MozillaBrowser
	 */
	public MozillaBrowser getBrowser() {
		return browser;
	}
}