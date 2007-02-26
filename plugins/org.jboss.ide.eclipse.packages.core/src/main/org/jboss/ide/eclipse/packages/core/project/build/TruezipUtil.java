package org.jboss.ide.eclipse.packages.core.project.build;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;

import de.schlichtherle.io.ArchiveDetector;
import de.schlichtherle.io.ArchiveException;
import de.schlichtherle.io.File;
import de.schlichtherle.io.FileOutputStream;

public class TruezipUtil {

	public static File getPackageFile (IPackage pkg)
	{
		return new File(pkg.getPackageFile().getRawLocation().toFile());
	}
	
	public static void umount (IPackage pkg)
	{
		File pkgFile = getPackageFile(pkg);
		
		try {
			File.umount(pkgFile);
		} catch (ArchiveException e) {
			Trace.trace(TruezipUtil.class, e);
		}
	}
	
	public static void umountAll ()
	{
		try {
			File.umount();
		} catch (ArchiveException e) {
			Trace.trace(TruezipUtil.class, e);
		}
	}
	
	public static OutputStream[] createFileOutputStreams (File[] files)
	{
		ArrayList streams = new ArrayList();		
		for (int i = 0; i < files.length; i++)
		{
			try {
				streams.add(new FileOutputStream(files[i]));
			} catch (FileNotFoundException e) {
				Trace.trace(TruezipUtil.class, e);
			}	
		}
		return (OutputStream[]) streams.toArray(new OutputStream[streams.size()]);
	}

	public static File[] createFiles (File[] roots, IPath subPath)
	{
		ArrayList files = new ArrayList();
		for (int i = 0; i < roots.length; i++)
		{
			files.add(new File(roots[i], subPath.toString(), ArchiveDetector.NULL));
		}
		
		return (File[]) files.toArray(new File[files.size()]); 
	}

	public static File[] createFiles (IPackageFileSet fileset, IPath subPath)
	{
		File[] roots = createFilesetRoots(fileset);
		return createFiles (roots, subPath);
	}

	public static File[] createFiles (IPackageFileSet fileset, IPath subPath, Hashtable pkgsAndPaths)
	{
		File[] roots = createFilesetRoots(fileset, pkgsAndPaths);
		return createFiles (roots, subPath);
	}

	public static File[] createFilesetRoots (IPackageFileSet fileset)
	{	
		Hashtable pkgsAndPaths = PackagesModel.instance().getTopLevelPackagesAndPathways(fileset);
		return createFilesetRoots (fileset, pkgsAndPaths);	
	}

	public static File[] createFilesetRoots (IPackageFileSet fileset, Hashtable pkgsAndPaths)
	{
		ArrayList roots = new ArrayList();
		
		for (Iterator iter = pkgsAndPaths.keySet().iterator(); iter.hasNext(); )
		{
			IPackage topLevelPackage = (IPackage) iter.next();
			ArrayList pathway = (ArrayList) pkgsAndPaths.get(topLevelPackage);
			
			File root = null;
			if (topLevelPackage.isDestinationInWorkspace())
			{
				IPath projectPath = ProjectUtil.getProjectLocation(topLevelPackage.getProject());
				IPath subPath = topLevelPackage.getDestinationContainer().getProjectRelativePath();
				root = new File(projectPath.append(subPath).toFile());
			} else {
				root = new File(topLevelPackage.getDestinationPath().toFile());
			}
			
			for (Iterator iter2 = pathway.iterator(); iter2.hasNext(); )
			{
				IPackageNode currentParent = (IPackageNode) iter2.next();
				
				if (currentParent.getNodeType() == IPackageNode.TYPE_PACKAGE
					|| currentParent.getNodeType() == IPackageNode.TYPE_PACKAGE_REFERENCE) {
					IPackage pkg = (IPackage)currentParent;
					root = new File(root, pkg.getName(), pkg.isExploded() ? ArchiveDetector.NULL : ArchiveDetector.DEFAULT);
				} else if (currentParent.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER) {
					IPackageFolder folder = (IPackageFolder)currentParent;
					root = new File(root, folder.getName(), ArchiveDetector.NULL);
				}
			}
			
			root.mkdirs();
			roots.add(root);
		}
		
		return (File[]) roots.toArray(new File[roots.size()]);
	}

}
