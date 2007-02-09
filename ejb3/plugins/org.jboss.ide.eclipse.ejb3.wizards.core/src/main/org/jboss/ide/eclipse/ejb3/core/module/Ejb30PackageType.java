package org.jboss.ide.eclipse.ejb3.core.module;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.server.core.IWebModule;
import org.eclipse.wst.server.core.IModule;
import org.jboss.ide.eclipse.as.core.packages.ObscurelyNamedPackageTypeSuperclass;
import org.jboss.ide.eclipse.as.core.packages.WarPackageType;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;

public class Ejb30PackageType extends ObscurelyNamedPackageTypeSuperclass {

	public static final String ID = "org.jboss.ide.eclipse.ejb3.wizards.core.ejbPackageType";
	public String getAssociatedModuleType() {
		return "jbide.ejb30";
	}

	public IPackage createDefaultConfiguration(IProject project, IProgressMonitor monitor) {
		boolean facetFound = J2EEProjectUtilities.isProjectOfType(project, getAssociatedModuleType()); 
		return createDefaultConfiguration(project, facetFound, monitor);
	}
	
	public IPackage createDefaultConfiguration(IProject project, boolean facetFound, IProgressMonitor monitor) {
		String metaInfDir = (facetFound ? EJBMODULE + Path.SEPARATOR : "") + METAINF;
		
		IPackage topLevel = createGenericIPackage(project, null, project.getName() + ".jar");
		topLevel.setDestinationContainer(project);
		IPackageFolder metainf = addFolder(project, topLevel, METAINF);
		IPackageFolder lib = addFolder(project, metainf, LIB);
		addFileset(project, metainf, metaInfDir, null);
		return topLevel;		
	}
}
