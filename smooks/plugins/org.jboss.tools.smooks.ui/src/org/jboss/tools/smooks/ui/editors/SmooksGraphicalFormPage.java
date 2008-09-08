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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.IOWrappedException;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.PrintAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.smooks.analyzer.AnalyzerFactory;
import org.jboss.tools.smooks.analyzer.IMappingAnalyzer;
import org.jboss.tools.smooks.analyzer.ISourceModelAnalyzer;
import org.jboss.tools.smooks.analyzer.ITargetModelAnalyzer;
import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.SmooksAnalyzerException;
import org.jboss.tools.smooks.analyzer.SmooksFileBuilder;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.GraphicalPackage;
import org.jboss.tools.smooks.graphical.MappingDataType;
import org.jboss.tools.smooks.graphical.util.GraphicalInformationSaver;
import org.jboss.tools.smooks.ui.IStrucutredDataCreationWizard;
import org.jboss.tools.smooks.ui.IViewerInitor;
import org.jboss.tools.smooks.ui.StructuredDataCreationWizardDailog;
import org.jboss.tools.smooks.ui.gef.editparts.SmooksEditPartFactory;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.RootModel;
import org.jboss.tools.smooks.ui.gef.model.SourceModel;
import org.jboss.tools.smooks.ui.gef.model.TargetModel;
import org.jboss.tools.smooks.ui.gef.model.TreeItemRelationModel;
import org.jboss.tools.smooks.ui.gef.tools.MappingPanelDropTargetListener;
import org.jboss.tools.smooks.ui.gef.tools.TargetTreeDropTargetListener;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.ui.wizards.TransformDataSelectionWizard;
import org.jboss.tools.smooks.utils.UIUtils;
import org.milyn.xsd.smooks.DocumentRoot;
import org.milyn.xsd.smooks.SmooksResourceListType;
import org.milyn.xsd.smooks.util.SmooksConstants;
import org.milyn.xsd.smooks.util.SmooksResourceFactoryImpl;

/**
 * @author Dart Peng
 * @Date Jul 28, 2008
 */
