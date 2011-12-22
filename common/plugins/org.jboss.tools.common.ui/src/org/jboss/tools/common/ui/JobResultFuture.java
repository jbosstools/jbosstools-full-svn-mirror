/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.common.ui;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

/**
 * A Future that allows you to wait with a timeout for the result of a job.
 * 
 * @author André Dietisheim
 */
public class JobResultFuture implements Future<IStatus> {

	private AtomicBoolean cancelled = new AtomicBoolean();
	private ArrayBlockingQueue<IStatus> queue = new ArrayBlockingQueue<IStatus>(1);

	public JobResultFuture(Job job) {
		onJobFinished(job);
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		cancelled.set(true);
		return true;
	}

	@Override
	public boolean isCancelled() {
		return cancelled.get();
	}

	@Override
	public boolean isDone() {
		return queue.size() == 1;
	}

	@Override
	public IStatus get() throws InterruptedException, ExecutionException {
		return queue.poll();
	}

	@Override
	public IStatus get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
			TimeoutException {
		return queue.poll(timeout, unit);
	}

	private JobChangeAdapter onJobFinished(final Job job) {
		return new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {
				queue.offer(job.getResult());
			}
		};
	}
}
