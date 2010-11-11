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
package org.jboss.tools.deltacloud.ui;

import org.eclipse.core.runtime.IAdapterFactory;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

public class CVInstanceElementAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("rawtypes")
	private static final Class[] ADAPTERS = new Class[]{
		DeltaCloudInstance.class,
		DeltaCloud.class
	};
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return UIUtils.adapt(adaptableObject, adapterType);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return ADAPTERS;
	}

}
