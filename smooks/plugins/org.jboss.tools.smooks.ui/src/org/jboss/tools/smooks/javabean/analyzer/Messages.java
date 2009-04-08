package org.jboss.tools.smooks.javabean.analyzer;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.javabean.analyzer.messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String JavaBeanAnalyzer_ClassNotExist;
	public static String JavaBeanAnalyzer_ConnectionQuestion;
	public static String JavaBeanAnalyzer_ConnectRootQuestion;
	public static String JavaBeanAnalyzer_DoesNotExist;
	public static String JavaModelConnectionResolveCommand_SmooksContextIsNull;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}