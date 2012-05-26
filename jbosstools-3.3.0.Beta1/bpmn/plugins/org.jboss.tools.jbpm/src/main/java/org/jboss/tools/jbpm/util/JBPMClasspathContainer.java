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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.ClasspathAccessRule;
import org.eclipse.jdt.internal.core.ClasspathEntry;
import org.jboss.tools.jbpm.JBPMEclipsePlugin;

public class JBPMClasspathContainer implements IClasspathContainer {

    IClasspathEntry jbpmLibraryEntries[];
    IPath path;
    IJavaProject javaProject;

    public JBPMClasspathContainer(IJavaProject project, IPath path) {
        javaProject = null;
        javaProject = project;
        this.path = path;
    }

    public IClasspathEntry[] getClasspathEntries() {
        if (jbpmLibraryEntries == null) {
            jbpmLibraryEntries = createJBPMLibraryEntries(javaProject);
        }
        return jbpmLibraryEntries;
    }

    public String getDescription() {
        return "jBPM Library";
    }

    public int getKind() {
        return 1;
    }

    public IPath getPath() {
        return path;
    }

    private IClasspathEntry[] createJBPMLibraryEntries(IJavaProject project) {
        String[] jarNames = getJarNames(project);
        List<IClasspathEntry> list = new ArrayList<IClasspathEntry>();
        if (jarNames != null) {
	        for (int i = 0; i < jarNames.length; i++) {
	        	Path path = new Path(jarNames[i]);
	        	list.add(JavaCore.newLibraryEntry(path, path, null));
	        }
        }
        return (IClasspathEntry[]) list.toArray(new IClasspathEntry[list.size()]);
    }

    private String[] getJarNames(IJavaProject project) {
    	return JBPMRuntimeManager.getJBPMRuntimeJars(project.getProject());
    }

}