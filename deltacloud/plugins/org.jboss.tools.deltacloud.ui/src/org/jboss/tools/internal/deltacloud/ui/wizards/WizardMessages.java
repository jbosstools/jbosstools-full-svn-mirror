package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class WizardMessages {

	private static final String BUNDLE_NAME = WizardMessages.class.getName();

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
