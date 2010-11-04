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
package org.jboss.tools.deltacloud.ui.views;

import org.jboss.tools.deltacloud.core.DeltaCloud;

public class CloudViewElementUtils {

	public static DeltaCloud getCloud(CloudViewElement element) {
		while (!(element instanceof CVCloudElement)) {
			element = (CloudViewElement) element.getParent();
		}
		CVCloudElement cvcloud = (CVCloudElement) element;
		DeltaCloud cloud = (DeltaCloud) cvcloud.getElement();
		return cloud;
	}
	
}
