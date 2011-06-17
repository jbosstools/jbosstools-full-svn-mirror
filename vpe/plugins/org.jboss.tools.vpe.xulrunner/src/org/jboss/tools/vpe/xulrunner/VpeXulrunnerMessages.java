package org.jboss.tools.vpe.xulrunner;

import org.eclipse.osgi.util.NLS;

public class VpeXulrunnerMessages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.vpe.xulrunner.messages"; //$NON-NLS-1$
	public static String XulRunnerBrowser_bundleDoesNotContainXulrunner;
	public static String XulRunnerBrowser_bundleNotFound;
	public static String XulRunnerBrowser_cannotGetPathToXulrunner;
	public static String XulRunnerBrowser_notAvailable;
	public static String XulRunnerBrowser_wrongVersion;
	public static String XulRunnerBrowser_embeddedXulRunnerIsDisabledByOption;
	public static String CURRENT_PLATFORM_IS_NOT_SUPPORTED;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, VpeXulrunnerMessages.class);
	}

	private VpeXulrunnerMessages() {
	}
}
