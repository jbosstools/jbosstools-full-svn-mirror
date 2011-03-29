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
package org.jboss.tools.deltacloud.ui.adapter;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.views.cloud.DeltaCloudViewItem;
import org.jboss.tools.deltacloud.ui.views.cloud.ImageItem;
import org.jboss.tools.deltacloud.ui.views.cloud.InstanceItem;
import org.jboss.tools.internal.deltacloud.ui.utils.CloudViewElementUtils;

public class CloudViewElementAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("rawtypes")
	private static final Class[] ADAPTERS = new Class[] {
			IPropertySource.class,
			DeltaCloudInstance.class,
			DeltaCloud.class
	};

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		Assert.isLegal(adaptableObject instanceof DeltaCloudViewItem);
		DeltaCloudViewItem element = (DeltaCloudViewItem) adaptableObject;
		if (adapterType == IPropertySource.class) {
			return element.getPropertySource();
		} else if (adapterType == DeltaCloudImage.class) {
			return getDeltaCloudImage(element);
		} else if (adapterType == DeltaCloudInstance.class) {
			return getDeltaCloudInstance(element);
		} else if (adapterType == DeltaCloud.class) {
			return CloudViewElementUtils.getCloud(element);
		} else {
			return null;
		}
	}

	private DeltaCloudInstance getDeltaCloudInstance(DeltaCloudViewItem<?> element) {
		if (element instanceof InstanceItem) {		
			return (DeltaCloudInstance) element.getModel();
		} else {
			return null;
		}
	}

	private DeltaCloudImage getDeltaCloudImage(DeltaCloudViewItem<?> element) {
		if (element instanceof ImageItem) {		
			return (DeltaCloudImage) element.getModel();
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return ADAPTERS;
	}

}
