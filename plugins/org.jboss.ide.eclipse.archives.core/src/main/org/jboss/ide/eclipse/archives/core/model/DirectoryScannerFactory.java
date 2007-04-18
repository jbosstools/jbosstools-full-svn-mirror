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

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.core.runtime.IPath;

/**
 *
 * @author rob.stryker@jboss.com
 */
public class DirectoryScannerFactory {

	public static DirectoryScannerExtension createDirectoryScanner (IPath filesystemFolder, String includes, String excludes, boolean scan) {
		if (includes == null) includes = "";
		if (excludes == null) excludes = "";
		
		DirectoryScannerExtension scanner = new DirectoryScannerExtension();
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
	
	public static class DirectoryScannerExtension extends DirectoryScanner {
	    public boolean isIncluded(String name) {
	    	return super.isIncluded(name);
	    }
	}
}
