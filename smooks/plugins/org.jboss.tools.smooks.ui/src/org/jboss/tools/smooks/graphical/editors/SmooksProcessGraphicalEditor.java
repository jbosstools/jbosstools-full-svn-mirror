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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandWrapper;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.ScrolledPageBook;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.CGraphNode;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.configuration.editors.SmooksReaderFormPage;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.validate.ISmooksModelValidateListener;
import org.jboss.tools.smooks.editor.AbstractSmooksFormEditor;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.editor.ISourceSynchronizeListener;
import org.jboss.tools.smooks.graphical.actions.AbstractProcessGraphAction;
import org.jboss.tools.smooks.graphical.actions.AddNextTaskNodeAction;
import org.jboss.tools.smooks.graphical.actions.DeleteTaskNodeAction;
import org.jboss.tools.smooks.graphical.editors.TaskTypeManager.TaskTypeDescriptor;
import org.jboss.tools.smooks.graphical.editors.process.IProcessProvider;
import org.jboss.tools.smooks.graphical.editors.process.ProcessFactory;
import org.jboss.tools.smooks.graphical.editors.process.ProcessGraphicalViewerLabelProvider;
import org.jboss.tools.smooks.graphical.editors.process.ProcessTaskAnalyzer;
import org.jboss.tools.smooks.graphical.editors.process.ProcessType;
import org.jboss.tools.smooks.graphical.editors.process.TaskNodeFigure;
import org.jboss.tools.smooks.graphical.editors.process.TaskType;
import org.jboss.tools.smooks.graphical.editors.process.TemplateAppyTaskNode;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart
 * 
 */
