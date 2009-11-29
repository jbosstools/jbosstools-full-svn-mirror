package org.jboss.tools.smooks.configuration;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.messages"; //$NON-NLS-1$
	public static String SmooksConfigurationActivator_Smooks_ErrorDialog_Title;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
