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

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandWrapper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledPageBook;
import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.SelectorCreationDialog;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaBeanModel;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanContentProvider;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanlabelProvider;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataContentProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataLabelProvider;
import org.jboss.tools.smooks.edimap.editor.EDIMappingMenuContextProvider;
import org.jboss.tools.smooks.editor.AbstractSmooksFormEditor;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.editor.ISourceSynchronizeListener;
import org.jboss.tools.smooks.gef.common.RootModel;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.figures.IMoveableModel;
import org.jboss.tools.smooks.gef.tree.model.TreeContainerModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.commands.IgnoreException;
import org.jboss.tools.smooks.graphical.editors.editparts.InputDataContainerEditPart;
import org.jboss.tools.smooks.graphical.editors.editparts.SmooksGraphUtil;
import org.jboss.tools.smooks.graphical.editors.model.InputDataContianerModel;
import org.jboss.tools.smooks.graphical.editors.model.InputDataRootModel;
import org.jboss.tools.smooks.graphical.editors.model.InputDataTreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.model.JavaBeanChildGraphModel;
import org.jboss.tools.smooks.graphical.editors.model.JavaBeanGraphModel;
import org.jboss.tools.smooks.model.graphics.ext.FigureType;
import org.jboss.tools.smooks.model.graphics.ext.GraphType;
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
public class SmooksGraphicalEditorPart extends GraphicalEditorWithPalette implements ISelectionChangedListener,
		ISourceSynchronizeListener, ISmooksGraphChangeListener {

	private List<IAction> processPanelActions = new ArrayList<IAction>();

	public static final int EXECUTE_COMMAND = 0;

	public static final int REDO_COMMAND = 1;

	public static final int UNDO_COMMAND = 2;

	private Object emptyKey = new Object();

	private DefaultEditDomain editDomain = null;

	private ISmooksModelProvider smooksModelProvider = null;

	private RootModel root;

	private SmooksResourceListType smooksResourceList;

	private List<Object> inputDataList = null;

	private GraphicalModelFactory graphicalModelFactory;

	private ConnectionModelFactory connectionModelFactory;

//	private GraphViewer processGraphViewer;

	private ScrolledPageBook pageBook;

	public SmooksGraphicalEditorPart(ISmooksModelProvider provider) {
		super();
		this.editDomain = new DefaultEditDomain(this);
		this.editDomain.setCommandStack(new CommandStack() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.gef.commands.CommandStack#execute(org.eclipse.gef
			 * .commands.Command)
			 */
			@Override
			public void execute(org.eclipse.gef.commands.Command command) {
				try {
					super.execute(command);
				} catch (Exception e) {
					if (e instanceof IgnoreException) {
						return;
					} else {
						throw new RuntimeException(e);
					}
				}
			}

		});
		this.editDomain.getCommandStack().addCommandStackEventListener(new CommandStackEventListener() {

			public void stackChanged(CommandStackEvent event) {
				firePropertyChange(PROP_DIRTY);
			}
		});
		this.smooksModelProvider = provider;
		this.setEditDomain(editDomain);
	}

	protected void createProcessGraphicalPanel(Composite parent) {
//		processGraphViewer = new GraphViewer(parent, SWT.NONE);
//		processGraphViewer.setContentProvider(new ProcessGraphContentProvider());
//
//		processGraphViewer.setLabelProvider(new LabelProvider() {
//
//			@Override
//			public Image getImage(Object element) {
//				// TODO Auto-generated method stub
//				return super.getImage(element);
//			}
//
//			@Override
//			public String getText(Object element) {
//				if (element instanceof TaskType) {
//					String id = ((TaskType) element).getId();
//					String name = ((TaskType) element).getName();
//					if (name == null) {
//						name = id;
//					}
//					if (name == null) {
//						name = "null";
//					}
//					return name;
//				}
//				return "";
//			}
//
//		});
//		processGraphViewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
//		HorizontalTreeLayoutAlgorithm layoutAlgorithm = new HorizontalTreeLayoutAlgorithm(
//				LayoutStyles.NO_LAYOUT_NODE_RESIZING);
//		processGraphViewer.setLayoutAlgorithm(layoutAlgorithm, true);
//		if (parent instanceof Section) {
//			((Section) parent).setClient(processGraphViewer.getControl());
//		}
	}

	protected void initProcessGraphicalViewer() {
		SmooksGraphicsExtType ext = this.smooksModelProvider.getSmooksGraphicsExt();
		ProcessesType processes = ext.getProcesses();
		ProcessType process = null;
		if (processes != null) {
			process = processes.getProcess();
		}

		if (process != null) {
//			getProcessGraphViewer().setInput(process);
		}
	}

	protected void configProcessGraphicalViewer() {
//		MenuManager manager = new MenuManager();
//
//		initProcessGraphicalPanelActions(manager);
//
//		Menu menu = manager.createContextMenu(getProcessGraphViewer().getControl());
//		getProcessGraphViewer().getControl().setMenu(menu);
//		manager.addMenuListener(new IMenuListener() {
//
//			public void menuAboutToShow(IMenuManager manager) {
//				for (Iterator<?> iterator = processPanelActions.iterator(); iterator.hasNext();) {
//					IAction a = (IAction) iterator.next();
//					if (a instanceof AbstractProcessGraphAction) {
//						((AbstractProcessGraphAction) a).selectionChanged(new SelectionChangedEvent(processGraphViewer,
//								processGraphViewer.getSelection()));
//					}
//				}
//				manager.update();
//			}
//		});
	}

	protected void initProcessGraphicalPanelActions(IMenuManager manager) {

//		AddTaskNodeAction addInputTaskAction = new AddTaskNodeAction(SmooksConstants.TASK_ID_INPUT, "Add Input Task",
//				smooksModelProvider) {
//
//			@Override
//			public void run() {
//				if (this.provider != null) {
//					SmooksGraphicsExtType graph = provider.getSmooksGraphicsExt();
//					ProcessType process = graph.getProcesses().getProcess();
//					if (process != null && process.getTask().isEmpty()) {
//						TaskType childTask = GraphFactory.eINSTANCE.createTaskType();
//						childTask.setId(taskID);
//						childTask.setName("Input Task");
//						Command command = AddCommand.create(provider.getEditingDomain(), process,
//								GraphPackage.Literals.PROCESS_TYPE__TASK, childTask);
//						provider.getEditingDomain().getCommandStack().execute(command);
//					}
//				}
//			}
//
//			@Override
//			public void update() {
//				this.setEnabled(false);
//				SmooksGraphicsExtType graph = smooksModelProvider.getSmooksGraphicsExt();
//				ProcessType process = graph.getProcesses().getProcess();
//				if (process != null && process.getTask().isEmpty()) {
//					this.setEnabled(true);
//				}
//			}
//
//		};
//		manager.add(addInputTaskAction);
//		processPanelActions.add(addInputTaskAction);
//
//		MenuManager addNextTaskMenuManager = new MenuManager("Add Next Task");
//		manager.add(addNextTaskMenuManager);
//
//		AddNextTaskNodeAction addNextInputAction = new AddNextTaskNodeAction(SmooksConstants.TASK_ID_INPUT, "Input",
//				smooksModelProvider);
//		this.processPanelActions.add(addNextInputAction);
//		addNextTaskMenuManager.add(addNextInputAction);
//
//		AddNextTaskNodeAction addNextJavaMappingAction = new AddNextTaskNodeAction(
//				SmooksConstants.TASK_ID_JAVA_MAPPING, "Java Mapping", smooksModelProvider);
//		this.processPanelActions.add(addNextJavaMappingAction);
//		addNextTaskMenuManager.add(addNextJavaMappingAction);
//
//		MenuManager addPreTaskMenuManager = new MenuManager("Add Previous Task");
//		manager.add(addPreTaskMenuManager);
//
//		AddPreviousTaskNodeAction addPreInputAction = new AddPreviousTaskNodeAction(SmooksConstants.TASK_ID_INPUT,
//				"Input", smooksModelProvider);
//		this.processPanelActions.add(addPreInputAction);
//		addPreTaskMenuManager.add(addPreInputAction);
//
//		AddPreviousTaskNodeAction addPreJavaMappingAction = new AddPreviousTaskNodeAction(
//				SmooksConstants.TASK_ID_JAVA_MAPPING, "Java Mapping", smooksModelProvider);
//		this.processPanelActions.add(addPreJavaMappingAction);
//		addPreTaskMenuManager.add(addPreJavaMappingAction);
//
//		DeleteTaskNodeAction deleteAction = new DeleteTaskNodeAction(smooksModelProvider);
//		manager.add(deleteAction);
//
//		this.processPanelActions.add(deleteAction);
//
//		getProcessGraphViewer().addSelectionChangedListener(new ISelectionChangedListener() {
//
//			public void selectionChanged(SelectionChangedEvent event) {
//				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
//				Object firstElement = selection.getFirstElement();
//				showTaskControl(firstElement);
//			}
//		});
	}

