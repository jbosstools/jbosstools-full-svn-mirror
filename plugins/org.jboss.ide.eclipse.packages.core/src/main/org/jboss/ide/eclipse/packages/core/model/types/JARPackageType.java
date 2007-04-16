package org.jboss.ide.eclipse.packages.core.model.types;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageFileSetImpl;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageImpl;

/**
 * The default JAR package type will simply jar-up all the classes in a java project's output directory, and place it in the top-level of the project.
 * The name of the resulting JAR will be the project's name followed by a ".jar" extension.
 * @author Marshall
 */
public class JARPackageType extends AbstractPackageType {

	public static final String TYPE_ID = "jar";
	public IPackage createDefaultConfiguration(IProject project, IProgressMonitor monitor) {
		//IPackageType t = this;
		Assert.isNotNull(project);
		
		IJavaProject javaProject = JavaCore.create(project);
		Assert.isNotNull(javaProject);
		
		if (monitor == null) monitor = new NullProgressMonitor();
		
		monitor.beginTask("Creating default JAR configuration for java project \"" + project.getName() + "\"", 2);
		
		IPath outputPath;
		try {
			outputPath = javaProject.getOutputLocation();
		} catch (JavaModelException e) {
			Trace.trace(JARPackageType.class, e);
			return null;
		}
		
		outputPath = outputPath.removeFirstSegments(1);
		IContainer outputContainer = project.getFolder(outputPath);
		
		IPackage jar = new PackageImpl(); 
		
		jar.setDestinationPath(project.getLocation(), true);
		jar.setExploded(false);
		jar.setName(project.getName() + ".jar");
		//jar.setPackageType(this);
		
		IPackageFileSet classes = new PackageFileSetImpl();
		classes.setIncludesPattern("**/*");
		classes.setSourcePath(outputContainer.getLocation());
		
		jar.addChild(classes);
		
		monitor.worked(1);
		monitor.done();
		
		return jar;
	}
	
	public int getSupportFor(IProject project) {
		
		try {
			if (project.hasNature(JavaCore.NATURE_ID))
			{
				return IPackageType.SUPPORT_FULL;
			}
		} catch (CoreException e) {
			Trace.trace(getClass(), e);
		}
		
		return IPackageType.SUPPORT_NONE;
	}
}
