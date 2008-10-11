/*******************************************************************************
* Copyright (c) 2007-2008 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributor:
*     Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.vpe.resref.core;

import java.util.Properties;

import org.jboss.tools.common.el.core.parser.ELParser;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.meta.action.impl.DefaultWizardDataValidator;
import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;

/**
 * @author mareshkau
 *
 */
public class VpeElVariableValidator extends DefaultWizardDataValidator {

	
	/**
	 * 
	 */
	public VpeElVariableValidator(SpecialWizardSupport support, int step) {
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
	 	ELParser elParser = ELParserUtil.getDefaultFactory().createParser();
	 	elParser.parse("#{"+location+'}'); //$NON-NLS-1$
	 	if(elParser.getSyntaxErrors().size()>0) {
	 		message=Messages.INVALID_EL_EXPRESSION;
	 	}
	 	
	}

}
