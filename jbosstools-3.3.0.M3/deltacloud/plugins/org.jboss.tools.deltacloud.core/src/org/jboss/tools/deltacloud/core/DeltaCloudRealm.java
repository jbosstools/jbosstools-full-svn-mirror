/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.core;

import org.apache.deltacloud.client.Realm;

/**
 * 
 * @author Jeff Johnston
 * 
 *         TODO: there are 2 realm classes:
 *         <ul>
 *         <li>org.jboss.tools.deltacloud.client.Realm</li>
 *         <li>org.jboss.tools.deltacloud.core.DeltaCloudRealm</li>
 *         </ul>
 *         remove the DeltaCloudRealm class
 * 
 */
public class DeltaCloudRealm {

	public final static String AVAILABLE = "AVAILABLE"; //$NON-NLS-1$
	public final static String UNAVAILABLE = "UNAVAILABLE"; //$NON-NLS-1$

	private Realm realm;

	public DeltaCloudRealm(Realm realm) {
		this.realm = realm;
	}

	public String getId() {
		return realm.getId();
	}

	public String getName() {
		return realm.getName();
	}

	public String getState() {
		return realm.getState().toString();
	}

}
