package org.jboss.tools.seam.ui.pages;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public class SeamUIPagesMessages extends NLS{
	private static final String BUNDLE_NAME = "org.jboss.tools.seam.ui.pages.messages"; //$NON-NLS-1$

	private static ResourceBundle fResourceBundle; 
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, SeamUIPagesMessages.class);		
	}
	
	public static String PAGESDIAGRAM_SELECT;
	public static String PAGESDIAGRAM_MARQUEE;
	public static String PAGESDIAGRAM_CREATE_NEW_CONNECTION;
	public static String SEAM_UI_PAGES_PLUGIN_NO_MESSAGES;
	
	public static ResourceBundle getResourceBundle() {
		try {
			if (fResourceBundle == null)
				fResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
		}
		catch (MissingResourceException x) {
			SeamUiPagesPlugin.log(x);
			fResourceBundle = null;
		}
		return fResourceBundle;
	}	

}
