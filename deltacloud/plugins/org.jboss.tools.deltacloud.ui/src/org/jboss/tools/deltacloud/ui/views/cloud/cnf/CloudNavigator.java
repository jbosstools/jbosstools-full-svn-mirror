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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.navigator.CommonNavigator;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.IDeltaCloudManagerListener;

public class CloudNavigator extends CommonNavigator implements IDeltaCloudManagerListener, PropertyChangeListener {
	protected Object getInitialInput() {
		return this;
	}
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
		getCommonViewer().setSorter(null);
		DeltaCloudManager.getDefault().addCloudManagerListener(this);
		try {
			DeltaCloud[] currentClouds = DeltaCloudManager.getDefault().getClouds();
			for( int i = 0; i < currentClouds.length; i++ ) {
				currentClouds[i].addPropertyChangeListener(DeltaCloud.PROP_NAME, this);
				currentClouds[i].addPropertyChangeListener(DeltaCloud.PROP_IMAGES, this);
				currentClouds[i].addPropertyChangeListener(DeltaCloud.PROP_INSTANCES, this);
			}
		} catch( DeltaCloudException dce ) { /* ignore */ }
	}
	public void cloudsChanged(int type, DeltaCloud cloud) {
		// This is super ugly
		if( type == IDeltaCloudManagerListener.ADD_EVENT) {
			cloud.addPropertyChangeListener(DeltaCloud.PROP_NAME, this);
			cloud.addPropertyChangeListener(DeltaCloud.PROP_IMAGES, this);
			cloud.addPropertyChangeListener(DeltaCloud.PROP_INSTANCES, this);
		}
		else if( type == IDeltaCloudManagerListener.REMOVE_EVENT) {
			cloud.removePropertyChangeListener(DeltaCloud.PROP_NAME, this);
			cloud.removePropertyChangeListener(DeltaCloud.PROP_IMAGES, this);
			cloud.removePropertyChangeListener(DeltaCloud.PROP_INSTANCES, this);
		}
		if( getCommonViewer() != null && !getCommonViewer().getTree().isDisposed())
			asyncRefresh(null);
	}
	
	private void asyncRefresh(final Object o) {
		Display.getDefault().asyncExec(new Runnable(){
			public void run() {
				if( o == null )
					getCommonViewer().refresh();
				else
					getCommonViewer().refresh(o);
			}
		});
	}
	
	public void dispose() {
		super.dispose();
		DeltaCloudManager.getDefault().removeCloudManagerListener(this);
		try {
			DeltaCloud[] currentClouds = DeltaCloudManager.getDefault().getClouds();
			for( int i = 0; i < currentClouds.length; i++ ) {
				currentClouds[i].removePropertyChangeListener(DeltaCloud.PROP_NAME, this);
				currentClouds[i].removePropertyChangeListener(DeltaCloud.PROP_IMAGES, this);
				currentClouds[i].removePropertyChangeListener(DeltaCloud.PROP_INSTANCES, this);
			}
		} catch( DeltaCloudException dce ) { /* ignore */ }
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		asyncRefresh(evt.getSource());
	}
}
