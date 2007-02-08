package org.jboss.ide.eclipse.ejb3.core.module;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
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
		return "jbide.jboss30";
	}

	public IPackage createDefaultConfiguration(IProject project, IProgressMonitor monitor) {
		IModule mod = getModule(project);
		if( mod == null ) 
			return createDefaultConfiguration2(project, monitor);
		else
			return createDefaultConfigFromModule(mod, monitor);
	}
	
	protected IPackage createDefaultConfiguration2(IProject project, IProgressMonitor monitor) {
		IPackage topLevel = createGenericIPackage(project, null, project.getName() + ".jar");
		topLevel.setDestinationContainer(project);
		IPackageFolder metainf = addFolder(project, topLevel, METAINF);
		IPackageFolder lib = addFolder(project, metainf, LIB);
		addFileset(project, metainf, METAINF, null);
		return topLevel;
	}

	protected IPackage createDefaultConfigFromModule(IModule mod, IProgressMonitor monitor) {
		try {
			IProject project = mod.getProject();

			IPackage topLevel = createGenericIPackage(project, null, project.getName() + ".jar");
			topLevel.setDestinationContainer(project);
			IPackageFolder metainf = addFolder(project, topLevel, METAINF);
			IPackageFolder lib = addFolder(project, metainf, LIB);
			addFileset(project, metainf, EJBMODULE + Path.SEPARATOR + METAINF, null);

			return topLevel;
		} catch( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}

}
