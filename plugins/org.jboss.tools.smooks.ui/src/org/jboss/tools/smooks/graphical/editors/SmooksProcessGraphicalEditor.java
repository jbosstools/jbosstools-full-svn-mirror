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
package org.jboss.tools.smooks.graphical.editors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandWrapper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.ScrolledPageBook;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.editor.AbstractSmooksFormEditor;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.editor.ISourceSynchronizeListener;
import org.jboss.tools.smooks.graphical.actions.AbstractProcessGraphAction;
import org.jboss.tools.smooks.graphical.actions.AddInputTaskAction;
import org.jboss.tools.smooks.graphical.actions.AddNextTaskNodeAction;
import org.jboss.tools.smooks.graphical.actions.AddPreviousTaskNodeAction;
import org.jboss.tools.smooks.graphical.actions.AddTaskNodeAction;
import org.jboss.tools.smooks.graphical.actions.DeleteTaskNodeAction;
import org.jboss.tools.smooks.graphical.actions.TaskTypeRules;
import org.jboss.tools.smooks.graphical.editors.TaskTypeManager.TaskTypeDescriptor;
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.ProcessType;
import org.jboss.tools.smooks.model.graphics.ext.ProcessesType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart
 * 
 */
public class SmooksProcessGraphicalEditor extends FormPage implements ISelectionChangedListener,
		ISourceSynchronizeListener, ISmooksGraphChangeListener, IPropertyListener {

	private List<IAction> processPanelActions = new ArrayList<IAction>();

	public static final int EXECUTE_COMMAND = 0;

	public static final int REDO_COMMAND = 1;

	public static final int UNDO_COMMAND = 2;

	private Object emptyKey = new Object();

	private ISmooksModelProvider smooksModelProvider = null;

	private GraphViewer processGraphViewer;

	private ScrolledPageBook pageBook;

	private Map<String, Object> registedTaskPages = new HashMap<String, Object>();

	public SmooksProcessGraphicalEditor(FormEditor editor, String id, String title, ISmooksModelProvider provider) {
		super(editor, id, title);
		this.smooksModelProvider = provider;
	}

	public SmooksProcessGraphicalEditor(String id, String title, ISmooksModelProvider provider) {
		super(id, title);
		this.smooksModelProvider = provider;
	}

	protected void createProcessGraphicalPanel(Composite parent) {
		processGraphViewer = new GraphViewer(parent, SWT.NONE);
		// GridData gd = new GridData(GridData.FILL_BOTH);
		// processGraphViewer.getControl().setLayoutData(gd);
		processGraphViewer.setNodeStyle(ZestStyles.NODES_FISHEYE);
		processGraphViewer.setContentProvider(new ProcessGraphContentProvider());
		processGraphViewer.setLabelProvider(new LabelProvider() {

			@Override
			public Image getImage(Object element) {
				if (element instanceof TaskType) {
					String id = ((TaskType) element).getId();
					List<TaskTypeDescriptor> des = TaskTypeManager.getAllTaskList();
					for (Iterator<?> iterator = des.iterator(); iterator.hasNext();) {
						TaskTypeDescriptor taskTypeDescriptor = (TaskTypeDescriptor) iterator.next();
						if (taskTypeDescriptor.getId().equals(id)) {
							return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
									taskTypeDescriptor.getImagePath());
						}
					}
				}
				return super.getImage(element);
			}

			@Override
			public String getText(Object element) {
				if (element instanceof TaskType) {
					String id = ((TaskType) element).getId();
					String name = ((TaskType) element).getName();
					if (name == null) {
						name = id;
					}
					if (name == null) {
						name = "null";
					}
					return name;
				}
				return "";
			}

		});
		processGraphViewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		HorizontalTreeLayoutAlgorithm layoutAlgorithm = new HorizontalTreeLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		processGraphViewer.setLayoutAlgorithm(layoutAlgorithm, true);
		if (parent instanceof Section) {
			((Section) parent).setClient(processGraphViewer.getControl());
		}
	}

	protected void initProcessGraphicalViewer() {
		SmooksGraphicsExtType ext = this.smooksModelProvider.getSmooksGraphicsExt();
		ProcessesType processes = ext.getProcesses();
		boolean disableProcessViewer = false;
		if (processes != null) {
			ProcessType process = processes.getProcess();
			if (process != null) {
				getProcessGraphViewer().getControl().setBackground(
						getManagedForm().getToolkit().getColors().getBackground());
				getProcessGraphViewer().getControl().setEnabled(true);
				getProcessGraphViewer().setInput(process);
			} else {
				disableProcessViewer = true;
			}
		} else {
			disableProcessViewer = true;
		}
		if (disableProcessViewer) {
			getProcessGraphViewer().getControl().setBackground(
					getManagedForm().getToolkit().getColors().getBorderColor());
			getProcessGraphViewer().setInput(null);
			getProcessGraphViewer().getControl().setEnabled(false);
		}
	}

	protected void hookProcessGraphicalViewer() {

		getProcessGraphViewer().getControl();
		getProcessGraphViewer().addDropSupport(DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK,
				new Transfer[] { TemplateTransfer.getInstance() }, new DropTargetListener() {
					private TaskType taskType = null;

					private ProcessType process = null;

					public void dropAccept(DropTargetEvent event) {

					}

					public void drop(DropTargetEvent event) {
						if (event.detail == DND.DROP_COPY) {
							if (this.taskType != null) {
								TaskTypeDescriptor des = (TaskTypeDescriptor) TemplateTransfer.getInstance()
										.getTemplate();
								AddNextTaskNodeAction action = new AddNextTaskNodeAction(des.getId(), des.getLabel(),
										smooksModelProvider);
								TaskType taskType = this.taskType;
								IStructuredSelection selection = new StructuredSelection(taskType);
								action.selectionChanged(new SelectionChangedEvent(getProcessGraphViewer(), selection));
								action.run();
								return;
							}
							if (this.process != null) {
								AddNextTaskNodeAction action = new AddInputTaskAction(smooksModelProvider);
								// IStructuredSelection selection = new
								// StructuredSelection(taskType);
								// action.selectionChanged(new
								// SelectionChangedEvent(getProcessGraphViewer(),
								// selection));
								action.run();
								return;
							}
						}
					}

					public void dragOver(DropTargetEvent event) {
						Control control = getProcessGraphViewer().getControl();
						if (control != null && control instanceof Graph) {
							Graph graph = (Graph) control;
							Point pp = graph.toControl(new Point(event.x, event.y));
							TaskTypeDescriptor des = (TaskTypeDescriptor) TemplateTransfer.getInstance().getTemplate();
							TaskType testType = GraphFactory.eINSTANCE.createTaskType();
							testType.setId(des.getId());
							IFigure figure = graph.getFigureAt(pp.x, pp.y);
							if (figure == null) {
								if (testType.getId().equals(TaskTypeManager.TASK_ID_INPUT)) {
									ProcessType process = (ProcessType) getProcessGraphViewer().getInput();
									if (process.getTask().isEmpty()) {
										event.detail = DND.DROP_COPY;
										this.process = process;
										return;
									}
								}
								event.detail = DND.DROP_NONE;
								this.taskType = null;
								process = null;
								return;
							}
							List<?> nodes = graph.getNodes();
							for (Iterator<?> iterator = nodes.iterator(); iterator.hasNext();) {
								Object object = (Object) iterator.next();
								if (object instanceof GraphNode) {
									IFigure f = ((GraphNode) object).getNodeFigure();
									if (figure == f) {
										TaskTypeRules rules = new TaskTypeRules();
										if (rules.isNextTask((TaskType) ((GraphNode) object).getData(), testType)) {
											event.detail = DND.DROP_COPY;
											this.taskType = (TaskType) ((GraphNode) object).getData();
											return;
										}
									}
								}
							}
							event.detail = DND.DROP_NONE;
							this.taskType = null;
							this.process = null;
						}
					}

					public void dragOperationChanged(DropTargetEvent event) {
					}

					public void dragLeave(DropTargetEvent event) {
					}

					public void dragEnter(DropTargetEvent event) {
						event.detail = DND.DROP_MOVE;
						this.taskType = null;
						process = null;
					}
				});
		getProcessGraphViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object firstElement = selection.getFirstElement();
				showTaskControl(firstElement);
			}
		});
	}

	protected void configProcessGraphicalViewer() {
		MenuManager manager = new MenuManager();

		initProcessGraphicalPanelActions(manager);

		Menu menu = manager.createContextMenu(getProcessGraphViewer().getControl());
		getProcessGraphViewer().getControl().setMenu(menu);
		manager.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				for (Iterator<?> iterator = processPanelActions.iterator(); iterator.hasNext();) {
					IAction a = (IAction) iterator.next();
					if (a instanceof AbstractProcessGraphAction) {
						((AbstractProcessGraphAction) a).selectionChanged(new SelectionChangedEvent(processGraphViewer,
								processGraphViewer.getSelection()));
					}
				}
				manager.update();
			}
		});
	}

	protected void initProcessGraphicalPanelActions(IMenuManager manager) {

		AddTaskNodeAction addInputTaskAction = new AddInputTaskAction(smooksModelProvider);
		manager.add(addInputTaskAction);
		processPanelActions.add(addInputTaskAction);

		MenuManager addNextTaskMenuManager = new MenuManager("Add Next Task");
		manager.add(addNextTaskMenuManager);

		fillNextTaskMenu(addNextTaskMenuManager);

		MenuManager addPreTaskMenuManager = new MenuManager("Add Previous Task");
		manager.add(addPreTaskMenuManager);

		fillPreTaskMenu(addPreTaskMenuManager);

		DeleteTaskNodeAction deleteAction = new DeleteTaskNodeAction(smooksModelProvider);
		manager.add(deleteAction);

		this.processPanelActions.add(deleteAction);

	}

	private void fillNextTaskMenu(MenuManager addNextTaskMenuManager) {
		List<TaskTypeDescriptor> list = TaskTypeManager.getAllTaskList();
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			TaskTypeDescriptor taskTypeDescriptor = (TaskTypeDescriptor) iterator.next();
			AddNextTaskNodeAction addNextInputAction = new AddNextTaskNodeAction(taskTypeDescriptor.getId(),
					taskTypeDescriptor.getLabel(), smooksModelProvider);
			this.processPanelActions.add(addNextInputAction);
			addNextTaskMenuManager.add(addNextInputAction);
		}
	}

	private void fillPreTaskMenu(MenuManager addPreTaskMenuManager) {
		List<TaskTypeDescriptor> list = TaskTypeManager.getAllTaskList();
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			TaskTypeDescriptor taskTypeDescriptor = (TaskTypeDescriptor) iterator.next();
			AddPreviousTaskNodeAction addNextInputAction = new AddPreviousTaskNodeAction(taskTypeDescriptor.getId(),
					taskTypeDescriptor.getLabel(), smooksModelProvider);
			this.processPanelActions.add(addNextInputAction);
			addPreTaskMenuManager.add(addNextInputAction);
		}
	}

	public void registeTaskDetailsPage(IEditorPart editor, String taskID) {
		editor.addPropertyListener(this);
		this.registedTaskPages.put(taskID, editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.editor.FormPage#createPartControl(org.eclipse.swt
	 * .widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		super.createPartControl(parent);
	}

	public GraphViewer getProcessGraphViewer() {
		return processGraphViewer;
	}

	protected void createProcessGraphicalSection(FormToolkit toolkit, Composite parent) {
		Section processGraphSection = toolkit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR);
		processGraphSection.setText("Process Map");
		processGraphSection.setDescription("Right-Click to open the PopMenu to add or remove task node");

		Composite processGraphComposite = toolkit.createComposite(processGraphSection);

		GridLayout processGraphGridLayoutLayout = new GridLayout();
		// processGraphFillLayout.marginWidth = 1;
		// processGraphFillLayout.marginHeight = 1;
		processGraphComposite.setLayout(processGraphGridLayoutLayout);

		processGraphSection.setClient(processGraphComposite);

		Composite toolBarComposite = toolkit.createComposite(processGraphComposite);
		FillLayout l = new FillLayout();
		l.marginHeight = 1;
		l.marginWidth = 1;
		toolBarComposite.setLayout(l);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		toolBarComposite.setLayoutData(gd);

		toolBarComposite.setBackground(toolkit.getColors().getBorderColor());

		createProcessToolBar(toolBarComposite, toolkit);

		Composite graphMainComposite = toolkit.createComposite(processGraphComposite);
		FillLayout l1 = new FillLayout();
		l1.marginHeight = 1;
		l1.marginWidth = 1;
		graphMainComposite.setLayout(l1);
		gd = new GridData(GridData.FILL_BOTH);
		graphMainComposite.setLayoutData(gd);
		graphMainComposite.setBackground(toolkit.getColors().getBorderColor());

		createProcessGraphicalPanel(graphMainComposite);

		hookProcessGraphicalViewer();
		configProcessGraphicalViewer();
		initProcessGraphicalViewer();
	}

	protected void createProcessToolBar(Composite parent, FormToolkit toolkit) {
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
		toolBar.setBackground(toolkit.getColors().getBackground());

		Transfer[] types = new Transfer[] { TemplateTransfer.getInstance() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

		final DragSource source = new DragSource(toolBar, operations);
		source.setTransfer(types);
		source.addDragListener(new DragSourceListener() {

			private Object data = null;

			public void dragStart(DragSourceEvent event) {
				this.data = null;
				DragSource source = (DragSource) event.getSource();
				ToolBar control = (ToolBar) source.getControl();
				ToolItem item = control.getItem(new Point(event.x, event.y));
				if (item == null)
					return;
				event.doit = true;
				TaskTypeDescriptor data = (TaskTypeDescriptor) item.getData();
				event.data = data;
				this.data = data;
				TemplateTransfer.getInstance().setObject(data);
			}

			public void dragSetData(DragSourceEvent event) {
				// DragSource source = (DragSource) event.getSource();
				// ToolBar control = (ToolBar) source.getControl();
				// ToolItem item = control.getItem(new Point(event.x, event.y));
				// if(item == null) return;
				// TaskTypeDescriptor data = (TaskTypeDescriptor)
				// item.getData();
				event.data = this.data;
				TemplateTransfer.getInstance().setObject(this.data);
				// System.out.println(data.getId());
			}

			public void dragFinished(DragSourceEvent event) {
				// if (event.detail == DND.DROP_MOVE)
				// label.setText ("");
				this.data = null;
			}
		});

		List<TaskTypeDescriptor> lis = TaskTypeManager.getAllTaskList();
		for (Iterator<?> iterator = lis.iterator(); iterator.hasNext();) {
			TaskTypeDescriptor taskTypeDescriptor = (TaskTypeDescriptor) iterator.next();
			ToolItem item = new ToolItem(toolBar, SWT.NONE);
			item.setData(taskTypeDescriptor);
			// item.setText(taskTypeDescriptor.getLabel());
			item.setToolTipText("Add " + taskTypeDescriptor.getLabel());
			item.setImage(SmooksConfigurationActivator.getDefault().getImageRegistry().get(
					taskTypeDescriptor.getImagePath()));
		}
	}

	protected void createTaskDetailsSection(FormToolkit toolkit, Composite parent) {
		Composite taskDetailsComposite = toolkit.createComposite(parent);
		FillLayout taskDetailsFillLayout = new FillLayout();
		taskDetailsFillLayout.marginWidth = 0;
		taskDetailsFillLayout.marginHeight = 5;
		taskDetailsComposite.setLayout(taskDetailsFillLayout);

		Section section = toolkit.createSection(taskDetailsComposite, Section.DESCRIPTION | Section.TITLE_BAR);
		section.setText("Task Configuration");
		section.setDescription("Configurate the selected task");
		pageBook = new ScrolledPageBook(section);
		pageBook.setBackground(toolkit.getColors().getBackground());
		section.setClient(pageBook);

		Composite emptyComposite = pageBook.createPage(emptyKey);
		emptyComposite.setLayout(new FillLayout());
		createEmptyTaskPanel(emptyComposite, toolkit);
		pageBook.showPage(emptyKey);
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
		super.createFormContent(managedForm);
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new FillLayout());
		form.setText("Processing");

		Composite mainComposite = toolkit.createComposite(form.getBody());

		GridLayout mgl = new GridLayout();
		mgl.marginHeight = 13;
		mgl.horizontalSpacing = 20;
		mainComposite.setLayout(mgl);

		SashForm sashForm = new SashForm(mainComposite, SWT.VERTICAL);
		sashForm.SASH_WIDTH = 1;
		GridData gd = new GridData(GridData.FILL_BOTH);
		sashForm.setLayoutData(gd);

		createProcessGraphicalSection(toolkit, sashForm);

		createTaskDetailsSection(toolkit, sashForm);

		sashForm.setWeights(new int[] { 3, 7 });
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
							Collection<?> activeModel = rawCommand.getAffectedObjects();
							for (Iterator<?> iterator = activeModel.iterator(); iterator.hasNext();) {
								Object object = (Object) iterator.next();
								if (object instanceof TaskType || object instanceof ProcessType) {
									if (getProcessGraphViewer() != null) {
										getProcessGraphViewer().refresh();
										getProcessGraphViewer().applyLayout();
									}

									return;
								}
							}
						}
					}

				});
			}
		});
	}

	public SmooksGraphicsExtType getSmooksGraphicsExtType() {
		if (smooksModelProvider != null) {
			return smooksModelProvider.getSmooksGraphicsExt();
		}
		return null;
	}

	/**
	 * @return the smooksModelProvider
	 */
	public ISmooksModelProvider getSmooksModelProvider() {
		return smooksModelProvider;
	}

	/**
	 * @param smooksModelProvider
	 *            the smooksModelProvider to set
	 */
	public void setSmooksModelProvider(ISmooksModelProvider smooksModelProvider) {
		this.smooksModelProvider = smooksModelProvider;
	}

	public EObject getSmooksResourceList() {
		if (smooksModelProvider != null) {
			EObject m = null;
			EObject smooksModel = smooksModelProvider.getSmooksModel();
			if (smooksModel instanceof org.jboss.tools.smooks10.model.smooks.DocumentRoot) {
				m = ((org.jboss.tools.smooks10.model.smooks.DocumentRoot) smooksModel).getSmooksResourceList();
			}
			if (smooksModel instanceof DocumentRoot) {
				m = ((DocumentRoot) smooksModel).getSmooksResourceList();
			}
			return m;
		}
		return null;
	}

	public EditingDomain getEditingDomain() {
		if (smooksModelProvider != null) {
			return smooksModelProvider.getEditingDomain();
		}
		return null;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		if (smooksModelProvider != null) {
			this.handleCommandStack(smooksModelProvider.getEditingDomain().getCommandStack());
		}
		SmooksJavaMappingGraphicalEditor javaMappingPart = new SmooksJavaMappingGraphicalEditor(smooksModelProvider);
		this.registeTaskDetailsPage(javaMappingPart, TaskTypeManager.TASK_ID_JAVA_MAPPING);

//		SmooksXSLTemplateGraphicalEditor xsltemplatePart = new SmooksXSLTemplateGraphicalEditor(smooksModelProvider);
//		this.registeTaskDetailsPage(xsltemplatePart, TaskTypeManager.TASK_ID_XSL_TEMPLATE);

		ProcessAnalyzer processAnalyzer = new ProcessAnalyzer();
		EObject smooksModel = getSmooksResourceList();
		if (smooksModel != null && smooksModel instanceof SmooksResourceListType) {
			boolean needSave = processAnalyzer.analyzeSmooksModels((SmooksResourceListType) smooksModel);
			if (needSave) {
				ResourceSet rs = this.smooksModelProvider.getEditingDomain().getResourceSet();
				List<Resource> resourceLis = rs.getResources();
				for (Iterator<?> iterator = resourceLis.iterator(); iterator.hasNext();) {
					Resource resource = (Resource) iterator.next();
					try {
						resource.save(Collections.emptyMap());
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	@Override
	public Object getAdapter(Class type) {
		if (type == ISmooksModelProvider.class) {
			return this.smooksModelProvider;
		}
		return super.getAdapter(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormPage#isDirty()
	 */
	@Override
	public boolean isDirty() {
		boolean dirty = false;
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof IEditorPart) {
				dirty = (((IEditorPart) object).isDirty() || dirty);
			}
		}
		return dirty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof IEditorPart) {
				((IEditorPart) object).doSave(monitor);
			}
		}
		firePropertyChange(PROP_DIRTY);
	}

	public void selectionChanged(SelectionChangedEvent event) {
	}

	protected Control createTaskPanel(Composite parent, FormToolkit toolkit, String taskID) {

		if (taskID == null)
			return null;

		if (taskID.equals(TaskTypeManager.TASK_ID_JAVA_MAPPING)) {
			return null;
		}

		if (taskID.equals(TaskTypeManager.TASK_ID_INPUT)) {
			GridLayout gl = new GridLayout();
			gl.numColumns = 2;
			parent.setLayout(gl);
			toolkit.createLabel(parent,
					"Click the link to switch to the \"Input\" tab to configurate the Smooks Input : ");
			Hyperlink link = toolkit.createHyperlink(parent, "Go to the Input page", SWT.NONE);
			link.addHyperlinkListener(new IHyperlinkListener() {

				public void linkExited(HyperlinkEvent e) {

				}

				public void linkEntered(HyperlinkEvent e) {

				}

				public void linkActivated(HyperlinkEvent e) {
					if (smooksModelProvider instanceof AbstractSmooksFormEditor) {
						((AbstractSmooksFormEditor) smooksModelProvider).setActivePage("reader_page");
					}
				}
			});

			return parent;
		}

		return null;
	}

	protected void showTaskControl(Object model) {
		FormToolkit toolkit = ((AbstractSmooksFormEditor) this.smooksModelProvider).getToolkit();
		if (model instanceof TaskType) {
			String id = ((TaskType) model).getId();
			if (id != null) {
				if (!pageBook.hasPage(id)) {
					Composite parent = pageBook.createPage(id);

					Object page = getRegisteTaskPage(id);
					if (page != null && page instanceof IEditorPart) {
						try {
							parent.setLayout(new FillLayout());
							createTaskPage((IEditorPart) page, parent);
							pageBook.showPage(id);
						} catch (PartInitException e) {
							pageBook.removePage(id);
							pageBook.showPage(emptyKey);
						}
					} else {
						Control control = createTaskPanel(parent, toolkit, id);
						if (control != null) {
							pageBook.showPage(id);
						} else {
							pageBook.removePage(id);
							pageBook.showPage(emptyKey);
						}
					}
				} else {
					pageBook.showPage(id);
				}
			}
		} else {
			// pageBook.showEmptyPage();
		}
	}

	protected IEditorSite createSite(IEditorPart editor) {
		return new SmooksTaskDetailsEditorSite(this.getEditor(), editor, this);
	}

	protected void createTaskPage(IEditorPart editorPart, Composite parent) throws PartInitException {
		IEditorSite site = createSite(editorPart);
		editorPart.init(site, getEditorInput());
		editorPart.createPartControl(parent);
	}

	public Object getRegisteTaskPage(String id) {
		return registedTaskPages.get(id);
	}

	protected Control createEmptyTaskPanel(Composite parent, FormToolkit toolkit) {
		parent.setLayout(new FillLayout());
		return toolkit.createLabel(parent, "Select the task node");
	}

	public void sourceChange(Object model) {
		if (getProcessGraphViewer() != null) {
			initProcessGraphicalViewer();
		}
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof ISmooksGraphChangeListener) {
				((ISourceSynchronizeListener) object).sourceChange(model);
			}
		}
	}

	public void graphChanged(SmooksGraphicsExtType extType) {
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof ISmooksGraphChangeListener) {
				((ISmooksGraphChangeListener) object).graphChanged(extType);
			}
		}
	}

	public void graphPropertyChange(EStructuralFeature featre, Object value) {
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof ISmooksGraphChangeListener) {
				((ISmooksGraphChangeListener) object).graphPropertyChange(featre, value);
			}
		}
	}

	public void inputTypeChanged(SmooksGraphicsExtType extType) {
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof ISmooksGraphChangeListener) {
				((ISmooksGraphChangeListener) object).inputTypeChanged(extType);
			}
		}
	}

	public void propertyChanged(Object source, int propId) {
		this.firePropertyChange(propId);
		if (propId == PROP_DIRTY) {
			this.getManagedForm().dirtyStateChanged();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormPage#dispose()
	 */
	@Override
	public void dispose() {
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof IEditorPart) {
				((IEditorPart) object).removePropertyListener(this);
				((IEditorPart) object).dispose();
			}
		}
		registedTaskPages.clear();
		registedTaskPages = null;
		super.dispose();
	}
}
