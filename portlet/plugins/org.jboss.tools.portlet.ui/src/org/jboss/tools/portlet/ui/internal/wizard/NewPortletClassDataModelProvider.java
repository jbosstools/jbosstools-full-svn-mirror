package org.jboss.tools.portlet.ui.internal.wizard;



import static org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties.SOURCE_FOLDER;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jst.j2ee.internal.common.J2EECommonMessages;
import org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties;
import org.eclipse.jst.j2ee.internal.common.operations.NewJavaClassDataModelProvider;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.j2ee.internal.web.operations.INewServletClassDataModelProperties;
import org.eclipse.jst.j2ee.internal.web.operations.NewServletClassDataModelProvider;
import org.eclipse.jst.j2ee.internal.web.operations.NewServletClassOperation;
import org.eclipse.jst.j2ee.internal.web.operations.NewWebClassDataModelProvider;
import org.eclipse.jst.j2ee.internal.web.operations.WebMessages;
import org.eclipse.jst.j2ee.web.validation.UrlPattern;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelProvider;
import org.eclipse.wst.common.frameworks.internal.plugin.WTPCommonPlugin;
import org.jboss.tools.portlet.operations.AddPortletOperation;
import org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties;

/**
 * 
 * @see NewServletClassDataModelProvider
 * 
 */
