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
package org.jboss.tools.internal.deltacloud.test.core.job;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.tools.deltacloud.client.Instance;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob.CLOUDELEMENT;
import org.jboss.tools.deltacloud.core.job.CloudElementSchedulingRule;
import org.jboss.tools.deltacloud.core.job.CloudSchedulingRule;
import org.jboss.tools.deltacloud.core.job.InstanceSchedulingRule;
import org.jboss.tools.internal.deltacloud.test.fakes.DeltaCloudFake;
import org.junit.Before;
import org.junit.Test;

/**
 * @author André Dietisheim
 */
public class CloudSchedulingRulesTest {

	private DeltaCloudFake cloudFake;
	private Instance instanceFake;
	private DeltaCloudInstance deltaCloudInstanceFake;

	@Before
	public void setUp() throws DeltaCloudException {
		this.cloudFake = new DeltaCloudFake();
		this.instanceFake = new Instance();
		this.deltaCloudInstanceFake = new DeltaCloudInstance(instanceFake, cloudFake);
	}

	@Test
	public void instanceRule2InstanceRule() throws DeltaCloudException {
		InstanceSchedulingRule instanceRule =
				new InstanceSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES, deltaCloudInstanceFake);
		assertTrue(instanceRule.isConflicting(instanceRule));
	}

	@Test
	public void instanceRule2CloudElementInstancesRule() throws DeltaCloudException {
		InstanceSchedulingRule instanceRule = new InstanceSchedulingRule(
				cloudFake, AbstractCloudElementJob.CLOUDELEMENT.INSTANCES, deltaCloudInstanceFake);
		CloudElementSchedulingRule cloudRule =
				new CloudElementSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES);
		assertTrue(instanceRule.isConflicting(cloudRule));
		assertTrue(cloudRule.isConflicting(instanceRule));
	}

	@Test
	public void instanceRule2CloudElementImagesRule() throws DeltaCloudException {
		InstanceSchedulingRule instanceRule =
				new InstanceSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES, deltaCloudInstanceFake);
		CloudElementSchedulingRule cloudRule = new CloudElementSchedulingRule(cloudFake, CLOUDELEMENT.IMAGES);
		assertFalse(instanceRule.isConflicting(cloudRule));
		assertFalse(cloudRule.isConflicting(instanceRule));
	}

	@Test
	public void instanceRule2CloudRule() throws DeltaCloudException {
		InstanceSchedulingRule instanceRule =
				new InstanceSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES, deltaCloudInstanceFake);
		CloudSchedulingRule cloudRule = new CloudSchedulingRule(cloudFake);
		assertTrue(instanceRule.isConflicting(cloudRule));
		assertTrue(cloudRule.isConflicting(instanceRule));
	}

	@Test
	public void cloudElementInstancesRule2CloudInstancesRule() throws DeltaCloudException {
		CloudElementSchedulingRule cloudInstancesRule =
				new CloudElementSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES);
		assertTrue(cloudInstancesRule.isConflicting(cloudInstancesRule));
	}

	@Test
	public void cloudElementInstancesRule2CloudImageRule() throws DeltaCloudException {
		CloudElementSchedulingRule cloudInstancesRule =
				new CloudElementSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES);
		CloudElementSchedulingRule cloudImagesRule =
				new CloudElementSchedulingRule(cloudFake, CLOUDELEMENT.IMAGES);
		assertFalse(cloudInstancesRule.isConflicting(cloudImagesRule));
		assertFalse(cloudImagesRule.isConflicting(cloudInstancesRule));
	}

	@Test
	public void cloudElementInstancesRule2CloudRule() throws DeltaCloudException {
		CloudElementSchedulingRule cloudInstancesRule =
				new CloudElementSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES);
		CloudSchedulingRule cloudRule = new CloudSchedulingRule(cloudFake);
		assertTrue(cloudInstancesRule.isConflicting(cloudRule));
		assertTrue(cloudRule.isConflicting(cloudInstancesRule));
	}

	@Test
	public void cloudElementImagesRule2CloudImagesRule() throws DeltaCloudException {
		CloudElementSchedulingRule cloudImagesRule =
				new CloudElementSchedulingRule(cloudFake, CLOUDELEMENT.IMAGES);
		assertTrue(cloudImagesRule.isConflicting(cloudImagesRule));
	}
}
