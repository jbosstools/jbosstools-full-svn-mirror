package org.jboss.ide.eclipse.archives.core.util;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;

import de.schlichtherle.io.ArchiveDetector;
import de.schlichtherle.io.File;

/**
 * This class is meant to bridge between the model
 * and the raw true-zip utility class. It is a higher level
 * API which deals with filesets and packages instead of 
 * raw Strings and paths.
 * 
 * It will also make sure that a de.schlichtherle.io.File is
 * created with the proper ArchiveDetector for each and every
 * level, rather than the TrueZipUtil class, which not accurately
 * create the proper File type for exploded archives.
 * 
 * @author rstryker
 *
 */
public class ModelTruezipBridge {
	public static void deleteArchive(IArchive archive) {
		final File file = getFile(archive);
		file.deleteAll();
		TrueZipUtil.sync();
	}
		
	public static void deleteFolder(IArchiveFolder folder) {
		IArchiveFileSet[] filesets = ModelUtil.findAllDescendentFilesets(folder);
		deleteFolder(folder,filesets);
	}
	
	public static void deleteFolder(IArchiveFolder folder, IArchiveFileSet[] filesets) {
		deleteFolder(folder, filesets, true);
	}
	public static void deleteFolder(final IArchiveFolder folder, final IArchiveFileSet[] filesets, boolean sync) {
		fullFilesetsRemove(filesets, false);
		final File file = getFile(folder);
		TrueZipUtil.deleteEmptyFolders(file);
		if( sync )
			TrueZipUtil.sync();
	}
	
	public static void fullFilesetBuild(IArchiveFileSet fileset) {
		fullFilesetBuild(fileset, true);
	}
	public static void fullFilesetBuild(final IArchiveFileSet fileset, boolean sync) {
		fileset.resetScanner();
		IPath[] paths = fileset.findMatchingPaths();
		copyFiles(fileset, paths, false);
		if( sync )
			TrueZipUtil.sync();
	}
		
	public static void fullFilesetsRemove(IArchiveFileSet[] filesets) {
		fullFilesetsRemove(filesets, true);
	}
	public static void fullFilesetsRemove(IArchiveFileSet[] filesets, boolean sync) {
		for( int i = 0; i < filesets.length; i++ )
			fullFilesetRemove(filesets[i], false);
		if( sync )
			TrueZipUtil.sync();
	}
	
	public static void fullFilesetRemove(IArchiveFileSet fileset) {
		fullFilesetRemove(fileset, true);
	}
	public static void fullFilesetRemove(final IArchiveFileSet fileset, boolean sync) {
		IPath[] paths = fileset.findMatchingPaths();
		for( int i = 0; i < paths.length; i++ ) {
			if( !ModelUtil.otherFilesetMatchesPath(fileset, paths[i])) {
				// remove
				deleteFiles(fileset, new IPath[] {paths[i]}, false);
			}
		}

		if( sync ) 
			TrueZipUtil.sync();
	}

	
	public static void copyFiles(IArchiveFileSet[] filesets, IPath[] paths) {
		copyFiles(filesets, paths, true);
	}
	
	public static void copyFiles(final IArchiveFileSet[] filesets, final IPath[] paths, boolean sync) {
		for( int i = 0; i < filesets.length; i++ ) {
			copyFiles(filesets[i], paths, false);
		}
		if( sync ) 
			TrueZipUtil.sync();
		
	}
	
	public static void copyFiles(IArchiveFileSet fileset, final IPath[] paths) {
		copyFiles(fileset, paths, true);
	}
	public static void copyFiles(IArchiveFileSet fileset, final IPath[] paths, boolean sync) {
		final File[] destFiles = getFiles(paths, fileset);
		for( int i = 0; i < paths.length; i++ ) {
			TrueZipUtil.copyFile(paths[i].toOSString(), destFiles[i]);
		}
		if( sync ) 
			TrueZipUtil.sync();
	}
	
	
	/*
	 * Deleting files
	 */
	public static void deleteFiles(IArchiveFileSet[] filesets, IPath[] paths ) {
		deleteFiles(filesets, paths, true);
	}
	public static void deleteFiles(final IArchiveFileSet[] filesets, final IPath[] paths, boolean sync ) {
		for( int i = 0; i < filesets.length; i++ ) {
			deleteFiles(filesets[i], paths, false);
		}
		if( sync ) 
			TrueZipUtil.sync();
	}
	
	public static void deleteFiles(IArchiveFileSet fileset, final IPath[] paths ) {
		deleteFiles(fileset, paths, true);
	}
	public static void deleteFiles(IArchiveFileSet fileset, final IPath[] paths, boolean sync ) {
		final File[] destFiles = getFiles(paths, fileset);
		for( int i = 0; i < paths.length; i++ ) {
			TrueZipUtil.deleteAll(destFiles[i]);
		}
		
		if( sync ) 	
			TrueZipUtil.sync();
	}
	
	
	/**
	 * Creates the file, folder, or archive represented by the node.
	 * Does nothing for filesets
	 * @param node
	 */
	public static void createFile(final IArchiveNode node) {
		createFile(node, true);
	}
	public static void createFile(final IArchiveNode node, boolean sync) {
		File f = getFile(node);
		if( f != null ) {
			f.mkdirs();
		}
		if( sync ) 
			TrueZipUtil.sync();
	}
	

	
	/**
	 * Gets all properly-created de.sch files for a fileset
	 * @param inputFiles
	 * @param fs
	 * @return
	 */
	private static File[] getFiles(IPath[] inputFiles, IArchiveFileSet fs ) {
		String filesetRelative;
		File fsFile = getFile(fs);
		File[] returnFiles = new File[inputFiles.length];
		int fsLength = fs.getGlobalSourcePath().toOSString().length()+1;
		for( int i = 0; i < inputFiles.length; i++ ) {
			filesetRelative = inputFiles[i].toOSString().substring(fsLength);
			returnFiles[i] = new File(fsFile, filesetRelative, ArchiveDetector.NULL);
		}
		return returnFiles;
	}
	
	
	/**
	 * This should go through the tree and create a file that is 
	 * correctly perceived at each step of the way. 
	 * 
	 * To just create a new File would let the Archive Detector have too 
	 * much control, and *ALL* war's and jars, including exploded ones, 
	 * would be treated as archives instead of folders. 
	 * @param node
	 * @return
	 */
	private static File getFile(IArchiveNode node) {
		if( node == null ) return null;
		
		if( node.getNodeType() == IArchiveNode.TYPE_MODEL ) return null;
		
		if( node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FILESET)
			return getFile(node.getParent());
		
		File parentFile = getFile(node.getParent());
		if( node.getNodeType() == IArchiveNode.TYPE_ARCHIVE ) {
			IArchive node2 = ((IArchive)node);
			boolean exploded = ((IArchive)node).isExploded();
			ArchiveDetector detector = exploded ? ArchiveDetector.NULL : ArchiveDetector.DEFAULT;
			if( parentFile == null ) 
				return new File(node2.getDestinationPath().append(node2.getName()).toOSString(), detector);
			return new File(parentFile, node2.getName(), detector);
		}
		if( node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FOLDER ) {
			return new File(parentFile, ((IArchiveFolder)node).getName(), ArchiveDetector.NULL);
		}
		return null;
	}
	
}
