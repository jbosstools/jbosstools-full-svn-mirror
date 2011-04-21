package org.jboss.tools.smooks.configuration.editors.json;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.editors.json.messages"; //$NON-NLS-1$
	public static String JsonDataPathWizardPage_WizardDes;
	public static String JsonDataPathWizardPage_WIzardTitle;
	public static String JsonDataWizard_PageText;
	public static String JsonDataWizard_WizardTitle;
	public static String JsonReaderUICreator_KeysMapLabel;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
