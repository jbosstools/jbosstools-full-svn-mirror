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
package org.jboss.tools.smooks.edimap.editor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.ModelPanelCreator;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.TagList;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataContentProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataLabelProvider;
import org.jboss.tools.smooks.configuration.validate.ISmooksModelValidateListener;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.common.AbstractAnyType;
import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.medi.Delimiters;
import org.jboss.tools.smooks.model.medi.Description;
import org.jboss.tools.smooks.model.medi.DocumentRoot;
import org.jboss.tools.smooks.model.medi.Segments;

/**
 * @author Dart
 * 
 */
public class EDIMapFormPage extends FormPage implements ISmooksModelValidateListener, ISmooksGraphChangeListener,
		ISelectionChangedListener {

	private ISmooksModelProvider modelProvider;

	private IEditingDomainItemProvider domainProvider;

	private Delimiters delimiters = null;

	private Description description = null;

	private AdapterFactoryEditingDomain editingDomain;

	private ModelPanelCreator delimitersCreator;

	private ModelPanelCreator descriptionCreator;

	private ModelPanelCreator segmentsCreator;

	private TreeViewer segmentsViewer;

	private Composite mappingModelPropertiesComposite;

	private String ediFilePath = null;

	private TreeViewer ediDataViewer;

	private Composite linesPanel;

	public EDIMapFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		if (editor instanceof ISmooksModelProvider) {
			modelProvider = (ISmooksModelProvider) editor;
			modelProvider.getSmooksGraphicsExt().addSmooksGraphChangeListener(this);
		}

		if (editor instanceof IEditingDomainItemProvider) {
			domainProvider = (IEditingDomainItemProvider) editor;
		}
		segmentsCreator = new ModelPanelCreator();
	}

	public EDIMapFormPage(String id, String title) {
		super(id, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.validate.ISmooksModelValidateListener
	 * #validateEnd(java.util.List)
	 */
	public void validateEnd(List<Diagnostic> diagnosticResult) {
		if (delimitersCreator != null) {
			delimitersCreator.markPropertyUI(modelProvider.getDiagnosticList());
		}
		if (descriptionCreator != null) {
			descriptionCreator.markPropertyUI(modelProvider.getDiagnosticList());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.validate.ISmooksModelValidateListener
	 * #validateStart()
	 */
	public void validateStart() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormPage#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		if (this.modelProvider != null) {
			DocumentRoot root = (DocumentRoot) modelProvider.getSmooksModel();

			this.delimiters = root.getEdimap().getDelimiters();

			this.description = root.getEdimap().getDescription();

			editingDomain = (AdapterFactoryEditingDomain) modelProvider.getEditingDomain();

			IItemPropertySource propertySource1 = (IItemPropertySource) editingDomain.getAdapterFactory().adapt(
					delimiters, IItemPropertySource.class);

			delimitersCreator = new ModelPanelCreator(delimiters, propertySource1);

			IItemPropertySource propertySource2 = (IItemPropertySource) editingDomain.getAdapterFactory().adapt(
					description, IItemPropertySource.class);

			descriptionCreator = new ModelPanelCreator(description, propertySource2);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui
	 * .forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());
		form.setText("EDI Message Mapping");

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 13;
		gridLayout.numColumns = 2;
		// gridLayout.makeColumnsEqualWidth = true;
		form.getBody().setLayout(gridLayout);

		Section descriptionSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = GridData.BEGINNING;
		descriptionSection.setLayoutData(gd);
		descriptionSection.setText("Mapping Descriptor");
		FillLayout flayout = new FillLayout();
		descriptionSection.setLayout(flayout);

		Composite mainComposite = toolkit.createComposite(descriptionSection, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		mainComposite.setLayout(gl);
		descriptionSection.setClient(mainComposite);

		if (descriptionCreator != null) {
			descriptionCreator.createModelPanel(toolkit, mainComposite, modelProvider, getEditor());
		}

		Section delimiterSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = GridData.BEGINNING;
		gd.verticalSpan = 2;
		delimiterSection.setLayoutData(gd);
		delimiterSection.setText("Delimiters");
		FillLayout flayout1 = new FillLayout();
		delimiterSection.setLayout(flayout1);

		Composite mainComposite1 = toolkit.createComposite(delimiterSection, SWT.NONE);
		GridLayout gl1 = new GridLayout();
		gl1.numColumns = 2;
		mainComposite1.setLayout(gl1);
		delimiterSection.setClient(mainComposite1);

		AttributeFieldEditPart pathEditPart = SmooksUIUtils.createFileSelectionTextFieldEditor("EDI Mapping File",
				mainComposite1, editingDomain, toolkit, null, null, SmooksUIUtils.VALUE_TYPE_TEXT, null, null);
		final Text fileText = (Text) pathEditPart.getContentControl();
		fileText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				ediFilePath = fileText.getText();
				setDataViewerModel();
			}
		});

		if (delimitersCreator != null) {
			delimitersCreator.createModelPanel(toolkit, mainComposite1, modelProvider, getEditor());
		}

		Section testSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = GridData.BEGINNING;
		// gd.horizontalSpan = 2;
		testSection.setLayoutData(gd);
		testSection.setText("Mapping Descriptor");
		FillLayout flayout2 = new FillLayout();
		testSection.setLayout(flayout2);

		Section ediModelViewerSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		ediModelViewerSection.setLayoutData(gd);
		ediModelViewerSection.setText("EDI Mapping");
		FillLayout flayout3 = new FillLayout();
		ediModelViewerSection.setLayout(flayout3);

		Composite viewerComposite = toolkit.createComposite(ediModelViewerSection);
		ediModelViewerSection.setClient(viewerComposite);
		GridLayout vgl = new GridLayout();
		vgl.numColumns = 2;

		viewerComposite.setLayout(vgl);
		
		CTabFolder tabForlder = new CTabFolder(viewerComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		tabForlder.setLayoutData(gd);
		
		CTabItem item = new CTabItem(tabForlder, SWT.NONE);
		item.setText("mapping");

		SashForm sashForm = new SashForm(tabForlder, SWT.NONE);
		item.setControl(sashForm);

		gd = new GridData(GridData.FILL_BOTH);
		sashForm.setLayoutData(gd);

		Composite tagComposite = toolkit.createComposite(sashForm);
		tagComposite.setBackground(new Color(null, 128, 128, 128));
		FillLayout tagLayout = new FillLayout();
		tagLayout.marginHeight = 1;
		tagLayout.marginWidth = 1;
		tagComposite.setLayout(tagLayout);

		segmentsViewer = new TreeViewer(tagComposite, SWT.NONE | SWT.FULL_SELECTION);
		segmentsViewer.addSelectionChangedListener(this);
		initSegmentsViewer();
		
		mappingModelPropertiesComposite = toolkit.createComposite(sashForm);
		gd = new GridData(GridData.FILL_BOTH);
		// gd.widthHint = 180
		mappingModelPropertiesComposite.setLayoutData(gd);
		GridLayout mgl = new GridLayout();
		mgl.numColumns = 2;
		mappingModelPropertiesComposite.setLayout(mgl);

		CTabItem item2 = new CTabItem(tabForlder,SWT.NONE);
		item2.setText("EDI");
//		linesPanel = toolkit.createComposite(sashForm);
//
		Composite dataComposite = toolkit.createComposite(tabForlder);
		dataComposite.setBackground(new Color(null, 128, 128, 128));
		FillLayout dataLayout = new FillLayout();
		dataLayout.marginHeight = 1;
		dataLayout.marginWidth = 1;
		dataComposite.setLayout(dataLayout);

		ediDataViewer = new TreeViewer(dataComposite, SWT.LEFT | SWT.READ_ONLY);
		ediDataViewer.setContentProvider(new XMLStructuredDataContentProvider());
		ediDataViewer.setLabelProvider(new XMLStructuredDataLabelProvider());
		
		item2.setControl(dataComposite);
		
		tabForlder.setSelection(0);
		tabForlder.setSimple(false);
		tabForlder.setMRUVisible(true);
//		tabForlder.setSingle(true);
	}

	private void setDataViewerModel() {
		ediDataViewer.setInput(new TagList());
		if (delimiters != null && ediFilePath != null) {
			String segment = delimiters.getSegment();
			String field = delimiters.getField();
			String component = delimiters.getComponent();
			String subComponent = delimiters.getSubComponent();
			char se = 1;
			char f = 1;
			char c = 1;
			char su = 1;
			if (segment != null) {
				if (segment.length() == 1)
					se = segment.toCharArray()[0];
			}

			if (field != null) {
				if (field.length() == 1)
					f = field.toCharArray()[0];
			}

			if (component != null) {
				if (component.length() == 1)
					c = component.toCharArray()[0];
			}

			if (subComponent != null) {
				if (subComponent.length() == 1)
					su = subComponent.toCharArray()[0];
			}

			EDIFileParser parser = new EDIFileParser();
			try {
				TagList tl = parser.parserEDIFile(ediFilePath, se, f, c, su);
				ediDataViewer.setInput(tl);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void initSegmentsViewer() {
		if (editingDomain != null) {
			DocumentRoot root = (DocumentRoot) modelProvider.getSmooksModel();
			Segments segments = root.getEdimap().getSegments();
			segmentsViewer.setContentProvider(new AdapterFactoryContentProvider(editingDomain.getAdapterFactory()));
			segmentsViewer.setLabelProvider(new DecoratingLabelProvider(new AdapterFactoryLabelProvider(editingDomain
					.getAdapterFactory()) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider#
				 * getText(java.lang.Object)
				 */
				@Override
				public String getText(Object object) {
					Object obj = AdapterFactoryEditingDomain.unwrap(object);
					if (obj instanceof AbstractAnyType) {
						return super.getText(obj);
					}
					return super.getText(object);
				}

			}, SmooksConfigurationActivator.getDefault().getWorkbench().getDecoratorManager().getLabelDecorator()));

			segmentsViewer.setInput(segments);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener#
	 * saveComplete
	 * (org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType)
	 */
	public void saveComplete(SmooksGraphicsExtType extType) {

	}

	private void disposeMappingModelPropertiesCompoisiteControls() {
		if (mappingModelPropertiesComposite != null) {
			Control[] children = mappingModelPropertiesComposite.getChildren();
			for (int i = 0; i < children.length; i++) {
				Control child = children[i];
				child.dispose();
				child = null;
			}
		}
	}

	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		Object model = selection.getFirstElement();
		if (model != null) {
			disposeMappingModelPropertiesCompoisiteControls();
			IItemPropertySource itemPropertySource = (IItemPropertySource) editingDomain.getAdapterFactory().adapt(
					model, IItemPropertySource.class);
			segmentsCreator.createModelPanel((EObject) model, this.getManagedForm().getToolkit(),
					mappingModelPropertiesComposite, itemPropertySource, modelProvider, getEditor());
			mappingModelPropertiesComposite.getParent().layout();
		}
	}
}
