package org.jboss.tools.deltacloud.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class DeltaCloudPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.addView("org.jboss.tools.deltacloud.ui.views.CloudView", IPageLayout.LEFT,
				0.25f, IPageLayout.ID_EDITOR_AREA);
		layout.addView(IPageLayout.ID_PROP_SHEET, IPageLayout.BOTTOM,
				0.80f, IPageLayout.ID_EDITOR_AREA);
	}

}
