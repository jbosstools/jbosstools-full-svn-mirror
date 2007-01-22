package org.jboss.ide.eclipse.ejb3.core.facet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.common.project.facet.core.ClasspathHelper;
import org.eclipse.jst.j2ee.internal.common.operations.JavaModelUtil;
import org.eclipse.jst.j2ee.project.facet.IJ2EEFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.J2EEFacetInstallDelegate;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.jboss.ide.eclipse.ejb3.core.classpath.AopFromRuntimeClasspathContainer;
import org.jboss.ide.eclipse.ejb3.core.classpath.EJB3ClasspathContainer;

public class Ejb30FacetPostInstallDelegate extends J2EEFacetInstallDelegate implements IDelegate {

	public void execute(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor) throws CoreException {

		if (monitor != null) {
			monitor.beginTask("", 3); //$NON-NLS-1$
		}
		
		wstEjbPostInstall(project, fv, config, monitor);
		jbossPostInstall(project, fv, config, monitor);
		
		if (monitor != null) {
			monitor.done();
		}
	}
	
	public void wstEjbPostInstall(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor) throws CoreException {
		IDataModel model = (IDataModel) config;

		ComponentCore.createComponent(project);
		
		final String earProjectName = (String) model.getProperty(
					IJ2EEModuleFacetInstallDataModelProperties.EAR_PROJECT_NAME);

		// Associate with an EAR, if necessary.
		if (model.getBooleanProperty(IJ2EEModuleFacetInstallDataModelProperties.ADD_TO_EAR)) {
			if (earProjectName != null && !earProjectName.equals("")) { //$NON-NLS-1$
				String j2eeVersionText = "5.0";
				
				final String moduleURI = model.getStringProperty(
							IJ2EEModuleFacetInstallDataModelProperties.MODULE_URI);

				installAndAddModuletoEAR( j2eeVersionText,
									earProjectName,
									(IRuntime) model.getProperty(IJ2EEFacetInstallDataModelProperties.FACET_RUNTIME),
									project,
									moduleURI,
									monitor );
			}

			// No support for clients
		}

		if (monitor != null) {
			monitor.worked(1);
		}

	}

	public void jbossPostInstall(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor) throws CoreException {
		addClasspathEntries(project, fv, config, monitor);
		addJNDIFile(project);
	}
	
	public void addClasspathEntries(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor) throws CoreException {
		try {
			IFacetedProject facetedProj = ProjectFacetsManager.create(project);
			IRuntime runtime = facetedProj.getPrimaryRuntime();
			String runtimeName = runtime.getName();
			
		    ArrayList list = new ArrayList(); 
		    String runtimeKey = IFacetProjectCreationDataModelProperties.FACET_RUNTIME;
		    if( config instanceof IDataModel ) {
		    	
		    	if( runtime != null && runtime instanceof IRuntime ) {
		    		runtimeName = ((IRuntime)runtime).getName();
					//list.add(JavaCore.newContainerEntry(new Path(AopJdk15ClasspathContainer.CONTAINER_ID)));
				    list.add(JavaCore.newContainerEntry(new Path(EJB3ClasspathContainer.CONTAINER_ID).append(runtimeName)));
				    list.add(JavaCore.newContainerEntry(new Path(AopFromRuntimeClasspathContainer.CONTAINER_ID).append(runtimeName)));
		
					ClasspathHelper.addClasspathEntries(project, fv, list);
		    	}
		    }
		} catch( Exception e ) {
			e.printStackTrace();
		}
	    
		if (monitor != null) {
			monitor.worked(1);
		}
	}
	
	private void printModelProperties(IDataModel model) {
		// diag
		System.out.println("\n\n");
		Collection c = model.getAllProperties();
		Object o;
		for( Iterator i = c.iterator(); i.hasNext();) {
			o = i.next();
			System.out.println(o + " - " + model.getProperty((String)o));
		}

	}
	
	public void addJNDIFile(IProject project) {
		try {
	        String sourcePath = findSourcePaths(project);
	        createJndiProperties(new Path(sourcePath));
		} catch( CoreException ce ) {
		}
	}
	
   private class JndiPropertiesFileFilter implements FileFilter {
	      public boolean accept(File file) {
	         return (file.getName().equals("jndi.properties"));
	      }
   }
   
   private void createJndiProperties(IPath srcPath) throws CoreException {
      IPath jndiPath = srcPath.append("jndi.properties");
      IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(jndiPath);

      String jndiProps = "java.naming.factory.initial=org.jnp.interfaces.NamingContextFactory\n"
            + "java.naming.factory.url.pkgs=org.jboss.naming:org.jnp.interfaces\n"
            + "java.naming.provider.url=localhost:1099\n";

      file.create(new ByteArrayInputStream(jndiProps.getBytes()), true, new NullProgressMonitor());
   }
   private String findSourcePaths(IProject project) throws CoreException {
	   IJavaElement elem = JavaCore.create(project);
	   IPackageFragmentRoot initRoot= null;
	   if (elem != null) {
			initRoot= JavaModelUtil.getPackageFragmentRoot(elem);
			try {
				if (initRoot == null || initRoot.getKind() != IPackageFragmentRoot.K_SOURCE) {
					IJavaProject jproject= elem.getJavaProject();
					if (jproject != null) {
							initRoot= null;
							if (jproject.exists()) {
								IPackageFragmentRoot[] roots= jproject.getPackageFragmentRoots();
								for (int i= 0; i < roots.length; i++) {
									if (roots[i].getKind() == IPackageFragmentRoot.K_SOURCE) {
										initRoot= roots[i];
										break;
									}
								}							
							}
						if (initRoot == null) {
							initRoot= jproject.getPackageFragmentRoot(jproject.getResource());
						}
					}
				}
			} catch (JavaModelException e) {}
		}	
	   return initRoot.getPath().makeRelative().toString();
   }

        
}
