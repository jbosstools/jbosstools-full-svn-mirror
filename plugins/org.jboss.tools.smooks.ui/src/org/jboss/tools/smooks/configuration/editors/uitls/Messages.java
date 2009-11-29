package org.jboss.tools.smooks.configuration.editors.uitls;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.configuration.editors.uitls.messages"; //$NON-NLS-1$
	public static String JavaTypeFieldDialog_CantOpenErrorMessage;
	public static String JavaTypeFieldDialog_CantOpenErrorTitle;
	public static String JavaTypeFieldDialog_SearchDialogTitle;
	public static String SelectorContentProposalProvider_3;
	public static String SmooksUIUtils_AltTooltip;
	public static String SmooksUIUtils_BrowseButtonLabel;
	public static String SmooksUIUtils_CantFindTypeErrorDialogTitle;
	public static String SmooksUIUtils_CtrlSpaceTooltip;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
