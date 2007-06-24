/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.mozilla.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.jboss.tools.common.log.LogHelper;
import org.jboss.tools.vpe.mozilla.MozillaJavaXpComPlugin;
import org.jboss.tools.vpe.mozilla.browser.MozillaBrowser;

public class MozillaViewPart extends ViewPart {
	private MozillaBrowser browser;

	public void createPartControl(Composite parent) {
		try {
			browser = new MozillaBrowser(parent, SWT.NONE);
			browser.setUrl("http://mozile.mozdev.org/index.html");
		} catch (Exception e) {
			LogHelper.logError(MozillaJavaXpComPlugin.ID, "Cannot show Mozilla browser.", e);
			Composite composite = new Composite(parent, SWT.NONE);
			Label label = new Label(composite, SWT.NONE);
			label.setText("Cannot show Mozilla browser.");
		}
	}

	public void setFocus() {
		browser.setFocus();
	}

}
