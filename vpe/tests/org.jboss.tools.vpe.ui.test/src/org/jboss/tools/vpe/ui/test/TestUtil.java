/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.test;

import org.jboss.tools.test.util.JobUtils;

/**
 * Class for importing project from jar file.
 * 
 * @author sdzmitrovich
 */
public class TestUtil extends org.jboss.tools.vpe.base.test.TestUtil {

	/**
	 * Wait until all background tasks are complete.
	 */
	public static void waitForJobs() {
		//commented by Maksim Areshkau
		//because this method wait only for jobs which has been runned in current thread,
		//and don't wait for others. It can cause https://jira.jboss.org/jira/browse/JBIDE-5820 
		//https://jira.jboss.org/jira/browse/JBIDE-5821 
//		while (Job.getJobManager().currentJob() != null)
//				delay(100);	
		waitForIdle();
	}
	
	/**
	 * Wait for idle.
	 */
	public static void waitForIdle(long maxIdle) {
		JobUtils.waitForIdle(500, maxIdle);
	}
	
	public static void waitForIdle() {
		waitForIdle(MAX_IDLE);
	}

}
