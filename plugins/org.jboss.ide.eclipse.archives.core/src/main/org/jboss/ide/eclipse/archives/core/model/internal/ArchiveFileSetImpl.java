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
package org.jboss.ide.eclipse.archives.core.model.internal;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.model.DirectoryScannerFactory;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.DirectoryScannerFactory.DirectoryScannerExtension;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFileSet;

/**
 * An implementation for filesets
 * @author <a href="rob.stryker@redhat.com">Rob Stryker</a>
 *
 */
public class ArchiveFileSetImpl extends ArchiveNodeImpl implements
		IArchiveFileSet {

	private XbFileSet filesetDelegate;
	private DirectoryScannerExtension scanner;
	private boolean rescanRequired = true;
	
	public ArchiveFileSetImpl() {
		this(new XbFileSet());
	}
	
	public ArchiveFileSetImpl (XbFileSet delegate) {
		super(delegate);
		this.filesetDelegate = delegate;
	}
	
	/*
	 * @see IArchiveFileSet#findMatchingPaths()
	 */
	public IPath[] findMatchingPaths () {
		DirectoryScannerExtension scanner = getScanner();
		ArrayList paths = new ArrayList();
		IPath sp = getGlobalSourcePath();
		String matched[] = scanner.getIncludedFiles();
		for (int i = 0; i < matched.length; i++) {
			IPath path = sp.append(new Path(matched[i]));
			paths.add(path);
		}
		
		return (IPath[])paths.toArray(new IPath[paths.size()]);
	}
	
	/*
	 * @see IArchiveFileSet#getExcludesPattern()
	 */
	public String getExcludesPattern() {
		return filesetDelegate.getExcludes();
	}
	
	/*
	 * @see IArchiveFileSet#isInWorkspace()
	 */
	public boolean isInWorkspace() {
		return filesetDelegate.isInWorkspace();
	}
	
	/*
	 * @see IArchiveFileSet#getIncludesPattern()
	 */
	public String getIncludesPattern() {
		return filesetDelegate.getIncludes();
	}

	/*
	 * @see IArchiveFileSet#getGlobalSourcePath()
	 */
	public IPath getGlobalSourcePath() {
		String path = filesetDelegate.getDir();
		if (path == null || path.equals(".") || path.equals("")) {
			return getProject() == null ? null : getProject().getLocation();
		} else if( isInWorkspace()){
			return ResourcesPlugin.getWorkspace().getRoot().getLocation().append(path);
		} else {
			return new Path(path);
		}
	}
	
	/*
	 * @see IArchiveFileSet#getSourcePath()
	 */
	public IPath getSourcePath() {
		return new Path(filesetDelegate.getDir());
	}
	
	/*
	 * @see IArchiveFileSet#matchesFile(IFile)
	 */
	public boolean matchesFile(IFile file) {
		return matchesFile(getScanner(), file);
	}

	private boolean matchesFile(DirectoryScannerExtension scanner, IFile file) {
		return matchesPath(file.getLocation());
	}
	
	/*
	 * @see IArchiveFileSet#matchesPath(IPath)
	 */
	public boolean matchesPath(IPath path) {
		return matchesPath(getScanner(), path);
	}

	private boolean matchesPath(DirectoryScannerExtension scanner, IPath path) {
		if( getGlobalSourcePath().isPrefixOf(path)) {
			String s = path.toOSString().substring(getGlobalSourcePath().toOSString().length()+1);
			return scanner.isIncluded(s);
		}
		return false;
	}
	
	/*
	 * Will re-scan if required, or use cached scanner
	 * @return
	 */
	private synchronized DirectoryScannerExtension getScanner() {
		if( scanner == null || rescanRequired) {
			rescanRequired = false;
			scanner = DirectoryScannerFactory.createDirectoryScanner(
					getGlobalSourcePath(), getIncludesPattern(), getExcludesPattern(), true);
		}
		return scanner;
	}
		
	/*
	 * @see IArchiveNode#getNodeType()
	 */
	public int getNodeType() {
		return TYPE_ARCHIVE_FILESET;
	}
	
	/*
	 * @see IArchiveFileSet#setExcludesPattern(String)
	 */
	public void setExcludesPattern(String excludes) {
		attributeChanged(EXCLUDES_ATTRIBUTE, getExcludesPattern(), excludes);
		filesetDelegate.setExcludes(excludes);
	}

	/*
	 * @see IArchiveFileSet#setIncludesPattern(String)
	 */
	public void setIncludesPattern(String includes) {
		attributeChanged(INCLUDES_ATTRIBUTE, getIncludesPattern(), includes);
		filesetDelegate.setIncludes(includes);
	}

	/*
	 * @see IArchiveFileSet#setInWorkspace(boolean)
	 */
	public void setInWorkspace(boolean isInWorkspace) {
		attributeChanged(IN_WORKSPACE_ATTRIBUTE, new Boolean(isInWorkspace()), new Boolean(isInWorkspace));
		filesetDelegate.setInWorkspace(isInWorkspace);
	}
	
	/*
	 * @see IArchiveFileSet#setSourcePath(IPath, boolean)
	 */
	public void setSourcePath (IPath path) {
		Assert.isNotNull(path);
		IPath src = getGlobalSourcePath();
		attributeChanged(SOURCE_PATH_ATTRIBUTE, src == null ? null : src.toString(), path == null ? null : path.toString());
		filesetDelegate.setDir(path.toString());
	}
	
	protected XbFileSet getFileSetDelegate () {
		return filesetDelegate;
	}

	/*
	 * filesets have no path of their own
	 * and should not be the parents of any other node
	 * so the parent is their base location
	 * @see IArchiveNode#getRootArchiveRelativePath()
	 */
	public IPath getRootArchiveRelativePath() {
		return getParent().getRootArchiveRelativePath(); 
	}
	
	/*
	 * @see IArchiveFileSet#getRootArchiveRelativePath(IPath)
	 */
	public IPath getRootArchiveRelativePath(IPath inputFile) {
		if( matchesPath(inputFile)) {
			String s = inputFile.toOSString().substring(getGlobalSourcePath().toOSString().length()+1);
			return getParent().getRootArchiveRelativePath().append(s);
		}
		return null;
	}

	/*
	 * @see IArchiveFileSet#resetScanner()
	 */
	public void resetScanner() {
		rescanRequired = true;
	}
}
