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
package org.jboss.tools.deltacloud.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.tools.deltacloud.core.client.Instance;

/**
 * @author André Dietisheim
 */
public class DeltaCloudInstancesRepository extends AbstractDeltaCloudObjectRepository<DeltaCloudInstance, String> {

	/**
	 * Instantiates a new delta cloud instances.
	 */
	public DeltaCloudInstancesRepository() {
		super(DeltaCloudInstance.class);
	}

	/**
	 * Adds the.
	 *
	 * @param instance the instance
	 * @param cloud the cloud
	 * @return the delta cloud instance
	 */
	public DeltaCloudInstance add(Instance instance, DeltaCloud cloud) {
		DeltaCloudInstance deltaCloudInstance = new DeltaCloudInstance(cloud, instance);
		add(deltaCloudInstance);
		return deltaCloudInstance;
	}

	/**
	 * Adds the.
	 *
	 * @param instancesToAdd the instances to add
	 * @param cloud the cloud
	 * @return the collection
	 */
	public Collection<DeltaCloudInstance> add(Collection<Instance> instancesToAdd, DeltaCloud cloud) {
		List<DeltaCloudInstance> deltaCloudInstances = new ArrayList<DeltaCloudInstance>();
		for (Instance instance : instancesToAdd) {
			DeltaCloudInstance deltaCloudInstance = add(instance, cloud);
			deltaCloudInstances.add(deltaCloudInstance);
		}
		return deltaCloudInstances;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.deltacloud.core.AbstractDeltaCloudObjectRepository#matches(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected boolean matches(String id, DeltaCloudInstance instance) {
			return instance != null
					&& id.equals(instance.getId());
	}
}
