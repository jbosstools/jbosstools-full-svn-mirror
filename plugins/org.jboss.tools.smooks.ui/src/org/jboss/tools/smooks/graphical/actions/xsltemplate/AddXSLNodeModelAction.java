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

/**
 * @author Dart
 * 
 */
public class AddXSLNodeModelAction extends AddXSLNodeAction {
	
	private String nodeName;
	
	private String actionText = null;
	
	private ImageDescriptor image = null;

	public AddXSLNodeModelAction(IWorkbenchPart part, int style, String nodeName, String label, ImageDescriptor image) {
		super(part, style);this.setText(label);
		this.nodeName = nodeName;
		this.actionText = label;
		this.image = image;
	}

	public AddXSLNodeModelAction(IWorkbenchPart part, String nodeName, String label, ImageDescriptor image) {
		super(part);
		this.setText(label);
		this.nodeName = nodeName;
		this.actionText = label;
		this.image = image;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.graphical.actions.xsltemplate.AddXSLNodeAction
	 * #getNodeName()
	 */
	@Override
	protected String getNodeName() {
		return nodeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.graphical.actions.xsltemplate.AddXSLNodeAction
	 * #isElementNode()
	 */
	@Override
	protected boolean isElementNode() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.graphical.actions.xsltemplate.AddXSLNodeAction
	 * #isXSLTypeNode()
	 */
	@Override
	protected boolean isXSLTypeNode() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.actions.AddSmooksModelAction#
	 * getActionImageDescriptor()
	 */
	@Override
	public ImageDescriptor getActionImageDescriptor() {
		return image;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.graphical.actions.AddSmooksModelAction#getActionText
	 * ()
	 */
	@Override
	public String getActionText() {
		return actionText;
	}

}
