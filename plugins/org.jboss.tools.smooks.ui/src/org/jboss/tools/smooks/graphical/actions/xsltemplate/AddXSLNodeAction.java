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
package org.jboss.tools.smooks.graphical.actions.xsltemplate;

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.tools.smooks.graphical.actions.AddSmooksModelAction;

/**
 * @author Dart
 * 
 */
public abstract class AddXSLNodeAction extends AddSmooksModelAction {

	public AddXSLNodeAction(IWorkbenchPart part, int style) {
		super(part, style);
		// TODO Auto-generated constructor stub
	}

	public AddXSLNodeAction(IWorkbenchPart part) {
		super(part);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.actions.AddSmooksModelAction#
	 * getCreationFactory()
	 */
	@Override
	protected CreationFactory getCreationFactory() {
		if (isXSLTypeNode()) {
			if (isElementNode()) {
				return XSLElementNodeCreationFactory.newXSLTypeElementCreationFactory(getNodeName());
			}else{
				return XSLElementNodeCreationFactory.newXSLTypeAttributeCreationFactory(getNodeName());
			}
		} else {
			if (isElementNode()) {
				return XSLElementNodeCreationFactory.newNormalElementCreationFactory(getNodeName());
			}else{
				return XSLElementNodeCreationFactory.newNormalAttributeCreationFactory(getNodeName());
			}
		}
	}

	protected abstract String getNodeName();

	protected abstract boolean isXSLTypeNode();

	protected abstract boolean isElementNode();
}
