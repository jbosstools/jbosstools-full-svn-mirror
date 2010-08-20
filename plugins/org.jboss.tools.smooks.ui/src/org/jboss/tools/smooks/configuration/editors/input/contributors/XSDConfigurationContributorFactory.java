/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.input.contributors;

import java.io.IOException;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;
import org.jboss.tools.smooks.configuration.SmooksModelUtils;
import org.jboss.tools.smooks.configuration.editors.file.FileSelectionListener;
import org.jboss.tools.smooks.configuration.editors.file.FileSelectionPageComponent;
import org.jboss.tools.smooks.configuration.editors.input.InputSourceType;
import org.jboss.tools.smooks.configuration.editors.input.InputTaskPanelContributor;
import org.jboss.tools.smooks.configuration.editors.input.InputTaskPanelContributorFactory;
import org.jboss.tools.smooks.configuration.editors.input.contributors.Messages;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.ICorePackage;
import org.jboss.tools.smooks.model.core.IParam;
import org.jboss.tools.smooks.templating.model.ModelBuilderException;
import org.jboss.tools.smooks.templating.model.xml.XSDModelBuilder;
import org.milyn.javabean.dynamic.Model;

/**
 * XSD configuration contributor factory.
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class XSDConfigurationContributorFactory implements InputTaskPanelContributorFactory {

	public InputTaskPanelContributor newInstance(FormToolkit toolkit, EditorPart editorPart, Model<SmooksModel> configModel) {
		return new PanelContributor(toolkit, editorPart, configModel);
	}

	public void setInputSourceType(InputSourceType inputSourceType) {
	}

	private class PanelContributor implements InputTaskPanelContributor {
		
		private EditorPart editorPart;
		private SmooksModel modelRoot;
		private EditingDomain editingDomain;
		private Composite inputSourceConfigComposite;
		private FileSelectionPageComponent pageSelectionComponent;
		private Combo rootElementsDropDown;
		private Composite xsdRootElementSelectionSection;

		public PanelContributor(FormToolkit toolkit, EditorPart editorPart, Model<SmooksModel> configModel) {
			this.editorPart = editorPart;
			this.modelRoot = configModel.getModelRoot();
			this.editingDomain = modelRoot.getModelProvider().getEditingDomain();
		}

		public void setInputSourceConfigComposite(Composite inputSourceConfigComposite) {
			this.inputSourceConfigComposite = inputSourceConfigComposite;
		}

		public void addInputSourceTypeConfigControls() {
			Composite xsdFileSelectionSection = new Composite(inputSourceConfigComposite, SWT.NONE);

			pageSelectionComponent = new FileSelectionPageComponent(editorPart.getEditorSite().getShell(), new String[] {"xsd"});
			pageSelectionComponent.setLabel(Messages.XSDConfigurationContributorFactory_XML_Schema_File);
			pageSelectionComponent.createControl(xsdFileSelectionSection);
			pageSelectionComponent.addFileSelectionListener(new FileSelectionListener() {
				public void fileSelected(String fileName) {
					CompoundCommand compoundCommand = new CompoundCommand();			

					IParam xsdInputParam = setInputTypeInfo(compoundCommand);
					populateRootElementsDropDown(xsdInputParam, compoundCommand);

					if(!compoundCommand.isEmpty()) {
						editingDomain.getCommandStack().execute(compoundCommand);
					}
				}
			});
			xsdFileSelectionSection.layout();
			
			xsdRootElementSelectionSection = new Composite(inputSourceConfigComposite, SWT.NONE);
			createRootElementDropDown(xsdRootElementSelectionSection);

			xsdRootElementSelectionSection.layout();
			xsdRootElementSelectionSection.setVisible(false);

			IParam xsdFileParam = getXSDParam();
			if(xsdFileParam != null) {
				String[] xsdPathTokens = xsdFileParam.getValue().split(";"); //$NON-NLS-1$

				pageSelectionComponent.setFilePath(xsdPathTokens[0]);
				if(xsdPathTokens.length == 2) {
					populateRootElementsDropDown(xsdPathTokens[0], xsdPathTokens[1]);
				}
			}			
			
			inputSourceConfigComposite.setBackgroundMode(SWT.TRANSPARENT);
			inputSourceConfigComposite.layout();			
		}

		protected IParam setInputTypeInfo(CompoundCommand compoundCommand) {
			String xsdFile = pageSelectionComponent.getFilePath();
			IParam xsdInputParam = getXSDParam();

			if(xsdInputParam == null) {
				xsdInputParam = SmooksUIUtils.recordInputDataInfomation(editingDomain, modelRoot.getParams(), SmooksModelUtils.INPUT_TYPE_XSD, xsdFile, null, compoundCommand);
			} else {
				setParamValue(xsdInputParam, xsdFile, compoundCommand);
			}
			
			return xsdInputParam;
		}

		private void setParamValue(IParam param, String xsdFile, CompoundCommand compoundCommand) {
			Command command = SetCommand.create(editingDomain, param, ICorePackage.Literals.PARAM__VALUE, xsdFile);
			if (command.canExecute()) {
				if(compoundCommand == null) {
					editingDomain.getCommandStack().execute(command);
				} else {
					compoundCommand.append(command);
				}
			}
		}

		private void createRootElementDropDown(Composite xsdRootElementSelectionSection) {			
			GridLayout layout = new GridLayout();
			GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
			
			layout.numColumns = 2;
			xsdRootElementSelectionSection.setLayout(layout);
			xsdRootElementSelectionSection.setLayoutData(gd);			

			gd = new GridData(GridData.BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING);
			Label rootElementsLabel = new Label(xsdRootElementSelectionSection, SWT.NONE);
	        rootElementsLabel.setLayoutData(gd);
			rootElementsLabel.setText(Messages.XSDConfigurationContributorFactory_Root_Element_Name);
			
			gd = new GridData(GridData.FILL_HORIZONTAL);
	        rootElementsDropDown = new Combo(xsdRootElementSelectionSection, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.RESIZE);
	        rootElementsDropDown.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					setRootElement(rootElementsDropDown.getItem(rootElementsDropDown.getSelectionIndex()));
				}				
				public void widgetDefaultSelected(SelectionEvent event) {
				}
			});
			rootElementsDropDown.setLayoutData(gd);
		}

		protected void setRootElement(String rootElementName) {
			IParam xsdParam = getXSDParam();
			
			if(xsdParam != null) {
				String[] paramTokens = xsdParam.getValue().split(";"); //$NON-NLS-1$
				setParamValue(xsdParam, paramTokens[0] + ";" + rootElementName, null); //$NON-NLS-1$
			}
		}

		private IParam getXSDParam() {
			return modelRoot.getParams().getParam(SmooksModelUtils.INPUT_TYPE_XSD);
		}

		private void populateRootElementsDropDown(IParam xsdInputParam, CompoundCommand compoundCommand) {
			rootElementsDropDown.removeAll();
			xsdRootElementSelectionSection.setVisible(false);
			
			String xsdFile = pageSelectionComponent.getFilePath();
			if(xsdFile != null && xsdFile.trim().length() > 0) {
				populateRootElementsDropDown(xsdFile, null);
				
				String rootElementName = rootElementsDropDown.getText();
				setParamValue(xsdInputParam, xsdFile + ";" + rootElementName, compoundCommand);
			}
		}

		private void populateRootElementsDropDown(String xsdFile, String selectedRootElement) {
			URI xsdURI = toURI(xsdFile);

			if(xsdURI != null) {
				try {
					XSDModelBuilder xsdModelBuilder = new XSDModelBuilder(xsdURI);
					Set<String> rootElementNames = xsdModelBuilder.getRootElementNames();
					
					if(!rootElementNames.isEmpty()) {
						for(String rootElementName : rootElementNames) {
							rootElementsDropDown.add(rootElementName);
							if(rootElementName.equals(selectedRootElement)) {
								rootElementsDropDown.select(rootElementsDropDown.indexOf(rootElementName));
							}
						}
						if(selectedRootElement == null) {
							rootElementsDropDown.select(0);
						}
					}
					
					xsdRootElementSelectionSection.setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ModelBuilderException e) {
					e.printStackTrace();
				}
			}
		}

		private URI toURI(String xsdFile) {
			return URI.createFileURI(SmooksUIUtils.parseFilePath(xsdFile));
		}
	}
}
