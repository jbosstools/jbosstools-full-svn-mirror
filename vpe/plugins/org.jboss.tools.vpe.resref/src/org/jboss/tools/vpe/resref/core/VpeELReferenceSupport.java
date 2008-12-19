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

import org.jboss.tools.common.meta.action.impl.WizardDataValidator;

/**
 * @author mareshkau
 *
 */
public class VpeELReferenceSupport extends VpeAddReferenceSupport {

	/* (non-Javadoc)
	 * @see org.jboss.tools.common.meta.action.impl.SpecialWizardSupport#getValidator(int)
	 */
	@Override
	public WizardDataValidator getValidator(int step) {
		return new VpeElVariableValidator(this ,getStepId());
	}

}
