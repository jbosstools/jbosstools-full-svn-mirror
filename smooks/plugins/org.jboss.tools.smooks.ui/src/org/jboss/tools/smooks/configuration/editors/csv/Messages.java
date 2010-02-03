package org.jboss.tools.smooks.configuration.editors.csv;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.editors.csv.messages"; //$NON-NLS-1$
	public static String CSVDataPathWizardPage_CantFindFileErrorMessage;
	public static String CSVDataPathWizardPage_ErrorPathErrorMessage;
	public static String CSVDataPathWizardPage_WizardDes;
	public static String CSVDataPathWizardPage_WizardTitle;
	public static String CSVInputDataWizard_ExtensionName;
	public static String CSVInputDataWizard_WizardPageText;
	public static String CSVInputDataWizard_WizardTitle;
	public static String CsvReaderUICreator_AddButtonLabel;
	public static String CsvReaderUICreator_FieldsLabel;
	public static String CsvReaderUICreator_RemoveButtonLabel;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
