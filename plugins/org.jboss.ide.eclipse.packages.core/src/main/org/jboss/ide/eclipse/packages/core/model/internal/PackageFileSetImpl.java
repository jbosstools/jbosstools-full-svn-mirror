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

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSetWorkingCopy;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeWorkingCopy;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFileSet;

/**
 * A PackageFileSetImpl.
 * 
 * @author <a href="marshall@jboss.org">Marshall Culpepper</a>
 * @version $Revision$
 */
public class PackageFileSetImpl extends PackageNodeImpl implements
		IPackageFileSet, IPackageFileSetWorkingCopy {

	private XbFileSet filesetDelegate;
	private PackageFileSetImpl original;
	
	public PackageFileSetImpl (IProject project, XbFileSet delegate)
	{
		super(project, delegate);
		this.filesetDelegate = delegate;
	}
	
	public IFile[] findMatchingFiles (DirectoryScanner scanner)
	{
		if (!isInWorkspace()) return new IFile[0];
		
		if (isSingleFile())
			return new IFile[] { getFile() };
		
		else
			return PackagesCore.findMatchingFiles(scanner, getSourceContainer(), getIncludesPattern(), getExcludesPattern());
	}
	
	public IFile[] findMatchingFiles() {
		DirectoryScanner scanner = createDirectoryScanner(true);
		
		return findMatchingFiles(scanner);
	}
	
	public IPath[] findMatchingPaths ()
	{
		DirectoryScanner scanner = createDirectoryScanner(true);
		return findMatchingPaths(scanner);
	}

	public IPath[] findMatchingPaths (DirectoryScanner scanner)
	{
		if (isInWorkspace()) return new IPath[0];
		
		if (isSingleFile())
			return new IPath[] { getFilePath() };
		else
			return PackagesCore.findMatchingPaths(scanner, getSourceFolder(), getIncludesPattern(), getExcludesPattern());
	}
	
	public String getDestinationFilename() {
		return filesetDelegate.getToFile();
	}

	public String getExcludesPattern() {
		return filesetDelegate.getExcludes();
	}
	
	public boolean isInWorkspace() {
		return filesetDelegate.isInWorkspace();
	}

	public IFile getFile() {
		if (!isInWorkspace()) return null;
		
		return getSourceProject().getFile(filesetDelegate.getFile());
	}

	public IPath getFilePath() {
		if (isInWorkspace()) return null;
		
		return new Path(filesetDelegate.getFile());
	}
	
	public String getIncludesPattern() {
		return filesetDelegate.getIncludes();
	}

	public IContainer getSourceContainer() {
		if (!isInWorkspace())
			return null;
		
		if (filesetDelegate.getDir() == null || filesetDelegate.getDir().equals(".") || filesetDelegate.getDir().equals(""))
			return getSourceProject();
		
		return getSourceProject().getFolder(filesetDelegate.getDir());
	}

	public IPath getSourceFolder() {
		if (isInWorkspace())
			return null;
		
		String path = filesetDelegate.getDir();
		if (path == null) return null;
		
		return new Path(path);
	}
	
	public IProject getSourceProject() {
		String projectName = filesetDelegate.getProject();
		if (projectName == null || projectName.length() == 0)
			return project;
		else
			return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
	}

	public boolean isSingleFile() {
		return filesetDelegate.getFile() != null;
	}

	public boolean matchesFile(DirectoryScanner scanner, IFile file)
	{
		if (!isInWorkspace()) return false;
		
		String files[] = scanner.getIncludedFiles();
		
		for (int i = 0; i < files.length; i++)
		{
			if (getSourceContainer().getFile(new Path(files[i])).equals(file))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean matchesPath(DirectoryScanner scanner, IPath path)
	{
		if (isInWorkspace()) return false;
		
		String paths[] = scanner.getIncludedFiles();
		
		for (int i = 0; i < paths.length; i++)
		{
			if (getSourceFolder().append(paths[i]).equals(path))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean matchesFile(IFile file) {
		DirectoryScanner scanner = createDirectoryScanner(true);
		return matchesFile(scanner, file);
	}
	
	public boolean matchesPath(IPath path) {
		DirectoryScanner scanner = createDirectoryScanner(true);
		return matchesPath(scanner, path);
	}

	public DirectoryScanner createDirectoryScanner(boolean scan) {
		if (isInWorkspace())
		{
			return PackagesModel.createDirectoryScanner(
					getSourceContainer(), getIncludesPattern(), getExcludesPattern(), scan);
		} else {
			return PackagesModel.createDirectoryScanner(
					getSourceFolder(), getIncludesPattern(), getExcludesPattern(), scan);
		}
	}
	
	public int getNodeType() {
		return TYPE_PACKAGE_FILESET;
	}

	public void setExcludesPattern(String excludes) {
		filesetDelegate.setExcludes(excludes);
	}

	public void setIncludesPattern(String includes) {
		filesetDelegate.setIncludes(includes);
	}

	public void setSingleFile(IFile file, String destinationFilename) {
		Assert.isNotNull(file);
		
		filesetDelegate.setFile(file.getProjectRelativePath().toString());
		filesetDelegate.setInWorkspace(true);
		
		if (destinationFilename != null) {
			filesetDelegate.setToFile(destinationFilename);
		}
	}
	
	public void setSingleFile(IPath path, String destinationFilename) {
		Assert.isNotNull(path);
		
		filesetDelegate.setFile(path.toString());
		filesetDelegate.setInWorkspace(false);
		
		if (destinationFilename != null)
		{
			if (destinationFilename != null) {
				filesetDelegate.setToFile(destinationFilename);
			}	
		}
	}
	
	public void setInWorkspace(boolean isInWorkspace) {
		filesetDelegate.setInWorkspace(isInWorkspace);
	}
	
	public void setSingleFile(IFile file) {
		setSingleFile(file, null);
	}

	public void setSingleFile(IPath path) {
		setSingleFile(path, null);
	}
	
	public void setSourceContainer(IContainer container) {
		Assert.isNotNull(container);
		
		filesetDelegate.setDir(container.getProjectRelativePath().toString());
		filesetDelegate.setInWorkspace(true);
	}
	
	public void setSourceFolder (IPath path) {
		Assert.isNotNull(path);
		
		filesetDelegate.setDir(path.toString());
		filesetDelegate.setInWorkspace(false);
	}
	
	public void setSourceProject(IProject project) {
		Assert.isNotNull(project);
		
		filesetDelegate.setProject(project.getName());
	}

	public IPackageNodeWorkingCopy createWorkingCopy() {
		return createFileSetWorkingCopy();
	}
	
	public IPackageFileSetWorkingCopy createFileSetWorkingCopy() {
		PackageFileSetImpl copy = new PackageFileSetImpl(project, new XbFileSet(filesetDelegate));
		copy.original = this;
		hasWorkingCopy = true;
		return copy;
	}
	
	public boolean hasWorkingCopy() {
		return hasWorkingCopy;
	}
	
	public IPackageNode save() {
		return saveFileSet();
	}
	
	public IPackageFileSet saveFileSet() {
		PackageFileSetImpl originalImpl = (PackageFileSetImpl) original;
		originalImpl.getFileSetDelegate().copyFrom(filesetDelegate);
		
		PackagesModel.instance().saveAndRegister(original);
		PackagesModel.instance().fireNodeChanged(original);
		original.hasWorkingCopy = false;
		return original;
	}
	
	public IPackageNode getOriginal() {
		return getOriginalFileSet();
	}
	
	public IPackageFileSet getOriginalFileSet() {
		return original;
	}

	protected XbFileSet getFileSetDelegate () {
		return filesetDelegate;
	}
	
	public String toString() {
		String includes = filesetDelegate.getIncludes();
		if (includes == null) includes = "";
		
		return filesetDelegate.getDir() + " : " + includes;
	}
}
