/*
 * JBoss, a division of Red Hat
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
package org.jboss.ide.eclipse.packages.core.build;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeVisitor;
import org.jboss.ide.eclipse.packages.core.model.events.EventManager;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageModelNode;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.core.util.TrueZipUtil;

/**
 * The builder is responsible for building packages.
 * 
 * @author Stryker
 */
public class PackagesBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "org.jboss.ide.eclipse.packages.core.packagesBuilder";
	
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		EventManager.fireBuildStarted();
		IProject[] interestingProjects = getInterestingProjectsInternal();

		final TreeSet addedChanged = createDefaultTreeSet();
		final TreeSet removed = createDefaultTreeSet();

		PackageBuildDelegate delegate = new PackageBuildDelegate();
		if (kind == IncrementalProjectBuilder.INCREMENTAL_BUILD || kind == IncrementalProjectBuilder.AUTO_BUILD) {
			fillDeltas(interestingProjects, addedChanged, removed);
			delegate.projectIncrementalBuild(addedChanged, removed);
			
		} else if (kind == IncrementalProjectBuilder.FULL_BUILD){
			// build each package fully
			IProject p = getProject();
			delegate.fullProjectBuild(p);
		}
		
		
		EventManager.fireBuildEnded();
		return interestingProjects;
	}
	
	protected void clean(IProgressMonitor monitor) throws CoreException {
		IProject p = getProject();
		PackageModelNode root = PackagesModel.instance().getRoot(p);
		IPackageNode[] nodes = root.getChildren(IPackageNode.TYPE_PACKAGE);
		for( int i = 0; i < nodes.length; i++ ) {
			IPath path = ((IPackage)nodes[i]).getDestinationPath();
			TrueZipUtil.deleteAll(path);
		}
	}

	protected void fillDeltas(IProject[] projects, final TreeSet addedChanged, final TreeSet removed) {
		for( int i = 0; i < projects.length; i++ ) {
			final IProject proj = projects[i];
			IResourceDelta delta = getDelta(proj);
			try {
				delta.accept(new IResourceDeltaVisitor () { 
					public boolean visit(IResourceDelta delta) throws CoreException {
						if (delta.getResource().getType() == IResource.FILE)  {
							if( (delta.getKind() & IResourceDelta.ADDED) > 0  
									|| (delta.getKind() & IResourceDelta.CHANGED) > 0) {
								
								// ignore the packages project. that will it's own build call, 
								// or will handle the change in some other way
								if( !delta.getResource().equals(proj.findMember(PackagesModel.PROJECT_PACKAGES_FILE))) 
									addedChanged.add(delta.getResource());
							} else if( (delta.getKind() & IResourceDelta.REMOVED ) > 0 ) {
								removed.add(delta.getResource());
							}
						} 
						return true;
					}
				});
			} catch( CoreException ce) {
				
			}
		}
	}
	protected IProject[] getInterestingProjectsInternal() {
		final TreeSet set = createDefaultTreeSet();
		set.add(getProject());
		
		final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		final int count = workspaceRoot.getLocation().segmentCount();

		PackagesModel.instance().getRoot(getProject()).accept(new IPackageNodeVisitor () {
			public boolean visit (IPackageNode node) {
				if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET) {
					IPackageFileSet fileset = (IPackageFileSet)node;
					IPath p = fileset.getSourcePath();
					if( workspaceRoot.getLocation().isPrefixOf(p)) {
						IProject proj = workspaceRoot.getProject(p.segment(count));
						set.add(proj);
					}
				}
				return true;
			}
		});
		return (IProject[]) set.toArray(new IProject[set.size()]);
	}
	
	protected TreeSet createDefaultTreeSet() {
		return new TreeSet(new Comparator () {
			public int compare(Object o1, Object o2) {
				if (o1.equals(o2)) return 0;
				else return -1;
			}
		});		
	}
	
}