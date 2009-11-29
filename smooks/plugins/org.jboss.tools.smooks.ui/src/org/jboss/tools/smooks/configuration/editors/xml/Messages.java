package org.jboss.tools.smooks.configuration.editors.xml;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.editors.xml.messages"; //$NON-NLS-1$
	public static String XSDStructuredDataWizard_WizardTitle;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
