package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.ICloudManagerListener;

public class CVRootElement extends CloudViewElement implements ICloudManagerListener {

	private boolean initialized;
	private Viewer viewer;

	public CVRootElement(Viewer viewer) {
		super(DeltaCloudManager.getDefault(), "root"); //$NON-NLS-1$
		this.viewer = viewer;
	}
	
	@Override
	public IPropertySource getPropertySource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getChildren() {
		if (!initialized) {
			DeltaCloudManager m = DeltaCloudManager.getDefault();
			DeltaCloud[] clouds = m.getClouds();
			for (int i = 0; i < clouds.length; ++i) {
				DeltaCloud cloud = clouds[i];
				CVCloudElement e = new CVCloudElement(cloud, cloud.getName(), viewer);
				addChild(e);
			}
			m.addCloudManagerListener(this);
			initialized = true;
		}
		return super.getChildren();
	}

	@Override
	protected void finalize() throws Throwable {
		DeltaCloudManager.getDefault().removeCloudManagerListener(this);
		super.finalize();
	}
	
	public void changeEvent(int type) {
		DeltaCloudManager m = DeltaCloudManager.getDefault();
		m.removeCloudManagerListener(this);
		DeltaCloud[] clouds = m.getClouds();
		for (int i = 0; i < clouds.length; ++i) {
			DeltaCloud cloud = clouds[i];
			CVCloudElement e = new CVCloudElement(cloud, cloud.getName(), viewer);
			addChild(e);
		}
		initialized = true;
		m.addCloudManagerListener(this);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				((TreeViewer)viewer).refresh(this, false);
			}
		});
	}

}
