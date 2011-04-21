package org.jboss.tools.profiler.internal.ui.launch;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.profiler.internal.ui.launch.messages"; //$NON-NLS-1$
	public static String JBossProfilerLaunchDelegate_error_writing_temporary_properties;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
