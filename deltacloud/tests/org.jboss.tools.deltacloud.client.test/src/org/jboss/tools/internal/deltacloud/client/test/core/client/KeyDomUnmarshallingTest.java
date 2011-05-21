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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.jboss.tools.deltacloud.client.Action;
import org.jboss.tools.deltacloud.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.client.HttpMethod;
import org.jboss.tools.deltacloud.client.Key;
import org.jboss.tools.deltacloud.client.unmarshal.ActionUnmarshaller;
import org.jboss.tools.deltacloud.client.unmarshal.KeyUnmarshaller;
import org.jboss.tools.deltacloud.client.unmarshal.KeysUnmarshaller;
import org.jboss.tools.internal.deltacloud.client.test.fakes.KeyResponseFakes;
import org.jboss.tools.internal.deltacloud.client.test.fakes.KeyResponseFakes.KeyActionResponse;
import org.jboss.tools.internal.deltacloud.client.test.fakes.KeyResponseFakes.KeyResponse;
import org.jboss.tools.internal.deltacloud.client.test.fakes.KeyResponseFakes.KeysResponse;
import org.junit.Test;

/**
 * @author André Dietisheim
 */
public class KeyDomUnmarshallingTest {

	@Test
	public void keyActionMayBeUnmarshalled() throws MalformedURLException, JAXBException, DeltaCloudClientException {
		Action<Key> keyAction = new Action<Key>();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(KeyActionResponse.keyActionResponse.getBytes());
		new ActionUnmarshaller<Key>().unmarshall(inputStream, keyAction);
		assertNotNull(keyAction);
		assertEquals(KeyActionResponse.name, keyAction.getName());
		assertEquals(KeyActionResponse.url, keyAction.getUrl().toString());
		assertEquals(KeyActionResponse.method.toUpperCase(), keyAction.getMethod().toString().toUpperCase());
	}

	@Test
	public void keyMayBeUnmarshalled() throws MalformedURLException, JAXBException, DeltaCloudClientException {
		Key key = new Key();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(KeyResponse.keyResponse.getBytes());
		new KeyUnmarshaller().unmarshall(inputStream, key);
		assertNotNull(key);
		assertEquals(KeyResponseFakes.KeyResponse.id, key.getId());
		assertEquals(KeyResponse.fingerprint, key.getFingerprint());
		assertEquals(new URL(KeyResponse.url), key.getUrl());
		assertEquals(KeyResponse.pem, key.getPem());
		assertEquals(1, key.getActions().size());
		Action<Key> action = key.getActions().get(0);
		assertNotNull(action);
		assertEquals(KeyResponse.url, action.getUrl().toString());
		assertEquals(KeyResponse.name, action.getName());
		assertEquals(HttpMethod.valueOf(KeyResponse.method.toUpperCase()), action.getMethod());
	}

	@Test
	public void keysMayBeUnmarshalled() throws MalformedURLException, JAXBException, DeltaCloudClientException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(KeysResponse.keysResponse.getBytes());
		List<Key> keys = new ArrayList<Key>();
		new KeysUnmarshaller().unmarshall(inputStream, keys);
		assertEquals(2, keys.size());
		Key key = keys.get(0);
		assertEquals(KeysResponse.id1, key.getId());
		assertEquals(KeysResponse.fingerprint1, key.getFingerprint());
		assertEquals(new URL(KeysResponse.url1), key.getUrl());
		assertEquals(KeysResponse.pem1, key.getPem());
		assertEquals(1, key.getActions().size());
		Action<Key> action = key.getActions().get(0);
		assertNotNull(action);
		assertEquals(KeysResponse.url1, action.getUrl().toString());
		assertEquals(KeysResponse.name1, action.getName());
		assertEquals(HttpMethod.valueOf(KeysResponse.method1.toUpperCase()), action.getMethod());
	}
}
