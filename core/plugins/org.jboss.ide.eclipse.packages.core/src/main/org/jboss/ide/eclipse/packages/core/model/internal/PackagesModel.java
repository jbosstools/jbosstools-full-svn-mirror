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
package org.jboss.ide.eclipse.packages.core.model.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSetWorkingCopy;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolderWorkingCopy;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageWorkingCopy;
import org.jboss.ide.eclipse.packages.core.model.IPackagesBuildListener;
import org.jboss.ide.eclipse.packages.core.model.IPackagesModelListener;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XMLBinding;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFileSet;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFolder;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackages;
import org.jboss.ide.eclipse.packages.core.project.PackagesNature;

public class PackagesModel {

	private static PackagesModel _instance;
	
	public static final String PROJECT_PACKAGES_FILE = ".packages";
	
	private Hashtable modelBridge;
	private Hashtable projectPackages;
	private Hashtable xbPackages;
	private ArrayList buildListeners;
	private ArrayList modelListeners;
	private Hashtable deferredEvents;
	
	private PackagesModel ()
	{
		modelBridge = new Hashtable();
		projectPackages = new Hashtable();
		xbPackages = new Hashtable();
		buildListeners = new ArrayList();
		modelListeners = new ArrayList();
		deferredEvents = new Hashtable();
	}
	
	public static PackagesModel instance ()
	{
		if (_instance == null)
			_instance = new PackagesModel();
		
		return _instance;
	}
	
