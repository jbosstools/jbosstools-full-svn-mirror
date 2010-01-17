package org.jboss.tools.smooks.configuration.editors.xml;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.editors.xml.messages"; //$NON-NLS-1$
	public static String XMLStructuredDataWizard_Window_Title;
	public static String XSDStructuredDataWizard_WizardTitle;
	public static String XMLTemplateCreationWizardPage_Button_Load;
	public static String XMLTemplateCreationWizardPage_Error_Must_Click_Load;
	public static String XMLTemplateCreationWizardPage_Error_Must_Select_Root;
	public static String XMLTemplateCreationWizardPage_Label_Select_Root;
	public static String XMLTemplateCreationWizardPage_page_description;
	public static String XMLTemplateCreationWizardPage_Page_Title;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
