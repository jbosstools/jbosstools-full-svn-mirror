package org.jboss.tools.smooks.configuration.editors.edi;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.editors.edi.messages"; //$NON-NLS-1$
	public static String EDIDataParser_Exception_EDI_Mapping_Cannot_Be_Empty;
	public static String EDIDataPathWizardPage_Page_Description_EDI_File_Select;
	public static String EDIDataPathWizardPage_Page_Title_EDI_File_Select;
	public static String EDIDataWizard_0;
	public static String EDIDataWizard_2;
	public static String EDIDataWizard_WizardTitle;
	public static String EDIMappingDataPathWizardPage_Button_Create_New_Reader;
	public static String EDIMappingDataPathWizardPage_Button_Create_Readers;
	public static String EDIMappingDataPathWizardPage_Button_Use_Existing_Reader;
	public static String EDIMappingDataPathWizardPage_EDI_Wizard_Description;
	public static String EDIMappingDataPathWizardPage_EDI_Wizard_Title;
	public static String EDIMappingDataPathWizardPage_Label_Encoding;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
