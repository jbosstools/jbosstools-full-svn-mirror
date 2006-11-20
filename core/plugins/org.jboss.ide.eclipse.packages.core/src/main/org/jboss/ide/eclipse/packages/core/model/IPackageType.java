package org.jboss.ide.eclipse.packages.core.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * This interface represents a package type (i.e. JAR,WAR,SAR etc).
 * 
 * A package type's main focus right now is to provide a default "template" Package for a given Project, making it easier
 * for users and adopters to automatically adapt projects into a deployable package type.
 * 
 * @author Marshall
 */
public interface IPackageType {

	public static final String ID_JAR = "jar";
	public static final String ID_WAR = "war";
	public static final String ID_EAR = "ear";
	public static final String ID_SAR = "sar";
	public static final String ID_EJB_JAR = "ejb-jar";

	/**
	 * @return The ID for this PackageType, i.e. "jar", "war" etc
	 */
	public String getId();
	
	/**
	 * This will create a "default" packaging configuration for this project using this package type
	 * @param project The project to create the packages configuration for
	 * @return The top level package that was created
	 */
	public IPackage createDefaultConfiguration(IProject project, IProgressMonitor monitor);
	
}
