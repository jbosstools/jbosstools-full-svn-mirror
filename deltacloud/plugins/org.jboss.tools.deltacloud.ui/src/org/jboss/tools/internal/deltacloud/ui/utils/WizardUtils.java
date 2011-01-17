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
package org.jboss.tools.internal.deltacloud.ui.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author André Dietisheim
 */
public class WizardUtils {

	/**
	 * Runs the given job in the given wizard container. In order to have the
	 * wizard displaying a progress bar, you need to set
	 * Wizard#setNeedsProgressMonitor to <code>true</code>.
	 * 
	 * 
	 * @param job
	 *            the job to run
	 * @param container
	 *            the wizard container to run the job in
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @throws InterruptedException
	 *             the interrupted exception
	 *             
	 * @author André Dietisheim
	 */
	public static void runInWizard(final Job job, IWizardContainer container) throws InvocationTargetException,
			InterruptedException {
		container.run(true, false, new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask(job.getName(), IProgressMonitor.UNKNOWN);
				final CountDownLatch latch = new CountDownLatch(1);
				job.addJobChangeListener(new JobChangeAdapter() {

					@Override
					public void done(IJobChangeEvent event) {
						latch.countDown();
					}
				});
				job.schedule();
				latch.await();
				monitor.done();
			}
		});
	}

	/**
	 * Flips to the next wizard page or finishes the current wizard.
	 *
	 * @param wizardPage the wizard page this call is executed in
	 */
	public static void nextPageOrFinish(IWizardPage wizardPage) {
		IWizard wizard = wizardPage.getWizard();
		if (wizardPage.canFlipToNextPage()) {
			IWizardPage nextPage = wizard.getNextPage(wizardPage);
			wizard.getContainer().showPage(nextPage);
		 } else if (wizard.canFinish()) {
			 wizard.getContainer().getShell().close();
		 }
	}
}
