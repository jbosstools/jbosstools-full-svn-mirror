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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandWrapper;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.PrintAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
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
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.ModelPanelCreator;
import org.jboss.tools.smooks.configuration.editors.uitls.ProjectClassLoader;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.TagList;
import org.jboss.tools.smooks.configuration.editors.xml.TagObject;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataContentProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataLabelProvider;
import org.jboss.tools.smooks.configuration.validate.ISmooksModelValidateListener;
import org.jboss.tools.smooks.edimap.actions.AddComponentAction;
import org.jboss.tools.smooks.edimap.actions.AddFieldAction;
import org.jboss.tools.smooks.edimap.actions.AddSegmentAction;
import org.jboss.tools.smooks.edimap.actions.AddSubComponentAction;
import org.jboss.tools.smooks.edimap.models.EDIDataContainerGraphModel;
import org.jboss.tools.smooks.edimap.models.EDIMappingNodeContainerGraphModel;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.common.RootModel;
import org.jboss.tools.smooks.gef.tree.editparts.RootEditPart;
import org.jboss.tools.smooks.gef.tree.figures.IMoveableModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.model.graphics.ext.FigureType;
import org.jboss.tools.smooks.model.graphics.ext.GraphType;
import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.ParamType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.medi.Delimiters;
import org.jboss.tools.smooks.model.medi.Description;
import org.jboss.tools.smooks.model.medi.DocumentRoot;
import org.jboss.tools.smooks.model.medi.Segment;
import org.jboss.tools.smooks.model.medi.Segments;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;
import org.milyn.Smooks;
import org.milyn.SmooksUtil;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.smooks.edi.SmooksEDIReader;
import org.milyn.xml.XmlUtil;
import org.w3c.dom.Document;

/**
 * @author Dart
 * 
 */
