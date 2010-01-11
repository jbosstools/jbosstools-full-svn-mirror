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
package org.jboss.tools.smooks.configuration.editors.xml;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xsd.XSDElementDeclaration;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.templating.model.ModelBuilderException;
import org.jboss.tools.smooks.templating.model.xml.XSDModelBuilder;
import org.xml.sax.SAXException;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class XSDStructuredDataWizardPage extends AbstractFileSelectionWizardPage {

	private Throwable parsingError = null;

	protected CheckboxTableViewer tableViewer = null;

	private boolean fireEvent = true;

	private String rootElementName = null;

	protected Button loadXSDButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.xml.
	 * AbstractFileSelectionWizardPage#loadedTheObject(java.lang.String)
	 */
	@Override
	protected Object loadedTheObject(String path) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public XSDStructuredDataWizardPage(String pageName, boolean multiSelect, Object[] initSelections,
			List<ViewerFilter> filters) {
		super(pageName, multiSelect, initSelections, filters);
		this.setTitle(Messages.XSDStructuredDataWizardPage_Page_Title);
		this.setDescription(Messages.XSDStructuredDataWizardPage_page_description);
	}

	public XSDStructuredDataWizardPage(String pageName) {
		super(pageName, new String[] { "xsd", "wsdl" }); //$NON-NLS-1$ //$NON-NLS-2$
		this.setTitle(Messages.XSDStructuredDataWizardPage_Page_Title);
		this.setDescription(Messages.XSDStructuredDataWizardPage_page_description);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.xml.
	 * AbstractFileSelectionWizardPage#changeWizardPageStatus()
	 */
	@Override
	protected void changeWizardPageStatus() {
		super.changeWizardPageStatus();
		String errorMessage = this.getErrorMessage();
		if (errorMessage == null) {
			
			if (reasourceLoaded) {
				if (tableViewer.getCheckedElements() == null || tableViewer.getCheckedElements().length == 0) {
					errorMessage = Messages.XSDStructuredDataWizardPage_Error_Must_Select_Root;
				}
			} else {
				errorMessage = Messages.XSDStructuredDataWizardPage_Error_Must_Click_Load;
			}
			if(parsingError != null){
				errorMessage = parsingError.getLocalizedMessage();
			}
			setErrorMessage(errorMessage);
			setPageComplete(errorMessage == null);
		}
	}

	/**
	 * @return the rootElementName
	 */
	public String getRootElementName() {
		return rootElementName;
	}

	/**
	 * @param rootElementName
	 *            the rootElementName to set
	 */
	public void setRootElementName(String rootElementName) {
		this.rootElementName = rootElementName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.xml.
	 * AbstractFileSelectionWizardPage
	 * #createFilePathText(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Text createFilePathText(Composite parent) {
		fileTextComposite = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		fileTextComposite.setLayoutData(gd);
		GridLayout xsdtgl = new GridLayout();
		xsdtgl.marginWidth = 0;
		xsdtgl.marginHeight = 0;
		xsdtgl.numColumns = 2;
		fileTextComposite.setLayout(xsdtgl);

		final Text fileText = new Text(fileTextComposite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(new ModifyListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.
			 * swt.events.ModifyEvent)
			 */
			public void modifyText(ModifyEvent e) {
				reasourceLoaded = false;
				if (tableViewer != null) {
					tableViewer.setInput(Collections.emptyList());
				}
			}

		});
		gd.grabExcessHorizontalSpace = true;

		loadXSDButton = new Button(fileTextComposite, SWT.NONE);
		loadXSDButton.setText(Messages.XSDStructuredDataWizardPage_Button_Load);
		loadXSDButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				parsingError = null;
				reasourceLoaded = false;
				if (fileText.getText() == null || fileText.getText().length() == 0) {
					changeWizardPageStatus();
					return;
				}
				
				List<String> list;
				try {
					list = loadElement(fileText.getText());
					if (list == null || list.isEmpty()) {
						setErrorMessage("Can't get the elements in the XSD file.");
					} else {
						tableViewer.setInput(list);
						reasourceLoaded = true;
					}
				} catch (InvocationTargetException e1) {
					parsingError = e1.getTargetException();
					while(parsingError instanceof InvocationTargetException){
						parsingError = ((InvocationTargetException)parsingError).getTargetException();
					}
				} catch (IOException e1) {
					parsingError = e1;
				} catch (ModelBuilderException e1) {
					parsingError = e1;
				}
				changeWizardPageStatus();
			}

		});
		return fileText;
	}

	private List<String> loadElement(String path) throws InvocationTargetException, IOException, ModelBuilderException {
		List<String> elements = new ArrayList<String>();
		if (path == null)
			return null;
		String pp = path.toLowerCase();
		if (pp.endsWith(".wsdl")) { //$NON-NLS-1$
			try {
				List<XSDElementDeclaration> xsdDec = WSDLObjectAnalyzer.loadAllElement(path);
				for (Iterator<?> iterator = xsdDec.iterator(); iterator.hasNext();) {
					XSDElementDeclaration xsdElementDeclaration = (XSDElementDeclaration) iterator.next();
					elements.add(xsdElementDeclaration.getAliasName());
				}
			} catch (ParserConfigurationException e) {
				throw new InvocationTargetException(e);
			} catch (SAXException e) {
				throw new InvocationTargetException(e);
			}
		}
		String file = null;
		file = SmooksUIUtils.parseFilePath(path);
		XSDModelBuilder xsdModelBuilder = new XSDModelBuilder(URI.createFileURI(file));
		Set<String> elementNames = xsdModelBuilder.getRootElementNames();
		Iterator<String> it = elementNames.iterator();
		while (it.hasNext()) {
			String name = it.next();
			elements.add(name);
		}
		return elements;
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		Composite mainComposite = (Composite) getControl();
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		Label label = new Label(mainComposite, SWT.NONE);
		label.setLayoutData(gd);
		label.setText(Messages.XSDStructuredDataWizardPage_Label_Select_Root);
		tableViewer = CheckboxTableViewer.newCheckList(mainComposite, SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 250;
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
					Object checkElement = event.getElement();
					if (checkElement instanceof String) {
						rootElementName = (String) checkElement;
					}
				}
				fireEvent = true;
				changeWizardPageStatus();
			}

		});

	}

}
