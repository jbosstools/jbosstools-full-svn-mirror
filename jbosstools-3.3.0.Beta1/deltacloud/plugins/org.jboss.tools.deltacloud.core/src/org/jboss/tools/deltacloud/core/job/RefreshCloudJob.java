package org.jboss.tools.deltacloud.core.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jboss.tools.deltacloud.core.Activator;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudMultiException;

public class RefreshCloudJob extends AbstractCloudJob {

	public RefreshCloudJob(DeltaCloud cloud) {
		super("Refreshing images and instances on " + cloud.getName(), cloud);
	}

	protected IStatus doRun(IProgressMonitor monitor) throws Exception {
		try {
			getCloud().loadChildren();
		} catch (DeltaCloudMultiException e) {
			return Activator.createMultiStatus(e);
		}
		return Status.OK_STATUS;
	}

}
