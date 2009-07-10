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
package org.jboss.tools.vpe.editor.toolbar.format;

import java.util.HashMap;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import org.jboss.tools.vpe.editor.template.textformating.FormatAttributeData;
import org.jboss.tools.vpe.editor.util.HTML;

/**
 * @author Igels
 */
public class BlockFormatController extends ComboFormatController {

    public static String TYPE = "BlockFormat"; //$NON-NLS-1$
    public static HashMap TAGS = new HashMap();
    static {
	TAGS.put("address", "Address"); //$NON-NLS-1$ //$NON-NLS-2$
	TAGS.put("h1", "Heading 1"); //$NON-NLS-1$ //$NON-NLS-2$
	TAGS.put("h2", "Heading 2"); //$NON-NLS-1$ //$NON-NLS-2$
	TAGS.put("h3", "Heading 3"); //$NON-NLS-1$ //$NON-NLS-2$
	TAGS.put("h4", "Heading 4"); //$NON-NLS-1$ //$NON-NLS-2$
	TAGS.put("h5", "Heading 5"); //$NON-NLS-1$ //$NON-NLS-2$
	TAGS.put("h6", "Heading 6"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    public static HashMap TEXTS = new HashMap();
    static {
	TEXTS.put("Address", "address"); //$NON-NLS-1$ //$NON-NLS-2$
	TEXTS.put("Heading 1", "h1"); //$NON-NLS-1$ //$NON-NLS-2$
	TEXTS.put("Heading 2", "h2"); //$NON-NLS-1$ //$NON-NLS-2$
	TEXTS.put("Heading 3", "h3"); //$NON-NLS-1$ //$NON-NLS-2$
	TEXTS.put("Heading 4", "h4"); //$NON-NLS-1$ //$NON-NLS-2$
	TEXTS.put("Heading 5", "h5"); //$NON-NLS-1$ //$NON-NLS-2$
	TEXTS.put("Heading 6", "h6"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private String tagName;

    /**
     * @param manager
     * @param comboBlockFormat
     */
    public BlockFormatController(FormatControllerManager manager,
	    Combo comboBlockFormat) {
	super(manager, comboBlockFormat);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#getType()
     */
    public String getType() {
	return TYPE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event) {
	selectionIndex = comboBlockFormat.getSelectionIndex();
	selectionText = comboBlockFormat.getText();
	tagName = (String) TEXTS.get(selectionText);
	if (tagName == null) {
	    tagName = "normal"; //$NON-NLS-1$
	}
	super.handleEvent(event);
    }

    protected void setStyle(Attr styleAttribute,
	    FormatAttributeData templateData) {
    }

    /**
     * @return Returns the tagName.
     */
    public String getTagName() {
	return tagName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.tools.vpe.editor.toolbar.format.IFormatController#setToolbarItemEnabled(boolean
     *      enabled)
     */
    public void setToolbarItemEnabled(boolean enabled) {
	comboBlockFormat.setEnabled(enabled);
	if (enabled) {

	    Node selectedNode = manager.getCurrentSelectedNode();
	    String nodeName = getNodeName(selectedNode).toLowerCase(); //
	    String text = (String) TAGS.get(nodeName);
	    if (text == null) {
		this.getComboBlockFormat().select(0);
		return;
	    }
	    if (text != null && text.equals(getComboBlockFormat().getText())) {
		return;
	    }
	    String[] items = this.getComboBlockFormat().getItems();
	    for (int i = 0; i < items.length; i++) {
		if (items[i].equalsIgnoreCase(text)) {
		    this.getComboBlockFormat().select(i);
		    return;
		}
	    }
	}
	getComboBlockFormat().deselectAll();
    }

    /**
     * 
     * @return node name (skip tags <u>, <b> and <i>)
     */
    private String getNodeName(Node node) {

	String nodeName = ""; //$NON-NLS-1$
	do {
	    nodeName = node.getNodeName();
	    node = node.getParentNode();
	} while (nodeName.equalsIgnoreCase(HTML.TAG_U)
		|| nodeName.equalsIgnoreCase(HTML.TAG_B)
		|| nodeName.equalsIgnoreCase(HTML.TAG_I));
	return nodeName;
    }
}