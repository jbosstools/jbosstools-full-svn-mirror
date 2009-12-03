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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.template.CollectionMapping;
import org.jboss.template.Mapping;
import org.jboss.template.TemplateBuilder;
import org.jboss.template.csv.CSVFreeMarkerTemplateBuilder;
import org.jboss.template.csv.CSVModelBuilder;
import org.jboss.tools.smooks.configuration.editors.actions.AbstractSmooksActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.ISmooksActionGrouper;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.common.RootModel;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeContainerModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.graphical.editors.autolayout.IAutoLayout;
import org.jboss.tools.smooks.graphical.editors.editparts.SmooksGraphUtil;
import org.jboss.tools.smooks.graphical.editors.editparts.freemarker.CSVLinkConnectionEditPart;
import org.jboss.tools.smooks.graphical.editors.editparts.freemarker.FreemarkerAutoLayout;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.CSVLinkConnection;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.CSVNodeModel;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerActionCreator;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerCSVNodeGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerContentProvider;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerLabelProvider;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerTemplateGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.model.javamapping.JavaBeanGraphModel;
import org.jboss.tools.smooks.graphical.editors.process.TaskType;
import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks.model.freemarker.Template;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Dart
 * 
 */
public class SmooksFreemarkerTemplateGraphicalEditor extends SmooksGraphicalEditorPart {

	public static final String ID = "__smooks_freemarker_template_graphical_editpart"; //$NON-NLS-1$

	private IAutoLayout autoLayout = null;

