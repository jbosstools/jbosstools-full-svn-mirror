/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.core.server.internal.v7;

import java.text.MessageFormat;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.core.JBossServerCorePlugin;
import org.jboss.ide.eclipse.as.core.Messages;
import org.jboss.ide.eclipse.as.core.extensions.polling.WebPortPoller;
import org.jboss.ide.eclipse.as.core.server.IJBoss7ManagerService;
import org.jboss.ide.eclipse.as.core.server.IServerStatePoller;
import org.jboss.ide.eclipse.as.core.server.internal.LocalJBossBehaviorDelegate;
import org.jboss.ide.eclipse.as.core.util.PollThreadUtils;
import org.jboss.ide.eclipse.as.core.util.ServerConverter;

public class LocalJBoss7BehaviorDelegate extends LocalJBossBehaviorDelegate {

	private IJBoss7ManagerService service;

	private LocalJBoss7BehaviorDelegate() throws Exception {
		this.service = JBoss7ManagerUtil.getService(getServer());
	}

	public IStatus canChangeState(String launchMode) {
		return Status.OK_STATUS;
	}

	@Override
	public void stop(boolean force) {
		if (force) {
			forceStop();
		} else {
			IStatus result = gracefullStop();
			if (!result.isOK()) {
				forceStop();
			}
		}
	}

	@Override
	protected IStatus gracefullStop() {
		IServer server = getServer();
		IJBoss7ManagerService service = null;
		try {
			service = JBoss7ManagerUtil.getService(server);
			JBoss7Server jbossServer = ServerConverter.checkedGetJBossServer(server, JBoss7Server.class);
			service.stop(jbossServer.getHost(), jbossServer.getManagementPort());
			return Status.OK_STATUS;
		} catch (Exception e) {
			return new Status(
					IStatus.ERROR, JBossServerCorePlugin.PLUGIN_ID,
					MessageFormat.format(Messages.JBoss7ServerBehavior_could_not_stop, server.getName()), e);
		} finally {
			JBoss7ManagerUtil.dispose(service);
		}
	}

	@Override
	protected void pollServer(final boolean expectedState) {
		// IF shutting down a process started OUTSIDE of eclipse, force use the web poller, 
		// since there's no process watch for shutdowns
		if( !expectedState 
				&& process == null ) {
			IServerStatePoller poller = PollThreadUtils.getPoller(expectedState, getServer());
			poller = PollThreadUtils.getPoller(WebPortPoller.WEB_POLLER_ID);
			pollServer(expectedState, poller);
		} else {
			super.pollServer(expectedState);
		}
	}

	@Override
	public void dispose() {
		JBoss7ManagerUtil.dispose(service);
	}
}
