/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.core.publishers;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.model.IModuleResourceDelta;
import org.jboss.ide.eclipse.as.core.JBossServerCorePlugin;
import org.jboss.ide.eclipse.as.core.Messages;
import org.jboss.ide.eclipse.as.core.extensions.events.IEventCodes;
import org.jboss.ide.eclipse.as.core.server.IDeployableServer;
import org.jboss.ide.eclipse.as.core.server.IJBossServerPublishMethod;
import org.jboss.ide.eclipse.as.core.server.IJBossServerPublisher;
import org.jboss.ide.eclipse.as.core.server.xpl.PublishCopyUtil;
import org.jboss.ide.eclipse.as.core.server.xpl.PublishCopyUtil.LocalCopyCallback;
import org.jboss.ide.eclipse.as.core.util.FileUtil;
import org.jboss.ide.eclipse.as.core.util.FileUtil.FileUtilListener;
import org.jboss.ide.eclipse.as.core.util.FileUtil.IFileUtilListener;
import org.jboss.ide.eclipse.as.core.util.IConstants;
import org.jboss.ide.eclipse.as.core.util.ServerConverter;

/**
 * This class provides a default implementation for packaging different types of
 * flexible projects. It uses the built-in heirarchy of the projects to do so.
 * 
 * @author rob.stryker@jboss.com
 */
public class JstPublisher extends PublishUtil implements IJBossServerPublisher {


	protected IModuleResourceDelta[] delta;
	protected IDeployableServer server;
	protected int publishState = IServer.PUBLISH_STATE_NONE;


	public JstPublisher() {
	}
	
	public boolean accepts(String type, IServer server, IModule[] module) {
		IDeployableServer ds = ServerConverter.getDeployableServer(server);
		boolean shouldAccept = ds != null && LocalPublishMethod.LOCAL_PUBLISH_METHOD.equals(type)
			&& !ds.zipsWTPDeployments()
			&& PublishUtil.isModuleCoreProject(module);
		return shouldAccept;
	}
	
	public IStatus publishModule(
			IJBossServerPublishMethod method,
			IServer server, IModule[] module, 
			int publishType, IModuleResourceDelta[] delta, 
			IProgressMonitor monitor) throws CoreException {
		IStatus status = null;
		this.server = ServerConverter.getDeployableServer(server);
		this.delta = delta;

		boolean deleted = false;
		for( int i = 0; i < module.length; i++ ) {
			if( module[i].isExternal() )
				deleted = true;
		}
		
		if (publishType == REMOVE_PUBLISH ) {
			status = unpublish(this.server, module, monitor);
		} else {
			if( deleted ) {
				publishState = IServer.PUBLISH_STATE_UNKNOWN;
			} else {
				if (publishType == FULL_PUBLISH ) {
					status = fullPublish(module, module[module.length-1], monitor);	
				} else if (publishType == INCREMENTAL_PUBLISH) {
					if(getDeployPath(module, this.server).toFile().isDirectory()) {
						status = incrementalPublish(module, module[module.length-1], monitor);
					} else {
						status =fullPublish(module, module[module.length-1], monitor);
					} 
				} 
			}
		}
		return status;
	}
		
	
	protected IStatus fullPublish(IModule[] moduleTree, IModule module, IProgressMonitor monitor) throws CoreException {
		IPath deployPath = getDeployPath(moduleTree, server);
		IPath tempDeployPath = getTempDeployFolder(moduleTree, server);
		IModuleResource[] members = getResources(module);
 
		ArrayList<IStatus> list = new ArrayList<IStatus>();
		// if the module we're publishing is a project, not a binary, clean it's folder
		if( !(new Path(module.getName()).segmentCount() > 1 ))
			list.addAll(Arrays.asList(localSafeDelete(deployPath)));

		if( !deployPackaged(moduleTree) && !isBinaryObject(moduleTree)) {
			LocalCopyCallback handler = new LocalCopyCallback(server.getServer(), deployPath, tempDeployPath);
			PublishCopyUtil util = new PublishCopyUtil(handler);
			list.addAll(Arrays.asList(util.publishFull(members, monitor)));
		}
		else if( isBinaryObject(moduleTree))
			list.addAll(Arrays.asList(copyBinaryModule(moduleTree)));
		else
			list.addAll(Arrays.asList(packModuleIntoJar(moduleTree[moduleTree.length-1], deployPath)));
		

		touchXMLFiles(deployPath);

		if( list.size() > 0 ) {
			MultiStatus ms = new MultiStatus(JBossServerCorePlugin.PLUGIN_ID, IEventCodes.JST_PUB_FULL_FAIL, 
					NLS.bind(Messages.FullPublishFail, module.getName()), null);
			for( int i = 0; i < list.size(); i++ )
				ms.add(list.get(i));
			return ms;
		}

		publishState = IServer.PUBLISH_STATE_NONE;
		IStatus ret = new Status(IStatus.OK, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.JST_PUB_FULL_SUCCESS, 
				NLS.bind(Messages.CountModifiedMembers, countMembers(module), module.getName()), null);
		return ret;
	}
	
	private void touchXMLFiles(IPath deployPath) {
		// adjust timestamps
		FileFilter filter = new FileFilter() {
			public boolean accept(File pathname) {
				if( pathname.getAbsolutePath().toLowerCase().endsWith(IConstants.EXT_XML))
					return true;
				return false;
			}
		};
		FileUtil.touch(filter, deployPath.toFile(), true);
	}

