/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
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
