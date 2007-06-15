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

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.model.DirectoryScannerFactory;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.DirectoryScannerFactory.DirectoryScannerExtension;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFileSet;
import org.jboss.ide.eclipse.archives.core.util.ModelUtil;

/**
 * An implementation for filesets
 * @author <a href="rob.stryker@redhat.com">Rob Stryker</a>
 *
 */
public class ArchiveFileSetImpl extends ArchiveNodeImpl implements
		IArchiveFileSet {

	private XbFileSet filesetDelegate;
	private DirectoryScannerExtension scanner;
	private ArrayList matchingPaths;
	private boolean rescanRequired = true;
	
	public ArchiveFileSetImpl() {
		this(new XbFileSet());
	}
	
	public ArchiveFileSetImpl (XbFileSet delegate) {
		super(delegate);
		this.filesetDelegate = delegate;
	}
	
	
	public void addMatchingFile(IPath file) {
		if( !matchingPaths.contains(file) && matchesPath(file)) 
			matchingPaths.add(file);
	}

	public void removeMatchingFile(IPath file ) {
		if( matchingPaths.contains(file) && matchesPath(file))
			matchingPaths.remove(file);
	}

	
	/*
	 * @see IArchiveFileSet#findMatchingPaths()
	 */
	public synchronized IPath[] findMatchingPaths () {
		getScanner();  // ensure up to date
		return (IPath[]) matchingPaths.toArray(new IPath[matchingPaths.size()]);
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
			return getProjectPath();
		} else if( isInWorkspace()){
			return ModelUtil.workspacePathToAbsolutePath(new Path(path)); 
		} else {
			return new Path(path);
		}
	}
	
	/*
	 * @see IArchiveFileSet#getSourcePath()
	 */
	public IPath getSourcePath() {
		return filesetDelegate.getDir() == null ? null : new Path(filesetDelegate.getDir());
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

			// new scanner
			scanner = DirectoryScannerFactory.createDirectoryScanner(
					getGlobalSourcePath(), getIncludesPattern(), getExcludesPattern(), true);
			
			// cache the paths
			ArrayList paths = new ArrayList();
			IPath sp = getGlobalSourcePath();
			String matched[] = scanner.getIncludedFiles();
			for (int i = 0; i < matched.length; i++) {
				IPath path = sp.append(new Path(matched[i]));
				paths.add(path);
			}

			matchingPaths = paths;
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
		rescanRequired = true;
	}

	/*
	 * @see IArchiveFileSet#setIncludesPattern(String)
	 */
	public void setIncludesPattern(String includes) {
		attributeChanged(INCLUDES_ATTRIBUTE, getIncludesPattern(), includes);
		filesetDelegate.setIncludes(includes);
		rescanRequired = true;
	}

	/*
	 * @see IArchiveFileSet#setInWorkspace(boolean)
	 */
	public void setInWorkspace(boolean isInWorkspace) {
		attributeChanged(IN_WORKSPACE_ATTRIBUTE, new Boolean(isInWorkspace()), new Boolean(isInWorkspace));
		filesetDelegate.setInWorkspace(isInWorkspace);
		rescanRequired = true;
	}
	
	/*
	 * @see IArchiveFileSet#setSourcePath(IPath, boolean)
	 */
	public void setSourcePath (IPath path) {
		Assert.isNotNull(path);
		IPath src = getSourcePath();
		attributeChanged(SOURCE_PATH_ATTRIBUTE, src == null ? null : src.toString(), path == null ? null : path.toString());
		filesetDelegate.setDir(path.toString());
		rescanRequired = true;
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
			return getParent().getRootArchiveRelativePath().append(getPathRelativeToParent(inputFile));
		}
		return null;
	}
	/*
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet#getPathRelativeToParent(org.eclipse.core.runtime.IPath)
	 */
	public IPath getPathRelativeToParent(IPath inputFile) {
		String s = inputFile.toOSString().substring(getGlobalSourcePath().toOSString().length()+1);
		return new Path(s);
}


	/*
	 * @see IArchiveFileSet#resetScanner()
	 */
	public void resetScanner() {
		rescanRequired = true;
	}
}
