package org.jboss.tools.vpe.resref.core;

import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.resref.core.ResourceReference;

public class ELReferenceWizardDialog extends
		ReferenceWizardDialog {

	public ELReferenceWizardDialog(Shell parentShell, Object fileLocation,
			ResourceReference resref, ResourceReference[] resrefList) {
		super(parentShell, new ELReferenceWizard(fileLocation, resrefList),
				resref);
	}
	
}
