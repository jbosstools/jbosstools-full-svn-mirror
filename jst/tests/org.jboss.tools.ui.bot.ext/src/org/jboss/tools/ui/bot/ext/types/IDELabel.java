 /*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.ui.bot.ext.types;

/**
 * Base label constants for all widgets. Naming convention is (except buttons
 * and menus) based on Eclipse platform class names of each part (e.g.
 * NewJavaProjectWizardPageOne)
 * 
 * @author jpeterka 
 */
public class IDELabel {
	public class Menu {
		public static final String FILE = "File";
		public static final String NEW = "New";
		public static final String PROJECT = "Project";
		public static final String OTHER = "Other...";
		public static final String WINDOW = "Window";
		public static final String SHOW_VIEW = "Show View";
		public static final String OPEN_PERSPECTIVE = "Open Perspective";
		public static final String OPEN_WITH =  "Open With";
		public static final String TEXT_EDITOR = "Text Editor";
		public static final String EDIT = "Edit";
		public static final String SELECT_ALL = "Select All";
		public static final String CLOSE = "Close";
		public static final String OPEN = "Open";
		public static final String RENAME = "Rename";
		public static final String JSP_FILE = "JSP File";
		public static final String PROPERTIES = "Properties";
		public static final String XHTML_FILE = "XHTML File";
		public static final String HELP = "Help";
		public static final String ABOUT_JBOSS_DEVELOPER_STUDIO = "About JBoss Developer Studio";
		public static final String HIBERNATE_CODE_GENERATION = "Hibernate Code Generation...";
		public static final String HIBERNATE_CODE_GENERATION_CONF = "Hibernate Code Generation Configurations...";
		public static final String REMOVE = "Remove";
		public static final String IMPORT = "Import...";
		public static final String RUN_AS = "Run As";
		public static final String WEB_PROJECT_JBT_JSF = "JBoss Tools JSF";
		public static final String PACKAGE_EXPLORER_JBT = "JBoss Tools";
		public static final String PACKAGE_EXPLORER_CONFIGURE = "Configure";
		public static final String ADD_JSF_CAPABILITIES = "Add JSF Capabilities...";
		public static final String CLOSE_PROJECT = "Close Project";
		public static final String OPEN_PROJECT = "Open Project";
		public static final String DELETE = "Delete";
		public static final String JBT_REMOVE_JSF_CAPABILITIES = "Remove JSF Capabilities";
		public static final String START = "Start";
		public static final String STOP = "Stop";
		public static final String STRUTS_PROJECT = "Struts Project";
		public static final String PREFERENCES = "Preferences";
    public static final String JBT_REMOVE_STRUTS_CAPABILITIES = "Remove Struts Capabilities";
    public static final String ADD_STRUTS_CAPABILITIES = "Add Struts Capabilities...";
    public static final String WEB_PROJECT_JBT_STRUTS = "JBoss Tools Struts";
    public static final String RUN = "Run";
    public static final String RUN_ON_SERVER = "Run on Server";
		
	}

	public class Button {
		public static final String NEXT = "Next >";
		public static final String BACK = "< Back";
		public static final String CANCEL = "Cancel";
		public static final String FINISH = "Finish";
		public static final String OK = "OK";
		public static final String YES = "Yes";
		public static final String NO = "No";
		public static final String CLOSE = "Close";
		public static final String RUN = "Run";
		public static final String APPLY = "Apply";
		public static final String ADD = "Add...";
		public static final String NEW = "New...";
		public static final String CONTINUE = "Continue";
	}