	protected IStatus incrementalPublish(IModule[] moduleTree, IModule module, IProgressMonitor monitor) throws CoreException {
		IStatus[] results = new IStatus[] {};
		IPath deployPath = getDeployPath(moduleTree, server);
		IPath tempDeployPath = getTempDeployFolder(moduleTree, server);
		LocalCopyCallback handler = null;
		if( !deployPackaged(moduleTree) && !isBinaryObject(moduleTree)) {
			handler = new LocalCopyCallback(server.getServer(), deployPath, tempDeployPath);
			results = new PublishCopyUtil(handler).publishDelta(delta, monitor);
		} else if( delta.length > 0 ) {
			if( isBinaryObject(moduleTree))
				results = copyBinaryModule(moduleTree);
			else
				results = packModuleIntoJar(moduleTree[moduleTree.length-1], deployPath);
		}
		if( results != null && results.length > 0 ) {
			MultiStatus ms = new MultiStatus(JBossServerCorePlugin.PLUGIN_ID, IEventCodes.JST_PUB_INC_FAIL, 
					NLS.bind(Messages.IncrementalPublishFail, module.getName()), null);
			for( int i = 0; i < results.length; i++ )
				ms.add(results[i]);
			return ms;
		}
		
		if( handler != null && handler.shouldRestartModule() )
			touchXMLFiles(deployPath);

		IStatus ret = new Status(IStatus.OK, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.JST_PUB_FULL_SUCCESS, 
				NLS.bind(Messages.CountModifiedMembers, countChanges(delta), module.getName()), null);
		return ret;
	}
	
	protected IStatus unpublish(IDeployableServer jbServer, IModule[] module,
			IProgressMonitor monitor) throws CoreException {
		IModule mod = module[module.length-1];
		IStatus[] errors = localSafeDelete(getDeployPath(module, server));
		if( errors.length > 0 ) {
			publishState = IServer.PUBLISH_STATE_FULL;
			MultiStatus ms = new MultiStatus(JBossServerCorePlugin.PLUGIN_ID, IEventCodes.JST_PUB_REMOVE_FAIL,
					NLS.bind(Messages.DeleteModuleFail, mod.getName()), 
					new Exception(Messages.DeleteModuleFail2));
			for( int i = 0; i < errors.length; i++ )
				ms.addAll(errors[i]);
			throw new CoreException(ms);
		}
		IStatus ret = new Status(IStatus.OK, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.JST_PUB_REMOVE_SUCCESS, 
				NLS.bind(Messages.ModuleDeleted, mod.getName()), null);
		return ret;
	}


	
	protected IStatus[] copyBinaryModule(IModule[] moduleTree) {
		try {
			IPath deployPath = getDeployPath(moduleTree, server);
			FileUtilListener listener = new FileUtilListener();
			IModuleResource[] members = getResources(moduleTree);
			File source = getFile(members[0]);
			if( source != null ) {
				FileUtil.fileSafeCopy(source, deployPath.toFile(), listener);
				return listener.getStatuses();
			} else {
				IStatus s = new Status(IStatus.ERROR, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.JST_PUB_COPY_BINARY_FAIL,
						NLS.bind(Messages.CouldNotPublishModule,
								moduleTree[moduleTree.length-1]), null);
				return new IStatus[] {s};
			}
		} catch( CoreException ce ) {
			IStatus s = new Status(IStatus.ERROR, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.JST_PUB_COPY_BINARY_FAIL,
					NLS.bind(Messages.CouldNotPublishModule,
							moduleTree[moduleTree.length-1]), null);
			return new IStatus[] {s};
		}
	}
	/**
	 * 
	 * @param deployPath
	 * @param event
	 * @return  returns whether an error was found
	 */
	protected IStatus[] localSafeDelete(IPath deployPath) {
        String serverDeployFolder = server.getDeployFolder();
        Assert.isTrue(!deployPath.toFile().equals(new Path(serverDeployFolder).toFile()), 
        		"An attempt to delete your entire deploy folder has been prevented. This should never happen"); //$NON-NLS-1$
        final ArrayList<IStatus> status = new ArrayList<IStatus>();
		IFileUtilListener listener = new IFileUtilListener() {
			public void fileCopied(File source, File dest, boolean result,Exception e) {}
			public void fileDeleted(File file, boolean result, Exception e) {
				if( result == false || e != null ) {
					status.add(new Status(IStatus.ERROR, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.JST_PUB_FILE_DELETE_FAIL, 
							NLS.bind(Messages.DeleteFileError, file.getAbsolutePath()),e));
				}
			}
			public void folderDeleted(File file, boolean result, Exception e) {
				if( result == false || e != null ) {
					status.add(new Status(IStatus.ERROR, JBossServerCorePlugin.PLUGIN_ID, IEventCodes.JST_PUB_FILE_DELETE_FAIL,
							NLS.bind(Messages.DeleteFolderError, file.getAbsolutePath()),e));
				}
			} 
		};
		FileUtil.safeDelete(deployPath.toFile(), listener);
		return (IStatus[]) status.toArray(new IStatus[status.size()]);
	}
	
	public int getPublishState() {
		return publishState;
	}
}
