package org.jboss.ide.eclipse.as.ui.xpl;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.debug.ui.IJavaDebugUIConstants;
import org.eclipse.jdt.internal.debug.ui.IJavaDebugHelpContextIds;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.launcher.DebugTypeSelectionDialog;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.jdt.internal.debug.ui.launcher.MainMethodSearchEngine;
import org.eclipse.jdt.internal.debug.ui.launcher.SharedJavaMainTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

/**
 * This class is only here because of all the pain in the ass 
 * shit the jdt classes put in your way to prevent subclassing.
 * 
 * Fuck that. 
 */

public class JavaMainTabClone extends SharedJavaMainTab {
	/**
	 * Boolean launch configuration attribute indicating that external jars (on
	 * the runtime classpath) should be searched when looking for a main type.
	 * Default value is <code>false</code>.
	 * 
	 * @since 2.1
	 */
	public static final String ATTR_INCLUDE_EXTERNAL_JARS = IJavaDebugUIConstants.PLUGIN_ID + ".INCLUDE_EXTERNAL_JARS"; //$NON-NLS-1$
	/**
	 * Boolean launch configuration attribute indicating whether types inheriting
	 * a main method should be considered when searching for a main type.
	 * Default value is <code>false</code>.
	 * 
	 * @since 3.0
	 */
	public static final String ATTR_CONSIDER_INHERITED_MAIN = IJavaDebugUIConstants.PLUGIN_ID + ".CONSIDER_INHERITED_MAIN"; //$NON-NLS-1$	
	
