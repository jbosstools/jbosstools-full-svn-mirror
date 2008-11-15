package org.jboss.tools.birt.core.internal.project.facet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.javaee.core.JavaeeFactory;
import org.eclipse.jst.javaee.core.UrlPatternType;
import org.eclipse.jst.javaee.web.Servlet;
import org.eclipse.jst.javaee.web.ServletMapping;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.javaee.web.WebFactory;
import org.eclipse.osgi.util.NLS;
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
import org.jboss.tools.birt.core.Messages;
import org.osgi.framework.Bundle;

public class BirtPostInstallListener implements IFacetedProjectListener {

	private static final String JBossBirtCorePluginId = "org.jboss.tools.birt.core"; //$NON-NLS-1$
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
			if (BirtCoreActivator.BIRT_FACET_ID.equals(projectFacet.getId())) {
				isBirtProject = true;
			}
			if (BirtCoreActivator.SEAM_FACET_ID.equals(projectFacet.getId())) {
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
					String message = ""; //$NON-NLS-1$
					Logger.log(Logger.ERROR, message);
					return;
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		if (isJBossBirtProject || isBirtProject) {
			String configIniString = configFolder
					+ "/WEB-INF/platform/configuration/config.ini"; //$NON-NLS-1$
			IProject project = facetedProject.getProject();
			IResource configFile = project
					.findMember(new Path(configIniString));
			if (!configFile.exists()) {
				String message = Messages.BirtPostInstallListener_The_config_ini_file_doesnt_exist;
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
				String bootDelegation = "org.osgi.framework.bootdelegation"; //$NON-NLS-1$
				String loader = "osgi.parentClassloader"; //$NON-NLS-1$
				properties
						.put(
								bootDelegation,
								"org.hibernate,org.hibernate.type,org.hibernate.metadata,org.hibernate.ejb, javax.persistence"); //$NON-NLS-1$
				properties.put(loader, "fwk"); //$NON-NLS-1$
				// FIXME
				// String compatibility = "osgi.compatibility.bootdelegation";
				// properties.put(compatibility,"false");
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
			String platformFolder = configFolder + "/WEB-INF/platform/plugins"; //$NON-NLS-1$
			IProject project = facetedProject.getProject();
			IProgressMonitor monitor = new NullProgressMonitor();
			BirtCoreActivator.copyPlugin(project, "org.jboss.tools.birt.oda", //$NON-NLS-1$
					platformFolder, monitor);
		}
		if (isSeamProject && (isBirtProject || isJBossBirtProject)) {
			IProject project = facetedProject.getProject();
			String libFolder = configFolder + "/WEB-INF/lib"; //$NON-NLS-1$
			IResource destResource = project.findMember(libFolder);
			if (destResource.getType() != IResource.FOLDER) {
				IStatus status = new Status(IStatus.WARNING,
						BirtCoreActivator.PLUGIN_ID, NLS.bind(Messages.BirtPostInstallListener_The_resource_is_not_a_folder,libFolder));
				BirtCoreActivator.getDefault().getLog().log(status);
				return;
			}
			IFolder folder = (IFolder) destResource;
			Bundle bundle = Platform.getBundle(JBossBirtCorePluginId);
			URL entryComponent = bundle
					.getEntry("/resources/jboss-seam-birt.jar"); //$NON-NLS-1$
			URL entryServlet = bundle
					.getEntry("/resources/jboss-birt-servlet.jar"); //$NON-NLS-1$
			try {
				copyEntry(entryComponent, folder);
				copyEntry(entryServlet, folder);
				configureJBossBirtServlet(facetedProject.getProject());
			} catch (Exception e) {
				IStatus status = new Status(IStatus.WARNING,
						BirtCoreActivator.PLUGIN_ID,
						Messages.BirtPostInstallListener_Error_while_creating_JBoss_BIRT_artifacts, e);
				BirtCoreActivator.getDefault().getLog().log(status);
			} finally {
				configFolder = null;
			}
		}
	}

	private void configureJBossBirtServlet(final IProject project) {
		IModelProvider modelProvider = ModelProviderManager
				.getModelProvider(project);
		Object modelObject = modelProvider.getModelObject();
		if (!(modelObject instanceof WebApp)) {
			// TODO log
			return;
		}
		IPath modelPath = new Path("WEB-INF").append("web.xml"); //$NON-NLS-1$ //$NON-NLS-2$
		boolean exists = project.getProjectRelativePath().append(modelPath)
				.toFile().exists();
		if (!exists) {
			modelPath = IModelProvider.FORCESAVE;
		}
		modelProvider.modify(new Runnable() {

			public void run() {
				IModelProvider modelProvider = ModelProviderManager
						.getModelProvider(project);
				Object modelObject = modelProvider.getModelObject();
				if (!(modelObject instanceof WebApp)) {
					// TODO log
					return;
				}
				WebApp webApp = (WebApp) modelObject;
				String servletClass = "org.jboss.tools.birt.servlet.JBossBirtServlet"; //$NON-NLS-1$
				String servletName = "JBoss BIRT Servlet"; //$NON-NLS-1$
				List servlets = webApp.getServlets();
				boolean added = false;
				for (Iterator iterator = servlets.iterator(); iterator
						.hasNext();) {
					Servlet servlet = (Servlet) iterator.next();
					if (servletName.equals(servlet.getServletName())) {
						servlet.setServletName(servletName);
						added = true;
						break;
					}
				}
				if (!added) {
					Servlet servlet = WebFactory.eINSTANCE.createServlet();
					servlet.setServletName(servletName);
					servlet.setServletClass(servletClass);
					webApp.getServlets().add(servlet);
				}
				
				String name = servletName;
				String value="/embed"; //$NON-NLS-1$
				List servletMappings = webApp.getServletMappings();
				added = false;
				for (Iterator iterator = servletMappings.iterator(); iterator.hasNext();) {
					ServletMapping servletMapping = (ServletMapping) iterator.next();
					if (servletMapping != null
							&& name.equals(servletMapping.getServletName())) {
						added = true;
						// FIXME
					}
				}
				if (!added) {
					ServletMapping mapping = WebFactory.eINSTANCE
							.createServletMapping();
					Servlet servlet = findServletByName(webApp, name);
					if (servlet != null) {
						mapping.setServletName(servlet.getServletName());
						UrlPatternType urlPattern = JavaeeFactory.eINSTANCE
								.createUrlPatternType();
						urlPattern.setValue(value);
						mapping.getUrlPatterns().add(urlPattern);
						webApp.getServletMappings().add(mapping);
					}
				}
			}

		}, modelPath);

	}

	private Servlet findServletByName(WebApp webApp, String name) {
		Iterator it = webApp.getServlets().iterator();
		while (it.hasNext()) {
			Servlet servlet = (Servlet) it.next();
			if (servlet.getServletName() != null
					&& servlet.getServletName().trim().equals(name)) {
				return servlet;
			}
		}
		return null;
	}
	private void copyEntry(URL entry, IFolder folder) throws Exception {
		String fileName = FileLocator.toFileURL(entry).getFile();
		File file = new File(fileName);
		List<File> filesToImport = new ArrayList<File>();
		filesToImport.add(file);
		ImportOperation importOperation = new ImportOperation(folder
				.getFullPath(), file.getParentFile(),
				FileSystemStructureProvider.INSTANCE,
				BirtCoreActivator.OVERWRITE_ALL_QUERY, filesToImport);
		importOperation.setCreateContainerStructure(false);
		IProgressMonitor monitor = new NullProgressMonitor();
		importOperation.run(monitor);
	}

}
