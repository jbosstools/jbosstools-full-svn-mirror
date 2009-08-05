package org.jboss.ide.eclipse.as.wtp.override.core.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.internal.ModuleFactory;
import org.eclipse.wst.server.core.internal.ServerPlugin;
import org.eclipse.wst.server.core.model.ModuleDelegate;
import org.eclipse.wst.server.core.model.ModuleFactoryDelegate;
import org.eclipse.wst.server.core.util.ProjectModuleFactoryDelegate;
import org.jboss.ide.eclipse.as.wtp.override.core.Activator;

public abstract class JBTProjectModuleFactory extends ProjectModuleFactoryDelegate {
	public static JBTProjectModuleFactory getFactory(String id) {
		ModuleFactory[] factories = ServerPlugin.getModuleFactories();
		for (int i = 0; i < factories.length; i++) {
			if (factories[i].getId().equals(id)) {
				ModuleFactoryDelegate o = factories[i]
						.getDelegate(new NullProgressMonitor());
				return (JBTProjectModuleFactory)o;
			}
		}
		return null;
	}

	
	protected HashMap<IModule, JBTProjectModuleDelegate> moduleToDelegate;
	protected String moduleType;
	protected String facetType;
	public JBTProjectModuleFactory(String moduleType, String facetType) {
		moduleToDelegate = new HashMap<IModule, JBTProjectModuleDelegate>();
		this.moduleType = moduleType;
		this.facetType = facetType;
	}

	@Override
	protected void clearCache(IProject project) {
		super.clearCache(project);
		moduleToDelegate.remove(project);
	}
	
	@Override
	public ModuleDelegate getModuleDelegate(IModule module) {
		return moduleToDelegate.get(module);
	}

	protected IModule[] createModules(IProject project) {
		IFacetedProject facetProject;
		try {
			facetProject = ProjectFacetsManager.create(project);
			if (facetProject == null) {
				return null;
			}
			IProjectFacet facet = ProjectFacetsManager
					.getProjectFacet(facetType);

			if (facetProject.hasProjectFacet(facet)) {
				IProjectFacetVersion version = facetProject.getProjectFacetVersion(facet);
				IModule module = createModule(
						moduleType + "." + project.getName(), 
						project.getName(), 
						moduleType, 
						version.getVersionString(), 
						project);
				JBTProjectModuleDelegate delegate = createDelegate(project);
				moduleToDelegate.put(module, delegate);
				
				createBinaryModules(ComponentCore.createComponent(project), delegate);
				// TODO - create children!!! see JEEDeployableFactory
				return new IModule[] { module };
			}
		} catch (CoreException e) {
			Activator.getDefault().getLog().log(e.getStatus());
		}
		return null;
	}
	
	protected void createBinaryModules(IVirtualComponent component, JBTProjectModuleDelegate delegate) {
		List projectModules = new ArrayList();
		IVirtualReference[] references = component.getReferences();
		for (int i = 0; i < references.length; i++) {
			IVirtualComponent moduleComponent = references[i].getReferencedComponent();
			if (moduleComponent.isBinary()) {		
				if( !delegate.shouldIncludeUtilityComponent(moduleComponent, references, null)) {
					// if we shouldn't include it there, we should do it here
					// TODO do this
				}
			}
		}
	}

	
	protected abstract JBTProjectModuleDelegate createDelegate(IProject project);
	
}
