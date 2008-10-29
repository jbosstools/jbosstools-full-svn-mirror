/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * Kaloyan Raev, kaloyan.raev@sap.com
 *******************************************************************************/
package org.jboss.tools.portlet.ui.internal.wizard;
import static org.eclipse.jst.j2ee.application.internal.operations.IAnnotationsDataModel.USE_ANNOTATIONS;
import static org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties.CLASS_NAME;
import static org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties.PROJECT;
import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.USE_EXISTING_CLASS;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.BROWSE_BUTTON_LABEL;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.CLASS_NAME_LABEL;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.j2ee.internal.war.ui.util.WebServletGroupItemProvider;
import org.eclipse.jst.j2ee.internal.wizard.AnnotationsStandaloneGroup;
import org.eclipse.jst.j2ee.internal.wizard.NewJavaClassWizardPage;
import org.eclipse.jst.j2ee.webapplication.WebApp;
import org.eclipse.jst.jee.ui.internal.navigator.web.GroupServletItemProvider;
import org.eclipse.jst.jee.ui.internal.navigator.web.WebAppProvider;
import org.eclipse.jst.servlet.ui.internal.plugin.WEBUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.FacetedProjectFramework;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.ui.MultiSelectFilteredFileSelectionDialog;
import org.jboss.tools.portlet.ui.internal.wizard.xpl.NewJavaClassWizardPageEx;

public class NewPortletClassWizardPage extends NewJavaClassWizardPageEx {

	protected AnnotationsStandaloneGroup annotationsGroup;
	
	protected Button existingClassButton;
	protected Label existingClassLabel;
	protected Text existingClassText;
	protected Button existingButton;
	
	public NewPortletClassWizardPage(IDataModel model, String pageName, String pageDesc, String pageTitle, String moduleType) {
		super(model, pageName, pageDesc, pageTitle, moduleType);
	}
	
	@Override
	protected Composite createTopLevelComposite(Composite parent) {
		Composite composite = super.createTopLevelComposite(parent);
		
		//projectNameLabel.setText(WEBUIMessages.WEB_PROJECT_LBL);
		
		addSeperator(composite, 3);
		createUseExistingGroup(composite);
		//createAnnotationsGroup(composite);
		model.setProperty(USE_ANNOTATIONS, false);
		
		Dialog.applyDialogFont(composite);
		
		return composite;
	}
	
	@Override
	protected boolean isProjectValid(IProject project) {
		boolean result;
		try {
			result = project.isAccessible() && 
				project.hasNature(JavaCore.NATURE_ID);
		} catch (CoreException ce) {
			result = false;
		}
		return result;
		
	}
	
	protected String getUseExistingCheckboxText() {
		return "Use an existing Portlet class";
	}
	
	
	protected String getUseExistingProperty() {
		return USE_EXISTING_CLASS;
	}

	@Override
	protected IProject getExtendedSelectedProject(Object selection) {
		if (selection instanceof WebServletGroupItemProvider) {
			WebApp webApp = (WebApp) ((WebServletGroupItemProvider) selection).getParent();
			return ProjectUtilities.getProject(webApp);
		} else if(selection instanceof WebAppProvider){
			return ((WebAppProvider) selection).getProject();
		} else if(selection instanceof GroupServletItemProvider){
			org.eclipse.jst.javaee.web.WebApp webApp = (org.eclipse.jst.javaee.web.WebApp) ((GroupServletItemProvider) selection).getJavaEEObject();
			return ProjectUtilities.getProject(webApp);
		}
		
		return super.getExtendedSelectedProject(selection);
	}
	
	protected void handleClassButtonSelected() {
		getControl().setCursor(new Cursor(getShell().getDisplay(), SWT.CURSOR_WAIT));
		IProject project = (IProject) model.getProperty(PROJECT);
		MultiSelectFilteredFileSelectionDialog ms = new MultiSelectFilteredFileSelectionDialog(
				getShell(),
				"New Portlet",
				"Choose a portlet class:", 
				new String[0], 
				false, 
				project);
		IVirtualComponent component = ComponentCore.createComponent(project);
		IContainer root = null;
		if (component != null) {
			root = component.getRootFolder().getUnderlyingFolder();
		} else {
			root = project;
		}
		ms.setInput(root);
		ms.open();
		if (ms.getReturnCode() == Window.OK) {
			String qualifiedClassName = ""; //$NON-NLS-1$
			IType type = (IType) ms.getFirstResult();
			if (type != null) {
				qualifiedClassName = type.getFullyQualifiedName();
			}
			existingClassText.setText(qualifiedClassName);
		}
		getControl().setCursor(null);
	}
	
	private void createUseExistingGroup(Composite composite) {
		existingButton = new Button(composite, SWT.CHECK);
		existingButton.setText(getUseExistingCheckboxText());
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		data.horizontalSpan = 3;
		existingButton.setLayoutData(data);
		synchHelper.synchCheckbox(existingButton, getUseExistingProperty(), null);
		existingButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleExistingButtonSelected();
			}
		});
		
		existingClassLabel = new Label(composite, SWT.LEFT);
		existingClassLabel.setText(CLASS_NAME_LABEL);
		existingClassLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		existingClassLabel.setEnabled(false);

		existingClassText = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		existingClassText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		existingClassText.setEnabled(false);
		synchHelper.synchText(existingClassText, CLASS_NAME, null);

		existingClassButton = new Button(composite, SWT.PUSH);
		existingClassButton.setText(BROWSE_BUTTON_LABEL);
		existingClassButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		existingClassButton.setEnabled(false);
		existingClassButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleClassButtonSelected();
			}
		});
	}
	
	private void handleExistingButtonSelected() {
		boolean enable = existingButton.getSelection();
		existingClassLabel.setEnabled(enable);
		existingClassButton.setEnabled(enable);
		packageText.setEnabled(!enable);
		packageButton.setEnabled(!enable);
		packageLabel.setEnabled(!enable);
		classText.setEnabled(!enable);
		classLabel.setEnabled(!enable);
		superText.setEnabled(!enable);
		superButton.setEnabled(!enable);
		superLabel.setEnabled(!enable);
	}

}
