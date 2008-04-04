package org.jboss.ide.eclipse.archives.core.ant;

import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModelCore;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModelException;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveBuildListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelRootNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeVisitor;

public class IsolatedTruezipExecution {
	private JBossArchivesTask task;
	public IsolatedTruezipExecution(JBossArchivesTask task) { 
		this.task = task;
	}
	public void execute() {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
		IArchiveBuildListener listener = getListener();

		task.log("Executing task using truezip. " + task.getLocation(), Project.MSG_VERBOSE);
		ResourceModel.getDefault();
		
        // needed so the correct XML binding / TrueZIP jars are loaded
        ClassLoader myCL = getClass().getClassLoader();
        Thread.currentThread().setContextClassLoader(myCL);
        
        ((AntVariables)ArchivesCore.getInstance().getVariables()).setCurrentTask(task);
		// figure out which one to build
		ArchivesModel.instance().addBuildListener(listener);
		IPath path = ArchivesCore.getInstance().getVariables().getProjectPath(task.getEclipseProject());
		if( verifyBuildPrereqs(path) )  {
			task.log("Building archives for project project: " + path, Project.MSG_VERBOSE);
	        ArchivesModelCore.buildProject(path, null);
		} else {
			throw new BuildException("Eclipse-relative paths missing absolute path mappings.");
		}

		ArchivesModel.instance().removeBuildListener(listener);
        Thread.currentThread().setContextClassLoader(original);
	}
	
	
	protected boolean verifyBuildPrereqs(IPath path) {
		// get the project's location
		if( path == null ) {
			task.log("Could not find a project location for " + task.getEclipseProject(), Project.MSG_ERR);
			return false;
		}
		
		if( ArchivesModel.instance().getRoot(path) == null) {
			task.log("Registering project: " + path + ". ", Project.MSG_VERBOSE);
			try {
				ArchivesModel.instance().registerProject(path, null);
			} catch( ArchivesModelException ame ) {
				// log it
			}
		}
		
		IArchiveModelRootNode node = ArchivesModel.instance().getRoot(path);
		final ArrayList<String> requiredProjects = new ArrayList<String>();
		node.accept(new IArchiveNodeVisitor() {
			public boolean visit(IArchiveNode node) {
				if( node.getNodeType() == IArchiveNode.TYPE_ARCHIVE) {
					IArchive archive = (IArchive)node;
					if( archive.isTopLevel() && archive.isDestinationInWorkspace() ) {
						IPath p = archive.getDestinationPath();
						if( p != null ) {
							String projName = p.segment(0);
							if( !requiredProjects.contains(projName))
								requiredProjects.add(projName);
						}
					}
				} else if( node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FILESET) {
					IArchiveFileSet fs = (IArchiveFileSet)node;
					if( fs.isInWorkspace() ) {
						IPath p = fs.getSourcePath();
						if( p != null ) {
							String projName = p.segment(0);
							if(!requiredProjects.contains(projName)) 
								requiredProjects.add(projName);
						}
					}
				}
				return true;
			} 
		});
		
		boolean retVal = true;
		AntVariables vars = ((AntVariables)ArchivesCore.getInstance().getVariables());
		for( int i = 0; i < requiredProjects.size(); i++ ) {
			IPath p = vars.getProjectPath(requiredProjects.get(i));
			if( p == null ) {
				retVal = false;
				task.log("Required project \"" + requiredProjects.get(i) + 
						"\" has unknown location. " + task.getLocation(), Project.MSG_ERR);
			}
		}
		
		return retVal;
	}
	
	protected IArchiveBuildListener getListener() {
		return new IArchiveBuildListener() {

			public void buildFailed(IArchive pkg, IStatus status) {
				task.log("buildFailed: ", Project.MSG_DEBUG);
			}

			public void cleanArchive(IArchive pkg) {
				task.log("cleanArchive", Project.MSG_DEBUG);
			}

			public void cleanProject(IPath project) {
				task.log("cleanProject", Project.MSG_DEBUG);
			}

 			public void fileUpdated(IArchive topLevelArchive,
					IArchiveFileSet fileset, IPath filePath) {
				task.log("fileUpdated", Project.MSG_DEBUG);
			}

			public void finishedBuild(IPath project) {
				task.log("finishedBuild", Project.MSG_DEBUG);
			}

			public void finishedBuildingArchive(IArchive pkg) {
				task.log("finishedBuildingArchive", Project.MSG_DEBUG);
			}

			public void finishedCollectingFileSet(IArchiveFileSet fileset) {
				task.log("finishedCollectingFileSet", Project.MSG_DEBUG);
			}

			public void startedBuild(IPath project) {
				task.log("startedBuild", Project.MSG_DEBUG);
			}

			public void startedBuildingArchive(IArchive pkg) {
				task.log("startedBuildingArchive", Project.MSG_DEBUG);
			}

			public void startedCollectingFileSet(IArchiveFileSet fileset) {
				task.log("startedCollectingFileSet", Project.MSG_DEBUG);
			}

			public void fileRemoved(IArchive topLevelArchive,
					IArchiveFileSet fileset, IPath filePath) {
				task.log("fileRemoved: " + filePath, Project.MSG_DEBUG);
			}
			
		};
	}
}
