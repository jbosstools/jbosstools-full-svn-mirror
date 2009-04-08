package org.jboss.tools.smooks.javabean.model;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.javabean.model.messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String JavaBeanModel_JavaBeanName;
	public static String JavaBeanModel_JavaBeanDetails;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}