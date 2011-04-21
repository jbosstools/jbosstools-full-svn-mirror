package org.jboss.tools.portlet.core.internal.project.facet;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.ui.wizards.datatransfer.ZipFileStructureProvider;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectEvent;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectListener;
import org.eclipse.wst.common.project.facet.core.events.IProjectFacetActionEvent;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.Messages;
import org.jboss.tools.portlet.core.PortletCoreActivator;

public class PortletPostInstallListener implements IFacetedProjectListener {

	private static final String SEAM_FACET_ID = "jst.seam"; //$NON-NLS-1$
	private static final IOverwriteQuery OVERWRITE_NONE_QUERY = new IOverwriteQuery()
    {
      public String queryOverwrite(String pathString)
      {
        return IOverwriteQuery.NO_ALL;
      }
    };
	private String portletbridgeRuntime;

	public void handleEvent(IFacetedProjectEvent event) {
		IFacetedProject facetedProject = event.getProject();
		Set<IProjectFacetVersion> projectFacets = facetedProject
				.getProjectFacets();
		boolean isJSFPortlet = false;
		boolean isSeamProject = false;
		for (IProjectFacetVersion projectFacetVersion : projectFacets) {
			IProjectFacet projectFacet = projectFacetVersion.getProjectFacet();
			if (IPortletConstants.JSFPORTLET_FACET_ID.equals(projectFacet
					.getId())) {
				isJSFPortlet = true;
			}
			if (SEAM_FACET_ID.equals(projectFacet.getId())) {
				isSeamProject = true;
			}

		}
		if (!isJSFPortlet)
			return;

		if (isJSFPortlet) {
			IProjectFacetActionEvent actionEvent = (IProjectFacetActionEvent) event;
			IDataModel dataModel = (IDataModel) actionEvent.getActionConfig();
			try {
				portletbridgeRuntime = dataModel
						.getStringProperty(IPortletConstants.PORTLET_BRIDGE_RUNTIME);
				if (portletbridgeRuntime == null) {
					PortletCoreActivator.log(null, Messages.PortletPostInstallListener_Invalid_Portletbridge_Runtime);
					return;
				}
			} catch (Exception e) {
				//PortletCoreActivator.log(e);
			}
		}
		
		if (isJSFPortlet) {
			File portletbridgeHome = new File(portletbridgeRuntime);
			if (!portletbridgeHome.exists()) {
				PortletCoreActivator.log(null, Messages.PortletPostInstallListener_Cannot_find_Portletbridge_Runtime);
				return;
			}
			if (!portletbridgeHome.isDirectory()) {
				PortletCoreActivator.log(null, Messages.PortletPostInstallListener_Invalid_Portletbridge_Runtime);
				return;
			}
			File examplesHome = new File(portletbridgeHome,"examples"); //$NON-NLS-1$
			if (!examplesHome.exists() || !examplesHome.isDirectory()) {
				PortletCoreActivator.log(null, Messages.PortletPostInstallListener_Cannot_find_the_examples_directory);
				return;
			}
			File richFacesPortletZip = new File(examplesHome,"RichFacesPortlet.war"); //$NON-NLS-1$
			if (!richFacesPortletZip.exists() || !richFacesPortletZip.isFile()) {
				PortletCoreActivator.log(null, Messages.PortletPostInstallListener_Cannot_find_the_RichFacesPortlet_war_file);
				return;
			}
			try {
				ZipFile zipFile = new ZipFile(richFacesPortletZip);
				ZipFileStructureProvider structureProvider = new ZipFileStructureProvider(
						zipFile );
				List<ZipEntry> list = prepareList(zipFile, isSeamProject);
				
				IProject project = facetedProject.getProject();
				IVirtualComponent component = ComponentCore
					.createComponent(project);
				//IVirtualFile libVirtualFile = component.getRootFolder()
				//	.getFile(IPortletConstants.WEB_INF_LIB);

				IVirtualFolder rootFolder = component.getRootFolder();
				IContainer folder = rootFolder.getUnderlyingFolder();
				IPath destPath = folder.getFullPath();
		
				ImportOperation op = new ImportOperation( destPath,
						structureProvider.getRoot( ), structureProvider, OVERWRITE_NONE_QUERY,
						list );
				op.run(new NullProgressMonitor() );
			} catch (Exception e) {
				PortletCoreActivator.log(e);
			} 
			
		}
	}

	private List<ZipEntry> prepareList(ZipFile zipFile, boolean isSeamProject) {
		if (zipFile == null) {
			return null;
		}
		List<ZipEntry> list = new ArrayList<ZipEntry>();
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (entry.getName().endsWith(".jar")) { //$NON-NLS-1$
				if (entry.getName().startsWith("WEB-INF/lib/richfaces")) { //$NON-NLS-1$
					list.add(entry);
				}
				if (!isSeamProject) {
					if (entry.getName().startsWith(
							"WEB-INF/lib/commons-beanutils") //$NON-NLS-1$
							|| entry.getName().startsWith(
									"WEB-INF/lib/commons-digester") //$NON-NLS-1$
							|| entry.getName().startsWith(
									"WEB-INF/lib/jsf-facelets")) { //$NON-NLS-1$
						list.add(entry);
					}
				}
			}
		}
		return list;
	}

}
