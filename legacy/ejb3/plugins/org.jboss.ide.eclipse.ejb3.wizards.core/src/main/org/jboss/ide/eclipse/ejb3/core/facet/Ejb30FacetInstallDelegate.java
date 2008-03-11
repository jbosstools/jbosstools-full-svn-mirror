package org.jboss.ide.eclipse.ejb3.core.facet;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jst.common.project.facet.WtpUtils;
import org.eclipse.jst.common.project.facet.core.ClasspathHelper;
import org.eclipse.jst.j2ee.ejb.componentcore.util.EJBArtifactEdit;
import org.eclipse.jst.j2ee.internal.J2EEConstants;
import org.eclipse.jst.j2ee.internal.common.J2EEVersionUtil;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.J2EEFacetInstallDelegate;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.datamodel.FacetDataModelProvider;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.project.facet.IProductConstants;
import org.eclipse.wst.project.facet.ProductManager;

public class Ejb30FacetInstallDelegate extends J2EEFacetInstallDelegate implements IDelegate {

	public void execute(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor) throws CoreException {

		
		if (monitor != null) {
			monitor.beginTask("", 1); //$NON-NLS-1$
		}

		try {
			
			// add java nature first
			addProjectNature(project, JavaCore.NATURE_ID);

			IDataModel model = (IDataModel) config;

			final IJavaProject jproj = JavaCore.create(project);

			// Add WTP natures.
			WtpUtils.addNatures(project);

			// Create the directory structure.
			final IWorkspace ws = ResourcesPlugin.getWorkspace();
			final IPath pjpath = project.getFullPath();

			// Setup the flexible project structure.
			final IVirtualComponent c = ComponentCore.createComponent(project);
			c.create(0, null);
			c.setMetaProperty("java-output-path", ProductManager.getProperty(IProductConstants.OUTPUT_FOLDER)); //$NON-NLS-1$

			final IVirtualFolder ejbroot = c.getRootFolder();

			IFolder ejbFolder = null;
			String configFolder = null;

			configFolder = model.getStringProperty(IJ2EEModuleFacetInstallDataModelProperties.CONFIG_FOLDER);
			ejbroot.createLink(new Path("/" + configFolder), 0, null); //$NON-NLS-1$

			String ejbFolderName = model.getStringProperty(IJ2EEModuleFacetInstallDataModelProperties.CONFIG_FOLDER);
			IPath ejbFolderpath = pjpath.append(ejbFolderName);

			ejbFolder = ws.getRoot().getFolder(ejbFolderpath);

			
			// add source folder maps
			final IClasspathEntry[] cp = jproj.getRawClasspath();
			for (int i = 0; i < cp.length; i++) {
				final IClasspathEntry cpe = cp[i];
				if (cpe.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					ejbroot.createLink(cpe.getPath().removeFirstSegments(1), 0, null);
				}
			}

			// Setup the classpath.
			ClasspathHelper.removeClasspathEntries(project, fv);

			if (!ClasspathHelper.addClasspathEntries(project, fv)) {
				// TODO: Support the no runtime case.
				// ClasspathHelper.addClasspathEntries( project, fv, <something> );
			}
			
			try {
				((IDataModelOperation) model.getProperty(FacetDataModelProvider.NOTIFICATION_OPERATION)).execute(monitor, null);
			} catch (ExecutionException e) {
				Logger.getLogger().logError(e);
			}
			
			if (monitor != null) {
				monitor.worked(1);
			}
		}

		finally {
			if (monitor != null) {
				monitor.done();
			}
		}
	}
	
	

	   /**
	    * Add the given project nature to the given project (if it isn't already added).
	    */
	   public boolean addProjectNature(IProject project, String natureId) {
	      boolean added = false;
	      try {
	         if (project != null && natureId != null) {
	            IProjectDescription desc = project.getDescription();

	            if (!project.hasNature(natureId)) {
	               String natureIds[] = desc.getNatureIds();
	               String newNatureIds[] = new String[natureIds.length + 1];

	               System.arraycopy(natureIds, 0, newNatureIds, 1, natureIds.length);
	               newNatureIds[0] = natureId;
	               desc.setNatureIds(newNatureIds);

	               project.getProject().setDescription(desc, new NullProgressMonitor());
	               added = true;
	            }
	         }
	      }
	      catch (CoreException e) {
	         e.printStackTrace();
	      }
	      return added;
	   }
	
	
}

