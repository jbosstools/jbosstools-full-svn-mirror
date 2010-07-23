package org.jboss.tools.deltacloud.ui.views;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CVMessages {

	private static final String BUNDLE_NAME = CVMessages.class.getName();

	public static String getString(String key) {
		try {
			return ResourceBundle.getBundle(BUNDLE_NAME).getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		} catch (NullPointerException e) {
			return '#' + key + '#';
		}
	}	

}
