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

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;

/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 21, 2008
 */
public class JavaBeanConfigWizardPage extends WizardPage implements
		SelectionListener ,IJavaBeanSelectionListener {
	
	TreeViewer treeViewer;

	Text classText = null;

	Button classBrowseButton = null;

	IJavaProject project = null;

	private JavaBeanModelLoadComposite javaPropertySelectComposite;
	

	public List<JavaBeanModel> getJavaBeanModelList() {
		if(javaPropertySelectComposite == null) return Collections.EMPTY_LIST;
		return javaPropertySelectComposite.getJavabeanList();
	}

	public JavaBeanConfigWizardPage(IJavaProject selection) {
		super("beansearchwizardpage"); //$NON-NLS-1$
		this.project = selection;
		setPageComplete(true);
		setTitle(Messages.getString("JavaBeanConfigWizardPage.JavaBeanSelectionDialogTitle")); //$NON-NLS-1$
		setDescription(Messages.getString("JavaBeanConfigWizardPage.JavaBeanSelectionDialogDesc")); //$NON-NLS-1$
	}
	
	protected void initilize() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ecSclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt
	 * .widgets.Composite)
	 */
	public void createControl(Composite parent) {
		initilize();
//		Composite cc = new Composite(parent,SWT.NONE);
//		cc.setLayout(new GridLayout());
		try {
			javaPropertySelectComposite = new JavaBeanModelLoadComposite(
					parent, SWT.NONE, getContainer(), project);
			javaPropertySelectComposite.addJavaBeanSelectionListener(this);
//			Button button = new Button(cc, SWT.BORDER);
//
//			final TreeViewer vi = new TreeViewer(cc, SWT.NONE);
//			GridData gd = new GridData(GridData.FILL_BOTH);
//			gd.grabExcessHorizontalSpace = true;
//			gd.grabExcessVerticalSpace = true;
//			vi.setContentProvider(new BeanContentProvider());
//			vi.setLabelProvider(new BeanlabelProvider());
//			vi.getTree().setLayoutData(gd);
//			
//			javaPropertySelectComposite.setLayoutData(gd);
//			button.addSelectionListener(new SelectionAdapter() {
//
//				/*
//				 * (non-Javadoc)
//				 * 
//				 * @see
//				 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org
//				 * .eclipse.swt.events.SelectionEvent)
//				 */
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					// TODO Auto-generated method stub
//					super.widgetSelected(e);
//					List l = new ArrayList();
//					l.add(javaPropertySelectComposite.getCheckedJavaBeanModel());
//					vi.setInput(l);
//				}
//
//			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		this.setControl(javaPropertySelectComposite);
		this.updatePage();
	}

	protected void updatePage() {
		String error = null;
		if (project == null) {
			error = Messages.getString("JavaBeanConfigWizardPage.JavaBeanSelectionDialogErrorMsg"); //$NON-NLS-1$
		}
		setPageComplete(error == null);
		this.setErrorMessage(error);
	}

//	public JavaBeanConfigWizardPage(String pageName, String title,
//			ImageDescriptor titleImage) {
//		super(pageName, title, titleImage);
//	}

	public JavaBeanConfigWizardPage(String pageName) {
		super(pageName);
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
		widgetSelected(arg0);
	}

	public void widgetSelected(SelectionEvent arg0) {

	}

	public void exceptionOccur(Exception e) {
		this.setErrorMessage(e.toString());
	}

}
