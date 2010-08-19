package org.jboss.tools.smooks.configuration.editors.file;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	
	public static String FileSelectionPageComponent_Browse_FileSys_Button;
	public static String FileSelectionPageComponent_Browse_Workspace_Button;

	static {
		// initialize resource bundle
		NLS.initializeMessages(Messages.class.getPackage().getName() + ".messages", Messages.class); //$NON-NLS-1$
	}

	private Messages() {
	}
}
