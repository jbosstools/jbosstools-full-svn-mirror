package org.jboss.tools.smooks.configuration.wizards;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.wizards.messages"; //$NON-NLS-1$
	public static String SmooksConfigurationFileNewWizard_0;
	public static String SmooksConfigurationFileNewWizard_ErrorDialogTitle;
	public static String SmooksConfigurationFileNewWizard_PageName;
	public static String SmooksConfigurationFileNewWizard_VersionPageName;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