public class SmooksProcessGraphicalEditor extends FormPage implements ISelectionChangedListener,
		ISourceSynchronizeListener, IPropertyListener, ISmooksModelValidateListener, IProcessProvider,
		PropertyChangeListener, ISmooksEditorInitListener {

	private int currentMessageType = IMessageProvider.NONE;

	private String currentMessage = null;

	private boolean processChanged = false;

	private boolean lockProcessChangeEvent = false;

	private List<IAction> processPanelActions = new ArrayList<IAction>();

	public static final int EXECUTE_COMMAND = 0;

	public static final int REDO_COMMAND = 1;

	public static final int UNDO_COMMAND = 2;

	private Object emptyKey = new Object();

	private ISmooksModelProvider smooksModelProvider = null;

	private GraphViewer processGraphViewer;

	private ScrolledPageBook pageBook;

	private Map<String, Object> registedTaskPages = new HashMap<String, Object>();

	private MenuManager manager;

	protected boolean needupdatewhenshow = true;

	private ProcessType process;

	private Map<Object, String> smooksModelIdMap = new HashMap<Object, String>();

	public SmooksProcessGraphicalEditor(FormEditor editor, String id, String title, ISmooksModelProvider provider) {
		super(editor, id, title);
		this.smooksModelProvider = provider;
	}

	public SmooksProcessGraphicalEditor(String id, String title, ISmooksModelProvider provider) {
		super(id, title);
		this.smooksModelProvider = provider;
		this.getManagedForm();
	}

	/**
	 * @return the process
	 */
	public ProcessType getProcess() {
		return process;
	}

	protected void createProcessGraphicalPanel(Composite parent) {
		processGraphViewer = new GraphViewer(parent, SWT.NONE);
		processGraphViewer.getControl().dispose();
		processGraphViewer.setControl(new Graph(parent, SWT.NONE) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.zest.core.widgets.Graph#dispose()
			 */
			@Override
			public void dispose() {
				try {
					super.dispose();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}

		});
		// GridData gd = new GridData(GridData.FILL_BOTH);
		// processGraphViewer.getControl().setLayoutData(gd);
		// processGraphViewer.setNodeStyle(ZestStyles.NODES_FISHEYE);
		processGraphViewer.setContentProvider(new ProcessGraphContentProvider());
		processGraphViewer.setLabelProvider(new ProcessGraphicalViewerLabelProvider(this));
		processGraphViewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		HorizontalTreeLayoutAlgorithm layoutAlgorithm = new HorizontalTreeLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		processGraphViewer.setLayoutAlgorithm(layoutAlgorithm, true);
		if (parent instanceof Section) {
			((Section) parent).setClient(processGraphViewer.getControl());
		}
	}

	protected void initProcessGraphicalViewer() {
		// SmooksGraphicsExtType ext =
		// this.smooksModelProvider.getSmooksGraphicsExt();
		// if (ext == null)
		// return;
		// ProcessesType processes = ext.getProcesses();
		boolean disableProcessViewer = false;
		// if (processes != null) {
		// ProcessType process = processes.getProcess();
		// if (process != null) {
		// getProcessGraphViewer().getControl().setBackground(
		// getManagedForm().getToolkit().getColors().getBackground());
		// getProcessGraphViewer().getControl().setEnabled(true);
		// getProcessGraphViewer().setInput(process);
		// } else {
		// disableProcessViewer = true;
		// }
		// } else {
		// disableProcessViewer = true;
		// }
		if (process == null) {
			process = ProcessFactory.eINSTANCE.createProcessType();
			process.addPropertyChangeListener(this);
		}
		ProcessTaskAnalyzer analyzer = new ProcessTaskAnalyzer();
		lockProcessChangeEvent = true;
		analyzer.analyzeTaskNode(process, getSmooksResourceListType());
		lockProcessChangeEvent = false;
		if (getProcessGraphViewer() != null) {
			getProcessGraphViewer().setInput(process);
		}
		if (disableProcessViewer) {
			getProcessGraphViewer().getControl().setBackground(
					getManagedForm().getToolkit().getColors().getBorderColor());
			getProcessGraphViewer().setInput(null);
			getProcessGraphViewer().getControl().setEnabled(false);
		}
	}

	protected void hookProcessGraphicalViewer() {
		// final DropTarget dropTarge = new
		// DropTarget(getProcessGraphViewer().getControl(), DND.DROP_MOVE |
		// DND.DROP_COPY);
		// dropTarge.setTransfer(new Transfer[] { TemplateTransfer.getInstance()
		// });
		// dropTarge.addDropListener(new DropTargetListener() {
		// private TaskType taskType = null;
		//
		// private ProcessType process = null;
		//
		// public void dropAccept(DropTargetEvent event) {
		//
		// }
		//
		// public void drop(DropTargetEvent event) {
		// if (event.detail == DND.DROP_COPY) {
		// if (this.taskType != null) {
		// TaskTypeDescriptor des = (TaskTypeDescriptor)
		// TemplateTransfer.getInstance().getTemplate();
		// AddNextTaskNodeAction action = new AddNextTaskNodeAction(des.getId(),
		// des.getLabel(),
		// smooksModelProvider);
		// TaskType taskType = this.taskType;
		// IStructuredSelection selection = new StructuredSelection(taskType);
		// action.selectionChanged(new
		// SelectionChangedEvent(getProcessGraphViewer(), selection));
		// action.run();
		// return;
		// }
		// if (this.process != null) {
		// AddNextTaskNodeAction action = new
		// AddInputTaskAction(smooksModelProvider);
		// // IStructuredSelection selection = new
		// // StructuredSelection(taskType);
		// // action.selectionChanged(new
		// // SelectionChangedEvent(getProcessGraphViewer(),
		// // selection));
		// action.run();
		// return;
		// }
		// }
		// }
		//
		// public void dragOver(DropTargetEvent event) {
		// Control control = getProcessGraphViewer().getControl();
		// if (control != null && control instanceof Graph) {
		// Graph graph = (Graph) control;
		// Point pp = graph.toControl(new Point(event.x, event.y));
		// TaskTypeDescriptor des = (TaskTypeDescriptor)
		// TemplateTransfer.getInstance().getTemplate();
		// TaskType testType = GraphFactory.eINSTANCE.createTaskType();
		// testType.setId(des.getId());
		// IFigure figure = graph.getFigureAt(pp.x, pp.y);
		// if (figure == null) {
		// if (testType.getId().equals(TaskTypeManager.TASK_ID_INPUT)) {
		// ProcessType process = (ProcessType)
		// getProcessGraphViewer().getInput();
		// if (process.getTask().isEmpty()) {
		// event.detail = DND.DROP_COPY;
		// this.process = process;
		// return;
		// }
		// }
		// event.detail = DND.DROP_NONE;
		// this.taskType = null;
		// process = null;
		// return;
		// }
		// List<?> nodes = graph.getNodes();
		// for (Iterator<?> iterator = nodes.iterator(); iterator.hasNext();) {
		// Object object = (Object) iterator.next();
		// if (object instanceof GraphNode) {
		// IFigure f = ((GraphNode) object).getNodeFigure();
		// if (figure == f) {
		// TaskTypeRules rules = new TaskTypeRules();
		// if (rules.isNextTask((TaskType) ((GraphNode) object).getData(),
		// testType)) {
		// event.detail = DND.DROP_COPY;
		// this.taskType = (TaskType) ((GraphNode) object).getData();
		// return;
		// }
		// }
		// }
		// }
		// event.detail = DND.DROP_NONE;
		// this.taskType = null;
		// this.process = null;
		// }
		// }
		//
		// public void dragOperationChanged(DropTargetEvent event) {
		// }
		//
		// public void dragLeave(DropTargetEvent event) {
		// }
		//
		// public void dragEnter(DropTargetEvent event) {
		// event.detail = DND.DROP_MOVE;
		// this.taskType = null;
		// process = null;
		// }
		// });
		getProcessGraphViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object firstElement = selection.getFirstElement();
				if (firstElement == null) {
					unhighlightGraphNodes();
				}
				Graph graph = getProcessGraphViewer().getGraphControl();
				List<?> nodes = graph.getNodes();
				GraphItem item = null;
				for (Iterator<?> iterator = nodes.iterator(); iterator.hasNext();) {
					GraphItem graphItem = (GraphItem) iterator.next();
					if (graphItem.getData() == firstElement) {
						item = graphItem;
						break;
					}
				}
				if (item != null) {
					unhighlightGraphNodes();
					highlightGraphNode(item);
				}
				showTaskControl(firstElement);
			}
		});
	}

	protected void unhighlightGraphNodes() {

		Graph graph = this.getProcessGraphViewer().getGraphControl();
		List<?> elements = graph.getNodes();
		for (Iterator<?> iterator = elements.iterator(); iterator.hasNext();) {
			GraphItem graphItem = (GraphItem) iterator.next();
			unhighlightGraphNode(graphItem);
		}
	}

	protected void unhighlightGraphNode(GraphItem item) {
		if (item instanceof CGraphNode) {
			IFigure figure = ((CGraphNode) item).getFigure();
			if (figure instanceof TaskNodeFigure) {
				((TaskNodeFigure) figure).unhighlightLabel();
			}
		}
	}

	protected void highlightGraphNode(GraphItem item) {
		if (item instanceof CGraphNode) {
			IFigure figure = ((CGraphNode) item).getFigure();
			if (figure instanceof TaskNodeFigure) {
				((TaskNodeFigure) figure).highlightLabel(org.eclipse.draw2d.ColorConstants.darkBlue);
			}
		}
	}

	protected void configProcessGraphicalViewer() {
		manager = new MenuManager();

		initProcessGraphicalPanelActions(manager);

		Menu menu = manager.createContextMenu(getProcessGraphViewer().getControl());
		getProcessGraphViewer().getControl().setMenu(menu);
		manager.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				try {
					manager.removeAll();
					if (needupdatewhenshow) {
						updateProcessActions(processGraphViewer.getSelection());
						fillProcessMenu(manager);
					} else {
						for (Iterator<?> iterator = processPanelActions.iterator(); iterator.hasNext();) {
							IAction action = (IAction) iterator.next();
							if (action.isEnabled() && !(action instanceof DeleteTaskNodeAction)) {
								manager.add(action);
							}
						}
					}
					needupdatewhenshow = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				needupdatewhenshow = true;
			}
		});
	}

	public void fillProcessMenu(IMenuManager manager) {
		MenuManager addNextTaskMenuManager = new MenuManager(Messages.SmooksProcessGraphicalEditor_AddTaskActionText);
		manager.add(addNextTaskMenuManager);

		for (Iterator<?> iterator = processPanelActions.iterator(); iterator.hasNext();) {
			IAction action = (IAction) iterator.next();
			if (action instanceof DeleteTaskNodeAction) {
				manager.add(action);
			} else {
				addNextTaskMenuManager.add(action);
			}
		}

		// MenuManager addPreTaskMenuManager = new
		// MenuManager("Add Previous Task");
		// manager.add(addPreTaskMenuManager);
		//
		// fillPreTaskMenu(addPreTaskMenuManager);
	}

	/**
	 * @return the needupdatewhenshow
	 */
	public boolean isNeedupdatewhenshow() {
		return needupdatewhenshow;
	}

	/**
	 * @param needupdatewhenshow
	 *            the needupdatewhenshow to set
	 */
	public void setNeedupdatewhenshow(boolean needupdatewhenshow) {
		this.needupdatewhenshow = needupdatewhenshow;
	}

	public void updateProcessActions(ISelection selection) {
		for (Iterator<?> iterator = processPanelActions.iterator(); iterator.hasNext();) {
			IAction a = (IAction) iterator.next();
			if (a instanceof AbstractProcessGraphAction) {
				((AbstractProcessGraphAction) a).selectionChanged(new SelectionChangedEvent(processGraphViewer,
						selection));
			}
		}
		manager.update();
	}

	protected void initProcessGraphicalPanelActions(IMenuManager manager) {

		// AddTaskNodeAction addInputTaskAction = new
		// AddInputTaskAction(smooksModelProvider);
		// manager.add(addInputTaskAction);
		// processPanelActions.add(addInputTaskAction);

		MenuManager addNextTaskMenuManager = new MenuManager(Messages.SmooksProcessGraphicalEditor_AddTaskActionText);
		manager.add(addNextTaskMenuManager);

		generateNextTaskActions(addNextTaskMenuManager);

		// MenuManager addPreTaskMenuManager = new
		// MenuManager("Add Previous Task");
		// manager.add(addPreTaskMenuManager);
		//
		// fillPreTaskMenu(addPreTaskMenuManager);

		DeleteTaskNodeAction deleteAction = new DeleteTaskNodeAction(this, smooksModelProvider, this);
		manager.add(deleteAction);

		this.processPanelActions.add(deleteAction);

	}

	private void generateNextTaskActions(MenuManager addNextTaskMenuManager) {
		List<TaskTypeDescriptor> list = TaskTypeManager.getAllTaskList();
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			TaskTypeDescriptor taskTypeDescriptor = (TaskTypeDescriptor) iterator.next();
			AddNextTaskNodeAction addNextInputAction = new AddNextTaskNodeAction(taskTypeDescriptor.getId(),
					taskTypeDescriptor.getLabel(), smooksModelProvider, this);
			this.processPanelActions.add(addNextInputAction);
			addNextTaskMenuManager.add(addNextInputAction);
		}
	}

	// private void fillPreTaskMenu(MenuManager addPreTaskMenuManager) {
	// List<TaskTypeDescriptor> list = TaskTypeManager.getAllTaskList();
	// for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
	// TaskTypeDescriptor taskTypeDescriptor = (TaskTypeDescriptor)
	// iterator.next();
	// AddPreviousTaskNodeAction addNextInputAction = new
	// AddPreviousTaskNodeAction(taskTypeDescriptor.getId(),
	// taskTypeDescriptor.getLabel(), smooksModelProvider);
	// this.processPanelActions.add(addNextInputAction);
	// addPreTaskMenuManager.add(addNextInputAction);
	// }
	// }

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
		updateHeaderFormMessage();
	}

	public GraphViewer getProcessGraphViewer() {
		return processGraphViewer;
	}

	protected void createProcessGraphicalSection(FormToolkit toolkit, Composite parent) {
		Section processGraphSection = toolkit.createSection(parent, Section.TITLE_BAR);
		processGraphSection.setText(Messages.SmooksProcessGraphicalEditor_TasksMapSectionTitle);

		Composite processGraphComposite = toolkit.createComposite(processGraphSection);

		GridLayout processGraphGridLayoutLayout = new GridLayout();
		// processGraphFillLayout.marginWidth = 1;
		// processGraphFillLayout.marginHeight = 1;
		processGraphComposite.setLayout(processGraphGridLayoutLayout);

		processGraphSection.setClient(processGraphComposite);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		// Composite toolBarComposite =
		// toolkit.createComposite(processGraphComposite);
		// FillLayout l = new FillLayout();
		// l.marginHeight = 1;
		// l.marginWidth = 1;
		// toolBarComposite.setLayout(l);

		// toolBarComposite.setLayoutData(gd);
		//
		// toolBarComposite.setBackground(toolkit.getColors().getBorderColor());
		//
		// createProcessToolBar(toolBarComposite, toolkit);

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

	protected void createTaskDetailsSection(FormToolkit toolkit, Composite parent) {
		Composite taskDetailsComposite = toolkit.createComposite(parent);
		FillLayout taskDetailsFillLayout = new FillLayout();
		taskDetailsFillLayout.marginWidth = 0;
		taskDetailsFillLayout.marginHeight = 5;
		taskDetailsComposite.setLayout(taskDetailsFillLayout);

		Section section = toolkit.createSection(taskDetailsComposite, Section.TITLE_BAR);
		section.setText(Messages.SmooksProcessGraphicalEditor_TaskConfigurationSectionTitle);
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
		form.setText(Messages.SmooksProcessGraphicalEditor_FormText);

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
										break;
									}
								}
							}
							if (rawCommand instanceof CompoundCommand) {
								List<Command> command = ((CompoundCommand) rawCommand).getCommandList();
								for (Iterator<?> iterator = command.iterator(); iterator.hasNext();) {
									Command command2 = (Command) iterator.next();
									while (command2 instanceof CommandWrapper) {
										command2 = ((CommandWrapper) command2).getCommand();
									}
									if (command2 instanceof DeleteCommand || command2 instanceof RemoveCommand) {
										Collection<?> objs = ((Command) command2).getAffectedObjects();
										for (Iterator<?> iterator2 = objs.iterator(); iterator2.hasNext();) {
											Object object = (Object) iterator2.next();
											object = AdapterFactoryEditingDomain.unwrap(object);
											if (object instanceof TaskType || object instanceof ProcessType) {
												showTaskControl(null);
												break;
											}
										}
									}

									if (command2 instanceof AddCommand || command2 instanceof SetCommand) {
										Collection<?> objs = ((Command) command2).getAffectedObjects();
										for (Iterator<?> iterator2 = objs.iterator(); iterator2.hasNext();) {
											Object object = (Object) iterator2.next();
											object = AdapterFactoryEditingDomain.unwrap(object);
											if (object instanceof TaskType) {
												showTaskControl((TaskType) object);
												break;
											}
										}
									}
								}
							} else {
								if (rawCommand instanceof DeleteCommand || rawCommand instanceof RemoveCommand) {
									activeModel = rawCommand.getAffectedObjects();
									for (Iterator<?> iterator = activeModel.iterator(); iterator.hasNext();) {
										Object object = (Object) iterator.next();
										object = AdapterFactoryEditingDomain.unwrap(object);
										if (object instanceof TaskType || object instanceof ProcessType) {
											if (getProcessGraphViewer() != null) {
												showTaskControl(null);
												break;
											}
										}
									}
								}
								if (rawCommand instanceof AddCommand || rawCommand instanceof SetCommand) {
									Collection<?> objs = ((Command) rawCommand).getAffectedObjects();
									for (Iterator<?> iterator2 = objs.iterator(); iterator2.hasNext();) {
										Object object = (Object) iterator2.next();
										object = AdapterFactoryEditingDomain.unwrap(object);
										if (object instanceof TaskType) {
											showTaskControl((TaskType) object);
											break;
										}
									}
								}
							}
						}
					}

				});
			}
		});
	}

	// public SmooksGraphicsExtType getSmooksGraphicsExtType() {
	// if (smooksModelProvider != null) {
	// return smooksModelProvider.getSmooksGraphicsExt();
	// }
	// return null;
	// }

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

		List<TaskTypeDescriptor> tasks = TaskTypeManager.getAllTaskList();
		for (Iterator<?> iterator = tasks.iterator(); iterator.hasNext();) {
			TaskTypeDescriptor taskTypeDescriptor = (TaskTypeDescriptor) iterator.next();
			IEditorPart part = createEditorPart(taskTypeDescriptor.getId());
			if (part != null) {
				this.registeTaskDetailsPage(part, taskTypeDescriptor.getId());
			}
		}
	}

	protected SmooksResourceListType getSmooksResourceListType() {
		if (smooksModelProvider != null) {
			return SmooksUIUtils.getSmooks11ResourceListType(smooksModelProvider.getSmooksModel());
		}
		return null;
	}

	protected IEditorPart createEditorPart(Object taskID) {
		if (taskID.equals(TaskTypeManager.TASK_ID_FREEMARKER_TEMPLATE)) {
			SmooksFreemarkerTemplateGraphicalEditor freemarkerPart = new SmooksFreemarkerTemplateGraphicalEditor(
					smooksModelProvider);
			return freemarkerPart;
		}
		if (taskID.equals(TaskTypeManager.TASK_ID_JAVA_MAPPING)) {
			SmooksJavaMappingGraphicalEditor javaMappingPart = new SmooksJavaMappingGraphicalEditor(smooksModelProvider);
			return javaMappingPart;
		}

		if (taskID.equals(TaskTypeManager.TASK_ID_INPUT)) {
			SmooksReaderFormPage readerPage = new SmooksReaderFormPage(getEditor(), "input", "input"); //$NON-NLS-1$ //$NON-NLS-2$
			return readerPage;
		}
		return null;
	}

	protected boolean isSingltonEditor(Object taskID) {
		if (taskID.equals(TaskTypeManager.TASK_ID_FREEMARKER_TEMPLATE)) {
			return false;
		}
		return true;
	}

	@Override
	public Object getAdapter(Class type) {
		if (type == ISmooksModelProvider.class) {
			return this.smooksModelProvider;
		}
		if (type == IProcessProvider.class) {
			return this;
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
		return (dirty || processChanged);
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
		processChanged = false;
		firePropertyChange(PROP_DIRTY);
	}

	public void selectionChanged(SelectionChangedEvent event) {
	}

	protected Control createTaskPanel(Composite parent, FormToolkit toolkit, String taskID) {

		// if (taskID == null)
		// return null;
		//
		// if (taskID.equals(TaskTypeManager.TASK_ID_JAVA_MAPPING)) {
		// return null;
		// }
		//
		// if (taskID.equals(TaskTypeManager.TASK_ID_INPUT)) {
		// GridLayout gl = new GridLayout();
		// gl.numColumns = 2;
		// parent.setLayout(gl);
		// toolkit.createLabel(parent,
		// "Click the link to switch to the \"Input\" tab to configurate the Smooks Input : ");
		// Hyperlink link = toolkit.createHyperlink(parent,
		// "Go to the Input page", SWT.NONE);
		// link.addHyperlinkListener(new IHyperlinkListener() {
		//
		// public void linkExited(HyperlinkEvent e) {
		//
		// }
		//
		// public void linkEntered(HyperlinkEvent e) {
		//
		// }
		//
		// public void linkActivated(HyperlinkEvent e) {
		// if (smooksModelProvider instanceof AbstractSmooksFormEditor) {
		// ((AbstractSmooksFormEditor)
		// smooksModelProvider).setActivePage("reader_page");
		// }
		// }
		// });
		//
		// return parent;
		// }

		return null;
	}

	protected String generateTaskSpecailID(TaskType task) {
		if (task instanceof TemplateAppyTaskNode) {
			if (!((TemplateAppyTaskNode) task).getSmooksModel().isEmpty()
					&& ((TemplateAppyTaskNode) task).getSmooksModel().size() == 1) {
				Object freemarker = ((TemplateAppyTaskNode) task).getSmooksModel().get(0);
				String taskID = smooksModelIdMap.get(freemarker);
				if (taskID == null) {
					String tempstamp = String.valueOf(System.currentTimeMillis());
					taskID = "freemarker" + "_" + tempstamp; //$NON-NLS-1$ //$NON-NLS-2$
					this.smooksModelIdMap.put(freemarker, taskID);
				}
				return taskID;
			}
		}
		return null;
	}

	protected void showTaskControl(Object model) {
		if (pageBook == null)
			return;
		if (model == null)
			pageBook.showEmptyPage();
		final Object finalModel = model;
		pageBook.getShell().getDisplay().syncExec(new Runnable() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				FormToolkit toolkit = ((AbstractSmooksFormEditor) smooksModelProvider).getToolkit();
				if (finalModel instanceof TaskType) {
					String id = ((TaskType) finalModel).getId();
					if (!isSingltonEditor(id)) {
						String idref = generateTaskSpecailID((TaskType) finalModel);
						if (idref != null) {
							// idref = id + "_" + idref;
							if (getRegisteTaskPage(idref) == null) {
								IEditorPart editor = createEditorPart(id);
								registeTaskDetailsPage(editor, idref);
							}
							id = idref;
						} else {
							id = id + "_unknown"; //$NON-NLS-1$
						}
					}
					if (id != null) {
						if (!pageBook.hasPage(id)) {
							Composite parent = pageBook.createPage(id);
							Object page = getRegisteTaskPage(id);
							if (page != null && page instanceof IEditorPart) {
								try {
									parent.setLayout(new FillLayout());

									ITaskNodeProvider nodeProvider = (ITaskNodeProvider) ((IEditorPart) page)
											.getAdapter(ITaskNodeProvider.class);
									if (nodeProvider != null) {
										nodeProvider.setTaskType((TaskType) finalModel);
									}
									createTaskPage((IEditorPart) page, parent);
									pageBook.showPage(id);
									parent.setData(page);

								} catch (Throwable e) {
									e.printStackTrace();
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
							Object page = getRegisteTaskPage(id);
							ITaskNodeProvider nodeProvider = (ITaskNodeProvider) ((IEditorPart) page)
									.getAdapter(ITaskNodeProvider.class);
							if (nodeProvider != null) {
								nodeProvider.setTaskType((TaskType) finalModel);
							}
							pageBook.showPage(id);
						}
					}
				} else {
					// pageBook.showEmptyPage();
				}
			}

		});

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
		return toolkit.createLabel(parent, "Select the task node"); //$NON-NLS-1$
	}

	public void sourceChange(Object model) {
		if (getProcessGraphViewer() != null) {
			initProcessGraphicalViewer();
		}
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof IEditorPart) {
				((IEditorPart) object).removePropertyListener(this);
				if (((IEditorPart) object).getEditorSite() == null) {
					continue;
				}
				((IEditorPart) object).dispose();
			}
		}
		registedTaskPages.clear();

		if (pageBook != null) {
			List<TaskTypeDescriptor> tasks = TaskTypeManager.getAllTaskList();
			for (Iterator<?> iterator = tasks.iterator(); iterator.hasNext();) {
				TaskTypeDescriptor taskTypeDescriptor = (TaskTypeDescriptor) iterator.next();
				pageBook.removePage(taskTypeDescriptor.getId(), true);
			}
			for (Iterator<String> iterator = smooksModelIdMap.values().iterator(); iterator.hasNext();) {
				String id = (String) iterator.next();
				pageBook.removePage(id, true);
			}
		}

		List<TaskTypeDescriptor> tasks = TaskTypeManager.getAllTaskList();
		for (Iterator<?> iterator = tasks.iterator(); iterator.hasNext();) {
			TaskTypeDescriptor taskTypeDescriptor = (TaskTypeDescriptor) iterator.next();
			IEditorPart part = createEditorPart(taskTypeDescriptor.getId());
			if (part != null) {
				this.registeTaskDetailsPage(part, taskTypeDescriptor.getId());
			}
		}

		// registedTaskPages.clear();
		// Collection<Object> editors = registedTaskPages.values();
		// for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();)
		// {
		// Object object = (Object) iterator.next();
		// if (object instanceof ISmooksGraphChangeListener) {
		// ((ISourceSynchronizeListener) object).sourceChange(model);
		// }
		// }
	}

	// public void graphChanged(SmooksGraphicsExtType extType) {
	// Collection<Object> editors = registedTaskPages.values();
	// for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
	// Object object = (Object) iterator.next();
	// if (object instanceof ISmooksGraphChangeListener) {
	// ((ISmooksGraphChangeListener) object).graphChanged(extType);
	// }
	// }
	// }

	// public void graphPropertyChange(EStructuralFeature featre, Object value)
	// {
	// Collection<Object> editors = registedTaskPages.values();
	// for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
	// Object object = (Object) iterator.next();
	// if (object instanceof ISmooksGraphChangeListener) {
	// ((ISmooksGraphChangeListener) object).graphPropertyChange(featre, value);
	// }
	// }
	// }

	// public void inputTypeChanged(SmooksGraphicsExtType extType) {
	// Collection<Object> editors = registedTaskPages.values();
	// for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
	// Object object = (Object) iterator.next();
	// if (object instanceof ISmooksGraphChangeListener) {
	// ((ISmooksGraphChangeListener) object).inputTypeChanged(extType);
	// }
	// }
	// }

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
				if (((IEditorPart) object).getEditorSite() == null) {
					continue;
				}
				((IEditorPart) object).dispose();
			}
		}
		registedTaskPages.clear();
		super.dispose();
	}

	public void validateEnd(List<Diagnostic> diagnosticResult) {
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof ISmooksModelValidateListener) {
				((ISmooksModelValidateListener) object).validateEnd(diagnosticResult);
			}
		}

	}

	public void validateStart() {
		Collection<Object> editors = registedTaskPages.values();
		for (Iterator<?> iterator = editors.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof ISmooksModelValidateListener) {
				((ISmooksModelValidateListener) object).validateStart();
			}
		}

	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (lockProcessChangeEvent)
			return;
		String name = evt.getPropertyName();
		Object newtask = evt.getNewValue();
		if (ProcessType.PRO_ADD_CHILD.equals(name) || ProcessType.PRO_REMOVE_CHILD.equals(name)) {
			if (getProcessGraphViewer() != null) {
				getProcessGraphViewer().refresh();
				getProcessGraphViewer().applyLayout();
			}
			processChanged = true;
			getManagedForm().dirtyStateChanged();
		}

		if (ProcessType.PRO_ADD_CHILD.equals(name)) {
			this.showTaskControl(newtask);
		}
		if (ProcessType.PRO_REMOVE_CHILD.equals(name)) {
			this.showTaskControl(null);
		}
	}

	public void initFailed(int messageType, String message) {
		this.currentMessage = message;
		this.currentMessageType = messageType;
		updateHeaderFormMessage();
	}

	protected void updateHeaderFormMessage() {
		if (this.getManagedForm() != null) {
			getManagedForm().getMessageManager().removeAllMessages();
			getManagedForm().getMessageManager().update();
			getProcessGraphViewer().getControl().setEnabled(true);
			getProcessGraphViewer().getControl().setBackground(
					getManagedForm().getToolkit().getColors().getBackground());
		}
		if (currentMessageType != IMessageProvider.NONE && currentMessage != null) {
			if (this.getProcessGraphViewer() != null) {
				getProcessGraphViewer().getControl().setBackground(GraphicsConstants.BORDER_CORLOR);
				getProcessGraphViewer().getControl().setEnabled(false);
				getProcessGraphViewer().setInput(new Object());
				showTaskControl(null);
			}
			if (this.getManagedForm() != null) {

				String[] messages = currentMessage.split("\n");
				List<IMessage> messageList = new ArrayList<IMessage>();
				for (int i = 0; i < messages.length; i++) {
					String message = messages[i];
					if (message != null)
						message.trim();
					if (message.length() == 0) {
						continue;
					}
					messageList.add(new SmooksMessage(currentMessageType, message));
				}
				String mainMessage = null;
				if (messageList.isEmpty()) {
					mainMessage = currentMessage;
				} else {
					mainMessage = messageList.get(0).getMessage();
				}

				this.getManagedForm().getForm().getForm().setMessage(mainMessage, currentMessageType,
						messageList.toArray(new IMessage[] {}));

			}
		}
	}
}
