/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.vpe.editor.menu.action;

import org.eclipse.jface.action.Action;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.w3c.dom.Node;

/**
 * VpeTextOperationAction class.
 *
 * @author Igor Zhukov (izhukov@exadel.com)
 */
public class VpeTextOperationAction extends Action {

	private String id = null;
	private Node region = null;
	private VpePageContext pageContext = null;
	private StructuredTextEditor sourceEditor;

	/**
	 * Constructor.
	 *
	 * @param name the name of action
	 * @param id action id
	 * @param region the Node object
	 * @param pageContext the VpePageContext object
	 * @param sourceEditor 
	 */
	public VpeTextOperationAction(String name, String id, Node region, VpePageContext pageContext, StructuredTextEditor sourceEditor) {
		super(name);
		this.id = id;
		this.region = region;
		this.pageContext = pageContext;
		this.sourceEditor = sourceEditor;
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		SelectionUtil.setSourceSelection(pageContext, region);
		this.sourceEditor.getAction(id).run();
	}
}