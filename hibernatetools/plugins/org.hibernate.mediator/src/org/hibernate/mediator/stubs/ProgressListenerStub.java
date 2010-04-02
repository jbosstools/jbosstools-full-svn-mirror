package org.hibernate.mediator.stubs;

import org.hibernate.cfg.reveng.ProgressListener;

public abstract class ProgressListenerStub {
	public static final String CL = "org.hibernate.cfg.reveng.ProgressListener"; //$NON-NLS-1$

	protected ProgressListener progressListener = new ProgressListener() {

		public void startSubTask(String name) {
			ProgressListenerStub.this.startSubTask(name);
		}
		
	};
	
	protected ProgressListenerStub() {
	}

	public abstract void startSubTask(String name);
}
