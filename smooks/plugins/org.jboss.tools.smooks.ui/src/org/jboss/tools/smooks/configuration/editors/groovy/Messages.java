package org.jboss.tools.smooks.configuration.editors.groovy;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.editors.groovy.messages"; //$NON-NLS-1$
	public static String GroovyUICreator_ExecuteOnElemenGroupName;
	public static String ScriptUICreator_1;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
