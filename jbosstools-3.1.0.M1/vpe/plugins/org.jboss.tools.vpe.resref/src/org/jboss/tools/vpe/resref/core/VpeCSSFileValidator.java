/******************************************************************************* 
* Copyright (c) 2007 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.vpe.resref.core;

import java.io.File;
import java.util.Properties;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.common.meta.action.impl.DefaultWizardDataValidator;
import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

/**
 * @author mareshkau
 *
 */
public class VpeCSSFileValidator extends DefaultWizardDataValidator {
	
	/**
	 * 
	 * @param support
	 * @param step
	 */
	public VpeCSSFileValidator(SpecialWizardSupport support, int step) {
		super.setSupport(support, step);
	}
	/* (non-Javadoc)
	 * @see org.jboss.tools.common.meta.action.impl.DefaultWizardDataValidator#validate(java.util.Properties)
	 */
	@Override
	public void validate(Properties data) {
		
		super.validate(data);
		
		if( (getErrorMessage()!=null) ||
				(getWarningMessage()!=null)) {
			return;
		} 
	 	String location = (String) data.get("location"); //$NON-NLS-1$
	 	if(!new File(location).isFile()) {
	 		message = NLS.bind(WebUIMessages.FILE_DOESNOT_EXIST,location);
	 	}
		
	}
}
