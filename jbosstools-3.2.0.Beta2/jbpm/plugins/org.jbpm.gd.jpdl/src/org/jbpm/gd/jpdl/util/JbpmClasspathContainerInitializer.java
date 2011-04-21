/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jbpm.gd.jpdl.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.tools.jbpm.preferences.JbpmInstallation;
import org.jboss.tools.jbpm.preferences.PreferencesManager;
import org.jbpm.gd.jpdl.Logger;

public class JbpmClasspathContainerInitializer extends
		ClasspathContainerInitializer {
	
	private JbpmInstallation getJbpmInstallation(IPath containerPath) {
		String jbpmInstallationName = containerPath.lastSegment();
		return PreferencesManager.getInstance().getJbpmInstallation(jbpmInstallationName);
	}

	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
		JbpmInstallation installation = getJbpmInstallation(containerPath);
		if (installation == null) {
			Logger.logInfo("Could not instantiate the jbpm installation with name: " + containerPath.lastSegment());
			return;
		}
		JbpmClasspathContainer container = new JbpmClasspathContainer(project, installation);
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project }, 	new IClasspathContainer[] { container }, null);
	}
	

}
