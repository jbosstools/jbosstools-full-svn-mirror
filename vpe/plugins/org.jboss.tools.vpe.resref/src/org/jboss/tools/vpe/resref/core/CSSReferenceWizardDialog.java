package org.jboss.tools.vpe.resref.core;

import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.resref.core.ResourceReference;

public class CSSReferenceWizardDialog extends
		ReferenceWizardDialog {
	
	public CSSReferenceWizardDialog(Shell parentShell, Object fileLocation,
			ResourceReference resref) {
		super(parentShell, new CSSReferenceWizard(fileLocation,
				Messages.VRD_ADD_CSS_PREFERENCE), resref);
	}

}
