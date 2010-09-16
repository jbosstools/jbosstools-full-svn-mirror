/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.gwt.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.j2ee.webapplication.WebapplicationFactory;
import org.eclipse.jst.j2ee.webapplication.WelcomeFile;
import org.eclipse.jst.javaee.core.JavaeeFactory;
import org.eclipse.jst.javaee.core.UrlPatternType;
import org.eclipse.jst.javaee.web.Servlet;
import org.eclipse.jst.javaee.web.ServletMapping;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.javaee.web.WebFactory;
import org.eclipse.jst.javaee.web.WelcomeFileList;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.jboss.tools.common.EclipseUtil;
import org.jboss.tools.common.log.LogHelper;
import org.jboss.tools.common.model.project.ProjectHome;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.gwt.core.internal.GWTCoreActivator;
import org.jboss.tools.gwt.core.util.ProjectUtils;
import org.jboss.tools.gwt.core.util.ResourceUtils;
import org.jboss.tools.gwt.core.util.ZipUtils;
import org.jboss.tools.usage.util.StatusUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;
import org.osgi.service.prefs.BackingStoreException;

/**
 * @author Andre Dietisheim
 */
public class GWTInstallFacetDelegate implements IDelegate {

	private static final String GWTSERVLETJAR_FOLDER_PATTERN = "gwt-{0}.{1}.{2}";

	public void execute(IProject project, IProjectFacetVersion projectFacetVersion, Object config,
			IProgressMonitor monitor) throws CoreException {
		IJavaProject javaProject = JavaCore.create(project);

		addGwtNature(javaProject, monitor);
		addGWTClasspathContainer(javaProject, monitor);

		IPath webContentPath = getWebContentFolder(project, monitor);

		createWebApplicationPreferences(project, webContentPath, javaProject, monitor);
		configureOutputFolder(webContentPath, javaProject, monitor);

		GWTInstallDataModelProvider dataModel = (GWTInstallDataModelProvider) config;
		if (dataModel.isGenerateSampleCode()) {
			List<IPath> srcFolderPaths = ProjectUtils.getSourceFolders(javaProject);
			createSample(srcFolderPaths, webContentPath, javaProject, monitor);
		}
		configureWebXml(project, monitor);
	}

	private IPath getWebContentFolder(IProject project, IProgressMonitor monitor) throws CoreException {
		IPath webContentPath = ProjectHome.getFirstWebContentPath(project);
		Assert.isTrue(webContentPath != null && !webContentPath.isEmpty(),
				MessageFormat
						.format("No web content folder was found in project {0}", project.getName()));
		return webContentPath;
	}

	private void addGwtNature(IJavaProject javaProject, IProgressMonitor monitor) throws CoreException {
		monitor.subTask("Adding GWT nature");

		EclipseUtil.addNatureToProject(javaProject.getProject(), IGoogleEclipsePluginConstants.GWT_NATURE);
	}

	private void addGWTClasspathContainer(IJavaProject javaProject, IProgressMonitor monitor) throws CoreException {
		monitor.subTask("Adding gwt container to classpath");

		IClasspathEntry entry = JavaCore.newContainerEntry(new Path(IGoogleEclipsePluginConstants.GWT_CONTAINER_ID),
				false);
		ProjectUtils.addClasspathEntry(javaProject, entry, monitor);
	}

	private void createWebApplicationPreferences(IProject project, IPath webContentPath,
			IJavaProject javaProject, IProgressMonitor monitor) throws CoreException {
		try {
			monitor.subTask("creating web application preferences");

			IScopeContext projectScope = new ProjectScope(project);
			IEclipsePreferences preferences = projectScope.getNode(IGoogleEclipsePluginConstants.GDT_PLUGIN_ID);

			preferences.put(IGoogleEclipsePluginConstants.WAR_SRCDIR_KEY,
					webContentPath.makeRelativeTo(project.getFullPath()).toString());
			preferences.put(IGoogleEclipsePluginConstants.WAR_SRCDIR_ISOUTPUT_KEY,
					IGoogleEclipsePluginConstants.WAR_SRCDIR_ISOUTPUT_DEFAULTVALUE);
			preferences.flush();
		} catch (BackingStoreException e) {
			throw new CoreException(StatusUtils.getErrorStatus(GWTCoreActivator.PLUGIN_ID,
					"Could not save project preferences", e));
		}
	}

