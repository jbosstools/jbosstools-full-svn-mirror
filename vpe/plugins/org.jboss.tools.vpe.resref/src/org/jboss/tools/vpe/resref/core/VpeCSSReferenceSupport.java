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

import org.jboss.tools.common.meta.action.impl.WizardDataValidator;

/**
 * @author mareshkau
 *
 */
public class VpeCSSReferenceSupport extends VpeAddReferenceSupport{

	private WizardDataValidator  wizardDataValidator;
	/* (non-Javadoc)
	 * @see org.jboss.tools.common.meta.action.impl.SpecialWizardSupport#getValidator(int)
	 */
	@Override
	public WizardDataValidator getValidator(int step) {
		if(this.wizardDataValidator==null) {
			
			this.wizardDataValidator = new VpeCSSFileValidator(this, step);
		}
		return this.wizardDataValidator;
	}
	
}
