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
import static org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties.PROJECT;
import static org.eclipse.jst.j2ee.internal.web.operations.INewServletClassDataModelProperties.IS_SERVLET_TYPE;
import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.USE_EXISTING_CLASS;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IType;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.j2ee.internal.war.ui.util.WebServletGroupItemProvider;
import org.eclipse.jst.j2ee.webapplication.WebApp;
import org.eclipse.jst.jee.ui.internal.navigator.web.GroupServletItemProvider;
import org.eclipse.jst.jee.ui.internal.navigator.web.WebAppProvider;
import org.eclipse.jst.servlet.ui.internal.wizard.NewWebClassWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.FacetedProjectFramework;
import org.eclipse.wst.common.project.facet.core.internal.FacetedProject;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.ui.MultiSelectFilteredFileSelectionDialog;

public class NewPortletClassWizardPage extends NewWebClassWizardPage {

	public NewPortletClassWizardPage(IDataModel model, String pageName, String pageDesc, String pageTitle, String moduleType) {
		super(model, pageName, pageDesc, pageTitle, moduleType);
	}
	
	@Override
	protected boolean isProjectValid(IProject project) {
		boolean result = super.isProjectValid(project);
		if (result == false) {
			return result;
		}
		try {
			result = FacetedProjectFramework.hasProjectFacet(project, IPortletConstants.PORTLET_FACET_ID);
		} catch (CoreException ce) {
			result = false;
		}
		return result;
	}
	@Override
	protected String getUseExistingCheckboxText() {
		return "Use an existing Portlet class";
	}
	
	@Override
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
	
	@Override
	protected void handleClassButtonSelected() {
		getControl().setCursor(new Cursor(getShell().getDisplay(), SWT.CURSOR_WAIT));
		IProject project = (IProject) model.getProperty(PROJECT);
		IVirtualComponent component = ComponentCore.createComponent(project);
		MultiSelectFilteredFileSelectionDialog ms = new MultiSelectFilteredFileSelectionDialog(
				getShell(),
				"New Portlet",
				"Choose a portlet class:", 
				new String[0], 
				false, 
				project);
		IContainer root = component.getRootFolder().getUnderlyingFolder();
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
	
}
