/**
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.tools.jbpm.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.jbpm.JBPMEclipsePlugin;

public class ProjectClassLoader {
	
	public static URLClassLoader getProjectClassLoader(IEditorPart editor) {
		IEditorInput input = editor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			return getProjectClassLoader(((IFileEditorInput) input).getFile());
		}
		return null;
	}

	public static URLClassLoader getProjectClassLoader(IFile file) {
		IProject project = file.getProject();
		IJavaProject javaProject = JavaCore.create(project);
		return getProjectClassLoader(javaProject);
	}

    public static URLClassLoader getProjectClassLoader(IJavaProject project) {
        List<URL> pathElements = getProjectClassPathURLs(project, new ArrayList<String>());
        URL urlPaths[] = pathElements.toArray(new URL[pathElements.size()]);
        return new URLClassLoader(urlPaths, Thread.currentThread().getContextClassLoader());
    }

    private static URL getRawLocationURL(IPath simplePath)
            throws MalformedURLException {
        File file = getRawLocationFile(simplePath);
        return file.toURI().toURL();
    }

    private static File getRawLocationFile(IPath simplePath) {
        IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(simplePath);
        File file = null;
        if (resource != null) {
            file = ResourcesPlugin.getWorkspace().getRoot().findMember(
                    simplePath).getRawLocation().toFile();
        } else {
            file = simplePath.toFile();
        }
        return file;
    }

    public static List<URL> getProjectClassPathURLs(IJavaProject project, List<String> alreadyLoadedProjects) {
        List<URL> pathElements = new ArrayList<URL>();
        try {
            IClasspathEntry[] paths = project.getResolvedClasspath(true);
            Set<IPath> outputPaths = new HashSet<IPath>();
            if (paths != null) {
                for ( int i = 0; i < paths.length; i++ ) {
                    IClasspathEntry path = paths[i];
                    if (path.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
                        URL url = getRawLocationURL(path.getPath());
                        pathElements.add(url);
                    } else if (path.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                    	IPath output = path.getOutputLocation();
                    	if (path.getOutputLocation() != null) {
                    		outputPaths.add(output);
                    	}
                    }
                }
            }
            IPath location = getProjectLocation(project.getProject());
            IPath outputPath = location.append(project.getOutputLocation().removeFirstSegments(1));
            pathElements.add(0, outputPath.toFile().toURI().toURL());
            for (IPath path: outputPaths) {
            	outputPath = location.append(path.removeFirstSegments(1));
                pathElements.add(0, outputPath.toFile().toURI().toURL());
            }
            
            // also add classpath of required projects
            for (String projectName: project.getRequiredProjectNames()) {
            	if (!alreadyLoadedProjects.contains(projectName)) {
            		alreadyLoadedProjects.add(projectName);
		            IProject reqProject = project.getProject().getWorkspace()
		                .getRoot().getProject(projectName);
		            if (reqProject != null) {
		                IJavaProject reqJavaProject = JavaCore.create(reqProject);
		                pathElements.addAll(getProjectClassPathURLs(reqJavaProject, alreadyLoadedProjects));
		            }
            	}
            }
        } catch (JavaModelException e) {
            JBPMEclipsePlugin.log(e);
        } catch (MalformedURLException e) {
            JBPMEclipsePlugin.log(e);
        } catch (Throwable t) {
        	JBPMEclipsePlugin.log(t);
        }
        return pathElements;
    }
    
    public static IPath getProjectLocation(IProject project) {
        if (project.getRawLocation() == null) {
            return project.getLocation();
        } else {
            return project.getRawLocation();
        }
    }
}
