package org.jboss.tools.smooks.configuration.editors.esbrouter;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.editors.esbrouter.messages"; //$NON-NLS-1$
	public static String RouteBeanPropertyUICreator_Category;
	public static String RouteBeanPropertyUICreator_Name;
	public static String RouteBeanPropertyUICreator_Route_On_Element;
	public static String RouteBeanPropertyUICreator_Route_To_Service;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
