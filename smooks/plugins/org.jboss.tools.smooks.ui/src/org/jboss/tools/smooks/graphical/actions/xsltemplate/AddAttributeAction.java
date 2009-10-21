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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;

/**
 * @author Dart
 *
 */
public class AddAttributeAction extends AddXSLNodeAction {

	public AddAttributeAction(IWorkbenchPart part, int style) {
		super(part, style);
		// TODO Auto-generated constructor stub
	}

	public AddAttributeAction(IWorkbenchPart part) {
		super(part);
		// TODO Auto-generated constructor stub
	}
	

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.graphical.actions.xsltemplate.AddXSLNodeAction#getActionImageDescriptor()
	 */
	@Override
	public ImageDescriptor getActionImageDescriptor() {
		return SmooksConfigurationActivator.getImageDescriptor(GraphicsConstants.IMAGE_XML_ATTRIBUTE);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.graphical.actions.AddSmooksModelAction#getActionText()
	 */
	@Override
	public String getActionText() {
		return "Add Attribute";
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.graphical.actions.xsltemplate.AddXSLNodeAction#getNodeName()
	 */
	@Override
	protected String getNodeName() {
		return "attribute";
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.graphical.actions.xsltemplate.AddXSLNodeAction#isElementNode()
	 */
	@Override
	protected boolean isElementNode() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.graphical.actions.xsltemplate.AddXSLNodeAction#isXSLTypeNode()
	 */
	@Override
	protected boolean isXSLTypeNode() {
		return false;
	}
}
