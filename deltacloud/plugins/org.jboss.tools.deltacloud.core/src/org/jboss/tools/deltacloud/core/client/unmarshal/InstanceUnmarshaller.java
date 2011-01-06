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
package org.jboss.tools.deltacloud.core.client.unmarshal;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.deltacloud.core.client.AddressList;
import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.Instance;
import org.jboss.tools.deltacloud.core.client.InstanceAction;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author André Dietisheim
 */
public class InstanceUnmarshaller extends AbstractActionAwareUnmarshaller<Instance, InstanceAction> {

	public InstanceUnmarshaller() {
		super("instance", Instance.class, "link");
	}

	protected Instance doUnmarshall(Element element, Instance instance) throws Exception {
		instance.setId(getAttributeText("id", element));
		instance.setName(getFirstElementText("name", element));
		instance.setOwnerId(getFirstElementText("owner_id", element));
		instance.setOwnerId(getFirstElementText("owner_id", element));
		instance.setImageId(getFirstElementAttributeText("image", "id", element));
		instance.setProfileId(getFirstElementAttributeText("hardware_profile", "id", element));
		instance.setRealmId(getFirstElementAttributeText("realm", "id", element));
		instance.setState(getFirstElementText("state", element));
		setKeyname(instance, element);
		instance.setActions(getActions(element, instance));
		instance.setPublicAddresses(getAddressList("public_addresses", element));
		instance.setPrivateAddresses(getAddressList("private_addresses", element));
		return instance;
	}

	private AddressList getAddressList(String elementName, Element element) {
		Element addressElement = getFirstElement(elementName, element);
		if (addressElement != null) {
			NodeList addressList = addressElement.getChildNodes();
			if (addressList != null) {
				List<String> addresses = new ArrayList<String>();
				for (int i = 0; i < addressList.getLength(); i++) {
					Node addressNode = addressList.item(i);
					if (addressNode != null) {
						addresses.add(addressNode.getTextContent());
					}
				}
				return new AddressList(addresses);
			}
		}
		return new AddressList();
	}

	private void setKeyname(Instance instance, Element element) {
		Element authenticationElement = getFirstElement("authentication", element);
		if (authenticationElement != null) {
			Element loginElement = getFirstElement("login", authenticationElement);
			if (loginElement != null) {
				String keyname = getFirstElementText("keyname", loginElement);
				instance.setKeyId(keyname);
			}
		}
	}

	@Override
	protected InstanceAction unmarshallAction(Element element) throws DeltaCloudClientException {
		InstanceAction action = new InstanceAction();
		new InstanceActionUnmarshaller().unmarshall(element, action);
		return action;
	}
}
