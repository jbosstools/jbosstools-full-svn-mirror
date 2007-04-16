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
	 * Represents full support for a project
	 */
	public static final int SUPPORT_FULL = 0;
	
	/**
	 * Represents no support for a project
	 */
	public static final int SUPPORT_NONE = 1;
	
	/**
	 * Represents conditional support for a project
	 */
	public static final int SUPPORT_CONDITIONAL = 2;
	
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
	
	/**
	 * This will return the type of support this package type has for the passed in project.
	 * The type of support can be FULL (should be no problems), NONE (this project is not supported),
	 *  or CONDITIONAL (there might be some user input needed before the project supports this package type)
	 * @param project The project to check
	 * @return The support type this package type has for the project
	 */
	public int getSupportFor (IProject project);
}
