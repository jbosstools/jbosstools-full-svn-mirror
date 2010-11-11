package org.jboss.tools.deltacloud.ui;

import org.eclipse.core.runtime.IAdapterFactory;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

public class CVInstanceElementAdapterFactory implements IAdapterFactory {

	private static final Class[] ADAPTERS = new Class[]{
		DeltaCloudInstance.class
	};
	
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return UIUtils.adapt(adaptableObject, DeltaCloudInstance.class);
	}

	@Override
	public Class[] getAdapterList() {
		return ADAPTERS;
	}

}
