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
package org.jboss.tools.smooks.configuration.editors.json;

import java.util.Properties;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class JsonDataWizard extends Wizard implements
		IStructuredDataSelectionWizard, INewWizard {

	private JsonDataPathWizardPage pathPage = null;

	private JsonDataConfiguraitonWizardPage configPage = null;

	public JsonDataWizard() {
		super();
	}

	public boolean canFinish() {
		if (configPage != null && pathPage != null) {
			if (configPage.isPageComplete() && pathPage.isPageComplete())
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		if (pathPage == null) {
			pathPage = new JsonDataPathWizardPage("Json Input Data Selection ",
					new String[] {});

		}
		if (configPage == null) {
			configPage = new JsonDataConfiguraitonWizardPage(
					"Json data configuration page");
		}
		this.addPage(pathPage);
		this.addPage(configPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.wizard.
	 * IStructuredDataSelectionWizard#getInputDataTypeID()
	 */
	public String getInputDataTypeID() {
		// TODO Auto-generated method stub
		return SmooksModelUtils.INPUT_TYPE_JSON;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.wizard.
	 * IStructuredDataSelectionWizard#getProperties()
	 */
	public Properties getProperties() {
		Properties properties = new Properties();
		fillProperties(properties);
		return properties;
	}

	private void fillProperties(Properties p) {
		if (configPage != null) {
			String aen = configPage.getArrayElementName();
			if (aen != null && aen.length() != 0) {
				p.setProperty("arrayElementName", aen);
			}

			String rn = configPage.getRootName();
			if (rn != null && rn.length() != 0) {
				p.setProperty("rootName", rn);
			}

			String encoding = configPage.getEncoding();
			if (encoding != null && encoding.length() != 0) {
				p.setProperty("encoding", encoding);
			}

			String sr = configPage.getKeyWhitspaceReplacement();
			if (sr != null && sr.length() != 0) {
				p.setProperty("spaceReplace", sr);
			}

			String pon = configPage.getKeyPrefixOnNumeric();
			if (pon != null && pon.length() != 0) {
				p.setProperty("prefixOnNumeric", pon);
			}

			String nvr = configPage.getNullValueReplacement();
			if (nvr != null && nvr.length() != 0) {
				p.setProperty("nullReplace", nvr);
			}

			String ier = configPage.getIllegalElementNameCharReplacement();
			if (ier != null && ier.length() != 0) {
				p.setProperty("illegalReplace", ier);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.wizard.
	 * IStructuredDataSelectionWizard#getReturnData()
	 */
	public Object getReturnData() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.wizard.
	 * IStructuredDataSelectionWizard#getStructuredDataSourcePath()
	 */
	public String getStructuredDataSourcePath() {
		if (pathPage != null) {
			return pathPage.getFilePath();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.wizard.
	 * IStructuredDataSelectionWizard#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) {

	}
}
