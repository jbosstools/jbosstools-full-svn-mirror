package org.jboss.tools.portlet.core.libprov;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryProviderOperation;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryProviderOperationConfig;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectBase;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IRuntime;
import org.jboss.ide.eclipse.as.core.server.IJBossServerConstants;
import org.jboss.ide.eclipse.as.core.server.IJBossServerRuntime;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.Messages;
import org.jboss.tools.portlet.core.PortletCoreActivator;

public class JSFPortletServerRuntimeLibraryProviderInstallOperation extends
		LibraryProviderOperation {

	@Override
	public void execute(LibraryProviderOperationConfig config,
			IProgressMonitor monitor) throws CoreException {
		IFacetedProjectBase facetedProject = config.getFacetedProject();
		IProjectFacet seamFacet = ProjectFacetsManager.getProjectFacet("jst.seam"); //$NON-NLS-1$
		boolean hasSeamFacet = facetedProject.hasProjectFacet(seamFacet);
		org.eclipse.wst.common.project.facet.core.runtime.IRuntime facetRuntime = facetedProject.getPrimaryRuntime();
		IProject project = facetedProject.getProject();
		IRuntime runtime = PortletCoreActivator.getRuntime(facetRuntime);
		IJBossServerRuntime jbossRuntime = (IJBossServerRuntime)runtime.loadAdapter(IJBossServerRuntime.class, new NullProgressMonitor());
		if (jbossRuntime != null) {
			// JBoss Portal server
			IPath jbossLocation = runtime.getLocation();
			IPath configPath = jbossLocation.append(IJBossServerConstants.SERVER).append(jbossRuntime.getJBossConfiguration());
			IPath portletLib = configPath.append(IPortletConstants.PORTLET_SAR_LIB);
			File portletLibFile = portletLib.toFile();
			String[] files = getPortletbridgeLibraries(portletLibFile, hasSeamFacet);
			if (files == null) {
				portletLib = configPath.append(IPortletConstants.PORTLET_SAR_HA_LIB);
				portletLibFile = portletLib.toFile();
				files = getPortletbridgeLibraries(portletLibFile, hasSeamFacet);
					
			}
			if (files != null) {
				try {
					List<File> filesToImport = new ArrayList<File>();

					for (int i = 0; i < files.length; i++) {
						filesToImport.add(new File(portletLibFile, files[i]));
					}
					IVirtualComponent component = ComponentCore
							.createComponent(project);
					IVirtualFile libVirtualFile = component.getRootFolder()
							.getFile(IPortletConstants.WEB_INF_LIB);

					IFile folder = libVirtualFile.getUnderlyingFile();

					File sourceFolder = new File(portletLib.toOSString());
					ImportOperation importOperation = new ImportOperation(
							folder.getFullPath(), sourceFolder,
							FileSystemStructureProvider.INSTANCE,
							PortletCoreActivator.OVERWRITE_ALL_QUERY,
							filesToImport);
					importOperation.setCreateContainerStructure(false);
					importOperation.run(monitor);
				} catch (Exception e) {
					PortletCoreActivator
					.log(e, Messages.JSFPortletFacetInstallDelegate_Error_loading_classpath_container);
				}
			}
		}
	}
	
	private String[] getPortletbridgeLibraries(File file, final boolean hasSeamFacet) {
		if (file != null && file.isDirectory()) {
			String[] list = file.list(new FilenameFilter() {

				public boolean accept(File dir, String name) {
					if ("portletbridge-api.jar".equals(name) || //$NON-NLS-1$
							"portletbridge-impl.jar".equals(name)) { //$NON-NLS-1$
						return true;
					}
					if (!hasSeamFacet) {
						if (name.startsWith("portal")) { //$NON-NLS-1$
							return false;
						} else {
							return true;
						}
						
					}
					return false;
				}
				
			});
			return list;
		}
		return null;
	}
}
