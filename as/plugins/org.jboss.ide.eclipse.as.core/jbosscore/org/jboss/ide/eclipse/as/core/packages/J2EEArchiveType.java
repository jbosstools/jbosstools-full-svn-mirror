/**
 * JBoss, a Division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
* This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.as.core.packages;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IModuleArtifact;
import org.eclipse.wst.server.core.internal.ServerPlugin;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.types.IArchiveType;
import org.jboss.ide.eclipse.archives.core.util.ArchiveNodeFactory;

/**
 *
 * @author rob.stryker@jboss.com
 */
public abstract class J2EEArchiveType implements IArchiveType {
	public static final String METAINF = "META-INF";
	public static final String WEBINF = "WEB-INF";
	public static final String CLASSES = "classes";
	public static final String LIB = "lib";
	public static final String WEBCONTENT = "WebContent";
	public static final String EARCONTENT = "EarContent";
	public static final String EJBMODULE = "ejbModule";


	protected boolean isModuleType(IModule module, String moduleTypeId){	
		if(module.getModuleType()!=null && moduleTypeId.equals(module.getModuleType().getId()))
			return true;
		return false;
	}

	protected IModule getModule(IProject project) {
		IModuleArtifact moduleArtifact = ServerPlugin.loadModuleArtifact(project);
		return moduleArtifact == null ? null : moduleArtifact.getModule();
	}
	
	// Find the source folder, then create the IPackage appropriately
	public static IArchive createGenericIArchive(IProject project, String deployDirectory, String packageName) {
		try {
			IJavaProject javaProject = JavaCore.create(project);
			Assert.isNotNull(javaProject);
			
			IPath sourcePath;
			try {
				sourcePath = javaProject.getOutputLocation();
			} catch (JavaModelException e) {
				e.printStackTrace();
				return null;
			}
			sourcePath = sourcePath.removeFirstSegments(1);
			IContainer sourcePathContainer;
			if( sourcePath.segmentCount() == 0 ) 
				sourcePathContainer = project;
			else
				sourcePathContainer = project.getFolder(sourcePath);
			return createGenericIArchive(project, deployDirectory, packageName, sourcePathContainer);
		} catch( Exception e ) {
			e.printStackTrace();
		}
		return null;
	} 
	
	// Create a detached package with some generic settings
	public static IArchive createGenericIArchive(IProject project, String deployDirectory, String packageName, IContainer sourceContainer) {
		IArchive jar = ArchiveNodeFactory.createArchive();
			
		if( deployDirectory != null ) {
			jar.setDestinationPath(new Path(deployDirectory), false);
			jar.setExploded(false);
		} else {
			jar.setDestinationPath(project.getLocation(), true);
			jar.setExploded(false);
		}
		jar.setName(packageName);
			
		IArchiveFileSet classes = ArchiveNodeFactory.createFileset();
		classes.setIncludesPattern("**/*");
		classes.setSourcePath(sourceContainer.getLocation());
		jar.addChild(classes);
		return jar;
	}

	
	public static IArchiveFolder addFolder(IProject project, IArchiveNode parent, String name) {
		IArchiveFolder folder = ArchiveNodeFactory.createFolder();
		folder.setName(name);
		parent.addChild(folder);
		return folder;
	}
	public static IArchiveFileSet addFileset(IProject project, IArchiveNode parent, String sourcePath, String includePattern) {
		IArchiveFileSet fs = ArchiveNodeFactory.createFileset();
		Assert.isNotNull(project);
		IJavaProject javaProject = JavaCore.create(project);
		Assert.isNotNull(javaProject);

		IContainer sourceContainer;
		if( sourcePath != null && !sourcePath.equals("")) {
			Path p = new Path(sourcePath);
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			sourceContainer = p.segmentCount() != 1 ? (IContainer)root.getFolder(p) : root.getProject(p.segment(0));
		} else {
			sourceContainer = project;
		}

		fs.setSourcePath(sourceContainer.getLocation());
		fs.setIncludesPattern(  includePattern == null ?  "**/*" : includePattern );
		parent.addChild(fs);
		return fs;
	}
	
	public abstract String getAssociatedModuleType();
	
}
