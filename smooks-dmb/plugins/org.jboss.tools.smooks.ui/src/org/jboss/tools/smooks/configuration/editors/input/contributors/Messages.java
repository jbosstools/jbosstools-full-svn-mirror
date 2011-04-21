package org.jboss.tools.smooks.configuration.editors.input.contributors;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	
	public static String ConfigurationContributorFactory_Browse_Button;
	public static String XSDConfigurationContributorFactory_XML_Schema_File;
	public static String XSDConfigurationContributorFactory_Root_Element_Name;
	public static String JavaConfigurationContributorFactory_Class_Name;

	static {
		// initialize resource bundle
		NLS.initializeMessages(Messages.class.getPackage().getName() + ".messages", Messages.class); //$NON-NLS-1$
	}

	private Messages() {
	}
}
