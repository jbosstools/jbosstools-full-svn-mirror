package org.jboss.tools.birt.core.internal.project.facet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.birt.integration.wtp.ui.internal.resource.BirtWTPMessages;
import org.eclipse.birt.integration.wtp.ui.internal.util.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectEvent;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectListener;
import org.eclipse.wst.common.project.facet.core.events.IProjectFacetActionEvent;
import org.jboss.tools.birt.core.BirtCoreActivator;
import org.osgi.framework.Bundle;

public class BirtPostInstallListener implements IFacetedProjectListener {

	private static final String JBossBirtCorePluginId = "org.jboss.tools.birt.core";
	private String configFolder;

	public void handleEvent(IFacetedProjectEvent event) {
		IFacetedProject facetedProject = event.getProject();
		Set<IProjectFacetVersion> projectFacets = facetedProject
				.getProjectFacets();
		boolean isJBossBirtProject = false;
		boolean isBirtProject = false;
		boolean isSeamProject = false;
		for (IProjectFacetVersion projectFacetVersion : projectFacets) {
			IProjectFacet projectFacet = projectFacetVersion.getProjectFacet();
			if (BirtCoreActivator.JBOSS_BIRT__FACET_ID.equals(projectFacet
					.getId())) {
				isJBossBirtProject = true;
			}
			if (BirtCoreActivator.BIRT_FACET_ID.equals(projectFacet
					.getId())) {
				isBirtProject = true;
			}
			if (BirtCoreActivator.SEAM_FACET_ID.equals(projectFacet
					.getId())) {
				isSeamProject = true;
			}
			
		}
		if (!isBirtProject && !isJBossBirtProject && !isSeamProject)
			return;
		
		if (isBirtProject || isJBossBirtProject) {
			IProjectFacetActionEvent actionEvent = (IProjectFacetActionEvent) event;
			IDataModel dataModel = (IDataModel) actionEvent.getActionConfig();
			try {
				configFolder = dataModel
					.getStringProperty("IJ2EEFacetInstallDataModelProperties.CONFIG_FOLDER"); //$NON-NLS-1$
				if (configFolder == null) {
					String message = BirtWTPMessages.BIRTErrors_wrong_webcontent;
					Logger.log(Logger.ERROR, message);
					return;
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		if (isJBossBirtProject || isBirtProject) {
			String configIniString = configFolder + "/WEB-INF/platform/configuration/config.ini";
			IProject project = facetedProject.getProject();
			IResource configFile = project.findMember(new Path(configIniString));
			if (!configFile.exists()) {
				String message = "The config.ini file doesn't exist";
				Logger.log(Logger.ERROR, message);
				return;
			}
			Properties properties = new Properties();
			InputStream inputStream = null;
			ByteArrayOutputStream outputStream = null;
			try {
				URL url = configFile.getLocation().toFile().toURL();
				inputStream = url.openStream();
				properties.load(inputStream);
				String bootDelegation = "org.osgi.framework.bootdelegation";
				String loader = "osgi.parentClassloader";
				properties.put(bootDelegation, "org.hibernate,org.hibernate.type,org.hibernate.metadata,org.hibernate.ejb, javax.persistence");
				properties.put(loader,"fwk");
				// FIXME
				//String compatibility = "osgi.compatibility.bootdelegation";
				//properties.put(compatibility,"false");
				IFile file = (IFile) configFile;
				outputStream = new ByteArrayOutputStream();
				properties.store(outputStream, null);
				file.setContents(new ByteArrayInputStream(outputStream
				        .toByteArray()), true, true, null);
				
			} catch (Exception e) {
				Logger.log(Logger.ERROR, e.getLocalizedMessage());
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						// ignore
					}
				}
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						// ignore
					}
				}
			}
			
		}
		if (isBirtProject && !isJBossBirtProject) {
			String platformFolder = configFolder + "/WEB-INF/platform/plugins";
			IProject project = facetedProject.getProject();
			IProgressMonitor monitor = new NullProgressMonitor();
			BirtCoreActivator.copyPlugin(project,"org.jboss.tools.birt.oda",platformFolder,monitor);
		}
		if (isSeamProject && (isBirtProject || isJBossBirtProject)) {
			IProject project = facetedProject.getProject();
			String libFolder = configFolder + "/WEB-INF/lib";
			IResource destResource = project.findMember(libFolder);
			if (destResource.getType() != IResource.FOLDER ) {
				IStatus status = new Status(IStatus.WARNING,BirtCoreActivator.PLUGIN_ID,"The " + libFolder + " resource is not a folder");
				BirtCoreActivator.getDefault().getLog().log(status);
				return;
			}
			IFolder folder = (IFolder) destResource;
			Bundle bundle = Platform.getBundle(JBossBirtCorePluginId);
			URL entry = bundle.getEntry("/resources/jboss-seam-birt.jar");
			try {
				String fileName = FileLocator.toFileURL(entry).getFile();
				File file = new File(fileName);
				List<File> filesToImport = new ArrayList<File>();
				filesToImport.add(file);
				ImportOperation importOperation = new ImportOperation(folder.getFullPath(),
						file.getParentFile(), FileSystemStructureProvider.INSTANCE,
						BirtCoreActivator.OVERWRITE_ALL_QUERY, filesToImport);
				importOperation.setCreateContainerStructure(false);
				IProgressMonitor monitor = new NullProgressMonitor();
				importOperation.run(monitor);
			} catch (Exception e) {
				IStatus status = new Status(IStatus.WARNING,BirtCoreActivator.PLUGIN_ID,"Error while copying jboss-seam-birt.jar",e);
				BirtCoreActivator.getDefault().getLog().log(status);
			} finally {
				configFolder=null;
			}
		}
	}

}
