package org.jboss.tools.smooks.model.csv.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.model.csv.impl.messages"; //$NON-NLS-1$
	public static String CsvFactoryImpl_Error_Datatype_Invalid;
	public static String CsvFactoryImpl_Error_Invalid_Class_Classifier;
	public static String CsvFactoryImpl_Error_Invalid_Classifier;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
