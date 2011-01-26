/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.deltacloud.ui.views.cloud.cnf;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.navigator.CommonNavigator;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.IDeltaCloudManagerListener;

public class CloudNavigator extends CommonNavigator implements IDeltaCloudManagerListener {
	protected Object getInitialInput() {
		return this;
	}
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
		getCommonViewer().setSorter(null);
		DeltaCloudManager.getDefault().addCloudManagerListener(this);
	}
	public void cloudsChanged(int type, DeltaCloud cloud) {
		if( getCommonViewer() != null && !getCommonViewer().getTree().isDisposed())
			getCommonViewer().refresh();
	}
	public void dispose() {
		super.dispose();
		DeltaCloudManager.getDefault().removeCloudManagerListener(this);
	}
}
