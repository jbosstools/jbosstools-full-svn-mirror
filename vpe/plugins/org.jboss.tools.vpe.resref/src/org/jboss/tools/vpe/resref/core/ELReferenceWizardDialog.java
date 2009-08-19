package org.jboss.tools.vpe.resref.core;

import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.resref.core.ResourceReference;

public class ELReferenceWizardDialog extends
		ReferenceWizardDialog {

	public ELReferenceWizardDialog(Shell parentShell, Object fileLocation,
			ResourceReference resref, ResourceReference[] resrefList) {
		super(parentShell, new ELReferenceWizard(fileLocation,
				Messages.VRD_ADD_EL_PREFERENCE, resrefList),
				resref);
	}
	
}