public class SmooksGraphicalFormPage extends FormPage implements
		ISelectionChangedListener, ISelectionProvider {
	private static final String REFERENCE_MODEL = "__reference_model";
	protected SmooksConfigurationFileGenerateContext smooksConfigurationFileGenerateContext;
	protected IViewerInitor sourceViewerInitor;
	protected IViewerInitor targetViewerInitor;
	protected TreeViewer targetViewer;
	protected TreeViewer sourceViewer;
	protected SelectionSynchronizer selectionSynchronizer = null;
	protected GraphicalViewer graphicalViewer;
	protected DefaultEditDomain editDomain;
	protected GraphRootModel rootModel;
	protected Hyperlink sourceLink = null;
	protected Hyperlink targetLink = null;
	protected String sourceDataTypeID = null;
	protected String targetDataTypeID = null;
	protected SmooksFileBuilder smooksFileBuilder = null;
	protected GraphicalInformationSaver graphicalInformationSaver = null;

	protected Object sourceTreeViewerInputModel = null;
	protected Object targetTreeViewerInputModel = null;
	protected List<MappingModel> initConnectionList = null;
	protected boolean commandStackChanged = false;
	protected ActionRegistry actionRegistry;

	private List selectionChangeListener = new ArrayList();
	private ISelection selection;

	public ISelection getSelection() {
		return selection;
	}

	public void setSelection(ISelection selection) {
		if (this.selection == selection)
			return;
		this.selection = selection;
		SelectionChangedEvent event = new SelectionChangedEvent(this, selection);
		for (Iterator iterator = selectionChangeListener.iterator(); iterator
				.hasNext();) {
			ISelectionChangedListener listener = (ISelectionChangedListener) iterator
					.next();
			listener.selectionChanged(event);
		}
	}

	public SmooksGraphicalFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		editDomain = new DefaultEditDomain(this);
		smooksFileBuilder = this.createSmooksFileBulder();
	}

	public SmooksGraphicalFormPage(String id, String title) {
		super(id, title);
		editDomain = new DefaultEditDomain(this);
		smooksFileBuilder = this.createSmooksFileBulder();
	}

	@Override
	public Object getAdapter(Class type) {
		if (type == CommandStack.class)
			return this.getGEFCommandStack();
		return super.getAdapter(type);
	}

	public CommandStack getGEFCommandStack() {
		return this.getEditDomain().getCommandStack();
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {

		try {
			this.initTansformViewerModel((IEditorSite) getSite(),
					getEditorInput());
		}catch(IOWrappedException ex){
			MessageDialog.openWarning(getSite().getShell(), "Waring", "Exceptions occurd during parsing Smooks file, no worries");
		} catch (Throwable e) {
			Status status = UIUtils.createErrorStatus(e);
			ErrorDialog.openError(getSite().getShell(), "Error", "error",
					status);
		}

		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();

		toolkit.decorateFormHeading(form.getForm());
		GridLayout gridLayout = UIUtils.createGeneralFormEditorLayout(1);
		gridLayout.horizontalSpacing = 0;
		form.getBody().setLayout(gridLayout);
		Composite rootMainControl = form.getBody();
		form.setText("Data Mapping Page");
		Section section = this.createPageSectionHeader(rootMainControl,
				Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE
						| Section.EXPANDED, "Mapping Graph Edit Panel",
				"Edit the source and target assosiation");
		Composite mappingMainComposite = toolkit.createComposite(section);
		GridLayout gly = new GridLayout();
		gly.numColumns = 3;
		gly.horizontalSpacing = 0;
		mappingMainComposite.setLayout(gly);
		section.setClient(mappingMainComposite);

		GridData sgd = new GridData(GridData.FILL_BOTH);
		section.setLayoutData(sgd);
		{
			Composite composite1 = toolkit
					.createComposite(mappingMainComposite);
			GridLayout layout = new GridLayout();
			layout.marginHeight = 1;
			layout.marginWidth = 1;
			GridData gd = new GridData(GridData.FILL_BOTH);
			composite1.setLayout(layout);
			sourceViewer = this.createSourceTreeViewer(composite1);
			sourceViewer.getTree().setLayoutData(gd);
			sourceViewer.addDragSupport(DND.DROP_MOVE | DND.DROP_COPY
					| DND.DROP_LINK, new Transfer[] { TemplateTransfer
					.getInstance() }, new DragSourceAdapter() {
				public void dragStart(DragSourceEvent event) {
					event.data = sourceViewer.getSelection();
					TemplateTransfer.getInstance().setTemplate(
							sourceViewer.getSelection());
					event.doit = true;
				}
			});
			sourceViewer.getTree().addPaintListener(
					new TreePaintControlListener());
			sourceViewer.getTree().addListener(SWT.PaintItem,
					new TreeItemPaintListener());

			sourceViewer.addSelectionChangedListener(this);
			composite1.setLayoutData(gd);
			composite1.setBackground(GraphicsConstants.groupBorderColor);
		}
		{
			Composite composite2 = toolkit
					.createComposite(mappingMainComposite);
			GridData composite2LData = new GridData();
			composite2LData.grabExcessHorizontalSpace = true;
			composite2LData.grabExcessVerticalSpace = true;
			composite2LData.horizontalAlignment = GridData.FILL;
			composite2LData.verticalAlignment = GridData.FILL;
			composite2.setLayoutData(composite2LData);
			composite2.setLayout(new FillLayout());
			this.setGraphicalViewer(createGraphicalViewer(composite2));
		}
		{
			Composite composite3 = toolkit
					.createComposite(mappingMainComposite);
			GridData composite3LData = new GridData();
			composite3LData.grabExcessHorizontalSpace = true;
			composite3LData.verticalAlignment = GridData.FILL;
			composite3LData.grabExcessVerticalSpace = true;
			composite3LData.horizontalAlignment = GridData.FILL;
			GridLayout layout = new GridLayout();
			layout.marginWidth = 1;
			layout.marginHeight = 1;
			composite3.setLayout(layout);
			GridData gd = new GridData(GridData.FILL_BOTH);
			targetViewer = createTargetTreeViewer(composite3, SWT.NONE);
			composite3.setLayoutData(gd);
			targetViewer.getTree().setLayoutData(gd);
			targetViewer.addDropSupport(DND.DROP_TARGET_MOVE | DND.DROP_MOVE
					| DND.DROP_LINK | DND.DROP_LINK,
					new Transfer[] { TemplateTransfer.getInstance() },
					new TargetTreeDropTargetListener(targetViewer,
							getGraphicalViewer()));
			targetViewer.getTree().addPaintListener(
					new TreePaintControlListener());
			targetViewer.getTree().addListener(SWT.PaintItem,
					new TreeItemPaintListener());
			targetViewer.addSelectionChangedListener(this);
			composite3.setBackground(GraphicsConstants.groupBorderColor);
		}

		{
			Composite underToolPanel = toolkit.createComposite(rootMainControl);
			GridData sgd1 = new GridData(GridData.FILL_HORIZONTAL);
			GridLayout underLayout = new GridLayout();
			underLayout.numColumns = 3;
			underToolPanel.setLayout(underLayout);
			underToolPanel.setLayoutData(sgd1);
			{
				sourceLink = toolkit.createHyperlink(underToolPanel,
						"Source Select", SWT.NONE);
				sourceLink.addHyperlinkListener(new DataSelectLinkListener(
						sourceViewer));
			}
			{
				Composite composite1 = toolkit.createComposite(underToolPanel);
				GridLayout composite1Layout = new GridLayout();
				composite1Layout.makeColumnsEqualWidth = true;
				GridData composite1LData = new GridData();
				composite1LData.horizontalAlignment = GridData.FILL;
				composite1LData.grabExcessHorizontalSpace = true;
				composite1LData.grabExcessVerticalSpace = true;
				composite1LData.verticalAlignment = GridData.FILL;
				composite1.setLayoutData(composite1LData);
				composite1.setLayout(composite1Layout);
			}
			{
				targetLink = toolkit.createHyperlink(underToolPanel,
						"Target Select", SWT.NONE);
				GridData label2LData = new GridData();
				label2LData.horizontalAlignment = GridData.END;
				targetLink.setLayoutData(label2LData);
				targetLink.addHyperlinkListener(new DataSelectLinkListener(
						targetViewer));
			}
		}

		// other fragment edit panel

		Section section1 = this.createPageSectionHeader(rootMainControl,
				Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE,
				"Filter Edit Panel", "Edit the filter panel");
		Composite otherComposite = this.createUISectionContainer(section1, 1);
		section1.setClient(otherComposite);

		GridData sgd1 = new GridData(GridData.FILL_HORIZONTAL);
		section1.setLayoutData(sgd1);

		toolkit.paintBordersFor(rootMainControl);

		form.pack();
		if (initSourceTreeViewerProviders()) {
			initSourceTreeViewer();
			expandSourceConnectionModel();
		}
		if (initTargetTreeViewerProviders()) {
			initTargetTreeViewer();
			expandTargetConnectionModel();
		}
		this.hookGraphicalViewer();
		this.initGraphicalViewer();

		// getSite().setSelectionProvider(this);
	}

	protected SmooksFileBuilder createSmooksFileBulder() {
		return new SmooksFileBuilder();
	}

	protected void initTargetTreeViewer() {
		if (this.targetTreeViewerInputModel != null) {
			List list = new ArrayList();
			list.add(targetTreeViewerInputModel);
			targetViewer.setInput(list);
		}
	}

	protected boolean initTargetTreeViewerProviders() {
		return this.initTreeViewerProvider(targetViewer, targetDataTypeID);
	}

	protected boolean initTreeViewerProvider(TreeViewer viewer,
			String dataTypeID) {
		return UIUtils.setTheProvidersForTreeViewer(viewer, dataTypeID);
	}

	protected TreeViewer createSourceTreeViewer(Composite composite) {
		sourceViewer = new TreeViewer(composite, SWT.NONE);
		return sourceViewer;
	}

	protected void initSourceTreeViewer() {
		if (this.sourceTreeViewerInputModel != null) {
			List list = new ArrayList();
			list.add(sourceTreeViewerInputModel);
			sourceViewer.setInput(list);
		}
	}

	protected void expandSourceConnectionModel() {
		this.expandConnectionModel(sourceViewer, true);
	}

	protected void expandConnectionModel(TreeViewer viewer, boolean isSource) {
		if (initConnectionList == null || initConnectionList.isEmpty())
			return;
		ITreeContentProvider provider = (ITreeContentProvider) viewer
				.getContentProvider();
		if (provider == null)
			return;
		for (Iterator iterator = initConnectionList.iterator(); iterator
				.hasNext();) {
			MappingModel connection = (MappingModel) iterator.next();
			Object currentModel = connection.getTarget();
			if (isSource) {
				currentModel = connection.getSource();
			}
			Object parent = provider.getParent(currentModel);
			while (parent != null && parent != currentModel) {
				viewer.expandToLevel(parent, 1);
				parent = provider.getParent(parent);
			}
		}
	}

	protected void expandTargetConnectionModel() {
		this.expandConnectionModel(targetViewer, false);
	}

	protected boolean initSourceTreeViewerProviders() {
		return this.initTreeViewerProvider(sourceViewer, sourceDataTypeID);
	}

	/**
	 * this method is only for demo
	 * 
	 * @param items
	 * @param modelClass
	 */
	protected void createGraphModels(TreeItem[] items,
			Class<? extends Object> modelClass) {
		for (int i = 0; i < items.length; i++) {
			TreeItem item = (TreeItem) items[i];
			if (item.getData(REFERENCE_MODEL) != null) {

			} else {
				AbstractStructuredDataModel model = null;
				if (modelClass == SourceModel.class) {
					model = new SourceModel();
				}
				if (modelClass == TargetModel.class) {
					model = new TargetModel();
				}
				if (model != null && model instanceof TreeItemRelationModel) {
					model.setReferenceEntityModel(item.getData());
					((TreeItemRelationModel) model).setTreeItem(item);
					item.setData(REFERENCE_MODEL, model);
					this.rootModel.addChild(model);
				}
			}
			if (item.getExpanded() && item.getItemCount() > 0) {
				createGraphModels(item.getItems(), modelClass);
			} else {
			}
		}
	}

	protected void initViewerInitor() {

	}

	protected void createSourceGraphModels() {
		Tree tree = sourceViewer.getTree();
		TreeItem[] items = tree.getItems();
		createGraphModels(items, SourceModel.class);
	}

	protected void createTargetGraphModels() {
		Tree tree = targetViewer.getTree();
		TreeItem[] items = tree.getItems();
		createGraphModels(items, TargetModel.class);
	}

	protected TreeViewer createTargetTreeViewer(Composite parent, int style) {
		TreeViewer viewer = new TreeViewer(parent, SWT.NONE | style);
		return viewer;
	}

	protected Section createPageSectionHeader(Composite parent, int style,
			String text, String description) {
		Section section = getManagedForm().getToolkit().createSection(parent,
				style);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		section.setLayout(layout);
		section.setText(text);
		section.setDescription(description);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		section.setLayoutData(data);
		return section;
	}

	protected GraphicalViewer createGraphicalViewer(Composite parent) {
		GraphicalViewer viewer = new ScrollingGraphicalViewer();
		Control control = viewer.createControl(parent);
		control.setBackground(ColorConstants.white);
		return viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormPage#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return commandStackChanged || super.isDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormPage#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		SmooksFileBuilder builder = this.getSmooksFileBuilder();
		SmooksConfigurationFileGenerateContext context = this
				.getSmooksConfigurationFileGenerateContext();
		this.initSmooksConfigurationFileGenerateContext(context);
		Exception exp = null;
		try {
			// generate smooks configuration file
			InputStream stream = builder.generateSmooksFile(context, monitor);
			IFile file = ((IFileEditorInput) this.getEditorInput()).getFile();
			if (file.exists()) {
				file.setContents(stream, IResource.FORCE, monitor);
			}

			// save graphical informations
			if (this.graphicalInformationSaver != null) {
				graphicalInformationSaver.doSave(monitor, context);
			}
		} catch (CoreException e) {
			exp = e;
		} catch (SmooksAnalyzerException e) {
			exp = e;
		} catch (IOException e) {
			exp = e;
		}
		if (exp != null) {
			MessageDialog.openError(getSite().getShell(), "Save Error",
					"Error! \n" + exp.getMessage());
		}
		super.doSave(monitor);
		commandStackChanged = false;
		getManagedForm().dirtyStateChanged();
	}

	protected SmooksConfigurationFileGenerateContext createContext() {
		SmooksConfigurationFileGenerateContext context = new SmooksConfigurationFileGenerateContext();
		return context;
	}

	protected void initSmooksConfigurationFileGenerateContext(
			SmooksConfigurationFileGenerateContext context) {
		context.setSourceDataTypeID(this.sourceDataTypeID);
		context.setTargetDataTypeID(this.targetDataTypeID);
		context.setSmooksType(SmooksConstants.SAX);
		context.setDataMappingRootModel(this.rootModel);
	}

	protected Composite createUISectionContainer(Composite parent, int columns) {
		Composite container = getManagedForm().getToolkit().createComposite(
				parent);
		GridLayout layout = new GridLayout();
		layout.numColumns = columns;
		container.setLayout(layout);
		return container;
	}

	protected void hookGraphicalViewer() {
		getSelectionSynchronizer().addViewer(getGraphicalViewer());
		this.getGraphicalViewer().addSelectionChangedListener(this);
		getSite().setSelectionProvider(getGraphicalViewer());
		getGraphicalViewer().addDropTargetListener(
				new MappingPanelDropTargetListener(this.getGraphicalViewer()));
	}

	protected ActionRegistry getActionRegistry() {
		if (actionRegistry == null)
			actionRegistry = new ActionRegistry();
		return actionRegistry;
	}

	protected void initGraphicalViewer() {

		createActions();
		rootModel = new GraphRootModel();
		this.createSourceGraphModels();
		this.createTargetGraphModels();
		this.createConnectionModels();
		this.getGraphicalViewer().setEditPartFactory(
				new SmooksEditPartFactory());
		this.getGraphicalViewer().setContents(rootModel);

		GraphicalViewerKeyHandler keyHandler = new GraphicalViewerKeyHandler(
				getGraphicalViewer());
		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 0), this
				.getActionRegistry().getAction(GEFActionConstants.DELETE));

		SmooksGraphViewerContextMenuProvider provider = new SmooksGraphViewerContextMenuProvider(
				getGraphicalViewer(), this.getActionRegistry());
		getGraphicalViewer().setContextMenu(provider);

		LayerManager manager = (LayerManager) getGraphicalViewer()
				.getEditPartRegistry().get(LayerManager.ID);
		ConnectionLayer layer = (ConnectionLayer) manager
				.getLayer(LayerConstants.CONNECTION_LAYER);
		if (layer != null)
			layer.setAntialias(SWT.SMOOTH);

		// hook command stack
		this.getEditDomain().getCommandStack().addCommandStackListener(
				new CommandStackListener() {

					public void commandStackChanged(EventObject event) {
						commandStackChanged = true;
						updateSelectionActions();
						getManagedForm().dirtyStateChanged();
					}

				});
	}

	protected void createConnectionModels() {
		List children = this.rootModel.getChildren();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			TreeItemRelationModel source = (TreeItemRelationModel) iterator
					.next();
			if (source instanceof SourceModel) {
				for (Iterator iterator2 = children.iterator(); iterator2
						.hasNext();) {
					TreeItemRelationModel target = (TreeItemRelationModel) iterator2
							.next();
					if (target instanceof TargetModel) {
						MappingModel mapping = getMappingModel(source, target);
						if (mapping != null) {
							LineConnectionModel connection = new LineConnectionModel(
									source, target);
							connection.setProperties(mapping.getProperties());
						}
					}
				}
			}
		}
	}

	protected MappingModel getMappingModel(TreeItemRelationModel source,
			TreeItemRelationModel target) {
		Object sourceReferModel = source.getReferenceEntityModel();
		Object targetReferModel = target.getReferenceEntityModel();
		if (initConnectionList == null)
			return null;
		for (Iterator iterator = initConnectionList.iterator(); iterator
				.hasNext();) {
			MappingModel mapping = (MappingModel) iterator.next();
			Object s = mapping.getSource();
			Object t = mapping.getTarget();
			if (s == sourceReferModel && t == targetReferModel)
				return mapping;
		}
		return null;
	}

	public SelectionSynchronizer getSelectionSynchronizer() {
		if (selectionSynchronizer == null)
			selectionSynchronizer = new SelectionSynchronizer();
		return selectionSynchronizer;
	}

	public void setSelectionSynchronizer(
			SelectionSynchronizer selectionSynchronizer) {
		this.selectionSynchronizer = selectionSynchronizer;
	}

	public DefaultEditDomain getEditDomain() {
		return editDomain;
	}

	public void setEditDomain(DefaultEditDomain editDomain) {
		this.editDomain = editDomain;
	}

	public GraphicalViewer getGraphicalViewer() {
		return graphicalViewer;
	}

	public void setGraphicalViewer(GraphicalViewer graphicalViewer) {
		this.graphicalViewer = graphicalViewer;
		this.getEditDomain().addViewer(graphicalViewer);
	}

	public IViewerInitor getSourceViewerInitor() {
		return sourceViewerInitor;
	}

	public void setSourceViewerInitor(IViewerInitor sourceViewerInitor) {
		this.sourceViewerInitor = sourceViewerInitor;
	}

	public IViewerInitor getTargetViewerInitor() {
		return targetViewerInitor;
	}

	public void setTargetViewerInitor(IViewerInitor targetViewerInitor) {
		this.targetViewerInitor = targetViewerInitor;
	}

	protected void initTansformViewerModel(IEditorSite site, IEditorInput input)
			throws Throwable {
		graphicalInformationSaver = new GraphicalInformationSaver(input);

		GraphInformations graph = null;
		try {
			graph = graphicalInformationSaver.doLoad();
			initFormEditorWithGraphInfo(graph);
		} catch (Throwable t) {
			// ignore
		}

		if (sourceDataTypeID == null || targetDataTypeID == null) {
			TypeIDSelectionWizard wizard = new TypeIDSelectionWizard();
			wizard.setSourceDataTypeID(sourceDataTypeID);
			wizard.setTargetDataTypeID(targetDataTypeID);
			WizardDialog dialog = new WizardDialog(getSite().getShell(), wizard);
			if (dialog.open() == org.eclipse.jface.dialogs.Dialog.OK) {
				sourceDataTypeID = wizard.getSourceDataTypeID();
				targetDataTypeID = wizard.getTargetDataTypeID();
			}
		}
		String path = ResourcesPlugin.getWorkspace().getRoot().getLocation()
				.append(((IFileEditorInput) input).getFile().getFullPath())
				.toString();
		Resource resource = new SmooksResourceFactoryImpl().createResource(URI
				.createFileURI(path));
		resource.load(Collections.EMPTY_MAP);
		ISourceModelAnalyzer sourceModelAnalyzer = AnalyzerFactory
				.getInstance().getSourceModelAnalyzer(sourceDataTypeID);
		ITargetModelAnalyzer targetModelAnalyzer = AnalyzerFactory
				.getInstance().getTargetModelAnalyzer(targetDataTypeID);
		IMappingAnalyzer connectionAnalyzer = AnalyzerFactory.getInstance()
				.getMappingAnalyzer(sourceDataTypeID, targetDataTypeID);
		SmooksResourceListType listType = ((DocumentRoot) resource
				.getContents().get(0)).getSmooksResourceList();
		if (sourceModelAnalyzer != null)
			sourceTreeViewerInputModel = sourceModelAnalyzer
					.buildSourceInputObjects(graph, listType,
							((IFileEditorInput) input).getFile());
		if (targetModelAnalyzer != null)
			targetTreeViewerInputModel = targetModelAnalyzer
					.buildTargetInputObjects(graph, listType,
							((IFileEditorInput) input).getFile());
		if (connectionAnalyzer != null) {
			initConnectionList = connectionAnalyzer.analyzeMappingSmooksModel(
					listType, sourceTreeViewerInputModel,
					targetTreeViewerInputModel);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormPage#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		// if (Registry.INSTANCE.get(GraphicalPackage.eNS_URI) == null) {
		Registry.INSTANCE.put(GraphicalPackage.eNS_URI,
				GraphicalPackage.eINSTANCE);
		// }
	}

	protected void initFormEditorWithGraphInfo(GraphInformations graph) {
		MappingDataType mapping = graph.getMappingType();
		if (mapping != null) {
			this.sourceDataTypeID = mapping.getSourceTypeID();
			this.targetDataTypeID = mapping.getTargetTypeID();
		}
		if (sourceDataTypeID == null || targetDataTypeID == null) {

		}
	}

	/**
	 * 
	 * @param viewer
	 */
	protected void showCreationWizard(TreeViewer viewer) {
		TransformDataSelectionWizard wizard = new TransformDataSelectionWizard();
		wizard.setInput(getEditorInput());
		wizard.setSite(getEditorSite());
		wizard.setForcePreviousAndNextButtons(true);
		StructuredDataCreationWizardDailog dialog = new StructuredDataCreationWizardDailog(
				getEditorSite().getShell(), wizard);
		if (dialog.open() == Dialog.OK) {
			IStrucutredDataCreationWizard cw = dialog
					.getCurrentCreationWizard();
			String typeID = cw.getInputDataTypeID();
			if (UIUtils.setTheProvidersForTreeViewer(viewer, typeID)) {
				viewer.setInput(cw.getTreeViewerInputContents());
				// the viewer must be expanded , then the graphics model can
				// calculate the location correctly
				try {
					// viewer.expandAll();
					if (viewer == this.sourceViewer) {
						this.createSourceGraphModels();
						sourceDataTypeID = typeID;
					}
					if (viewer == this.targetViewer) {
						this.createTargetGraphModels();
						targetDataTypeID = typeID;
					}
				} catch (Exception e) {
					e.printStackTrace();
					MessageDialog.openError(getSite().getShell(), "Error",
							"a error occurs during filling Data into the viewer:\n"
									+ e.toString());
				}
			} else {
				MessageDialog.openError(getSite().getShell(), "Error",
						"a error occurs during filling Data into the viewer");
			}
		}
	}

	protected void createActions() {
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

		registry.registerAction(new PrintAction(this));
	}

	private class DataSelectLinkListener implements IHyperlinkListener {
		TreeViewer viewer = null;

		public DataSelectLinkListener(TreeViewer viewer) {
			this.viewer = viewer;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.forms.events.IHyperlinkListener#linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent)
		 */
		public void linkActivated(HyperlinkEvent e) {
			showCreationWizard(viewer);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.forms.events.IHyperlinkListener#linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent)
		 */
		public void linkEntered(HyperlinkEvent e) {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.forms.events.IHyperlinkListener#linkExited(org.eclipse.ui.forms.events.HyperlinkEvent)
		 */
		public void linkExited(HyperlinkEvent e) {

		}

	}

	/**
	 * @return the smooksFileBuilder
	 */
	protected SmooksFileBuilder getSmooksFileBuilder() {
		return smooksFileBuilder;
	}

	/**
	 * @param smooksFileBuilder
	 *            the smooksFileBuilder to set
	 */
	protected void setSmooksFileBuilder(SmooksFileBuilder smooksFileBuilder) {
		this.smooksFileBuilder = smooksFileBuilder;
	}

	/**
	 * @author Dart Peng
	 * @Date Aug 19, 2008
	 */
	private class TreeItemPaintListener implements Listener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
		 */
		public void handleEvent(Event event) {
			TreeItem item = (TreeItem) event.item;
			if (item.getBounds().y == 0)
				return;
			Object obj = item
					.getData(TreeItemRelationModel.PRO_TREE_ITEM_SELECTION_STATUS);
			if (obj == null) {
				return;
			}
			boolean drawLine = ((Boolean) obj).booleanValue();
			if (drawLine) {
				// event.gc.draw(GraphicsConstants.groupHeaderColor);
				// event.gc.fillRectangle(event.x, event.y, event.width,
				// event.height);
				// event.gc.setLineWidth(2);
				// event.gc.drawLine(x + 20, y,
				// item.getParent().getBounds().width - 2, y);
			}
		}

	}

	/**
	 * @author Dart Peng
	 * @Date Aug 19, 2008
	 */
	private class TreePaintControlListener implements PaintListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		public void paintControl(PaintEvent e) {
			Tree tree = (Tree) e.getSource();
			Class modelClass = TargetModel.class;
			if (tree == sourceViewer.getTree()) {
				modelClass = SourceModel.class;
			}
			createGraphModels(new TreeItem[] { tree.getTopItem() }, modelClass);
			if (tree.getData(TreeItemRelationModel.PRO_TREE_REPAINT) != null) {
				// System.out.println("Block a event fire !!");
				return;
			}
			rootModel.firePropertyChange(RootModel.P_REFRESH_PANEL, null,
					new Object());
		}

	}

	public void selectionChanged(SelectionChangedEvent event) {
		updateSelectionActions();
		// this.setSelection(event.getSelection());
	}

	protected void updateSelectionActions() {
		IAction action = getActionRegistry().getAction(
				IWorkbenchActionConstants.DELETE);
		if (action != null) {
			if (action instanceof UpdateAction)
				((UpdateAction) action).update();
		}
	}

	public SmooksConfigurationFileGenerateContext getSmooksConfigurationFileGenerateContext() {
		if (smooksConfigurationFileGenerateContext == null) {
			smooksConfigurationFileGenerateContext = createContext();
		}
		return smooksConfigurationFileGenerateContext;
	}

	private class StructuredDataTreeListener implements TreeListener {

		public void treeCollapsed(TreeEvent e) {

		}

		public void treeExpanded(TreeEvent e) {
			// Tree tree = (Tree) e.getSource();
			// Class modelClass = TargetModel.class;
			// if (tree == sourceViewer.getTree()) {
			// modelClass = SourceModel.class;
			// }
			// createGraphModels(new TreeItem[] { tree.getTopItem() },
			// modelClass);
		}

	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.selectionChangeListener.add(listener);
	}

	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		this.selectionChangeListener.remove(listener);
	}

}
