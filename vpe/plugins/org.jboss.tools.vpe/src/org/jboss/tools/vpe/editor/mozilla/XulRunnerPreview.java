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

public class XulRunnerPreview extends XulRunnerEditor {

	private MozillaPreview mozillaPreview;

	public XulRunnerPreview(Composite parent, MozillaPreview mozillaPreview) throws XulRunnerException {
		super(parent);
		this.mozillaPreview = mozillaPreview;
	}

	public void onLoadWindow() {
		super.onLoadWindow();
		mozillaPreview.onLoadWindow();
	}
	
	public void onShowTooltip(int x, int y, String text) {
		if (mozillaPreview != null && mozillaPreview.getTooltipListener() != null) {
			mozillaPreview.getTooltipListener().onShowTooltip(x, y, text);
		}
	}
	public void onHideTooltip() {
		if (mozillaPreview != null && mozillaPreview.getTooltipListener() != null) {
			mozillaPreview.getTooltipListener().onHideTooltip();
		}
	}
	
	public void onDispose() {
		if (mozillaPreview != null) {
			mozillaPreview.detachMozillaEventAdapter();
			mozillaPreview = null;
		}
		super.onDispose();
	}
}
