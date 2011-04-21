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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xsd.XSDElementDeclaration;
import org.jboss.tools.smooks.configuration.editors.ClassPathFileProcessor;
import org.jboss.tools.smooks.configuration.editors.CurrentProjecViewerFilter;
import org.jboss.tools.smooks.configuration.editors.FileSelectionWizard;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.XSDListContentProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XSDListLabelProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XSDObjectAnalyzer;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;

/**
 * @author Dart
 * 
 */
public class XSLTemplateCreationWizardPage extends WizardPage {

	public static final int TEMPLATE_TYPE_NONE = 0;

	public static final int TEMPLATE_TYPE_XSL = 1;

	public static final int TEMPLATE_TYPE_XML_XSD = 2;

	private Combo typeCombo;
	private CheckboxTableViewer tableViewer;
	private Button fileBrowseButton1;
	private Text contentFileText;
	private Text externalFileText;
	private Button fileBrowseButton;
	private ISmooksModelProvider smooksModelProvider = null;
	protected boolean fireEvent = true;
	protected String rootElementName;

	private static final Object EMPTY_TABLEVIEWER_INPUT = new Object();

	public XSLTemplateCreationWizardPage(String pageName, String title, ImageDescriptor titleImage,
			ISmooksModelProvider smooksModelProvider) {
		super(pageName, title, titleImage);
		this.smooksModelProvider = smooksModelProvider;
	}

	public XSLTemplateCreationWizardPage(String pageName, ISmooksModelProvider smooksModelProvider) {
		super(pageName);
		this.smooksModelProvider = smooksModelProvider;
	}

	public int getTemplateType() {
		return typeCombo.getSelectionIndex();
	}

	public String getExtenalFilePath() {
		return externalFileText.getText();
	}

	public String getRootElementName() {
		return rootElementName;
	}

	public String getContentsFile() {
		return contentFileText.getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);

		GridData gd = new GridData(GridData.FILL_BOTH);
		mainComposite.setLayoutData(gd);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		mainComposite.setLayout(gridLayout);

		Label templateType = new Label(mainComposite, SWT.NONE);
		templateType.setText(Messages.XSLTemplateCreationWizardPage_Label_Template_Type);

		typeCombo = new Combo(mainComposite, SWT.BORDER | SWT.READ_ONLY);
		typeCombo.add(""); //$NON-NLS-1$
		typeCombo.add(Messages.XSLTemplateCreationWizardPage_Combo_External_File);
		typeCombo.add(Messages.XSLTemplateCreationWizardPage_Combo_Inner_Contents);

