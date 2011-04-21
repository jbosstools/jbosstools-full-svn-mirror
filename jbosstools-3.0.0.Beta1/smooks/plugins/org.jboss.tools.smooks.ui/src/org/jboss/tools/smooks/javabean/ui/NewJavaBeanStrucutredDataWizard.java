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
package org.jboss.tools.smooks.javabean.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.ui.IStructuredDataCreationWizard;
import org.jboss.tools.smooks.ui.SmooksUIActivator;

/**
 * @author Dart Peng
 * @Date Aug 5, 2008
 */
public class NewJavaBeanStrucutredDataWizard extends Wizard implements
		IStructuredDataCreationWizard, INewWizard {
	JavaBeanConfigWizardPage page = null;
	IJavaProject project = null;
	Object result = null;
	Properties properties = new Properties();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		if (page == null) {
			page = new JavaBeanConfigWizardPage(project);
			this.addPage(page);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		result = page.getJavaBeanModel();
		return true;
	}

	public Object getTreeViewerInputContents() {
		List<Object> list = new ArrayList<Object>();
		list.add(result);
		return list;
	}

	public void init(IEditorSite site, IEditorInput input) {
		if (input != null && input instanceof IFileEditorInput) {
			IFileEditorInput fi = (IFileEditorInput) input;
			IProject project = fi.getFile().getProject();
			if (project instanceof IJavaProject) {
				this.project = (IJavaProject) project;
			} else {
				this.project = JavaCore.create(project);
			}
		}
	}

	public String getInputDataTypeID() {
		return SmooksUIActivator.DATA_TYPE_ID_JAVABEAN;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		if (selection != null) {
			Object obj = selection.getFirstElement();
			if (obj instanceof JavaProject) {
				this.project = (JavaProject) obj;
			}
			if (obj instanceof IResource) {
				IProject project = ((IResource) obj).getProject();
				this.project = JavaCore.create(project);
			}

			if (project == null) {
				if (obj instanceof IAdaptable) {
					IResource relateResource = (IResource) ((IAdaptable) obj)
							.getAdapter(IResource.class);
					IProject project = relateResource.getProject();
					this.project = JavaCore.create(project);
				}
			}
		}
	}

	public String getStructuredDataSourcePath() {
		JavaBeanModel model = page.getJavaBeanModel();
		if (model != null) {
			Class clazz = model.getBeanClass();
			if (clazz != null)
				return clazz.getName();
		}
		return null;
	}
}
