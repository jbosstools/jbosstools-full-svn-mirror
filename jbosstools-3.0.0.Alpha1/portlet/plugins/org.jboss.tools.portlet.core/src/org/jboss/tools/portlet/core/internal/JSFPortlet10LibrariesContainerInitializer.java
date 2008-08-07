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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.ide.eclipse.as.classpath.core.jee.AbstractClasspathContainer;
import org.jboss.ide.eclipse.as.classpath.core.jee.AbstractClasspathContainerInitializer;
import org.jboss.ide.eclipse.as.classpath.core.xpl.ClasspathDecorations;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.PortletCoreActivator;

/**
 * @author snjeza
 * 
 */
public class JSFPortlet10LibrariesContainerInitializer extends
		AbstractClasspathContainerInitializer {

	public String getDescription(IPath containerPath, IJavaProject project) {
		return "JBoss JSF Portlet Classpath Container Initializer v1.0";
	}

	@Override
	protected AbstractClasspathContainer createClasspathContainer(IPath path) {
		return new JSFPortlet10ClasspathContainer(path);
	}

	@Override
	protected String getClasspathContainerID() {
		return IPortletConstants.JSFPORTLET_CONTAINER_10_ID;
	}

	private class JSFPortlet10ClasspathContainer extends AbstractClasspathContainer {
		
		public final static String SUFFIX = PortletCoreActivator.JSFPORTLET_FOLDER;//$NON-NLS-1$
		public final static String PREFIX = "org.jboss.tools.portlet.core";
		public final static String DESCRIPTION = "JBoss JSF Portlet Libraries v1.0";

		public JSFPortlet10ClasspathContainer(IPath path) {
			super(path, DESCRIPTION, SUFFIX);
		}

		@Override
		protected String getBaseDir() {
			try {
				URL installURL = FileLocator.toFileURL(PortletCoreActivator
						.getDefault().getBundle().getEntry("/"));
				return installURL.getFile().toString();
			} catch (IOException e) {
				PortletCoreActivator
						.log(e, "Error loading classpath container");
			}
			return null;
		}
		
		@Override
		protected IClasspathEntry[] computeEntries() {
			ArrayList<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();

			String baseDir = getBaseDir();
			if (baseDir == null)
				return new IClasspathEntry[0];

			File libDir = new File(baseDir
					+ "/" + PortletCoreActivator.RESOURCES_FOLDER + "/" + PortletCoreActivator.JSFPORTLET_FOLDER);//$NON-NLS-1$ //$NON-NLS-2$

			File[] jars = libDir.listFiles(new FileFilter() {
				public boolean accept(File file) {
					return (file.toString().endsWith(".jar"));//$NON-NLS-1$
				}
			});

			if (jars != null) {
				for (int i = 0; i < jars.length; i++) {
					File jarFile = jars[i];

					IPath entryPath = new Path(jarFile.toString());

					IPath sourceAttachementPath = null;
					IPath sourceAttachementRootPath = null;

					final ClasspathDecorations dec = decorations
							.getDecorations(getDecorationManagerKey(getPath()
									.toString()), entryPath.toString());

					IClasspathAttribute[] attrs = {};
					if (dec != null) {
						sourceAttachementPath = dec.getSourceAttachmentPath();
						sourceAttachementRootPath = dec
								.getSourceAttachmentRootPath();
						attrs = dec.getExtraAttributes();
					}

					IAccessRule[] access = {};
					IClasspathEntry entry = JavaCore.newLibraryEntry(entryPath,
							sourceAttachementPath, sourceAttachementRootPath,
							access, attrs, false);
					entries.add(entry);
				}
			}

			return entries.toArray(new IClasspathEntry[entries.size()]);
		}
	}
}
