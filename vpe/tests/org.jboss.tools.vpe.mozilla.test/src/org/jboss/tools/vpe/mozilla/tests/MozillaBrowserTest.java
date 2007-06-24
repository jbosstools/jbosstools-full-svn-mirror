/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.mozilla.tests;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.vpe.mozilla.view.MozillaView;

public class MozillaBrowserTest extends TestCase {
	public static final String VIEW_ID
			= "org.jboss.tools.vpe.mozilla.view.MozillaView";
	/**
	* Process UI input but do not return for the
	* specified time interval.
	*
	* @param waitTimeMillis the number of milliseconds
	*/
	protected void delay(long waitTimeMillis) {
		Display display = Display.getCurrent();
		
		// If this is the UI thread,
		// then process input.
		if (display != null) {
			long endTimeMillis = System.currentTimeMillis() + waitTimeMillis;
			while (System.currentTimeMillis() < endTimeMillis) {
				if (!display.readAndDispatch())	display.sleep();
			}
			display.update();
		}
		// Otherwise, perform a simple sleep.
		else {
		   try {
			   Thread.sleep(waitTimeMillis);
		   }
		   catch (InterruptedException e) {
			   // Ignored.
		   }
		}
	}
	
	
	/**
	* Wait until all background tasks are complete.
	*/
	public void waitForJobs() {
	   while (Platform.getJobManager().currentJob() != null)
	      delay(1000);
	}
    
	public void testMozillaBrowser() throws PartInitException {
	    waitForJobs();
	    MozillaView mozillaView =
	    		(MozillaView) PlatformUI
	    				.getWorkbench()
						.getActiveWorkbenchWindow()
						.getActivePage()
						.showView(VIEW_ID);

	    // Delay for 3 seconds so that
	    // the Favorites view can be seen.
	    waitForJobs();
	    delay(3000);

	    PlatformUI.getWorkbench()
	    		.getActiveWorkbenchWindow()
				.getActivePage()
				.hideView(mozillaView);
	}
}
