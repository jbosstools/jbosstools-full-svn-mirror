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
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;

/**
 * @author André Dietisheim
 */
public abstract class AbstractInstanceJob extends AbstractCloudElementJob {

	private DeltaCloudInstance instance;

	public AbstractInstanceJob(String name, DeltaCloudInstance instance) {
		this(name, instance, null);
	}

	public AbstractInstanceJob(String name, DeltaCloudInstance instance, String family) {
		super(name, instance.getDeltaCloud(), CLOUDELEMENT.INSTANCES, family);
		this.instance = instance;
	}

	@Override
	protected ISchedulingRule getSchedulingRule() {
		return new InstanceSchedulingRule(getCloud(), getCloudElement(), instance);
	}

	protected DeltaCloudInstance getInstance() {
		return instance;
	}
}
