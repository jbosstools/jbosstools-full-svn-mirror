/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.packages.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Plugin;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XMLBinding;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class PackagesCorePlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.ide.eclipse.packages.core";

	// The shared instance
	private static PackagesCorePlugin plugin;
	
	/**
	 * The constructor
	 */
	public PackagesCorePlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		// force JBossXB initialization
		XMLBinding.init();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static boolean isFolderInWorkspace (IPath path)
	{
		boolean inWorkspace = false;
		try {
			IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
			
			inWorkspace = (folder != null && folder.exists());
		} catch (IllegalArgumentException e) {
			// Swallow, we assume this isn't in the workspace if it's an invalid path
		}
		
		return inWorkspace;
	}

	public static boolean isFileInWorkspace (IPath path)
		{
			boolean inWorkspace = false;
			try {
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				
				inWorkspace = (file != null && file.exists());
			} catch (IllegalArgumentException e) {
				// Swallow, we assume this isn't in the workspace if it's an invalid path
			}
			
			return inWorkspace;
		}
	//	protected void loadPackageTypes ()
	//	{
	//		IPackageType[] packageTypes = ExtensionManager.findPackageTypes();
	//		
	//		for (int i = 0; i < packageTypes.length; i++)
	//		{
	//			this.packageTypes.put(packageTypes[i].getId(), packageTypes[i]);
	//		}
	//	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static PackagesCorePlugin getDefault() {
		return plugin;
	}

}
