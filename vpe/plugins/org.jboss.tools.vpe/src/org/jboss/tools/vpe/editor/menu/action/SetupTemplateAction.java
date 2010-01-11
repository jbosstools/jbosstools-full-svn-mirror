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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.editor.template.VpeEditAnyDialog;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Node;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class SetupTemplateAction extends Action {

	private Node actionNode;

	private VpePageContext pageContext;

	public void setActionNode(Node actionNode) {
		this.actionNode = actionNode;
	}

	public void setPageContext(VpePageContext pageContext) {
		this.pageContext = pageContext;
	}

	public void setData(VpeAnyData data) {
		this.data = data;
	}

	private VpeAnyData data;

	/**
	 * @param text
	 */
	public SetupTemplateAction(String title, Node actionNode, VpeAnyData data,
			VpePageContext pageContext) {
		super(title);
		this.actionNode = actionNode;
		this.pageContext = pageContext;
		this.data = data;

	}
	
	public SetupTemplateAction(VpePageContext pageContext) {
		this.pageContext = pageContext;
	}
	
	public SetupTemplateAction() {
	}

	@Override
	public void run() {
		boolean isCorrectNS = pageContext.isCorrectNS(actionNode);
		if (isCorrectNS) {
			data.setUri(pageContext.getSourceTaglibUri(actionNode));
			data.setName(actionNode.getNodeName());
		}
		
		Shell shell = PlatformUI
		.getWorkbench().getActiveWorkbenchWindow().getShell();
		
		if (isCorrectNS) {
			VpeEditAnyDialog editDialog = new VpeEditAnyDialog(shell, data);
			editDialog.open();
		} else {
			MessageBox message = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
			message.setMessage(VpeUIMessages.NAMESPACE_NOT_DEFINED);
			message.setText("Warning"); //$NON-NLS-1$
			message.open();
		}
		if (data != null && data.isChanged())
			VpeTemplateManager.getInstance().setAnyTemplate(data);
	}
}
