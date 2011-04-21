package org.jboss.tools.smooks.utils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
/**
 * This class code comes from HibernateSynchronizer
 * @author Dart Peng
 * 
 * @CreateTime Jul 21, 2008
 */
public class ProjectClassLoader extends URLClassLoader {

	public ProjectClassLoader (IJavaProject project) throws JavaModelException  {
		super(getURLSFromProject(project, null), Thread.currentThread().getContextClassLoader());
	}

	public ProjectClassLoader (IJavaProject project, URL[] extraUrls) throws JavaModelException  {
		super(getURLSFromProject(project, extraUrls), Thread.currentThread().getContextClassLoader());
	}

	private static URL[] getURLSFromProject (IJavaProject project, URL[] extraUrls) throws JavaModelException {
		List list = new ArrayList();
		if (null != extraUrls) {
			for (int i=0; i<extraUrls.length; i++) {
				list.add(extraUrls[i]);
			}
		}
		
		IPackageFragmentRoot[] roots = project.getAllPackageFragmentRoots();
		String installLoc = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile().getAbsolutePath();
		installLoc = installLoc.replace('\\', '/');
		if (installLoc.endsWith("/")) installLoc = installLoc.substring(0, installLoc.length()-1);

		for (int i=0; i<roots.length; i++) {
			try {
				if (roots[i].isArchive()) {
					File f = new File(Platform.resolve(roots[i].getPath().makeAbsolute().toFile().toURL()).getFile());
					if (f.exists()) {
						list.add(Platform.resolve(roots[i].getPath().makeAbsolute().toFile().toURL()));
					}
					else {
						String s = roots[i].getPath().toOSString().replace('\\', '/');
						if (!s.startsWith("/")) s = "/" + s;
						f = new File(installLoc + s);
						if (f.exists()) {
							list.add(f.toURL());
						}
						else {
							f = new File("c:" + installLoc + s);
							if (f.exists()) {
								list.add(f.toURL());
							}
							else {
								f = new File("d:" + installLoc + s);
								if (f.exists()) {
									list.add(f.toURL());
								}
							}
						}
					}
				}
				else {
                    IPath path = roots[i].getJavaProject().getOutputLocation();
                    if (path.segmentCount() > 1) {
                        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                        path = root.getFolder(path).getLocation();
                        list.add(path.toFile().toURL());
                    }
                    else {
                        path = roots[i].getJavaProject().getProject().getLocation();
                        list.add(path.toFile().toURL());
                    }
				}
			}
			catch (Exception e) {}
		}
		
		URL[] urls = new URL[list.size()];
		int index = 0;
		for (Iterator i=list.iterator(); i.hasNext(); index++) {
			urls[index] = (URL) i.next();
		}
		return urls;
	}
}