package org.jboss.tools.smooks.xml;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.xml.messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String AbstractFileSelectionWizardPage_BrowseFileSystemButtonText;
	public static String AbstractFileSelectionWizardPage_BrowseWorkspaceButtonText;
	public static String AbstractFileSelectionWizardPage_Errormessage;
	public static String AbstractFileSelectionWizardPage_XMLFilePathLabelText;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}