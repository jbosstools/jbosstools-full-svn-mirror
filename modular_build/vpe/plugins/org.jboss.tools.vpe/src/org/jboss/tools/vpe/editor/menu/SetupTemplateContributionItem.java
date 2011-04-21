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

package org.jboss.tools.vpe.editor.menu;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.menu.action.SetupTemplateAction;
import org.jboss.tools.vpe.editor.template.VpeHtmlTemplate;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.w3c.dom.Element;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class SetupTemplateContributionItem extends ActionContributionItem {

	private VpePageContext pageContext;
	private StructuredTextEditor sourceEditor;
	private final JSPMultiPageEditor editor;
	private final VpeController vpeController;

	/**
	 * Constructor
	 */
	public SetupTemplateContributionItem() {
		super(new SetupTemplateAction());
		editor = (JSPMultiPageEditor) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		this.sourceEditor = editor.getSourceEditor();
		/*
		 * Fixes https://jira.jboss.org/jira/browse/JBIDE-5996
		 * When VisualEditor is not initialized it is impossible
		 * to fill this menu item.
		 */
		this.vpeController = ((VpeEditorPart) editor.getVisualEditor()).getController();
		if (null != vpeController) {
			this.pageContext = vpeController.getPageContext();
		} else {
			this.pageContext = null;
		}
		getAction().setPageContext(pageContext);
	}

	@Override
	public void fill(Menu menu, int index) {
		if (null != vpeController) {
			IStructuredSelection selection = (IStructuredSelection) sourceEditor
				.getSelectionProvider().getSelection();
			if (selection != null && selection.size() == 1
					&& selection.getFirstElement() instanceof Element) {
				Element element = (Element) selection.getFirstElement();
				VpeElementMapping elementMapping = (VpeElementMapping) pageContext
				.getDomMapping().getNodeMapping(element);
				if (elementMapping != null
						&& elementMapping.getTemplate() != null
						&& elementMapping.getTemplate().getType() 
						== VpeHtmlTemplate.TYPE_ANY) {
					
					getAction().setText(NLS.bind(
							VpeUIMessages.SETUP_TEMPLATE_FOR_MENU,
							element.getNodeName()));
					getAction().setActionNode(element);
					getAction().setData(elementMapping.getTemplate().getAnyData());
					/*
					 * https://jira.jboss.org/jira/browse/JBIDE-4541
					 * Index should be used as is.
					 */
					super.fill(menu, index);
				}
			}
		}
	}

	@Override
	public SetupTemplateAction getAction() {
		return (SetupTemplateAction) super.getAction();
	}
}
