package org.eclipse.swt.browser;


public class WebKitInitializer {

	private static final String USE_WEB_KIT_GTK = "org.eclipse.swt.browser.UseWebKitGTK"; //$NON-NLS-1$

	static {
		String useWebKitGTK = System.getProperty(USE_WEB_KIT_GTK);
		if (useWebKitGTK == null) {
			System.setProperty(USE_WEB_KIT_GTK, "false"); //$NON-NLS-1$
		}
	}

}
