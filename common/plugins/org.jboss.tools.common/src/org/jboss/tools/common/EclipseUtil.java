/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaModelManager;

/**
 * @author Alexey Kazakov, Viacheslav Kabanovich
 */
public class EclipseUtil {

	public static IJavaProject getJavaProject(IProject project) {
		try {
			if(project == null || !project.isOpen()) return null;
			if(!project.hasNature(JavaCore.NATURE_ID)) return null;
			return JavaCore.create(project);
		} catch (CoreException e) {
			CommonPlugin.getPluginLog().logError(e);
			return null;		
		}
	}

	public static String getJavaProjectOutputLocation(IProject project) {
		IJavaProject javaProject = getJavaProject(project);
		if(javaProject == null) return null;
		try {
			IPath p = javaProject.getOutputLocation();
			IResource r = project.getWorkspace().getRoot().findMember(p);
			return (r == null || r.getLocation() == null) ? null : r.getLocation().toString();
		} catch (CoreException e) {
			CommonPlugin.getPluginLog().logError(e);
			return null;
		}
	}

	public static IResource[] getJavaSourceRoots(IProject project) {
		IJavaProject javaProject = getJavaProject(project);
		if(javaProject == null) return null;
		List<IResource> resources = new ArrayList<IResource>();
		try {
			IClasspathEntry[] es = javaProject.getResolvedClasspath(true);
			for (int i = 0; i < es.length; i++) {
				if(es[i].getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					IResource findMember = ResourcesPlugin.getWorkspace().getRoot().findMember(es[i].getPath());
					if(findMember != null && findMember.exists()) {
						resources.add(findMember);
					}
				} 
			}
		} catch(CoreException ce) {
			CommonPlugin.getPluginLog().logError("Error while locating java source roots for " + project, ce); //$NON-NLS-1$
		}
		return resources.toArray(new IResource[resources.size()]);
	}

	/**
	 * Returns compilation unit for file, if it exists in Java model, otherwise returns null.
	 * 
	 * @param f
	 * @return compilation unit for file, if it exists in Java model, otherwise null
	 * @throws CoreException
	 */
	public static ICompilationUnit getCompilationUnit(IFile f) {
		IJavaProject jp = getJavaProject(f.getProject());
		if(jp != null) {
			IPackageFragment pkg = (IPackageFragment)JavaModelManager.determineIfOnClasspath(f, jp);
			if(pkg != null) {
				ICompilationUnit result = pkg.getCompilationUnit(f.getName());
				return (result.exists()) ? result : null;
			}
		}
		return null;
	}

	public static void addNatureToProject(IProject project, String natureId) throws CoreException {
		IProject proj = project.getProject();
		IProjectDescription description = proj.getDescription();
		String[] prevNatures = description.getNatureIds();
		if (findIndex(prevNatures, natureId) != -1) return; 		
		description.setNatureIds(append(prevNatures, natureId));
		proj.setDescription(description, null);
	}

	public static void addBuilderToProject(IProject project, String builderId) throws CoreException {
	    IProjectDescription desc = project.getDescription();
	    ICommand[] existing = desc.getBuildSpec();
	    for (ICommand command : existing) {
			if(builderId.equals(command.getBuilderName())) {
				return;
			}
		}
	    ICommand[] cmds = new ICommand[existing.length + 1];
	    ICommand newcmd = project.getDescription().newCommand();
	    newcmd.setBuilderName(builderId);
	    cmds[0] = newcmd;
	    System.arraycopy(existing, 0, cmds, 1, existing.length );
	    desc.setBuildSpec(cmds);
	    project.setDescription(desc, null);
	}

	public static void removeNatureFromProject(IProject project, String natureId) throws CoreException {
		IProject proj = project.getProject();
		IProjectDescription description = proj.getDescription();
		String[] prevNatures = description.getNatureIds();
		int natureIndex = findIndex(prevNatures, natureId);
		if(natureIndex == -1) return; 				
		description.setNatureIds(remove(prevNatures, natureIndex));
		proj.setDescription(description, null);
	}

	protected static String[] remove(String[] os, int index) {
		String[] ns = new String[os.length - 1];
		System.arraycopy(os, 0, ns, 0, index);
		System.arraycopy(os, index + 1, ns, index, os.length - (index + 1));
		return ns;
	}

	protected static int findIndex(String[] os, String s) {
		for (int i = 0; i < os.length; i++) 
			if(os[i].equals(s)) return i;
		return -1;
	}

	protected static String[] append(String[] os, String s) {
		String[] ns = new String[os.length + 1];
		System.arraycopy(os, 0, ns, 0, os.length);
		ns[os.length] = s;
		return ns;
	}
}