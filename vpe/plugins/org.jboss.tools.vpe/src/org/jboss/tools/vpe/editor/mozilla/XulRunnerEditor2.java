/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.mozilla;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.vpe.xulrunner.XulRunnerException;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.mozilla.interfaces.nsIDOMElement;

public class XulRunnerEditor2 extends XulRunnerEditor {

	private MozillaEditor mozillaEditor;

	public XulRunnerEditor2(Composite parent, MozillaEditor mozillaEditor) throws XulRunnerException {
		super(parent);
		this.mozillaEditor = mozillaEditor;
	}

	public void onLoadWindow() {
		if (mozillaEditor == null) {
			return;
		}
		// if the first load page
		if (!mozillaEditor.isRefreshPage()) {
			super.onLoadWindow();
//			MozillaEditor.this.onLoadWindow();
		}
		// if only refresh page
		else {
			mozillaEditor.onReloadWindow();
			mozillaEditor.setRefreshPage(false);
		}

	}
	public void onElementResize(nsIDOMElement element, int constrains, int top, int left, int width, int height) {
		if (mozillaEditor != null && mozillaEditor.getResizeListener() != null) {
			mozillaEditor.getResizeListener().elementResized(element, constrains, top, left, width, height);
		}
	}
	public void onShowTooltip(int x, int y, String text) {
		if (mozillaEditor != null && mozillaEditor.getTooltipListener() != null) {
			mozillaEditor.getTooltipListener().onShowTooltip(x, y, text);
		}
	}
	public void onHideTooltip() {
		if (mozillaEditor != null && mozillaEditor.getTooltipListener() != null) {
			mozillaEditor.getTooltipListener().onHideTooltip();
		}
	}
	public void onDispose() {
		if (mozillaEditor != null) {
			mozillaEditor.tearDownEditor();
			mozillaEditor.removeListeners();
			mozillaEditor = null;
		}
		super.onDispose();
	}
}
