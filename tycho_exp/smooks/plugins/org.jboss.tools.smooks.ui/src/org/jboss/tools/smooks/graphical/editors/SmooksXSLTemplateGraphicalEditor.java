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

import java.util.List;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.configuration.editors.actions.AbstractSmooksActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.ISmooksActionGrouper;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeContainerModel;
import org.jboss.tools.smooks.graphical.actions.xsltemplate.XSLActionCreator;
import org.jboss.tools.smooks.graphical.editors.autolayout.IAutoLayout;
import org.jboss.tools.smooks.graphical.editors.autolayout.XSLMappingAutoLayout;
import org.jboss.tools.smooks.graphical.editors.model.xsl.XSLTemplateContentProvider;
import org.jboss.tools.smooks.graphical.editors.model.xsl.XSLTemplateGraphicalModel;
import org.jboss.tools.smooks.model.xsl.Xsl;

/**
 * @author Dart
 * 
 */
public class SmooksXSLTemplateGraphicalEditor extends SmooksGraphicalEditorPart {
	
	private XSLMappingAutoLayout autoLayout = null;

	public SmooksXSLTemplateGraphicalEditor(ISmooksModelProvider provider) {
		super(provider);
		// TODO Auto-generated constructor stub
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#getAutoLayout()
	 */
	@Override
	public IAutoLayout getAutoLayout() {
		if(autoLayout == null){
			autoLayout = new XSLMappingAutoLayout();
		}
		return autoLayout;
	}



	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#createActions()
	 */
	@Override
	protected void createActions() {
		super.createActions();
		XSLActionCreator creator = new XSLActionCreator();
		creator.registXSLActions(getActionRegistry(), getSelectionActions(), this);
	}



	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#getPaletteRoot()
	 */
//	@Override
	protected PaletteRoot getPaletteRoot() {
		SmooksGraphicalEditorPaletteRootCreator creator = new SmooksGraphicalEditorPaletteRootCreator(
				this.smooksModelProvider, (AdapterFactoryEditingDomain) this.smooksModelProvider.getEditingDomain(),
				getSmooksResourceListType()){

					/* (non-Javadoc)
					 * @see org.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPaletteRootCreator#fillActionGrouper(java.util.List)
					 */
					@Override
					protected void fillActionGrouper(List<ISmooksActionGrouper> grouperList) {
						AbstractSmooksActionGrouper xslgrouper = new AbstractSmooksActionGrouper() {
							
							public String getGroupName() {
								return "XSL Template";
							}
							
							@Override
							protected boolean canAdd(Object value) {
								if (value instanceof Xsl) {
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
		return new XSLTemplateConnectionModelFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * createGraphicalModelFactory()
	 */
	@Override
	protected GraphicalModelFactory createGraphicalModelFactory() {
		return new XSLTemplateGraphicalModelFactory();
	}

	private class XSLTemplateConnectionModelFactory extends ConnectionModelFactoryImpl {

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
			return false;
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

	private class XSLTemplateGraphicalModelFactory extends GraphicalModelFactoryImpl {
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

				if (model instanceof Xsl) {
					graphModel = new XSLTemplateGraphicalModel(model, new XSLTemplateContentProvider(contentProvider),
							new XSLLabelProvider(labelProvider), provider);
					((TreeContainerModel) graphModel).setHeaderVisable(true);
				}
				if (graphModel != null) {
					return graphModel;
				}
				return super.createGraphicalModel(graphModel, provider);
			}
			return null;
		}

	}

}
