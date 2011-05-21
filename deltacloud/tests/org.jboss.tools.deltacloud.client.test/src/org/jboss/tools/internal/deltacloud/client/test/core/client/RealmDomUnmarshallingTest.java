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
package org.jboss.tools.internal.deltacloud.client.test.core.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.deltacloud.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.client.Realm;
import org.jboss.tools.deltacloud.client.Realm.RealmState;
import org.jboss.tools.deltacloud.client.unmarshal.RealmUnmarshaller;
import org.jboss.tools.deltacloud.client.unmarshal.RealmsUnmarshaller;
import org.jboss.tools.internal.deltacloud.client.test.fakes.RealmResponseFakes;
import org.jboss.tools.internal.deltacloud.client.test.fakes.RealmResponseFakes.RealmsResponse;
import org.junit.Test;

/**
 * @author André Dietisheim
 */
public class RealmDomUnmarshallingTest {

	@Test
	public void realmMayBeUnmarshalled() throws DeltaCloudClientException {
		Realm realm = new Realm();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(RealmResponseFakes.realmResponse.response.getBytes());
		new RealmUnmarshaller().unmarshall(inputStream, realm);
		assertNotNull(realm);
		assertEquals(RealmResponseFakes.realmResponse.id, realm.getId());
		assertEquals(RealmResponseFakes.realmResponse.name, realm.getName());
		assertEquals(RealmState.valueOf(RealmResponseFakes.realmResponse.state.toUpperCase()), realm.getState());
		assertEquals(RealmResponseFakes.realmResponse.getIntLimit(), realm.getLimit());
	}

	@Test
	public void emptyLimitSetsDefaultLimit() throws DeltaCloudClientException {
		Realm realm = new Realm();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(RealmResponseFakes.invalidLimitRealmResponse.response.getBytes());
		new RealmUnmarshaller().unmarshall(inputStream, realm);
		assertNotNull(realm);
		assertEquals(Realm.LIMIT_DEFAULT, realm.getLimit());
	}

	@Test
	public void invalidStateSetsUNKNOWNState() throws DeltaCloudClientException {
		Realm realm = new Realm();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(RealmResponseFakes.invalidLimitRealmResponse.response.getBytes());
		new RealmUnmarshaller().unmarshall(inputStream, realm);
		assertNotNull(realm);
		assertEquals(Realm.LIMIT_DEFAULT, realm.getLimit());
	}

	@Test
	public void realmsMayBeUnmarshalled() throws DeltaCloudClientException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(RealmsResponse.response.getBytes());
		List<Realm> realms = new ArrayList<Realm>();
		new RealmsUnmarshaller().unmarshall(inputStream, realms);
		assertEquals(2, realms.size());

		Realm realm = realms.get(0);
		assertEquals(RealmsResponse.id1, realm.getId());
		assertEquals(RealmsResponse.name1, realm.getName());

		realm = realms.get(1);
		assertEquals(RealmsResponse.id2, realm.getId());
		assertEquals(RealmsResponse.name2, realm.getName());
	}

}
