package org.hibernate.mediator.stubs;

import org.hibernate.cfg.reveng.ProgressListener;

public abstract class ProgressListenerStub {
	
	protected ProgressListener progressListener = new ProgressListener() {

		public void startSubTask(String name) {
			ProgressListenerStub.this.startSubTask(name);
		}
		
	};
	
	protected ProgressListenerStub() {
	}

	public abstract void startSubTask(String name);
}
