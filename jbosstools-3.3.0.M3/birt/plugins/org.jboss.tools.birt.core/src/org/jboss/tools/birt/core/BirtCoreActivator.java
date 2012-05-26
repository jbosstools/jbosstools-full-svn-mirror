package org.jboss.tools.birt.core;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class BirtCoreActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.birt.core"; //$NON-NLS-1$

	public static final IOverwriteQuery OVERWRITE_ALL_QUERY = new IOverwriteQuery()
    {
      public String queryOverwrite(String pathString)
      {
        return IOverwriteQuery.ALL;
      }
    };
	// The shared instance
	private static BirtCoreActivator plugin;
	
	// The facet id
	public static final String JBOSS_BIRT__FACET_ID = "jboss.birt"; //$NON-NLS-1$

	public static final String BIRT_FACET_ID = "birt.runtime"; //$NON-NLS-1$

	public static final String SEAM_FACET_ID = "jst.seam"; //$NON-NLS-1$

	
	/**
	 * The constructor
	 */
	public BirtCoreActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static BirtCoreActivator getDefault() {
		return plugin;
	}

	public static void copyPlugin(IProject project,String pluginId, String destination, IProgressMonitor monitor) {
		IResource destResource = project.findMember(destination);
		if (!destResource.exists()) {
			IStatus status = new Status(IStatus.WARNING,BirtCoreActivator.PLUGIN_ID,NLS.bind(Messages.BirtCoreActivator_The_folder_doesnt_exists, destination));
			BirtCoreActivator.getDefault().getLog().log(status);
			return;
		}
		if (destResource.getType() != IResource.FOLDER ) {
			IStatus status = new Status(IStatus.WARNING,BirtCoreActivator.PLUGIN_ID,NLS.bind(Messages.BirtCoreActivator_The_resource_is_not_folder, destination));
			BirtCoreActivator.getDefault().getLog().log(status);
			return;
		}
		try {
			IFolder destFolder = (IFolder) destResource;
			Bundle bundle = Platform.getBundle(pluginId);
			File bundleFile = FileLocator.getBundleFile(bundle);
			IFolder folder = null;
			File file = null;
			List<File> filesToImport = new ArrayList<File>();
			if (bundleFile.isDirectory()) {
				URL url = bundle.getEntry("/"); //$NON-NLS-1$
				String fileName = FileLocator.toFileURL(url).getFile();
				file = new File(fileName);
				filesToImport.addAll(Arrays.asList(file.listFiles()));
				String name = file.getName();
				folder = destFolder.getFolder(new Path(name));
				if (folder.exists()) {
					return;
				}
				folder.create(true, true, monitor);
			} else {
				filesToImport.add(bundleFile);
				file = bundleFile.getParentFile();
				folder = destFolder;
				String outputFileName = bundleFile.getName();
				IFile outputFile = folder.getFile(outputFileName);
				if (outputFile.exists()) {
					return;
				}
			}
			ImportOperation importOperation = new ImportOperation(folder.getFullPath(),
					file, FileSystemStructureProvider.INSTANCE,
					OVERWRITE_ALL_QUERY, filesToImport);
			importOperation.setCreateContainerStructure(false);
			importOperation.run(monitor);
		} catch (Exception e) {
			IStatus status = new Status(IStatus.WARNING,BirtCoreActivator.PLUGIN_ID,e.getMessage());
			BirtCoreActivator.getDefault().getLog().log(status);
			return;
		}
		
	}

}
