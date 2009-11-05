/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.ide.eclipse.archives.webtools.modules;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.model.IModuleResourceDelta;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.util.PathUtils;
import org.jboss.ide.eclipse.archives.webtools.IntegrationPlugin;
import org.jboss.ide.eclipse.archives.webtools.Messages;
import org.jboss.ide.eclipse.archives.webtools.modules.PackageModuleFactory.PackagedModuleDelegate;
import org.jboss.ide.eclipse.as.core.publishers.LocalPublishMethod;
import org.jboss.ide.eclipse.as.core.publishers.PublishUtil;
import org.jboss.ide.eclipse.as.core.server.IDeployableServer;
import org.jboss.ide.eclipse.as.core.server.IJBossServerPublishMethod;
import org.jboss.ide.eclipse.as.core.server.IJBossServerPublisher;
import org.jboss.ide.eclipse.as.core.util.FileUtil;
import org.jboss.ide.eclipse.as.core.util.ServerConverter;
import org.jboss.ide.eclipse.as.core.util.FileUtil.FileUtilListener;

/**
 *
 * @author rob.stryker@jboss.com
 */
public class PackagesPublisher implements IJBossServerPublisher {

	protected IDeployableServer server;
	protected IModuleResourceDelta[] delta;
	protected FileUtilListener listener;
	public PackagesPublisher() {
	}

	public int getPublishState() {
		return IServer.PUBLISH_STATE_NONE;
	}

	public boolean accepts(String method, IServer server, IModule[] module) {
		if( LocalPublishMethod.LOCAL_PUBLISH_METHOD.equals(method) && module != null && module.length > 0
				&& PackageModuleFactory.MODULE_TYPE.equals(module[0].getModuleType().getId()))
			return true;
		return false;
	}
	public IStatus publishModule(
			IJBossServerPublishMethod method, 
			IServer server, IModule[] module,
			int publishType, IModuleResourceDelta[] delta,
			IProgressMonitor monitor)
			throws CoreException {
		listener = new FileUtilListener();
		this.server = ServerConverter.getDeployableServer(server);
		this.delta = delta;
		IModule module2 = module[0];

		try {
	    	// if it's being removed
	    	if( publishType == REMOVE_PUBLISH ) {
	    		removeModule(module2, monitor);
	    	} else if( publishType == FULL_PUBLISH ) {
	    		publishModule(module2, false, monitor);
	    	} else if( publishType == INCREMENTAL_PUBLISH ) {
	    		publishModule(module2, true, monitor);
	    	}
		}catch(Exception e) {
			IStatus status = new Status(IStatus.ERROR, IntegrationPlugin.PLUGIN_ID, 
					NLS.bind(Messages.ErrorDuringPublish, module2.getName()), e);
			return status;
		}
		
		if( listener.getStatuses().length > 0 ) {
			MultiStatus ms = new MultiStatus(IntegrationPlugin.PLUGIN_ID, IStatus.ERROR, 
					NLS.bind(Messages.ErrorDuringPublish, module2.getName()), null);
			for( int i = 0; i < listener.getStatuses().length; i++ ) {
				ms.add(listener.getStatuses()[i]);
			}
			return ms;
		}
		
		IStatus ret = new Status(IStatus.OK, IntegrationPlugin.PLUGIN_ID, 
				NLS.bind(Messages.PublishSuccessful, module2.getName()));
		return ret;
	}

	protected void removeModule(IModule module, IProgressMonitor monitor) {
		IArchive pack = getPackage(module);
		// remove all of the deployed items
		if( pack != null ) {
			IPath sourcePath = pack.getArchiveFilePath();
			IModule[] tree = new IModule[] { module };
			IPath destPath = PublishUtil.getDeployPath(tree, server);
			IPath destPath2 = destPath.append(sourcePath.lastSegment());
			// remove the entire file or folder
			FileUtil.safeDelete(destPath2.toFile(), listener);
		}
	}



	protected void publishModule(IModule module, boolean incremental, IProgressMonitor monitor) {
		IArchive pack = getPackage(module);
		IPath sourcePath = pack.getArchiveFilePath();
		IModule[] tree = new IModule[] { module };
		IPath destPathRoot = PublishUtil.getDeployPath(tree, server);

		// if destination is deploy directory... no need to re-copy!
		if( destPathRoot.toOSString().equals(PathUtils.getGlobalLocation(pack).toOSString())) {
			// fire null publish event
			return;
		}

		if( !incremental || !pack.isExploded() ) { // full publish, or zipped
			// full publish, copy whole folder or file
			FileUtil.fileSafeCopy(sourcePath.toFile(), destPathRoot.toFile(), listener);
		} else if( destPathRoot.toFile().exists() && destPathRoot.toFile().isFile()) {
			FileUtil.fileSafeCopy(sourcePath.toFile(), destPathRoot.toFile(), listener);
		} else {
			publishFromDelta(module, destPathRoot, sourcePath.removeLastSegments(1), delta);
		}
	}

	protected void publishFromDelta(IModule module, IPath destPathRoot, IPath sourcePrefix,
								IModuleResourceDelta[] delta) {
		ArrayList<IPath> changedFiles = new ArrayList<IPath>();
		for( int i = 0; i < delta.length; i++ ) {
			publishFromDeltaHandle(delta[i], destPathRoot, sourcePrefix, changedFiles);
		}
	}

	protected void publishFromDeltaHandle(IModuleResourceDelta delta, IPath destRoot,
			IPath sourcePrefix, ArrayList<IPath> changedFiles) {
		IModuleResource imr = delta.getModuleResource();
		File f = (File)imr.getAdapter(File.class);
		if( f != null ) {
			IPath destPath = destRoot.append(imr.getModuleRelativePath()).append(imr.getName());
			switch( delta.getKind()) {
			case IModuleResourceDelta.REMOVED:
				FileUtil.safeDelete(destPath.toFile(), listener);
				return;
			case IModuleResourceDelta.ADDED:
				FileUtil.fileSafeCopy(f, destPath.toFile(), listener);
				return;
			case IModuleResourceDelta.CHANGED:
				FileUtil.fileSafeCopy(f, destPath.toFile(), listener);
				break;
			}
		}
		IModuleResourceDelta[] children = delta.getAffectedChildren();
		if( children != null ) {
			for( int i = 0; i < children.length; i++ ) {
				publishFromDeltaHandle(children[i], destRoot, sourcePrefix, changedFiles);
			}
		}
	}

	protected IArchive getPackage(IModule module) {
		PackagedModuleDelegate delegate = (PackagedModuleDelegate)module.loadAdapter(PackagedModuleDelegate.class, new NullProgressMonitor());
		return delegate == null ? null : delegate.getPackage();
	}
}
