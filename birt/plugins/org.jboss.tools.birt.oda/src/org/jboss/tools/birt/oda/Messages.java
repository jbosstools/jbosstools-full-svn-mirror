package org.jboss.tools.birt.oda;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.birt.oda.messages"; //$NON-NLS-1$
	public static String AbstractOdaFactory_The_type_is_not_valid;
	public static String ConsoleConfigurationOdaFactory_Invalid_configuration;
	public static String DataTypes_Invalid_type_name;
	public static String HibernateConnection_Invalid_AppContext;
	public static String HibernateDataSetMetaData_Hibernate_Data_Source;
	public static String HibernateDriver_Non_defined;
	public static String HibernateResultSet_Cursor_has_not_been_initialized;
	public static String HibernateResultSet_The_data_type_is_not_valid;
	public static String HibernateResultSetMetaData_Argument_cannot_be_null;
	public static String HibernateResultSetMetaData_Invalid_index;
	public static String ReflectServerOdaFactory_Cannot_create_Hibernate_session_factory;
	public static String ReflectServerOdaFactory_The_type_is_not_valid;
	public static String ServerOdaFactory_Cannot_create_Hibernate_session_factory;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
