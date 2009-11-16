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

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.configuration.editors.actions.AbstractSmooksActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.ISmooksActionGrouper;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeContainerModel;
import org.jboss.tools.smooks.graphical.editors.autolayout.IAutoLayout;
import org.jboss.tools.smooks.graphical.editors.autolayout.XSLMappingAutoLayout;
import org.jboss.tools.smooks.graphical.editors.editparts.freemarker.CSVLinkConnectionEditPart;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.CSVLinkConnection;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerActionCreator;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerContentProvider;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerLabelProvider;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerTemplateGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.model.javamapping.JavaBeanGraphModel;
import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean12.BeanType;

/**
 * @author Dart
 * 
 */
public class SmooksFreemarkerTemplateGraphicalEditor extends SmooksGraphicalEditorPart {

	public static final String ID = "__smooks_freemarker_template_graphical_editpart";

	private XSLMappingAutoLayout autoLayout = null;

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
		// if(autoLayout == null){
		// autoLayout = new XSLMappingAutoLayout();
		// }
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
						return "Freemarker Template";
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
					graphModel = new FreemarkerTemplateGraphicalModel(model, new FreemarkerContentProvider(
							contentProvider), new FreemarkerLabelProvider(labelProvider), provider);
					((TreeContainerModel) graphModel).setHeaderVisable(true);
				}
				if (model instanceof BindingsType || model instanceof BeanType) {
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
