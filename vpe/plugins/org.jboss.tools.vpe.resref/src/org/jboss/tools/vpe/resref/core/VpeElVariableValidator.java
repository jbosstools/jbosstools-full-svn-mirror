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

import org.jboss.tools.common.el.core.model.ELModel;
import org.jboss.tools.common.el.core.parser.ELParser;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.meta.action.impl.DefaultWizardDataValidator;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.common.resref.ui.BaseAddReferenceSupport;

/**
 * @author mareshkau
 *
 */
public class VpeElVariableValidator extends DefaultWizardDataValidator {
	private ResourceReference[] currentReferenceList;
	private ResourceReference editingReference;

	/**
	 * 
	 * @param support
	 * @param step
	 * @param resourceReferences 
	 * @param editingReference 
	 */
	public VpeElVariableValidator(BaseAddReferenceSupport support, int step, ResourceReference[] currentReferenceList, ResourceReference editingReference) {
		super.setSupport(support, step);
		this.currentReferenceList = currentReferenceList;
		this.editingReference = editingReference;
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
	 	ELModel model = elParser.parse("#{"+location+'}'); //$NON-NLS-1$
	 	if(model == null || model.getSyntaxErrors().size()>0) {
	 		message = Messages.INVALID_EL_EXPRESSION;
	 	} else {
	 		// yradtsevich: JBIDE-3576: EL expression overriding
	 		// check if there is no another EL reference in the same scope with the same name
	 		final int selectedScope = ((BaseAddReferenceSupport)support).getSelectedScope(data);
	 		for (ResourceReference listItemReference : currentReferenceList) {
				if (editingReference != listItemReference
						&& listItemReference.getScope() == selectedScope
						&& location.equals(listItemReference.getLocation())) {
					message = Messages.EL_EXPRESSION_ALREADY_EXISTS;
					break;
	 			}
	 		}
	 	}
	}

}