	public void registerProject(IProject project, IProgressMonitor monitor)
	{
		monitor.beginTask("Loading configuration...", XMLBinding.NUM_UNMARSHAL_MONITOR_STEPS + 2);
		IFile packagesFile = project.getFile(PROJECT_PACKAGES_FILE);
		if (packagesFile.exists())
		{
			try {
				if (!project.hasNature(PackagesNature.NATURE_ID))
					ProjectUtil.addProjectNature(project, PackagesNature.NATURE_ID);
				
				XbPackages packages = XMLBinding.unmarshal(packagesFile.getContents(), monitor);
				monitor.worked(1);
				
				xbPackages.put(project, packages);
				createPackageNodeImpl(project, packages);
				monitor.worked(1);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	public IPackageWorkingCopy createPackage(IProject project)
	{
		Assert.isNotNull(project);
		
		return new PackageImpl(project, new XbPackage());
	}
	
	public IPackageFolderWorkingCopy createPackageFolder(IProject project) {
		Assert.isNotNull(project);
		
		return new PackageFolderImpl(project, new XbFolder());
	}

	public IPackageFileSetWorkingCopy createPackageFileSet(IProject project) {
		Assert.isNotNull(project);
		
		return new PackageFileSetImpl(project, new XbFileSet());
	}
	
	public void removePackage(IPackage pkg) {
		if (projectPackages.containsKey(pkg.getProject()))
		{
			if (getProjectPackages(pkg.getProject()).contains(pkg))
				getProjectPackages(pkg.getProject()).remove(pkg);
		}
		if (modelBridge.containsKey(pkg))
		{
			modelBridge.remove(pkg);
		}
		if (xbPackages.containsKey(pkg.getProject()))
		{
			PackageImpl packageImpl = (PackageImpl) pkg;
			
			XbPackages packages = getXbPackages(pkg.getProject());
			packages.removeChild(packageImpl.getNodeDelegate());
		}
	}
	
	public XbPackages getXbPackages(IProject project)
	{
		return (XbPackages) xbPackages.get(project);
	}
	
	public List getProjectPackages (IProject project)
	{
		return (List)projectPackages.get(project);
	}
	
	public void addPackagesModelListener (IPackagesModelListener listener)
	{
		modelListeners.add(listener);
	}
	
	public void removePackagesModelListener (IPackagesModelListener listener)
	{
		if (modelListeners.contains(listener))
			modelListeners.remove(listener);
	}
	
	public void addPackagesBuildListener (IPackagesBuildListener listener)
	{
		buildListeners.add(listener);
	}
	
	public void removePackagesBuildListener (IPackagesBuildListener listener)
	{
		if (buildListeners.contains(listener))
			buildListeners.remove(listener);
	}
	
	protected List getBuildListeners ()
	{
		return buildListeners;
	}
	
	public static DirectoryScanner createDirectoryScanner (IPath filesystemFolder, String include, String excludes)
	{
		return createDirectoryScanner(filesystemFolder, include, excludes, true);
	}
	
	public static DirectoryScanner createDirectoryScanner (IContainer srcFolder, String include, String excludes)
	{
		return createDirectoryScanner(srcFolder, include, excludes, true);
	}
	
	public static DirectoryScanner createDirectoryScanner (IPath filesystemFolder, String includes, String excludes, boolean scan)
	{
		if (includes == null) includes = "";
		if (excludes == null) excludes = "";
		
		DirectoryScanner scanner = new DirectoryScanner();
		String excludesList[] = excludes.split(" ?, ?");
		String includesList[] = includes.split(" ?, ?");
		
		File basedir = filesystemFolder.toFile();	
		scanner.setBasedir(basedir);
		scanner.setExcludes(excludesList);
		scanner.setIncludes(includesList);
		if (scan)
			scanner.scan();
		
		return scanner;
	}
	
	public static DirectoryScanner createDirectoryScanner (IContainer srcFolder, String includes, String excludes, boolean scan)
	{
		if (includes == null) includes = "";
		if (excludes == null) excludes = "";
		
		DirectoryScanner scanner = new DirectoryScanner();
		String excludesList[] = excludes.split(" ?, ?");
		String includesList[] = includes.split(" ?, ?");
		
		File basedir = srcFolder.getLocation().toFile();
		if (srcFolder.getRawLocation() != null)
			basedir = srcFolder.getRawLocation().toFile();
			
		scanner.setBasedir(basedir);
		scanner.setExcludes(excludesList);
		scanner.setIncludes(includesList);
		if (scan)
			scanner.scan();
		
		return scanner;
	}
	
	protected void saveAndRegister (PackageNodeImpl node, PackageNodeImpl original)
	{
		XbPackageNode parent = null;
		
		if (original != null)
		{
			unregisterPackageNode(original, original.getNodeDelegate());
			
			parent = original.getNodeDelegate().getParent();
			
			if (parent != null)
				parent.removeChild(original.getNodeDelegate());
			
			if (original.getNodeType() == IPackageNode.TYPE_PACKAGE)
			{
				IPackage pkg = (IPackage) original;
				removePackage(pkg);
			}
			
			original = null;
		}
		
		if (parent != null)
		{
			parent.addChild(node.getNodeDelegate());
		}
		else if (node.getNodeType() == IPackageNode.TYPE_PACKAGE)
		{
			XbPackages packages = getXbPackages(node.getProject());
			if (packages == null) {
				packages = new XbPackages();
				xbPackages.put(node.getProject(), packages);
			}
			
			if (packages.getChildren(XbPackage.class) == null || !packages.getChildren(XbPackage.class).contains(node.getNodeDelegate()))
			{	
				packages.addChild(node.getNodeDelegate());
			}
		}
		
		registerPackageNode(node, node.getNodeDelegate());
		
		saveModel(node.getProject());
		
		if (deferredEvents.containsKey(node))
		{
			ArrayList events = (ArrayList) deferredEvents.get(node);
			for (Iterator iter = events.iterator(); iter.hasNext(); )
			{
				Runnable runnable = (Runnable) iter.next();
				runnable.run();
			}
		}
	}
	
	protected void saveModel (IProject project)
	{
		try {
			IProgressMonitor nullMonitor = new NullProgressMonitor();
			
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(bytesOut);
			XMLBinding.marshal(PackagesModel.instance().getXbPackages(project), writer, nullMonitor);
			writer.close();
			
			ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());
			IFile packagesFile = project.getFile(PackagesModel.PROJECT_PACKAGES_FILE);
			if (!packagesFile.exists())
				packagesFile.create(bytesIn, true, nullMonitor);
			else
				packagesFile.setContents(bytesIn, true, true, nullMonitor);
			
			bytesIn.close();
			bytesOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected PackageNodeImpl createPackageNodeImpl (IProject project, XbPackageNode node)
	{
		PackageNodeImpl nodeImpl = null;
		
		if (node instanceof XbPackage)
		{
			nodeImpl = new PackageImpl(project, (XbPackage)node);
		}
		else if (node instanceof XbFolder)
		{
			nodeImpl = new PackageFolderImpl(project, (XbFolder)node);
		}
		else if (node instanceof XbFileSet)
		{
			nodeImpl = new PackageFileSetImpl(project, (XbFileSet)node);
		}
		
		for (Iterator iter = node.getAllChildren().iterator(); iter.hasNext(); )
		{
			XbPackageNode child = (XbPackageNode) iter.next();
			
			// we don't need to do anything with child node impl's as they will 
			// be retrieved via lookup from the node delegates
			createPackageNodeImpl(project, child);
		}
		
		if (nodeImpl != null)
		{
			registerPackageNode(nodeImpl, node);
		}
		
		return nodeImpl;
	}
	
	protected void fireNodeAdded (final IPackageNode added)
	{
		fireEventMaybeDeferred(added, new Runnable() {
			public void run() {
				for (Iterator iter = modelListeners.iterator(); iter.hasNext(); )
				{
					IPackagesModelListener listener = (IPackagesModelListener) iter.next();
					listener.packageNodeAdded(added);
				}
			}
		});
	}
	
	protected void fireNodeRemoved (final IPackageNode removed)
	{
		fireEventMaybeDeferred(removed, new Runnable() {
			public void run() {
				for (Iterator iter = modelListeners.iterator(); iter.hasNext(); )
				{
					IPackagesModelListener listener = (IPackagesModelListener) iter.next();
					listener.packageNodeRemoved(removed);
				}
			}
		});
	}
	
	protected void fireNodeChanged (final IPackageNode changed)
	{
		fireEventMaybeDeferred(changed, new Runnable() {
			public void run() {
				for (Iterator iter = modelListeners.iterator(); iter.hasNext(); )
				{
					IPackagesModelListener listener = (IPackagesModelListener) iter.next();
					listener.packageNodeChanged(changed);
				}
			}
		});
	}
	
	protected void fireEventMaybeDeferred (IPackageNode source, Runnable runnable)
	{
		PackageNodeImpl nodeImpl = (PackageNodeImpl) source;
		if (nodeImpl.isWorkingCopy())
		{
			if (!deferredEvents.containsKey(source))
			{
				deferredEvents.put(source, new ArrayList());
			}
			
			((ArrayList)deferredEvents.get(source)).add(runnable);
		}
		else {
			runnable.run();
		}
	}
	
	protected void registerPackageNode(PackageNodeImpl nodeImpl, XbPackageNode node)
	{
		if (!modelBridge.containsKey(nodeImpl))
		{
			modelBridge.put(nodeImpl, node);
			
			if (nodeImpl instanceof IPackage)
			{
				IPackage pkg = (IPackage) nodeImpl;
				if (pkg.isTopLevel()) {
					if (!projectPackages.containsKey(pkg.getProject()))
					{
						projectPackages.put(pkg.getProject(), new ArrayList());
					}
					getProjectPackages(pkg.getProject()).add(pkg);
				}
			}
			
			fireNodeAdded(nodeImpl);
		}
	}
	
	protected void unregisterPackageNode(PackageNodeImpl nodeImpl, XbPackageNode node) {
		if (modelBridge.containsKey(nodeImpl))
		{
			modelBridge.remove(nodeImpl);
		
			if (nodeImpl instanceof IPackage)
			{
				IPackage pkg = (IPackage) nodeImpl;
				if (pkg.isTopLevel()) {
					if (projectPackages.containsKey(pkg.getProject()))
					{
						List pkgs = getProjectPackages(pkg.getProject());
						if (pkgs.contains(pkg))
						{
							pkgs.remove(pkg);
						}
					}
				}
			}
			
			fireNodeRemoved(nodeImpl);
		}
	}
	
	protected PackageNodeImpl getPackageNodeImpl(XbPackageNode node)
	{
		for (Iterator iter = modelBridge.keySet().iterator(); iter.hasNext(); )
		{
			PackageNodeImpl nodeImpl = (PackageNodeImpl)iter.next();
			if (modelBridge.get(nodeImpl).equals(node))
			{
				return nodeImpl;
			}
		}
		return null;
	}
	
	protected XbPackageNode getPackageNode(PackageNodeImpl nodeImpl)
	{
		return (XbPackageNode) modelBridge.get(nodeImpl);
	}
}
