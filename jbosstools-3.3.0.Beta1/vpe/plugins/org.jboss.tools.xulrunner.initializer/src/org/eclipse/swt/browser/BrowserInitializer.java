package org.eclipse.swt.browser;

import org.eclipse.core.runtime.Platform;

public class BrowserInitializer {

	private static final String PROPERTY_DEFAULTTYPE = "org.eclipse.swt.browser.DefaultType"; //$NON-NLS-1$

	static {
		/* Under Linux instantiation of WebKit should be avoided,
		 * because WebKit and XULRunner running simultaneously
		 * may cause native errors.
		 * 
		 * Also see JBIDE-9144 and JBIDE-10185. 	 */
		if (Platform.OS_LINUX.equals(Platform.getOS())) {
			String defaultType = System.getProperty(PROPERTY_DEFAULTTYPE);
			if (defaultType == null) {
				System.setProperty(PROPERTY_DEFAULTTYPE, "mozilla"); //$NON-NLS-1$
			}
		}
	}
}
