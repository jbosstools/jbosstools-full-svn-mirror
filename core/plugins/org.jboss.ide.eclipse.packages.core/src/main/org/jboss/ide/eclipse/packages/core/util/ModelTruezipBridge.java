package org.jboss.ide.eclipse.packages.core.util;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;

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
	public static void deletePackage(IPackage pkg) {
		final File file = getFile(pkg);
		TrueZipUtil.syncExec(new Runnable() {
			public void run() {
				file.deleteAll();
			}
		});
	}
	
	public static void createContainer(IPackageNode node) {
		File f = getFile(node);
		f.mkdirs();
	}
	
	public static void deleteFolder(IPackageFolder folder) {
		IPackageFileSet[] filesets = ModelUtil.findAllDescendentFilesets(folder);
		deleteFolder(folder,filesets);
	}
	
	public static void deleteFolder(IPackageFolder folder, IPackageFileSet[] filesets) {
		// remove all files from filesets
		for( int i = 0; i < filesets.length; i++ )
			fullFilesetRemove(filesets[i]);
		
		final File file = getFile(folder);
		TrueZipUtil.syncExec(new Runnable() {
			public void run() {
				TrueZipUtil.deleteEmptyFolders(file);
			}
		});
	}
	
	public static void fullFilesetBuild(IPackageFileSet fileset) {
		fileset.resetScanner();
		IPath[] paths = fileset.findMatchingPaths();
		ModelTruezipBridge.copyFiles(fileset, paths);
	}
		
	public static void fullFilesetRemove(IPackageFileSet fileset) {
		IPath[] paths = fileset.findMatchingPaths();
		for( int i = 0; i < paths.length; i++ ) {
			if( !ModelUtil.otherFilesetMatchesPath(fileset, paths[i])) {
				// remove
				ModelTruezipBridge.deleteFiles(fileset, new IPath[] {paths[i]});
			}
		}
	}

	
	public static void copyFiles(IPackageFileSet[] filesets, IPath[] paths) {
		for( int i = 0; i < filesets.length; i++ ) {
			copyFiles(filesets[i], paths);
		}
	}
	
	public static void copyFiles(IPackageFileSet fileset, final IPath[] paths) {
		final File[] destFiles = getFiles(paths, fileset);
		TrueZipUtil.syncExec(new Runnable() {
			public void run() {
				for( int i = 0; i < paths.length; i++ ) {
					TrueZipUtil.copyFile(paths[i].toOSString(), destFiles[i]);
				}
			}
		});
	}
	
	public static void deleteFiles(IPackageFileSet[] filesets, IPath[] paths ) {
		for( int i = 0; i < filesets.length; i++ ) {
			deleteFiles(filesets[i], paths);
		}
	}
	public static void deleteFiles(IPackageFileSet fileset, final IPath[] paths ) {
		final File[] destFiles = getFiles(paths, fileset);
		TrueZipUtil.syncExec(new Runnable() {
			public void run() {
				for( int i = 0; i < paths.length; i++ ) {
					TrueZipUtil.deleteAll(destFiles[i]);
				}
			}
		});
	}
	
	/**
	 * Gets all properly-created de.sch files for a fileset
	 * @param inputFiles
	 * @param fs
	 * @return
	 */
	private static File[] getFiles(IPath[] inputFiles, IPackageFileSet fs ) {
		String filesetRelative;
		File fsFile = getFile(fs);
		File[] returnFiles = new File[inputFiles.length];
		int fsLength = fs.getSourcePath().toOSString().length()+1;
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
	private static File getFile(IPackageNode node) {
		if( node == null ) return null;
		
		if( node.getNodeType() == IPackageNode.TYPE_MODEL ) return null;
		
		if( node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
			return getFile(node.getParent());
		
		File parentFile = getFile(node.getParent());
		if( node.getNodeType() == IPackageNode.TYPE_PACKAGE ) {
			IPackage node2 = ((IPackage)node);
			boolean exploded = ((IPackage)node).isExploded();
			ArchiveDetector detector = exploded ? ArchiveDetector.NULL : ArchiveDetector.DEFAULT;
			if( parentFile == null ) 
				return new File(node2.getDestinationPath().append(node2.getName()).toOSString(), detector);
			return new File(parentFile, node2.getName(), detector);
		}
		if( node.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER ) {
			return new File(parentFile, ((IPackageFolder)node).getName(), ArchiveDetector.NULL);
		}
		return null;
	}
	
	
	/**
	 * Creates the file, folder, or archive represented by the node.
	 * Does nothing for filesets
	 * @param node
	 */
	public static void createFile(final IPackageNode node) {
		TrueZipUtil.syncExec(new Runnable() {
			public void run() {
				File f = getFile(node);
				if( f != null ) {
					f.mkdirs();
				}
			}
		});
	}
	
}
