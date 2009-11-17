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
package org.jboss.tools.vpe.editor.menu;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.editor.VpeEditorPart;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.w3c.dom.Node;

/**
 * An utility class that allows easily and quickly get
 * some environment variables.
 * <P>IMPORTANT: If an environment variable changes
 * between the constructor call and the variable's getter call, the method
 * could return an incorrect result. You must create a new instance to
 * get correct result!
 * </P>  
 * 
 * @author yradtsevich
 */
public class VpeMenuUtil {
	
	public static final String ICON_MENU_ZOOM_IN = "icons/menu/zoom_in.png"; //$NON-NLS-1$
	public static final String ICON_MENU_ZOOM_OUT = "icons/menu/zoom_out.png"; //$NON-NLS-1$
	
	private JSPMultiPageEditor editor;
	private boolean editorInitialized = false;
	private StructuredTextEditor sourceEditor;
	private boolean sourceEditorInitialized = false;
	private VpeDomMapping domMapping;
	private boolean domMappingInitialized = false;
	private VpePageContext pageContext;
	private boolean pageContextInitialized = false;
	private Node selectedNode;
	private boolean selectedNodeInitialized = false;
	private IStructuredSelection selection;
	private boolean selectionInitialized = false;
	private MozillaEditor mozillaEditor;
	private boolean mozillaEditorInitialized = false;

	/**
	 * Returns active {@code JSPMultiPageEditor}
	 */
	public JSPMultiPageEditor getEditor() {
		if (!editorInitialized) {
			editor = (JSPMultiPageEditor) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow()
					.getActivePage().getActiveEditor();
			editorInitialized = true;
		}
		return editor;
	}
	/**
	 * Returns active {@code StructuredTextEditor}
	 */
	public StructuredTextEditor getSourceEditor() {
		if (!sourceEditorInitialized) {
			sourceEditor = getEditor().getSourceEditor();
			sourceEditorInitialized = true;
		}
		return sourceEditor;
	}
	/**
	 * Returns active {@code StructuredTextEditor}
	 */
	public VpeDomMapping getDomMapping() {
		if (!domMappingInitialized) {
			domMapping = getPageContext().getDomMapping();
			domMappingInitialized = true;
		}
		return domMapping;
	}
	/**
	 * Returns {@code VpePageContext} of 
	 * the active editor
	 */
	public VpePageContext getPageContext() {
		if (!pageContextInitialized) {
			pageContext = ((VpeEditorPart) getEditor().getVisualEditor())
					.getController().getPageContext();
			pageContextInitialized = true;
		}
		return pageContext;
	}
	/**
	 * Returns active source selection
	 */
	public IStructuredSelection getSelection() {
		if (!selectionInitialized) {
			selection = (IStructuredSelection) getSourceEditor()
					.getSelectionProvider().getSelection();
			selectionInitialized = true;
		}
		return selection;
	}
	/**
	 * Returns selected node.
	 */
	public Node getSelectedNode() {
		if (!selectedNodeInitialized) {
			selection = getSelection();
			if (selection != null
					&& selection.getFirstElement() instanceof Node) {
				selectedNode = (Node) selection.getFirstElement();
			}
			selectedNodeInitialized = true;
		}
		return selectedNode;
	}
	/**
	 * Returns active {@code MozillaEditor}
	 */
	public MozillaEditor getMozillaEditor() {
		if (!mozillaEditorInitialized) {
			mozillaEditor = ((VpeEditorPart) getEditor().getVisualEditor())
					.getVisualEditor();
			mozillaEditorInitialized = true;
		}
		return mozillaEditor;
	}
}
