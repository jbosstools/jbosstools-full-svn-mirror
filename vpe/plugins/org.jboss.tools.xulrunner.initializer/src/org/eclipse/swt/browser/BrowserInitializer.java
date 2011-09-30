package org.eclipse.swt.browser;

public class BrowserInitializer {

	private static final String PROPERTY_DEFAULTTYPE = "org.eclipse.swt.browser.DefaultType"; //$NON-NLS-1$

	static {
		String defaultType = System.getProperty(PROPERTY_DEFAULTTYPE);
		if (defaultType == null) {
			System.setProperty(PROPERTY_DEFAULTTYPE, "mozilla"); //$NON-NLS-1$
		}
	}
}
