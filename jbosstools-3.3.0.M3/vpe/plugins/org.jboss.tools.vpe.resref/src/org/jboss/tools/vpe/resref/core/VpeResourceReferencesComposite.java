/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.resref.core;

import org.eclipse.jface.dialogs.Dialog;
import org.jboss.tools.common.resref.core.ResourceReference;

public abstract class VpeResourceReferencesComposite extends AbstractResourceReferencesComposite {
	
	public VpeResourceReferencesComposite() {
		super();
	}
	
	protected void add(int index) {
		ResourceReference resref = getDefaultResourceReference();
		
		int returnCode = -1;
		ReferenceWizardDialog  d = getDialog(resref);
		if (null != d) {
			returnCode = d.open();
		}
		if (Dialog.OK == returnCode) {
			dataList.add(resref);
			update();
			table.setSelection(dataList.size() - 1);
		}
	}
	
	protected void edit(int index) {
		if(index < 0) {
			return;
		}
		ResourceReference resref = getReferenceArray()[index];
		int returnCode = -1;
		ReferenceWizardDialog  d = getDialog(resref);
		if (null != d) {
			returnCode = d.open();
		}
		if (Dialog.OK == returnCode) {
			update();
		}
	}
}
