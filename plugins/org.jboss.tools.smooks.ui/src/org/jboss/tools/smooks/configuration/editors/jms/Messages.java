package org.jboss.tools.smooks.configuration.editors.jms;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.editors.jms.messages"; //$NON-NLS-1$
	public static String JmsRouterUICreator_RouteOnGroupText;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
