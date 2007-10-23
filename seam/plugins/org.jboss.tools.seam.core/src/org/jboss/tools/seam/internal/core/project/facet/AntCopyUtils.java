/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.seam.internal.core.project.facet;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.util.FileUtils;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.seam.core.SeamCoreMessages;
import org.jboss.tools.seam.core.SeamCorePlugin;

/**
 * 
 * @author eskimo
 *
 */
public class AntCopyUtils {
	
	public static class FileSet {
		
		File dir = null;
		
		List<Pattern> include = new ArrayList<Pattern>();
		
		List<Pattern> exclude = new ArrayList<Pattern>();
		
		public FileSet(String dir) {
			this.dir = new File(dir);
		}
		
		public FileSet(File dir) {
			this.dir = dir;
		}
		
		public FileSet(FileSet template) {
			addTemplate(template);
		}
		
		public void addTemplate(FileSet template){
			include.addAll(template.getIncluded());
			exclude.addAll(template.getExcluded());
		}
		
		public FileSet() {
			
		}
		
		public FileSet add(FileSet set) {
			addTemplate(set);
			return this;
		}
		
		public FileSet dir(String dir) {
			this.dir = new File(dir);
			return this;
		}
		
		public FileSet dir(File dir) {
			this.dir = dir;
			return this;
		}
		
		public FileSet include(String pattern) {
			include.add(Pattern.compile(pattern));
			return this;
			
		}
		
		public FileSet exclude(String pattern) {
			exclude.add(Pattern.compile(pattern));
			return this;
		}
		
		public boolean isIncluded(String file) { 
			int i = dir.getAbsolutePath().length()+1;
			String relatedPath = file.substring(i);
			for (Pattern pattern : include) {			
				if(pattern.matcher(relatedPath.replace('\\', '/')).matches() ) {
					return !isExcluded(relatedPath);
				}
			}
			return false;
		}
		
		public boolean isExcluded(String file){
			for (Pattern pattern : exclude) {
				if(pattern.matcher(file.replace('\\', '/')).matches()) return true;
			}
			return false;	
		}
		
		public List<Pattern> getExcluded() {
			return Collections.unmodifiableList(exclude);
		}
		
		public List<Pattern> getIncluded() {
			return Collections.unmodifiableList(include);
		}
	
		/**
		 * @return
		 */
		public File getDir() {
			return dir;
		}
	}

	public static class FileSetFileFilter implements FileFilter {
		
		FileSet set;
		public FileSetFileFilter(FileSet set) {
			this.set = set;
		}
		
		public boolean accept(File pathname){
			return set.isIncluded(pathname.getAbsolutePath());
		}
	}

	public static void copyFilesAndFolders(File sourceFolder, File destinationFolder, FilterSetCollection set, boolean override) {
		copyFilesAndFolders(sourceFolder, destinationFolder,null, set, override);
	}
	
	public static void copyFilesAndFolders(File sourceFolder, File destinationFolder,
			AntCopyUtils.FileSetFileFilter fileSetFilter,
			FilterSetCollection filterSetCollection, boolean override) {
		if(!sourceFolder.exists()) {
			throw new IllegalArgumentException(NLS.bind(SeamCoreMessages.ANT_COPY_UTILS_COPY_FAILED,sourceFolder));
		}
		File[] files = fileSetFilter==null?sourceFolder.listFiles():sourceFolder.listFiles(fileSetFilter);
		
		if(files==null) {
			if(fileSetFilter==null) {
				throw new IllegalArgumentException(NLS.bind(SeamCoreMessages.ANT_COPY_UTILS_COULD_NOT_FIND_FOLDER,sourceFolder));
			}
		}
		for (File file : files) {
			if(file.isDirectory()) {
				copyFilesAndFolders(file,new File(destinationFolder,file.getName()),fileSetFilter,filterSetCollection,override);
			} else {
				try {
					if(!destinationFolder.exists()) 
						destinationFolder.mkdirs();
					FileUtils.getFileUtils().copyFile(file, new File(destinationFolder,file.getName()),filterSetCollection,override);
				} catch (IOException e) {
					SeamCorePlugin.getPluginLog().logError(e);
				}
			}
		}
		if(files.length==0 && !destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
	}
	
	public static void copyFileToFolder(File source, File dest, boolean override) {
			copyFileToFolder(source, dest,new FilterSetCollection(),override);
	}
	
	public static void copyFileToFolder(File source, File dest, FilterSetCollection filterSetCollection, boolean override ) {
		try {
			File destFile = new File(dest,source.getName());
			if(!source.equals(destFile)) {
				FileUtils.getFileUtils().copyFile(source, destFile ,filterSetCollection,override);
			}
		} catch (IOException e) {
			SeamCorePlugin.getPluginLog().logError(e);
		}		
	}
	
	public static void copyFileToFile(File source, File dest, FilterSetCollection filterSetCollection, boolean override ) {
		try {
			FileUtils.getFileUtils().copyFile(source, dest,filterSetCollection,override);
		} catch (IOException e) {
			SeamCorePlugin.getPluginLog().logError(e);
		}		
	}

	public static void copyFiles(File source, File dest, FileFilter filter) {
		dest.mkdir();
		
		File[] listFiles = source.listFiles(filter);
		if(listFiles==null) {
				throw new IllegalArgumentException(NLS.bind(SeamCoreMessages.ANT_COPY_UTILS_COULD_NOT_FIND_FOLDER,source));
		}
		for (File file:listFiles) {
			if(file.isDirectory())continue;
			try {
				FileUtils.getFileUtils().copyFile(file, new File(dest,file.getName()),new FilterSetCollection(),true);
			} catch (IOException e) {
				SeamCorePlugin.getPluginLog().logError(e);
			}
		}
	}
	
	public static void copyFiles(String[] files, File dest) {
		copyFiles(files,dest, true);
	}
	
	public static void copyFiles(String[] files, File dest,boolean override) {
		for (String fileName : files) {
			File file = new File(fileName);
			if(file.exists() && file.isFile()) {
				copyFileToFolder(file, dest, null, override);
			} else {
				try {
					SeamCorePlugin.getPluginLog().logError(NLS.bind(SeamCoreMessages.ANT_COPY_UTILS_CANNOT_COPY_JDBC_DRIVER_JAR,file.getCanonicalPath()));
				} catch (IOException e) {
					SeamCorePlugin.getPluginLog().logError(e);
				}
			}
		}
	}
}
