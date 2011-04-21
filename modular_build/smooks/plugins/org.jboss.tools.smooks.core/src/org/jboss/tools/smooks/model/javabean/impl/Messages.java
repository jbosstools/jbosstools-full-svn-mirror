package org.jboss.tools.smooks.model.javabean.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.model.javabean.impl.messages"; //$NON-NLS-1$
	public static String JavabeanFactoryImpl_Error_Class_not_valid;
	public static String JavabeanFactoryImpl_Error_Not_Valid_Identifier;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
