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
package org.jboss.tools.smooks.javabean.ui;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;

/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 22, 2008
 */
public class JavaBeanModelCreationDialog extends TitleAreaDialog {

	IJavaProject project = null;
	private JavaBeanModelLoadComposite jc;
	
	private JavaBeanModel returnModel;

	/**
	 * 
	 * @return
	 */
	public JavaBeanModel getCheckedJavaBeanModel() {
		return returnModel;
	}

	public JavaBeanModelCreationDialog(Shell parentShell, IJavaProject project) {
		super(parentShell);
		this.project = project;
	}

	public int open(){
		return super.open();
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		super.okPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Control area = super.createDialogArea(parent);
		try {
			this.createJavaBeanDialog(area, project);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return area;
	}

	/**
	 * 
	 * @param area
	 * @param project
	 * @return
	 * @throws Exception
	 */
	protected Control createJavaBeanDialog(Control area, IJavaProject project)
			throws Exception {
		jc = new JavaBeanModelLoadComposite((Composite) area, SWT.NONE, null,
				project);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		jc.setLayoutData(gd);
		gd.heightHint = 400;
		return area;
	}

}
