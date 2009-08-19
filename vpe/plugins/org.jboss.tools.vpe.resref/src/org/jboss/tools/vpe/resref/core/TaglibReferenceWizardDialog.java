package org.jboss.tools.vpe.resref.core;

import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.resref.core.ResourceReference;

public class TaglibReferenceWizardDialog extends
		ReferenceWizardDialog {
	
	public TaglibReferenceWizardDialog(Shell parentShell, Object fileLocation,
			ResourceReference resref) {
		super(parentShell, new TaglibReferenceWizard(fileLocation,
				Messages.VRD_ADD_TAGLIB_PREFERENCE), resref);
	}
	
}