	public SmooksFreemarkerTemplateGraphicalEditor(ISmooksModelProvider provider) {
		super(provider);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * createEdtiPartFactory()
	 */
	@Override
	protected EditPartFactory createEdtiPartFactory() {
		FreemarkerTemplateEditFactory factory = new FreemarkerTemplateEditFactory();
		((FreemarkerTemplateEditFactory) factory).setDisplayInput(false);
		return factory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * getAutoLayout()
	 */
	@Override
	public IAutoLayout getAutoLayout() {
		if (autoLayout == null) {
			autoLayout = new FreemarkerAutoLayout();
		}
		return autoLayout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * createActions()
	 */
	@Override
	protected void createActions() {
		super.createActions();
		FreemarkerActionCreator creator = new FreemarkerActionCreator();
		creator.registXSLActions(getActionRegistry(), getSelectionActions(), this, this.smooksModelProvider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * getPaletteRoot()
	 */
	protected PaletteRoot getPaletteRoot() {
		SmooksGraphicalEditorPaletteRootCreator creator = new SmooksGraphicalEditorPaletteRootCreator(
				this.smooksModelProvider, (AdapterFactoryEditingDomain) this.smooksModelProvider.getEditingDomain(),
				getSmooksResourceListType()) {

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.jboss.tools.smooks.graphical.editors.
			 * SmooksGraphicalEditorPaletteRootCreator
			 * #createConnectionCreationFactory()
			 */
			@Override
			protected CreationFactory createConnectionCreationFactory() {
				return new CreationFactory() {

					public Object getObjectType() {
						return CSVLinkConnection.class;
					}

					public Object getNewObject() {
						return null;
					}
				};
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.jboss.tools.smooks.graphical.editors.
			 * SmooksGraphicalEditorPaletteRootCreator
			 * #fillActionGrouper(java.util.List)
			 */
			@Override
			protected void fillActionGrouper(List<ISmooksActionGrouper> grouperList) {
				AbstractSmooksActionGrouper xslgrouper = new AbstractSmooksActionGrouper() {

					public String getGroupName() {
						return "Freemarker Template"; //$NON-NLS-1$
					}

					@Override
					protected boolean canAdd(Object value) {
						if (value instanceof Freemarker) {
							return true;
						}
						return false;
					}
				};
				grouperList.add(xslgrouper);
			}

		};
		return creator.createPaletteRoot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * createConnectionModelFactory()
	 */
	@Override
	protected ConnectionModelFactory createConnectionModelFactory() {
		return new FreemarkerTemplateConnectionModelFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * createGraphicalModelFactory()
	 */
	@Override
	protected GraphicalModelFactory createGraphicalModelFactory() {
		return new FreemarkerTemplateGraphicalModelFactory();
	}

	private class FreemarkerTemplateConnectionModelFactory extends ConnectionModelFactoryImpl {

		private List<Mapping> mappingList = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.jboss.tools.smooks.graphical.editors.ConnectionModelFactoryImpl
		 * #hasBeanIDConnection
		 * (org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel)
		 */
		@Override
		public boolean hasBeanIDConnection(AbstractSmooksGraphicalModel model) {
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.jboss.tools.smooks.graphical.editors.ConnectionModelFactoryImpl
		 * #hasSelectorConnection
		 * (org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel)
		 */
		@Override
		public boolean hasSelectorConnection(AbstractSmooksGraphicalModel model) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.jboss.tools.smooks.graphical.editors.ConnectionModelFactoryImpl
		 * #createConnection(java.util.List, org.eclipse.emf.ecore.EObject,
		 * org.jboss.tools.smooks.gef.common.RootModel,
		 * org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel)
		 */
		@Override
		public Collection<TreeNodeConnection> createConnection(List<Object> inputDataList, EObject rootModel,
				RootModel root, AbstractSmooksGraphicalModel model) {
			List<TreeNodeConnection> collections = new ArrayList<TreeNodeConnection>();

			if (model instanceof FreemarkerCSVNodeGraphicalModel) {
				CSVNodeModel data = (CSVNodeModel) model.getData();
				AbstractSmooksGraphicalModel freemarkerGraphModel = model.getParent();
				if (!(freemarkerGraphModel instanceof FreemarkerTemplateGraphicalModel)) {
					freemarkerGraphModel = freemarkerGraphModel.getParent();
				}

				if (freemarkerGraphModel instanceof FreemarkerTemplateGraphicalModel) {
					Freemarker freemarker = (Freemarker) AdapterFactoryEditingDomain.unwrap(freemarkerGraphModel
							.getData());
					// Template template = freemarker.getTemplate();
					fillMapping(freemarker);
				}
				AbstractSmooksGraphicalModel sourceNode = null;
				AbstractSmooksGraphicalModel targetNode = model;
				SmooksResourceListType listType = SmooksUIUtils.getSmooks11ResourceListType(smooksModelProvider
						.getSmooksModel());
				List<EObject> beanidModels = new ArrayList<EObject>();
				if (listType != null) {
					SmooksUIUtils.fillBeanIdModelList(listType, beanidModels);
				}
				for (Iterator<?> iterator = mappingList.iterator(); iterator.hasNext();) {
					Mapping mapping = (Mapping) iterator.next();
					String path = mapping.getSrcPath();
					Node node = mapping.getMappingNode();
					String nodeName = node.getNodeName();
					if (data.isRecord()) {
						if (mapping instanceof CollectionMapping) {
							for (Iterator<?> iterator2 = beanidModels.iterator(); iterator2.hasNext();) {
								EObject eObject = (EObject) iterator2.next();
								EStructuralFeature feature = SmooksUIUtils.getBeanIDFeature(eObject);
								if (feature != null) {
									String beanid = (String) eObject.eGet(feature);
									if (path.equals(beanid)) {
										sourceNode = SmooksGraphUtil.findSmooksGraphModel((RootModel) root, eObject);
										if (sourceNode != null && targetNode != null) {
											TreeNodeConnection connection = new TreeNodeConnection(sourceNode,
													targetNode);
											connection.connectSource();
											targetNode.getTargetConnections().add(connection);
											targetNode.fireConnectionChanged();
											collections.add(connection);
											break;
										}
									}
								}
							}
						}
					} else {
						if (nodeName.equals(data.getName())) {
							String[] subpath = path.split("\\."); //$NON-NLS-1$
							if (subpath.length >= 2) {
								String[] temppath = new String[2];
								System.arraycopy(subpath, subpath.length - 2, temppath, 0, 2);
								Object beanModel = findJavaBeanModel(temppath[0], beanidModels);
								if (beanModel != null) {
									Object targetModel = findJavaGraphModel(temppath[1], beanModel);
									sourceNode = SmooksGraphUtil.findSmooksGraphModel((RootModel) root, targetModel);
									if (sourceNode != null && targetNode != null) {
										TreeNodeConnection connection = new TreeNodeConnection(sourceNode, targetNode);
										connection.connectSource();
										targetNode.getTargetConnections().add(connection);
										targetNode.fireConnectionChanged();
										collections.add(connection);
									}
								}
							}
						}
					}
				}
			}

			Collection<TreeNodeConnection> cs = super.createConnection(inputDataList, rootModel, root, model);
			if (cs != null) {
				collections.addAll(cs);
			}
			return collections;
		}

		private Object findJavaGraphModel(String propertyName, Object bean) {
			if (bean instanceof BeanType) {
				List<?> values = ((BeanType) bean).getValue();
				for (Iterator<?> iterator = values.iterator(); iterator.hasNext();) {
					org.jboss.tools.smooks.model.javabean12.ValueType value = (org.jboss.tools.smooks.model.javabean12.ValueType) iterator
							.next();
					if (propertyName.equals(value.getProperty())) {
						return value;
					}
				}
			}
//			if (bean instanceof BindingsType) {
//				List<?> values = ((BindingsType) bean).getValue();
//				for (Iterator<?> iterator = values.iterator(); iterator.hasNext();) {
//					ValueType value = (ValueType) iterator.next();
//					if (propertyName.equals(value.getProperty())) {
//						return value;
//					}
//				}
//			}
			return null;
		}

		private Object findJavaBeanModel(String beanid, List<EObject> beans) {
			for (Iterator<?> iterator = beans.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
//				if (object instanceof BindingsType) {
//					if (beanid.equals(((BindingsType) object).getBeanId())) {
//						return object;
//					}
//				}
				if (object instanceof BeanType) {
					if (beanid.equals(((BeanType) object).getBeanId())) {
						return object;
					}
				}
			}
			return null;
		}

		private void fillMapping(Freemarker freemarker) {
			if (mappingList == null) {
				mappingList = new ArrayList<Mapping>();
			} else {
				mappingList.clear();
			}
			Template template = freemarker.getTemplate();
			String contents = SmooksModelUtils.getAnyTypeCDATA(template);
			char seprator = SmooksModelUtils.getFreemarkerCSVSeperator(freemarker);
			char quote = SmooksModelUtils.getFreemarkerCSVQuote(freemarker);
			String[] fields = SmooksModelUtils.getFreemarkerCSVFileds(freemarker);
			try {
				if(contents != null) {
					CSVModelBuilder modelBuilder = new CSVModelBuilder(fields);
					Document model = modelBuilder.buildModel();
					TemplateBuilder builder = new CSVFreeMarkerTemplateBuilder(model, seprator, quote, contents);
					List<Mapping> mappings = builder.getMappings();
					mappingList.addAll(mappings);
				}
			} catch (Exception e) {
				// ignore exception
//				e.printStackTrace();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.jboss.tools.smooks.graphical.editors.ConnectionModelFactoryImpl
		 * #hasConnection
		 * (org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel)
		 */
		@Override
		public boolean hasConnection(AbstractSmooksGraphicalModel model) {
			if (model instanceof FreemarkerCSVNodeGraphicalModel) {
				return true;
			}
			return super.hasConnection(model);
		}
	}

	private class FreemarkerTemplateGraphicalModelFactory extends GraphicalModelFactoryImpl {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.jboss.tools.smooks.graphical.editors.GraphicalModelFactoryImpl
		 * #createGraphicalModel(java.lang.Object,
		 * org.jboss.tools.smooks.editor.ISmooksModelProvider)
		 */
		@Override
		public Object createGraphicalModel(Object model, ISmooksModelProvider provider) {
			if (canCreateGraphicalModel(model, provider)) {
				AbstractSmooksGraphicalModel graphModel = null;
				AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) provider.getEditingDomain();
				ITreeContentProvider contentProvider = new AdapterFactoryContentProvider(editingDomain
						.getAdapterFactory());
				ILabelProvider labelProvider = createLabelProvider(editingDomain.getAdapterFactory());

				if (model instanceof Freemarker) {
					// Template template = ((Freemarker) model).getTemplate();
					String messageType = SmooksModelUtils.getTemplateType((Freemarker) model);
					if (messageType != null && SmooksModelUtils.FREEMARKER_TEMPLATE_TYPE_CSV.equals(messageType)) {
						TaskType taskType = getTaskType();
						if (taskType.inTheTask(model)) {
							graphModel = new FreemarkerTemplateGraphicalModel(model, new FreemarkerContentProvider(
									contentProvider), new FreemarkerLabelProvider(labelProvider), provider);
							((TreeContainerModel) graphModel).setHeaderVisable(true);
							((FreemarkerTemplateGraphicalModel) graphModel)
									.setTemplateType(FreemarkerTemplateGraphicalModel.TYPE_CSV);
						}
					}
				}
				if (model instanceof BeanType) {
					graphModel = new JavaBeanGraphModel(model, contentProvider, labelProvider, provider,
							SmooksFreemarkerTemplateGraphicalEditor.this);
					((JavaBeanGraphModel) graphModel).setHeaderVisable(true);
				}
				if (graphModel != null) {
					return graphModel;
				}
				return super.createGraphicalModel(graphModel, provider);
			}
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#getID
	 * ()
	 */
	@Override
	public String getID() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * createInputDataGraphModel()
	 */
	@Override
	protected List<AbstractSmooksGraphicalModel> createInputDataGraphModel() {
		return Collections.emptyList();
	}

	private class FreemarkerTemplateEditFactory extends SmooksEditFactory {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.jboss.tools.smooks.graphical.editors.SmooksEditFactory#createEditPart
		 * (org.eclipse.gef.EditPart, java.lang.Object)
		 */
		@Override
		public EditPart createEditPart(EditPart context, Object model) {
			EditPart editPart = null;
			if (model.getClass() == CSVLinkConnection.class) {
				editPart = new CSVLinkConnectionEditPart();
				editPart.setModel(model);
				return editPart;
			}
			return super.createEditPart(context, model);
		}

	}
}
