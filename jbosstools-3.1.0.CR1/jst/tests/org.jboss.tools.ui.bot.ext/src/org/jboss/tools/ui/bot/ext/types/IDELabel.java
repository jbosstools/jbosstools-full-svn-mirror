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
		public static final String JSP_FILE = "JSP File";
		public static final String XHTML_FILE = "XHTML File";
		public static final String CLOSE = "Close";
		public static final String OPEN = "Open";
		public static final String RENAME = "Rename";
		public static final String PROPERTIES = "Properties";
		public static final String HELP = "Help";
		public static final String ABOUT_JBOSS_DEVELOPER_STUDIO = "About JBoss Developer Studio";
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
	}

	public class Shell {
		public static final String NEW_JAVA_PROJECT = "New Java Project";
		public static final String NEW_JAVA_CLASS = "New Java Class";
		public static final String NEW_HIBERNATE_MAPPING_FILE = "New Hibernate XML Mapping file (hbm.xml)";
		public static final String NEW = "New";
		public static final String NEW_JSP_FILE = "New File JSP";
		public static final String NEW_XHTML_FILE = "New File XHTML";
		public static final String SAVE_RESOURCE = "Save Resource";
		public static final String RENAME_RESOURCE = "Rename Resource";
		public static final String PROPERTIES = "Properties";
	}

	public class EntityGroup {
		public static final String HIBERNATE = "Hibernate";
		public static final String JAVA = "Java";
		public static final String SEAM = "Seam";
	}
	
	public class EntityLabel {
		public static final String HIBERNATE_MAPPING_FILE = "Hibernate XML Mapping file (hbm.xml)";
		public static final String JAVA_CLASS = "Class";
		public static final String JAVA_PROJECT =  "Java Project";
		public static final String SEAM_PROJECT = "Seam Web Project";
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
	}
	
	public class ViewGroup {
		public static final String GENERAL = "General";
		public static final String JAVA = "Java";
	}

	public class SelectPerspectiveDialog {
		public static final String JAVA = "Java";
		public static final String HIBERNATE = "Hibernate";

	}
	
  public class WebProjectsTree {
    public static final String WEB_CONTENT = "WebContent";
    public static final String TAG_LIBRARIES = "Tag Libraries";
    public static final String RESOURCE_BUNDLES = "Resource Bundles";
    public static final String CONFIGURATION = "Configuration";
    public static final String BEANS = "Beans";
    public static final String TILES = "Tiles";
    public static final String WEB_XML = "web.xml";
    public static final String CONTEXT_PARAMS = "Context Params";
    public static final String JAVAX_FACES_CONFIG_FILES = "javax.faces.CONFIG_FILES";
  }

  public class NewJSPFileDialog {
    public static final String NAME = "Name*";
    public static final String TEMPLATE = "Template";
    public static final String TEMPLATE_JSF_BASE_PAGE = "JSFBasePage";
  }
  
  public class NewXHTMLFileDialog {
    public static final String NAME = "Name*";
    public static final String TEMPLATE = "Template";
    public static final String TEMPLATE_FACELET_FORM_XHTML = "FaceletForm.xhtml";
  }
  public class RenameResourceDialog {
    public static final String NEW_NAME = "New name:";
  }
  public class PropertiesDialog {
    public static final String PARAM_NAME = "Param-Name";
    public static final String PARAM_VALUE = "Param-Value";
  }
}
