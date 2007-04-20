package org.jboss.ide.eclipse.archives.core.model;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.ExtensionManager;
import org.jboss.ide.eclipse.archives.core.build.ArchiveBuildDelegate;
import org.jboss.ide.eclipse.archives.core.build.ModelChangeListener;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.types.IArchiveType;

public class ArchivesCore {
	private static ArchivesCore instance;
	public static ArchivesCore getInstance() {
		if( instance == null ) 
			instance = new ArchivesCore();
		return instance;
	}
	
	private ArrayList buildListeners;
	private ArrayList modelListeners;
	public ArchivesCore() {
		buildListeners = new ArrayList();
		modelListeners = new ArrayList();
		addModelListener(new ModelChangeListener());
	}
	
	public void addBuildListener(IArchiveBuildListener listener) {
		if( !buildListeners.contains(listener)) 
			buildListeners.add(listener);
	}
	public void removeBuildListener(IArchiveBuildListener listener) {
		if( buildListeners.contains(listener)) 
			buildListeners.remove(listener);
	}
	public IArchiveBuildListener[] getBuildListeners() {
		return (IArchiveBuildListener[]) buildListeners.toArray(new IArchiveBuildListener[buildListeners.size()]);
	}
	
	public void addModelListener(IArchiveModelListener listener) {
		if( !modelListeners.contains(listener)) 
			modelListeners.add(listener);
	}
	public void removeModelListener(IArchiveModelListener listener) {
		if( modelListeners.contains(listener)) 
			modelListeners.remove(listener);
	}
	public IArchiveModelListener[] getModelListeners() {
		return (IArchiveModelListener[]) modelListeners.toArray(new IArchiveModelListener[modelListeners.size()]);
	}
	
	
	
	/**
	 * Builds all of a project's packages. Note that this does not call any builders before or after the package builder (i.e. the JDT builder).
	 * If you are looking to run all the builders on a project use project.build()
	 * @param project The project to build
	 * @param buildType FULL_BUILD, INCREMENTAL_BUILD, CLEAN_BUILD, etc
	 * @param monitor A progress monitor
	 */
	public static void buildProject (IProject project, IProgressMonitor monitor) {
		if (monitor == null) monitor = new NullProgressMonitor();
		new ArchiveBuildDelegate().fullProjectBuild(project);
	}
	
	/**
	 * Build the passed-in package.
	 * @param pkg The package to build
	 */
	public static void buildArchive (IArchive pkg, IProgressMonitor monitor) {
		if (monitor == null) monitor = new NullProgressMonitor();
		new ArchiveBuildDelegate().fullArchiveBuild(pkg);
	}

	public static IArchive[] getAllProjectPackages(IProgressMonitor monitor) {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		ArrayList results = new ArrayList();
		for( int i = 0; i < projects.length; i++ ) {
			if( projects[i].isAccessible()) {
				results.addAll(Arrays.asList(getProjectPackages(projects[i], monitor, true)));
			}
		}
		return (IArchive[]) results.toArray(new IArchive[results.size()]);
	}
	
	public static IArchive[] getProjectPackages (IProject project, IProgressMonitor monitor, boolean forceInit) {
		if (monitor == null) monitor = new NullProgressMonitor();
		
		monitor.beginTask("Fetching packages for \"" + project.getName() + "\"...", 2);
		IArchive[] packages = ArchivesModel.instance().getProjectArchives(project);
		monitor.worked(1);
		
		if (packages == null) {
			if (forceInit && packageFileExists(project)) {
				ArchivesModel.instance().registerProject(project, monitor);
				packages = ArchivesModel.instance().getProjectArchives(project);
			}
			
			if (packages == null) return new IArchive[0];
		}

		monitor.worked(1);
		monitor.done();
		return packages;
	}

	public static boolean packageFileExists (IProject project) {
		return project.getFile(ArchivesModel.PROJECT_PACKAGES_FILE).exists();
	}
	
	public static boolean projectRegistered(IProject project) {
		return ArchivesModel.instance().getRoot(project) == null ? false : true;
	}

	
	/**
	 * Visit all of the top-level packages in the passed in project with the passed in node visitor
	 * @param project The project whose packages to visit
	 * @param visitor The visitor
	 */
	public static void visitProjectArchives (IProject project, IArchiveNodeVisitor visitor) {
		if (packageFileExists(project)) {
			IArchive packages[] = getProjectPackages(project, null, false);
			if( packages == null ) return;
			for (int i = 0; i < packages.length; i++) {
				boolean keepGoing = packages[i].accept(visitor);
				if (!keepGoing) break;
			}
		}
	}

	
	public static IArchiveType getArchiveType (String packageType) {
		return ExtensionManager.getArchiveType(packageType);
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
