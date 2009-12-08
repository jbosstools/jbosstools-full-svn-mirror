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
package org.jboss.tools.smooks.graphical.editors.editparts.xsl;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.configuration.editors.xml.XMLUtils;
import org.jboss.tools.smooks.configuration.editors.xml.XSLModelAnalyzer;
import org.jboss.tools.smooks.configuration.editors.xml.XSLTagObject;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.graphical.actions.xsltemplate.XSLConstants;
import org.jboss.tools.smooks.graphical.editors.commands.AddSmooksGraphicalModelCommand;
import org.jboss.tools.smooks.graphical.editors.editparts.AbstractResourceConfigEditPart;
import org.jboss.tools.smooks.graphical.editors.model.AbstractResourceConfigGraphModel;
import org.jboss.tools.smooks.graphical.editors.model.xsl.XSLNodeGraphicalModel;
import org.jboss.tools.smooks.model.xsl.XslPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Dart
 * 
 */
public class XSLTemplateEditPart extends AbstractResourceConfigEditPart {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.editparts.
	 * AbstractResourceConfigEditPart#getFeature(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected EStructuralFeature getHostFeature(EObject model) {
		return XslPackage.Literals.DOCUMENT_ROOT__XSL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.editparts.
	 * AbstractResourceConfigEditPart#getCreateCommand(org.eclipse.gef.EditPart,
	 * org.eclipse.gef.requests.CreateRequest)
	 */
	@Override
	protected Command getCreateCommand(EditPart host, CreateRequest request) {
		Object model = request.getNewObject();
		Object graphModel = host.getModel();
		if (graphModel instanceof AbstractResourceConfigGraphModel) {
			if (!((AbstractResourceConfigGraphModel) graphModel).getChildrenWithoutDynamic().isEmpty()) {
				return null;
			}
			if (model instanceof XSLTagObject) {
				ILabelProvider provider = ((AbstractResourceConfigGraphModel) graphModel).getLabelProvider();
				ITreeContentProvider provider1 = ((AbstractResourceConfigGraphModel) graphModel).getContentProvider();
				IEditingDomainProvider provider2 = ((AbstractResourceConfigGraphModel) graphModel).getDomainProvider();
				XSLNodeGraphicalModel childGraphModel = new XSLNodeGraphicalModel(model, provider1, provider, provider2);

				Document documentRoot = null;
				try {
					documentRoot = XMLUtils.createDocument();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}

				String name = ((XSLTagObject) model).getName();
				String namespace = ((XSLTagObject) model).getNamespaceURI();
				String namespaceprefix = ((XSLTagObject) model).getNameSpacePrefix();

				if (XSLModelAnalyzer.isXSLTagObject((XSLTagObject) model)) {
					if (!XSLConstants.STYLESHEET.equals(name)) {
						return null;
					}
				}

				if (documentRoot != null) {

					Element element = documentRoot.createElementNS(namespace, name);

					documentRoot.appendChild(element);

					((XSLTagObject) model).setReferenceElement(element);
					AddSmooksGraphicalModelCommand command = new AddSmooksGraphicalModelCommand(
							(AbstractSmooksGraphicalModel) graphModel, childGraphModel);
					return command;
				}
			}
		}
		return null;
	}
}
