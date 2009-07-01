package org.jboss.tools.process.ruleflow;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.process.ruleflow.messages"; //$NON-NLS-1$
	public static String RuleFlowPaletteFactory_ConnectionDesc;
	public static String RuleFlowPaletteFactory_ConnectionLabel;
	public static String RuleFlowPaletteFactory_StartDesc;
	public static String RuleFlowPaletteFactory_StartLabel;
	public static String RuleFlowPaletteFactory_SubProcessDesc;
	public static String RuleFlowPaletteFactory_SubProcessLabel;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
