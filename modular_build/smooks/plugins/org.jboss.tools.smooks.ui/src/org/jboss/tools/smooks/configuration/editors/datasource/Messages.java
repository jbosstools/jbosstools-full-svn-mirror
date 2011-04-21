package org.jboss.tools.smooks.configuration.editors.datasource;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.editors.datasource.messages"; //$NON-NLS-1$
	public static String DirectUICreator_Binding_On_Element;
	public static String DirectUICreator_DB_Connection_Test;
	public static String DirectUICreator_Load_DB_Properties;
	public static String DirectUICreator_Msg_Test_Success;
	public static String DirectUICreator_Msg_User_Canceled;
	public static String DirectUICreator_Test_Connection;
	public static String DirectUICreator_Test_Success;
	public static String DirectUICreator_Trying_to_Connect;
	public static String DirectUICreator_User_Canceled;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
