package org.jboss.ide.eclipse.packages.core.model.types;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.packages.core.model.IPackage;

/**
 * This interface represents a package type (i.e. JAR,WAR,SAR etc).
 * 
 * A package type's main focus right now is to provide a default "template" Package for a given Project, making it easier
 * for users and adopters to automatically adapt projects into a deployable package type.
 * 
 * @author Marshall
 */
public interface IPackageType {

	/**
	 * @return The ID for this PackageType, i.e. "jar", "war" etc
	 */
	public String getId();
	
	/**
	 * @return The label for this PackageType (usually shown in UI)
	 */
	public String getLabel();
	
	/**
	 * This will create a "default" packaging configuration for this project using this package type.
	 * 
	 * If the passed-in project does not support this package type, a null IPackage should be returned.
	 * 
	 * @param project The project to create the packages configuration for
	 * @return The top level package that was created
	 */
	public IPackage createDefaultConfiguration(IProject project, IProgressMonitor monitor);
	
}
