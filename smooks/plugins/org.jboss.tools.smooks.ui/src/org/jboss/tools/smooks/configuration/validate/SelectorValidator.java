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
package org.jboss.tools.smooks.configuration.validate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.SelectorCreationDialog;
import org.jboss.tools.smooks.configuration.editors.groovy.GroovyUICreator;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.calc.CalcPackage;
import org.jboss.tools.smooks.model.calc.Counter;
import org.jboss.tools.smooks.model.datasource.DatasourcePackage;
import org.jboss.tools.smooks.model.datasource.Direct;
import org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage;
import org.jboss.tools.smooks.model.esbrouting.RouteBean;
import org.jboss.tools.smooks.model.fileRouting.FileRoutingPackage;
import org.jboss.tools.smooks.model.fileRouting.OutputStream;
import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks.model.freemarker.FreemarkerPackage;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.groovy.Groovy;
import org.jboss.tools.smooks.model.groovy.GroovyPackage;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.ExpressionType;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.javabean.ValueType;
import org.jboss.tools.smooks.model.javabean.WiringType;
import org.jboss.tools.smooks.model.jmsrouting.JmsRouter;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.ResourceConfigType;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks.model.xsl.Xsl;
import org.jboss.tools.smooks.model.xsl.XslPackage;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class SelectorValidator extends AbstractValidator {

	private SmooksGraphicsExtType extType = null;

	private List<Object> list = new ArrayList<Object>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.validate.AbstractValidator#validate
	 * (java.util.Collection, org.eclipse.emf.edit.domain.EditingDomain)
	 */
	@Override
	public List<Diagnostic> validate(Collection<?> selectedObjects, EditingDomain editingDomain) {
		return super.validate(selectedObjects, editingDomain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.validate.AbstractValidator#validateModel
	 * (java.lang.Object, org.eclipse.emf.edit.domain.EditingDomain)
	 */
	@Override
	protected Diagnostic validateModel(Object model, EditingDomain editingDomain) {
		EAttribute feature = getAttribute(model);
		String path = getPath(model);
		if (path == null) {
			return null;
		}
		// if(feature != null && path == null){
		// return newWaringDiagnostic("Selector '" +path+ "' isn't available",
		// model, feature);
		// }
		String sperator = "/";
		if (path.indexOf('/') == -1) {
			sperator = " ";
		}
		if (feature != null && path != null) {
			Object node = null;
			for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
				Object obj = (Object) iterator.next();
				if (obj instanceof IXMLStructuredObject) {
					if (node == null) {
						try {
							node = SmooksUIUtils
									.localXMLNodeWithPath(path, (IXMLStructuredObject) obj, sperator, false);
						} catch (Throwable e) {
							SmooksConfigurationActivator.getDefault().log(e);
						}
					}
					if (node != null) {
						return null;
					}
				}
			}
			if (node == null) {
				return newWaringDiagnostic("Selector '" + path + "' isn't available", model, feature);
			}
		}
		return super.validateModel(model, editingDomain);
	}

	private EAttribute getAttribute(Object model) {
		if (model instanceof BindingsType) {
			return JavabeanPackage.Literals.BINDINGS_TYPE__CREATE_ON_ELEMENT;
		}
		if (model instanceof Counter) {
			return CalcPackage.Literals.COUNTER__COUNT_ON_ELEMENT;
		}
		if (model instanceof Direct) {
			return DatasourcePackage.Literals.DIRECT__BIND_ON_ELEMENT;
		}
		if (model instanceof RouteBean) {
			return EsbroutingPackage.Literals.ROUTE_BEAN__ROUTE_ON_ELEMENT;
		}
		if (model instanceof OutputStream) {
			return FileRoutingPackage.Literals.OUTPUT_STREAM__OPEN_ON_ELEMENT;
		}
		if (model instanceof Freemarker) {
			return FreemarkerPackage.Literals.FREEMARKER__APPLY_ON_ELEMENT;
		}
		if (model instanceof Xsl) {
			return XslPackage.Literals.XSL__APPLY_ON_ELEMENT;
		}
		if (model instanceof GroovyUICreator) {
			return GroovyPackage.Literals.GROOVY__EXECUTE_ON_ELEMENT;
		}
		if (model instanceof JmsRouter) {
			return JmsroutingPackage.Literals.JMS_ROUTER__ROUTE_ON_ELEMENT;
		}

		if (model instanceof ResourceConfigType) {
			return SmooksPackage.Literals.RESOURCE_CONFIG_TYPE__SELECTOR;
		}

		if (model instanceof SmooksResourceListType) {
			return SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR;
		}

		if (model instanceof WiringType) {
			return JavabeanPackage.Literals.WIRING_TYPE__WIRE_ON_ELEMENT;
		}
		if (model instanceof ExpressionType) {
			return JavabeanPackage.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT;
		}
		if (model instanceof ValueType) {
			return JavabeanPackage.Literals.VALUE_TYPE__DATA;
		}
		return null;
	}

	private String getPath(Object model) {
		if (model instanceof ExpressionType) {
			return ((ExpressionType) model).getExecOnElement();
		}
		if (model instanceof ValueType) {
			return ((ValueType) model).getData();
		}
		if (model instanceof WiringType) {
			return ((WiringType) model).getWireOnElement();
		}
		if (model instanceof SmooksResourceListType) {
			return ((SmooksResourceListType) model).getDefaultSelector();
		}
		if (model instanceof ResourceConfigType) {
			return ((ResourceConfigType) model).getSelector();
		}
		if (model instanceof JmsRouter) {
			return ((JmsRouter) model).getRouteOnElement();
		}
		if (model instanceof GroovyUICreator) {
			return ((Groovy) model).getExecuteOnElement();
		}
		if (model instanceof Xsl) {
			return ((Xsl) model).getApplyOnElement();
		}
		if (model instanceof Counter) {
			return ((Counter) model).getCountOnElement();
		}
		if (model instanceof BindingsType) {
			return ((BindingsType) model).getCreateOnElement();
		}
		if (model instanceof Direct) {
			return ((Direct) model).getBindOnElement();
		}
		if (model instanceof RouteBean) {
			return ((RouteBean) model).getRouteOnElement();
		}
		if (model instanceof OutputStream) {
			return ((OutputStream) model).getOpenOnElement();
		}
		if (model instanceof Freemarker) {
			return ((Freemarker) model).getApplyOnElement();
		}
		return null;
	}

	public void initValidator(Collection<?> selectedObjects, EditingDomain editingDomain) {
		list.clear();
		Resource resource = editingDomain.getResourceSet().getResources().get(0);
		if (resource.getContents().isEmpty()) {
			return;
		}
		Object obj = resource.getContents().get(0);
		SmooksResourceListType listType = null;
		if (obj instanceof DocumentRoot) {
			listType = ((DocumentRoot) obj).getSmooksResourceList();
			IResource r = SmooksUIUtils.getResource(listType);
			IFile file = null;
			if (r instanceof IFile) {
				file = (IFile) r;
			}
			final IFile ff = file;
			String extName = ff.getName() + ".ext";

			IFile extFile = ff.getParent().getFile(new Path(extName));
			try {
				extType = SmooksUIUtils.loadSmooksGraphicsExt(extFile);
			} catch (IOException e) {
				// ignore
			}
		}
		if (extType != null) {
			List<Object> l = SelectorCreationDialog.generateInputData(extType, listType);
			if (l != null) {
				list.addAll(l);
			}
		}
	}

}
