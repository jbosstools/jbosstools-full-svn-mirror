package org.jboss.tools.smooks.configuration.editors.xsl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.editors.xsl.messages"; //$NON-NLS-1$
	public static String TemplateUICreator_External_Template_File;
	public static String TemplateUICreator_Inline_Template;
	public static String XslUICreator_Apply_On_Element;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
