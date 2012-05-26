package org.jboss.tools.deltacloud.integration.wizard;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.rse.core.model.IHost;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;

public class ConvertRSEToServerWizard extends Wizard {
	private RSEandASWizardPage page1;
	private DeltaCloudInstance instance;
	private IHost host;
	
	public ConvertRSEToServerWizard(DeltaCloudInstance instance) {
		this.instance = instance;
	}
	
	public ConvertRSEToServerWizard(DeltaCloudInstance instance, IHost host) {
		this(instance);
		this.host = host;
	}

	
	@Override
	public boolean performFinish() {
		Job j = page1.createPerformFinishJob(instance);
		if( j != null ) {
			j.schedule();
		}
		return true;
	}
	public void addPages() {
		page1 = new RSEandASWizardPage(host);
		addPage(page1);
	}
}
