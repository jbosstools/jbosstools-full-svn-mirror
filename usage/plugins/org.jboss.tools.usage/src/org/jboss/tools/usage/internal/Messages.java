package org.jboss.tools.usage.internal;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.usage.internal.messages"; //$NON-NLS-1$
	public static String UsageReport_Checkbox_Text;
	public static String UsageReport_DialogMessage;
	public static String UsageReport_DialogTitle;
	public static String UsageReport_Error_SavePreferences;
	public static String UsageReport_GoogleAnalyticsAccount;
	public static String UsageReport_HostName;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
