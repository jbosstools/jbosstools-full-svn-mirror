package org.jboss.tools.deltacloud.core.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jboss.tools.deltacloud.core.DeltaCloud;

public class LoadCloudImagesJob extends AbstractCloudElementJob {

	private DeltaCloud cloud;
	private Runnable post = null;
	public LoadCloudImagesJob(DeltaCloud cloud) {
		super("Loading Cloud Images for cloud " + cloud.getName(), 
				cloud, CLOUDELEMENT.INSTANCES);
		this.cloud = cloud;
	}
	public LoadCloudImagesJob(DeltaCloud cloud, Runnable post) {
		this(cloud);
		this.post = post;
	}

	@Override
	protected IStatus doRun(IProgressMonitor monitor) throws Exception {
		cloud.loadImages();
		if( post != null ) 
			post.run();
		return Status.OK_STATUS;
	}
}