	public class Shell {
		public static final String NEW_JAVA_PROJECT = "New Java Project";
		public static final String NEW_JAVA_CLASS = "New Java Class";
		// JBT
		// public static final String NEW_HIBERNATE_MAPPING_FILE = "New Hibernate XML Mapping file (hbm.xml)";
		// JBDS
		public static final String NEW_HIBERNATE_MAPPING_FILE = "Create Hibernate XML Mapping file (hbm.xml)";		
		public static final String NEW = "New";
		public static final String SAVE_RESOURCE = "Save Resource";
		public static final String RENAME_RESOURCE = "Rename Resource";
		public static final String NEW_JSP_FILE = "New File JSP";
		public static final String PROPERTIES = "Properties";
		public static final String NEW_XHTML_FILE = "New File XHTML";
		public static final String IMPORT_JSF_PROJECT = "Import JSF Project";
		public static final String IMPORT = "Import";
		public static final String DELETE_SERVER = "Delete Server";
		public static final String NEW_STRUTS_PROJECT = "New Struts Project";
	  public static final String PREFERENCES = "Preferences";
	  public static final String NEW_SERVER_RUNTIME_ENVIRONMENT = "New Server Runtime Environment";
	  public static final String OPEN_ASSOCIATED_PERSPECTIVE = "Open Associated Perspective?";
	  public static final String DELETE_RESOURCES = "Delete Resources";
	  public static final String IMPORT_STRUTS_PROJECT = "Import Struts Project";
	  public static final String UNSUPPORTED_CONTENT_TYPE = "Unsupported Content Type";
	  public static final String NEW_SERVER = "New Server";
	  public static final String RUN_ON_SERVER = "Run On Server";
	  public static final String WARNING = "Warning";
	}

	public class EntityGroup {
		public static final String HIBERNATE = "Hibernate";
		public static final String JAVA = "Java";
		public static final String SEAM = "Seam";
		public static final String STRUTS = "Struts";
		public static final String JBOSS_TOOLS_WEB = "JBoss Tools Web";
		public static final String JPA = "JPA";
	}
	
	public class EntityLabel {
		public static final String HIBERNATE_MAPPING_FILE = "Hibernate XML Mapping file (hbm.xml)";
		public static final String HIBERNATE_REVERSE_FILE = "Hibernate Reverse Engineering File(reveng.xml)";
		public static final String HIBERNATE_CONSOLE = "Hibernate Console Configuration";
		public static final String JAVA_CLASS = "Class";
		public static final String JAVA_PROJECT =  "Java Project";
		public static final String SEAM_PROJECT = "Seam Web Project";
		public static final String HIBERNATE_CONFIGURATION_FILE = "Hibernate Configuration File (cfg.xml)";
		public static final String STRUTS_PROJECT = "Struts Project";
		public static final String JPA_PROJECT = "JPA Project";
	}

	public class JavaProjectWizard {
		public static final String PROJECT_NAME = "Project name:";
	}

	public class NewClassCreationWizard {
		public static final String CLASS_NAME = "Name:";
		public static final String PACKAGE_NAME = "Package:";
	}

	public class ShowViewDialog {
		public static final String JAVA_GROUP = "Java";
		public static final String PROJECT_EXPLORER = "Project Explorer";

	}

	public class View {
		public static final String WELCOME = "Welcome";
		public static final String PROJECT_EXPLORER = "Project Explorer";
		public static final String PACKAGE_EXPLORER = "Package Explorer";
		public static final String DATA_SOURCE_EXPLORER = "Data Source Explorer";
		public static final String SERVERS = "Servers";
		public static final String WEB_PROJECTS = "Web Projects";
	}
	
	public class ViewGroup {
		public static final String GENERAL = "General";
		public static final String JAVA = "Java";
		public static final String DATA_MANAGEMENT = "Data Management";
		public static final String SERVER = "Server";
		public static final String JBOSS_TOOLS_WEB = "JBoss Tools Web";
	}

	public class SelectPerspectiveDialog {
		public static final String JAVA = "Java";
		public static final String HIBERNATE = "Hibernate";
		public static final String SEAM = "Seam";
		public static final String WEB_DEVELOPMENT = "Web Development";
		public static final String DB_DEVELOPMENT = "Database Development";
		public static final String JPA = "JPA";
	}
	/**
	 * Hibernate Console Wizard (ConsoleConfigurationCreationWizard) Labels (
	 * @author jpeterka
	 *
	 */
	public class HBConsoleWizard {
		public static final String MAIN_TAB = "Main";
		public static final String OPTIONS_TAB = "Options";
		public static final String CLASSPATH_TAB = "Classpath";
		public static final String MAPPINGS_TAB = "Mappings";
		public static final String COMMON_TAB = "Common";
		public static final String PROJECT_GROUP = "Project:";
		public static final String CONFIGURATION_FILE_GROUP = "Configuration file:";
		public static final String SETUP_BUTTON = "Setup...";
		public static final String CREATE_NEW_BUTTON = "Create new...";
		public static final String USE_EXISTING_BUTTON = "Use existing...";
		public static final String DATABASE_DIALECT = "Database dialect:";
		public static final String DRIVER_CLASS = "Driver class:";
		public static final String CONNECTION_URL = "Connection URL:";
		public static final String USERNAME = "Username:";
		public static final String CREATE_CONSOLE_CONFIGURATION = "Create a console configuration";
	}
	
