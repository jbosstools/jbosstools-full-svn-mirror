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

/**
 * A scheduling rule for jobs that prevents 2 jobs from running on the same
 * cloud instance.
 * 
 * @author André Dietisheim
 */
public class CloudSchedulingRule implements ISchedulingRule {

	private DeltaCloud cloud;

	public CloudSchedulingRule(DeltaCloud cloud) {
		this.cloud = cloud;
	}

	@Override
	public boolean contains(ISchedulingRule rule) {
		return rule == this;
	}

	@Override
	public boolean isConflicting(ISchedulingRule rule) {
		return isOnSameCloud(rule);
	}

	private boolean isOnSameCloud(ISchedulingRule rule) {
		if (isCloudRule(rule)) {
			CloudSchedulingRule cloudRule = (CloudSchedulingRule) rule;
			return cloud.equals(cloudRule.getCloud());
		} else {
			return false;
		}
	}

	private boolean isCloudRule(ISchedulingRule rule) {
		return CloudSchedulingRule.class.isAssignableFrom(rule.getClass());
	}

	protected DeltaCloud getCloud() {
		return cloud;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("[CloudSchedulingRule ")
				.append("@").append(System.identityHashCode(this)).append(" ")
				.append("cloud :\"")
				.append(getCloud().getName())
				.append("\"")
				.append("]")
				.toString();
	}
}
