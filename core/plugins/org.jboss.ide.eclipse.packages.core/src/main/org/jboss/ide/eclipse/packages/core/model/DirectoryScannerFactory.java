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
package org.jboss.ide.eclipse.packages.core.model;

import java.io.File;

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.core.util.ProjectUtil;

/**
 *
 * @author rob.stryker@jboss.com
 */
public class DirectoryScannerFactory {

	public static DirectoryScanner createDirectoryScanner (IFile file) {
		return createDirectoryScanner(file, true);
	}
	
	public static DirectoryScanner createDirectoryScanner (IPath path) {
		return createDirectoryScanner(path, true);
	}
	
	public static DirectoryScanner createDirectoryScanner (IPath filesystemFolder, 
			String include, String excludes) {
		return createDirectoryScanner(filesystemFolder, include, excludes, true);
	}
	
	public static DirectoryScanner createDirectoryScanner (IContainer srcFolder, 
			String include, String excludes) {
		return createDirectoryScanner(srcFolder, include, excludes, true);
	}
	
	public static DirectoryScanner createDirectoryScanner (IFile file, boolean scan) {
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setBasedir(ProjectUtil.getProjectLocation(file.getProject()).toFile());
		scanner.setIncludes(new String[] { file.getProjectRelativePath().toString() });
		
		if (scan)
			scanner.scan();
		
		return scanner;
	}
	
	public static DirectoryScanner createDirectoryScanner (IPath path, boolean scan) {
		DirectoryScanner scanner = new DirectoryScanner();
		
		String filename = path.lastSegment();
		IPath parentPath = path.removeLastSegments(1);
		
		scanner.setBasedir(parentPath.toFile());
		scanner.setIncludes(new String[] { filename });
		
		if (scan)
			scanner.scan();
		
		return scanner;
	}
	
	public static DirectoryScanner createDirectoryScanner (IPath filesystemFolder, String includes, String excludes, boolean scan) {
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
	
	public static DirectoryScanner createDirectoryScanner (IContainer srcFolder, String includes, String excludes, boolean scan) {
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

}
