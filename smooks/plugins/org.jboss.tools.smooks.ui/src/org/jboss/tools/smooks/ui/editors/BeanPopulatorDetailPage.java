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

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.search.JavaSearchScopeFactory;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksPackage;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.SmooksUIActivator;

/**
 * @deprecated
 * @author Dart Peng<br>
 *         Date : Sep 11, 2008
 */
public class BeanPopulatorDetailPage extends AbstractSmooksModelDetailPage {

	private Text selectorText;
	private Text beanClassText;
	private Text beanIDText;
	private Button clazzBrowseButton;
	private Button idBrowseButton;

	public BeanPopulatorDetailPage(SmooksFormEditor parentEditor,
			EditingDomain domain) {
		super(parentEditor, domain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.ui.editors.AbstractSmooksModelDetailPage#createSectionContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createSectionContents(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		parent.setLayout(gridLayout);

		this.formToolKit.createLabel(parent, "Selector : ");
		selectorText = formToolKit.createText(parent, "");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		selectorText.setLayoutData(gd);

		this.formToolKit.createLabel(parent, "Bean Class : ");
		Composite beanClassComposite = formToolKit.createComposite(parent);
		GridLayout bcgl = new GridLayout();
		bcgl.numColumns = 2;
		bcgl.marginHeight = 0;
		bcgl.marginWidth = 1;
		beanClassComposite.setLayout(bcgl);
		beanClassText = formToolKit.createText(beanClassComposite, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		beanClassText.setLayoutData(gd);

		clazzBrowseButton = formToolKit.createButton(beanClassComposite,
				"Browse", SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		beanClassComposite.setLayoutData(gd);
		formToolKit.paintBordersFor(beanClassComposite);

		this.formToolKit.createLabel(parent, "Bean ID : ");
		beanIDText = formToolKit.createText(parent, "");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		beanIDText.setLayoutData(gd);
		
//		Composite beanIDComposite = formToolKit.createComposite(parent);
//		GridLayout bilg = new GridLayout();
//		bilg.numColumns = 1;
//		bilg.marginHeight = 1;
//		bilg.marginWidth = 1;
//		beanIDComposite.setLayout(bilg);
//
//		beanIDText = formToolKit.createText(beanIDComposite, "");
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.grabExcessHorizontalSpace = true;
//		beanIDText.setLayoutData(gd);
//
//		idBrowseButton = formToolKit.createButton(beanIDComposite, "Browse",
//				SWT.NONE);
//		idBrowseButton.setEnabled(false);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.grabExcessHorizontalSpace = true;
//		beanIDComposite.setLayoutData(gd);
//		formToolKit.paintBordersFor(beanIDComposite);
		formToolKit.paintBordersFor(parent);
		
		
		configControls();
		hookContorls();
	}

	protected void idBrowseButtonSelected() {

	}

	protected void clazzBrowseButtonSelected() {
		IFileEditorInput input = (IFileEditorInput) this.parentEditor
				.getEditorInput();
		IJavaProject javaProject = null;
		if (input != null) {
			IFile file = input.getFile();
			javaProject = JavaCore.create(file.getProject());
		}
		if (javaProject == null)
			return;
		IJavaSearchScope scope = JavaSearchScopeFactory.getInstance()
				.createJavaProjectSearchScope(javaProject, true);
		SelectionDialog dialog;
		try {
			IWorkbenchWindow runnableContext = SmooksUIActivator.getDefault()
					.getWorkbench().getActiveWorkbenchWindow();
			dialog = JavaUI.createTypeDialog(this.parentEditor.getSite()
					.getShell(), runnableContext, scope,
					IJavaElementSearchConstants.CONSIDER_CLASSES, false);
			dialog.setMessage("Source Java Bean:");
			dialog.setTitle("Search java bean");

			if (dialog.open() == Window.OK) {
				Object[] results = dialog.getResult();
				if (results.length > 0) {
					Object result = results[0];
					String packageFullName = JavaModelUtil
							.getTypeContainerName((IType) result);
					beanClassText.setText(packageFullName + "."
							+ ((IType) result).getElementName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isStale() {
		return true;
	}

	private void hookContorls() {
		selectorText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				selectorChanged();
			}

		});
		beanClassText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				beanClassChanged();
			}

		});
		clazzBrowseButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				clazzBrowseButtonSelected();
			}

		});
		beanIDText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				beanIDChanged();
			}

		});
//		idBrowseButton.addSelectionListener(new SelectionAdapter() {
//
//			public void widgetSelected(SelectionEvent e) {
//				idBrowseButtonSelected();
//			}
//
//		});
	}

	private void configControls() {
		if (resourceConfig != null) {
			String selector = "";
			selector = resourceConfig.getSelector();
			if (selector == null)
				selector = "";
			selectorText.setText(selector);

			String beanClass = SmooksModelUtils.getParmaText("beanClass",
					resourceConfig);
			if (beanClass == null)
				beanClass = "";
			String beanId = SmooksModelUtils.getParmaText("beanId",
					resourceConfig);
			if (beanId == null)
				beanId = "";

			beanClassText.setText(beanClass);
			beanIDText.setText(beanId);
		}
	}

	protected void beanIDChanged() {
		if (!isCanFireChange())
			return;
		String beanIdStr = beanIDText.getText();
		if (beanIdStr == null)
			return;
		List paramList = resourceConfig.getParam();
		ParamType beanId = null;
		for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
			ParamType param = (ParamType) iterator.next();
			String name = param.getName();
			if (SmooksModelConstants.BEAN_ID.equals(name)) {
				beanId = param;
				break;
			}
		}
		if (beanId == null) {
			beanId = SmooksFactory.eINSTANCE.createParamType();
			beanId.setName(SmooksModelConstants.BEAN_ID);
			AddCommand.create(domain, resourceConfig,
					SmooksPackage.eINSTANCE.getResourceConfigType_Param(),
					beanId).execute();
		}
		SmooksModelUtils.setTextToAnyType(beanId, beanIdStr);
		this.parentEditor.fireEditorDirty(true);
	}

	protected void beanClassChanged() {
		if (!isCanFireChange())
			return;
		String beanClassStr = beanClassText.getText();
		if (beanClassStr == null)
			return;
		List paramList = resourceConfig.getParam();
		ParamType beanClass = null;
		for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
			ParamType param = (ParamType) iterator.next();
			String name = param.getName();
			if (SmooksModelConstants.BEAN_CLASS.equals(name)) {
				beanClass = param;
				break;
			}
		}
		if (beanClass == null) {
			beanClass = SmooksFactory.eINSTANCE.createParamType();
			beanClass.setName(SmooksModelConstants.BEAN_CLASS);
			AddCommand.create(domain, resourceConfig,
					SmooksPackage.eINSTANCE.getResourceConfigType_Param(),
					beanClass).execute();
		}
		SmooksModelUtils.setTextToAnyType(beanClass, beanClassStr);
		this.parentEditor.fireEditorDirty(true);
	}

	protected void selectorChanged() {
		if (!isCanFireChange())
			return;
		String selector = selectorText.getText();
		if (selector != null) {
			Command command = SetCommand.create(this.getDomain(),
					resourceConfig, SmooksPackage.eINSTANCE
							.getResourceConfigType_Selector(), selector);
			getDomain().getCommandStack().execute(command);
		}
	}

	@Override
	protected void initSectionUI() {
		configControls();
	}

}
