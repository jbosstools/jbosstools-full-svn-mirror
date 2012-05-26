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
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.SelectionUtil;

/**
 * Class is used to handle insert action.
 *
 * @author Igor Zhukov (izhukov@exadel.com)
 * @deprecated use {@link InsertAction2} instead.
 */
@Deprecated
public class InsertAction extends Action {
	private XModelObject item;
	private Point region;
	private VpePageContext pageContext;
	private StructuredTextEditor sourceEditor;
	private boolean replace; 

	/**
	 * Constructor.
	 *
	 * @param title the name of the action
	 * @param region the Point object
	 * @param item XModelObject object
	 * @param pageContext the VpePageContext element
	 * @param sourceEditor the StructuredTextEditor element
	 */
	public InsertAction(String title, Point region, XModelObject item,
			VpePageContext pageContext,	StructuredTextEditor sourceEditor) {
		this(title, region, item, pageContext, sourceEditor, false);
	}
	
	/**
	 * Constructor.
	 *
	 * @param title the name of the action
	 * @param region the Point object
	 * @param item XModelObject object
	 * @param pageContext the VpePageContext element
	 * @param sourceEditor the StructuredTextEditor element
	 */
	public InsertAction(String title, Point region,
			XModelObject item, VpePageContext pageContext,
			StructuredTextEditor sourceEditor, boolean replace) {
		super(title);
		this.item = item;
		this.region = region;
		this.pageContext = pageContext;
		this.sourceEditor = sourceEditor;
		this.replace = replace;
	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		final IUndoManager undoManager = sourceEditor.getTextViewer()
				.getUndoManager();
		try {
			undoManager.beginCompoundChange();
			insert();
		} finally {
			undoManager.endCompoundChange();
		}
	}

	/**
	 * Inserts the specified {@code item} into the 
	 * {@code sourceEditor}. 
	 */
	private void insert() {
		String tagName = item.getAttributeValue("name"); //$NON-NLS-1$

		XModelObject parent = item.getParent();
		String uri = (parent == null) ? Constants.EMPTY : parent.getAttributeValue(URIConstants.LIBRARY_URI);
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
				+ item.getAttributeValue("start text"); //$NON-NLS-1$
		String endText = Constants.EMPTY
				+ item.getAttributeValue("end text"); //$NON-NLS-1$
		
		if (region != null) {
			if (this.replace) {
				getSourceEditor().getTextViewer().getTextWidget().replaceTextRange(
						region.x, region.y, ""); //$NON-NLS-1$
			} else {
				// set source selection
				SelectionUtil.setSourceSelection(pageContext, region.x, region.y);
			}
		}

		// Gets source editor's selection provider with updated text selection.
		ISelectionProvider selProvider = sourceEditor.getSelectionProvider();

		Properties p = new Properties();
		p.setProperty("tag name", tagName); //$NON-NLS-1$
		p.setProperty("start text", startText); //$NON-NLS-1$
		p.setProperty("end text", endText); //$NON-NLS-1$
		p.setProperty("automatically reformat tag body", //$NON-NLS-1$
				Constants.EMPTY + item.getAttributeValue("automatically reformat tag body")); //$NON-NLS-1$
		p.setProperty(URIConstants.LIBRARY_URI, uri);
		p.setProperty(URIConstants.LIBRARY_VERSION, libraryVersion);
		String addTaglib = item.getParent().getAttributeValue(TLDToPaletteHelper.ADD_TAGLIB);
		p.setProperty(URIConstants.DEFAULT_PREFIX, defaultPrefix);
		p.setProperty(JSPPaletteInsertHelper.PROPOPERTY_ADD_TAGLIB, addTaglib);
		/*
		 * Added by Dzmitry Sakovich Fix for JBIDE-1626
		 */
		// if(((Node)region).getNodeType() == Node.ELEMENT_NODE)
		p.put("selectionProvider", selProvider); //$NON-NLS-1$
		JSPPaletteInsertHelper.getInstance().insertIntoEditor(sourceEditor.getTextViewer(), p);
	}

	/**
	 * @return the pageContext
	 */
	protected VpePageContext getPageContext() {
		return pageContext;
	}

	/**
	 * @return the sourceEditor
	 */
	protected StructuredTextEditor getSourceEditor() {
		return sourceEditor;
	}

}
