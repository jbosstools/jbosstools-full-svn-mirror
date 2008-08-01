package org.jboss.tools.portlet.ui;

/**
 * @author snjeza
 */
public interface IPortletUIConstants {

	static final String NEW_PORTLET_WIZARD_PAGE_TITLE = "Create Portlet";
	static final String ADD_PORTLET_WIZARD_PAGE_DESC = "Enter portlet deployment descriptor specific information.";
	
	static final String ADD_JBOSS_PORTLET_WIZARD_PAGE_TITLE = "Create Portlet";
	static final String ADD_JBOSS_JSF_PORTLET_WIZARD_PAGE_TITLE = "Create JBoss JSF Portlet";
	static final String ADD_JBOSS_PORTLET_WIZARD_PAGE_DESC = "Enter JBoss portlet specific information";

	static final String DISPLAY_NAME_LABEL = "Display name:";
	static final String TITLE_LABEL = "Title:";

	static final String QUALIFIED_PORTLET = "javax.portlet.Portlet"; //$NON-NLS-1$
	static final String QUALIFIED_GENERIC_PORTLET = "javax.portlet.GenericServlet"; //$NON-NLS-1$
	
	static final String QUALIFIED_PORTLET_EXCEPTION = "javax.portlet.PortletException"; //$NON-NLS-1$
	static final String QUALIFIED_IO_EXCEPTION = "java.io.IOException"; //$NON-NLS-1$
	static final String QUALIFIED_SECURITY_EXCEPTION = "javax.portlet.PortletSecurityException"; //$NON-NLS-1$
	static final String QUALIFIED_UNAVALIABLE_EXCEPTION = "javax.portlet.UnavailableException"; //$NON-NLS-1$
	static final String QUALIFIED_PRINTWRITER = "java.io.PrintWriter"; //$NON-NLS-1$
	
	static final String QUALIFIED_PORTLET_CONFIG = "javax.portlet.PortletConfig"; //$NON-NLS-1$
	static final String QUALIFIED_PORTLET_REQUEST = "javax.portlet.RenderRequest"; //$NON-NLS-1$
	static final String QUALIFIED_PORTLET_RESPONSE = "javax.portlet.RenderResponse"; //$NON-NLS-1$
	static final String QUALIFIED_ACTION_REQUEST = "javax.portlet.ActionRequest"; //$NON-NLS-1$
	static final String QUALIFIED_ACTION_RESPONSE = "javax.portlet.ActionResponse"; //$NON-NLS-1$
	
	
	static final String METHOD_INIT = "init"; //$NON-NLS-1$
	static final String METHOD_DESTROY = "destroy"; //$NON-NLS-1$
	static final String METHOD_GET_PORTLET_CONFIG = "getPortletConfig"; //$NON-NLS-1$
	static final String METHOD_DO_EDIT = "doEdit"; //$NON-NLS-1$
	static final String METHOD_DO_VIEW = "doView"; //$NON-NLS-1$
	static final String METHOD_DO_HELP = "doHelp"; //$NON-NLS-1$
	static final String METHOD_DO_DISPATCH = "doDispatch"; //$NON-NLS-1$
	static final String METHOD_PROCESS_ACTION = "doProcessAction"; //$NON-NLS-1$
	static final String METHOD_RENDER = "render"; //$NON-NLS-1$
	static final String METHOD_TO_STRING = "toString"; //$NON-NLS-1$
	
	static final String PORTLET_INIT_SIGNATURE = "(Ljavax/portlet/PortletConfig;)V"; //$NON-NLS-1$
	static final String DESTROY_SIGNATURE = "()V"; //$NON-NLS-1$
	static final String GET_PORTLET_CONFIG_SIGNATURE = "()Ljavax/portlet/PortletConfig;"; //$NON-NLS-1$
	static final String DO_VIEW_SIGNATURE = "(Ljavax/portlet/RenderRequest;Ljavax/portlet/RenderResponse;)V"; //$NON-NLS-1$
	static final String DO_EDIT_SIGNATURE = "(Ljavax/portlet/RenderRequest;Ljavax/portlet/RenderResponse;)V"; //$NON-NLS-1$
	static final String DO_HELP_SIGNATURE = "(Ljavax/portlet/RenderRequest;Ljavax/portlet/RenderResponse;)V"; //$NON-NLS-1$
	static final String DO_DISPATCH_SIGNATURE = "(Ljavax/portlet/RenderRequest;Ljavax/portlet/RenderResponse;)V"; //$NON-NLS-1$
	static final String RENDER_SIGNATURE = "(Ljavax/portlet/RenderRequest;Ljavax/portlet/RenderResponse;)V"; //$NON-NLS-1$
	static final String PROCESS_ACTION_SIGNATURE = "(Ljavax/portlet/ActionRequest;Ljavax/portlet/ActionResponse;)V"; //$NON-NLS-1$
	
	static final String ADD_PORTLET_LABEL = "Create Portlet Instance";
	static final String INSTANCE_NAME_LABEL = "Instance Name:";
	static final String WINDOW_NAME_LABEL = "Window Name:";
	static final String PAGE_NAME_LABEL = "Page Name:";
	static final String PARENT_PORTAL_LABEL = "Parent Reference:";
	static final String PAGE_REGION_LABEL = "Region:";
	static final String PORTLET_HEIGHT_LABEL = "Height:";
	static final String IF_EXISTS_LABEL = "If Exists:";
	static final String NEW_JBOSS_JSF_PORTLET_WIZARD_PAGE_DESC = "Specify class file information";
	static final String JBOSS_JSF_PORTLET_CLASS = "javax.portlet.faces.GenericFacesPortlet";
	static final String JBOSS_JSF_PORTLET_NAME = "riPortlet";
	static final String JBOSS_JSF_DISPLAY_PORTLET_NAME = "JBoss JSF Portlet";
	static final String JBOSS_JSF_PORTLET_TITLE = "JBoss JSF Portlet";
	static final String JBOSS_APP_LABEL = "JBoss Application Name:";
	static final String ADD_JBOSS_APP_LABEL = "Create JBoss Application";
	static final String ADD_JBOSS_PORTLET_LABEL = "Add the jboss-portlet.xml file";
	static final String COPY_JSF_TEMPLATES_LABEL = "Copy JSF Templates";
	
	
}
