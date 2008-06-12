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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.DirectoryScannerFactory;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchivesLogger;
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

	private DirectoryScannerExtension scanner;
	private ArrayList<IPath> matchingPaths;
	private boolean rescanRequired = true;
	
	public ArchiveFileSetImpl() {
		this(new XbFileSet());
	}
	
	public ArchiveFileSetImpl (XbFileSet delegate) {
		super(delegate);
	}
	
	/*
	 * @see IArchiveFileSet#findMatchingPaths()
	 */
	public synchronized IPath[] findMatchingPaths () {
		getScanner();  // ensure up to date
		return matchingPaths == null ? new IPath[0] : matchingPaths.toArray(new IPath[matchingPaths.size()]);
	}
	
	/*
	 * @see IArchiveFileSet#getExcludesPattern()
	 */
	public String getExcludesPattern() {
		return getFileSetDelegate().getExcludes();
	}
	
	/*
	 * @see IArchiveFileSet#isInWorkspace()
	 */
	public boolean isInWorkspace() {
		return getFileSetDelegate().isInWorkspace();
	}
	
	/*
	 * @see IArchiveFileSet#getIncludesPattern()
	 */
	public String getIncludesPattern() {
		return getFileSetDelegate().getIncludes();
	}

	/*
	 * @see IArchiveFileSet#getGlobalSourcePath()
	 */
	public IPath getGlobalSourcePath() {
		IPath ret;
		String path = getFileSetDelegate().getDir();
		if (path == null || path.equals(".") || path.equals("")) {
			ret = getProjectPath();
		} else if( isInWorkspace()){
			ret = ModelUtil.workspacePathToAbsolutePath(new Path(path)); 
		} else {
			ret = new Path(path);
		}
		return ret;
	}
	
	/*
	 * @see IArchiveFileSet#getSourcePath()
	 */
	public IPath getSourcePath() {
		return getFileSetDelegate().getDir() == null ? 
				null : new Path(getFileSetDelegate().getDir());
	}
	
	/*
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet#isFlattened()
	 */
	public boolean isFlattened() {
		return getFileSetDelegate().isFlattened();
	}
	
	/*
	 * @see IArchiveFileSet#matchesPath(IPath)
	 */
	public boolean matchesPath(IPath path) {
		return matchesPath(getScanner(), path);
	}

	private boolean matchesPath(DirectoryScannerExtension scanner, IPath path) {
		IPath global = getGlobalSourcePath();
		if( global != null ) {
			if( global.isPrefixOf(path)) {
				String s = path.toOSString().substring(getGlobalSourcePath().toOSString().length()+1);
				return scanner.isUltimatelyIncluded(s);
			}
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

			try {
				// new scanner
				scanner = DirectoryScannerFactory.createDirectoryScanner(
						getGlobalSourcePath(), getIncludesPattern(), getExcludesPattern(), true);
				
				ArrayList<IPath> paths = new ArrayList<IPath>();
				if( scanner != null ) {
					// cache the paths
					IPath sp = getGlobalSourcePath();
					String matched[] = scanner.getIncludedFiles();
					for (int i = 0; i < matched.length; i++) {
						IPath path = sp.append(new Path(matched[i]));
						paths.add(path);
					}
				}
				matchingPaths = paths;
			} catch( IllegalStateException ise ) {
				ArchivesCore.getInstance().getLogger().log(IStatus.WARNING, "Could not create directory scanner", ise);
			}
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
		getFileSetDelegate().setExcludes(excludes);
		rescanRequired = true;
	}

	/*
	 * @see IArchiveFileSet#setIncludesPattern(String)
	 */
	public void setIncludesPattern(String includes) {
		attributeChanged(INCLUDES_ATTRIBUTE, getIncludesPattern(), includes);
		getFileSetDelegate().setIncludes(includes);
		rescanRequired = true;
	}

	/*
	 * @see IArchiveFileSet#setInWorkspace(boolean)
	 */
	public void setInWorkspace(boolean isInWorkspace) {
		attributeChanged(IN_WORKSPACE_ATTRIBUTE, new Boolean(isInWorkspace()), new Boolean(isInWorkspace));
		getFileSetDelegate().setInWorkspace(isInWorkspace);
		rescanRequired = true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet#setFlattened(boolean)
	 */
	public void setFlattened(boolean flat) {
		attributeChanged(FLATTENED_ATTRIBUTE, new Boolean(isFlattened()), new Boolean(flat));
		getFileSetDelegate().setFlattened(flat);
		//TODO:  rescanRequired = true;
	}
	
	/*
	 * @see IArchiveFileSet#setSourcePath(IPath, boolean)
	 */
	public void setSourcePath (IPath path) {
		Assert.isNotNull(path);
		IPath src = getSourcePath();
		attributeChanged(SOURCE_PATH_ATTRIBUTE, src == null ? null : src.toString(), path == null ? null : path.toString());
		getFileSetDelegate().setDir(path.toString());
		rescanRequired = true;
	}
	
	protected XbFileSet getFileSetDelegate () {
		return (XbFileSet)nodeDelegate;
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
		String s;
		if( isFlattened() )
			s = inputFile.toOSString().substring(getGlobalSourcePath().toOSString().length()+1);
		else
			s = inputFile.lastSegment();
		return new Path(s);
	}


	/*
	 * @see IArchiveFileSet#resetScanner()
	 */
	public void resetScanner() {
		rescanRequired = true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.ide.eclipse.archives.core.model.internal.ArchiveNodeImpl#validateChild(org.jboss.ide.eclipse.archives.core.model.IArchiveNode)
	 */
	public boolean validateModel() {
		return getAllChildren().length == 0 ? true : false; 
	}
	
	public boolean canBuild() {
		return getGlobalSourcePath() != null 
			&& super.canBuild();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{dir=");
		sb.append(getFileSetDelegate().getDir());
		sb.append(",includes=");
		sb.append(getFileSetDelegate().getIncludes());
		sb.append(",excludes=");
		sb.append(getFileSetDelegate().getExcludes());
		sb.append(",inWorkspace=");
		sb.append(getFileSetDelegate().isInWorkspace());
		sb.append(",flatten=");
		sb.append(getFileSetDelegate().isFlattened());
		return sb.toString();
	}

}
