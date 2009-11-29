package org.jboss.tools.smooks.configuration.editors.javabean12;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.editors.javabean12.messages"; //$NON-NLS-1$
	public static String Javabean12ExpressionUICreator_ExecuteOnGroupText;
	public static String Javabean12ExpressionUICreator_ExpressionLabel;
	public static String JavaBean12PropertyUICreator_CantGetClassValueErrorMessage;
	public static String JavaBean12PropertyUICreator_ClassValueEmptyErrorMessage;
	public static String JavaBean12PropertyUICreator_CreateOnElementGroupText;
	public static String JavaBean12PropertyUICreator_ErrorDialogTitle;
	public static String JavabeanValueBinding12UICreator_DataGroupText;
	public static String JavabeanWiringBiding12UICreator_WiringOnGroupText;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
