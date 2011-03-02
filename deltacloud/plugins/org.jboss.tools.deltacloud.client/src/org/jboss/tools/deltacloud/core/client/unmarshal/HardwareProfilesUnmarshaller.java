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

import org.jboss.tools.deltacloud.core.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.core.client.HardwareProfile;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author André Dietisheim
 */
public class HardwareProfilesUnmarshaller extends AbstractDeltaCloudObjectsUnmarshaller<HardwareProfile> {

	public HardwareProfilesUnmarshaller() {
		super("hardware_profiles", "hardware_profile");
	}

	@Override
	protected HardwareProfile unmarshallChild(Node node) throws DeltaCloudClientException {
		return new HardwareProfileUnmarshaller().unmarshall((Element) node, new HardwareProfile());
	}
}