		typeCombo.addSelectionListener(new SelectionAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = typeCombo.getSelectionIndex();
				setEnabledToAllControls(true);
				if (index == 0) {
					setEnabledToAllControls(false);
				}
				if (index == 1) {
					disableInnerContentControls();
				}
				if (index == 2) {
					disableExternalFileControls();
				}
				updatePage();
			}

		});

		gd = new GridData(GridData.FILL_HORIZONTAL);
		typeCombo.setLayoutData(gd);

		// create external file selection text and button

		Label externalFilePathLabel = new Label(mainComposite, SWT.NONE);
		externalFilePathLabel.setText(Messages.XSLTemplateCreationWizardPage_Label_External_Path);

		Composite externalFileComposite = new Composite(mainComposite, SWT.NONE);

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginHeight = 0;
		gl.marginWidth = 0;

		externalFileComposite.setLayout(gl);

		externalFileText = new Text(externalFileComposite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		externalFileText.setLayoutData(gd);
		externalFileText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updatePage();
			}
		});

		fileBrowseButton = new Button(externalFileComposite, SWT.NONE);
		fileBrowseButton.setText(Messages.XSLTemplateCreationWizardPage_Button_Browse);
		fileBrowseButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				FileSelectionWizard wizard = new FileSelectionWizard();
				wizard.setExtensionNames(new String[] { "xsl", "xslt" }); //$NON-NLS-1$ //$NON-NLS-2$
				EObject fm = smooksModelProvider.getSmooksModel();
				IResource resource = SmooksUIUtils.getResource((EObject) fm);
				Object[] initSelections = new Object[] {};
				if (resource != null) {
					initSelections = new Object[] { resource };
				}
				wizard.setFilePathProcessor(new ClassPathFileProcessor());
				wizard.setInitSelections(initSelections);
				List<ViewerFilter> filterList = new ArrayList<ViewerFilter>();
				filterList.add(new CurrentProjecViewerFilter(resource));
				wizard.setViewerFilters(filterList);
				WizardDialog dialog = new WizardDialog(getShell(), wizard);
				if (dialog.open() == Dialog.OK) {
					externalFileText.setText(wizard.getFilePath());
				}
			}

		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		externalFileComposite.setLayoutData(gd);

		// create XML/XSD file selection text and button

		Label xslInnerContentFileLabel = new Label(mainComposite, SWT.NONE);
		xslInnerContentFileLabel.setText(Messages.XSLTemplateCreationWizardPage_Label_XML_XSD_File);

		Composite xslInnerContentFileComposite = new Composite(mainComposite, SWT.NONE);

		GridLayout gl1 = new GridLayout();
		gl1.numColumns = 2;
		gl1.marginHeight = 0;
		gl1.marginWidth = 0;

		xslInnerContentFileComposite.setLayout(gl1);

		contentFileText = new Text(xslInnerContentFileComposite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		contentFileText.setLayoutData(gd);
		contentFileText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				String path = contentFileText.getText();
				tableViewer.getTable().setEnabled(true);
				try {
					List<?> elements = XSDObjectAnalyzer.loadAllElement(path);
					if (elements == null || elements.isEmpty()) {
						tableViewer.setInput(EMPTY_TABLEVIEWER_INPUT);
						tableViewer.getTable().setEnabled(false);
						rootElementName = null;
					} else {
						tableViewer.setInput(elements);
					}
				} catch (InvocationTargetException e1) {
					tableViewer.setInput(EMPTY_TABLEVIEWER_INPUT);
					tableViewer.getTable().setEnabled(false);
					rootElementName = null;
				} catch (IOException e1) {
					tableViewer.setInput(EMPTY_TABLEVIEWER_INPUT);
					tableViewer.getTable().setEnabled(false);
					rootElementName = null;
				}
				updatePage();
			}
		});
		fileBrowseButton1 = new Button(xslInnerContentFileComposite, SWT.NONE);
		fileBrowseButton1.setText(Messages.XSLTemplateCreationWizardPage_Button_Browse);
		fileBrowseButton1.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				FileSelectionWizard wizard = new FileSelectionWizard();
				wizard.setExtensionNames(new String[] { "xml", "xsd" }); //$NON-NLS-1$ //$NON-NLS-2$
				EObject fm = smooksModelProvider.getSmooksModel();
				IResource resource = SmooksUIUtils.getResource((EObject) fm);
				Object[] initSelections = new Object[] {};
				if (resource != null) {
					initSelections = new Object[] { resource };
				}
				wizard.setProcessFilePath(false);
				wizard.setInitSelections(initSelections);
				List<ViewerFilter> filterList = new ArrayList<ViewerFilter>();
				filterList.add(new CurrentProjecViewerFilter(resource));
				wizard.setViewerFilters(filterList);
				WizardDialog dialog = new WizardDialog(getShell(), wizard);
				if (dialog.open() == Dialog.OK) {
					contentFileText.setText(wizard.getFilePath());
				}
			}

		});

		gd = new GridData(GridData.FILL_HORIZONTAL);
		xslInnerContentFileComposite.setLayoutData(gd);

		// create XSD root element selection composite;

		Label chooseElementLabel = new Label(mainComposite, SWT.NONE);
		chooseElementLabel.setText(Messages.XSLTemplateCreationWizardPage_Label_Choose_Root);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		chooseElementLabel.setLayoutData(gd);

		tableViewer = CheckboxTableViewer.newCheckList(mainComposite, SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 250;
		gd.horizontalSpan = 2;
		tableViewer.getControl().setLayoutData(gd);

		tableViewer.setContentProvider(new XSDListContentProvider());
		tableViewer.setLabelProvider(new XSDListLabelProvider());
		tableViewer.addCheckStateListener(new ICheckStateListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.jface.viewers.ICheckStateListener#checkStateChanged
			 * (org.eclipse.jface.viewers.CheckStateChangedEvent)
			 */
			public void checkStateChanged(CheckStateChangedEvent event) {
				if (!fireEvent)
					return;
				rootElementName = null;
				fireEvent = false;
				tableViewer.setAllChecked(false);
				if (event.getChecked()) {
					tableViewer.setChecked(event.getElement(), true);
					rootElementName = ((XSDElementDeclaration) event.getElement()).getAliasName();
				}
				fireEvent = true;
				updatePage();
			}

		});

		this.setControl(mainComposite);

		this.setEnabledToAllControls(false);
	}

	protected void updatePage() {
		String error = null;
		int type = typeCombo.getSelectionIndex();
		if (type == 0) {

		}
		if (type == 1) {
			String path = externalFileText.getText();
			if (path == null || path.length() == 0) {
				error = Messages.XSLTemplateCreationWizardPage_Error_External_Path_Empty;
			}
		}
		if (type == 2) {
			String path = contentFileText.getText();
			if (path == null || path.length() == 0) {
				error = Messages.XSLTemplateCreationWizardPage_Error_XSD_XML_Path_Empty;
			}

			Object input = tableViewer.getInput();
			if (input != EMPTY_TABLEVIEWER_INPUT) {
				if (tableViewer.getCheckedElements().length == 0) {
					error = Messages.XSLTemplateCreationWizardPage_Error_Root_Element_Empty;
				}
			}
		}
		this.setErrorMessage(error);
		this.setPageComplete(error == null);
	}

	protected void disableExternalFileControls() {
		externalFileText.setEnabled(false);
		fileBrowseButton.setEnabled(false);
	}

	protected void disableInnerContentControls() {
		tableViewer.getTable().setEnabled(false);

		contentFileText.setEnabled(false);
		fileBrowseButton1.setEnabled(false);

	}

	protected void setEnabledToAllControls(boolean flag) {
		tableViewer.getTable().setEnabled(flag);

		contentFileText.setEnabled(flag);
		fileBrowseButton1.setEnabled(flag);

		externalFileText.setEnabled(flag);
		fileBrowseButton.setEnabled(flag);
	}

}
