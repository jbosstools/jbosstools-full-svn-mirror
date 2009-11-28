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

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.configuration.editors.actions.ISmooksActionGrouper;
import org.jboss.tools.smooks.configuration.editors.actions.JavaBean11ActionGrouper;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.autolayout.IAutoLayout;
import org.jboss.tools.smooks.graphical.editors.autolayout.JavaMappingAutoLayout;
import org.jboss.tools.smooks.graphical.editors.model.javamapping.JavaBeanGraphModel;
import org.jboss.tools.smooks.graphical.editors.model.javamapping.JavaMappingActionCreator;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.ExpressionType;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.javabean.ValueType;
import org.jboss.tools.smooks.model.javabean.WiringType;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.javabean12.Javabean12Package;

/**
 * @author Dart
 * 
 */
public class SmooksJavaMappingGraphicalEditor extends SmooksGraphicalEditorPart {

	private IAutoLayout javaMappingAutoLayout;

	public SmooksJavaMappingGraphicalEditor(ISmooksModelProvider provider) {
		super(provider);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * getDiagnosticMessage(org.eclipse.emf.common.util.Diagnostic)
	 */
	@Override
	protected String getDiagnosticMessage(Diagnostic diagnostic) {
		List<?> datas = diagnostic.getData();
		if (datas.size() == 2) {
			Object parentObj = datas.get(0);

			if (parentObj instanceof BeanType || parentObj instanceof BindingsType) {
				Object obj = datas.get(1);
				if (obj == JavabeanPackage.Literals.BINDINGS_TYPE__BEAN_ID
						|| obj == Javabean12Package.Literals.BEAN_TYPE__BEAN_ID) {
					String message = diagnostic.getMessage();
					if (message != null && message.startsWith("The required feature")) {
						return "The Bean ID shouldn't be empty";
					}
				}
			}

			if (parentObj instanceof ValueType
					|| parentObj instanceof org.jboss.tools.smooks.model.javabean12.ValueType) {
				Object obj = datas.get(1);
				if (obj == JavabeanPackage.Literals.VALUE_TYPE__DATA
						|| obj == Javabean12Package.Literals.VALUE_TYPE__DATA) {
					String message = diagnostic.getMessage();
					if (message != null && message.startsWith("The required feature")) {
						return "The node must be linked with input source";
					}
				}
			}

			if (parentObj instanceof WiringType
					|| parentObj instanceof org.jboss.tools.smooks.model.javabean12.WiringType) {
				Object obj = datas.get(1);
				if (obj == JavabeanPackage.Literals.WIRING_TYPE__BEAN_ID_REF
						|| obj == Javabean12Package.Literals.WIRING_TYPE__BEAN_ID_REF) {
					String message = diagnostic.getMessage();
					if (message != null && message.startsWith("The required feature")) {
						return "The node must link to another Java Bean";
					}
				}
			}
		}
		return super.getDiagnosticMessage(diagnostic);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * getPaletteRoot()
	 */
	// @Override
	protected PaletteRoot getPaletteRoot() {
		SmooksGraphicalEditorPaletteRootCreator creator = new SmooksGraphicalEditorPaletteRootCreator(
				this.smooksModelProvider, (AdapterFactoryEditingDomain) this.smooksModelProvider.getEditingDomain(),
				getSmooksResourceListType()) {

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.jboss.tools.smooks.graphical.editors.
			 * SmooksGraphicalEditorPaletteRootCreator
			 * #fillActionGrouper(java.util.List)
			 */
			@Override
			protected void fillActionGrouper(List<ISmooksActionGrouper> grouperList) {
				grouperList.add(new JavaBean11ActionGrouper());
			}

		};
		return creator.createPaletteRoot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * getAutoLayout()
	 */
	@Override
	public IAutoLayout getAutoLayout() {
		if (javaMappingAutoLayout == null) {
			javaMappingAutoLayout = new JavaMappingAutoLayout();
		}
		return javaMappingAutoLayout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * createConnectionModelFactory()
	 */
	@Override
	protected ConnectionModelFactory createConnectionModelFactory() {
		return new JavaMappingConnectionModelFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart#
	 * createGraphicalModelFactory()
	 */
	@Override
	protected GraphicalModelFactory createGraphicalModelFactory() {
		return new JavaMappingGraphicalModelFactory();
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
		JavaMappingActionCreator creator = new JavaMappingActionCreator();
		creator.registXSLActions(getActionRegistry(), getSelectionActions(), this, this.getSmooksModelProvider());
	}

	private class JavaMappingConnectionModelFactory extends ConnectionModelFactoryImpl {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.jboss.tools.smooks.graphical.editors.ConnectionModelFactoryImpl
		 * #hasXSLConnection
		 * (org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel)
		 */
		@Override
		public boolean hasXSLConnection(AbstractSmooksGraphicalModel model) {
			return false;
		}

	}

	private class JavaMappingGraphicalModelFactory extends GraphicalModelFactoryImpl {
		protected String getGraphLabelText(Object element) {
			Object obj = AdapterFactoryEditingDomain.unwrap(element);
			if (obj instanceof BeanType) {
				String p = ((BeanType) obj).getBeanId();
				if (p == null) {
					p = "<NULL>";
				}
				return p;
			}
			if (obj instanceof BindingsType) {
				String p = ((BindingsType) obj).getBeanId();
				if (p == null) {
					p = "<NULL>";
				}
				return p;
			}

			if (obj instanceof ValueType) {
				String p = ((ValueType) obj).getProperty();
				if (p == null) {
					p = "<NULL>";
				}
				return p;
			}
			if (obj instanceof WiringType) {
				String p = ((WiringType) obj).getProperty();
				if (p == null) {
					p = "<NULL>";
				}
				return p;
			}
			if (obj instanceof ExpressionType) {
				String p = ((ExpressionType) obj).getProperty();
				if (p == null) {
					p = "<NULL>";
				}
				return p;
			}

			if (obj instanceof org.jboss.tools.smooks.model.javabean12.ValueType) {
				String p = ((org.jboss.tools.smooks.model.javabean12.ValueType) obj).getProperty();
				if (p == null) {
					p = "<NULL>";
				}
				return p;
			}
			if (obj instanceof org.jboss.tools.smooks.model.javabean12.WiringType) {
				String p = ((org.jboss.tools.smooks.model.javabean12.WiringType) obj).getProperty();
				if (p == null) {
					p = "<NULL>";
				}
				return p;
			}
			if (obj instanceof org.jboss.tools.smooks.model.javabean12.ExpressionType) {
				String p = ((org.jboss.tools.smooks.model.javabean12.ExpressionType) obj).getProperty();
				if (p == null) {
					p = "<NULL>";
				}
				return p;
			}
			return null;
		}

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

				if (model instanceof BindingsType || model instanceof BeanType) {
					graphModel = new JavaBeanGraphModel(model, contentProvider, labelProvider, provider,
							SmooksJavaMappingGraphicalEditor.this);
					((JavaBeanGraphModel) graphModel).setHeaderVisable(true);
				}
				// if (model instanceof Xsl) {
				// graphModel = new XSLTemplateGraphicalModel(model, new
				// XSLTemplateContentProvider(contentProvider),
				// new XSLLabelProvider(labelProvider), provider);
				// ((TreeContainerModel) graphModel).setHeaderVisable(true);
				// }
				// if (graphModel == null && model instanceof
				// AbstractResourceConfig) {
				// graphModel = new ResourceConfigGraphModelImpl(model,
				// contentProvider, labelProvider, provider);
				// ((ResourceConfigGraphModelImpl)
				// graphModel).setHeaderVisable(true);
				// }
				if (graphModel != null) {
					return graphModel;
				}
				return super.createGraphicalModel(graphModel, provider);
			}
			return null;
		}

	}

}
