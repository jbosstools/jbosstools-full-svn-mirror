package org.jboss.tools.smooks.model.calc.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.model.calc.impl.messages"; //$NON-NLS-1$
	public static String CalcFactoryImpl_Error_Invalid_Classifier;
	public static String CalcFactoryImpl_Error_Invalid_Classifier2;
	public static String CalcFactoryImpl_Error_Invalid_Datatype;
	public static String CalcFactoryImpl_Error_Invalid_Enumerator;
	public static String CalcFactoryImpl_Error_Invalid_Enumerator2;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
