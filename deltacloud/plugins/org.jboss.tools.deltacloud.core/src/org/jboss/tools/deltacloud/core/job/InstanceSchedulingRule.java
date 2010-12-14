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
		return super.isConflicting(rule)
				&& isOnSameInstance(rule);
	}

	private boolean isOnSameInstance(ISchedulingRule rule) {
		if (InstanceSchedulingRule.class.isAssignableFrom(rule.getClass())) {
			return instance.equals(((InstanceSchedulingRule) rule).getInstance());
		} else {
			// this rules conflicts with a cloud element rule which is not an instance rule
			return true;
		}
	}
	
	protected DeltaCloudInstance getInstance() {
		return instance;
	}

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
