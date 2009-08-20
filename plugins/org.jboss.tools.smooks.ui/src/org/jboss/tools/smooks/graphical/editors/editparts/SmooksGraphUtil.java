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
package org.jboss.tools.smooks.graphical.editors.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.gef.common.RootModel;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.model.InputDataContianerModel;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.javabean.ValueType;
import org.jboss.tools.smooks.model.javabean.WiringType;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.javabean12.Javabean12Package;

/**
 * @author Dart
 * 
 */
public class SmooksGraphUtil {

	public static String generateFigureID(AbstractSmooksGraphicalModel model) {
		Object data = model.getData();
		data = AdapterFactoryEditingDomain.unwrap(data);
		if (data instanceof BindingsType) {
			String beanId = ((BindingsType) data).getBeanId();
			if (beanId == null) {
				return null;
			}
			beanId = beanId.trim();
			return "BindingsType_" + beanId;
		}
		
		if (data instanceof BeanType) {
			String beanId = ((BeanType) data).getBeanId();
			if (beanId == null) {
				return null;
			}
			beanId = beanId.trim();
			return "BeanType" + beanId;
		}
		
		if (data instanceof IXMLStructuredObject) {
			List<?> children = ((IXMLStructuredObject) data).getChildren();
			if (children == null || children.isEmpty()) {

			} else {
				Object child = children.get(0);
				if (child != null && child instanceof IXMLStructuredObject) {
					return ((IXMLStructuredObject) child).getID().toString();
				}
			}
			return ((IXMLStructuredObject) data).getID().toString();
		}
		return null;
	}

	public static EStructuralFeature getSelectorFeature(EObject obj) {
		if (obj == null)
			return null;
		// for javabean 1.1
		if (obj instanceof BindingsType) {
			return JavabeanPackage.Literals.BINDINGS_TYPE__CREATE_ON_ELEMENT;
		}
		if(obj instanceof ValueType){
			return JavabeanPackage.Literals.VALUE_TYPE__DATA;
		}
		if(obj instanceof WiringType){
			return JavabeanPackage.Literals.WIRING_TYPE__WIRE_ON_ELEMENT;
		}
		
		// for javabean 1.2
		
		if(obj instanceof BeanType){
			return Javabean12Package.Literals.BEAN_TYPE__CREATE_ON_ELEMENT;
		}
		if(obj instanceof org.jboss.tools.smooks.model.javabean12.ValueType){
			return Javabean12Package.Literals.VALUE_TYPE__DATA;
		}
		if(obj instanceof org.jboss.tools.smooks.model.javabean12.WiringType){
			return Javabean12Package.Literals.WIRING_TYPE__WIRE_ON_ELEMENT;
		}
		return null;
	}

	public static AbstractSmooksGraphicalModel findInputGraphModel(String selector, IXMLStructuredObject root,
			RootModel graphRoot) {
		IXMLStructuredObject model = null;
		model = SmooksUIUtils.localXMLNodeWithPath(selector, root, "/", false);
		if (model == null) {
			SmooksUIUtils.localXMLNodeWithPath(selector, root, " ", false);
		}
		if(model == null) return null;
		IXMLStructuredObject p = model;
		List<IXMLStructuredObject> parentList = new ArrayList<IXMLStructuredObject>();
		IXMLStructuredObject rootParent = SmooksUIUtils.getRootParent(model);
		while (p != null) {
			parentList.add(p);
			if(p == rootParent){
				break;
			}
			p = p.getParent();
			if (p == null) {
				break;
			}
		}
		List<AbstractSmooksGraphicalModel> children = graphRoot.getChildren();
		if (model != null) {
			AbstractSmooksGraphicalModel parentGraph = null;
			for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
				AbstractSmooksGraphicalModel abstractSmooksGraphicalModel = (AbstractSmooksGraphicalModel) iterator
						.next();
				if (abstractSmooksGraphicalModel instanceof InputDataContianerModel) {
					List<AbstractSmooksGraphicalModel> cs = ((InputDataContianerModel) abstractSmooksGraphicalModel)
							.getChildren();
					if (cs.isEmpty()) {
						continue;
					}
					AbstractSmooksGraphicalModel childGraph = cs.get(0);
					if (childGraph.getData() == parentList.get(parentList.size() - 1)) {
						parentGraph = childGraph;
						break;
					}
				}
			}
			int index = parentList.size() - 2;
			if (index < 0) {
				return parentGraph;
			}
			if (parentGraph != null) {
				AbstractSmooksGraphicalModel tempParent = parentGraph;
				for (int i = index; i >= 0; i--) {
					IXMLStructuredObject m = parentList.get(i);
					List<AbstractSmooksGraphicalModel> nc = tempParent.getChildren();
					boolean find = false;
					for (Iterator<?> iterator = nc.iterator(); iterator.hasNext();) {
						AbstractSmooksGraphicalModel abstractSmooksGraphicalModel = (AbstractSmooksGraphicalModel) iterator
								.next();
						if (abstractSmooksGraphicalModel.getData() == m) {
							tempParent = abstractSmooksGraphicalModel;
							find = true;
							break;
						}
					}
					if (!find) {
						return null;
					}
				}
				return tempParent;
			}
		}
		return null;
	}

}
