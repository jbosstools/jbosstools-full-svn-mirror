package org.jboss.tools.smooks.graphical.editors;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.graphical.editors.messages"; //$NON-NLS-1$
	public static String SmooksJavaMappingGraphicalEditor_BeanIdEmptyErrormessage;
	public static String SmooksJavaMappingGraphicalEditor_NodeMustLinkWithJavaBean;
	public static String SmooksJavaMappingGraphicalEditor_NodeMustLinkWithSource;
	public static String SmooksJavaMappingGraphicalEditor_NullLabel;
	public static String SmooksProcessGraphicalEditor_AddTaskActionText;
	public static String SmooksProcessGraphicalEditor_FormText;
	public static String SmooksProcessGraphicalEditor_TaskConfigurationSectionTitle;
	public static String SmooksProcessGraphicalEditor_TasksMapSectionTitle;
	public static String TaskTypeManager_ApplyTemplateTaskLabel;
	public static String TaskTypeManager_ApplyXSLTemplateTaskLabel;
	public static String TaskTypeManager_Input;
	public static String TaskTypeManager_InputTaskLabel;
	public static String TaskTypeManager_JavaMappingTaskLabel;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
