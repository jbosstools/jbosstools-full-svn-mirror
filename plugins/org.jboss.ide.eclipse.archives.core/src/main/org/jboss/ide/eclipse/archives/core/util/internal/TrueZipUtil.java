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
package org.jboss.ide.eclipse.archives.core.util.internal;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;

import de.schlichtherle.io.ArchiveDetector;
import de.schlichtherle.io.ArchiveException;

/**
 * Accesses raw files with the truezip filesystem
 * @author <a href="rob.stryker@redhat.com">Rob Stryker</a>
 *
 */
public class TrueZipUtil {
	
	public static de.schlichtherle.io.File getFile(IPath path) {
		return getFile(path, ArchiveDetector.DEFAULT);
	}
	public static de.schlichtherle.io.File getFile(IPath path, ArchiveDetector detector) {
		return new de.schlichtherle.io.File(path.toOSString(), detector);
	}
	
	public static boolean pathExists(IPath path) {
		return pathExists( getFile(path));
	}
	public static boolean pathExists( de.schlichtherle.io.File file) {
		return file.exists();
	}
	

	public static long getTimestamp(IPath path) {
		return getTimestamp( getFile(path));
	}

	public static long getTimestamp(de.schlichtherle.io.File file) {
		return file.lastModified();
	}
	
	
	public static void copyFile(String source, IPath dest) throws IOException {
		copyFile(source, getFile(dest));
	}
	
	public static void copyFile(String source, de.schlichtherle.io.File file) {
		try {
			// make parent folders
			if( !file.getParentFile().exists() )
				file.getParentFile().mkdirs();
			
			de.schlichtherle.io.FileOutputStream fos = new de.schlichtherle.io.FileOutputStream(file);
			java.io.FileInputStream fis = new java.io.FileInputStream(source);
			
			byte[] buf = new byte[1024];
			int i = 0;
			while((i=fis.read(buf))!=-1) {
			  fos.write(buf, 0, i);
			  }
			fis.close();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    updateParentTimestamps(file);
	}
	
	public static void touchFile(IPath path) {
		de.schlichtherle.io.File f = getFile(path);
		f.setLastModified(System.currentTimeMillis());
	    updateParentTimestamps(path);
	}
	
	
	// Delete methods
	public static void deleteAll(IPath path, String fileName) {
		deleteAll(path.append(fileName));
	}
	public static void deleteAll(IPath path) {
		deleteAll(getFile(path));
	}
	public static void deleteAll(de.schlichtherle.io.File file) {
		file.deleteAll();
	}
	
	public static void deleteEmptyChildren(java.io.File file) {
		if( file.isDirectory() ) {
			java.io.File[] children = file.listFiles();
			for( int i = 0; i < children.length; i++ )
				deleteEmptyFolders(children[i]);
		}
	}
	public static void deleteEmptyFolders(java.io.File file ) {
		if( file.isDirectory() ) {
			java.io.File[] children = file.listFiles();
			for( int i = 0; i < children.length; i++ )
				deleteEmptyFolders(children[i]);
			if( file.listFiles().length == 0 )
				file.delete();
		}
	}
	
	
	public static void createFolder(IPath parent, String folderName) {
		createFolder(parent.append(folderName));
	}
	public static void createFolder(IPath path) {
		getFile(path, ArchiveDetector.NULL).mkdirs();
	    updateParentTimestamps(path);
	}
	public static void createArchive(IPath parent, String folderName) {
		createArchive(parent.append(folderName));
	}
	public static void createArchive(IPath path) {
		getFile(path).mkdirs();
	    updateParentTimestamps(path);
	}
	public static void umount() {
		try {
			de.schlichtherle.io.File.umount();
		} catch( ArchiveException ae ) {
		}
	}
	
	/**
	 * Sync's with file system after executing a runnable
	 * @param run Runnable or null
	 */
	public static void syncExec(Runnable run) {
		try {
			if( run != null )
				run.run();
		} catch (Exception e ) {}
		umount();
	}
	
	public static void sync() {
		syncExec(null);
	}
	
	public static void updateParentTimestamps(IPath path) {
		updateParentTimestamps(getFile(path));
	}
	public static void updateParentTimestamps(de.schlichtherle.io.File file) {
		long time = System.currentTimeMillis();
		de.schlichtherle.io.File parent = file.getEnclArchive();
		while( parent != null ) {
			parent.setLastModified(time);
			parent = parent.getEnclArchive();
		}

	}
}