	public class HBLaunchConfigurationDialog {

		public static final String MAIN_TAB = "Main";
		public static final String EXPORTERS_TAB = "Exporters";
		public static final String REFRESH_TAB = "Refresh";
		public static final String COMMON_TAB = "Common";
		
	}
	
	public class HBConfigurationWizard {
		public static final String FILE_NAME = "File name:";
	}

	public static class RenameResourceDialog {

		public static final String NEW_NAME = "New name:";
		
	}
	
	public static class WebProjectsTree {

		public static final String WEB_CONTENT = "WebContent";
		public static final String CONFIGURATION = "Configuration";
		public static final String WEB_XML = "web.xml";
		public static final String CONTEXT_PARAMS = "Context Params";
		public static final String JAVAX_FACES_CONFIG_FILES = "javax.faces.CONFIG_FILES";
		public static final String DEFAULT = "default";
		public static final String SERVLETS = "Servlets";
		public static final String ACTION_STRUTS = "action:org.apache.struts.action.ActionServlet";
		public static final String CONFIG = "config";
		public static final String TAG_LIBRARIES = "Tag Libraries";
		
	}
	
	public static class NewJSPFileDialog {

		public static final String NAME = "Name*";
		public static final String TEMPLATE = "Template";
		public static final String TEMPLATE_JSF_BASE_PAGE = "JSFBasePage";
		
	}
	
	public static class PropertiesDialog {

		public static final String PARAM_VALUE = "Param-Value";
		
	}

	public static final class NewXHTMLFileDialog {

		public static final String NAME = "Name*";
		public static final String TEMPLATE = "Template";
		public static final String TEMPLATE_FACELET_FORM_XHTML = "FaceletForm.xhtml";
		
	}
	
	public static final class ServerName {

	  public static final String JBOSS_EAP_4_3_RUNTIME_SERVER = "JBoss EAP 4.3 Runtime Server";
	  // Server with this Label is created during JBDS installation for bundled EAP
	  public static final String JBOSS_EAP = "jboss-eap";
	    
	}
	 
   public static final class ServerRuntimeName {

     public static final String JBOSS_EAP_4_3 = "JBoss EAP 4.3 Runtime";
     // Server Runtime with this Label is created during JBDS installation for bundled EAP
     public static final String JBOSS_EAP = "jboss-eap Runtime";

   }
	
   public static final class ServerJobName {

     public static final String STARTING_JBOSS_EAP_43_RUNTIME = "Starting JBoss EAP 4.3 Runtime Server";
     public static final String STOPPING_JBOSS_EAP_43_RUNTIME = "Stoppig JBoss EAP 4.3 Runtime Server";
     public static final String STARTING_JBOSS_EAP = "Starting jboss-eap";
     public static final String STOPPING_JBOSS_EAP = "Stopping jboss-eap";
     
   }
   
   public static class ImportJSFProjectDialog {

     public static final String RUNTIME = "Runtime*";
     public static final String CHOICE_LIST_IS_EMPTY = "Choice list is empty.";
     
   }
   
   public class NewStrutsProjectDialog{
     
     public static final String NAME = "Project Name*";
     public static final String TEMPLATE = "Template*";
     public static final String TEMPLATE_KICK_START = "KickStart";
     
   }
   
   public static class PreferencesDialog {

     public static final String SERVER_GROUP = "Server";
     public static final String RUNTIME_ENVIRONMENTS = "Runtime Environments";
     
   }
   
   public static class JBossServerRuntimeDialog {

     public static final String NAME = "Name";
     public static final String HOME_DIRECTORY = "Home Directory";
     
   }
   public static final class ServerGroup {

     public static final String JBOSS_EAP_4_3 = "JBoss Enterprise Middleware";
       
   }
   public static final class ServerRuntimeType {

     public static final String JBOSS_EAP_4_3 = "JBoss Enterprise Application Platform 4.3 Runtime";
       
   }
   public static final class ServerType {

     public static final String JBOSS_EAP_4_3 = "JBoss Enterprise Application Platform 4.3";
       
   }
}