	// UI widgets
	private Button fSearchExternalJarsCheckButton;
	private Button fConsiderInheritedMainButton;
	private Button fStopInMainCheckButton;

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, parent.getFont(), 1, 1, GridData.FILL_BOTH);
		((GridLayout)comp.getLayout()).verticalSpacing = 0;
		createProjectEditor(comp);
		createVerticalSpacer(comp, 1);
		createMainTypeEditor(comp, LauncherMessages.JavaMainTab_Main_cla_ss__4);
		setControl(comp);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IJavaDebugHelpContextIds.LAUNCH_CONFIGURATION_DIALOG_MAIN_TAB);
	}

	/**
	 * @see org.eclipse.jdt.internal.debug.ui.launcher.SharedJavaMainTab#createMainTypeExtensions(org.eclipse.swt.widgets.Composite)
	 */
	protected void createMainTypeExtensions(Composite parent) {
		fSearchExternalJarsCheckButton = SWTFactory.createCheckButton(parent, LauncherMessages.JavaMainTab_E_xt__jars_6, null, false, 2);
		fSearchExternalJarsCheckButton.addSelectionListener(getDefaultListener());
		
		fConsiderInheritedMainButton = SWTFactory.createCheckButton(parent, LauncherMessages.JavaMainTab_22, null, false, 2);
		fConsiderInheritedMainButton.addSelectionListener(getDefaultListener());
		
		fStopInMainCheckButton = SWTFactory.createCheckButton(parent, LauncherMessages.JavaMainTab_St_op_in_main_1, null, false, 1);
		fStopInMainCheckButton.addSelectionListener(getDefaultListener());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getImage()
	 */
	public Image getImage() {
		return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_CLASS);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return LauncherMessages.JavaMainTab__Main_19;
	}
	
	/**
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getId()
	 * 
	 * @since 3.3
	 */
	public String getId() {
		return "org.eclipse.jdt.debug.ui.javaMainTab"; //$NON-NLS-1$
	}
	
	/**
	 * Show a dialog that lists all main types
	 */
	protected void handleSearchButtonSelected() {
		IJavaProject project = getJavaProject();
		IJavaElement[] elements = null;
		if ((project == null) || !project.exists()) {
			IJavaModel model = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
			if (model != null) {
				try {
					elements = model.getJavaProjects();
				}
				catch (JavaModelException e) {JDIDebugUIPlugin.log(e);}
			}
		}
		else {
			elements = new IJavaElement[]{project};
		}
		if (elements == null) {
			elements = new IJavaElement[]{};
		}
		int constraints = IJavaSearchScope.SOURCES;
		constraints |= IJavaSearchScope.APPLICATION_LIBRARIES;
		if (fSearchExternalJarsCheckButton.getSelection()) {
			constraints |= IJavaSearchScope.SYSTEM_LIBRARIES;
		}
		IJavaSearchScope searchScope = SearchEngine.createJavaSearchScope(elements, constraints);
		MainMethodSearchEngine engine = new MainMethodSearchEngine();
		IType[] types = null;
		try {
			types = engine.searchMainMethods(getLaunchConfigurationDialog(), searchScope, fConsiderInheritedMainButton.getSelection());
		}
		catch (InvocationTargetException e) {
			setErrorMessage(e.getMessage());
			return;
		}
		catch (InterruptedException e) {
			setErrorMessage(e.getMessage());
			return;
		}
		DebugTypeSelectionDialog mmsd = new DebugTypeSelectionDialog(getShell(), types, LauncherMessages.JavaMainTab_Choose_Main_Type_11); 
		if (mmsd.open() == Window.CANCEL) {
			return;
		}
		Object[] results = mmsd.getResult();	
		IType type = (IType)results[0];
		if (type != null) {
			fMainText.setText(type.getFullyQualifiedName());
			fProjText.setText(type.getJavaProject().getElementName());
		}
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.debug.ui.launcher.AbstractJavaMainTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration config) {
		//super.initializeFrom(config);
		//updateProjectFromConfig(config);
		setCurrentLaunchConfiguration(config);
		updateMainTypeFromConfig(config);
		updateStopInMainFromConfig(config);
		updateInheritedMainsFromConfig(config);
		updateExternalJars(config);
	}	

	private ILaunchConfiguration fLaunchConfig;
	protected ILaunchConfiguration getCurrentLaunchConfiguration() {
		return fLaunchConfig;
	}
	protected void setCurrentLaunchConfiguration(ILaunchConfiguration config) {
		fLaunchConfig = config;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#isValid(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public boolean isValid(ILaunchConfiguration config) {
		setErrorMessage(null);
		setMessage(null);
		String name = fProjText.getText().trim();
		if (name.length() > 0) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IStatus status = workspace.validateName(name, IResource.PROJECT);
			if (status.isOK()) {
				IProject project= ResourcesPlugin.getWorkspace().getRoot().getProject(name);
				if (!project.exists()) {
					setErrorMessage(MessageFormat.format(LauncherMessages.JavaMainTab_20, new String[] {name})); 
					return false;
				}
				if (!project.isOpen()) {
					setErrorMessage(MessageFormat.format(LauncherMessages.JavaMainTab_21, new String[] {name})); 
					return false;
				}
			}
			else {
				setErrorMessage(MessageFormat.format(LauncherMessages.JavaMainTab_19, new String[]{status.getMessage()})); 
				return false;
			}
		}
		name = fMainText.getText().trim();
		if (name.length() == 0) {
			setErrorMessage(LauncherMessages.JavaMainTab_Main_type_not_specified_16); 
			return false;
		}
		return true;
	}
			
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy config) {
		//config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, fProjText.getText().trim());
		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, fMainText.getText().trim());
		mapResources(config);
		
		// attribute added in 2.1, so null must be used instead of false for backwards compatibility
		if (fStopInMainCheckButton.getSelection()) {
			config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_STOP_IN_MAIN, true);
		}
		else {
			config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_STOP_IN_MAIN, (String)null);
		}
		
		// attribute added in 2.1, so null must be used instead of false for backwards compatibility
		if (fSearchExternalJarsCheckButton.getSelection()) {
			config.setAttribute(ATTR_INCLUDE_EXTERNAL_JARS, true);
		}
		else {
			config.setAttribute(ATTR_INCLUDE_EXTERNAL_JARS, (String)null);
		}
		
		// attribute added in 3.0, so null must be used instead of false for backwards compatibility
		if (fConsiderInheritedMainButton.getSelection()) {
			config.setAttribute(ATTR_CONSIDER_INHERITED_MAIN, true);
		}
		else {
			config.setAttribute(ATTR_CONSIDER_INHERITED_MAIN, (String)null);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		IJavaElement javaElement = getContext();
		if (javaElement != null) {
			initializeJavaProject(javaElement, config);
		}
		else {
			config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, EMPTY_STRING);
		}
		initializeMainTypeAndName(javaElement, config);
	}
	
	/**
	 * updates the external jars attribute from the specified launch config
	 * @param config the config to load from
	 */
	protected void updateExternalJars(ILaunchConfiguration config) {
		boolean search = false;
		try {
			search = config.getAttribute(ATTR_INCLUDE_EXTERNAL_JARS, false);
		}
		catch (CoreException e) {JDIDebugUIPlugin.log(e);}
		fSearchExternalJarsCheckButton.setSelection(search);
	}

	/**
	 * update the inherited mains attribute from the specified launch config
	 * @param config the config to load from
	 */
	protected void updateInheritedMainsFromConfig(ILaunchConfiguration config) {
		boolean inherit = false;
		try {
			inherit = config.getAttribute(ATTR_CONSIDER_INHERITED_MAIN, false);
		}
		catch (CoreException e) {JDIDebugUIPlugin.log(e);}
		fConsiderInheritedMainButton.setSelection(inherit);
	}

	/**
	 * updates the stop in main attribute from the specified launch config
	 * @param config the config to load the stop in main attribute from
	 */
	protected void updateStopInMainFromConfig(ILaunchConfiguration config) {
		boolean stop = false;
		try {
			stop = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_STOP_IN_MAIN, false);
		}
		catch (CoreException e) {JDIDebugUIPlugin.log(e);}
		fStopInMainCheckButton.setSelection(stop);
	}

}
