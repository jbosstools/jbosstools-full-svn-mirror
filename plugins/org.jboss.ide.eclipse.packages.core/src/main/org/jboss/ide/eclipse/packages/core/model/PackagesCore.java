package org.jboss.ide.eclipse.packages.core.model;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.packages.core.ExtensionManager;
import org.jboss.ide.eclipse.packages.core.build.ModelChangeListener;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.core.model.types.IPackageType;

public class PackagesCore {
	private static PackagesCore instance;
	public static PackagesCore getInstance() {
		if( instance == null ) 
			instance = new PackagesCore();
		return instance;
	}
	
	private ArrayList buildListeners;
	private ArrayList modelListeners;
	public PackagesCore() {
		buildListeners = new ArrayList();
		modelListeners = new ArrayList();
		addModelListener(new ModelChangeListener());
	}
	
	public void addBuildListener(IPackagesBuildListener listener) {
		if( !buildListeners.contains(listener)) 
			buildListeners.add(listener);
	}
	public void removeBuildListener(IPackagesBuildListener listener) {
		if( buildListeners.contains(listener)) 
			buildListeners.remove(listener);
	}
	public IPackagesBuildListener[] getBuildListeners() {
		return (IPackagesBuildListener[]) buildListeners.toArray(new IPackagesBuildListener[buildListeners.size()]);
	}
	
	public void addModelListener(IPackagesModelListener listener) {
		if( !modelListeners.contains(listener)) 
			modelListeners.add(listener);
	}
	public void removeModelListener(IPackagesModelListener listener) {
		if( modelListeners.contains(listener)) 
			modelListeners.remove(listener);
	}
	public IPackagesModelListener[] getModelListeners() {
		return (IPackagesModelListener[]) modelListeners.toArray(new IPackagesModelListener[modelListeners.size()]);
	}
	
	
	
	
	
	
	/**
	 * Builds all of a project's packages  (performs a FULL_BUILD)
	 * @param project The project to build
	 * @param monitor A progress monitor
	 */
	public static void buildProject (IProject project, IProgressMonitor monitor) {
		buildProject(project, IncrementalProjectBuilder.FULL_BUILD, monitor);
	}
	
	/**
	 * Builds all of a project's packages. Note that this does not call any builders before or after the package builder (i.e. the JDT builder).
	 * If you are looking to run all the builders on a project use project.build()
	 * @param project The project to build
	 * @param buildType FULL_BUILD, INCREMENTAL_BUILD, CLEAN_BUILD, etc
	 * @param monitor A progress monitor
	 */
	public static void buildProject (IProject project, int buildType, IProgressMonitor monitor) {
		if (monitor == null) monitor = new NullProgressMonitor();
		
//		PackageBuildDelegate delegate = PackageBuildDelegate.instance();
//		delegate.setProject(project);
//		try {
//			delegate.build(buildType, null, monitor);
//		} catch (CoreException e) {
//			Trace.trace(PackagesCore.class, e);
//		}
	}
	
	/**
	 * Build the passed-in package.
	 * @param pkg The package to build
	 */
	public static void buildPackage (IPackage pkg, IProgressMonitor monitor) {
//		if (monitor == null) monitor = new NullProgressMonitor();
//
//		PackageBuildDelegate delegate = PackageBuildDelegate.instance();
//		delegate.setProject(pkg.getProject());
//		
//		delegate.buildSinglePackage(pkg, monitor);
	}

	public static IPackage[] getAllProjectPackages(IProgressMonitor monitor) {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		ArrayList results = new ArrayList();
		for( int i = 0; i < projects.length; i++ ) {
			if( projects[i].isAccessible()) {
				results.addAll(Arrays.asList(getProjectPackages(projects[i], monitor, true)));
			}
		}
		return (IPackage[]) results.toArray(new IPackage[results.size()]);
	}
	
	public static IPackage[] getProjectPackages (IProject project, IProgressMonitor monitor, boolean forceInit) {
		if (monitor == null) monitor = new NullProgressMonitor();
		
		monitor.beginTask("Fetching packages for \"" + project.getName() + "\"...", 2);
		IPackage[] packages = PackagesModel.instance().getProjectPackages(project);
		monitor.worked(1);
		
		if (packages == null) {
			if (forceInit && packageFileExists(project)) {
				PackagesModel.instance().registerProject(project, monitor);
				packages = PackagesModel.instance().getProjectPackages(project);
			}
			
			if (packages == null) return new IPackage[0];
		}

		monitor.worked(1);
		monitor.done();
		return packages;
	}

	public static boolean packageFileExists (IProject project) {
		return project.getFile(PackagesModel.PROJECT_PACKAGES_FILE).exists();
	}
	
	public static boolean projectRegistered(IProject project) {
		return PackagesModel.instance().getRoot(project) == null ? false : true;
	}

	
	/**
	 * Visit all of the top-level packages in the passed in project with the passed in node visitor
	 * @param project The project whose packages to visit
	 * @param visitor The visitor
	 */
	public static void visitProjectPackages (IProject project, IPackageNodeVisitor visitor) {
		if (packageFileExists(project)) {
			IPackage packages[] = getProjectPackages(project, null, false);
			if( packages == null ) return;
			for (int i = 0; i < packages.length; i++) {
				boolean keepGoing = packages[i].accept(visitor);
				if (!keepGoing) break;
			}
		}
	}

	
	public static IPackageType getPackageType (String packageType) {
		return ExtensionManager.getPackageType(packageType);
	}

	public static IPath[] findMatchingPaths(IPath root, String includes, String excludes) {
		DirectoryScanner scanner  = 
			DirectoryScannerFactory.createDirectoryScanner(root, includes, excludes, true);
		String[] files = scanner.getIncludedFiles();
		IPath[] paths = new IPath[files.length];
		for( int i = 0; i < files.length; i++ ) {
			paths[i] = new Path(files[i]);
		}
		return paths;
	}
}
