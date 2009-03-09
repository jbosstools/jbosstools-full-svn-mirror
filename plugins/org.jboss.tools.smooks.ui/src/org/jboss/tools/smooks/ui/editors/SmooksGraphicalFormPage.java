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

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
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
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
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
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.smooks.analyzer.AnalyzerFactory;
import org.jboss.tools.smooks.analyzer.DesignTimeAnalyzeResult;
import org.jboss.tools.smooks.analyzer.IMappingAnalyzer;
import org.jboss.tools.smooks.analyzer.ISourceModelAnalyzer;
import org.jboss.tools.smooks.analyzer.ITargetModelAnalyzer;
import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.ResolveCommand;
import org.jboss.tools.smooks.analyzer.SmooksAnalyzerException;
import org.jboss.tools.smooks.analyzer.SmooksFileBuilder;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.GraphicalFactory;
import org.jboss.tools.smooks.graphical.MappingDataType;
import org.jboss.tools.smooks.graphical.Param;
import org.jboss.tools.smooks.graphical.Params;
import org.jboss.tools.smooks.graphical.util.GraphicalInformationSaver;
import org.jboss.tools.smooks.model.AbstractResourceConfig;
import org.jboss.tools.smooks.model.DocumentRoot;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.model.util.SmooksResourceFactoryImpl;
import org.jboss.tools.smooks.ui.AnalyzeResult;
import org.jboss.tools.smooks.ui.IAnalyzeListener;
import org.jboss.tools.smooks.ui.IStructuredDataCreationWizard;
import org.jboss.tools.smooks.ui.IViewerInitor;
import org.jboss.tools.smooks.ui.SmooksUIActivator;
import org.jboss.tools.smooks.ui.StructuredDataCreationWizardDailog;
import org.jboss.tools.smooks.ui.TargetTreeDragListener;
import org.jboss.tools.smooks.ui.ViewerInitorStore;
import org.jboss.tools.smooks.ui.gef.commands.DeleteConnectionCommand;
import org.jboss.tools.smooks.ui.gef.editparts.SmooksEditPartFactory;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.SourceModel;
import org.jboss.tools.smooks.ui.gef.model.TargetModel;
import org.jboss.tools.smooks.ui.gef.model.TreeItemRelationModel;
import org.jboss.tools.smooks.ui.gef.tools.MappingPanelDropTargetListener;
import org.jboss.tools.smooks.ui.gef.tools.TargetTreeDropTargetListener;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.ui.popup.ActionProviderManager;
import org.jboss.tools.smooks.ui.popup.ISmooksAction;
import org.jboss.tools.smooks.ui.popup.IViewerActionsProvider;
import org.jboss.tools.smooks.ui.popup.SmooksAction;
import org.jboss.tools.smooks.ui.wizards.SmooksConfigFileNewWizard;
import org.jboss.tools.smooks.ui.wizards.TransformDataSelectionWizard;
import org.jboss.tools.smooks.utils.SmooksGraphConstants;
import org.jboss.tools.smooks.utils.UIUtils;
import org.jboss.tools.smooks.xml.model.ITransformTreeNode;

/**
 * @author Dart Peng
 * @Date Jul 28, 2008
 */
