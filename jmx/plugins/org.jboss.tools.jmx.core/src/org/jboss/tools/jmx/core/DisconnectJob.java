/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    "Rob Stryker" <rob.stryker@redhat.com> - Initial implementation
 *******************************************************************************/
package org.jboss.tools.jmx.core;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class DisconnectJob extends ChainedJob {
	private IConnectionWrapper[] connection;
	public DisconnectJob(IConnectionWrapper[] connection) {
		super(JMXCoreMessages.DisconnectJob, JMXActivator.PLUGIN_ID);
		this.connection = connection;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			for( int i = 0; i < connection.length; i++ )
				if( connection[i].isConnected() )
					connection[i].disconnect();
			return Status.OK_STATUS;
		} catch( IOException ioe ) {
			return new Status(IStatus.ERROR, JMXActivator.PLUGIN_ID, JMXCoreMessages.DisconnectJobFailed, ioe);
		}
	}

}
