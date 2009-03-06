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
package org.jboss.tools.smooks.ui.editors;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.NormalSmooksModelPackage;
import org.jboss.tools.smooks.model.ImportType;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksPackage;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.AnalyzeResult;
import org.jboss.tools.smooks.ui.IAnalyzeListener;
import org.jboss.tools.smooks.ui.SmooksUIActivator;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;
import org.jboss.tools.smooks.utils.SmooksGraphConstants;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart Peng Date : 2008-9-9
 */
public class SmooksNormalContentEditFormPage extends FormPage implements
		IAnalyzeListener {

	protected NormalSmooksModelPackage modelPackage = null;

	protected SmooksResourceConfigFormBlock resourceBlock = null;

	protected ResourceConfigType transformType = null;

	private Button saxButton;

	private Button domButton;

	private Button saxdomButton;

	private EditingDomain domain;

	private List hidenResourceConfigs;

	private boolean disableGUI = false;

	public boolean isDisableGUI() {
		return disableGUI;
	}

	public void setDisableGUI(boolean disableGUI) {
		this.disableGUI = disableGUI;
		setGUIStates();
	}

	private Section parseTypeSection;

	private Button addFileButton;

	private Button removeFileButton;

	private Button editFileButton;

	private TableViewer fileViewer;

	public SmooksNormalContentEditFormPage(FormEditor editor, String id,
			String title, NormalSmooksModelPackage modelPacakge) {
		super(editor, id, title);
		domain = ((SmooksFormEditor) editor).getEditingDomain();
		this.createResourceConfigFormBlock();
		this.setModelPackage(modelPacakge);
	}

	public SmooksNormalContentEditFormPage(String id, String title,
			NormalSmooksModelPackage modelPacakge) {
		super(id, title);
		this.createResourceConfigFormBlock();
		this.setModelPackage(modelPacakge);
	}

	protected void createResourceConfigFormBlock() {
		resourceBlock = new SmooksResourceConfigFormBlock();
		resourceBlock.setDomain(getEditingDomain());
		resourceBlock.setParentEditor((SmooksFormEditor) this.getEditor());
	}

	protected EditingDomain getEditingDomain() {
		return ((SmooksFormEditor) getEditor()).getEditingDomain();
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit tool = managedForm.getToolkit();
		tool.decorateFormHeading(form.getForm());
		GridLayout gridLayout = UIUtils.createGeneralFormEditorLayout(1);
		resourceBlock.createContent(managedForm);
		Composite rootMainControl = form.getBody();
		form
				.setText(Messages
						.getString("SmooksNormalContentEditFormPage.ConfigurationPageText")); //$NON-NLS-1$
		createSmooksTypeGUI(rootMainControl, tool);
		form.getBody().setLayout(gridLayout);
		form.pack();
		this.initOtherConfigurationsGUI();
		resourceBlock.initViewers(transformType);

		setGUIStates();
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	public void setGUIStates() {
		if (resourceBlock != null) {
			resourceBlock.setSectionStates(!disableGUI);
		}

		if (this.parseTypeSection != null && !parseTypeSection.isDisposed()) {
			parseTypeSection.setEnabled(!disableGUI);
		}
	}

	private ResourceConfigType createTransformType() {
		ResourceConfigType transformType = SmooksFactory.eINSTANCE
				.createResourceConfigType();
		AddCommand.create(
				domain,
				modelPackage.getSmooksResourceList(),
				SmooksPackage.eINSTANCE
						.getSmooksResourceListType_AbstractResourceConfig(),
				transformType).execute();
		MoveCommand.create(domain, modelPackage.getSmooksResourceList(),
				SmooksPackage.eINSTANCE
						.getSmooksResourceListType_AbstractResourceConfig(),
				transformType, 0);
		transformType.setSelector(SmooksModelConstants.GLOBAL_PARAMETERS);
		ParamType typeParam = SmooksFactory.eINSTANCE.createParamType();
		typeParam.setName(SmooksModelConstants.STREAM_FILTER_TYPE);
		transformType.getParam().add(typeParam);

		return transformType;
	}

	protected void initOtherConfigurationsGUI() {
		if (saxButton != null)
			saxButton.setSelection(false);
		if (domButton != null)
			domButton.setSelection(false);
		if (saxdomButton != null)
			saxdomButton.setSelection(false);
		if (this.getModelPackage() != null) {
			List list = modelPackage.getSmooksResourceList()
					.getAbstractResourceConfig();
			if (fileViewer != null) {
				fileViewer.setInput(list);
			}
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object sr = iterator.next();
				if (sr instanceof ResourceConfigType) {
					if (SmooksModelUtils
							.isTransformTypeResourceConfig((ResourceConfigType) sr)) {
						this.transformType = (ResourceConfigType) sr;
						break;
					}
				}
			}
			if (transformType == null) {
				transformType = createTransformType();
			}
			if (transformType != null) {
				String type = SmooksModelUtils.getTransformType(transformType);
				if (SmooksModelConstants.SAX.equals(type)) {
					if (saxButton != null)
						saxButton.setSelection(true);
				}
				if (SmooksModelConstants.DOM.equals(type)) {
					if (domButton != null)
						domButton.setSelection(true);
				}
				if ("SAX/DOM".equals(type)) { //$NON-NLS-1$
					if (saxdomButton != null)
						saxdomButton.setSelection(true);
				}
			}
		}
	}

	protected void createSmooksTypeGUI(Composite mainComposite, FormToolkit tool) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		parseTypeSection = tool.createSection(mainComposite, Section.TITLE_BAR
				| Section.TWISTIE);
		parseTypeSection.setLayoutData(gd);
		Composite otherConfigurationComposite = tool
				.createComposite(parseTypeSection);
		parseTypeSection.setClient(otherConfigurationComposite);
		parseTypeSection.setText(Messages
				.getString("SmooksNormalContentEditFormPage.SmooksParseType")); //$NON-NLS-1$
		GridLayout layout1 = new GridLayout();
		otherConfigurationComposite.setLayout(layout1);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		otherConfigurationComposite.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		otherConfigurationComposite.setLayout(layout);

		Composite buttonComposite = tool
				.createComposite(otherConfigurationComposite);
		GridLayout buttonAreaLayout = new GridLayout();
		buttonAreaLayout.numColumns = 4;
		buttonComposite.setLayout(buttonAreaLayout);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		buttonComposite.setLayoutData(gd);

		tool.createLabel(buttonComposite, "Parse Type : ");
		saxButton = createTypeSelectRadioButton(buttonComposite, tool,
				SmooksModelConstants.SAX);
		domButton = createTypeSelectRadioButton(buttonComposite, tool,
				SmooksModelConstants.DOM);
		saxdomButton = createTypeSelectRadioButton(buttonComposite, tool,
				SmooksModelConstants.SAX_DOM);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
//		tool.createSeparator(otherConfigurationComposite, SWT.HORIZONTAL)
//				.setLayoutData(gd);

		// below is for creating import files GUI.
		Composite importFileComposite = tool
				.createComposite(otherConfigurationComposite);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		GridLayout fileLayout = new GridLayout();
		fileLayout.numColumns = 2;
		importFileComposite.setLayoutData(gd);
		importFileComposite.setLayout(fileLayout);
		// importFileComposite.setBackground(ColorConstants.black);

		Label fileListLabel = tool.createLabel(importFileComposite,
				"Import Files :");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		fileListLabel.setLayoutData(gd);

		Composite viewerComposite = tool.createComposite(importFileComposite);
		FillLayout viewerLayout = new FillLayout();
		viewerLayout.marginHeight = 1;
		viewerLayout.marginWidth = 1;
		viewerComposite.setLayout(viewerLayout);
		fileViewer = new TableViewer(viewerComposite, SWT.NONE);
		fileViewer.setContentProvider(new FileImportContentProvider());
		fileViewer.setLabelProvider(new FileImportLabelProvider());
		if (getModelPackage() != null) {
			SmooksResourceListType listType = getModelPackage()
					.getSmooksResourceList();
			if (listType != null && listType.getAbstractResourceConfig() != null) {
				fileViewer.setInput(listType.getAbstractResourceConfig());
			}
		}
		fileViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				openFile(fileViewer.getSelection());
			}
		});
		fileViewer.addFilter(new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				if (element instanceof ImportType)
					return true;
				return false;
			}

		});
		gd = new GridData(GridData.FILL_BOTH);
		viewerComposite.setLayoutData(gd);
		viewerComposite.setBackground(GraphicsConstants.groupBorderColor);
		tool.paintBordersFor(viewerComposite);

		Composite fileActionComposite = tool
				.createComposite(importFileComposite);
		gd = new GridData(GridData.FILL_VERTICAL);
		fileActionComposite.setLayoutData(gd);

		GridLayout fileActionLayout = new GridLayout();
		fileActionComposite.setLayout(fileActionLayout);

		addFileButton = createFileActionButton(fileActionComposite, tool, "Add");
		removeFileButton = createFileActionButton(fileActionComposite, tool,
				"Remove");
		editFileButton = createFileActionButton(fileActionComposite, tool,
				"Edit");

		tool.paintBordersFor(fileActionComposite);

		hookButtons();
	}

	protected void openFile(ISelection selection) {
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		if (obj != null && obj instanceof ImportType) {
			String filePath = ((ImportType) obj).getFile();
			IFile input = createEditorInput(filePath);
			if (input != null) {
				try {
					IDE.openEditor(SmooksUIActivator.getDefault()
							.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage(), input);
				} catch (PartInitException e) {
					UIUtils.showErrorDialog(getSite().getShell(), UIUtils
							.createErrorStatus(e));
				}
			}
		}
	}

	private IProject getCurrentProject() {
		IFile file = ((IFileEditorInput) getEditor().getEditorInput())
				.getFile();
		if (file != null) {
			IProject project = file.getProject();
			return project;
		}
		return null;
	}

	protected IFile createEditorInput(String filePath) {
		String workspacePath = this.getWorkspaceFilePath(filePath);
		IProject project = getCurrentProject();
		if (project != null) {
			IResource resource = project.findMember(new Path(workspacePath));
			if (resource instanceof IFile) {
				return (IFile) resource;
			}
		}
		return null;
	}

	private Button createFileActionButton(Composite parent, FormToolkit tool,
			String text) {
		Button button = tool.createButton(parent, text, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		button.setLayoutData(gd);
		return button;
	}

	private void setTransformType(String type) {
		if (this.transformType != null) {
			SmooksModelUtils.setTransformType(transformType, type);
			((SmooksFormEditor) getEditor()).fireEditorDirty(true);
		}
	}

	protected void hookButtons() {
		saxButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				setTransformType(SmooksModelConstants.SAX);
			}

		});

		domButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setTransformType(SmooksModelConstants.DOM);
			}
		});

		saxdomButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				setTransformType(SmooksModelConstants.SAX_DOM);
			}

		});

		addFileButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				addFileImport();
			}

		});

		removeFileButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				removeFileImport();
			}

		});

		editFileButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				editFileImport();
			}

		});
	}

	protected void editFileImport() {
		IStructuredSelection selection = (IStructuredSelection) fileViewer
				.getSelection();
		Object obj = selection.getFirstElement();
		if (obj instanceof ImportType) {
			String filePath = getWorkspaceFilePath(((ImportType) obj).getFile());
			IProject project = getCurrentProject();
			if (project != null) {
				IResource resource = project.findMember(new Path(filePath));
				IFile[] files = WorkspaceResourceDialog.openFileSelection(
						getSite().getShell(), "Select Files", "", false,
						new Object[] { resource }, Collections.EMPTY_LIST);
				if (files != null) {
					IPath path1 = files[0].getFullPath().removeFirstSegments(1);
					String s = path1.toString();
					if (s.startsWith("/") || s.startsWith("\\")) {

					} else {
						s = "/" + s;
					}
					((ImportType) obj).setFile("classpath:" + s);
					fileViewer.refresh();
					((SmooksFormEditor) getEditor()).fireEditorDirty(true);
				}
			}

		}
	}

	protected void removeFileImport() {
		IStructuredSelection selection = (IStructuredSelection) fileViewer
				.getSelection();
		NormalSmooksModelPackage modelPackage = getModelPackage();
		if (modelPackage != null) {
			modelPackage.getSmooksResourceList().getAbstractResourceConfig()
					.removeAll(selection.toList());
			fileViewer.refresh();
			((SmooksFormEditor) getEditor()).fireEditorDirty(true);
		}
	}

	protected void addFileImport() {
		IFile[] files = WorkspaceResourceDialog.openFileSelection(getSite()
				.getShell(), "Select Files", "", false, null,
				Collections.EMPTY_LIST);
		if (files != null && files.length > 0) {
			ImportType fileImport = SmooksFactory.eINSTANCE.createImportType();
			IPath path1 = files[0].getFullPath().removeFirstSegments(1);
			String s = path1.toString();
			if (s.startsWith("/") || s.startsWith("\\")) {

			} else {
				s = "/" + s;
			}
			fileImport.setFile("classpath:" + s);
			NormalSmooksModelPackage modelPackage = getModelPackage();
			if (modelPackage != null) {
				Command command = AddCommand
						.create(
								((SmooksFormEditor) getEditor())
										.getEditingDomain(),
								modelPackage.getSmooksResourceList(),
								SmooksPackage.eINSTANCE
										.getSmooksResourceListType_AbstractResourceConfig(),
								fileImport);
				((SmooksFormEditor) getEditor()).getEditingDomain()
						.getCommandStack().execute(command);
				fileViewer.refresh();
				((SmooksFormEditor) getEditor()).fireEditorDirty(true);
			}
		}
	}

	private Button createTypeSelectRadioButton(Composite parent,
			FormToolkit tool, String labelName) {
		Button button = tool.createButton(parent, labelName, SWT.RADIO);
		return button;
	}

	private String getWorkspaceFilePath(String filePath) {
		if (filePath == null)
			return "";
		if (filePath.indexOf(":") != -1) {
			int index = filePath.indexOf(":");
			return filePath.substring(index + 1, filePath.length());
		}
		return filePath;
	}

	/**
	 * @return the modelPackage
	 */
	public NormalSmooksModelPackage getModelPackage() {
		return modelPackage;
	}

	/**
	 * @param modelPackage
	 *            the modelPackage to set
	 */
	public void setModelPackage(NormalSmooksModelPackage modelPackage) {
		if (modelPackage == this.modelPackage)
			return;
		this.modelPackage = modelPackage;
		if (resourceBlock != null)
			this.resourceBlock.setModelPackage(this.modelPackage);
	}

	public void endAnalyze(AnalyzeResult result) {
		if (result.getError() == null) {
			disableGUI = false;
			SmooksFormEditor parentEditor = (SmooksFormEditor) getEditor();
			NormalSmooksModelPackage pa = parentEditor
					.createNewSmooksModelPackage();
			SmooksGraphicalFormPage graphicalEditor = (SmooksGraphicalFormPage) result
					.getSourceEdtior();
			MappingResourceConfigList rclist = graphicalEditor
					.getMappingResourceConfigList();
			// for make sure that the SmooksResourceConfig model was the same :
			if (rclist != null) {
				List<ResourceConfigType> renderList = rclist
						.getGraphRenderResourceConfigList();
				if (renderList != null && renderList.size() > 0) {
					ResourceConfigType resourceConfig = renderList.get(0);
					SmooksResourceListType rootList = (SmooksResourceListType) resourceConfig
							.eContainer();
					if (!(rootList == pa.getSmooksResourceList())) {
						pa.setSmooksResourceList(rootList);
					}
				}
				pa.setHidenSmooksElements(renderList);
			}
			setModelPackage(pa);
		} else {
			setModelPackage(null);
			disableGUI = true;
		}
		initOtherConfigurationsGUI();
		if (resourceBlock != null)
			this.resourceBlock.initViewers(transformType);
		setGUIStates();
	}

	private class FileImportContentProvider implements
			IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			}
			return new Object[] {};
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class FileImportLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			if (element instanceof ImportType) {
				String path = getWorkspaceFilePath(((ImportType) element)
						.getFile());
				IProject project = getCurrentProject();
				IResource resource = project.findMember(new Path(path));
				if (resource == null || !resource.exists())
					return SmooksUIActivator.getDefault().getImageRegistry()
							.get(SmooksGraphConstants.IMAGE_ERROR);
			}
			return super.getImage(element);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof ImportType) {
				return getWorkspaceFilePath(((ImportType) element).getFile());
			}
			return super.getText(element);
		}

	}
}
