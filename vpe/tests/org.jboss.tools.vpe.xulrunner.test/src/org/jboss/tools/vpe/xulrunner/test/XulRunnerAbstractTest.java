/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.xulrunner.test;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.vpe.xulrunner.editor.XulRunnerEditor;
import org.jboss.tools.vpe.xulrunner.view.XulRunnerView;

import junit.framework.TestCase;

/**
 * @author Yahor Radtsevich (yradtsevich): extracted superclass from
 * {@code XulRunnerBrowserTest}.
 *
 */
public abstract class XulRunnerAbstractTest extends TestCase {

	public static final String VIEW_ID = "org.jboss.tools.vpe.xulrunner.view.XulRunnerView";

	/**
	 * Contains browser instamce
	 */
	protected XulRunnerEditor xulRunnerEditor;
	protected XulRunnerView xulRunnerView;
	
	public XulRunnerAbstractTest() {
		super();
	}

	/**
	 * @param name
	 */
	public XulRunnerAbstractTest(String name) {
		super(name);
	}

	/**
	 * Process UI input but do not return for the specified time interval.
	 * 
	 * @param waitTimeMillis
	 *                the number of milliseconds
	 */
	protected void delay(long waitTimeMillis) {
		Display display = Display.getCurrent();
		
		// If this is the UI thread,
		// then process input.
		if (display != null) {
		    long endTimeMillis = System.currentTimeMillis() + waitTimeMillis;
		    while (System.currentTimeMillis() < endTimeMillis) {
			if (!display.readAndDispatch())
			    display.sleep();
		    }
		    display.update();
		}
		// Otherwise, perform a simple sleep.
		else {
		    try {
			Thread.sleep(waitTimeMillis);
		    } catch (InterruptedException e) {
			// Ignored.
		    }
		}
	}

	/**
	 * Wait until all background tasks are complete.
	 */
	public void waitForJobs() {
		while (Job.getJobManager().currentJob() != null)
		    delay(1000);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		waitForJobs();
		xulRunnerView
			= ((XulRunnerView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().showView(VIEW_ID));

		// Delay for 3 seconds so that
		// the Favorites view can be seen.
		waitForJobs();
		// commented by dgolovin to get rid of jvm error [libexpat.so.0+0xeff4]
		//delay(3000);
		
		xulRunnerEditor = xulRunnerView.getBrowser();
	}
	
	@Override
	protected void tearDown() throws Exception {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.hideView(xulRunnerView);
		
		xulRunnerEditor = null;
		xulRunnerView = null;
		super.tearDown();
	}
}
