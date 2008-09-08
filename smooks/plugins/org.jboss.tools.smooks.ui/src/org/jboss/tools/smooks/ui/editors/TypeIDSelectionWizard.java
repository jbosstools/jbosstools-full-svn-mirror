/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.editors;

import org.eclipse.jface.wizard.Wizard;

/**
 * @author Dart Peng<br>
 * Date : Sep 5, 2008
 */
public class TypeIDSelectionWizard extends Wizard {
	private String sourceDataTypeID = null;
	private String targetDataTypeID = null;
	private TypeIDSelectionWizardPage page = null;
	public String getSourceDataTypeID() {
		return sourceDataTypeID;
	}
	public void setSourceDataTypeID(String sourceDataTypeID) {
		this.sourceDataTypeID = sourceDataTypeID;
	}
	public String getTargetDataTypeID() {
		return targetDataTypeID;
	}
	public void setTargetDataTypeID(String targetDataTypeID) {
		this.targetDataTypeID = targetDataTypeID;
	}
	
	public void addPages(){
		if(page == null){
			page = new TypeIDSelectionWizardPage("TypeID Selection");
			this.addPage(page);
		}
		super.addPages();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		String sid = page.getSourceDataTypeID();
		String tid = page.getTargetDataTypeID();
		if(sid == null || tid == null) return false;
		this.sourceDataTypeID = sid;
		this.targetDataTypeID = tid;
		return true;
	}

}
