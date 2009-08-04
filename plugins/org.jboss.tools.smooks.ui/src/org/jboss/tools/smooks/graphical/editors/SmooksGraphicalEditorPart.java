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
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.SelectoreSelectionDialog;
import org.jboss.tools.smooks.configuration.editors.javabean.JavaBeanModel;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanContentProvider;
import org.jboss.tools.smooks.configuration.editors.javabean.JavabeanlabelProvider;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataContentProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XMLStructuredDataLabelProvider;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
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
import org.jboss.tools.smooks.graphical.editors.model.JavaBeanChildGraphModel;
import org.jboss.tools.smooks.graphical.editors.model.JavaBeanGraphModel;
import org.jboss.tools.smooks.model.common.AbstractAnyType;
import org.jboss.tools.smooks.model.graphics.ext.FigureType;
import org.jboss.tools.smooks.model.graphics.ext.GraphType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart
 * 
 */
public class SmooksGraphicalEditorPart extends GraphicalEditor {

	private DefaultEditDomain editDomain = null;

	private ISmooksModelProvider smooksModelProvider = null;

	private RootModel root;

	private SmooksResourceListType smooksResourceList;

	private List<Object> inputDataList = null;

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
	
	protected void refershRecentAffectedModel(Command command, Collection<?> affectedObjects) {
		for (Iterator<?> iterator = affectedObjects.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			TreeNodeModel node = findGraphicalModel(object);
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

	private TreeNodeModel findGraphicalModel(Object object) {
		return null;
	}

	public DefaultEditDomain getEditDomain() {
		return editDomain;
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getGraphicalViewer().setEditDomain(editDomain);
		getGraphicalViewer().setEditPartFactory(new SmooksEditFactory());

		getGraphicalViewer().setRootEditPart(new FreeformGraphicalRootEditPart());
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
				// if(object instanceof TagObject){
				// TagList tl = new TagList();
				// tl.addRootTag((TagObject)object);
				// TreeContainerModel container = new
				// InputDataContianerModel(tl, contentProvider,
				// labelProvider);
				// root.addTreeNode(container);
				// }
				containerModel.getChildren().add((IXMLStructuredObject) object);
				if (containerModel != null) {
					TreeContainerModel container = new InputDataContianerModel(containerModel, contentProvider,
							labelProvider);
					root.addTreeNode(container);
				}
			}
			// to find Bean v1.2
			SmooksResourceListType listType = ((DocumentRoot) obj).getSmooksResourceList();
			List<?> arcList = listType.getAbstractResourceConfig();
			ITreeContentProvider contentProvider = new AdapterFactoryContentProvider(editingDomain.getAdapterFactory());
			ILabelProvider labelProvider = new AdapterFactoryLabelProvider(editingDomain.getAdapterFactory()) {
				@Override
				public String getText(Object object) {
					Object obj = AdapterFactoryEditingDomain.unwrap(object);
					if (obj instanceof AbstractAnyType) {
						return super.getText(obj);
					}
					return super.getText(object);
				}

			};
			for (Iterator<?> iterator = arcList.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				if (object instanceof BindingsType) {
					JavaBeanGraphModel graphModel = new JavaBeanGraphModel(object, contentProvider, labelProvider);
					graphModel.setHeaderVisable(true);
					root.addTreeNode(graphModel);
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

	protected List<TreeNodeConnection> createConnectionModel() {
		List<TreeNodeConnection> connections = new ArrayList<TreeNodeConnection>();
		if (root != null) {
			createConnection(root.getChildren(), connections);
		}
		return connections;
	}

	private List<TreeNodeConnection> createConnection(AbstractSmooksGraphicalModel model) {
		List<TreeNodeConnection> cs = new ArrayList<TreeNodeConnection>();
		if (hasSelectorConnection(model)) {
			List<TreeNodeConnection> cList = createSelectorConnection(model);
			if (cList != null) {
				cs.addAll(cList);
			}
		}
		if (hasBeanIDReferenceConnection(model)) {
			TreeNodeConnection c = createBeanIDReferenceConnection(model);
			if (c != null) {
				cs.add(c);
			}
		}
		if (cs.isEmpty())
			return null;
		return cs;
	}

	private TreeNodeConnection createBeanIDReferenceConnection(AbstractSmooksGraphicalModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<TreeNodeConnection> createSelectorConnection(AbstractSmooksGraphicalModel model) {
		Object data = model.getData();
		data = AdapterFactoryEditingDomain.unwrap(data);
		List<TreeNodeConnection> connections = new ArrayList<TreeNodeConnection>();
		if (data != null && data instanceof EObject) {
			EStructuralFeature feature = SmooksGraphUtil.getSelectorFeature((EObject) data);
			Object sd = ((EObject) data).eGet(feature);
			if (sd != null) {
				String selector = sd.toString();
				if (inputDataList != null) {
					for (Iterator<?> iterator = inputDataList.iterator(); iterator.hasNext();) {
						Object obj = (Object) iterator.next();
						if (obj instanceof IXMLStructuredObject) {
							AbstractSmooksGraphicalModel sourceGraphModel = SmooksGraphUtil.findInputGraphModel(
									selector, (IXMLStructuredObject) obj, root);
							if (sourceGraphModel != null) {
								TreeNodeConnection connection = new TreeNodeConnection(sourceGraphModel, model);
								connection.connect();
								connections.add(connection);
							}
						}
					}
				}
			}
		}
		return connections;
	}

	private boolean hasSelectorConnection(AbstractSmooksGraphicalModel model) {
		Object data = model.getData();
		data = AdapterFactoryEditingDomain.unwrap(data);
		if (data instanceof EObject) {
			return (SmooksGraphUtil.getSelectorFeature((EObject) data) != null);
		}
		return false;
	}

	private boolean hasBeanIDReferenceConnection(AbstractSmooksGraphicalModel model) {
		return false;
	}

	private void createConnection(List<AbstractSmooksGraphicalModel> children, List<TreeNodeConnection> connections) {
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			AbstractSmooksGraphicalModel abstractSmooksGraphicalModel = (AbstractSmooksGraphicalModel) iterator.next();
			if (!(abstractSmooksGraphicalModel instanceof InputDataContianerModel)) {
				if (canCreateConnection(abstractSmooksGraphicalModel)) {
					List<TreeNodeConnection> c = createConnection(abstractSmooksGraphicalModel);
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
				inputDataList = SelectoreSelectionDialog.generateInputData(smooksModelProvider.getSmooksGraphicsExt(),
						smooksResourceList);
			}
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		initSmooksData();
		if(smooksModelProvider != null){
			this.handleCommandStack(smooksModelProvider.getEditingDomain().getCommandStack());
		}
	}

	protected void expandConnectedModels(List<TreeNodeConnection> connections) {
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
		List<?> childrenEditPart = rootEditPart.getChildren();
		for (Iterator<?> iterator = childrenEditPart.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof InputDataContainerEditPart) {
				SmooksUIUtils.expandGraphTree(expanedTreeNodeList, (InputDataContainerEditPart) object);
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

}