	private void configureOutputFolder(IPath webContentProjectPath, final IJavaProject javaProject,
			IProgressMonitor monitor)
			throws CoreException, JavaModelException {
		IProject project = javaProject.getProject();
		monitor.subTask("Configuring output folder");
		IPath outputFolderProjectPath = webContentProjectPath.append(new Path(
				IGoogleEclipsePluginConstants.OUTPUT_FOLDER_DEFAULTVALUE));
		final IFolder outputWorkspaceFolder = project.getWorkspace().getRoot().getFolder(outputFolderProjectPath);
		project.getWorkspace().run(
				new IWorkspaceRunnable() {
					public void run(IProgressMonitor monitor) throws CoreException {
						if (!outputWorkspaceFolder.exists()) {
							ResourceUtils.create(outputWorkspaceFolder, monitor);
						}
						javaProject.setOutputLocation(outputWorkspaceFolder.getFullPath(), monitor);
					}
				}, monitor);
	}

	private void createSample(final List<IPath> srcPaths, final IPath webContentPath, final IJavaProject javaProject,
			IProgressMonitor monitor)
			throws CoreException {

		if (srcPaths.size() <= 0) {
			LogHelper.logWarning(GWTCoreActivator.PLUGIN_ID,
					MessageFormat.format("No source folders were found in project {0}", javaProject.getElementName()));
			return;
		}

		monitor.subTask("Creating sample code");

		javaProject.getProject().getWorkspace().run(new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				try {
					/**
					 * TODO: it is not secure to take the first source-folder
					 * that was found (there might be several of them).
					 */
					unzipSrc(srcPaths.get(0), javaProject);
					unzipWebContent(webContentPath, javaProject);
					copyGwtServlet(javaProject, webContentPath, monitor);

				} catch (IOException e) {
					throw new CoreException(StatusUtils.getErrorStatus(GWTCoreActivator.PLUGIN_ID,
								"Could not unzip samples", e));
				}
			}
		}, monitor);

		javaProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}

	private void unzipSrc(final IPath srcFolderPath, final IJavaProject javaProject) throws IOException {
		ZipInputStream inputStream = new ZipInputStream(new BufferedInputStream(
				getClass().getResourceAsStream(IGoogleEclipsePluginConstants.SAMPLE_HELLO_SRC_ZIP_FILENAME)));
		ZipUtils.unzipToFolder(inputStream, ResourceUtils.getFile(srcFolderPath, javaProject.getProject()));
	}

	private void unzipWebContent(IPath webContentPath, IJavaProject javaProject) throws IOException {
		ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(
				getClass().getResourceAsStream(IGoogleEclipsePluginConstants.SAMPLE_HELLO_WEBCONTENT_ZIP_FILENAME)));
		ZipUtils.unzipToFolder(zipInputStream, ResourceUtils.getFile(webContentPath, javaProject.getProject()));
	}

	private void copyGwtServlet(IJavaProject javaProject, IPath webContentPath, IProgressMonitor monitor)
			throws IOException, CoreException {
		FileUtil.copy(getGwtServletJar(), getGwtServletDestination(javaProject, webContentPath, monitor));
	}

	private FileOutputStream getGwtServletDestination(IJavaProject javaProject, IPath webContentPath,
			IProgressMonitor monitor)
			throws CoreException, FileNotFoundException {
		IPath webInfLibPath = webContentPath.append(new Path(IGoogleEclipsePluginConstants.WEB_INF_LIB));
		IWorkspaceRoot workspaceRoot = javaProject.getProject().getWorkspace().getRoot();
		ResourceUtils.create(workspaceRoot.getFolder(webInfLibPath), monitor);
		IPath gwtServletFilePath = webInfLibPath.append(IGoogleEclipsePluginConstants.GWT_SERVLET_NAME);
		File file = new File(workspaceRoot.getFile(gwtServletFilePath).getLocationURI());
		return new FileOutputStream(file);
	}

	private InputStream getGwtServletJar() throws CoreException, IOException {
		Bundle gwtBundle = getGwtSdkBundle(GWTCoreActivator.getDefault().getBundle().getBundleContext());
		Assert.isTrue(gwtBundle != null,
				MessageFormat.format("GWT SDK bundle was not found. Could not copy {0}",
						IGoogleEclipsePluginConstants.GWT_SERVLET_NAME));
		String gwtSdkVersion = getGwtServletFolder(gwtBundle);
		IPath gwtServletPath = new Path(gwtSdkVersion).append(IGoogleEclipsePluginConstants.GWT_SERVLET_NAME);
		return gwtBundle.getEntry(gwtServletPath.toFile().toString()).openStream();
	}

	private String getGwtServletFolder(Bundle bundle) {
		Version bundleVersion = bundle.getVersion();
		return MessageFormat.format(GWTSERVLETJAR_FOLDER_PATTERN, bundleVersion.getMajor(), bundleVersion.getMinor(),
				bundleVersion.getMicro());
	}

	private Bundle getGwtSdkBundle(BundleContext bundleContext) {
		for (Bundle bundle : bundleContext.getBundles()) {
			if (bundle.getSymbolicName().contains(IGoogleEclipsePluginConstants.GWT_SDK_BUNDLENAME)) {
				return bundle;
			}
		}
		return null;
	}

	/**
	 * Configures the web xml. Adds the gwt servlet, servlet mapping and welcome
	 * page.
	 * 
	 * @param project
	 *            the project to configure the web xml of.
	 * @param monitor
	 *            the monitor to inform on progress
	 */
	protected void configureWebXml(final IProject project, IProgressMonitor monitor) {
		monitor.subTask("configuring web.xml");

		IModelProvider modelProvider = ModelProviderManager.getModelProvider(project);
		IPath webXmlPath = ProjectUtils.getWebXmlPath(project);
		modelProvider.modify(new Runnable() {

			public void run() {
				WebApp webApp = ProjectUtils.getWebApp(project);
				Servlet servlet = createServlet(
						IGoogleEclipsePluginConstants.SERVLET_NAME,
						IGoogleEclipsePluginConstants.SERVLET_CLASS,
						webApp);
				createServletMapping(
						IGoogleEclipsePluginConstants.SERVLET_MAPPING,
						servlet,
						webApp);
				addWelcomePage(
						IGoogleEclipsePluginConstants.WELCOME_FILE,
						webApp);
			}

		}, webXmlPath);
	}

	private void createServletMapping(String urlPattern, Servlet servlet, WebApp webApp) {
		ServletMapping mapping = WebFactory.eINSTANCE.createServletMapping();
		mapping.setServletName(servlet.getServletName());
		UrlPatternType urlPatternType = JavaeeFactory.eINSTANCE.createUrlPatternType();
		urlPatternType.setValue(urlPattern);
		mapping.getUrlPatterns().add(urlPatternType);
		webApp.getServletMappings().add(mapping);
	}

	private Servlet createServlet(String servletName, String servletClass, WebApp webApp) {
		Servlet servlet = WebFactory.eINSTANCE.createServlet();
		servlet.setServletName(servletName);
		servlet.setServletClass(servletClass);
		webApp.getServlets().add(servlet);
		return servlet;
	}

	private void addWelcomePage(String welcomeFileUrl, WebApp webApp) {
		List<WelcomeFileList> welcomeList = webApp.getWelcomeFileLists();
		WelcomeFileList welcomeFileList = null;
		if (welcomeList.isEmpty()) {
			welcomeFileList = WebFactory.eINSTANCE.createWelcomeFileList();
			webApp.getWelcomeFileLists().add(welcomeFileList);
		} else {
			welcomeFileList = (WelcomeFileList) welcomeList.get(0);
		}

		WelcomeFile welcomeFile = WebapplicationFactory.eINSTANCE.createWelcomeFile();
		welcomeFile.setWelcomeFile(welcomeFileUrl);
		welcomeFileList.getWelcomeFiles().add(welcomeFileUrl);

	}
}
