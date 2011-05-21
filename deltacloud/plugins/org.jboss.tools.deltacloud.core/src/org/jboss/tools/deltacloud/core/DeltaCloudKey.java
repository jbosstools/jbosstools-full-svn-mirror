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

import org.jboss.tools.deltacloud.client.Key;

/**
 * @author André Dietisheim
 */
public class DeltaCloudKey extends AbstractDeltaCloudElement {

	
	private Key key;

	public DeltaCloudKey(Key key, DeltaCloud cloud) {
		super(cloud);
		this.key = key;
	}

	@Override
	public String getName() {
		return key.getId();
	}

	@Override
	public String getId() {
		return key.getId();
	}

	public String getFingerprint() {
		return key.getFingerprint();
	}
	
	public String getPem() {
		return key.getPem();
	}
	
	@Override
	public String toString() {
		return "DeltaCloudKey [key=" + key + "]";
	}

	protected Key getKey() {
		return key;
	}
}
