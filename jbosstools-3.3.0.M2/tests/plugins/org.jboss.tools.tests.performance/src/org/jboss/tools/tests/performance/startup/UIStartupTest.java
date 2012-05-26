/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.tests.performance.startup;

import junit.framework.*;
import org.eclipse.test.performance.*;

public class UIStartupTest extends TestCase {

	public static Test suite() {
		return new TestSuite(UIStartupTest.class);
	}

	public UIStartupTest(String methodName) {
		super(methodName);
	}

	public void testUIApplicationStartup() {
		PerformanceMeter meter = Performance.getDefault().createPerformanceMeter(getClass().getName() + '.' + getName());
		try {
			meter.stop();
			Performance performance = Performance.getDefault();
			performance.tagAsGlobalSummary(meter, "Core UI Startup", Dimension.ELAPSED_PROCESS);
			meter.commit();
			performance.assertPerformanceInRelativeBand(meter, Dimension.ELAPSED_PROCESS, -50, 5);
			System.out.println("sleeping....");
			try {
				Thread.sleep(5000);
			} catch  (InterruptedException ie) {
				
			}
		} finally {
			meter.dispose();
		}
	}
}
