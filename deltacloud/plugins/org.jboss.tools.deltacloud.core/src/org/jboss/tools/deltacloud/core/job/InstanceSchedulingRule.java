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
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob.CLOUDELEMENT;

/**
 * A scheduling rule for jobs that prevents 2 jobs from running on the same
 * cloud instance.
 * 
 * @author André Dietisheim
 */
public class InstanceSchedulingRule extends CloudElementSchedulingRule {

	private DeltaCloudInstance instance;

	public InstanceSchedulingRule(DeltaCloud cloud, CLOUDELEMENT element, DeltaCloudInstance instance) {
		super(cloud, element);
		this.instance = instance;
	}

	@Override
	public boolean isConflicting(ISchedulingRule rule) {
		if (super.isConflicting(rule)) {
			if (isInstanceSchedulingRule(rule)) {
				return isOnSameInstance((InstanceSchedulingRule) rule);
			}
			return true;
		}
		return false;
	}

	private boolean isOnSameInstance(InstanceSchedulingRule rule) {
		return instance.equals(((InstanceSchedulingRule) rule).getInstance());
	}

	private boolean isInstanceSchedulingRule(ISchedulingRule rule) {
		return InstanceSchedulingRule.class.isAssignableFrom(rule.getClass());
	}

	protected DeltaCloudInstance getInstance() {
		return instance;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("[InstanceSchedulingRule ")
				.append("cloud :\"")
				.append(getCloud().getName())
				.append("\"")
				.append("instance :\"")
				.append(getInstance().getName())
				.append("\"")
				.append("]")
				.toString();
	}

}
