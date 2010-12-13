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
package org.jboss.tools.internal.deltacloud.ui.utils;

import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.ui.views.cloud.CloudItem;
import org.jboss.tools.deltacloud.ui.views.cloud.DeltaCloudViewItem;

public class CloudViewElementUtils {

	/**
	 * Returns a DeltaCloud instance for a given cloud view element
	 * 
	 * @param element
	 *            the cloud view element to get the DeltaCloud for
	 * @return the cloud for the given CloudItem
	 * 
	 * @see DeltaCloud
	 * @see DeltaCloudViewItem
	 */
	public static DeltaCloud getCloud(DeltaCloudViewItem<?> element) {
		CloudItem cloudItem = getCloudItem(element);
		if (cloudItem == null) {
			return null;
		}
		return cloudItem.getModel();
	}

	/**
	 * Returns a CVCloudElement for a given cloud view element
	 * 
	 * @param element
	 *            the cloud view element to get the CVCloudElement for
	 * @return the CVCloudElement for the given CloudItem
	 * 
	 * @see DeltaCloudViewItem
	 * @see CloudItem
	 */
	public static CloudItem getCloudItem(DeltaCloudViewItem<?> element) {
		while (!(element instanceof CloudItem) 
				&& element != null) {
			element = (DeltaCloudViewItem<?>) element.getParent();
		}
		return (CloudItem) element;
	}
}
