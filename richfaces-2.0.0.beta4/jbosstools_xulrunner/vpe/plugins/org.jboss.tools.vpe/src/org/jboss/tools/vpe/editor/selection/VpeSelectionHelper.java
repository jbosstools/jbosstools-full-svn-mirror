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
package org.jboss.tools.vpe.editor.selection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

public class VpeSelectionHelper {

	public static StyledText getSourceTextWidget(StructuredTextEditor sourceEditor) {
		if (sourceEditor != null && sourceEditor.getTextViewer() != null && sourceEditor.getTextViewer().getTextWidget() != null && !sourceEditor.getTextViewer().getTextWidget().isDisposed()) {
			return (StyledText)sourceEditor.getTextViewer().getTextWidget();
		}
		return null;
	}
		 
	public static int getCaretOffset(StructuredTextEditor sourceEditor) {
		StyledText t = getSourceTextWidget(sourceEditor);
		return t == null ? 0 : t.getCaretOffset();
	}
		 
	public static List getTextWidgetSelectedNodes(IStructuredModel model, ISelectionProvider provider) {
		ISelection s = provider.getSelection();
		if(s == null || s.isEmpty() || !(s instanceof ITextSelection)) return new ArrayList();
		ITextSelection ts = (ITextSelection)s;
		int offset = ts.getOffset();
		if (model == null) return new ArrayList(0);
		IndexedRegion firstSelectedNode = model.getIndexedRegion(offset);

		// Never send a "null" in the selection
		List selectedNodes = null;
		if (firstSelectedNode != null) {
			selectedNodes = new ArrayList(1);
			selectedNodes.add(firstSelectedNode);
		}
		else {
			selectedNodes = new ArrayList(0);
		}
		return selectedNodes;
	}


}
