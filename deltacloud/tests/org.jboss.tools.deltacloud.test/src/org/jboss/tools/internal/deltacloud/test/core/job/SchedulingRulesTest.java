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

import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.SecurePasswordStore;
import org.jboss.tools.deltacloud.core.client.Instance;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob.CLOUDELEMENT;
import org.jboss.tools.deltacloud.core.job.CloudElementSchedulingRule;
import org.jboss.tools.deltacloud.core.job.CloudSchedulingRule;
import org.jboss.tools.deltacloud.core.job.InstanceSchedulingRule;
import org.junit.Before;
import org.junit.Test;

/**
 * @author André Dietisheim
 */
public class SchedulingRulesTest {

	private DeltaCloudFake cloudFake;
	private Instance instanceFake;
	private DeltaCloudInstance deltaCloudInstanceFake;

	@Before
	public void setUp() throws DeltaCloudException {
		this.cloudFake = new DeltaCloudFake();
		this.instanceFake = new Instance();
		this.deltaCloudInstanceFake = new DeltaCloudInstance(cloudFake, instanceFake);
	}

	@Test
	public void instanceRule2InstanceRule() throws DeltaCloudException {
		InstanceSchedulingRule instanceRule =
				new InstanceSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES, deltaCloudInstanceFake);
		assertTrue(instanceRule.isConflicting(instanceRule));
	}

	@Test
	public void instanceRule2CloudElementInstanceRule() throws DeltaCloudException {
		InstanceSchedulingRule instanceRule = new InstanceSchedulingRule(
				cloudFake, AbstractCloudElementJob.CLOUDELEMENT.INSTANCES, deltaCloudInstanceFake);
		CloudElementSchedulingRule cloudRule =
				new CloudElementSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES);
		assertTrue(instanceRule.isConflicting(cloudRule));
	}

	@Test
	public void instanceRule2CloudElementImageRule() throws DeltaCloudException {
		InstanceSchedulingRule instanceRule =
				new InstanceSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES, deltaCloudInstanceFake);
		CloudElementSchedulingRule cloudRule = new CloudElementSchedulingRule(cloudFake, CLOUDELEMENT.IMAGES);
		assertFalse(instanceRule.isConflicting(cloudRule));
	}

	@Test
	public void instanceRule2CloudRule() throws DeltaCloudException {
		InstanceSchedulingRule instanceRule =
				new InstanceSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES, deltaCloudInstanceFake);
		CloudSchedulingRule cloudRule = new CloudSchedulingRule(cloudFake);
		assertTrue(instanceRule.isConflicting(cloudRule));
	}

	@Test
	public void cloudInstancesRule2CloudInstancesRule() throws DeltaCloudException {
		CloudElementSchedulingRule cloudInstancesRule =
				new CloudElementSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES);
		assertTrue(cloudInstancesRule.isConflicting(cloudInstancesRule));
	}

	@Test
	public void cloudInstancesRule2CloudImageRule() throws DeltaCloudException {
		CloudElementSchedulingRule cloudInstancesRule =
				new CloudElementSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES);
		CloudElementSchedulingRule cloudImagesRule =
				new CloudElementSchedulingRule(cloudFake, CLOUDELEMENT.IMAGES);
		assertFalse(cloudInstancesRule.isConflicting(cloudImagesRule));
	}

	@Test
	public void cloudInstancesRule2CloudRule() throws DeltaCloudException {
		CloudElementSchedulingRule cloudInstancesRule =
				new CloudElementSchedulingRule(cloudFake, CLOUDELEMENT.INSTANCES);
		CloudSchedulingRule cloudRule = new CloudSchedulingRule(cloudFake);
		assertTrue(cloudInstancesRule.isConflicting(cloudRule));
	}

	private static class DeltaCloudFake extends DeltaCloud {

		public DeltaCloudFake() throws DeltaCloudException {
			super("mock", "http://dummy.org", "dummyUser", "dummyPassword");
		}

		@Override
		protected SecurePasswordStore createSecurePasswordStore(String name, String username, String password) {
			return new SecurePasswordStoreFake("dummyPassword");
		}
	}

	private static class SecurePasswordStoreFake extends SecurePasswordStore {

		private String password;

		public SecurePasswordStoreFake(String password) {
			super(null, password);
			this.password = password;
		}

		@Override
		public String getPassword() throws DeltaCloudException {
			return password;
		}

		@Override
		public void setPassword(String password) throws DeltaCloudException {
			this.password = password;
		}

		@Override
		public void update(IStorageKey key, String password) throws DeltaCloudException {
			setPassword(password);
		}

		@Override
		public void remove() throws DeltaCloudException {
		}

	}
}