//	public GraphViewer getProcessGraphViewer() {
//		return processGraphViewer;
//	}

	@Override
	public void createPartControl(Composite parent) {
		// FormToolkit toolkit = ((AbstractSmooksFormEditor)
		// this.smooksModelProvider).getToolkit();
		// Composite composite = new Composite(parent, SWT.NONE);
		// composite.setBackground(toolkit.getColors().getBackground());
		//
		// FillLayout fillLayout = new FillLayout();
		// fillLayout.marginWidth = 5;
		// fillLayout.marginHeight = 5;
		// composite.setLayout(fillLayout);
		//
		// SashForm sashForm = new SashForm(composite, SWT.VERTICAL);
		// sashForm.SASH_WIDTH = 1;
		//
		// Section processGraphSection = toolkit.createSection(sashForm,
		// Section.DESCRIPTION | Section.TITLE_BAR);
		// processGraphSection.setText("Process Map");
		// processGraphSection.setDescription("Right-Click to open the PopMenu to add or remove task node");
		//
		// Composite processGraphComposite =
		// toolkit.createComposite(processGraphSection);
		//
		// FillLayout processGraphFillLayout = new FillLayout();
		// processGraphFillLayout.marginWidth = 1;
		// processGraphFillLayout.marginHeight = 1;
		// processGraphComposite.setLayout(processGraphFillLayout);
		//
		// processGraphComposite.setBackground(toolkit.getColors().getBorderColor());
		//
		// processGraphSection.setClient(processGraphComposite);
		//
		// createProcessGraphicalPanel(processGraphComposite);
		// configProcessGraphicalViewer();
		// initProcessGraphicalViewer();
		//
		// Composite taskDetailsComposite = toolkit.createComposite(sashForm);
		// FillLayout taskDetailsFillLayout = new FillLayout();
		// taskDetailsFillLayout.marginWidth = 0;
		// taskDetailsFillLayout.marginHeight = 5;
		// taskDetailsComposite.setLayout(taskDetailsFillLayout);
		//
		// Section section = toolkit.createSection(taskDetailsComposite,
		// Section.DESCRIPTION | Section.TITLE_BAR);
		// section.setText("Task Configuration");
		// section.setDescription("Configurate the selected task");
		// pageBook = new ScrolledPageBook(section);
		// pageBook.setBackground(toolkit.getColors().getBackground());
		// section.setClient(pageBook);
		//
		// sashForm.setWeights(new int[] { 4, 6 });
		//
		// Composite emptyComposite = pageBook.createPage(emptyKey);
		// emptyComposite.setLayout(new FillLayout());
		// createEmptyTaskPanel(emptyComposite, toolkit);
		// pageBook.showPage(emptyKey);

		super.createPartControl(parent);

		// pageBook.showPage(pageBook);

	}

	private void handleCommandStack(org.eclipse.emf.common.command.CommandStack commandStack) {
		commandStack.addCommandStackListener(new org.eclipse.emf.common.command.CommandStackListener() {
			public void commandStackChanged(EventObject event) {
				final Command mostRecentCommand = ((org.eclipse.emf.common.command.CommandStack) event.getSource())
						.getMostRecentCommand();
				final EventObject fe = event;
				getEditorSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (mostRecentCommand != null) {
//							if (getProcessGraphViewer() != null) {
//								getProcessGraphViewer().refresh();
//								getProcessGraphViewer().applyLayout();
//							}
							Command rawCommand = mostRecentCommand;
							while (rawCommand instanceof CommandWrapper) {
								rawCommand = ((CommandWrapper) rawCommand).getCommand();
							}
							int commandType = EXECUTE_COMMAND;
							Command undoCommand = ((org.eclipse.emf.common.command.CommandStack) fe.getSource())
									.getUndoCommand();
							Command redoCommand = ((org.eclipse.emf.common.command.CommandStack) fe.getSource())
									.getRedoCommand();
							// System.out.println("Undo Command : " +
							// undoCommand);
							// System.out.println("Redo Command : " +
							// redoCommand);
							// System.out.println("Same With Undo : " +
							// rawCommand.equals(undoCommand));
							// System.out.println("Same With Redo : " +
							// rawCommand.equals(redoCommand));
							if (undoCommand != null || rawCommand.equals(undoCommand)) {
								commandType = EXECUTE_COMMAND;
							}
							if (redoCommand != null || rawCommand.equals(redoCommand)) {
								commandType = UNDO_COMMAND;
							}
							if (rawCommand instanceof SetCommand || rawCommand instanceof AddCommand
									|| rawCommand instanceof DeleteCommand || rawCommand instanceof RemoveCommand) {
								refershRecentAffectedModel(rawCommand, mostRecentCommand.getAffectedObjects(),
										commandType);
							}
						}
					}

				});
			}
		});
	}

	public ConnectionModelFactory getConnectionModelFactory() {

		if (connectionModelFactory == null) {
			connectionModelFactory = createConnectionModelFactory();
		}
		return connectionModelFactory;
	}

	private ConnectionModelFactory createConnectionModelFactory() {
		return new ConnectionModelFactoryImpl();
	}

	public void setConnectionModelFactory(ConnectionModelFactory connectionModelFactory) {
		this.connectionModelFactory = connectionModelFactory;
	}

	public GraphicalModelFactory getGraphicalModelFactory() {
		if (graphicalModelFactory == null) {
			graphicalModelFactory = createGraphicalModelFactory();
		}
		return graphicalModelFactory;
	}

	private GraphicalModelFactory createGraphicalModelFactory() {
		return new GraphicalModelFactoryImpl();
	}

	public void setGraphicalModelFactory(GraphicalModelFactoryImpl graphicalModelFactory) {
		this.graphicalModelFactory = graphicalModelFactory;
	}

	@Override
	protected void createActions() {
		super.createActions();
	}

	private void deleteRelatedConnection(AbstractSmooksGraphicalModel effecedNode, EStructuralFeature feature,
			SetCommand command) {
		EObject data = (EObject) AdapterFactoryEditingDomain.unwrap(effecedNode.getData());

		if (feature.equals(SmooksUIUtils.getBeanIDRefFeature(data))) {
			List<TreeNodeConnection> sourceConnections = effecedNode.getSourceConnections();
			List<TreeNodeConnection> temp = new ArrayList<TreeNodeConnection>(sourceConnections);
			for (Iterator<?> iterator = temp.iterator(); iterator.hasNext();) {
				TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator.next();
				AbstractSmooksGraphicalModel target = treeNodeConnection.getTargetNode();
				String refID = command.getValue().toString();
				Object targetModel = AdapterFactoryEditingDomain.unwrap(target.getData());
				if (targetModel instanceof EObject) {
					EStructuralFeature idfeature = SmooksUIUtils.getBeanIDFeature((EObject) targetModel);
					if (idfeature == null)
						continue;
					Object iddata = ((EObject) targetModel).eGet(idfeature);
					if (iddata != null) {
						if (refID == null || !refID.equals(iddata)) {
							target.getTargetConnections().remove(treeNodeConnection);
							effecedNode.getSourceConnections().remove(treeNodeConnection);
							target.fireConnectionChanged();
						}
					}
				}
			}
		}

		if (feature.equals(SmooksUIUtils.getBeanIDFeature(data))) {
			List<TreeNodeConnection> targetConnections = effecedNode.getTargetConnections();
			List<TreeNodeConnection> temp = new ArrayList<TreeNodeConnection>(targetConnections);
			for (Iterator<?> iterator = temp.iterator(); iterator.hasNext();) {
				TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator.next();
				AbstractSmooksGraphicalModel source = treeNodeConnection.getSourceNode();
				String beanID = command.getValue().toString();
				Object sourceModel = AdapterFactoryEditingDomain.unwrap(source.getData());
				if (sourceModel instanceof EObject) {
					EStructuralFeature idRefFeature = SmooksUIUtils.getBeanIDRefFeature((EObject) sourceModel);
					if (idRefFeature == null)
						continue;
					Object idRefData = ((EObject) sourceModel).eGet(idRefFeature);
					if (idRefData != null) {
						if (beanID == null || !beanID.equals(idRefData)) {
							source.getSourceConnections().remove(treeNodeConnection);
							effecedNode.getTargetConnections().remove(treeNodeConnection);
							source.fireConnectionChanged();
						}
					}
				}
			}
		}

		if (feature.equals(SmooksUIUtils.getSelectorFeature(data))) {
			List<TreeNodeConnection> targetConnections = effecedNode.getTargetConnections();
			List<TreeNodeConnection> temp = new ArrayList<TreeNodeConnection>(targetConnections);
			for (Iterator<?> iterator = temp.iterator(); iterator.hasNext();) {
				TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator.next();
				AbstractSmooksGraphicalModel source = treeNodeConnection.getSourceNode();
				if (source instanceof InputDataContianerModel || source instanceof InputDataTreeNodeModel) {
					String selector = command.getValue().toString();
					Object sourceModel = AdapterFactoryEditingDomain.unwrap(source.getData());
					if (sourceModel instanceof IXMLStructuredObject) {
						IXMLStructuredObject root = SmooksUIUtils.getRootParent((IXMLStructuredObject) sourceModel);
						Object oldNode = null;
						try {
							oldNode = SmooksUIUtils.localXMLNodeWithPath(selector, root);
						} catch (Throwable t) {

						}
						if (oldNode == sourceModel) {
							break;
						} else {
							source.getSourceConnections().remove(treeNodeConnection);
							effecedNode.getTargetConnections().remove(treeNodeConnection);
							source.fireConnectionChanged();
						}
					}
				}

			}
		}
	}

	/**
	 * Very important method ! neet to improve
	 * 
	 * @param command
	 * @param affectedObjects
	 * @param commandType
	 */
	protected void refershRecentAffectedModel(Command command, Collection<?> affectedObjects, int commandType) {
		for (Iterator<?> iterator = affectedObjects.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			object = AdapterFactoryEditingDomain.unwrap(object);
			if (commandType == EXECUTE_COMMAND || commandType == REDO_COMMAND) {
				if (object instanceof SmooksResourceListType) {
					if (command instanceof AddCommand) {
						Collection<?> colletion = ((AddCommand) command).getCollection();
						for (Iterator<?> iterator2 = colletion.iterator(); iterator2.hasNext();) {
							Object childModel = (Object) iterator2.next();
							childModel = AdapterFactoryEditingDomain.unwrap(childModel);
							AbstractSmooksGraphicalModel graphModel = createGraphModel(childModel);
							if (graphModel == null)
								continue;
							root.addTreeNode(graphModel);
							applyGraphicalInformation(graphModel);
							Collection<TreeNodeConnection> connections = createAllConnection(graphModel);
							expandConnectedModels(connections);
						}
					}
					if (command instanceof DeleteCommand || command instanceof RemoveCommand) {
						Collection<?> colletion = null;
						if (command instanceof DeleteCommand) {
							colletion = ((DeleteCommand) command).getCollection();
						}
						if (command instanceof RemoveCommand) {
							colletion = ((RemoveCommand) command).getCollection();
						}
						for (Iterator<?> iterator2 = colletion.iterator(); iterator2.hasNext();) {
							Object childModel = (Object) iterator2.next();
							childModel = AdapterFactoryEditingDomain.unwrap(childModel);
							AbstractSmooksGraphicalModel graphModel = findGraphicalModel(childModel);
							if (graphModel == null)
								continue;
							AbstractSmooksGraphicalModel.disconnectAllConnections(graphModel);
							root.removeTreeNode(graphModel);
						}
					}
				} else {
					object = AdapterFactoryEditingDomain.unwrap(object);
					AbstractSmooksGraphicalModel node = findGraphicalModel(object);
					if (command instanceof SetCommand) {
						if (node == null) {
							continue;
						}
						node.fireVisualChanged();
						EStructuralFeature feature = ((SetCommand) command).getFeature();
						if (SmooksUIUtils.isRelatedConnectionFeature(feature)) {
							deleteRelatedConnection(node, feature, (SetCommand) command);
							Collection<TreeNodeConnection> connections = createConnection(node);
							node.fireConnectionChanged();
							expandConnectedModels(connections);
						}
					}
					if (command instanceof AddCommand) {
						Object owner = ((AddCommand) command).getOwner();
						owner = AdapterFactoryEditingDomain.unwrap(owner);
						if (owner instanceof SmooksResourceListType) {
							AbstractSmooksGraphicalModel graphModel = createGraphModel(object);
							if (graphModel == null)
								continue;
							root.addTreeNode(graphModel);
							applyGraphicalInformation(graphModel);
							Collection<TreeNodeConnection> connections = createAllConnection(graphModel);
							expandConnectedModels(connections);
						} else {
							AbstractSmooksGraphicalModel ownerGraph = findGraphicalModel(owner);
							if (ownerGraph != null) {
								ownerGraph.fireChildrenChanged();
							}
						}
						if (node == null) {
							continue;
						}
						node.fireChildrenChanged();
						node.fireConnectionChanged();
					}
					if (command instanceof DeleteCommand || command instanceof RemoveCommand) {
						if (node != null) {
							node.fireChildrenChanged();
						}
					}
				}
				continue;
			}

			if (commandType == UNDO_COMMAND) {
				object = AdapterFactoryEditingDomain.unwrap(object);
				AbstractSmooksGraphicalModel node = findGraphicalModel(object);
				if (command instanceof AddCommand) {
					if (object instanceof SmooksResourceListType) {
						Collection<?> cccc = ((AddCommand) command).getCollection();
						for (Iterator<?> iterator2 = cccc.iterator(); iterator2.hasNext();) {
							Object object2 = (Object) iterator2.next();
							object2 = AdapterFactoryEditingDomain.unwrap(object2);
							AbstractSmooksGraphicalModel gmodel = findGraphicalModel(object2);
							if (gmodel != null) {
								root.removeTreeNode(gmodel);
								break;
							}
						}
					} else {
						if (node != null) {
							node.fireChildrenChanged();
						}
					}
				}
				if (command instanceof SetCommand) {
					if (node == null) {
						continue;
					}
					node.fireVisualChanged();
					EStructuralFeature feature = ((SetCommand) command).getFeature();
					if (SmooksUIUtils.isRelatedConnectionFeature(feature)) {
						deleteRelatedConnection(node, feature, (SetCommand) command);
						Collection<TreeNodeConnection> connections = createConnection(node);
						node.fireConnectionChanged();
						expandConnectedModels(connections);
					}
				}
				if (command instanceof DeleteCommand || command instanceof RemoveCommand) {
					Collection<?> cccc = null;
					if (command instanceof DeleteCommand) {
						cccc = ((DeleteCommand) command).getCollection();
					}
					if (command instanceof RemoveCommand) {
						cccc = ((RemoveCommand) command).getCollection();
					}
					for (Iterator<?> iterator2 = cccc.iterator(); iterator2.hasNext();) {
						Object object2 = (Object) iterator2.next();
						object2 = AdapterFactoryEditingDomain.unwrap(object2);
						// it means that it's deletecommand undo
						if (object == object2) {
							EObject owner = ((EObject) object).eContainer();
							if (owner instanceof SmooksResourceListType) {
								object = AdapterFactoryEditingDomain.unwrap(object);
								AbstractSmooksGraphicalModel graphModel = createGraphModel(object);
								if (graphModel == null)
									continue;
								node = graphModel;
								root.addTreeNode(graphModel);
								Collection<TreeNodeConnection> connections = createAllConnection(graphModel);
								expandConnectedModels(connections);
								applyGraphicalInformation(graphModel);
							} else {
								AbstractSmooksGraphicalModel ownernode = findGraphicalModel(owner);
								ownernode.fireChildrenChanged();
								node = findGraphicalModel(object);
								if (node == null)
									continue;
								Collection<TreeNodeConnection> connections = createAllConnection(node);
								expandConnectedModels(connections);
							}
							break;
						}
					}
				}
				continue;
			}
		}
	}

	private AbstractSmooksGraphicalModel findGraphicalModel(Object object) {
		return SmooksGraphUtil.findSmooksGraphModel(root, object);
	}

	public DefaultEditDomain getEditDomain() {
		return editDomain;
	}

	@Override
	protected void configurePaletteViewer() {
		super.configurePaletteViewer();
		getPaletteViewer().addDragSourceListener(new TemplateTransferDragSourceListener(getPaletteViewer()));
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getGraphicalViewer().setEditDomain(editDomain);
		getGraphicalViewer().setEditPartFactory(new SmooksEditFactory());

		getGraphicalViewer().setRootEditPart(new FreeformGraphicalRootEditPart());

		getGraphicalViewer().addDropTargetListener(
				(TransferDropTargetListener) new TemplateTransferDropTargetListener(getGraphicalViewer()));

		GraphicalViewerKeyHandler keyHandler = new GraphicalViewerKeyHandler(getGraphicalViewer());
		keyHandler.put(org.eclipse.gef.KeyStroke.getPressed(SWT.DEL, 0), this.getActionRegistry().getAction(
				ActionFactory.DELETE.getId()));

		EDIMappingMenuContextProvider provider = new EDIMappingMenuContextProvider(getGraphicalViewer(), this
				.getActionRegistry());
		getGraphicalViewer().setContextMenu(provider);

		hookSelectionActions();
	}

	private void hookSelectionActions() {
		Iterator<?> actions = getActionRegistry().getActions();
		while (actions.hasNext()) {
			Object action = actions.next();
			if (action instanceof SelectionAction) {
				((SelectionAction) action).setSelectionProvider(getGraphicalViewer());
			}
		}
	}

	protected List<AbstractSmooksGraphicalModel> createInputDataGraphModel() {
		List<AbstractSmooksGraphicalModel> inputGraphModel = new ArrayList<AbstractSmooksGraphicalModel>();
		if (inputDataList != null && root != null) {
			for (Iterator<?> iterator = inputDataList.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				ITreeContentProvider contentProvider = new XMLStructuredDataContentProvider();
				ILabelProvider labelProvider = new XMLStructuredDataLabelProvider();
				InputDataRootModel containerModel = new InputDataRootModel();

				if (object instanceof JavaBeanModel) {
					contentProvider = new JavabeanContentProvider();
					labelProvider = new JavabeanlabelProvider();

				}
				containerModel.getChildren().add((IXMLStructuredObject) object);
				if (containerModel != null) {
					TreeContainerModel container = new InputDataContianerModel(containerModel, contentProvider,
							labelProvider);
					root.addTreeNode(container);
					inputGraphModel.add(container);
				}
			}
		}
		return inputGraphModel;
	}

	@Override
	protected void hookGraphicalViewer() {
		// super.hookGraphicalViewer();
		getGraphicalViewer().addSelectionChangedListener(getSelectionSynchronizer());
		getGraphicalViewer().addSelectionChangedListener(this);
	}

	protected void initGraphicalModel() {
		if (root == null) {
			root = new RootModel();
		} else {
			root.removeAllTreeNode();

		}
		Object obj = smooksModelProvider.getSmooksModel();
		if (obj == null)
			return;
		AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) smooksModelProvider
				.getEditingDomain();
		if (inputDataList != null && obj != null && obj instanceof DocumentRoot && editingDomain != null) {
			createInputDataGraphModel();
			SmooksResourceListType listType = ((DocumentRoot) obj).getSmooksResourceList();
			List<?> arcList = listType.getAbstractResourceConfig();
			for (Iterator<?> iterator = arcList.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				AbstractSmooksGraphicalModel gmodel = createGraphModel(object);
				if (gmodel != null) {
					root.addTreeNode(gmodel);
				}
			}
			getGraphicalViewer().setContents(root);
		}

		// create connection
		List<TreeNodeConnection> connections = createConnectionModel();

		// init position

		initModelGraphicsInformation(smooksModelProvider.getSmooksGraphicsExt());

		// expand connected models
		expandConnectedModels(connections);
	}

	protected AbstractSmooksGraphicalModel createGraphModel(Object model) {
		GraphicalModelFactory factory = getGraphicalModelFactory();
		if (factory != null) {
			Object gmodel = factory.createGraphicalModel(model, smooksModelProvider);
			if (gmodel != null && gmodel instanceof AbstractSmooksGraphicalModel) {
				AbstractSmooksGraphicalModel graphicalModel = (AbstractSmooksGraphicalModel) gmodel;
				return graphicalModel;
			}
		}
		return null;
	}

	protected List<TreeNodeConnection> createConnectionModel() {
		List<TreeNodeConnection> connections = new ArrayList<TreeNodeConnection>();
		if (root != null) {
			createConnection(root.getChildren(), connections);
		}
		return connections;
	}

	protected Collection<TreeNodeConnection> createAllConnection(AbstractSmooksGraphicalModel model) {
		try {
			if (model == null)
				return null;
			List<TreeNodeConnection> connections = new ArrayList<TreeNodeConnection>();
			Collection<TreeNodeConnection> c1 = createConnection(model);
			if (c1 != null) {
				connections.addAll(c1);
			}
			List<AbstractSmooksGraphicalModel> children = model.getChildren();
			for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
				AbstractSmooksGraphicalModel abstractSmooksGraphicalModel = (AbstractSmooksGraphicalModel) iterator
						.next();
				Collection<TreeNodeConnection> cc = createConnection(abstractSmooksGraphicalModel);
				if (cc != null) {
					connections.addAll(cc);
				}
			}
			return connections;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	protected Collection<TreeNodeConnection> createConnection(AbstractSmooksGraphicalModel model) {
		ConnectionModelFactory connectionModelFactory = getConnectionModelFactory();
		List<TreeNodeConnection> cs = new ArrayList<TreeNodeConnection>();
		if (connectionModelFactory != null) {
			if (connectionModelFactory.hasSelectorConnection(model)) {
				Collection<TreeNodeConnection> cList = connectionModelFactory.createSelectorConnection(inputDataList,
						root, model);
				if (cList != null) {
					cs.addAll(cList);
				}
			}
			if (connectionModelFactory.hasBeanIDConnection(model)) {
				Collection<TreeNodeConnection> c = connectionModelFactory.createBeanIDReferenceConnection(
						getSmooksResourceList(), root, model);
				if (c != null) {
					cs.addAll(c);
				}
			}
		}
		if (cs.isEmpty())
			return null;
		return cs;
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

	private void createConnection(List<AbstractSmooksGraphicalModel> children, List<TreeNodeConnection> connections) {
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			AbstractSmooksGraphicalModel abstractSmooksGraphicalModel = (AbstractSmooksGraphicalModel) iterator.next();
			if (!(abstractSmooksGraphicalModel instanceof InputDataContianerModel)) {
				if (canCreateConnection(abstractSmooksGraphicalModel)) {
					Collection<TreeNodeConnection> c = createConnection(abstractSmooksGraphicalModel);
					if (c != null) {
						connections.addAll(c);
					}
				}
				List<AbstractSmooksGraphicalModel> cchildren = abstractSmooksGraphicalModel.getChildren();
				createConnection(cchildren, connections);
			}
		}
	}

	private boolean canCreateConnection(AbstractSmooksGraphicalModel model) {
		if (model instanceof JavaBeanGraphModel) {
			return true;
		}
		if (model instanceof JavaBeanChildGraphModel) {
			return true;
		}
		return false;
	}

	protected void initSmooksData() {
		if (smooksModelProvider != null) {
			Object obj = smooksModelProvider.getSmooksModel();
			smooksResourceList = null;
			if (obj instanceof DocumentRoot) {
				smooksResourceList = ((DocumentRoot) obj).getSmooksResourceList();
				inputDataList = SelectorCreationDialog.generateInputData(smooksModelProvider.getSmooksGraphicsExt(),
						smooksResourceList);
			}
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		initSmooksData();
		if (smooksModelProvider != null) {
			this.handleCommandStack(smooksModelProvider.getEditingDomain().getCommandStack());
		}
	}

	protected void expandConnectedModels(Collection<TreeNodeConnection> connections) {
		if (connections == null || connections.isEmpty())
			return;
		List<TreeNodeModel> expanedTreeNodeList = new ArrayList<TreeNodeModel>();
		for (Iterator<?> iterator = connections.iterator(); iterator.hasNext();) {
			TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator.next();
			AbstractSmooksGraphicalModel source = treeNodeConnection.getSourceNode();
			Object data = source.getData();
			if (source instanceof TreeNodeModel && data instanceof IXMLStructuredObject) {
				expanedTreeNodeList.add((TreeNodeModel) source);
			}
		}
		EditPart rootEditPart = getGraphicalViewer().getContents();
		if (rootEditPart != null) {
			List<?> childrenEditPart = rootEditPart.getChildren();
			for (Iterator<?> iterator = childrenEditPart.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				if (object instanceof InputDataContainerEditPart) {
					SmooksUIUtils.expandGraphTree(expanedTreeNodeList, (InputDataContainerEditPart) object);
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	@Override
	protected void initializeGraphicalViewer() {
		initGraphicalModel();
	}

	protected void applyGraphicalInformation(AbstractSmooksGraphicalModel graphicalModel) {
		GraphType graph = getSmooksGraphicsExtType().getGraph();
		applyGraphicalInformation(graph, graphicalModel);
	}

	protected void applyGraphicalInformation(GraphType graph, AbstractSmooksGraphicalModel graphicalModel) {
		if (graphicalModel instanceof IMoveableModel) {
			String id = SmooksGraphUtil.generateFigureID(graphicalModel);
			if (id != null) {
				FigureType ft = SmooksGraphUtil.findFigureType(graph, id);
				try {
					int x = Integer.parseInt(ft.getX());
					int y = Integer.parseInt(ft.getY());
					((IMoveableModel) graphicalModel).setLocation(new Point(x, y));
				} catch (Throwable t) {
				}
			}
		}
	}

	protected void initModelGraphicsInformation(SmooksGraphicsExtType ext) {
		GraphType graph = ext.getGraph();
		if (graph == null)
			return;
		List<AbstractSmooksGraphicalModel> list = root.getChildren();
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			AbstractSmooksGraphicalModel treeNodeModel = (AbstractSmooksGraphicalModel) iterator.next();
			applyGraphicalInformation(graph, treeNodeModel);
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
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		super.selectionChanged(part, selection);
	}

	public void selectionChanged(SelectionChangedEvent event) {
		updateActions(getSelectionActions());
	}

	protected Control createTaskPanel(Composite parent, FormToolkit toolkit, String taskID) {

		if (taskID == null)
			return null;

		if (taskID.equals(SmooksConstants.TASK_ID_JAVA_MAPPING)) {
			parent.setBackground(toolkit.getColors().getBorderColor());

			FillLayout detailsFillLayout = new FillLayout();
			detailsFillLayout.marginWidth = 1;
			detailsFillLayout.marginHeight = 1;
			parent.setLayout(detailsFillLayout);

			super.createPartControl(parent);
			return parent;
		}

		if (taskID.equals(SmooksConstants.TASK_ID_INPUT)) {
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
					Control control = createTaskPanel(parent, toolkit, id);
					if (control != null) {
						pageBook.showPage(id);
					} else {
						pageBook.removePage(id);
						pageBook.showPage(emptyKey);
					}
				} else {
					pageBook.showPage(id);
				}
			}
		} else {
			// pageBook.showEmptyPage();
		}
	}

	protected Control createEmptyTaskPanel(Composite parent, FormToolkit toolkit) {
		parent.setLayout(new FillLayout());
		return toolkit.createLabel(parent, "Select the task node");
	}

	public void sourceChange(Object model) {
		initGraphicalModel();
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		SmooksGraphicalEditorPaletteRootCreator creator = new SmooksGraphicalEditorPaletteRootCreator(
				this.smooksModelProvider, (AdapterFactoryEditingDomain) this.smooksModelProvider.getEditingDomain(),
				getSmooksResourceListType());
		return creator.createPaletteRoot();
	}

	public SmooksResourceListType getSmooksResourceListType() {
		if (smooksModelProvider != null) {
			Object obj = smooksModelProvider.getSmooksModel();
			smooksResourceList = null;
			if (obj instanceof DocumentRoot) {
				smooksResourceList = ((DocumentRoot) obj).getSmooksResourceList();
			}
			return smooksResourceList;
		}
		return null;
	}

	public void graphChanged(SmooksGraphicsExtType extType) {

	}

	public void graphPropertyChange(EStructuralFeature featre, Object value) {

	}

	public void inputTypeChanged(SmooksGraphicsExtType extType) {
		if (root != null) {
			List<Object> newInputDataList = SelectorCreationDialog.generateInputData(smooksModelProvider
					.getSmooksGraphicsExt(), getSmooksResourceListType());

			List<InputDataContianerModel> inputs = new ArrayList<InputDataContianerModel>();
			List<AbstractSmooksGraphicalModel> children = root.getChildren();
			// remove all input data graph model
			for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
				AbstractSmooksGraphicalModel abstractSmooksGraphicalModel = (AbstractSmooksGraphicalModel) iterator
						.next();
				if (abstractSmooksGraphicalModel instanceof InputDataContianerModel) {
					inputs.add((InputDataContianerModel) abstractSmooksGraphicalModel);
				}
			}

			for (Iterator<?> iterator = inputs.iterator(); iterator.hasNext();) {
				InputDataContianerModel inputModel = (InputDataContianerModel) iterator.next();
				AbstractSmooksGraphicalModel.disconnectAllConnections(inputModel);
				root.removeTreeNode(inputModel);
			}

			inputDataList.clear();
			inputDataList.addAll(newInputDataList);

			// renew input data graph model
			List<AbstractSmooksGraphicalModel> inputGraphModel = createInputDataGraphModel();
			if (inputGraphModel != null && !inputGraphModel.isEmpty()) {
				List<TreeNodeConnection> connections = createConnectionModel();
				createConnection(inputGraphModel, connections);
				expandConnectedModels(connections);
			}
		}
	}
}
