/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.deltacloud.core;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.jboss.tools.common.log.StatusFactory;

public abstract class AbstractCloudJob extends Job {

	public AbstractCloudJob(String name) {
		super(name);
		// setUser(true);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);
		monitor.worked(1);
		try {
			return doRun(monitor);
		} catch (Exception e) {
			return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID,
					MessageFormat.format("Could not {0}", getName()));
		}
	}

	protected abstract IStatus doRun(IProgressMonitor monitor) throws Exception;

}
