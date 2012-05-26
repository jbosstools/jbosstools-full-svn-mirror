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

package org.jboss.tools.esb.core.facet;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.jboss.ide.eclipse.as.wtp.core.vcf.VCFClasspathCommand;
import org.jboss.tools.esb.core.StatusUtils;
import org.jboss.tools.esb.core.messages.JBossFacetCoreMessages;
import org.jboss.tools.esb.core.runtime.JBossRuntimeClassPathInitializer;

/**
 * @author Denny Xu
 */
public class JBossClassPathCommand extends AbstractDataModelOperation {

	IProject project;
	private IDataModel model;

	public JBossClassPathCommand(IProject project, IDataModel model) {
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
				// store runtime name and runtime location to the project
				boolean isServerSupplied = model.getBooleanProperty(IJBossESBFacetDataModelProperties.RUNTIME_IS_SERVER_SUPPLIED);
				IPath esbContainerPath = null;
				if(isServerSupplied){
					String serverRuntimeId = getProjectTargetRuntimeID(project);
					esbContainerPath = new Path(JBossRuntimeClassPathInitializer.JBOSS_ESB_RUNTIME_CLASSPATH_CONTAINER_ID)
							.append(JBossRuntimeClassPathInitializer.JBOSS_ESB_RUNTIME_CLASSPATH_SERVER_SUPPLIED)
							.append(serverRuntimeId);
				}else{
					String runtimeName = model
							.getStringProperty(IJBossESBFacetDataModelProperties.RUNTIME_ID);
					String runtimeLocation = model
							.getStringProperty(IJBossESBFacetDataModelProperties.RUNTIME_HOME);
					String esbcontentFolder = model.getStringProperty(IJBossESBFacetDataModelProperties.ESB_CONTENT_FOLDER);
					project.setPersistentProperty(
									IJBossESBFacetDataModelProperties.PERSISTENCE_PROPERTY_QNAME_RUNTIME_NAME,
									runtimeName);
					project.setPersistentProperty(
									IJBossESBFacetDataModelProperties.PERSISTENCE_PROPERTY_RNTIME_LOCATION,
									runtimeLocation);
					project.setPersistentProperty(IJBossESBFacetDataModelProperties.QNAME_ESB_CONTENT_FOLDER, esbcontentFolder);
					esbContainerPath = new Path(JBossRuntimeClassPathInitializer.JBOSS_ESB_RUNTIME_CLASSPATH_CONTAINER_ID)
							.append(runtimeName );
				}
				
				// Add the esb container
				status = addClassPath(project, esbContainerPath);
				
				// Add the regular server container
//				IPath containerPath = new Path("org.eclipse.jst.server.core.container").append("org.jboss.ide.eclipse.as.core.server.runtime.runtimeTarget"); //$NON-NLS-1$ //$NON-NLS-2$
//				path = containerPath.append(id);

		} catch (CoreException e) {
			status = StatusUtils.errorStatus(
					JBossFacetCoreMessages.Error_Add_Facet_JBossESB, e);
		}
		return status;
	}

	private String getProjectTargetRuntimeID(IProject project) throws CoreException{
		IFacetedProject fp = ProjectFacetsManager.create(project);
		IRuntime runtime = fp.getPrimaryRuntime();
		if(runtime == null){
			return "";
		}
		return runtime.getProperty("id");
		
	}
	
	/**
	 * This can add *any* container path
	 * @param project
	 * @param path
	 * @return
	 */
	public static IStatus addClassPath(IProject project, IPath path) {
		return VCFClasspathCommand.addClassPath(project, path);
	}
}