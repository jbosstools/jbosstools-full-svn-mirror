package org.jboss.tools.smooks.editor.propertySections;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.editor.propertySections.messages"; //$NON-NLS-1$
	public static String ValueDecodeParamSection_DeleteButtonText;
	public static String ValueDecodeParamSection_NewParamButtonText;
	public static String ValueDecodeParamSection_NullText;
	public static String ValueDecodeParamSection_ParamNameColumnText;
	public static String ValueDecodeParamSection_ParamValueColumnText;
	public static String ValueDecodeParamSection_SectionTitle;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
