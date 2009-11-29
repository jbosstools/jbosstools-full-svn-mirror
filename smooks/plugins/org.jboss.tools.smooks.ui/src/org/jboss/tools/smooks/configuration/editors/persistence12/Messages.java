package org.jboss.tools.smooks.configuration.editors.persistence12;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.editors.persistence12.messages"; //$NON-NLS-1$
	public static String DeleterUICreator_DeleteOnElementGroupLabel;
	public static String FlusherUICreator_FlushGroupLabel;
	public static String InserterUICreator_InsertOnElementGroupText;
	public static String LocatorExpressionParamUICreator_ExecuteOnElementGroupText;
	public static String LocatorUICreator_LookupOnElementGroupLabel;
	public static String LocatorValueParamUICreator_DataGroupText;
	public static String LocatorWiringParamUICreator_WiringOnElementGroupLabel;
	public static String UpdaterUICreator_UpdateOnElementgroupLabel;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
