package org.jboss.tools.birt.core.internal.project.facet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

import org.eclipse.birt.integration.wtp.ui.internal.resource.BirtWTPMessages;
import org.eclipse.birt.integration.wtp.ui.internal.util.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectEvent;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectListener;
import org.eclipse.wst.common.project.facet.core.events.IProjectFacetActionEvent;
import org.jboss.tools.birt.core.BirtCoreActivator;

public class BirtPostInstallListener implements IFacetedProjectListener {

	public void handleEvent(IFacetedProjectEvent event) {
		IFacetedProject facetedProject = event.getProject();
		Set<IProjectFacetVersion> projectFacets = facetedProject
				.getProjectFacets();
		boolean isBirtProject = false;
		for (IProjectFacetVersion projectFacetVersion : projectFacets) {
			IProjectFacet projectFacet = projectFacetVersion.getProjectFacet();
			if (BirtCoreActivator.JBOSS_BIRT__FACET_ID.equals(projectFacet
					.getId())) {
				isBirtProject = true;
				break;
			}
		}
		if (isBirtProject) {
			IProjectFacetActionEvent actionEvent = (IProjectFacetActionEvent) event;
			IDataModel dataModel = (IDataModel) actionEvent.getActionConfig();
			String configFolder = dataModel
					.getStringProperty("IJ2EEFacetInstallDataModelProperties.CONFIG_FOLDER"); //$NON-NLS-1$
			if (configFolder == null) {
				String message = BirtWTPMessages.BIRTErrors_wrong_webcontent;
				Logger.log(Logger.ERROR, message);
				return;
			}
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
				properties.put(bootDelegation, "org.hibernate,org.hibernate.type,org.hibernate.metadata");
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
	}

}
