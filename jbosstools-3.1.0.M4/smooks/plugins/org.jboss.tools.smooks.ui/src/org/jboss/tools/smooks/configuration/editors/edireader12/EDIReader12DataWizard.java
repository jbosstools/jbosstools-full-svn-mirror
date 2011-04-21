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
package org.jboss.tools.smooks.configuration.editors.edireader12;

import java.util.Properties;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.configuration.editors.edi.EDIDataParser;
import org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard;
import org.jboss.tools.smooks.model.edi12.EDI12Reader;
import org.jboss.tools.smooks.model.edi12.Edi12Factory;
import org.jboss.tools.smooks.model.edi12.Edi12Package;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart
 * 
 */
public class EDIReader12DataWizard extends Wizard implements IStructuredDataSelectionWizard, INewWizard {

	private EDIReader12MappingDataPathWizardPage mappingFilePage;

	private EDIReader12DataPathWizardPage ediFilePage;

	private SmooksResourceListType resourceList;

	private EditingDomain editingDomain;

	@Override
	public void addPages() {
		ediFilePage = new EDIReader12DataPathWizardPage("EDI Data Page", new String[] {});
		this.addPage(ediFilePage);

		mappingFilePage = new EDIReader12MappingDataPathWizardPage("EDI Config Page", null);
		this.addPage(mappingFilePage);
		super.addPages();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		if (mappingFilePage.isUseAvaliableReader()) {
			return true;
		}
		if (mappingFilePage.isCreateNewReader()) {
			String encoding = mappingFilePage.getEncoding();
			String path = mappingFilePage.getFilePath();
			String validate = mappingFilePage.getValidate();
			EDI12Reader reader = Edi12Factory.eINSTANCE.createEDI12Reader();
			reader.setEncoding(encoding);
			reader.setMappingModel(path);
			if (validate != null && validate.length() != 0) {
				reader.setValidate(Boolean.valueOf(validate));
			}
			Command command = AddCommand.create(editingDomain, resourceList,
					SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP, FeatureMapUtil
							.createEntry(Edi12Package.Literals.EDI12_DOCUMENT_ROOT__READER, reader));
			editingDomain.getCommandStack().execute(command);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.wizard.
	 * IStructuredDataSelectionWizard
	 * #complate(org.jboss.tools.smooks.configuration
	 * .editors.SmooksMultiFormEditor)
	 */
	public void complate(SmooksMultiFormEditor formEditor) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.wizard.
	 * IStructuredDataSelectionWizard#getInputDataTypeID()
	 */
	public String getInputDataTypeID() {
		return SmooksModelUtils.INPUT_TYPE_EDI_1_2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.wizard.
	 * IStructuredDataSelectionWizard#getProperties()
	 */
	public Properties getProperties() {
		Properties pros = new Properties();
		if (mappingFilePage.isUseAvaliableReader() || mappingFilePage.isCreateNewReader()) {
			pros.put(EDIDataParser.USE_AVAILABEL_READER, "true");
			return pros;
		}
		String encoding = mappingFilePage.getEncoding();
		if (encoding != null && encoding.length() != 0) {
			pros.put(EDIDataParser.ENCODING, encoding);
		}
		
		String path = mappingFilePage.getFilePath();
		if(path != null && path.length() != 0){
		pros.put(EDIDataParser.MAPPING_MODEL, path);
		}
		
		String validate = mappingFilePage.getValidate();
		if(validate != null && validate.length() != 0){
			pros.put(EDIDataParser.VALIDATE, validate);
		}
		return pros;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.wizard.
	 * IStructuredDataSelectionWizard#getReturnData()
	 */
	public Object getReturnData() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.wizard.
	 * IStructuredDataSelectionWizard#getStructuredDataSourcePath()
	 */
	public String getStructuredDataSourcePath() {
		if (ediFilePage.getFilePath() != null) {
			return ediFilePage.getFilePath();
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
		IEditorPart editorPart = site.getWorkbenchWindow().getActivePage().findEditor(input);
		if (editorPart != null && editorPart instanceof SmooksMultiFormEditor) {
			SmooksMultiFormEditor formEditor = (SmooksMultiFormEditor) editorPart;
			Object smooksModel = formEditor.getSmooksModel();
			if (smooksModel instanceof DocumentRoot) {
				resourceList = ((DocumentRoot) smooksModel).getSmooksResourceList();
			}
			editingDomain = formEditor.getEditingDomain();
		}
		if (this.mappingFilePage != null) {
			mappingFilePage.setSmooksResourceList(resourceList);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

}
