/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.toolbar.format.handler;

import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.selection.VpeSelectedNodeInfo;
import org.jboss.tools.vpe.editor.template.textformating.FormatData;

/**
 * @author Igels
 */
public interface IAddNodeHandler {

	/**
	 * Add children.
	 * @param formatData is Format Data of node.
	 * @param node is Node which is adding children.
	 * @param selectedNode is Node which is selected.
	 */
	public void run(FormatData formatData, Node node, VpeSelectedNodeInfo selectedNode);
}