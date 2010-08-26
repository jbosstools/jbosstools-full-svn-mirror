package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.IInstanceListListener;

public class CVInstancesCategoryElement extends CVCategoryElement implements IInstanceListListener {

	private Viewer viewer;
	private CVInstancesCategoryElement category;
	
	public CVInstancesCategoryElement(Object element, String name, Viewer viewer) {
		super(element, name, CVCategoryElement.INSTANCES);
		this.viewer = viewer;
		DeltaCloud cloud = (DeltaCloud)getElement();
		cloud.addInstanceListListener(this);
		this.category = this;
	}

	protected void finalize() throws Throwable {
		DeltaCloud cloud = (DeltaCloud)getElement();
		cloud.removeInstanceListListener(this);
		super.finalize();
	}
	
	@Override
	public Object[] getChildren() {
		if (!initialized) {
			DeltaCloud cloud = (DeltaCloud)getElement();
			cloud.removeInstanceListListener(this);
			DeltaCloudInstance[] instances = cloud.getCurrInstances();
			for (int i = 0; i < instances.length; ++i) {
				DeltaCloudInstance d = instances[i];
				CVInstanceElement element = new CVInstanceElement(d, d.getName());
				addChild(element);
			}
			initialized = true;
			cloud.addInstanceListListener(this);
		}
		return super.getChildren();
	}

	@Override
	public void listChanged(DeltaCloud cloud, DeltaCloudInstance[] instances) {
		clearChildren();
		for (int i = 0; i < instances.length; ++i) {
			DeltaCloudInstance d = instances[i];
			CVInstanceElement element = new CVInstanceElement(d, d.getName());
			addChild(element);
		}
		initialized = true;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				((TreeViewer)viewer).refresh(category, false);
			}
		});
	}

}
