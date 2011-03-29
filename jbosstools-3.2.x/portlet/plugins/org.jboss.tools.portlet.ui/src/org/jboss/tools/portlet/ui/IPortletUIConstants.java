package org.jboss.tools.portlet.ui;

/**
 * @author snjeza
 */
public interface IPortletUIConstants {

	static final String NEW_PORTLET_WIZARD_PAGE_TITLE = Messages.IPortletUIConstants_Create_Portlet;
	static final String ADD_PORTLET_WIZARD_PAGE_DESC = Messages.IPortletUIConstants_Enter_portlet_deployment_descriptor_specific_information;
	
	static final String ADD_JBOSS_PORTLET_WIZARD_PAGE_TITLE = Messages.IPortletUIConstants_Create_Portlet;
	static final String ADD_JBOSS_JSF_PORTLET_WIZARD_PAGE_TITLE = Messages.IPortletUIConstants_Create_JBoss_JSF_Portlet;
	static final String ADD_JBOSS_PORTLET_WIZARD_PAGE_DESC = Messages.IPortletUIConstants_Enter_JBoss_portlet_specific_information;

	static final String DISPLAY_NAME_LABEL = Messages.IPortletUIConstants_Display_name;
	static final String TITLE_LABEL = Messages.IPortletUIConstants_Title;

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
	
	static final String ADD_PORTLET_LABEL = Messages.IPortletUIConstants_Create_portlet_instance;
	static final String INSTANCE_NAME_LABEL = Messages.IPortletUIConstants_Inastance_name;
	static final String WINDOW_NAME_LABEL = Messages.IPortletUIConstants_Window_name;
	static final String PAGE_NAME_LABEL = Messages.IPortletUIConstants_Page_name;
	static final String PARENT_PORTAL_LABEL = Messages.IPortletUIConstants_Parent_Reference;
	static final String PAGE_REGION_LABEL = Messages.IPortletUIConstants_Region;
	static final String PORTLET_HEIGHT_LABEL = Messages.IPortletUIConstants_Height;
	static final String INITIAL_WINDOW_STATE_LABEL = Messages.IPortletUIConstants_Initial_Window_State;
	static final String IF_EXISTS_LABEL = Messages.IPortletUIConstants_If_Exists;
	static final String NEW_JBOSS_JSF_PORTLET_WIZARD_PAGE_DESC = Messages.IPortletUIConstants_Specify_class_file_information;
	static final String JBOSS_JSF_PORTLET_CLASS = "javax.portlet.faces.GenericFacesPortlet"; //$NON-NLS-1$
	static final String JBOSS_JSF_PORTLET_NAME = "riPortlet"; //$NON-NLS-1$
	static final String JBOSS_SEAM_PORTLET_NAME = "seamPortlet"; //$NON-NLS-1$
	static final String JBOSS_JSF_DISPLAY_PORTLET_NAME = Messages.IPortletUIConstants_JBoss_JSF_Portlet;
	static final String JBOSS_JSF_PORTLET_TITLE = Messages.IPortletUIConstants_JBoss_JSF_Portlet;
	static final String JBOSS_SEAM_DISPLAY_PORTLET_NAME = Messages.IPortletUIConstants_JBoss_Seam_Portlet;
	static final String JBOSS_SEAM_PORTLET_TITLE = Messages.IPortletUIConstants_JBoss_Seam_Portlet;
	static final String JBOSS_APP_LABEL = Messages.IPortletUIConstants_JBoss_Application_Name;
	static final String ADD_JBOSS_APP_LABEL = Messages.IPortletUIConstants_Create_JBoss_Application;
	static final String ADD_JBOSS_PORTLET_LABEL = Messages.IPortletUIConstants_Add_the_jboss_portlet_xml_file;
	static final String COPY_JSF_TEMPLATES_LABEL = Messages.IPortletUIConstants_Copy_JSF_Templates;
	static final String CONFIGURE_GATEIN_PARAMETERS_LABEL = Messages.IPortletUIConstants_Configure_GateIn_parameters;
	
	
}
