/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.menu.action;

import java.util.Properties;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.views.palette.PaletteInsertHelper;
import org.jboss.tools.jst.jsp.jspeditor.dnd.JSPPaletteInsertHelper;
import org.jboss.tools.jst.web.tld.URIConstants;
import org.jboss.tools.jst.web.tld.model.helpers.TLDToPaletteHelper;
import org.jboss.tools.vpe.editor.menu.InsertType;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.NodesManagingUtil;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Class is used to handle insert action.
 *
 * @author Igor Zhukov (izhukov@exadel.com)
 * @author yradtsevich
 */
public class InsertAction2 extends Action {

	private final XModelObject item;
	private final StructuredTextEditor sourceEditor;
	private final InsertType insertType;

	/**
	 * Constructor.
	 *
	 * @param title the name of the action
	 * @param region the Point object
	 * @param item XModelObject object
	 * @param pageContext the VpePageContext element
	 * @param sourceEditor the StructuredTextEditor element
	 * @param insertType the type of the action
	 */
	public InsertAction2(String title, XModelObject item,
			StructuredTextEditor sourceEditor, InsertType insertType) {
		super(title);
		this.item = item;
		this.sourceEditor = sourceEditor;
		this.insertType = insertType;
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		final Point userSelection = SelectionUtil
				.getSourceSelectionRange(sourceEditor);
		
		/* we must clear the selection before an element is inserted 
		 * (https://jira.jboss.org/jira/browse/JBIDE-3519)            */
		getSourceEditor().getTextViewer().getTextWidget().setSelection(
				userSelection.x);

		final IUndoManager undoManager = sourceEditor.getTextViewer()
				.getUndoManager();
		try {
			undoManager.beginCompoundChange();
			
			prepareInsertion(userSelection);
			doInsertion();
		} finally {
			undoManager.endCompoundChange();
		}
	}

	/**
	 * Sets the cursor to an appropriate position. 
	 * If REPLACE_WITH action is chosen, it removes 
	 * the selected tag.
	 */
	private void prepareInsertion(final Point userSelection) {
		int start = userSelection.x;
		int length = userSelection.y;

		final Node firstNode = SelectionUtil
				.getNodeBySourcePosition(sourceEditor, userSelection.x);
		final Node endNode = SelectionUtil
				.getNodeBySourcePosition(sourceEditor, 
						userSelection.x + userSelection.y);

		if (firstNode != null) {
			if (firstNode.getNodeType() == Node.TEXT_NODE) {
				start = userSelection.x;
			} else {
				start = NodesManagingUtil.getStartOffsetNode(firstNode);
			}
		}

		if (endNode != null) {
			if (endNode.getNodeType() == Node.TEXT_NODE) {
				length = (userSelection.x - start) + userSelection.y;
			} else {
				length = NodesManagingUtil.getEndOffsetNode(endNode) - start;
			}
		}

		final int insertionStart;
		final int insertionLength;
		switch (insertType) {
		case INSERT_BEFORE:
			insertionStart = start;
			insertionLength = 0;
			break;
		case INSERT_AFTER:
			insertionStart = start + length;
			insertionLength = 0;
			break;
		case INSERT_INTO:
			if (firstNode != null
					&& firstNode.getNodeType() == Node.ELEMENT_NODE) {
				final Element firstElement = (Element) firstNode;
				Node prevNode = firstElement
						.getOwnerDocument().createTextNode(""); //$NON-NLS-1$
				try {
					firstElement.appendChild(prevNode);
				} catch(DOMException e) {
					prevNode = firstNode;
				}

				insertionStart = NodesManagingUtil.getEndOffsetNode(prevNode);
			} else {
				insertionStart = start + length;
			}
			insertionLength = 0;
			break;
		default:
			insertionStart = start;
			insertionLength = length;
			break;
		}

		if (insertType == InsertType.REPLACE_WITH) {
			sourceEditor.getTextViewer().getTextWidget().replaceTextRange(
					insertionStart, insertionLength, ""); //$NON-NLS-1$
		} else {
			// set source selection
			sourceEditor.getTextViewer().getTextWidget().setSelection(
					insertionStart, insertionStart + insertionLength);
//			SelectionUtil.setSourceSelection(pageContext,
//					insertionStart, insertionLength);
		}

	}
	
	/**
	 * Inserts chosen tag at the cursor. 
	 */
	private void doInsertion() {
		String tagName = item.getAttributeValue("name"); //$NON-NLS-1$

		XModelObject parent = item.getParent();
		String uri = (parent == null) 
				? Constants.EMPTY 
				: parent.getAttributeValue(URIConstants.LIBRARY_URI);
		String libraryVersion = (parent == null) 
				? Constants.EMPTY 
				: parent.getAttributeValue(URIConstants.LIBRARY_VERSION);
		String defaultPrefix = (parent == null) 
				? Constants.EMPTY
				: parent.getAttributeValue(URIConstants.DEFAULT_PREFIX);

		/*
		 * Fixes https://jira.jboss.org/jira/browse/JBIDE-1363. Fixes
		 * https://jira.jboss.org/jira/browse/JBIDE-2442. author:
		 * dmaliarevich StructuredSelectionProvider from source view is used
		 * instead of VpeSelectionProvider. It helps automatically update
		 * selection range after taglib insertion.
		 */
		String startText = Constants.EMPTY 
				+ item.getAttributeValue(TLDToPaletteHelper.START_TEXT);
		String endText = Constants.EMPTY
				+ item.getAttributeValue(TLDToPaletteHelper.END_TEXT);
		

		// Gets source editor's selection provider with updated text selection.
		ISelectionProvider selectionProvider
				= sourceEditor.getSelectionProvider();

		Properties p = new Properties();
		p.setProperty(PaletteInsertHelper.PROPOPERTY_TAG_NAME, tagName);
		p.setProperty(PaletteInsertHelper.PROPOPERTY_START_TEXT, startText);
		p.setProperty(PaletteInsertHelper.PROPOPERTY_END_TEXT, endText);
		p.setProperty(PaletteInsertHelper.PROPOPERTY_REFORMAT_BODY,
				item.getAttributeValue(TLDToPaletteHelper.REFORMAT));
		p.setProperty(URIConstants.LIBRARY_URI, uri);
		p.setProperty(URIConstants.LIBRARY_VERSION, libraryVersion);
		String addTaglib = item.getParent().getAttributeValue(
				TLDToPaletteHelper.ADD_TAGLIB);
		p.setProperty(URIConstants.DEFAULT_PREFIX, defaultPrefix);
		p.setProperty(JSPPaletteInsertHelper.PROPOPERTY_ADD_TAGLIB, addTaglib);
		/*
		 * Added by Dzmitry Sakovich Fix for JBIDE-1626
		 */
		// if(((Node)region).getNodeType() == Node.ELEMENT_NODE)
		p.put(PaletteInsertHelper.PROPOPERTY_SELECTION_PROVIDER,
				selectionProvider);
		JSPPaletteInsertHelper.getInstance().insertIntoEditor(sourceEditor.getTextViewer(), p);
	}

	/**
	 * @return the sourceEditor
	 */
	protected StructuredTextEditor getSourceEditor() {
		return sourceEditor;
	}
}
