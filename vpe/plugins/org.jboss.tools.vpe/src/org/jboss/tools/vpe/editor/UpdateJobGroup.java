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
package org.jboss.tools.vpe.editor;


import org.eclipse.ui.progress.UIJob;

/**
 * @author mareshkau
 * 
 * Creates a group of jobs for updating vpe content
 */

public abstract class UpdateJobGroup extends UIJob {

	/**
	 * Current job identification 
	 */
	private String jobName;
	/**
	 * Contains indetification for job group
	 */
	public static final String UPDATE_JOB="UPDATE_JOB"; //$NON-NLS-1$
	
	/**
	 *  Update Job
	 * @param name
	 * @param jobName
	 */
	public UpdateJobGroup(String name, String jobName) {
		super(name);
		setJobName(jobName);
	}
	
    public boolean belongsTo(Object family) {
    
    	return UPDATE_JOB.equals(getJobName());
    }
	/**
	 * @return the jobName
	 */
	private String getJobName() {
		return jobName;
	}
	/**
	 * @param jobName the jobName to set
	 */
	private void setJobName(String jobName) {
		this.jobName = jobName;
	}

}