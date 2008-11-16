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
import org.eclipse.jdt.core.JavaCore;
import org.jboss.ide.eclipse.as.classpath.core.jee.AbstractClasspathContainer;
import org.jboss.ide.eclipse.as.classpath.core.xpl.ClasspathDecorations;
import org.jboss.tools.portlet.core.Messages;
import org.jboss.tools.portlet.core.PortletCoreActivator;

public abstract class BasePortletClasspathContainer extends AbstractClasspathContainer {
		private static final String PORTLET_FOLDER = "portlet"; //$NON-NLS-1$
		public final static String SUFFIX = PORTLET_FOLDER;//$NON-NLS-1$
		public final static String PREFIX = "org.jboss.tools.portlet.core"; //$NON-NLS-1$
		private static final String RESOURCES_FOLDER = "resources"; //$NON-NLS-1$

		public BasePortletClasspathContainer(IPath path, String description,String suffix) {
			super(path, description, suffix);
		}

		@Override
		protected String getBaseDir() {
			try {
				URL installURL = FileLocator.toFileURL(PortletCoreActivator
						.getDefault().getBundle().getEntry("/")); //$NON-NLS-1$
				return installURL.getFile().toString();
			} catch (IOException e) {
				PortletCoreActivator
						.log(e, Messages.BasePortletClasspathContainer_Error_loading_classpath_container);
			}
			return null;
		}

		@Override
		protected IClasspathEntry[] computeEntries() {
			ArrayList<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();

			String baseDir = getBaseDir();
			if (baseDir == null)
				return new IClasspathEntry[0];

			String version = getPortletVersion();
			File libDir = new File(baseDir
					+ "/" + RESOURCES_FOLDER + "/" + PORTLET_FOLDER + "/" + version);//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

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

		protected abstract String getPortletVersion();

}
