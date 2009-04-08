package org.jboss.tools.smooks.xml.ui;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.xml.ui.messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String XMLStructuredDataWizardPage_XMLDataWizardPageDescription;
	public static String XMLStructuredDataWizardPage_XMLDataWizardPageTitle;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}