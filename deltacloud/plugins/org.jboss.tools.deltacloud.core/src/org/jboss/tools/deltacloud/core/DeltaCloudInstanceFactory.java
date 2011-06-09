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

import org.apache.deltacloud.client.Instance;
import org.eclipse.core.runtime.Assert;

/**
 * @author André Dietisheim
 */
public class DeltaCloudInstanceFactory {

	public static DeltaCloudInstance create(Instance instance, DeltaCloud cloud, String alias) {
		Assert.isLegal(instance != null);
		Assert.isLegal(cloud != null);
		Assert.isLegal(alias != null);

		DeltaCloudInstance deltaCloudInstance = new DeltaCloudInstance(instance, cloud);
		deltaCloudInstance.setAlias(alias);
		return deltaCloudInstance;
	}

	public static DeltaCloudInstance create(Instance instance, DeltaCloud cloud, Collection<IInstanceAliasMapping> instanceMappigs) {
		Assert.isLegal(instance != null);
		Assert.isLegal(cloud != null);

		DeltaCloudInstance deltaCloudInstance = new DeltaCloudInstance(instance, cloud);
		setAlias(deltaCloudInstance, instanceMappigs);
		return deltaCloudInstance;
	}

	public static Collection<DeltaCloudInstance> create(List<Instance> instances, DeltaCloud cloud, Collection<IInstanceAliasMapping> instanceMappings) {
		Assert.isLegal(instances != null);
		Assert.isLegal(cloud != null);
		
		List<DeltaCloudInstance> deltaCloudInstances = new ArrayList<DeltaCloudInstance>();
		for(Instance instance : instances) {
			DeltaCloudInstance deltaCloudInstance = create(instance, cloud, instanceMappings);
			deltaCloudInstances.add(deltaCloudInstance);
		}
		return deltaCloudInstances;
	}

	private static void setAlias(DeltaCloudInstance instance, Collection<IInstanceAliasMapping> aliasMappings) {
		if (aliasMappings == null || aliasMappings.size() == 0) {
			return;
		}
		for (IInstanceAliasMapping aliasMapping : aliasMappings) {
			if (aliasMapping.matches(instance)) {
				instance.setAlias(aliasMapping.getAlias());
			}
		}
	}
}
