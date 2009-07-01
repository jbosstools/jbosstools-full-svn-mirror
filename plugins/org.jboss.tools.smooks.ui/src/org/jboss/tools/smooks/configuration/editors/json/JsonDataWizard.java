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

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.configuration.editors.json.JsonDataConfiguraitonWizardPage.KeyValueModel;
import org.jboss.tools.smooks.configuration.editors.uitls.JsonInputDataParser;
import org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard;
import org.jboss.tools.smooks.model.json.JsonFactory;
import org.jboss.tools.smooks.model.json.JsonReader;
import org.jboss.tools.smooks.model.json.Key;
import org.jboss.tools.smooks.model.json.KeyMap;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
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
		return true;
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

			boolean createJsonReader = configPage.isCreateJsonReader();

			if (createJsonReader) {
				p.setProperty(JsonInputDataParser.LINK_JSON_READER, "true");
				return;
			}

			List<KeyValueModel> keyMapList = configPage.getKeyValueList();
			for (Iterator<?> iterator = keyMapList.iterator(); iterator
					.hasNext();) {
				KeyValueModel keyValueModel = (KeyValueModel) iterator.next();
				String key = keyValueModel.getKey();
				String value = keyValueModel.getValue();
				p.setProperty(JsonInputDataParser.KEY + key, value);
			}

			String aen = configPage.getArrayElementName();
			if (aen != null && aen.length() != 0) {
				p.setProperty(JsonInputDataParser.ARRAY_ELEMENT_NAME, aen);
			}

			String rn = configPage.getRootName();
			if (rn != null && rn.length() != 0) {
				p.setProperty(JsonInputDataParser.ROOT_NAME, rn);
			}

			String encoding = configPage.getEncoding();
			if (encoding != null && encoding.length() != 0) {
				p.setProperty(JsonInputDataParser.ENCODING2, encoding);
			}

			String sr = configPage.getKeyWhitspaceReplacement();
			if (sr != null && sr.length() != 0) {
				p.setProperty(JsonInputDataParser.SPACE_REPLACE, sr);
			}

			String pon = configPage.getKeyPrefixOnNumeric();
			if (pon != null && pon.length() != 0) {
				p.setProperty(JsonInputDataParser.PREFIX_ON_NUMERIC, pon);
			}

			String nvr = configPage.getNullValueReplacement();
			if (nvr != null && nvr.length() != 0) {
				p.setProperty(JsonInputDataParser.NULL_REPLACE, nvr);
			}

			String ier = configPage.getIllegalElementNameCharReplacement();
			if (ier != null && ier.length() != 0) {
				p.setProperty(JsonInputDataParser.ILLEGAL_REPLACE, ier);
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

	public void complate(SmooksMultiFormEditor formEditor) {
		if (configPage != null && configPage.isCreateJsonReader()) {
			List<KeyValueModel> keyMapList = configPage.getKeyValueList();

			JsonReader reader = JsonFactory.eINSTANCE.createJsonReader();
			if (keyMapList != null && !keyMapList.isEmpty()) {
				KeyMap map = JsonFactory.eINSTANCE.createKeyMap();
				for (Iterator<?> iterator = keyMapList.iterator(); iterator
						.hasNext();) {
					KeyValueModel keyValueModel = (KeyValueModel) iterator
							.next();
					String key = keyValueModel.getKey();
					String value = keyValueModel.getValue();
					Key k = JsonFactory.eINSTANCE.createKey();
					k.setFrom(key);
					k.setTo(value);
					map.getKey().add(k);
				}
				reader.setKeyMap(map);
			}

			String aen = configPage.getArrayElementName();
			if (aen != null && aen.length() != 0) {
				reader.setArrayElementName(aen);
			}

			String rn = configPage.getRootName();
			if (rn != null && rn.length() != 0) {
				reader.setRootName(rn);
			}

			String encoding = configPage.getEncoding();
			if (encoding != null && encoding.length() != 0) {
				reader.setEncoding(encoding);
			}

			String sr = configPage.getKeyWhitspaceReplacement();
			if (sr != null && sr.length() != 0) {
				reader.setKeyWhitspaceReplacement(sr);
			}

			String pon = configPage.getKeyPrefixOnNumeric();
			if (pon != null && pon.length() != 0) {
				reader.setKeyPrefixOnNumeric(pon);
			}

			String nvr = configPage.getNullValueReplacement();
			if (nvr != null && nvr.length() != 0) {
				reader.setNullValueReplacement(nvr);
			}

			String ier = configPage.getIllegalElementNameCharReplacement();
			if (ier != null && ier.length() != 0) {
				reader.setIllegalElementNameCharReplacement(ier);
			}

			SmooksResourceListType resourceList = null;
			Object smooksModel = formEditor.getSmooksModel();
			if (smooksModel instanceof DocumentRoot) {
				resourceList = ((DocumentRoot) smooksModel)
						.getSmooksResourceList();
			}
			Command command = AddCommand
					.create(
							formEditor.getEditingDomain(),
							resourceList,
							SmooksPackage.eINSTANCE
									.getSmooksResourceListType_AbstractReader(),
							reader);
			formEditor.getEditingDomain().getCommandStack().execute(command);
		}
	}
}
