/**
 * JBoss, a Division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.as.core.publishers;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.model.IModuleResourceDelta;
import org.eclipse.wst.server.core.model.ServerBehaviourDelegate;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.as.core.ServerConverter;
import org.jboss.ide.eclipse.as.core.model.EventLogModel.EventLogTreeItem;
import org.jboss.ide.eclipse.as.core.module.PackageModuleFactory.PackagedModuleDelegate;
import org.jboss.ide.eclipse.as.core.publishers.PublisherEventLogger.PublishEvent;
import org.jboss.ide.eclipse.as.core.publishers.PublisherEventLogger.PublisherFileUtilListener;
import org.jboss.ide.eclipse.as.core.server.attributes.IDeployableServer;
import org.jboss.ide.eclipse.as.core.util.FileUtil;

/**
 *
 * @author rob.stryker@jboss.com
 */
public class PackagesPublisher implements IJBossServerPublisher {
	
	protected IDeployableServer server;
	protected IModuleResourceDelta[] delta;
	protected EventLogTreeItem eventRoot;
	
	public PackagesPublisher(IDeployableServer server, EventLogTreeItem eventContext) {
		this.server = server;
		eventRoot = eventContext;
	}
	public PackagesPublisher(IServer server, EventLogTreeItem eventContext) {
		this( ServerConverter.getDeployableServer(server), eventContext);
	}
	public void setDelta(IModuleResourceDelta[] delta) {
		this.delta = delta;
	}

	
	public int getPublishState() {
		return IServer.PUBLISH_STATE_NONE;
	}

    public void publishModule(int kind, int deltaKind, int modulePublishState, 
    		IModule module, IProgressMonitor monitor) throws CoreException {
		try {
	    	// if it's being removed
	    	if( deltaKind == ServerBehaviourDelegate.REMOVED ) {
	    		removeModule(module, kind, deltaKind, monitor);
	    		return;
	    	}
	    	
	    	if( deltaKind == ServerBehaviourDelegate.ADDED || deltaKind == ServerBehaviourDelegate.CHANGED) {
	    		publishModule(module, kind, deltaKind,  modulePublishState, monitor);
	    		return;
	    	}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected void removeModule(IModule module, int kind, int deltaKind, IProgressMonitor monitor) {
		IArchive pack = getPackage(module);
		// remove all of the deployed items
		PublishEvent event = PublisherEventLogger.createSingleModuleTopEvent(eventRoot, module, kind, deltaKind);
		IPath sourcePath = pack.getArchiveFilePath();
		IPath destPath = new Path(server.getDeployDirectory()).append(sourcePath.lastSegment());
		// remove the entire file or folder
		PublisherFileUtilListener listener = new PublisherFileUtilListener(event);
		FileUtil.safeDelete(destPath.toFile(), listener);
	}
	

	
	protected void publishModule(IModule module, int kind, int deltaKind, int modulePublishState, IProgressMonitor monitor) {
		PublishEvent event = PublisherEventLogger.createSingleModuleTopEvent(eventRoot, module, kind, deltaKind);
		IArchive pack = getPackage(module);
		IPath sourcePath = pack.getArchiveFilePath();
		IPath destPathRoot = new Path(server.getDeployDirectory());
		
		// if destination is deploy directory... no need to re-copy!
		if( destPathRoot.toOSString().equals(pack.getDestinationPath().toOSString())) {
			// fire null publish event
			return;
		}

		PublisherFileUtilListener listener = new PublisherFileUtilListener(event);

		if( shouldPublishIncremental(module, kind, deltaKind, modulePublishState) ) {
			IPath sourcePrefix = sourcePath.removeLastSegments(1);
			IPath destPath;
			
			ArrayList updated2 = new ArrayList();
			ArrayList removed2 = new ArrayList();
			fillFromDelta(updated2, removed2, delta);
			IPath[] updated = (IPath[]) updated2.toArray(new IPath[updated2.size()]);
			IPath[] removed = (IPath[]) removed2.toArray(new IPath[removed2.size()]);

			// incrementally remove removed files
			for( int j = 0; j < removed.length; j++ ) {
				destPath = destPathRoot.append(removed[j]);
				FileUtil.completeDelete(destPath.toFile(), listener);
			}
			
			// incrementally update updated files
			for( int j = 0; j < updated.length; j++ ) {
				destPath = destPathRoot.append(updated[j]);
				IPath srcp = sourcePrefix.append(updated[j]);
				FileUtil.fileSafeCopy(srcp.toFile(), destPath.toFile(), listener);
			}
			
		} else {
			// full publish, copy whole folder or file
			FileUtil.fileSafeCopy(sourcePath.toFile(), destPathRoot.append(sourcePath.lastSegment()).toFile(), listener);
		}
	}
	protected boolean shouldPublishIncremental(IModule module, int kind, int deltaKind, int modulePublishState) {
		if(modulePublishState == IServer.PUBLISH_STATE_FULL || kind == IServer.PUBLISH_FULL) 
			return false;
		return true;
	}
	protected void fillFromDelta(ArrayList updated2, ArrayList removed2, IModuleResourceDelta[] delta) {
		for( int j = 0; j < delta.length; j++ ) {
			switch(delta[j].getKind()) {
				case IModuleResourceDelta.ADDED:
				case IModuleResourceDelta.CHANGED:
					updated2.add(delta[j].getModuleResource().getModuleRelativePath());
					break;
				case IModuleResourceDelta.REMOVED:
					removed2.add(delta[j].getModuleResource().getModuleRelativePath());
					break;
			}
		}
	}
	
	protected IArchive getPackage(IModule module) {
		PackagedModuleDelegate delegate = (PackagedModuleDelegate)module.loadAdapter(PackagedModuleDelegate.class, new NullProgressMonitor());
		return delegate == null ? null : delegate.getPackage();
	}
}
