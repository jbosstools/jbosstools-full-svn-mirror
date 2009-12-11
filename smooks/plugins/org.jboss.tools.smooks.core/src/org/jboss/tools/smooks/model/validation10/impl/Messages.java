package org.jboss.tools.smooks.model.validation10.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.model.validation10.impl.messages"; //$NON-NLS-1$
	public static String Validation10FactoryImpl_Error_Class_Not_Valid;
	public static String Validation10FactoryImpl_Error_Datatype_Not_Valid;
	public static String Validation10FactoryImpl_Error_Not_Valid_Classifier;
	public static String Validation10FactoryImpl_Error_Not_Valid_Enumerator;
	public static String Validation10FactoryImpl_Error_Value_Not_Valid;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
