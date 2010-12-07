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
import org.jboss.tools.deltacloud.ui.views.cloud.CloudViewElement;
import org.jboss.tools.deltacloud.ui.views.cloud.DeltaCloudViewElement;

public class CloudViewElementUtils {

	/**
	 * Returns a DeltaCloud instance for a given cloud view element
	 * 
	 * @param element
	 *            the cloud view element to get the DeltaCloud for
	 * @return the cloud for the given CloudViewElement
	 * 
	 * @see DeltaCloud
	 * @see DeltaCloudViewElement
	 */
	public static DeltaCloud getCloud(DeltaCloudViewElement element) {
		CloudViewElement cvCloud = getCVCloudElement(element);
		if (cvCloud == null) {
			return null;
		}
		DeltaCloud cloud = (DeltaCloud) cvCloud.getModel();
		return cloud;
	}

	/**
	 * Returns a CVCloudElement for a given cloud view element
	 * 
	 * @param element
	 *            the cloud view element to get the CVCloudElement for
	 * @return the CVCloudElement for the given CloudViewElement
	 * 
	 * @see DeltaCloudViewElement
	 * @see CloudViewElement
	 */
	public static CloudViewElement getCVCloudElement(DeltaCloudViewElement element) {
		while (!(element instanceof CloudViewElement) 
				&& element != null) {
			element = (DeltaCloudViewElement) element.getParent();
		}
		return (CloudViewElement) element;
	}

}
