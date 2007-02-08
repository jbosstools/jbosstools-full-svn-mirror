/**
 * JBoss, a Division of Red Hat
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
package org.jboss.ide.eclipse.ejb3.core.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.logger.proxy.Logger;
import org.eclipse.jst.j2ee.internal.deployables.J2EEFlexProjDeployable;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.componentcore.internal.StructureEdit;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.model.ModuleDelegate;
import org.eclipse.wst.server.core.util.ProjectModuleFactoryDelegate;
import org.jboss.ide.eclipse.as.core.JBossServerCorePlugin;
import org.jboss.ide.eclipse.ejb3.core.EJB3WizardsCorePlugin;

/**
 *
 * @author rob.stryker@jboss.com
 */
public class Ejb30ModuleFactory extends ProjectModuleFactoryDelegate {
	protected Map moduleDelegates = new HashMap(5);
	
	public static final String ID = "org.jboss.ide.eclipse.ejb3.core.ejb3Factory"; //$NON-NLS-1$
	public static final String EJB30_TYPE = "jbide.ejb30";

	protected IModule[] createModules(IProject project) {
		try {
			ModuleCoreNature nature = (ModuleCoreNature) project.getNature(IModuleConstants.MODULE_NATURE_ID);
			if (nature != null)
				return createModules(nature);
		} catch (CoreException e) {
			Logger.getLogger().write(e);
		}
		return null;
	}

	protected IModule[] createModules(ModuleCoreNature nature) {
		IProject project = nature.getProject();
		try {
			IVirtualComponent comp = ComponentCore.createComponent(project);
			return createModuleDelegates(comp);
		} catch (Exception e) {
			Logger.getLogger().write(e);
		}
		return null;
	}

	public ModuleDelegate getModuleDelegate(IModule module) {
		return (ModuleDelegate) moduleDelegates.get(module);
	}

	protected IModule[] createModuleDelegates(IVirtualComponent component) {
		List projectModules = new ArrayList();
		try {
			IModule module = null;
			if( isProjectEjb30(component.getProject())) {
				String version = getProjectEjb30Version(component.getProject());
				module = createModule(component.getDeployedName(), component.getDeployedName(), EJB30_TYPE, version, component.getProject());

				Object moduleDelegate = new J2EEFlexProjDeployable(component.getProject());

				moduleDelegates.put(module, moduleDelegate);
				projectModules.add(module);
			}
		} catch (Exception e) {
			Logger.getLogger().write(e);
		}
		return (IModule[]) projectModules.toArray(new IModule[projectModules.size()]);
	}
	

	public static boolean isProjectEjb30(IProject project) {
		IFacetedProject facetedProject = null;
		try {
			facetedProject = ProjectFacetsManager.create(project);
		} catch (CoreException e) {
			return false;
		}

		if (facetedProject != null && ProjectFacetsManager.isProjectFacetDefined(EJB30_TYPE)) {
			IProjectFacet projectFacet = ProjectFacetsManager.getProjectFacet(EJB30_TYPE);
			return projectFacet != null && facetedProject.hasProjectFacet(projectFacet);
		}
		return false;
	}
	
	public static String getProjectEjb30Version(IProject project) {
		IFacetedProject facetedProject = null;
		IProjectFacet facet = null;
		try {
			facetedProject = ProjectFacetsManager.create(project);
			facet = ProjectFacetsManager.getProjectFacet(EJB30_TYPE);
		} catch (Exception e) {
			// Not Faceted project or not J2EE Project
		}
		if (facet != null && facetedProject.hasProjectFacet(facet))
			return facetedProject.getInstalledVersion(facet).getVersionString();
		return null;
	}
	

	/**
	 * Returns the list of resources that the module should listen to
	 * for state changes. The paths should be project relative paths.
	 * Subclasses can override this method to provide the paths.
	 *
	 * @return a possibly empty array of paths
	 */
	protected IPath[] getListenerPaths() {
		return new IPath[] {
			new Path(".project"), // nature
			new Path(StructureEdit.MODULE_META_FILE_NAME), // component
			new Path(".settings/org.eclipse.wst.common.project.facet.core.xml") // facets
		};
	}

	protected void clearCache() {
		moduleDelegates = new HashMap(5);
	}
}