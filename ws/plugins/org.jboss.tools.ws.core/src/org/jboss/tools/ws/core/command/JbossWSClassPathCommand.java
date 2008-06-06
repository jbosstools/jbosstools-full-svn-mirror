/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.ws.core.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.jboss.tools.ws.core.facet.delegate.IJBossWSFacetDataModelProperties;
import org.jboss.tools.ws.core.messages.JbossWSCoreMessages;
import org.jboss.tools.ws.core.utils.StatusUtils;

/**
 * @author Grid Qian
 */
public class JbossWSClassPathCommand extends AbstractDataModelOperation {

	IProject project;
	private IDataModel model;

	public JbossWSClassPathCommand(IProject project, IDataModel model) {
		this.project = project;
		this.model = model;
	}

	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return executeOverride(monitor);
	}

	public IStatus executeOverride(IProgressMonitor monitor) {
		IStatus status = Status.OK_STATUS;
		try {
			boolean isServerSupplied = model
					.getBooleanProperty(IJBossWSFacetDataModelProperties.JBOSS_WS_RUNTIME_IS_SERVER_SUPPLIED);
			if (isServerSupplied) {
				QualifiedName serverSupplied_qn = new QualifiedName(
						IJBossWSFacetDataModelProperties.QUALIFIEDNAME_IDENTIFIER_IS_SERVER_SUPPLIED,
						IJBossWSFacetDataModelProperties.PERSISTENT_PROPERTY_IS_SERVER_SUPPLIED_RUNTIME);
				project.setPersistentProperty(serverSupplied_qn, "1");
			} else {
				// store runtime name and runtime location to the project
				QualifiedName qRuntimeName = new QualifiedName(
						IJBossWSFacetDataModelProperties.QUALIFIEDNAME_IDENTIFIER_IS_SERVER_SUPPLIED,
						IJBossWSFacetDataModelProperties.JBOSS_WS_RUNTIME_ID);
				QualifiedName qRuntimeLocation = new QualifiedName(
						IJBossWSFacetDataModelProperties.QUALIFIEDNAME_IDENTIFIER_IS_SERVER_SUPPLIED,
						IJBossWSFacetDataModelProperties.JBOSS_WS_RUNTIME_ID);
				String runtimeName = model
						.getStringProperty(IJBossWSFacetDataModelProperties.JBOSS_WS_RUNTIME_ID);
				String runtimeLocation = model
						.getStringProperty(IJBossWSFacetDataModelProperties.JBOSS_WS_RUNTIME_HOME);
				project.setPersistentProperty(qRuntimeName, runtimeName);
				project
						.setPersistentProperty(qRuntimeLocation,
								runtimeLocation);
				status = addClassPath(project, runtimeName);
			}

		} catch (CoreException e) {
			status = StatusUtils.errorStatus(
					JbossWSCoreMessages.ERROR_ADD_FACET_JBOSSWS, e);
		}
		return status;
	}

	public IStatus addClassPath(IProject project, String segment) {
		IStatus status = Status.OK_STATUS;
		try {

			IJavaProject javaProject = JavaCore.create(project);

			IClasspathEntry newClasspath = JavaCore.newContainerEntry(new Path(
					JbossWSCoreMessages.JBossWS_Runtime_Lib).append(segment));

			IClasspathEntry[] oldClasspathEntries = javaProject
					.readRawClasspath();

			boolean isFolderInClassPathAlready = false;
			for (int i = 0; i < oldClasspathEntries.length
					&& !isFolderInClassPathAlready; i++) {
				if (oldClasspathEntries[i].getPath().equals(
						project.getFullPath())
						|| oldClasspathEntries[i].getPath().lastSegment()
								.toUpperCase().contains(
										JbossWSCoreMessages.JBossAS)) {
					isFolderInClassPathAlready = true;
					break;
				}
			}

			if (!isFolderInClassPathAlready) {

				IClasspathEntry[] newClasspathEntries = new IClasspathEntry[oldClasspathEntries.length + 1];
				for (int i = 0; i < oldClasspathEntries.length; i++) {
					newClasspathEntries[i] = oldClasspathEntries[i];
				}
				newClasspathEntries[oldClasspathEntries.length] = newClasspath;

				javaProject.setRawClasspath(newClasspathEntries,
						new NullProgressMonitor());
			}
		} catch (JavaModelException e) {
			status = StatusUtils.errorStatus(NLS.bind(
					JbossWSCoreMessages.Error_Copy, new String[] { e
							.getLocalizedMessage() }), e);
			return status;
		}

		return status;
	}

}