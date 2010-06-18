/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.esb.core.runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.as.core.util.IJBossRuntimeResourceConstants;

public class ESBRuntimeResolver_47 extends AbstractESBRuntimeResolver implements
		IESBRuntimeResolver {

	private final static String CONFIG_MODEL_JAR_11 = "jbossesb-config-model-1.1.0.jar";
	private final static String CONFIG_MODEL_JAR_12 = "jbossesb-config-model-1.2.0.jar";

	
	public boolean isValidESBRuntime(String location, String version, String configuration){
		List<String> jarNames = new ArrayList<String>();
		
		for(File file : getAllRuntimeJars(location, configuration)){
			jarNames.add(file.getName());
		}
			
		return jarNames.contains(ROSETTA_JAR)
				&& jarNames.contains(CONFIG_MODEL_JAR_11)
				&& jarNames.contains(CONFIG_MODEL_JAR_12);
	}
	
	
	public List<IPath> getJarDirectories(String runtimeLocation, String configuration) {
		List<IPath> directories = super.getJarDirectories(runtimeLocation, configuration);
		IPath rtHome = new Path(runtimeLocation);
		IPath soapDeployPath = rtHome.append(SOAP_AS_LOCATION)
			.append(IJBossRuntimeResourceConstants.SERVER).append(configuration)
			.append(IJBossRuntimeResourceConstants.DEPLOYERS)
			.append("esb.deployer").append("lib");
		
		IPath deployPath = rtHome
			.append(IJBossRuntimeResourceConstants.SERVER).append(configuration)
			.append(IJBossRuntimeResourceConstants.DEPLOYERS)
			.append("esb.deployer").append("lib");
		directories.add(soapDeployPath);
		directories.add(deployPath);
		
		return directories;
	}
	

}
