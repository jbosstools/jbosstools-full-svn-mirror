package org.jboss.tools.birt.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.birt.core.messages"; //$NON-NLS-1$
	public static String BirtCoreActivator_The_folder_doesnt_exists;
	public static String BirtCoreActivator_The_resource_is_not_folder;
	public static String BirtPostInstallListener_Error_while_creating_JBoss_BIRT_artifacts;
	public static String BirtPostInstallListener_The_config_ini_file_doesnt_exist;
	public static String BirtPostInstallListener_The_resource_is_not_a_folder;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
