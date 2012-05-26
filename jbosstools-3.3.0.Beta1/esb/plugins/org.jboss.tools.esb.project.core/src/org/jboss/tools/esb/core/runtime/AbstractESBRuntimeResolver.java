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
import org.jboss.ide.eclipse.as.core.server.IJBossServerConstants;
import org.jboss.ide.eclipse.as.core.util.IJBossRuntimeResourceConstants;

public abstract class AbstractESBRuntimeResolver implements IESBRuntimeResolver {

	protected final static String JBOSSESB_ESB = "jbossesb.esb"; //$NON-NLS-1$
	protected final static String JBOSSESB_SAR = "jbossesb.sar"; //$NON-NLS-1$
	protected final static String SOAP_AS_LOCATION = "jboss-as"; //$NON-NLS-1$
	protected final static String ROSETTA_JAR = "jbossesb-rosetta.jar"; //$NON-NLS-1$
	protected final static String RIFTSAWESB_ESB = "riftsaw-esb.esb";//$NON-NLS-1$
	
	public List<IPath> getJarDirectories(String runtimeLocation, String configuration) {
		
		if("".equals(configuration)){
			configuration = IJBossRuntimeResourceConstants.DEFAULT_CONFIGURATION;
		}
		
		List<IPath> directories = new ArrayList<IPath>();
		
		IPath rtHome = new Path(runtimeLocation);
		IPath soapDeployPath = rtHome.append(SOAP_AS_LOCATION)
			.append(IJBossRuntimeResourceConstants.SERVER).append(configuration)
			.append(IJBossRuntimeResourceConstants.DEPLOY);
		IPath deployPath = rtHome.append(IJBossRuntimeResourceConstants.SERVER)
			.append(configuration).append(IJBossRuntimeResourceConstants.DEPLOY);

		IPath esbPath = deployPath.append(JBOSSESB_ESB);
		IPath sarPath = deployPath.append(JBOSSESB_SAR);
		IPath riftsawESBPath = deployPath.append(RIFTSAWESB_ESB);

		IPath libPath = rtHome.append("lib");
		
		directories.add(esbPath);
		directories.add(sarPath.append("lib"));
		directories.add(riftsawESBPath.append("lib"));
		directories.add(libPath.append(JBOSSESB_ESB));
		directories.add(libPath.append(JBOSSESB_SAR).append("lib"));
		directories.add(soapDeployPath.append(JBOSSESB_ESB));
		directories.add(soapDeployPath.append(JBOSSESB_SAR).append("lib"));
		
		return directories;
	}
	
	public File getRosettaJar(String runtimeLocation , String configuration){
		List<File> jars = getAllRuntimeJars(runtimeLocation, configuration);
		for(File file : jars){
			if(file.getName().equals(ROSETTA_JAR)){
				return file;
			}
		}
		
		return null;
	}
	
	public List<File> getAllRuntimeJars(String runtimeLocation, String configuration) {
		List<File> jarList = new ArrayList<File>();
		
		for(IPath dir : getJarDirectories(runtimeLocation, configuration)){
			List<File> tmpJarList = new ArrayList<File>();
			if(dir.toFile().exists()){
				tmpJarList = getJarsOfFolder(dir.toFile());
				jarList = mergeJarList(jarList, tmpJarList);
			}
		}
		
		return jarList;
	}



	private List<File> getJarsOfFolder(File folder){
		List<File> jars = new ArrayList<File>();
		if(folder.isDirectory()){
			for(File file: folder.listFiles()){
				if(file.isFile() && (file.getName().endsWith(".jar") || file.getName().endsWith(".zip"))){
					jars.add(file);
				}else if (folder.isDirectory()){
					jars.addAll(getJarsOfFolder(file));
				}
			}
		}
		
		return jars;
	}
	
	// if two folders have the same jar file, one of them will be ignored.
	private List<File> mergeJarList(List<File> jarList1, List<File> jarList2){
		List<File> duplicateList = new ArrayList<File>();
		
		for(File file : jarList1){
			for(File file2 : jarList2){
				if(file.getName().equals(file2.getName())){
					duplicateList.add(file);
				}
			}
		}
		jarList1.removeAll(duplicateList);
		jarList1.addAll(jarList2);
		
		return jarList1;
	}
	
}
