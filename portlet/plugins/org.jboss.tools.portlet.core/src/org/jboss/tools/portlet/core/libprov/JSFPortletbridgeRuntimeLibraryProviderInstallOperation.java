package org.jboss.tools.portlet.core.libprov;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryProviderOperation;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryProviderOperationConfig;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.project.facet.core.FacetedProjectFramework;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectBase;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.Messages;
import org.jboss.tools.portlet.core.PortletCoreActivator;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class JSFPortletbridgeRuntimeLibraryProviderInstallOperation extends
		LibraryProviderOperation {

	@Override
	public void execute(LibraryProviderOperationConfig config,
			IProgressMonitor monitor) throws CoreException {
		IFacetedProjectBase facetedProject = config.getFacetedProject();
		IProject project = facetedProject.getProject();
		JSFPortletbridgeRuntimeLibraryProviderInstallOperationConfig portletbridgeConfig = (JSFPortletbridgeRuntimeLibraryProviderInstallOperationConfig) config;
		String pbRuntime = portletbridgeConfig.getPortletbridgeHome();
		getPortletbridgeLibraries(monitor, project, pbRuntime);
		
		try {
			Preferences prefs = FacetedProjectFramework.getPreferences( config.getProjectFacet() );
			prefs = prefs.node(IPortletConstants.PORTLET_BRIDGE_HOME);
			prefs.put(IPortletConstants.PREFS_PORTLETBRIDGE_HOME, pbRuntime);
			prefs.flush();
		} catch (BackingStoreException e) {
			PortletCoreActivator.log(e);
		}
	}

	private void getPortletbridgeLibraries(IProgressMonitor monitor,
			IProject project, String pbRuntime) {
		if (pbRuntime != null && pbRuntime.trim().length() > 0) {
			pbRuntime = pbRuntime.trim();
			File pbFolder = new File(pbRuntime);
			if (pbFolder.exists() && pbFolder.isDirectory()) {
				String[] fileList = pbFolder.list(new FilenameFilter() {

					public boolean accept(File dir, String name) {
						if (name.startsWith("portletbridge") || name.endsWith(".jar")) { //$NON-NLS-1$ //$NON-NLS-2$
							return true;
						}
						
						return false;
					}

				});

				List<File> filesToImport = new ArrayList<File>();

				for (int i = 0; i < fileList.length; i++) {
					filesToImport.add(new File(pbRuntime, fileList[i]));
				}
				IVirtualComponent component = ComponentCore
						.createComponent(project);
				IVirtualFile libVirtualFile = component.getRootFolder()
						.getFile(IPortletConstants.WEB_INF_LIB);

				IFile folder = libVirtualFile.getUnderlyingFile();

				ImportOperation importOperation = new ImportOperation(
						folder.getFullPath(), pbFolder,
						FileSystemStructureProvider.INSTANCE,
						PortletCoreActivator.OVERWRITE_ALL_QUERY,
						filesToImport);
				importOperation.setCreateContainerStructure(false);
				try {
					importOperation.run(monitor);
				} catch (Exception e) {
					PortletCoreActivator.log(e, Messages.JSFPortletFacetInstallDelegate_Error_loading_classpath_container);
				} 
			}
		}
	}
	
	
}
