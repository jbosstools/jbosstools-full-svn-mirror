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
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.SelectorCreationDialog;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaBeanModel;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanContentProvider;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanlabelProvider;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataContentProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataLabelProvider;
import org.jboss.tools.smooks.edimap.editor.EDIMappingMenuContextProvider;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.editor.ISourceSynchronizeListener;
import org.jboss.tools.smooks.gef.common.RootModel;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.figures.IMoveableModel;
import org.jboss.tools.smooks.gef.tree.model.TreeContainerModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.editparts.InputDataContainerEditPart;
import org.jboss.tools.smooks.graphical.editors.editparts.SmooksGraphUtil;
import org.jboss.tools.smooks.graphical.editors.model.InputDataContianerModel;
import org.jboss.tools.smooks.graphical.editors.model.InputDataRootModel;
import org.jboss.tools.smooks.graphical.editors.model.InputDataTreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.model.JavaBeanChildGraphModel;
import org.jboss.tools.smooks.graphical.editors.model.JavaBeanGraphModel;
import org.jboss.tools.smooks.model.graphics.ext.FigureType;
import org.jboss.tools.smooks.model.graphics.ext.GraphType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart
 * 
 */
public class SmooksGraphicalEditorPart extends GraphicalEditorWithPalette implements ISelectionChangedListener,
		ISourceSynchronizeListener {

	public static final int EXECUTE_COMMAND = 0;

	public static final int REDO_COMMAND = 1;

	public static final int UNDO_COMMAND = 2;

	private DefaultEditDomain editDomain = null;

	private ISmooksModelProvider smooksModelProvider = null;

	private RootModel root;

	private SmooksResourceListType smooksResourceList;

	private List<Object> inputDataList = null;

	private GraphicalModelFactory graphicalModelFactory;

	private ConnectionModelFactory connectionModelFactory;

	public SmooksGraphicalEditorPart(ISmooksModelProvider provider) {
		super();
		this.editDomain = new DefaultEditDomain(this);
		this.editDomain.getCommandStack().addCommandStackEventListener(new CommandStackEventListener() {

			public void stackChanged(CommandStackEvent event) {
				firePropertyChange(PROP_DIRTY);
			}
		});
		this.smooksModelProvider = provider;
		this.setEditDomain(editDomain);
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
							Command rawCommand = mostRecentCommand;
							while (rawCommand instanceof CommandWrapper) {
								rawCommand = ((CommandWrapper) rawCommand).getCommand();
							}
							int commandType = EXECUTE_COMMAND;
							Command undoCommand = ((org.eclipse.emf.common.command.CommandStack) fe.getSource())
									.getUndoCommand();
							Command redoCommand = ((org.eclipse.emf.common.command.CommandStack) fe.getSource())
									.getRedoCommand();
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
								if (graphModel instanceof IMoveableModel) {
									String id = SmooksGraphUtil.generateFigureID(graphModel);
									if (id != null) {
										FigureType ft = SmooksModelUtils.findFigureType(smooksModelProvider
												.getSmooksGraphicsExt().getGraph(), id);
										try {
											int x = Integer.parseInt(ft.getX());
											int y = Integer.parseInt(ft.getY());
											((IMoveableModel) graphModel).setLocation(new Point(x, y));
										} catch (Throwable t) {
											continue;
										}
									}
								}
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

	// private AbstractSmooksGraphicalModel
	// findGraphicalModel(AbstractSmooksGraphicalModel graph, Object object) {
	// if (AdapterFactoryEditingDomain.unwrap(graph.getData()) == object) {
	// return graph;
	// }
	// List<?> children = graph.getChildrenWithoutDynamic();
	// for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
	// Object child = (Object) iterator.next();
	// if (child instanceof AbstractSmooksGraphicalModel) {
	// AbstractSmooksGraphicalModel model =
	// findGraphicalModel((AbstractSmooksGraphicalModel) child, object);
	// if (model != null) {
	// return model;
	// }
	// }
	// }
	// return null;
	// }

	public DefaultEditDomain getEditDomain() {
		return editDomain;
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getGraphicalViewer().setEditDomain(editDomain);
		getGraphicalViewer().setEditPartFactory(new SmooksEditFactory());

		getGraphicalViewer().setRootEditPart(new FreeformGraphicalRootEditPart());

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
		AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) smooksModelProvider
				.getEditingDomain();
		if (inputDataList != null && obj != null && obj instanceof DocumentRoot && editingDomain != null) {
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
				}
			}
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
				return (AbstractSmooksGraphicalModel) gmodel;
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
		if (model == null)
			return null;
		List<TreeNodeConnection> connections = new ArrayList<TreeNodeConnection>();
		Collection<TreeNodeConnection> c1 = createConnection(model);
		if (c1 != null) {
			connections.addAll(c1);
		}
		List<AbstractSmooksGraphicalModel> children = model.getChildren();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			AbstractSmooksGraphicalModel abstractSmooksGraphicalModel = (AbstractSmooksGraphicalModel) iterator.next();
			Collection<TreeNodeConnection> cc = createConnection(abstractSmooksGraphicalModel);
			if (cc != null) {
				connections.addAll(cc);
			}
		}
		return connections;
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

	private EObject getSmooksResourceList() {
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
		if (connections == null)
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

	protected void initModelGraphicsInformation(SmooksGraphicsExtType ext) {
		GraphType graph = ext.getGraph();
		if (graph == null)
			return;
		List<AbstractSmooksGraphicalModel> list = root.getChildren();
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
			AbstractSmooksGraphicalModel treeNodeModel = (AbstractSmooksGraphicalModel) iterator.next();
			if (treeNodeModel instanceof IMoveableModel) {
				String id = SmooksGraphUtil.generateFigureID(treeNodeModel);
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

	public void sourceChange(Object model) {
		initGraphicalModel();
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		SmooksGraphicalEditorPaletteRootCreator creator = new SmooksGraphicalEditorPaletteRootCreator();
		return creator.createPaletteRoot();
	}
}
