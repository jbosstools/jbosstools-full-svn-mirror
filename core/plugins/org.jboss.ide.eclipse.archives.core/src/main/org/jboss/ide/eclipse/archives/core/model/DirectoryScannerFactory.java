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
package org.jboss.ide.eclipse.archives.core.model;

import java.io.File;
import java.util.HashMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.asf.DirectoryScanner;

/**
 * Utility methods to create scanners for matching
 * @author rob.stryker@jboss.com
 */
public class DirectoryScannerFactory {

	public static DirectoryScannerExtension createDirectoryScanner (IPath filesystemFolder, String includes, String excludes, boolean scan) {
		if (includes == null) includes = "";
		if (excludes == null) excludes = "";
		if( filesystemFolder == null ) 
			return null;
		
		DirectoryScannerExtension scanner = new DirectoryScannerExtension(false);
		String excludesList[] = excludes.split(" ?, ?");
		String includesList[] = includes.split(" ?, ?");
		
		scanner.setBasedir2(filesystemFolder);
		scanner.setExcludes(excludesList);
		scanner.setIncludes(includesList);
		if (scan) {
			scanner.scan();
		}
		
		return scanner;
	}

	public static DirectoryScannerExtension createDirectoryScanner(IArchiveFileSet fs, boolean scan) {
		if( !fs.isInWorkspace()) {
			return createDirectoryScanner(fs.getGlobalSourcePath(), fs.getIncludesPattern(), fs.getExcludesPattern(), scan);
		}
		
		// in workspace
		DirectoryScannerExtension scanner = new DirectoryScannerExtension(fs);
		if (scan) {
			scanner.scan();
		}
		return scanner;
	}
	
	/**
	 * Exposes the isIncluded method so that entire scans do not need to occur
	 * to find matches. 
	 * 
	 * Overwrites 
	 */
	public static class DirectoryScannerExtension extends DirectoryScanner {
		protected boolean workspaceRelative;
		// maps a File to it's workspace path
		protected HashMap<File, IPath> absoluteToWorkspace;
		protected IArchiveFileSet fs;
		
		public DirectoryScannerExtension(boolean relative) {
			workspaceRelative = relative;
			absoluteToWorkspace = new HashMap<File, IPath>();
		}
		
		public DirectoryScannerExtension(IArchiveFileSet fs) {
			this.fs = fs;
			String includes = fs.getIncludesPattern() == null ? "" : fs.getIncludesPattern();
			String excludes = fs.getExcludesPattern() == null ? "" : fs.getExcludesPattern();
			String includesList[] = includes.split(" ?, ?");
			String excludesList[] = excludes.split(" ?, ?");
			setExcludes(excludesList);
			setIncludes(includesList);
			workspaceRelative = fs.isInWorkspace();
			absoluteToWorkspace = new HashMap<File, IPath>();
			setBasedir2(fs.getSourcePath());
		}
		
		public void setBasedir2(IPath path) {
			IPath p = path;
			if( workspaceRelative ) {
				p = ArchivesCore.getInstance().getVFS()
					.workspacePathToAbsolutePath(path);
				absoluteToWorkspace.put(p.toFile(), path);
			}
			setBasedir(p.toFile());
		}
	    
		public IPath getWorkspacePath(IPath absolutePath) {
			return absoluteToWorkspace.get(absolutePath.toFile());
		}
		
	    protected File[] list2(File file) {
	    	return workspaceRelative ? list3(file) : file.listFiles();
	    }
	    
	    protected String getName(File file) {
	    	return workspaceRelative ? absoluteToWorkspace.get(file).lastSegment() : super.getName(file);
	    }
	    
	    /* Only used when workspace relative! */
	    protected File[] list3(File file) {
	    	IPath workspaceRelative = absoluteToWorkspace.get(file);
	    	
	    	if( workspaceRelative == null )
	    		return new File[0];
	    	
	    	IPath[] childrenWorkspace = ArchivesCore.getInstance()
	    			.getVFS().getWorkspaceChildren(workspaceRelative);
	    	IPath[] childrenAbsolute = ArchivesCore.getInstance()
	    			.getVFS().workspacePathToAbsolutePath(childrenWorkspace);
	    	File[] files = new File[childrenAbsolute.length];
	    	for( int i = 0; i < files.length; i++ ) {
	    		files[i] = childrenAbsolute[i].toFile();
	    		if( files[i] != null && childrenWorkspace[i] != null )
	    			absoluteToWorkspace.put(files[i], childrenWorkspace[i]);
	    	}
	    	return files;
	    }

	    public boolean isUltimatelyIncluded(String name) {
	    	return super.isIncluded(name) && !super.isExcluded(name);
	    }
	    
	    public IPath[] getAbsoluteIncludedFiles() {
	    	String[] relative = super.getIncludedFiles();
	    	if( workspaceRelative ) {
	    		IPath[] absolutes = new IPath[relative.length];
	    		for( int i = 0; i < relative.length; i++ ) 
	    			absolutes[i] = ArchivesCore.getInstance()
	    			.getVFS().workspacePathToAbsolutePath(fs.getSourcePath().append(relative[i]));
	    		return absolutes;
	    	} else {
				IPath base2 = new Path(basedir.getAbsolutePath());
				IPath[] absolutes = new IPath[relative.length];
				for( int i = 0; i < relative.length; i++ )
					absolutes[i] =  base2.append(relative[i]);
				return absolutes;
	    	}
	    }
	}
}
