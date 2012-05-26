/*******************************************************************************
 * Copyright (c) 2007-2011 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.toolbar.format;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.jst.css.dialog.CSSStyleDialog;
import org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author dmaliarevich
 * 
 * CSSStyleDialog should be on the Formatting toolbar.
 * https://issues.jboss.org/browse/JBIDE-8220
 */
public class StyleFormatController extends AttributeFormatController {
	
	public static final String TYPE = "StyleFormat"; //$NON-NLS-1$
	
	protected ToolItem toolItem;
	private String styleString;
	
	public StyleFormatController(FormatControllerManager manager, ToolItem toolItem) {
		super(manager);
		this.toolItem = toolItem;
		setToolbarItemEnabled(false);
		styleString = null;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public void setToolbarItemEnabled(boolean enabled) {
		toolItem.setEnabled(enabled);
	}

	@Override
	public void handleEvent(Event event) {
		/*
		 * Reset values from previous execution
		 */
		styleString = null;
		String baseStyle = ""; //$NON-NLS-1$
		Node node = manager.getCurrentSelectedNode();
		/*
		 * Try to get style attribute value
		 */
		if (node instanceof Element) {
			Element element =  (Element) node;
			if (element.hasAttribute(FormatAttributeData.STYLE_TYPE)) {
				baseStyle = element.getAttribute(FormatAttributeData.STYLE_TYPE);
			}
		}
		/*
		 * Open the dialog
		 */
		CSSStyleDialog cssDialog = new CSSStyleDialog(PlatformUI.getWorkbench().getDisplay()
				.getActiveShell(), baseStyle);
		if (cssDialog.open() == Window.OK) {
			styleString = cssDialog.getStyle();
		}
		super.handleEvent(event);
	}

	@Override
	protected void setStyle(Attr styleAttribute,
			FormatAttributeData templateData) {
		/*
		 * 'styleString' is not null when user has pressed OK in the dialog.
		 * Thus node's style should be replaced with the new value.
		 * Otherwise nothing should be and will be changed.  
		 */
		if (null != styleString) {
			styleAttribute.setValue(styleString);
		}
	}

	@Override
	protected boolean isStyleSet(Attr attribute,
			FormatAttributeData templateData) {
		return false;
	}

	@Override
	public void setToolbarItemSelection() {
		/*
		 * Do nothing
		 */
	}
	
	@Override
	protected void setToolbarItemSelection(boolean selected) {
		/*
		 * Do nothing
		 */
	}

}
