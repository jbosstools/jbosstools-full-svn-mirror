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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.common.meta.action.impl.DefaultWizardDataValidator;
import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;

/**
 * @author mareshkau
 *
 */
public class VpeTaglibValidator extends DefaultWizardDataValidator {
	
	/**
	 * 
	 * @param support
	 * @param step
	 */
	public VpeTaglibValidator(SpecialWizardSupport support, int step) {
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
	 	String uriStr = (String) data.get("location"); //$NON-NLS-1$
	 	String prefix = (String) data.get("prefix");//$NON-NLS-1$
	    String nameStrt = "[A-Za-z_]|[^\\x00-\\x7F]"; //$NON-NLS-1$
	    String nameChar = "[A-Za-z0-9_.-]|[^\\x00-\\x7F]"; //$NON-NLS-1$
	    String name = '(' + nameStrt + ')'+'(' + nameChar + ")*"; //$NON-NLS-1$
	    
	 	try {
	 		new URI(uriStr);
	 	}catch (URISyntaxException ex) {
	 		message = NLS.bind(WebUIMessages.INCORRECT_URI,uriStr);
	 	}
	 	if((prefix!=null)&&(!prefix.matches(name))){
	 		message = NLS.bind(WebUIMessages.INCORRECT_PREFIX,prefix);
	 	}
	}
}
