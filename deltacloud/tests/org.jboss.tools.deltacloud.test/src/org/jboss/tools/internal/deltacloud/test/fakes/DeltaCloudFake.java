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
package org.jboss.tools.internal.deltacloud.test.fakes;

import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.SecurePasswordStore;

/**
 * @author André Dietisheim
 */
public class DeltaCloudFake extends DeltaCloud {

	public DeltaCloudFake() throws DeltaCloudException {
		this("http://dummy.org", "aUser", "aPassword");
	}
	
	public DeltaCloudFake(String url, String username, String password) throws DeltaCloudException {
		super("fake", url, username, password);
	}

	@Override
	protected SecurePasswordStore createSecurePasswordStore(String name, String username, String password) {
		return new SecurePasswordStoreFake("dummyPassword");
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