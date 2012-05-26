package org.jboss.tools.profiler.internal.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.profiler.internal.ui.messages"; //$NON-NLS-1$
	public static String BaseBlock_cannot_open_directory;
	public static String BaseBlock_cannot_open_file;
	public static String BaseBlock_choose_a_directory;
	public static String BaseBlock_choose_location_relative_to_ws;
	public static String BaseBlock_directory_not_found;
	public static String BaseBlock_file_not_found;
	public static String BaseBlock_filesystem;
	public static String BaseBlock_open_directory;
	public static String BaseBlock_open_file;
	public static String BaseBlock_select_directory;
	public static String BaseBlock_select_file;
	public static String BaseBlock_select_properties_file;
	public static String BaseBlock_the_is_not_specified;
	public static String BaseBlock_variables;
	public static String BaseBlock_workspace;
	public static String JBossProfilerUiPlugin_no_message;
	public static String LaunchUtils_probem_finding_embedded_runtime;
	public static String LaunchUtils_problem_finding_embedded_agent_runtime;
	public static String LaunchUtils_problem_finding_embedded_client;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
