package org.jboss.tools.portlet.ui;

import static org.eclipse.wst.common.componentcore.internal.operation.IArtifactEditOperationDataModelProperties.PROJECT_NAME;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.PortletCoreActivator;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class PortletUIActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.portlet.ui";

	// The shared instance
	private static PortletUIActivator plugin;
	
	/**
	 * The constructor
	 */
	public PortletUIActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static PortletUIActivator getDefault() {
		return plugin;
	}
	
	public static IFile getPortletXmlFile(IProject project) {
		IVirtualComponent component = ComponentCore.createComponent(project);
		IVirtualFile portletVirtualFile = component.getRootFolder().getFile(
				IPortletConstants.CONFIG_PATH);

		if (!portletVirtualFile.getUnderlyingFile().exists()) {
			log(new RuntimeException("The portlet.xml file doesn't exist"));
			return null;
		}

		IFile portletFile = portletVirtualFile.getUnderlyingFile();
		return portletFile;
	}

	public static void log(Exception e, String message) {
		IStatus status = new Status(IStatus.ERROR,PLUGIN_ID,message,e);
		PortletCoreActivator.getDefault().getLog().log(status);
	}
	
	public static void log(Throwable e) {
		IStatus status = new Status(IStatus.ERROR,PLUGIN_ID,e.getLocalizedMessage(),e);
		PortletCoreActivator.getDefault().getLog().log(status);
	}

	public static boolean isPortletProject(IDataModel model) {
		String projectName = model.getStringProperty(PROJECT_NAME);
		if(projectName != null && !"".equals(projectName.trim())){
			IProject project = ProjectUtilities.getProject(projectName);
			try {
				IFacetedProject facetedProject = ProjectFacetsManager.create(project);
				return facetedProject != null && facetedProject.hasProjectFacet(getPortletFacet());
			} catch (CoreException e) {
				PortletUIActivator.log(e);
			}
		}
		return false;
		
	}
	
	private static IProjectFacet getPortletFacet() {
		try {
			return ProjectFacetsManager.getProjectFacet(IPortletConstants.PORTLET_FACET_ID);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
