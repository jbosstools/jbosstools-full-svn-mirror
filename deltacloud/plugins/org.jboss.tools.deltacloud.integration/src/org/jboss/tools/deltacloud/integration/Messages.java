package org.jboss.tools.deltacloud.integration;

import org.eclipse.osgi.util.NLS;

public class Messages {
	private static final String BUNDLE_NAME = DeltaCloudIntegrationPlugin.PLUGIN_ID + ".Messages"; //$NON-NLS-1$
	
	public static String ERROR;
	public static String COULD_NOT_LAUNCH_RSE_EXPLORER;
	public static String COULD_NOT_LAUNCH_RSE_EXPLORER2;
	public static String RSE_CONNECTING_MESSAGE;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	private Messages() {
	}
}