public class SmooksGraphicalFormPage extends FormPage implements
		ISelectionChangedListener,
		org.eclipse.emf.common.command.CommandStackListener, ISaveListener {

	private HashMap<Object, Object> graph_trasform_data_map = new HashMap<Object, Object>();

	private CompositeSelectionProvider compositeSelectionProvider = new CompositeSelectionProvider();

	private final String[] REQUIRED_SOURCE_SELECT_TYPE = new String[] { "org.jboss.tools.smooks.xml.viewerInitor.xml" };

	private List<IAnalyzeListener> analyzeListenerList = new ArrayList<IAnalyzeListener>();

	protected boolean disableMappingGUI = false;

	private List<DesignTimeAnalyzeResult> analyzeResultList = new ArrayList<DesignTimeAnalyzeResult>();

	private static final String REFERENCE_MODEL = "__reference_model"; //$NON-NLS-1$
	protected SmooksConfigurationFileGenerateContext smooksConfigurationFileGenerateContext;
	protected IViewerInitor sourceViewerInitor;
	protected IViewerInitor targetViewerInitor;
	protected TreeViewer targetViewer;
	protected TreeViewer sourceViewer;
	protected SelectionSynchronizer selectionSynchronizer = null;
	protected GraphicalViewer graphicalViewer;
	protected DefaultEditDomain gefEditDomain;
	protected GraphRootModel rootModel;
	protected Hyperlink sourceLink = null;
	protected Hyperlink targetLink = null;
	protected String sourceDataTypeID = null;
	protected GraphInformations graphinformations = null;

	public String getSourceDataTypeID() {
		return sourceDataTypeID;
	}

	public void setSourceDataTypeID(String sourceDataTypeID) {
		this.sourceDataTypeID = sourceDataTypeID;
	}

	public String getTargetDataTypeID() {
		return targetDataTypeID;
	}

	public void setTargetDataTypeID(String targetDataTypeID) {
		this.targetDataTypeID = targetDataTypeID;
	}

	protected String targetDataTypeID = null;
	protected SmooksFileBuilder smooksFileBuilder = null;
	protected GraphicalInformationSaver graphicalInformationSaver = null;
	protected Resource smooksResource = null;
	protected Object sourceTreeViewerInputModel = null;
	protected Object targetTreeViewerInputModel = null;
	protected List<MappingModel> mappingModelList = null;
	protected boolean commandStackChanged = false;
	protected ActionRegistry actionRegistry;

	private List selectionChangeListener = new ArrayList();
	private ISelection selection;
	protected MappingResourceConfigList mappingResourceConfigList;

	public MappingResourceConfigList getMappingResourceConfigList() {
		return mappingResourceConfigList;
	}

	protected AdapterFactoryEditingDomain editingDomain;
	private boolean canSaveFile = true;

	private Composite designTimeAnalyzeResultRegion;

	private Section mappingGUISection;

	private Section problemSection;

	public ISelection getSelection() {
		return selection;
	}

	public void fireEmptySelection() {
		StructuredSelection selection = new StructuredSelection(new Object[] {});
		compositeSelectionProvider.setSelection(selection);
	}

	public SmooksGraphicalFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
		gefEditDomain = new DefaultEditDomain(this);
		smooksFileBuilder = this.createSmooksFileBulder();
	}

	public SmooksGraphicalFormPage(String id, String title) {
		super(id, title);
		gefEditDomain = new DefaultEditDomain(this);
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

	protected void cleanMappingResourceConfig() {
		// SmooksResourceListType list = null;
		// if (!smooksResource.getContents().isEmpty()) {
		// DocumentRoot doc = (DocumentRoot) this.smooksResource.getContents()
		// .get(0);
		// list = doc.getSmooksResourceList();
		// ResourceConfigEraser eraser = new ResourceConfigEraser();
		// eraser.cleanMappingResourceConfig(list, mappingResourceConfigList,
		// this.editingDomain);
		// } else {
		// DocumentRoot doc = SmooksFactory.eINSTANCE.createDocumentRoot();
		// smooksResource.getContents().add(doc);
		// list = SmooksFactory.eINSTANCE.createSmooksResourceListType();
		// doc.setSmooksResourceList(list);
		// }
	}

	public void addAnalyzeListener(IAnalyzeListener listener) {
		if (listener != null) {
			analyzeListenerList.add(listener);
		}
	}

	public void removeAnalyzeListener(IAnalyzeListener listener) {
		if (listener != null) {
			analyzeListenerList.remove(listener);
		}
	}

	public void cleanAnalyzeListenerList() {
		analyzeListenerList.clear();
	}

	@Override
	public void dispose() {
		cleanAnalyzeListenerList();
		super.dispose();
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {

		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();

		toolkit.decorateFormHeading(form.getForm());
		GridLayout gridLayout = UIUtils.createGeneralFormEditorLayout(1);
		gridLayout.horizontalSpacing = 0;
		form.getBody().setLayout(gridLayout);
		Composite rootMainControl = form.getBody();
		form.setText(Messages
				.getString("SmooksGraphicalFormPage.MappingPageFormTitle")); //$NON-NLS-1$

		createErrorMessageLinkGUI(toolkit, rootMainControl);

		mappingGUISection = this
				.createPageSectionHeader(
						rootMainControl,
						Section.TITLE_BAR | Section.DESCRIPTION,
						Messages
								.getString("SmooksGraphicalFormPage.MappingSectionTitle"), //$NON-NLS-1$
						Messages
								.getString("SmooksGraphicalFormPage.MappingSectionDescription")); //$NON-NLS-1$

		Composite mainComposite = toolkit.createComposite(mappingGUISection);
		mappingGUISection.setClient(mainComposite);
		GridData mapgd = new GridData(GridData.FILL_HORIZONTAL);
		mappingGUISection.setLayoutData(mapgd);
		GridLayout mainLayout = new GridLayout();
		mainComposite.setLayout(mainLayout);

		SashForm sashForm = new SashForm(mainComposite, SWT.VERTICAL);
		GridData sashFormLd = new GridData(GridData.FILL_BOTH);
		sashForm.setLayoutData(sashFormLd);
		// sashForm.
		// sashForm.setSashWidth(1);

		SashForm mappingMainComposite = new SashForm(sashForm, SWT.NONE);
		// TODO under the eclipse3.3
		// mappingMainComposite.setSashWidth(1);
		GridData sgd = new GridData(GridData.FILL_BOTH);
		mappingGUISection.setLayoutData(sgd);
		{
			Composite composite1 = toolkit
					.createComposite(mappingMainComposite);
			GridLayout layout = new GridLayout();
			layout.marginWidth = 1;
			layout.marginHeight = 1;
			composite1.setLayout(layout);

			GridData gd = new GridData(GridData.FILL_BOTH);
			sourceViewer = this.createSourceTreeViewer(composite1);
			compositeSelectionProvider.addSelectionProvider(sourceViewer);
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
			final MenuManager menuManager = new MenuManager("#SourceViewerMenu");
			sourceViewer.getTree().setMenu(
					menuManager.createContextMenu(sourceViewer.getTree()));
			menuManager.add(new Separator("smooks_additions"));
			menuManager.addMenuListener(new IMenuListener() {

				public void menuAboutToShow(IMenuManager manager) {
					manager.removeAll();
					fillMenuManager(menuManager, true);
				}

			});
			getSite().registerContextMenu(menuManager, sourceViewer);

			sourceViewer.addSelectionChangedListener(this);
			composite1.setLayoutData(gd);
			composite1.setBackground(GraphicsConstants.groupBorderColor);
		}

		{
			Composite composite2 = toolkit
					.createComposite(mappingMainComposite);
			composite2.setLayout(new FillLayout());
			this.setGraphicalViewer(createGraphicalViewer(composite2));
		}
		{
			Composite composite3 = toolkit
					.createComposite(mappingMainComposite);
			GridLayout layout = new GridLayout();
			layout.marginWidth = 1;
			layout.marginHeight = 1;
			composite3.setLayout(layout);
			GridData gd = new GridData(GridData.FILL_BOTH);
			targetViewer = createTargetTreeViewer(composite3, SWT.NONE);
			compositeSelectionProvider.addSelectionProvider(targetViewer);
			composite3.setLayoutData(gd);
			targetViewer.getTree().setLayoutData(gd);

			final MenuManager menuManager = new MenuManager("#TargetViewerMenu");
			targetViewer.getTree().setMenu(
					menuManager.createContextMenu(targetViewer.getTree()));
			menuManager.addMenuListener(new IMenuListener() {

				public void menuAboutToShow(IMenuManager manager) {
					manager.removeAll();
					fillMenuManager(menuManager, false);
				}

			});
			menuManager.add(new Separator("smooks_additions"));
			getSite().registerContextMenu(menuManager, targetViewer);
			composite3.setBackground(GraphicsConstants.groupBorderColor);
		}

		{
			Composite underToolPanel = toolkit.createComposite(mainComposite);
			GridData sgd1 = new GridData(GridData.FILL_HORIZONTAL);
			GridLayout underLayout = new GridLayout();
			underLayout.numColumns = 3;
			sgd1.horizontalSpan = 3;
			underToolPanel.setLayout(underLayout);
			underToolPanel.setLayoutData(sgd1);
			{
				sourceLink = toolkit
						.createHyperlink(
								underToolPanel,
								Messages
										.getString("SmooksGraphicalFormPage.SourceSelectLinkText"), SWT.NONE); //$NON-NLS-1$
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
				targetLink = toolkit
						.createHyperlink(
								underToolPanel,
								Messages
										.getString("SmooksGraphicalFormPage.TargetSelectLinkText"), SWT.NONE); //$NON-NLS-1$
				GridData label2LData = new GridData();
				label2LData.horizontalAlignment = GridData.END;
				targetLink.setLayoutData(label2LData);
				targetLink.addHyperlinkListener(new DataSelectLinkListener(
						targetViewer));
			}
		}

		toolkit.paintBordersFor(rootMainControl);
		form.pack();

		/*
		 * below is init GUIs
		 */

		Throwable throwable = null;
		try {
			this.initTransformViewerModel((IEditorSite) getSite(),
					getEditorInput());
		} catch (Throwable e) {
			throwable = e;
		}
		if (throwable != null) {
			this.disableMappingGUI = true;
			((SmooksFormEditor) getEditor()).setParseException(true, throwable);
		}
		this.notifyAnalyzeListeners(throwable);

		if (initSourceTreeViewerProviders()) {
			initSourceTreeViewer();
			expandSourceConnectionModel();
		}
		if (initTargetTreeViewerProviders()) {
			initTargetTreeViewer();
			expandTargetConnectionModel();
		}

		getSite().setSelectionProvider(compositeSelectionProvider);
		this.hookTargetTreeViewer(targetViewer);
		this.hookGraphicalViewer();
		this.initGraphicalViewer();
		initMappingGUIStates();
	}

	protected void hookTargetTreeViewer(TreeViewer targetViewer) {
		targetViewer.addDropSupport(DND.DROP_TARGET_MOVE | DND.DROP_MOVE
				| DND.DROP_LINK | DND.DROP_LINK,
				new Transfer[] { TemplateTransfer.getInstance() },
				new TargetTreeDropTargetListener(targetViewer,
						getGraphicalViewer()));

		targetViewer.addDragSupport(DND.DROP_MOVE | DND.DROP_COPY
				| DND.DROP_LINK, new Transfer[] { TemplateTransfer
				.getInstance() }, new TargetTreeDragListener(targetViewer,
				getGraphicalViewer()));

		targetViewer.getTree().addPaintListener(new TreePaintControlListener());
		targetViewer.getTree().addListener(SWT.PaintItem,
				new TreeItemPaintListener());
		targetViewer.addSelectionChangedListener(this);
	}

	protected void createErrorMessageLinkGUI(FormToolkit toolkit,
			Composite parent) {

		problemSection = this.createPageSectionHeader(parent, Section.TITLE_BAR
				| Section.EXPANDED, "Problems", "No problems");
		designTimeAnalyzeResultRegion = toolkit.createComposite(problemSection);
		GridLayout ngl = new GridLayout();
		ngl.numColumns = 2;
		ngl.marginWidth = 0;
		problemSection.setLayout(new FillLayout());
		designTimeAnalyzeResultRegion.setLayout(ngl);
		problemSection.setClient(designTimeAnalyzeResultRegion);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.exclude = true;
		problemSection.setLayoutData(gd);
		problemSection.setVisible(false);
	}

	public void refreshAllGUI(InputStream stream) {
		sourceTreeViewerInputModel = null;
		targetTreeViewerInputModel = null;
		Throwable throwable = null;
		try {
			analyzeGraphicalModel(stream);
		} catch (Throwable e) {
			throwable = e;
		}
		if (throwable == null) {
			this.disableMappingGUI = false;
			if (mappingGUISection != null)
				mappingGUISection.setEnabled(true);
			if (initSourceTreeViewerProviders()) {
				initSourceTreeViewer();
				expandSourceConnectionModel();
			}
			if (initTargetTreeViewerProviders()) {
				initTargetTreeViewer();
				expandTargetConnectionModel();
			}
			this.redrawMappingPanel();
			notifyAnalyzeListeners(null);
		} else {
			cleanMappingPanel();
			this.disableMappingGUI = true;
			if (mappingGUISection != null)
				mappingGUISection.setEnabled(false);
			this.notifyAnalyzeListeners(throwable);
		}

		// show/unshow the problem panel
		if (disableMappingGUI) {
			analyzeResultList.clear();
			updateNotifyMessage();
		} else {
			analyzeDesignGraph();
		}
	}

	public void refreshAllGUI() {
		sourceTreeViewerInputModel = null;
		targetTreeViewerInputModel = null;
		Throwable throwable = null;
		try {
			this.getSmooksResource().unload();
			this.initTransformViewerModel((IEditorSite) getSite(),
					getEditorInput());
		} catch (Throwable e) {
			throwable = e;
		}
		if (throwable == null) {
			this.disableMappingGUI = false;
			if (mappingGUISection != null)
				mappingGUISection.setEnabled(true);
			if (initSourceTreeViewerProviders()) {
				initSourceTreeViewer();
				expandSourceConnectionModel();
			}
			if (initTargetTreeViewerProviders()) {
				initTargetTreeViewer();
				expandTargetConnectionModel();
			}
			this.redrawMappingPanel();
			notifyAnalyzeListeners(null);
		} else {
			cleanMappingPanel();
			this.disableMappingGUI = true;
			if (mappingGUISection != null)
				mappingGUISection.setEnabled(false);
			this.notifyAnalyzeListeners(throwable);
		}

		// show/unshow the problem panel
		if (disableMappingGUI) {
			analyzeResultList.clear();
			updateNotifyMessage();
		} else {
			analyzeDesignGraph();
		}
	}

	protected void initMappingGUIStates() {
		if (this.disableMappingGUI) {
			mappingGUISection.setEnabled(false);
		}
	}

	protected SmooksFileBuilder createSmooksFileBulder() {
		return new SmooksFileBuilder(this.getEditingDomain());
	}

	protected void initTargetTreeViewer() {
		if (this.targetTreeViewerInputModel != null) {
			// if (targetTreeViewerInputModel instanceof ITransformTreeNode
			// && targetViewer instanceof PropertyChangeListener) {
			// ((ITransformTreeNode) targetTreeViewerInputModel)
			// .addNodePropetyChangeListener((PropertyChangeListener)
			// targetViewer);
			// }
			targetViewer.setInput(targetTreeViewerInputModel);
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
		sourceViewer = new TransformDataTreeViewer(composite, SWT.NONE, this);
		return sourceViewer;
	}

	protected void initSourceTreeViewer() {
		if (this.sourceTreeViewerInputModel != null) {
			if (sourceTreeViewerInputModel instanceof ITransformTreeNode
					&& sourceViewer instanceof PropertyChangeListener) {
				((ITransformTreeNode) sourceTreeViewerInputModel)
						.addNodePropetyChangeListener((PropertyChangeListener) sourceViewer);
			}
			sourceViewer.setInput(sourceTreeViewerInputModel);
		}
	}

	protected void expandSourceConnectionModel() {
		this.expandConnectionModel(sourceViewer, true);
	}

	protected void expandConnectionModel(TreeViewer viewer, boolean isSource) {
		if (mappingModelList == null || mappingModelList.isEmpty())
			return;
		ITreeContentProvider provider = (ITreeContentProvider) viewer
				.getContentProvider();
		if (provider == null)
			return;
		for (Iterator iterator = mappingModelList.iterator(); iterator
				.hasNext();) {
			MappingModel connection = (MappingModel) iterator.next();
			Object currentModel = connection.getTarget();
			if (isSource) {
				currentModel = connection.getSource();
			}
			Object parent = provider.getParent(currentModel);
			try {
				while (parent != null && parent != currentModel) {
					viewer.expandToLevel(parent, 1);
					parent = provider.getParent(parent);
				}
			} catch (Exception e) {
				continue;
			}
			currentModel = connection.getSource();
			parent = provider.getParent(currentModel);
			try {
				while (parent != null && parent != currentModel) {
					viewer.expandToLevel(parent, 1);
					parent = provider.getParent(parent);
				}
			} catch (Exception e) {
				continue;
			}
		}
	}

	protected void expandTargetConnectionModel() {
		this.expandConnectionModel(targetViewer, false);
	}

	protected boolean initSourceTreeViewerProviders() {
		return this.initTreeViewerProvider(sourceViewer, sourceDataTypeID);
	}

	protected void createGraphModels(TreeItem[] items,
			Class<? extends Object> modelClass, Object[] effectiveElements) {
		for (int i = 0; i < items.length; i++) {
			TreeItem item = (TreeItem) items[i];
			if (effectiveElements != null) {
				boolean effective = false;
				for (int j = 0; j < effectiveElements.length; j++) {
					if (item.getData() == effectiveElements[j]) {
						effective = true;
						break;
					}
				}
				if (!effective)
					continue;
			}
			if (item == null)
				continue;
			Object referenceModel = item.getData(REFERENCE_MODEL);
			if (referenceModel != null && !item.isDisposed()) {
				if (!rootModel.getChildren().contains(referenceModel)) {
					rootModel.addChild(referenceModel);
				}
				((TreeItemRelationModel) referenceModel).setTreeItem(item);
				AbstractStructuredDataModel model = (AbstractStructuredDataModel) graph_trasform_data_map
						.get(item.getData());
				if (model == null) {
					graph_trasform_data_map.put(item.getData(), referenceModel);
				}
			} else {
				AbstractStructuredDataModel model = (AbstractStructuredDataModel) graph_trasform_data_map
						.get(item.getData());
				boolean needAdd = false;
				if (model == null) {
					if (modelClass == SourceModel.class) {
						model = new SourceModel();
						needAdd = true;
					}
					if (modelClass == TargetModel.class) {
						model = new TargetModel();
						needAdd = true;
					}
					if (model instanceof TreeItemRelationModel) {
						graph_trasform_data_map.put(item.getData(), model);
						model.setReferenceEntityModel(item.getData());
						((TreeItemRelationModel) model).setTreeItem(item);
						item.setData(REFERENCE_MODEL, model);
						if (needAdd)
							this.rootModel.addChild(model);
					}
				} else {
					if (!rootModel.getChildren().contains(model)) {
						rootModel.addChild(model);
					}
					((TreeItemRelationModel) model).setTreeItem(item);
				}
			}
			if (item.getExpanded() && item.getItemCount() > 0) {
				createGraphModels(item.getItems(), modelClass,
						effectiveElements);
			} else {
			}
		}
	}

	/**
	 * 
	 * @param items
	 * @param modelClass
	 */
	protected void createGraphModels(TreeItem[] items,
			Class<? extends Object> modelClass) {
		createGraphModels(items, modelClass, null);
	}

	protected void createSourceGraphModels() {
		clearExsitingGraphModels(SourceModel.class);
		if (sourceViewer == null)
			return;
		Tree tree = sourceViewer.getTree();
		TreeItem[] items = tree.getItems();
		createGraphModels(items, SourceModel.class);
	}

	private void disConnectGraphModel(Class clazz, TreeItemRelationModel model) {
		if (clazz == SourceModel.class) {
			List list = model.getModelSourceConnections();
			List temp = new ArrayList(list);
			for (Iterator iterator = temp.iterator(); iterator.hasNext();) {
				LineConnectionModel line = (LineConnectionModel) iterator
						.next();
				line.disConnect();
			}
			temp.clear();
			list.clear();
		}

		if (clazz == TargetModel.class) {
			List list = model.getModelTargetConnections();
			List temp = new ArrayList(list);
			for (Iterator iterator = temp.iterator(); iterator.hasNext();) {
				LineConnectionModel line = (LineConnectionModel) iterator
						.next();
				line.disConnect();
			}
			temp.clear();
			list.clear();
		}
	}

	/**
	 * 
	 * @param transformModel
	 */
	public boolean clearGraphModel(Object transformModel) {
		AbstractStructuredDataModel graphModel = UIUtils.findGraphModel(
				rootModel, transformModel);
		if (graphModel != null) {
			if (graphModel.getClass() == SourceModel.class) {
				List list = ((TreeItemRelationModel) graphModel)
						.getModelSourceConnections();
				List temp = new ArrayList(list);
				for (Iterator iterator = temp.iterator(); iterator.hasNext();) {
					LineConnectionModel line = (LineConnectionModel) iterator
							.next();
					DeleteConnectionCommand command = new DeleteConnectionCommand();
					command.setConnectionModel(line);
					getEditDomain().getCommandStack().execute(command);
				}
				temp.clear();
				list.clear();
			}

			if (graphModel.getClass() == TargetModel.class) {
				List list = ((TreeItemRelationModel) graphModel)
						.getModelTargetConnections();
				List temp = new ArrayList(list);
				for (Iterator iterator = temp.iterator(); iterator.hasNext();) {
					LineConnectionModel line = (LineConnectionModel) iterator
							.next();
					DeleteConnectionCommand command = new DeleteConnectionCommand();
					command.setConnectionModel(line);
					getEditDomain().getCommandStack().execute(command);
				}
				temp.clear();
				list.clear();
			}
			rootModel.removeChild(graphModel);
			deAssosiateGraphAndTransformModel(graphModel);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Remove GEF models from the root , and remove all associated connection
	 * model.
	 * <p>
	 * If the param is SourceModel.class , it will remove the SourceModel and
	 * the their connections.Same with TargetModel.class param
	 * 
	 * @param class SourceModel.class | TargetModel.class
	 */
	private void clearExsitingGraphModels(Class<? extends Object> clazz) {
		if (rootModel != null) {
			List children = rootModel.getChildren();
			List removeList = new ArrayList();
			for (Iterator iterator = children.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				if (object.getClass() == clazz) {
					removeList.add(object);
					disConnectGraphModel(clazz, (TreeItemRelationModel) object);
				}
			}
			rootModel.removeChildrenList(removeList);
			for (Iterator iterator = removeList.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				deAssosiateGraphAndTransformModel(object);
			}
		}
	}

	private void deAssosiateGraphAndTransformModel(Object graphModel) {
		if (graph_trasform_data_map.containsValue(graphModel)) {
			Iterator it = graph_trasform_data_map.keySet().iterator();
			while (it.hasNext()) {
				Object key = it.next();
				if (graphModel == graph_trasform_data_map.get(key)) {
					graph_trasform_data_map.put(key, null);
					graph_trasform_data_map.remove(key);
					break;
				}
			}
		}
	}

	protected void createTargetGraphModels() {
		clearExsitingGraphModels(TargetModel.class);
		if (targetViewer == null)
			return;
		Tree tree = targetViewer.getTree();
		TreeItem[] items = tree.getItems();
		createGraphModels(items, TargetModel.class);
	}

	protected TreeViewer createTargetTreeViewer(Composite parent, int style) {
		TreeViewer viewer = new TransformDataTreeViewer(parent, SWT.NONE
				| style, this) {
			protected void internalRefresh(Object obj) {
				super.internalRefresh(obj);
				if (obj != null)
					createGraphModels(getTree().getItems(), TargetModel.class,
							null);
			}
		};
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
		compositeSelectionProvider.addSelectionProvider(viewer);
		Control control = viewer.createControl(parent);
		control.setBackground(ColorConstants.white);
		return viewer;
	}

	public void setDirty(boolean dirty) {
		commandStackChanged = dirty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormPage#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return commandStackChanged;
	}

	public InputStream generateSmooksContents(IProgressMonitor monitor)
			throws SmooksAnalyzerException, IOException, CoreException {
		SmooksFileBuilder builder = this.getSmooksFileBuilder();
		builder.setSmooksResource(this.smooksResource);
		SmooksConfigurationFileGenerateContext context = this
				.getSmooksConfigurationFileGenerateContext();
		this.cleanMappingResourceConfig();
		return builder.generateSmooksFile(context, monitor);
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
		if (!canSaveFile) {
			boolean cleanError = MessageDialog
					.openQuestion(
							getSite().getShell(),
							Messages
									.getString("SmooksGraphicalFormPage.CleanErrorsDialogTitle"), //$NON-NLS-1$
							Messages
									.getString("SmooksGraphicalFormPage.CleanErrorsDialogContents1") //$NON-NLS-1$
									+ Messages
											.getString("SmooksGraphicalFormPage.CleanErrorsDialogContents2")); //$NON-NLS-1$
			if (cleanError)
				return;
		}
		Exception exp = null;
		try {
			// generate smooks configuration file
			InputStream stream = generateSmooksContents(monitor);
			IFile file = ((IFileEditorInput) this.getEditorInput()).getFile();
			if (file.exists()) {
				file.setContents(stream, IResource.FORCE, monitor);
				file.refreshLocal(IResource.DEPTH_ZERO, monitor);
			}

			// save graphical informations
			if (this.graphicalInformationSaver != null) {
				SmooksConfigurationFileGenerateContext context = new SmooksConfigurationFileGenerateContext();
				initSmooksConfigurationFileGenerateContext(context);
				context.getProperties().clear();
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
			ErrorDialog
					.openError(
							getSite().getShell(),
							Messages
									.getString("SmooksGraphicalFormPage.SaveErrorDlgTitle"), //$NON-NLS-1$
							Messages
									.getString("SmooksGraphicalFormPage.SaveErrorDlgContent"), UIUtils //$NON-NLS-1$
									.createErrorStatus(exp));
		}
		super.doSave(monitor);
		commandStackChanged = false;
		getManagedForm().dirtyStateChanged();
	}

	protected SmooksConfigurationFileGenerateContext createContext() {
		SmooksConfigurationFileGenerateContext context = new SmooksConfigurationFileGenerateContext();
		return context;
	}

	/**
	 * Init the Smooks context. The context will be used by IAnalyzer to save or
	 * parse the Smooks configuration file.
	 * <p>
	 * 
	 * @param context
	 */
	protected void initSmooksConfigurationFileGenerateContext(
			SmooksConfigurationFileGenerateContext context) {
		context.setSourceDataTypeID(this.sourceDataTypeID);
		context.setTargetDataTypeID(this.targetDataTypeID);
		context.setSmooksType(SmooksModelConstants.SAX);
		context.setGraphInformations(graphinformations);
		context.setDataMappingRootModel(this.rootModel);
		context.setSmooksConfigFile(((IFileEditorInput) getEditorInput())
				.getFile());
		context.setSmooksGraphcalPage(this);
		context.setGefDomain(getEditDomain());
		List contents = this.smooksResource.getContents();
		if (contents.size() > 0) {
			DocumentRoot documentRoot = (DocumentRoot) contents.get(0);
			context.setSmooksResourceListModel(documentRoot
					.getSmooksResourceList());
		}
		context.setDomain(getEditingDomain());
		if (sourceViewer != null) {
			context.setSourceViewerLabelProvider((LabelProvider) sourceViewer
					.getLabelProvider());
			context
					.setSourceViewerContentProvider((ITreeContentProvider) sourceViewer
							.getContentProvider());
		}
		if (targetViewer != null) {
			context.setTargetViewerLabelProvider((LabelProvider) targetViewer
					.getLabelProvider());
			context
					.setTargetViewerContentProvider((ITreeContentProvider) targetViewer
							.getContentProvider());
		}

		context.setShell(getSite().getShell());
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
		// getSite().setSelectionProvider(getGraphicalViewer());
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
						analyzeDesignGraph();
						updateSelectionActions();
						IManagedForm form = getManagedForm();
						if (form != null) {
							form.dirtyStateChanged();
						}
					}

				});
	}

	/**
	 * 
	 */
	protected void createConnectionModels() {
		if (rootModel == null)
			return;

		List<SourceModel> sourceModelList = rootModel.loadSourceModelList();
		List<TargetModel> targetModelList = rootModel.loadTargetModelList();

		for (Iterator iterator = sourceModelList.iterator(); iterator.hasNext();) {
			SourceModel sourceModel = (SourceModel) iterator.next();
			for (Iterator iterator2 = targetModelList.iterator(); iterator2
					.hasNext();) {
				TargetModel targetModel = (TargetModel) iterator2.next();
				MappingModel mapping = getMappingModel(sourceModel, targetModel);
				if (mapping != null) {
					LineConnectionModel connection = new LineConnectionModel(
							sourceModel, targetModel);
					connection.setProperties(mapping.getProperties());
				}
			}
		}
		List temp = new ArrayList(targetModelList);
		for (Iterator iterator = targetModelList.iterator(); iterator.hasNext();) {
			TargetModel targetModel = (TargetModel) iterator.next();
			for (Iterator iterator2 = temp.iterator(); iterator2.hasNext();) {
				TargetModel targetModel1 = (TargetModel) iterator2.next();
				MappingModel mapping = getMappingModel(targetModel,
						targetModel1);
				if (mapping != null) {
					LineConnectionModel connection = new LineConnectionModel(
							targetModel, targetModel1);
					connection.setProperties(mapping.getProperties());
				}
			}
		}
	}

	protected MappingModel getMappingModel(TreeItemRelationModel source,
			TreeItemRelationModel target) {
		Object sourceReferModel = source.getReferenceEntityModel();
		Object targetReferModel = target.getReferenceEntityModel();
		if (mappingModelList == null)
			return null;
		for (Iterator iterator = mappingModelList.iterator(); iterator
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
		return gefEditDomain;
	}

	public void setEditDomain(DefaultEditDomain editDomain) {
		this.gefEditDomain = editDomain;
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

	protected Resource getSmooksResource() {
		return smooksResource;
	}

	protected void analyzeGraphicalModel(SmooksResourceListType listType,
			GraphInformations graph, IFile file) throws IOException,
			CoreException, InvocationTargetException {
		ISourceModelAnalyzer sourceModelAnalyzer = AnalyzerFactory
				.getInstance().getSourceModelAnalyzer(sourceDataTypeID);
		ITargetModelAnalyzer targetModelAnalyzer = AnalyzerFactory
				.getInstance().getTargetModelAnalyzer(targetDataTypeID);
		IMappingAnalyzer connectionAnalyzer = AnalyzerFactory.getInstance()
				.getMappingAnalyzer(sourceDataTypeID, targetDataTypeID);
		if (sourceModelAnalyzer != null && sourceTreeViewerInputModel == null)
			sourceTreeViewerInputModel = sourceModelAnalyzer
					.buildSourceInputObjects(graph, listType, file,
							sourceViewer);
		if (targetModelAnalyzer != null && targetTreeViewerInputModel == null)
			targetTreeViewerInputModel = targetModelAnalyzer
					.buildTargetInputObjects(graph, listType, file,
							targetViewer);
		if (connectionAnalyzer != null) {
			mappingResourceConfigList = connectionAnalyzer
					.analyzeMappingSmooksModel(listType,
							sourceTreeViewerInputModel,
							targetTreeViewerInputModel);
			if (mappingResourceConfigList != null) {
				mappingModelList = mappingResourceConfigList
						.getMappingModelList();
			}
		}
	}

	protected void initTransformViewerModel(IEditorSite site, IEditorInput input)
			throws Throwable {
		graphicalInformationSaver = new GraphicalInformationSaver(input);

		try {
			if (graphinformations == null) {
				graphinformations = graphicalInformationSaver.doLoad();
			}
			initFormEditorWithGraphInfo(graphinformations);
			initSmooksContext(graphinformations, this
					.getSmooksConfigurationFileGenerateContext());
		} catch (Throwable t) {
			// ignore
		}
		IFile file = ((IFileEditorInput) input).getFile();
		// if the type id is null, open a dialog to select
		if (sourceDataTypeID == null || targetDataTypeID == null) {
			TypeIDSelectionWizard wizard = new TypeIDSelectionWizard();
			wizard.setSourceDataTypeID(sourceDataTypeID);
			wizard.setTargetDataTypeID(targetDataTypeID);
			WizardDialog dialog = new WizardDialog(getSite().getShell(), wizard);
			if (dialog.open() == org.eclipse.jface.dialogs.Dialog.OK) {
				sourceDataTypeID = wizard.getSourceDataTypeID();
				targetDataTypeID = wizard.getTargetDataTypeID();

				SmooksConfigurationFileGenerateContext context = getSmooksConfigurationFileGenerateContext();
				context.setSourceDataTypeID(sourceDataTypeID);
				context.setTargetDataTypeID(targetDataTypeID);
				sourceTreeViewerInputModel = selectSourceDataSource(
						sourceDataTypeID, context);
				targetTreeViewerInputModel = selectTargetDataSource(
						targetDataTypeID, context);
				graphicalInformationSaver.doSave(new NullProgressMonitor(),
						context);
			}
		}
		smooksResource = this.getSmooksResource();
		if (smooksResource != null) {
			smooksResource.load(Collections.EMPTY_MAP);
			if (smooksResource.getContents().isEmpty())
				return;
			SmooksResourceListType listType = ((DocumentRoot) smooksResource
					.getContents().get(0)).getSmooksResourceList();
			checkSmooksConfigFileModel(listType);
			this.analyzeGraphicalModel(listType, graphinformations, file);
		}
	}

	/**
	 * 
	 * @param listType
	 */
	protected void checkSmooksConfigFileModel(SmooksResourceListType listType) {
		List<AbstractResourceConfig> list = listType
				.getAbstractResourceConfig();
		ResourceConfigType globalParameterResource = null;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			AbstractResourceConfig arc = (AbstractResourceConfig) iterator
					.next();
			if (arc instanceof ResourceConfigType) {
				String selector = ((ResourceConfigType) arc).getSelector();
				if (selector == null)
					continue;
				selector = selector.trim();
				if (SmooksModelConstants.GLOBAL_PARAMETERS.equals(selector)) {
					globalParameterResource = (ResourceConfigType) arc;
					break;
				}
			}
		}

		if (globalParameterResource == null) {
			globalParameterResource = SmooksFactory.eINSTANCE
					.createResourceConfigType();
			globalParameterResource
					.setSelector(SmooksModelConstants.GLOBAL_PARAMETERS);
			SmooksModelUtils.setParamText("stream.filter.type", "SAX",
					globalParameterResource);
		} else {
			String value = SmooksModelUtils.getParmaText("stream.filter.type",
					globalParameterResource);
			if (value == null) {
				SmooksModelUtils.setParamText("stream.filter.type", "SAX",
						globalParameterResource);
			} else {
				if (value.trim().length() == 0) {
					SmooksModelUtils.setParamText("stream.filter.type", "SAX",
							globalParameterResource);
				}
			}
		}
	}

	public void analyzeGraphicalModel(InputStream stream) throws IOException,
			CoreException, InvocationTargetException {
		Resource resource = new SmooksResourceFactoryImpl()
				.createResource(null);
		resource.load(stream, Collections.EMPTY_MAP);
		SmooksResourceListType listType = ((DocumentRoot) resource
				.getContents().get(0)).getSmooksResourceList();
		if (smooksResource != null) {
			smooksResource.getContents().clear();
			smooksResource.getContents().add(listType.eContainer());
		}
		// GraphInformations graph = null;
		// if (graphicalInformationSaver != null)
		// graph = graphicalInformationSaver.doLoad();
		this.analyzeGraphicalModel(listType, graphinformations,
				((IFileEditorInput) getEditorInput()).getFile());
	}

	private boolean requiredSelectDataSource(String typeID) {
		for (int i = 0; i < this.REQUIRED_SOURCE_SELECT_TYPE.length; i++) {
			String s = REQUIRED_SOURCE_SELECT_TYPE[i];
			if (s.equals(typeID))
				return true;
		}
		return false;
	}

	private Object selectTargetDataSource(String typeID,
			SmooksConfigurationFileGenerateContext context) {
		if (requiredSelectDataSource(typeID)) {
			IStructuredDataCreationWizard wizard1 = ViewerInitorStore
					.getInstance().getStructuredDataCreationWizard(typeID);
			WizardDialog dialog1 = new WizardDialog(getSite().getShell(),
					wizard1);
			((Wizard) wizard1).setWindowTitle("Target Data Selection");
			if (dialog1.open() == Dialog.OK) {
				context.getProperties().put("targetDataPath",
						wizard1.getStructuredDataSourcePath());
			}
			return wizard1.getTreeViewerInputContents();
		}
		return null;
	}

	private Object selectSourceDataSource(String typeID,
			SmooksConfigurationFileGenerateContext context) {
		if (requiredSelectDataSource(typeID)) {
			IStructuredDataCreationWizard wizard1 = ViewerInitorStore
					.getInstance().getStructuredDataCreationWizard(typeID);
			WizardDialog dialog1 = new WizardDialog(getSite().getShell(),
					wizard1);
			((Wizard) wizard1).setWindowTitle("Source Data Selection");
			if (dialog1.open() == Dialog.OK) {
				context.getProperties().put("sourceDataPath",
						wizard1.getStructuredDataSourcePath());
			}
			return wizard1.getTreeViewerInputContents();
		}
		return null;
	}

	protected void initSmooksContext(GraphInformations graph,
			SmooksConfigurationFileGenerateContext context) {
		Params params = graph.getParams();
		if (params != null) {
			List list = params.getParam();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Param p = (Param) iterator.next();
				String name = p.getName();
				String value = p.getValue();
				if (name != null && value != null)
					context.putProperty(name, value);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormPage#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		FormEditor parentEditor = this.getEditor();
		if (parentEditor instanceof SmooksFormEditor) {
			editingDomain = ((SmooksFormEditor) parentEditor)
					.getEditingDomain();
			smooksResource = ((SmooksFormEditor) parentEditor)
					.getSmooksResource();
		}
		if (input instanceof SmooksFileEditorInput) {
			this.sourceTreeViewerInputModel = ((SmooksFileEditorInput) input)
					.getSourceTreeViewerInputContents();

			this.targetTreeViewerInputModel = ((SmooksFileEditorInput) input)
					.getTargetTreeViewerInputContents();
		}
	}

	/**
	 * 
	 * @param error
	 *            . The exception occurs when analyze the smooks config file.
	 *            <p>
	 *            If the param is NULL means that there doesn't occur exception
	 *            during analyze config file.
	 */
	protected void notifyAnalyzeListeners(Throwable error) {
		AnalyzeResult result = new AnalyzeResult();
		result.setSourceEdtior(this);
		result.setError(error);
		for (Iterator<IAnalyzeListener> iterator = this.analyzeListenerList
				.iterator(); iterator.hasNext();) {
			IAnalyzeListener listener = (IAnalyzeListener) iterator.next();
			listener.endAnalyze(result);
		}
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

	public void replaceInputDataPathData(String pathName, String value) {
		if (graphinformations != null) {
			Params params = graphinformations.getParams();
			if (params == null) {
				return;
			}
			Param param = null;
			List list = params.getParam();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Param p = (Param) iterator.next();
				if (pathName.equals(p.getName())) {
					param = p;
					break;
				}
			}
			if (param == null) {
				param = GraphicalFactory.eINSTANCE.createParam();
				params.getParam().add(param);
				param.setName(pathName);
			}
			param.setValue(value);
		}
	}

	/**
	 * It's a very important method
	 * <p>
	 * If call the method , there will open the data selection wizard to allow
	 * user select new data ,
	 * <p>
	 * when user select the new data , the connections will be removed.
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
		if (dialog.open() == WizardDialog.OK) {
			IStructuredDataCreationWizard cw = dialog
					.getCurrentCreationWizard();
			this.getSmooksConfigurationFileGenerateContext().addProperties(
					cw.getProperties());
			String typeID = cw.getInputDataTypeID();
			if (viewer.getInput() != null) {
				if (!MessageDialog
						.openQuestion(
								getSite().getShell(),
								Messages
										.getString("SmooksGraphicalFormPage.ReselectViewerContentDlgTitle"), //$NON-NLS-1$
								Messages
										.getString("SmooksGraphicalFormPage.ReselectViewerContentDlgContent"))) { //$NON-NLS-1$
					return;
				}
			}
			if (UIUtils.setTheProvidersForTreeViewer(viewer, typeID)) {
				Object obj = cw.getTreeViewerInputContents();
				viewer.setInput(obj);
				try {
					if (viewer == this.sourceViewer) {
						this.createSourceGraphModels();
						replaceInputDataPathData(
								SmooksConfigFileNewWizard.PRO_SOURCE_DATA_PATH,
								cw.getStructuredDataSourcePath());
						sourceDataTypeID = typeID;
					}
					if (viewer == this.targetViewer) {
						this.createTargetGraphModels();
						targetDataTypeID = typeID;
						replaceInputDataPathData(
								SmooksConfigFileNewWizard.PRO_TARGET_DATA_PATH,
								cw.getStructuredDataSourcePath());
					}
					commandStackChanged = true;
					firePropertyChange(PROP_DIRTY);
				} catch (Exception e) {
					MessageDialog
							.openError(
									getSite().getShell(),
									Messages
											.getString("SmooksGraphicalFormPage.FillViewerErrorTitle"), //$NON-NLS-1$
									Messages
											.getString("SmooksGraphicalFormPage.FillViewerErrorContent") //$NON-NLS-1$
											+ e.toString());
				}
			} else {
				MessageDialog
						.openError(
								getSite().getShell(),
								Messages
										.getString("SmooksGraphicalFormPage.FillViewerErrorTitle"), //$NON-NLS-1$
								Messages
										.getString("SmooksGraphicalFormPage.FillViewerErrorContent")); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Clean the source and target treeviewer , and remove all items on the
	 * graphical panel (middle panel).
	 */
	public void cleanMappingPanel() {
		if (sourceViewer != null && targetViewer != null) {
			sourceViewer.setInput(Collections.EMPTY_LIST);
			targetViewer.setInput(Collections.EMPTY_LIST);
		}
		clearExsitingGraphModels(SourceModel.class);
		clearExsitingGraphModels(TargetModel.class);
	}

	public void redrawMappingPanel() {
		this.createSourceGraphModels();
		this.createTargetGraphModels();
		createConnectionModels();
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
		 * @see
		 * org.eclipse.ui.forms.events.IHyperlinkListener#linkActivated(org.
		 * eclipse.ui.forms.events.HyperlinkEvent)
		 */
		public void linkActivated(HyperlinkEvent e) {
			showCreationWizard(viewer);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.forms.events.IHyperlinkListener#linkEntered(org.eclipse
		 * .ui.forms.events.HyperlinkEvent)
		 */
		public void linkEntered(HyperlinkEvent e) {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.forms.events.IHyperlinkListener#linkExited(org.eclipse
		 * .ui.forms.events.HyperlinkEvent)
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
		 * @see
		 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets
		 * .Event)
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
		 * @see
		 * org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt
		 * .events.PaintEvent)
		 */
		public void paintControl(PaintEvent e) {
			Tree tree = (Tree) e.getSource();
			Class modelClass = TargetModel.class;
			if (tree == sourceViewer.getTree()) {
				modelClass = SourceModel.class;
			}
			createGraphModels(tree.getItems(), modelClass);
			rootModel.firePropertyChange(
					AbstractStructuredDataModel.P_REFRESH_PANEL, null,
					new Object());
		}

	}

	public void selectionChanged(SelectionChangedEvent event) {
		this.selection = event.getSelection();
		if (compositeSelectionProvider != null) {
			compositeSelectionProvider.setSelection(selection);
		}
		updateSelectionActions();
		updataViewerAction(event);
	}

	private void updataViewerAction(SelectionChangedEvent event) {
		boolean isSource = false;
		if (event.getSource() == sourceViewer) {
			isSource = true;
		}
		ISelection selection = null;
		String typeID = null;
		if (isSource) {
			typeID = getSourceDataTypeID();
			selection = sourceViewer.getSelection();
		} else {
			typeID = getTargetDataTypeID();
			selection = targetViewer.getSelection();
		}
		if (typeID != null) {
			IViewerActionsProvider provider = ActionProviderManager
					.getInstance().getActionProvider(typeID);
			if (provider != null) {
				List<ISmooksAction> list = provider.getActionList();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					ISmooksAction smooksAction = (ISmooksAction) iterator
							.next();
					if (smooksAction instanceof SmooksAction) {
						((SmooksAction) smooksAction).setViewer(event
								.getSource());
					}
					smooksAction.selectionChanged(selection);
				}
			}
		}
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
		initSmooksConfigurationFileGenerateContext(smooksConfigurationFileGenerateContext);
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

	public void commandStackChanged(EventObject event) {
		commandStackChanged = true;
		analyzeDesignGraph();
		updateSelectionActions();
		getManagedForm().dirtyStateChanged();
	}

	protected void updateErrorMessage() {
		boolean hasProblems = false;
		for (Iterator<DesignTimeAnalyzeResult> iterator = this.analyzeResultList
				.iterator(); iterator.hasNext();) {
			DesignTimeAnalyzeResult result = (DesignTimeAnalyzeResult) iterator
					.next();
			if (result.getErrorMessage() != null) {
				hasProblems = true;
				if (canSaveFile)
					canSaveFile = false;
				Label imageLabel = new Label(designTimeAnalyzeResultRegion,
						SWT.NONE);
				GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
				imageLabel.setLayoutData(gd);
				imageLabel.setImage(SmooksUIActivator.getDefault()
						.getImageRegistry().get(
								SmooksGraphConstants.IMAGE_ERROR));

				Composite fixComposite = new Composite(
						designTimeAnalyzeResultRegion, SWT.NONE);
				GridLayout gl = new GridLayout();
				gl.numColumns = 2;
				gl.makeColumnsEqualWidth = false;
				gl.marginHeight = 0;
				gl.marginWidth = 0;
				fixComposite.setLayout(gl);
				Label notifyLabel = new Label(fixComposite, SWT.NONE);
				Hyperlink fixLink = getManagedForm().getToolkit()
						.createHyperlink(fixComposite, "(Fix it)", SWT.NONE);
				final Menu menu = new Menu(getSite().getShell(), SWT.POP_UP);
				List<ResolveCommand> list = result.getResolveProblem();
				for (Iterator<ResolveCommand> iterator2 = list.iterator(); iterator2
						.hasNext();) {
					final ResolveCommand resolveCommand = (ResolveCommand) iterator2
							.next();
					MenuItem item = new MenuItem(menu, SWT.NONE);
					item.addSelectionListener(new SelectionListener() {

						public void widgetDefaultSelected(SelectionEvent arg0) {
							widgetSelected(arg0);
						}

						public void widgetSelected(SelectionEvent arg0) {
							try {
								resolveCommand.execute();
								commandStackChanged = true;
								analyzeDesignGraph();
								firePropertyChange(PROP_DIRTY);
							} catch (Exception e) {
								UIUtils.showErrorDialog(getSite().getShell(),
										UIUtils.createErrorStatus(e));
							}
						}

					});
					item.setText(resolveCommand.getResolveDescription());
					item.setImage(resolveCommand.getImage());
				}
				fixLink.addHyperlinkListener(new IHyperlinkListener() {

					public void linkActivated(HyperlinkEvent e) {
						menu.setLocation(getSite().getShell().getDisplay()
								.getCursorLocation());
						menu.setVisible(true);
					}

					public void linkEntered(HyperlinkEvent e) {
					}

					public void linkExited(HyperlinkEvent e) {
					}

				});
				notifyLabel.setMenu(menu);
				GridData nlgd = new GridData();
				notifyLabel.setLayoutData(nlgd);
				nlgd = new GridData(GridData.FILL_HORIZONTAL);
				fixComposite.setLayoutData(nlgd);
				notifyLabel.setText(result.getErrorMessage());
			}
		}
		// GridData gd = new GridData(GridData.FILL_BOTH);

		if (analyzeResultList.size() == 0) {
			// gd.widthHint = 0;
		}
		try {
			// designTimeAnalyzeResultRegion.setLayoutData(gd);
			if (hasProblems) {
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				problemSection.setVisible(true);
				problemSection.setLayoutData(gd);
			} else {
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.exclude = true;
				problemSection.setLayoutData(gd);
				gd.heightHint = 0;
				problemSection.setVisible(false);
			}
			designTimeAnalyzeResultRegion.getParent().getParent().layout();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	protected void updateWarningMessage() {

	}

	protected void analyzeDesignGraph() {
		try {
			IMappingAnalyzer analyzer = AnalyzerFactory.getInstance()
					.getMappingAnalyzer(sourceDataTypeID, targetDataTypeID);
			if (analyzer == null)
				return;
			SmooksConfigurationFileGenerateContext context = this
					.createContext();
			this.initSmooksConfigurationFileGenerateContext(context);
			DesignTimeAnalyzeResult[] results = analyzer
					.analyzeGraphModel(context);
			analyzeResultList.clear();
			if (results != null) {
				for (int i = 0; i < results.length; i++) {
					analyzeResultList.add(results[i]);
				}
			}
			updateNotifyMessage();
		} catch (CoreException e) {
			UIUtils.showErrorDialog(getSite().getShell(), UIUtils
					.createErrorStatus(e));
		}
	}

	protected void updateNotifyMessage() {
		canSaveFile = true;
		Control[] children = designTimeAnalyzeResultRegion.getChildren();
		for (int i = 0; i < children.length; i++) {
			Control c = children[i];
			c.setVisible(false);
			c.dispose();
			c = null;
		}
		updateErrorMessage();
		try {
			designTimeAnalyzeResultRegion.layout(true);
		} catch (Exception e) {
			// ignore
			e.printStackTrace();
		}
	}

	/**
	 * @return the editingDomain
	 */
	protected AdapterFactoryEditingDomain getEditingDomain() {
		if (editingDomain == null) {
			FormEditor parentEditor = this.getEditor();
			editingDomain = ((SmooksFormEditor) parentEditor)
					.getEditingDomain();
			editingDomain.getCommandStack().addCommandStackListener(this);
		}
		return editingDomain;
	}

	/**
	 * @param editingDomain
	 *            the editingDomain to set
	 */
	protected void setEditingDomain(AdapterFactoryEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	public void endSave(SaveResult result) {
		IEditorPart editor = result.getSourceEdtior();
		if (editor instanceof StructuredTextEditor) {
			this.refreshAllGUI();
		}
	}

	public void preSave(SaveResult result) {

	}

	private void fillMenuManager(IMenuManager menuManager, boolean isSource) {
		String typeID = null;
		if (isSource) {
			typeID = getSourceDataTypeID();
		} else {
			typeID = getTargetDataTypeID();
		}
		if (typeID != null) {
			IViewerActionsProvider provider = ActionProviderManager
					.getInstance().getActionProvider(typeID);
			if (provider != null) {
				List<ISmooksAction> list = provider.getActionList();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					ISmooksAction smooksAction = (ISmooksAction) iterator
							.next();
					smooksAction
							.setSmooksContext(getSmooksConfigurationFileGenerateContext());
					if (isSource) {
						if (smooksAction instanceof SmooksAction) {
							((SmooksAction) smooksAction)
									.setViewer(sourceViewer);
						}
						smooksAction.selectionChanged(sourceViewer
								.getSelection());
					} else {
						if (smooksAction instanceof SmooksAction) {
							((SmooksAction) smooksAction)
									.setViewer(targetViewer);
						}
						smooksAction.selectionChanged(targetViewer
								.getSelection());
					}
					menuManager.add(smooksAction);
				}
			}
		}
	}
}
