package org.jboss.tools.smooks.utils;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.utils.messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String UIUtils_ConnectAllConnections;
	public static String UIUtils_ConnectNode;
	public static String UIUtils_DisconnectAllConnections;
	public static String UIUtils_InstanceClassResolveMessage1;
	public static String UIUtils_InstanceClassResolveMessage2;
	public static String UIUtils_InstanceLoadedErrorMessage;
	public static String UIUtils_InstanceLoadedResolveMessage;
	public static String UIUtils_JavaModelLoadedErrorMessage;
	public static String UIUtils_ParentNodeConnectErrorMessage;
	public static String UIUtils_SelectorCheckErrorMessage;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}