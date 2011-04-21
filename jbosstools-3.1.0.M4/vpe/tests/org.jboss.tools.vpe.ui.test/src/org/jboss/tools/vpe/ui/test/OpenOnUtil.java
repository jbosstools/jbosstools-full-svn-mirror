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

import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink;

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

}
