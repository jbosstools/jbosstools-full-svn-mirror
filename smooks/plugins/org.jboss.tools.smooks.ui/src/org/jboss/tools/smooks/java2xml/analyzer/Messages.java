package org.jboss.tools.smooks.java2xml.analyzer;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.java2xml.analyzer.messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String Java2XMLAnalyzer_CantFindRoot;
	public static String Java2XMLAnalyzer_CantGenerateConfig;
	public static String Java2XMLAnalyzer_DontSupportJ2X;
	public static String Java2XMLAnalyzer_Warning;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}