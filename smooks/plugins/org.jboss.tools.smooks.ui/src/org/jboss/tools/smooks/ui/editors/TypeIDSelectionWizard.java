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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * @author Dart Peng<br>
 * Date : Sep 5, 2008
 */
public class TypeIDSelectionWizard extends Wizard implements INewWizard {
	private String sourceDataTypeID = null;
	private String targetDataTypeID = null;
	private Object sourceTreeViewerInputContents;
	private Object targetTreeViewerInputContents;
	private TypeIDSelectionWizardPage page = null;
	private IStructuredSelection selection;
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
			page = new TypeIDSelectionWizardPage(Messages.getString("TypeIDSelectionWizard.TypeIDSelection"),false); //$NON-NLS-1$
			page.setSelection(selection);
			this.addPage(page);
		}
		super.addPages();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		String sid = page.getSourceID();
		String tid = page.getTargetID();
		if(sid == null || tid == null) return false;
		this.sourceDataTypeID = sid;
		this.targetDataTypeID = tid;
//		setSourceTreeViewerInputContents(page.getSourceTreeViewerInputContents());
//		setTargetTreeViewerInputContents(page.getTargetTreeViewerInputContents());
		return true;
	}
	public Object getSourceTreeViewerInputContents() {
		return sourceTreeViewerInputContents;
	}
	public void setSourceTreeViewerInputContents(
			Object sourceTreeViewerInputContents) {
		this.sourceTreeViewerInputContents = sourceTreeViewerInputContents;
	}
	public Object getTargetTreeViewerInputContents() {
		return targetTreeViewerInputContents;
	}
	public void setTargetTreeViewerInputContents(
			Object targetTreeViewerInputContents) {
		this.targetTreeViewerInputContents = targetTreeViewerInputContents;
	}
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		if(page != null) page.setSelection(selection);
	}

}
