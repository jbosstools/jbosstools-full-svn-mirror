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
import org.jboss.tools.deltacloud.ui.views.CVCloudElement;
import org.jboss.tools.deltacloud.ui.views.CloudViewElement;

public class CloudViewElementUtils {

	/**
	 * Returns a DeltaCloud instance for a given cloud view element
	 * 
	 * @param element
	 *            the cloud view element to get the DeltaCloud for
	 * @return the cloud for the given CloudViewElement
	 * 
	 * @see DeltaCloud
	 * @see CloudViewElement
	 */
	public static DeltaCloud getCloud(CloudViewElement element) {
		CVCloudElement cvCloud = getCVCloudElement(element);
		if (cvCloud == null) {
			return null;
		}
		DeltaCloud cloud = (DeltaCloud) cvCloud.getElement();
		return cloud;
	}

	/**
	 * Returns a CVCloudElement for a given cloud view element
	 * 
	 * @param element
	 *            the cloud view element to get the CVCloudElement for
	 * @return the CVCloudElement for the given CloudViewElement
	 * 
	 * @see CloudViewElement
	 * @see CVCloudElement
	 */
	public static CVCloudElement getCVCloudElement(CloudViewElement element) {
		while (!(element instanceof CVCloudElement)) {
			element = (CloudViewElement) element.getParent();
		}
		return (CVCloudElement) element;
	}

}
