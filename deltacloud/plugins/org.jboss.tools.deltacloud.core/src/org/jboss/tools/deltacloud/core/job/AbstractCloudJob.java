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
package org.jboss.tools.deltacloud.core.job;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.deltacloud.core.Activator;
import org.jboss.tools.deltacloud.core.DeltaCloud;

/**
 * @author André Dietisheim
 */
public abstract class AbstractCloudJob extends Job {

	private DeltaCloud cloud;

	public AbstractCloudJob(String name, DeltaCloud cloud) {
		super(name);
		this.cloud = cloud;
		// setUser(true);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		ISchedulingRule rule = getSchedulingRule();
		Job.getJobManager().beginRule(rule, monitor);
		monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);
		monitor.worked(1);
		try {
			return doRun(monitor);
		} catch (Exception e) {
			// TODO: internationalize strings
			return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID,
					MessageFormat.format("Could not {0}", getName()));
		} finally {
			Job.getJobManager().endRule(rule);
		}
	}

	protected DeltaCloud getCloud() {
		return cloud;
	}

	protected abstract IStatus doRun(IProgressMonitor monitor) throws Exception;

	protected ISchedulingRule getSchedulingRule() {
		return new CloudSchedulingRule(cloud);
	}
}
