/*************************************************************************************
 * Copyright (c) 2008 JBoss, a division of Red Hat and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss, a division of Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.portlet.core.internal;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.wst.server.core.IRuntime;
import org.jboss.ide.eclipse.as.classpath.core.jee.AbstractClasspathContainer;
import org.jboss.ide.eclipse.as.classpath.core.jee.AbstractClasspathContainerInitializer;
import org.jboss.ide.eclipse.as.classpath.core.xpl.ClasspathDecorations;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.Messages;
import org.jboss.tools.portlet.core.PortletCoreActivator;

/**
 * @author snjeza
 * 
 */
public class PortletRuntimeLibrariesContainerInitializer extends
		AbstractClasspathContainerInitializer {

	private IJavaProject project;

	@Override
	public void initialize(IPath containerPath, IJavaProject project)
			throws CoreException {
		this.project = project;
		super.initialize(containerPath, project);
	}

	public String getDescription(IPath containerPath, IJavaProject project) {
		return Messages.PortletLibrariesContainerInitializer_JBoss_Portlet_Classpath_Container_Initializer;
	}

	@Override
	protected AbstractClasspathContainer createClasspathContainer(IPath path) {
		return new PortletRuntimeClasspathContainer(path, project);
	}

	@Override
	protected String getClasspathContainerID() {
		return IPortletConstants.PORTLET_RUNTIME_CONTAINER_ID;
	}

	public class PortletRuntimeClasspathContainer extends
			BasePortletClasspathContainer {

		public PortletRuntimeClasspathContainer(IPath path, IJavaProject project) {
			super(
					project,
					path,
					Messages.PortletLibrariesContainerInitializer_JBoss_Portlet_Library,
					SUFFIX);
		}

		@Override
		protected IClasspathEntry[] computeEntries() {
			ArrayList<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();

			IJavaProject javaProject = getProject();

			if (javaProject != null) {
				IProject project = javaProject.getProject();
				if (project != null) {
					try {
						IRuntime runtime = J2EEProjectUtilities
								.getServerRuntime(project);
						if (runtime != null) {
							File location = runtime.getLocation().toFile();
							File libDir = getLibDirectory(location);
							if (libDir != null) {
								File[] jars = libDir
										.listFiles(new FileFilter() {
											public boolean accept(File file) {
												String name = file.getName();
												return (name.startsWith(IPortletConstants.PORTLET_API) && name
														.endsWith(IPortletConstants.JAR));
											}
										});

								if (jars != null) {
									for (int i = 0; i < jars.length; i++) {
										File jarFile = jars[i];

										IPath entryPath = new Path(jarFile
												.toString());

										IPath sourceAttachementPath = null;
										IPath sourceAttachementRootPath = null;

										final ClasspathDecorations dec = decorations
												.getDecorations(
														getDecorationManagerKey(getPath()
																.toString()),
														entryPath.toString());

										IClasspathAttribute[] attrs = {};
										if (dec != null) {
											sourceAttachementPath = dec
													.getSourceAttachmentPath();
											sourceAttachementRootPath = dec
													.getSourceAttachmentRootPath();
											attrs = dec.getExtraAttributes();
										}

										IAccessRule[] access = {};
										IClasspathEntry entry = JavaCore
												.newLibraryEntry(
														entryPath,
														sourceAttachementPath,
														sourceAttachementRootPath,
														access, attrs, false);
										entries.add(entry);
									}
								}
							}
						}
					} catch (CoreException e) {
						PortletCoreActivator.log(e);
					}
				}
			}
			return entries.toArray(new IClasspathEntry[entries.size()]);
		}

		private File getLibDirectory(File location) {
			File libDirectory = getDirectory(location,
					IPortletConstants.SERVER_DEFAULT_DEPLOY_JBOSS_PORTAL_SAR);
			if (libDirectory != null) {
				libDirectory = new File(libDirectory, "lib"); //$NON-NLS-1$
			} else {
				libDirectory = getDirectory(location,
						IPortletConstants.SERVER_DEFAULT_DEPLOY_SIMPLE_PORTAL);
				if (libDirectory != null) {
					libDirectory = new File(libDirectory, "lib"); //$NON-NLS-1$
				} else {
					// Tomcat adds portlet-api.jat automatically
					/*File tomcatLib = new File(location,
							IPortletConstants.TOMCAT_LIB);
					if (tomcatLib != null && tomcatLib.isDirectory()) {
						libDirectory = tomcatLib;
					}*/
				}
			}
			return libDirectory;
		}

		private File getDirectory(File location, String portalDir) {
			if (Platform.getOS().equals(Platform.OS_WIN32)) {
				portalDir = portalDir.replace("/", "\\"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			File file = new File(location, portalDir);
			if (file.exists() && file.isDirectory()) {
				return file;
			}
			return null;
		}
	}
}
