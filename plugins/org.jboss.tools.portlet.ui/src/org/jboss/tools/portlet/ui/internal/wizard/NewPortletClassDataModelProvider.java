package org.jboss.tools.portlet.ui.internal.wizard;

import static org.eclipse.jst.j2ee.web.IServletConstants.QUALIFIED_SERVLET;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.Signature;
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
		propertyNames.add(PARENT_PORTAL);
		propertyNames.add(PAGE_REGION);
		propertyNames.add(PORTLET_HEIGHT);
		propertyNames.add(IF_EXISTS);
		
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
		
		if (propertyName.equals(DO_VIEW)) {
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
			return getDefaultProperty(NAME) + "Instance";
		}
		if (propertyName.equals(WINDOW_NAME)) {
			return getDefaultProperty(NAME) + "Window";
		}
		if (propertyName.equals(PORTLET_HEIGHT)) {
			return "1";
		}
		if (propertyName.equals(PAGE_REGION)) {
			return "center";
		}
		if (propertyName.equals(PARENT_PORTAL)) {
			return "default.default";
		}
		if (propertyName.equals(IF_EXISTS)) {
			return "overwrite";
		}
		
		// Otherwise check super for default value for property
		return super.getDefaultProperty(propertyName);
	}

	@Override
	public IStatus validate(String propertyName) {
		// Validate super class
		if (propertyName.equals(SUPERCLASS)) 
			return validateSuperClassName(getStringProperty(propertyName));

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
			ifaces.add(QUALIFIED_SERVLET);
			
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

}
