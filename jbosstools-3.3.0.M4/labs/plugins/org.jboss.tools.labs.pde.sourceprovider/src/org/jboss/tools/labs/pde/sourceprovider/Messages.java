package org.jboss.tools.labs.pde.sourceprovider;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.labs.pde.sourceprovider.messages"; //$NON-NLS-1$
	public static String EclipseSourceContainerType_EclipseHomeNotFonud;
	public static String EclipseSourceContainerType_EclipseInstallationName;
	public static String EclipseSourceContainerType_ErrorUnserializing;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
