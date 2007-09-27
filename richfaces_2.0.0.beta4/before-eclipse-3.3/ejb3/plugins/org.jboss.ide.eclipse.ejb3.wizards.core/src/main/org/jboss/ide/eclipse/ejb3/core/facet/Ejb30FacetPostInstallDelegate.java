package org.jboss.ide.eclipse.ejb3.core.facet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
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
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jst.j2ee.application.Module;
import org.eclipse.jst.j2ee.application.internal.impl.EjbModuleImpl;
import org.eclipse.jst.j2ee.application.internal.operations.AddComponentToEnterpriseApplicationDataModelProvider;
import org.eclipse.jst.j2ee.application.internal.operations.AddComponentToEnterpriseApplicationOp;
import org.eclipse.jst.j2ee.application.internal.operations.IAddComponentToEnterpriseApplicationDataModelProperties;
import org.eclipse.jst.j2ee.internal.common.operations.JavaModelUtil;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.j2ee.project.facet.IJ2EEFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.J2EEFacetInstallDelegate;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.datamodel.properties.ICreateReferenceComponentsDataModelProperties;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;

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

    protected void addToEar(IVirtualComponent earComp, IVirtualComponent j2eeComp, String moduleURI ){
		final IDataModel dataModel = DataModelFactory.createDataModel(new AddEjb30ToEnterpriseApplicationDataModelProvider());
		Map map = (Map)dataModel.getProperty(IAddComponentToEnterpriseApplicationDataModelProperties.TARGET_COMPONENTS_TO_URI_MAP);
		map.put(j2eeComp, moduleURI);
		
		dataModel.setProperty(ICreateReferenceComponentsDataModelProperties.SOURCE_COMPONENT, earComp);
			
		List modList = (List) dataModel.getProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST);
		modList.add(j2eeComp);
		dataModel.setProperty(ICreateReferenceComponentsDataModelProperties.TARGET_COMPONENT_LIST, modList);
		try {
			dataModel.getDefaultOperation().execute(null, null);
		} catch (ExecutionException e) {
			Logger.getLogger().logError(e);
		}
    }
	protected class AddEjb30ToEnterpriseApplicationDataModelProvider extends AddComponentToEnterpriseApplicationDataModelProvider {
		public IDataModelOperation getDefaultOperation() {
			return new AddEjb30ToEnterpriseApplicationOp(model);
		}
	}
	
	public class AddEjb30ToEnterpriseApplicationOp extends AddComponentToEnterpriseApplicationOp {
		public AddEjb30ToEnterpriseApplicationOp(IDataModel model) {
			super(model);
		}
		protected Module createNewModule(IVirtualComponent wc) {
			IProject p = wc.getProject();
			if( J2EEProjectUtilities.isProjectOfType(p, "jbide.ejb30"))
				return new EjbModuleImpl();
			return null;
		}

		
	}
	
	public void jbossPostInstall(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor) throws CoreException {
		addClasspathEntries(project, fv, config, monitor);
		addJNDIFile(project);
		addMetaInfFolder(project);
	}
	
	public void addClasspathEntries(IProject project, IProjectFacetVersion fv, Object config, IProgressMonitor monitor) throws CoreException {
//		try {
//			IFacetedProject facetedProj = ProjectFacetsManager.create(project);
//			IRuntime runtime = facetedProj.getPrimaryRuntime();
//			String runtimeName = runtime.getName();
//			
//		    ArrayList list = new ArrayList(); 
//		    String runtimeKey = IFacetProjectCreationDataModelProperties.FACET_RUNTIME;
//		    if( config instanceof IDataModel ) {
//		    	
//		    	if( runtime != null && runtime instanceof IRuntime ) {
//		    		runtimeName = ((IRuntime)runtime).getName();
//					//list.add(JavaCore.newContainerEntry(new Path(AopJdk15ClasspathContainer.CONTAINER_ID)));
//				    list.add(JavaCore.newContainerEntry(new Path(EJB3ClasspathContainer.CONTAINER_ID).append(runtimeName)));
//				    list.add(JavaCore.newContainerEntry(new Path(AopFromRuntimeClasspathContainer.CONTAINER_ID).append(runtimeName)));
//		
//					ClasspathHelper.addClasspathEntries(project, fv, list);
//		    	}
//		    }
//		} catch( Exception e ) {
//			e.printStackTrace();
//		}
//	    
//		if (monitor != null) {
//			monitor.worked(1);
//		}
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
	        Path sourcePath = new Path(findSourcePaths(project));
	        IPath jndiPath = sourcePath.append("jndi.properties");
	        IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(jndiPath);

	        String jndiProps = "java.naming.factory.initial=org.jnp.interfaces.NamingContextFactory\n"
	              + "java.naming.factory.url.pkgs=org.jboss.naming:org.jnp.interfaces\n"
	              + "java.naming.provider.url=localhost:1099\n";

	        file.create(new ByteArrayInputStream(jndiProps.getBytes()), true, new NullProgressMonitor());
		} catch( CoreException ce ) {
		}
	}
	
	protected void addMetaInfFolder(IProject project) {
		try {
	        Path sourcePath = new Path(findSourcePaths(project));
	        IPath metainfPath = sourcePath.append("META-INF");
	        IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(metainfPath);
	        folder.create(true, true, new NullProgressMonitor());
		} catch( CoreException ce) {
		}
	}
	
   private class JndiPropertiesFileFilter implements FileFilter {
	      public boolean accept(File file) {
	         return (file.getName().equals("jndi.properties"));
	      }
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
