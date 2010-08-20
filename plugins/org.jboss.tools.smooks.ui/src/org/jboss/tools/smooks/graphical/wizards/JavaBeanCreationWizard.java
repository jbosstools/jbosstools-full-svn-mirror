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
package org.jboss.tools.smooks.graphical.wizards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.tools.smooks.configuration.editors.java.JavaBeanModel;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.ISmooksModelProvider;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.javabean.IBean;
import org.milyn.javabean.dynamic.Model;

/**
 * @author Dart
 * 
 */
public class JavaBeanCreationWizard extends SmooksCreationModelConfigureWizard {

	private JavaBeanCreationWizardPage wizardPage;

	private Object[] checedObjects = null;

	private JavaBeanModel javaBeanModel;


	public JavaBeanCreationWizard(ISmooksModelProvider modelProvider, Object model) {
		super(modelProvider, model);
		this.setWindowTitle(Messages.JavaBeanCreationWizard_WizardTitle);
	}

	public Object[] getBindings() {
		return checedObjects;
	}
	
	/**
	 * @return the javaBeanModel
	 */
	public JavaBeanModel getJavaBeanModel() {
		return javaBeanModel;
	}

	@Override
	public void addPages() {
		Model<SmooksModel> root = modelProvider.getSmooksModel();
		SmooksModel smooksModel = root.getModelRoot();
		IJavaProject project = SmooksUIUtils.getJavaProject((EObject)smooksModel);
		List<String> ids = new ArrayList<String>();
		if (smooksModel != null ) {
//			List<BindingsType> bindingsTypes = SmooksUIUtils.getBindingsTypeList(listType);
//			for (Iterator<?> iterator = bindingsTypes.iterator(); iterator.hasNext();) {
//				BindingsType bindingsType = (BindingsType) iterator.next();
//				String id = bindingsType.getBeanId();
//				if (id != null) {
//					ids.add(id);
//				}
//			}
			List<IBean> beanTypeTypes = SmooksUIUtils.getBeanTypeList(smooksModel);

			for (Iterator<?> iterator = beanTypeTypes.iterator(); iterator.hasNext();) {
				IBean beanType = (IBean) iterator.next();
				String id = beanType.getBeanId();
				if (id != null) {
					ids.add(id);
				}
			}
		}
		if (project != null) {
			wizardPage = new JavaBeanCreationWizardPage("Java Bean Creation Page", project, ids); //$NON-NLS-1$
			this.addPage(wizardPage);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		checedObjects = wizardPage.getSelectionProperties();
		javaBeanModel = wizardPage.getJavaBeanModel();
		return true;
	}

}
