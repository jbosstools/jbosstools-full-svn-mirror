package org.jboss.tools.smooks.xml2java.analyzer;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.xml2java.analyzer.messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String AbstractXMLModelAnalyzer_FileDosentExistErrorMessage;
	public static String AbstractXMLModelAnalyzer_IllegalPathErrorMessage;
	public static String XML2JavaAnalyzer_CantFindNodeErrorMessage;
	public static String XML2JavaAnalyzer_CantFindRootNodeErrorMessage;
	public static String XML2JavaAnalyzer_ConnectQDlgContent;
	public static String XML2JavaAnalyzer_ConnectQDlgTitle;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}