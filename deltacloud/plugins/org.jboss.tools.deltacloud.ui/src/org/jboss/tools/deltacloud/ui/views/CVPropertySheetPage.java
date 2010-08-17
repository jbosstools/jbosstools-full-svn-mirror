package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.ui.views.properties.PropertySheetPage;

public class CVPropertySheetPage extends PropertySheetPage {
	
	public CVPropertySheetPage() {
		super();
		setSorter(new CVPropertySheetNonSorter());
	}

}