public class NewPortletClassDataModelProvider extends
		NewWebClassDataModelProvider implements INewPortletClassDataModelProperties{

	/**
	 * The fully qualified default portlet superclass: GenericPortlet.
	 */
	private final static String PORTLET_SUPERCLASS = "javax.portlet.GenericPortlet";
	
	/**
	 * String array of the default, minimum required fully qualified Portlet
	 * interfaces
	 */
	private final static String QUALIFIED_PORTLET = "javax.portlet.Portlet";
	private final static String[] PORTLET_INTERFACES = { QUALIFIED_PORTLET }; 

	private final static String ANNOTATED_TEMPLATE_DEFAULT = "portlet.javajet"; //$NON-NLS-1$

	private final static String NON_ANNOTATED_TEMPLATE_DEFAULT = "portlet.javajet"; //$NON-NLS-1$

	private boolean isJSFPortlet;

	private boolean isSeamPortlet;

	public NewPortletClassDataModelProvider(boolean isJSFPortlet, boolean isSeamPortlet) {
		this.isJSFPortlet = isJSFPortlet;
		this.isSeamPortlet = isSeamPortlet;
	}

	public NewPortletClassDataModelProvider() {
		this(false,false);
	}

	/**
	 * Subclasses may extend this method to provide their own default operation
	 * for this data model provider. This implementation uses the
	 * AddPortletOperation to drive the portlet creation. It will not return
	 * null.
	 * 
	 * @see IDataModel#getDefaultOperation()
	 * 
	 * @return IDataModelOperation AddPortletOperation
	 */
	@Override
	public IDataModelOperation getDefaultOperation() {
		return new AddPortletOperation(model);
	}

	/**
	 * Subclasses may extend this method to add their own data model's
	 * properties as valid base properties.
	 * 
	 * @see org.eclipse.wst.common.frameworks.datamodel.IDataModelProvider#getPropertyNames()
	 */
	@Override
	public Set getPropertyNames() {
		// Add portlet specific properties defined in this data model
		Set propertyNames = super.getPropertyNames();
		
		propertyNames.add(INIT);
		propertyNames.add(DESTROY);
		propertyNames.add(GET_PORTLET_CONFIG);
		propertyNames.add(DO_DISPATCH);
		propertyNames.add(DO_EDIT);
		propertyNames.add(DO_VIEW);
		propertyNames.add(DO_HELP);
		propertyNames.add(PROCESS_ACTION);
		propertyNames.add(RENDER);
		propertyNames.add(TO_STRING);
		propertyNames.add(INIT_PARAM);
		propertyNames.add(NAME);
		propertyNames.add(TITLE);
		propertyNames.add(VIEW_MODE);
		propertyNames.add(EDIT_MODE);
		propertyNames.add(HELP_MODE);
		propertyNames.add(NON_ANNOTATED_TEMPLATE_FILE);
		propertyNames.add(TEMPLATE_FILE);
		propertyNames.add(INSTANCE_NAME);
		propertyNames.add(WINDOW_NAME);
		propertyNames.add(PAGE_NAME);
		propertyNames.add(PARENT_PORTAL);
		propertyNames.add(PAGE_REGION);
		propertyNames.add(PORTLET_HEIGHT);
		propertyNames.add(INITIAL_WINDOW_STATE);
		propertyNames.add(IF_EXISTS);
		propertyNames.add(ADD_PORTLET);
		propertyNames.add(ADD_JBOSS_APP);
		propertyNames.add(ADD_JBOSS_PORTLET);
		propertyNames.add(JBOSS_APP);
		propertyNames.add(IS_JSF_PORTLET);
		propertyNames.add(IS_SEAM_PORTLET);
		propertyNames.add(COPY_JSF_TEMPLATES);
		
		return propertyNames;
	}
	
	@Override
	public boolean isPropertyEnabled(String propertyName) {
		if (ABSTRACT_METHODS.equals(propertyName)) {
			return true;
		} else if (INIT.equals(propertyName) ||
				DESTROY.equals(propertyName) ||
				GET_PORTLET_CONFIG.equals(propertyName) ||
				DO_DISPATCH.equals(propertyName) || 
				DO_EDIT.equals(propertyName) ||
				DO_VIEW.equals(propertyName) ||
				DO_HELP.equals(propertyName) ||
				PROCESS_ACTION.equals(propertyName) ||
				RENDER.equals(propertyName) ||
				INIT_PARAM.equals(propertyName)) {
			boolean inherit = model.getBooleanProperty(ABSTRACT_METHODS);
			return inherit;
		}
		
		// Otherwise return super implementation
		return super.isPropertyEnabled(propertyName);
	}

	@Override
	public Object getDefaultProperty(String propertyName) {
		
		if (propertyName.equals(IS_JSF_PORTLET)) {
			if (isJSFPortlet)
				return Boolean.TRUE;
			return Boolean.FALSE;
		}
		if (propertyName.equals(IS_SEAM_PORTLET)) {
			if (isSeamPortlet)
				return Boolean.TRUE;
			return Boolean.FALSE;
		}
		if (propertyName.equals(COPY_JSF_TEMPLATES)) {
			return Boolean.FALSE;
		}
		if (propertyName.equals(ADD_PORTLET)) {
			return Boolean.TRUE;
		}
		if (propertyName.equals(ADD_JBOSS_APP)) {
			return Boolean.TRUE;
		}
		if (propertyName.equals(ADD_JBOSS_PORTLET)) {
			return Boolean.TRUE;
		}
		if (propertyName.equals(DO_VIEW)) {
			return Boolean.TRUE;
		}
		if (isJSFPortlet) {
			if (propertyName.equals(EDIT_MODE) || propertyName.equals(HELP_MODE))
					return Boolean.TRUE;
		}
		if (propertyName.equals(DO_EDIT) ||	propertyName.equals(DO_HELP) || propertyName.equals(INIT) || propertyName.equals(DESTROY) || 
			propertyName.equals(GET_PORTLET_CONFIG)) {
				return Boolean.FALSE;
		}
		
		if (propertyName.equals(DISPLAY_NAME)) {
			String className = getStringProperty(CLASS_NAME);
			className = Signature.getSimpleName(className);
			return className;
		}
		if (propertyName.equals(NAME)) {
			String className = getStringProperty(CLASS_NAME);
			className = Signature.getSimpleName(className);
			return className;
		}
		if (propertyName.equals(TITLE)) {
			String className = getStringProperty(CLASS_NAME);
			className = Signature.getSimpleName(className);
			return className;
		}
		
		if (propertyName.equals(VIEW_MODE)) {
			return Boolean.TRUE;
		}
		if (propertyName.equals(EDIT_MODE)) {
			return Boolean.FALSE;
		}
		if (propertyName.equals(HELP_MODE)) {
			return Boolean.FALSE;
		}
		if (propertyName.equals(INTERFACES))
			return getPortletInterfaces();
		if (propertyName.equals(SUPERCLASS))
			return PORTLET_SUPERCLASS;
		if (propertyName.equals(TEMPLATE_FILE))
			return ANNOTATED_TEMPLATE_DEFAULT;
		if (propertyName.equals(NON_ANNOTATED_TEMPLATE_FILE))
			return NON_ANNOTATED_TEMPLATE_DEFAULT;
		if (propertyName.equals(CONSTRUCTOR))
			return Boolean.FALSE;
		
		if (propertyName.equals(INSTANCE_NAME)) {
			return getPortletPrefix() + "Instance";
		}
		if (propertyName.equals(WINDOW_NAME)) {
			return getPortletPrefix() + "Window";
		}
		if (propertyName.equals(PAGE_NAME)) {
			if (isSeamPortlet) {
				return "SeamPortlet";
			}
			if (isJSFPortlet) {
				return "JSFPortlet";
			}
			return "";
		}
		if (propertyName.equals(PORTLET_HEIGHT)) {
			return "1";
		}
		if (propertyName.equals(INITIAL_WINDOW_STATE)) {
			return "maximized";
		}
		if (propertyName.equals(JBOSS_APP)) {
			if (isSeamPortlet) {
				return "seamPortletApp";
			}
			if (isJSFPortlet) {
				return "riPortletApp";
			}
			return "portletApp";
		}
		if (propertyName.equals(PAGE_REGION)) {
			return "center";
		}
		if (propertyName.equals(PARENT_PORTAL)) {
			if (isSeamPortlet || isJSFPortlet) {
				return "default";
			}
			return "default.default";
		}
		if (propertyName.equals(IF_EXISTS)) {
			return "overwrite";
		}
		if (propertyName.equals(INewJavaClassDataModelProperties.JAVA_SOURCE_FOLDER)) {
			return getSourceFolder();
		}
		// Otherwise check super for default value for property
		return super.getDefaultProperty(propertyName);
	}

	private Object getSourceFolder() {
		Object result = getJavaSourceFolder();
		if (result != null) {
			return result;
		}
		return getDefaultJavaSourceFolder();
	}

	@Override
	protected IFolder getDefaultJavaSourceFolder() {
		IProject project = getTargetProject();
		if (project == null)
			return null;
		IPackageFragmentRoot[] sources = J2EEProjectUtilities.getSourceContainers(project);
		// Try and return the first source folder
		if (sources.length > 0) {
			try {
				return (IFolder) sources[0].getCorrespondingResource();
			} catch (Exception e) {
				return null;
			}
		}
		IJavaProject javaProject = JavaCore.create(project);
		if (javaProject == null) {
			return null;
		}
		try {
			IClasspathEntry[] entries = javaProject.getResolvedClasspath(false);
			for (int i = 0; i < entries.length; i++) {
				IClasspathEntry entry = entries[i];
				if (entry.getContentKind() == IPackageFragmentRoot.K_SOURCE) {
					IPath path = entry.getPath();
					IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
					if (resource instanceof IFolder) {
						return (IFolder) resource;
					}
				}
			}
		} catch (JavaModelException e) {
			// ignore
		}
		return null;
	}

	private String getPortletPrefix() {
		String prefix = null;
		if (isSeamPortlet) {
			prefix = "SeamPortlet";
		} else if (isJSFPortlet) {
			prefix = "JSFPortlet";
		} else {
			prefix = (String) getDefaultProperty(NAME);
		}
		return prefix;
	}

	@Override
	public IStatus validate(String propertyName) {
		// Validate super class
		if (propertyName.equals(SUPERCLASS)) 
			return validateSuperClassName(getStringProperty(propertyName));

		if ((isJSFPortlet || isSeamPortlet) && propertyName.equals(CLASS_NAME)) {
			if (getStringProperty(propertyName).length()!=0) {
				return Status.OK_STATUS;
			}
				
		}
		// Otherwise defer to super to validate the property
		return super.validate(propertyName);
	}
	
	@Override
	public boolean propertySet(String propertyName, Object propertyValue) {
		boolean result = false;
		
		if (SUPERCLASS.equals(propertyName)) {
			model.notifyPropertyChange(ABSTRACT_METHODS, IDataModel.ENABLE_CHG);
			model.notifyPropertyChange(INIT, IDataModel.ENABLE_CHG);
			model.notifyPropertyChange(DESTROY, IDataModel.ENABLE_CHG);
			model.notifyPropertyChange(GET_PORTLET_CONFIG, IDataModel.ENABLE_CHG);
			model.notifyPropertyChange(DO_VIEW, IDataModel.ENABLE_CHG);
			model.notifyPropertyChange(DO_EDIT, IDataModel.ENABLE_CHG);
			model.notifyPropertyChange(DO_HELP, IDataModel.ENABLE_CHG);
			model.notifyPropertyChange(DO_DISPATCH, IDataModel.ENABLE_CHG);
			model.notifyPropertyChange(RENDER, IDataModel.ENABLE_CHG);
			model.notifyPropertyChange(PROCESS_ACTION, IDataModel.ENABLE_CHG);
			
			if (!hasSuperClass()) {
				model.setProperty(ABSTRACT_METHODS, null);
				model.setProperty(INIT, null);
				model.setProperty(DESTROY, null);
				model.setProperty(GET_PORTLET_CONFIG, null);
				model.setProperty(DO_VIEW, null);
				model.setProperty(DO_EDIT, null);
				model.setProperty(DO_HELP, null);
				model.setProperty(DO_DISPATCH, null);
				model.setProperty(RENDER, null);
				model.setProperty(PROCESS_ACTION, null);
			}
			
			model.notifyPropertyChange(ABSTRACT_METHODS, IDataModel.DEFAULT_CHG);
			model.notifyPropertyChange(INIT, IDataModel.DEFAULT_CHG);
			model.notifyPropertyChange(DESTROY, IDataModel.DEFAULT_CHG);
			model.notifyPropertyChange(GET_PORTLET_CONFIG, IDataModel.DEFAULT_CHG);
			model.notifyPropertyChange(DO_VIEW, IDataModel.DEFAULT_CHG);
			model.notifyPropertyChange(DO_EDIT, IDataModel.DEFAULT_CHG);
			model.notifyPropertyChange(DO_HELP, IDataModel.DEFAULT_CHG);
			model.notifyPropertyChange(DO_DISPATCH, IDataModel.DEFAULT_CHG);
			model.notifyPropertyChange(RENDER, IDataModel.DEFAULT_CHG);
			model.notifyPropertyChange(PROCESS_ACTION, IDataModel.DEFAULT_CHG);
			
			List ifaces = (List) model.getProperty(INTERFACES);
			ifaces.add(QUALIFIED_PORTLET);
			
		}
		
		return result || super.propertySet(propertyName, propertyValue);
	}
	
	protected IStatus validateSuperClassName(String superclassName) {
		
		// Check the super class as a java class
		IStatus status = null;
		if (superclassName.trim().length() > 0) {
			status = super.validate(SUPERCLASS);
			if (status.getSeverity() == IStatus.ERROR)
				return status;
		}
		
		return status;
	}
	
	private List getPortletInterfaces() {
		if (interfaceList == null) {
			interfaceList = new ArrayList();
			for (int i = 0; i < PORTLET_INTERFACES.length; i++) {
				interfaceList.add(PORTLET_INTERFACES[i]);
			}
			interfaceList.remove(QUALIFIED_PORTLET);
			
		}
		return interfaceList;
	}
	
	@Override
	protected boolean isAnnotationsSupported() {
		return false;
	}

	@Override
	protected IStatus validateJavaSourceFolder(String folderFullPath) {
		// Ensure that the source folder path is not empty
		if (folderFullPath == null || folderFullPath.length() == 0) {
			String msg = J2EECommonMessages.ERR_JAVA_CLASS_FOLDER_NAME_EMPTY;
			return WTPCommonPlugin.createErrorStatus(msg);
		}
		// Ensure that the source folder path is absolute
		else if (!new Path(folderFullPath).isAbsolute()) {
			String msg = J2EECommonMessages.ERR_JAVA_CLASS_FOLDER_NOT_ABSOLUTE;
			return WTPCommonPlugin.createErrorStatus(msg);
		}
		IProject project = getTargetProject();
		// Ensure project is not closed
		if (project == null) {
			String msg = J2EECommonMessages.ERR_JAVA_CLASS_FOLDER_NOT_EXIST;
			return WTPCommonPlugin.createErrorStatus(msg);
		}
		// Ensure project is accessible.
		if (!project.isAccessible()) {
			String msg = J2EECommonMessages.ERR_JAVA_CLASS_FOLDER_NOT_EXIST;
			return WTPCommonPlugin.createErrorStatus(msg);
		}
		// Ensure the project is a java project.
		try {
			if (!project.hasNature(JavaCore.NATURE_ID)) {
				String msg = J2EECommonMessages.ERR_JAVA_CLASS_NOT_JAVA_PROJECT;
				return WTPCommonPlugin.createErrorStatus(msg);
			}
		} catch (CoreException e) {
			Logger.getLogger().log(e);
		}
		// Ensure the selected folder is a valid java source folder for the component
		IFolder sourcefolder = (IFolder) getSourceFolder();
		if (sourcefolder == null || (sourcefolder != null && !sourcefolder.getFullPath().equals(new Path(folderFullPath)))) {
			String msg = J2EECommonMessages.getResourceString(J2EECommonMessages.ERR_JAVA_CLASS_FOLDER_NOT_SOURCE, new String[]{folderFullPath});
			return WTPCommonPlugin.createErrorStatus(msg);
		}
		// Valid source is selected
		return WTPCommonPlugin.OK_STATUS;
	}
}
