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
package org.jboss.tools.vpe.ui.test;

import java.lang.reflect.Method;
import static junit.framework.Assert.assertEquals;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class OpenOnUtil {

	/**
	 * method does open on action in editor
	 * 
	 * @param textEditor
	 * @param lineNumber
	 * @param lineOffset
	 * @throws Throwable
	 */
	public static final void performOpenOnAction(
			StructuredTextEditor textEditor, int offset) throws Throwable {

		// hack to get hyperlinks detectors, no other was have been founded
		Method method = AbstractTextEditor.class
				.getDeclaredMethod("getSourceViewerConfiguration"); //$NON-NLS-1$
		method.setAccessible(true);
		SourceViewerConfiguration sourceViewerConfiguration = (SourceViewerConfiguration) method
				.invoke(textEditor);
		IHyperlinkDetector[] hyperlinkDetectors = sourceViewerConfiguration
				.getHyperlinkDetectors(textEditor.getTextViewer());

		for (IHyperlinkDetector iHyperlinkDetector : hyperlinkDetectors) {
			IHyperlink[] hyperLinks = iHyperlinkDetector.detectHyperlinks(
					textEditor.getTextViewer(), new Region(offset, 0), false);
			if (hyperLinks != null && hyperLinks.length > 0
					&& hyperLinks[0] instanceof AbstractHyperlink) {
				AbstractHyperlink abstractHyperlink = (AbstractHyperlink) hyperLinks[0];
				abstractHyperlink.open();
				break;
			}
		}

	}
	
	/**
	 * Function for checking openOn functionality
	 * 
	 * @param editorInput
	 * @param editorId
	 * @param lineNumber
	 * @param lineOffset
	 * @param openedOnFileName
	 * @throws Throwable
	 * 
	 * @author mareshkau
	 */
	
	public static final void checkOpenOnInEditor(IEditorInput editorInput,String editorId,int lineNumber, int lineOffset, String openedOnFileName) throws Throwable {
		IEditorPart editorPart = PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.openEditor(editorInput, editorId);
		StructuredTextEditor textEditor = null;
		if(editorPart instanceof MultiPageEditorPart){
			IEditorPart[] editorParts = ((MultiPageEditorPart)editorPart).findEditors(editorInput);
			((MultiPageEditorPart)editorPart).setActiveEditor(editorParts[0]);
			textEditor = (StructuredTextEditor) editorParts[0];
		} else if(editorPart instanceof JSPMultiPageEditor) {
			textEditor = ((JSPMultiPageEditor)editorPart).getSourceEditor();
		} else if(editorPart instanceof EditorPartWrapper) {
			IEditorPart[] editorParts = ((MultiPageEditorPart)((EditorPartWrapper)editorPart).getEditor()).findEditors(editorInput);
			((MultiPageEditorPart)((EditorPartWrapper)editorPart).getEditor()).setActiveEditor(editorParts[1]);
			textEditor = (StructuredTextEditor) editorParts[1];
		}
		int openOnPosition = TestUtil.getLinePositionOffcet(textEditor
				.getTextViewer(),lineNumber, lineOffset);
		// hack to get hyperlinks detectors, no other was have been founded
		Method method = AbstractTextEditor.class
				.getDeclaredMethod("getSourceViewerConfiguration"); //$NON-NLS-1$
		method.setAccessible(true);
		SourceViewerConfiguration sourceViewerConfiguration = (SourceViewerConfiguration) method
				.invoke(textEditor);
		IHyperlinkDetector[] hyperlinkDetectors = sourceViewerConfiguration
				.getHyperlinkDetectors(textEditor.getTextViewer());
		for (IHyperlinkDetector iHyperlinkDetector : hyperlinkDetectors) {
			IHyperlink[] hyperLinks = iHyperlinkDetector.detectHyperlinks(
					textEditor.getTextViewer(), new Region(openOnPosition, 0),
					false);
			if (hyperLinks != null && hyperLinks.length > 0
					&& hyperLinks[0] instanceof AbstractHyperlink) {
				AbstractHyperlink abstractHyperlink = (AbstractHyperlink) hyperLinks[0];
				abstractHyperlink.open();
				break;
			}
		}
		IEditorPart activeEditor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		assertEquals(
				"Active page should be ", openedOnFileName, activeEditor.getEditorInput().getName()); //$NON-NLS-1$

	}

}
