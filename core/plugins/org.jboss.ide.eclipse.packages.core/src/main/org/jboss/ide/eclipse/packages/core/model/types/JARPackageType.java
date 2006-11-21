package org.jboss.ide.eclipse.packages.core.model.types;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSetWorkingCopy;
import org.jboss.ide.eclipse.packages.core.model.IPackageWorkingCopy;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;

/**
 * The default JAR package type will simply jar-up all the classes in a project's output directory, and place it in the top-level of the project.
 * The name of the resulting JAR will be the project's name followed by a ".jar" extension.
 * @author Marshall
 */
public class JARPackageType extends AbstractPackageType {

	public static final String TYPE_ID = "jar";

	public IPackage createDefaultConfiguration(IProject project, IProgressMonitor monitor)
	{
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
		
		IContainer outputContainer = ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(outputPath);
		
		IPackage jar = PackagesCore.createPackage(project, true);
		IPackageWorkingCopy jarWC = jar.createPackageWorkingCopy();
		
		jarWC.setDestinationContainer(project);
		jarWC.setExploded(false);
		jarWC.setName(project.getName() + ".jar");
		jarWC.setPackageType(this);
		jar = jarWC.savePackage();
		
		IPackageFileSet classes = PackagesCore.createPackageFileSet(project);
		IPackageFileSetWorkingCopy classesWC = classes.createFileSetWorkingCopy();
		classesWC.setIncludesPattern("**/*");
		classesWC.setSourceContainer(outputContainer);
		classes = classesWC.saveFileSet();
		jar.addChild(classes);
		
		monitor.worked(1);
		monitor.done();
		
		return jar;
	}
}
