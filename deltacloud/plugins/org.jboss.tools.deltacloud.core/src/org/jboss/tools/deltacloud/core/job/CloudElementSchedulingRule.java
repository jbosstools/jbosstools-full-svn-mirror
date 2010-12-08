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

import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob.CLOUDELEMENT;

/**
 * A scheduling rule for jobs that prevents 2 jobs from running on the same
 * cloud instance.
 * 
 * @author André Dietisheim
 */
public class CloudElementSchedulingRule extends CloudSchedulingRule {

	private CLOUDELEMENT element;

	public CloudElementSchedulingRule(DeltaCloud cloud, CLOUDELEMENT element) {
		super(cloud);
		this.element = element;
	}

	@Override
	public boolean contains(ISchedulingRule rule) {
		return rule == this;
	}

	@Override
	public boolean isConflicting(ISchedulingRule rule) {
		return super.isConflicting(rule)
				&& isOnSameElement(rule);
	}

	private boolean isOnSameElement(ISchedulingRule rule) {
		return rule.getClass().isAssignableFrom(CloudElementSchedulingRule.class)
		&& ((CloudElementSchedulingRule) rule).getCloudElement().equals(element);
	}

	private CLOUDELEMENT getCloudElement() {
		return element;
	}
}