public class EDIMapFormPage extends FormPage implements ISmooksModelValidateListener, ISmooksGraphChangeListener,
		ISelectionChangedListener, ModifyListener {

	private ISmooksModelProvider modelProvider;

	private Delimiters delimiters = null;

	private Description description = null;

	private AdapterFactoryEditingDomain editingDomain;

	private ModelPanelCreator delimitersCreator;

	private ModelPanelCreator descriptionCreator;

	private ModelPanelCreator segmentsCreator;

	private Composite mappingModelPropertiesComposite;

	private String ediFilePath = null;

	private GraphicalViewer graphicalViewer;

	private Segments segments = null;

	private RootModel graphicalRootModel;

	private ActionRegistry actionRegistry;

	private DefaultEditDomain editDomain = null;

	private Composite descriptorComposite;

	private Composite delimitersComposite;

	public EDIMapFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		if (editor instanceof ISmooksModelProvider) {
			modelProvider = (ISmooksModelProvider) editor;
			modelProvider.getSmooksGraphicsExt().addSmooksGraphChangeListener(this);
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
		if (segmentsCreator != null) {
			IStructuredSelection selection = (IStructuredSelection) getGraphicalViewer().getSelection();
			if (selection.size() > 1)
				return;
			Object model = selection.getFirstElement();
			if (model instanceof EditPart) {
				model = ((EditPart) model).getModel();
			}
			if (model != null) {
				if (model instanceof TreeNodeModel) {
					model = ((TreeNodeModel) model).getData();
					if (model instanceof EObject) {
						segmentsCreator.markPropertyUI(modelProvider.getDiagnosticList(), (EObject) model);
					}
				}
			}
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

	protected void createGraphicalViewer(Composite parent) {
		GraphicalViewer viewer = new ScrollingGraphicalViewer();
		viewer.createControl(parent);

		setGraphicalViewer(viewer);

		configureGraphicalViewer();
		hookGraphicalViewer();
		initializeGraphicalViewer();
	}

	protected void initGraphicalModel() {
		if (graphicalRootModel == null) {
			graphicalRootModel = new RootModel();
		} else {
			graphicalRootModel.removeAllTreeNode();
		}
		DocumentRoot root = (DocumentRoot) modelProvider.getSmooksModel();

		Segments segments = root.getEdimap().getSegments();
		EDIMappingNodeContainerGraphModel container = new EDIMappingNodeContainerGraphModel(segments,
				new AdapterFactoryContentProvider(editingDomain.getAdapterFactory()),
				new EDIGraphicalTreeLabelProvider(editingDomain.getAdapterFactory()), modelProvider);
		container.setHeaderVisable(true);

		graphicalRootModel.addTreeNode(container);

		this.ediFilePath = getEDIFilePath();

		createEDIDataGraphModel();

		List<TreeNodeModel> linkedSourceModel = createLinkModel();

		SmooksGraphicsExtType exttype = this.modelProvider.getSmooksGraphicsExt();
		initModelGraphicsInformation(exttype);
		getGraphicalViewer().setContents(graphicalRootModel);

		expandSegmentNode(linkedSourceModel);

	}

	private void expandSegmentNode(List<TreeNodeModel> linkedSourceModel) {
		EditPart editPart = getGraphicalViewer().getContents();
		EDISegementsEditPart segmentsPart = null;
		if (editPart instanceof RootEditPart) {
			List<?> children = ((RootEditPart) editPart).getChildren();
			for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				if (object instanceof EDISegementsEditPart) {
					segmentsPart = (EDISegementsEditPart) object;
					break;
				}
			}
		}
		if (segmentsPart != null) {
			SmooksUIUtils.expandGraphTree(linkedSourceModel, segmentsPart);
		}
	}

	protected void initializeGraphicalViewer() {
		initGraphicalModel();
	}

	protected void initModelGraphicsInformation(SmooksGraphicsExtType ext) {
		GraphType graph = ext.getGraph();
		if (graph == null)
			return;

		List<TreeNodeModel> list = graphicalRootModel.getChildren();
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			TreeNodeModel treeNodeModel = (TreeNodeModel) iterator.next();
			if (treeNodeModel instanceof IMoveableModel) {
				Object data = treeNodeModel.getData();
				String id = EDISegementsEditPart.generateFigureId(data);
				if (id != null) {
					FigureType ft = SmooksModelUtils.findFigureType(graph, id);
					try {
						int x = Integer.parseInt(ft.getX());
						int y = Integer.parseInt(ft.getY());
						((IMoveableModel) treeNodeModel).setLocation(new Point(x, y));
					} catch (Throwable t) {
						continue;
					}
				}
			}
		}
	}

	protected void createGEFActions() {
		ActionRegistry registry = getActionRegistry();
		IAction action;

		action = new UndoAction(this);
		registry.registerAction(action);

		action = new RedoAction(this);
		registry.registerAction(action);

		action = new SelectAllAction(this);
		registry.registerAction(action);

		action = new DeleteAction((IWorkbenchPart) this);
		registry.registerAction(action);

		action = new SaveAction(this);
		registry.registerAction(action);

		action = new AddSegmentAction(this, modelProvider);
		registry.registerAction(action);

		action = new AddFieldAction(this, modelProvider);
		registry.registerAction(action);

		action = new AddComponentAction(this, modelProvider);
		registry.registerAction(action);

		action = new AddSubComponentAction(this, modelProvider);
		registry.registerAction(action);

		registry.registerAction(new PrintAction(this));
		Iterator<?> it = registry.getActions();
		while (it.hasNext()) {
			IAction a = (IAction) it.next();
			if (a instanceof SelectionAction) {
				((SelectionAction) a).setSelectionProvider(getGraphicalViewer());
			}
		}
	}

	protected void configureGraphicalViewer() {
		createGEFActions();
		getGraphicalViewer().getControl().setBackground(ColorConstants.button);
		getGraphicalViewer().setRootEditPart(new FreeformGraphicalRootEditPart());
		getGraphicalViewer().setEditPartFactory(new EDIEditPartFactory());

		GraphicalViewerKeyHandler keyHandler = new GraphicalViewerKeyHandler(getGraphicalViewer());
		keyHandler.put(org.eclipse.gef.KeyStroke.getPressed(SWT.DEL, 0), this.getActionRegistry().getAction(
				ActionFactory.DELETE.getId()));

		EDIMappingMenuContextProvider provider = new EDIMappingMenuContextProvider(getGraphicalViewer(), this
				.getActionRegistry());
		getGraphicalViewer().setContextMenu(provider);
	}

	public void rebuildGUI() {
		initEDIModels();
		disposeCompositeControls(descriptorComposite, null);
		FormToolkit toolkit = getManagedForm().getToolkit();
		if (descriptionCreator != null) {
			IItemPropertySource itemPropertySource = (IItemPropertySource) editingDomain.getAdapterFactory().adapt(
					description, IItemPropertySource.class);
			descriptionCreator.createModelPanel(description, toolkit, descriptorComposite, itemPropertySource,
					modelProvider, getEditor());
		}
		descriptorComposite.getParent().layout();

		disposeCompositeControls(delimitersComposite, null);

		AttributeFieldEditPart pathEditPart = SmooksUIUtils.createFileSelectionTextFieldEditor("EDI Mapping File",
				delimitersComposite, editingDomain, toolkit, null, null, SmooksUIUtils.VALUE_TYPE_TEXT, null, null);
		final Text ediFileText = (Text) pathEditPart.getContentControl();
		String filePath = getEDIFilePath();
		if (filePath != null) {
			ediFileText.setText(filePath);
		}
		ediFileText.addModifyListener(this);

		if (delimitersCreator != null) {
			IItemPropertySource itemPropertySource = (IItemPropertySource) editingDomain.getAdapterFactory().adapt(
					delimiters, IItemPropertySource.class);
			Map<Object, Object> fieldEditorMap = delimitersCreator.createModelPanel(delimiters, toolkit,
					delimitersComposite, itemPropertySource, modelProvider, getEditor());
			handleDelimiterFieldEditor(fieldEditorMap);
		}
		delimitersComposite.getParent().layout();
		initGraphicalModel();
	}

	private void handleDelimiterFieldEditor(Map<Object, Object> fieldEditorMap) {
		// Iterator<Object> keyIterator = fieldEditorMap.keySet().iterator();
		// while (keyIterator.hasNext()) {
		// Object key = keyIterator.next();
		// AttributeFieldEditPart editPart = (AttributeFieldEditPart)
		// fieldEditorMap.get(key);
		// Control control = editPart.getContentControl();
		// if (control instanceof Text) {
		// ((Text) control).addModifyListener(new DelimiterModifyListener());
		//
		// }
		// }
	}

	private void removeEDIGraphModel() {
		List<TreeNodeModel> children = this.graphicalRootModel.getChildren();
		List<TreeNodeModel> temp = new ArrayList<TreeNodeModel>(children);
		for (Iterator<?> iterator = temp.iterator(); iterator.hasNext();) {
			TreeNodeModel treeNodeModel = (TreeNodeModel) iterator.next();
			if (treeNodeModel.getData() instanceof TagObject) {
				graphicalRootModel.removeTreeNode(treeNodeModel);
			}
		}
		temp.clear();
		temp = null;
	}

	private String getEDIFilePath() {
		if (modelProvider != null) {
			SmooksGraphicsExtType ext = modelProvider.getSmooksGraphicsExt();
			List<InputType> inputList = ext.getInput();
			for (Iterator<?> iterator = inputList.iterator(); iterator.hasNext();) {
				InputType inputType = (InputType) iterator.next();
				if (SmooksModelUtils.INPUT_TYPE_EDI.equals(inputType.getType())) {
					List<ParamType> paramList = inputType.getParam();
					for (Iterator<?> iterator2 = paramList.iterator(); iterator2.hasNext();) {
						ParamType paramType = (ParamType) iterator2.next();
						if (SmooksModelUtils.PARAM_NAME_PATH.equals(paramType.getName())) {
							return paramType.getValue();
						}
					}
				}
			}
		}
		return null;
	}

	private ActionRegistry getActionRegistry() {
		if (actionRegistry == null)
			actionRegistry = new ActionRegistry();
		return actionRegistry;
	}

	protected void updateSelectionActions() {
		for (Iterator<?> iterator = getActionRegistry().getActions(); iterator.hasNext();) {
			Object type = (Object) iterator.next();
			if (type instanceof UpdateAction) {
				((UpdateAction) type).update();
			}
		}
	}

	protected void hookGraphicalViewer() {
		getGraphicalViewer().addSelectionChangedListener(this);
	}

	/**
	 * @return the graphicalViewer
	 */
	public GraphicalViewer getGraphicalViewer() {
		return graphicalViewer;
	}

	/**
	 * @param graphicalViewer
	 *            the graphicalViewer to set
	 */
	public void setGraphicalViewer(GraphicalViewer graphicalViewer) {
		this.graphicalViewer = graphicalViewer;
		this.graphicalViewer.setEditDomain(editDomain);
		editDomain.addViewer(this.graphicalViewer);
	}

	public Object getAdapter(Class type) {
		if (type == CommandStack.class) {
			if (editDomain != null) {
				return editDomain.getCommandStack();
			}
		}
		return super.getAdapter(type);
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
		editDomain = new DefaultEditDomain(this);
		editDomain.getCommandStack().addCommandStackListener(new CommandStackListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.gef.commands.CommandStackListener#commandStackChanged
			 * (java.util.EventObject)
			 */
			public void commandStackChanged(EventObject event) {
				getEditor().editorDirtyStateChanged();
			}

		});
		if (this.modelProvider != null) {
			this.initEDIModels();
			IItemPropertySource propertySource1 = (IItemPropertySource) editingDomain.getAdapterFactory().adapt(
					delimiters, IItemPropertySource.class);

			delimitersCreator = new ModelPanelCreator(delimiters, propertySource1);

			IItemPropertySource propertySource2 = (IItemPropertySource) editingDomain.getAdapterFactory().adapt(
					description, IItemPropertySource.class);

			descriptionCreator = new ModelPanelCreator(description, propertySource2);

			handleCommandStack(editingDomain.getCommandStack());
		}
	}

	protected void initEDIModels() {
		DocumentRoot root = (DocumentRoot) modelProvider.getSmooksModel();

		this.delimiters = root.getEdimap().getDelimiters();

		this.description = root.getEdimap().getDescription();

		this.segments = root.getEdimap().getSegments();
		if(segments.getXmltag() == null){
			segments.setXmltag("root");
		}

		editingDomain = (AdapterFactoryEditingDomain) modelProvider.getEditingDomain();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormPage#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		if (modelProvider != null) {
			try {
				modelProvider.getSmooksGraphicsExt().eResource().save(Collections.emptyMap());
				editDomain.getCommandStack().flush();
				firePropertyChange(PROP_DIRTY);
			} catch (Throwable t) {

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormPage#isDirty()
	 */
	@Override
	public boolean isDirty() {
		if (editDomain != null) {
			return editDomain.getCommandStack().isDirty();
		}
		return super.isDirty();
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
		gridLayout.makeColumnsEqualWidth = true;
		form.getBody().setLayout(gridLayout);

		Section descriptionSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = GridData.BEGINNING;
		descriptionSection.setLayoutData(gd);
		descriptionSection.setText("Mapping Description");
		FillLayout flayout = new FillLayout();
		descriptionSection.setLayout(flayout);

		Composite desciptorContainer = toolkit.createComposite(descriptionSection);
		GridLayout dgl = new GridLayout();
		dgl.marginHeight = 0;
		dgl.marginWidth = 0;
		desciptorContainer.setLayout(dgl);
		descriptionSection.setClient(desciptorContainer);

		descriptorComposite = toolkit.createComposite(desciptorContainer, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		descriptorComposite.setLayout(gl);
		gd = new GridData(GridData.FILL_BOTH);
		descriptorComposite.setLayoutData(gd);

		if (descriptionCreator != null) {
			descriptionCreator.createModelPanel(toolkit, descriptorComposite, modelProvider, getEditor());
		}

		Hyperlink showTransformResultLink = toolkit.createHyperlink(desciptorContainer, "Test EDI transform", SWT.NONE);
		showTransformResultLink.setVisible(false);
		showTransformResultLink.addHyperlinkListener(new IHyperlinkListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.ui.forms.events.IHyperlinkListener#linkActivated(
			 * org.eclipse.ui.forms.events.HyperlinkEvent)
			 */
			public void linkActivated(HyperlinkEvent e) {
				testEDITransform();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.ui.forms.events.IHyperlinkListener#linkEntered(org
			 * .eclipse.ui.forms.events.HyperlinkEvent)
			 */
			public void linkEntered(HyperlinkEvent e) {

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.ui.forms.events.IHyperlinkListener#linkExited(org
			 * .eclipse.ui.forms.events.HyperlinkEvent)
			 */
			public void linkExited(HyperlinkEvent e) {

			}

		});
		gd = new GridData();
		showTransformResultLink.setLayoutData(gd);

		Section delimiterSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = GridData.BEGINNING;
		gd.verticalSpan = 2;
		delimiterSection.setLayoutData(gd);
		delimiterSection.setText("EDI File Configurations");
		FillLayout flayout1 = new FillLayout();
		delimiterSection.setLayout(flayout1);

		delimitersComposite = toolkit.createComposite(delimiterSection, SWT.NONE);
		GridLayout gl1 = new GridLayout();
		gl1.numColumns = 2;
		delimitersComposite.setLayout(gl1);
		delimiterSection.setClient(delimitersComposite);

		AttributeFieldEditPart pathEditPart = SmooksUIUtils.createFileSelectionTextFieldEditor("EDI Mapping File",
				delimitersComposite, editingDomain, toolkit, null, null, SmooksUIUtils.VALUE_TYPE_TEXT, null, null);
		final Text ediFileText = (Text) pathEditPart.getContentControl();
		String filePath = getEDIFilePath();
		if (filePath != null) {
			ediFileText.setText(filePath);
		}
		ediFileText.addModifyListener(this);

		if (delimitersCreator != null) {
			Map<Object, Object> editPartMap = delimitersCreator.createModelPanel(toolkit, delimitersComposite,
					modelProvider, getEditor());
			handleDelimiterFieldEditor(editPartMap);
		}

		// Section testSection = toolkit.createSection(form.getBody(),
		// Section.TITLE_BAR | Section.TWISTIE | Section.DESCRIPTION);
		// gd = new GridData(GridData.FILL_HORIZONTAL);
		// gd.verticalAlignment = GridData.BEGINNING;
		// // gd.horizontalSpan = 2;
		// testSection.setLayoutData(gd);
		// testSection.setText("Mapping Descriptor");
		// FillLayout flayout2 = new FillLayout();
		// testSection.setLayout(flayout2);

		Section ediModelViewerSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		ediModelViewerSection.setLayoutData(gd);
		ediModelViewerSection.setText("Graphical Mapping");
		FillLayout flayout3 = new FillLayout();
		ediModelViewerSection.setLayout(flayout3);

		Composite viewerComposite = toolkit.createComposite(ediModelViewerSection);
		ediModelViewerSection.setClient(viewerComposite);
		GridLayout vgl = new GridLayout();
		vgl.numColumns = 2;

		viewerComposite.setLayout(vgl);

		SashForm sashForm = new SashForm(viewerComposite, SWT.NONE);

		gd = new GridData(GridData.FILL_BOTH);
		sashForm.setLayoutData(gd);

		Composite tagComposite = toolkit.createComposite(sashForm);
		tagComposite.setBackground(new Color(null, 128, 128, 128));
		FillLayout tagLayout = new FillLayout();
		tagLayout.marginHeight = 1;
		tagLayout.marginWidth = 1;
		tagComposite.setLayout(tagLayout);

		createGraphicalViewer(tagComposite);

		Composite mc = toolkit.createComposite(sashForm);
		GridLayout mcgl = new GridLayout();
		mc.setLayout(mcgl);

		toolkit.createLabel(mc, "Properties:");
		Composite cs = toolkit.createCompositeSeparator(mc);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 1;
		cs.setLayoutData(gd);

		mappingModelPropertiesComposite = toolkit.createComposite(mc);
		gd = new GridData(GridData.FILL_BOTH);
		// gd.widthHint = 180
		mappingModelPropertiesComposite.setLayoutData(gd);
		GridLayout mgl = new GridLayout();
		mgl.numColumns = 2;
		mappingModelPropertiesComposite.setLayout(mgl);

		sashForm.setWeights(new int[] { 10, 3 });

	}

	private void handleCommandStack(org.eclipse.emf.common.command.CommandStack commandStack) {
		commandStack.addCommandStackListener(new org.eclipse.emf.common.command.CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				final Command mostRecentCommand = ((org.eclipse.emf.common.command.CommandStack) event.getSource())
						.getMostRecentCommand();
				getEditorSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (mostRecentCommand != null) {
							Command rawCommand = mostRecentCommand;
							while (rawCommand instanceof CommandWrapper) {
								rawCommand = ((CommandWrapper) rawCommand).getCommand();
							}
							if (rawCommand instanceof SetCommand || rawCommand instanceof AddCommand
									|| rawCommand instanceof DeleteCommand) {
								refershRecentAffectedModel(rawCommand, mostRecentCommand.getAffectedObjects());
							}
						}
					}
				});
			}
		});
	}

	private void refershRecentAffectedModel(Command command, Collection<?> models) {
		for (Iterator<?> iterator = models.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object == this.delimiters) {
				rebuildEDIGraph();
				continue;
			}
			TreeNodeModel node = findEDIGraphicalModel(object);
			if (node == null)
				continue;
			if (command instanceof SetCommand) {
				node.fireVisualChanged();
			}
			if (command instanceof AddCommand || command instanceof DeleteCommand) {
				node.fireChildrenChanged();
				node.fireConnectionChanged();
			}
		}
	}

	private void createEDIDataGraphModel() {
		if (ediFilePath != null) {
			try {
				if (!new File(SmooksUIUtils.parseFilePath(ediFilePath)).exists()) {
					return;
				}
			} catch (Throwable t) {
				return;
			}
		}
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
				List<?> children = tl.getChildren();
				for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
					Object object = (Object) iterator.next();
					EDIDataContainerGraphModel container = new EDIDataContainerGraphModel(object,
							new XMLStructuredDataContentProvider(), new XMLStructuredDataLabelProvider());
					container.setHeaderVisable(true);
					graphicalRootModel.addTreeNode(container);
				}
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void disconnectSegement(List<Segment> segmentList) {
		for (Iterator<?> iterator = segmentList.iterator(); iterator.hasNext();) {
			Segment segment = (Segment) iterator.next();
			TreeNodeModel segmentGraphicalModel = findEDIGraphicalModel(segment);
			if (segmentGraphicalModel == null)
				continue;
			List<TreeNodeConnection> sourceConnections = segmentGraphicalModel.getSourceConnections();
			List<TreeNodeConnection> targetConnections = segmentGraphicalModel.getTargetConnections();
			List<TreeNodeConnection> sourceConnectionsTemp = new ArrayList<TreeNodeConnection>(sourceConnections);
			List<TreeNodeConnection> targetConnectionsTemp = new ArrayList<TreeNodeConnection>(targetConnections);
			for (Iterator<?> iterator2 = targetConnectionsTemp.iterator(); iterator2.hasNext();) {
				TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator2.next();
				// don't earse the segcode
				treeNodeConnection.getSourceNode().getSourceConnections().remove(treeNodeConnection);
				treeNodeConnection.getSourceNode().fireConnectionChanged();
				treeNodeConnection.getTargetNode().getTargetConnections().remove(treeNodeConnection);
				treeNodeConnection.getTargetNode().fireConnectionChanged();
				// treeNodeConnection.disconnect();
			}
			targetConnectionsTemp.clear();
			targetConnectionsTemp = null;

			for (Iterator<?> iterator2 = sourceConnectionsTemp.iterator(); iterator2.hasNext();) {
				TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator2.next();
				treeNodeConnection.getSourceNode().getSourceConnections().remove(treeNodeConnection);
				treeNodeConnection.getSourceNode().fireConnectionChanged();
				treeNodeConnection.getTargetNode().getTargetConnections().remove(treeNodeConnection);
				treeNodeConnection.getTargetNode().fireConnectionChanged();
			}
			sourceConnectionsTemp.clear();
			sourceConnectionsTemp = null;

			disconnectSegement(segment.getSegment());
		}
	}

	private void disconnectSegements() {
		if (graphicalRootModel != null && segments != null) {
			List<Segment> segmentList = segments.getSegment();
			disconnectSegement(segmentList);
		}
	}

	private List<TreeNodeModel> createLinkModel() {
		if (graphicalRootModel != null && segments != null) {
			List<Segment> segmentList = segments.getSegment();
			return linkNode(segmentList);
		}
		return Collections.emptyList();
	}

	private List<TreeNodeModel> linkNode(List<Segment> segmentList) {
		List<TreeNodeModel> sourceLinkedModel = new ArrayList<TreeNodeModel>();
		for (Iterator<?> iterator = segmentList.iterator(); iterator.hasNext();) {
			Segment segment = (Segment) iterator.next();
			TreeNodeModel segmentGraphicalModel = findEDIGraphicalModel(segment);
			String code = segment.getSegcode();
			TreeNodeModel model = findEDIDataGraphicalModel(code);
			if (segmentGraphicalModel != null && model != null) {
				new TreeNodeConnection(segmentGraphicalModel, model).connect();
			}
			sourceLinkedModel.add(segmentGraphicalModel);
			List<TreeNodeModel> linkedModel = linkNode(segment.getSegment());
			if (linkedModel != null) {
				sourceLinkedModel.addAll(linkedModel);
			}
		}
		return sourceLinkedModel;
	}

	// private TreeNodeModel findEDIGraphModel(Object model){
	// if (graphicalRootModel != null) {
	// List<TreeNodeModel> treeNodeList = graphicalRootModel.getChildren();
	// return findEDIGraphicalModel(model, treeNodeList);
	// }
	// return null;
	// }
	//
	// private TreeNodeModel findEDIGraphicalModel(Object model,
	// List<TreeNodeModel> treeNodeList) {
	// for (Iterator<?> iterator = treeNodeList.iterator(); iterator.hasNext();)
	// {
	// TreeNodeModel treeNodeModel = (TreeNodeModel) iterator.next();
	// Object model = treeNodeModel.getData();
	// if (model == segment) {
	// return treeNodeModel;
	// }
	// TreeNodeModel m = findSegmentGraphicalModel(segment,
	// treeNodeModel.getChildren());
	// if (m != null) {
	// return m;
	// }
	// }
	// return null;
	// }

	/**
	 * it's dangerous , the method is dangerous
	 * 
	 * @param model
	 * @return
	 */
	private TreeNodeModel findEDIGraphicalModel(Object model) {
		if (graphicalRootModel != null) {
			List<TreeNodeModel> treeNodeList = graphicalRootModel.getChildren();
			return findEDIGraphicalModel(model, treeNodeList);
		}
		return null;
	}

	private TreeNodeModel findEDIGraphicalModel(Object model, List<TreeNodeModel> treeNodeList) {
		for (Iterator<?> iterator = treeNodeList.iterator(); iterator.hasNext();) {
			TreeNodeModel treeNodeModel = (TreeNodeModel) iterator.next();
			Object data = treeNodeModel.getData();
			if (data == model) {
				return treeNodeModel;
			}
			TreeNodeModel m = findEDIGraphicalModel(model, treeNodeModel.getChildren());
			if (m != null) {
				return m;
			}
		}
		return null;
	}

	private TreeNodeModel findEDIDataGraphicalModel(String code) {
		if (code == null)
			return null;
		if (graphicalRootModel != null) {
			List<TreeNodeModel> treeNodeList = graphicalRootModel.getChildren();
			for (Iterator<?> iterator = treeNodeList.iterator(); iterator.hasNext();) {
				TreeNodeModel treeNodeModel = (TreeNodeModel) iterator.next();
				Object model = treeNodeModel.getData();
				if (model instanceof TagObject) {
					String name = ((TagObject) model).getName();
					if (code.equals(name)) {
						return treeNodeModel;
					}
				}
			}
		}
		return null;
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

	protected void disposeCompositeControls(Composite composite, Control[] ignoreControl) {
		if (composite != null) {
			Control[] children = composite.getChildren();
			for (int i = 0; i < children.length; i++) {
				Control child = children[i];
				if (ignoreControl != null) {
					for (int j = 0; j < ignoreControl.length; j++) {
						if (child == ignoreControl[j]) {
							continue;
						}
					}
				}
				child.dispose();
				child = null;
			}
		}
	}

	private void disposeMappingModelPropertiesCompoisiteControls() {
		disposeCompositeControls(mappingModelPropertiesComposite, null);
	}

	public void selectionChanged(SelectionChangedEvent event) {
		if (event.getSource() == getGraphicalViewer()) {
			initEDIModels();
			disposeMappingModelPropertiesCompoisiteControls();
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			if (selection.size() > 1)
				return;
			Object model = selection.getFirstElement();
			if (model instanceof EditPart) {
				model = ((EditPart) model).getModel();
			}
			if (model != null) {
				if (model instanceof TreeNodeModel) {
					model = ((TreeNodeModel) model).getData();
				}
				if ((model instanceof EObject)) {
					IItemPropertySource itemPropertySource = (IItemPropertySource) editingDomain.getAdapterFactory()
							.adapt(model, IItemPropertySource.class);
					segmentsCreator.setModel((EObject) model);
					segmentsCreator.setPropertySource(itemPropertySource);
					segmentsCreator.createModelPanel((EObject) model, this.getManagedForm().getToolkit(),
							mappingModelPropertiesComposite, itemPropertySource, modelProvider, getEditor());
					mappingModelPropertiesComposite.getParent().layout();
				}
			}
			updateSelectionActions();
		}
	}

	protected void testEDITransform() {
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			if (this.getEditor().isDirty()) {
				MessageDialog.openInformation(getEditorSite().getShell(), "Info",
						"The configuration file should be saved");
				return;
			}
			IFileEditorInput input = (IFileEditorInput) getEditorInput();
			IFile file = input.getFile();
			String path = file.getLocation().toOSString();
			File ediFile = new File(SmooksUIUtils.parseFilePath(ediFilePath));

			if (ediFile == null || !ediFile.exists()) {
				MessageDialog.openError(getEditorSite().getShell(), "Transform Error", "Can't the find the EDI file : "
						+ ediFilePath);
				return;
			}

			if (file == null || !file.exists()) {
				MessageDialog.openError(getEditorSite().getShell(), "Transform Error",
						"Can't find the EDI mapping configuration file : " + path);
				return;
			}

			IPath path1 = file.getFullPath().removeFirstSegments(1);

			ProjectClassLoader classLoader = new ProjectClassLoader(JavaCore.create(file.getProject()));

			Thread.currentThread().setContextClassLoader(classLoader);
			Smooks smooks = new Smooks();

			SmooksResourceConfiguration readerConfig = new SmooksResourceConfiguration("org.xml.sax.driver",
					SmooksEDIReader.class.getName());

			readerConfig.setParameter("mapping-model", path1.toString());
			readerConfig.setParameter("encoding", "UTF-8");

			SmooksUtil.registerResource(readerConfig, smooks);

			// Use a DOM result to capture the message model for the supplied
			// CSV
			// message...
			DOMResult domResult = new DOMResult();

			// Filter the message through Smooks and capture the result as a DOM
			// in
			// the domResult instance...
			smooks.filter(new StreamSource(new FileInputStream(ediFile)), domResult);

			// Get the Document object from the domResult. This is the message
			// model!!!...
			Document model = (Document) domResult.getNode();

			// So using the model Document, you can construct a tree structure
			// for
			// the editor.

			// We'll just print out the model DOM here so you can see it....
			StringWriter modelWriter = new StringWriter();
			XmlUtil.serialize(model, true, modelWriter);
			System.out.println(modelWriter);
		} catch (Throwable t) {
			SmooksUIUtils.showErrorDialog(getEditorSite().getShell(), SmooksUIUtils.createErrorStatus(t));
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void modifyText(ModifyEvent e) {
		Text ediFileText = (Text) e.getSource();
		ediFilePath = ediFileText.getText();
		InputType inputType1 = null;
		SmooksGraphicsExtType ext = modelProvider.getSmooksGraphicsExt();
		List<InputType> list = ext.getInput();
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			InputType inputType = (InputType) iterator.next();
			if (SmooksModelUtils.INPUT_TYPE_EDI.equals(inputType.getType())) {
				inputType1 = inputType;
				break;
			}
		}
		SmooksUIUtils.recordInputDataInfomation(inputType1, modelProvider.getSmooksGraphicsExt(),
				SmooksModelUtils.INPUT_TYPE_EDI, ediFilePath, null);
		rebuildEDIGraph();
	}

	private void rebuildEDIGraph() {
		SmooksGraphicsExtType ext = modelProvider.getSmooksGraphicsExt();
		disconnectSegements();
		removeEDIGraphModel();
		createEDIDataGraphModel();
		initModelGraphicsInformation(ext);
		List<TreeNodeModel> sourceLinkedModel = this.createLinkModel();
		expandSegmentNode(sourceLinkedModel);
	}

	private class DelimiterModifyListener implements ModifyListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.
		 * events.ModifyEvent)
		 */
		public void modifyText(ModifyEvent e) {
			rebuildEDIGraph();

		}

	}
}
