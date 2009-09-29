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
package org.jboss.tools.jst.jsp;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jst.jsp.core.text.IJSPPartitions;
import org.eclipse.jst.jsp.ui.StructuredTextViewerConfigurationJSP;
import org.eclipse.jst.jsp.ui.internal.style.jspel.LineStyleProviderForJSPEL;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;

/**
 * @author Igels
 */
public class JSPTextViewerConfiguration extends StructuredTextViewerConfigurationJSP implements ITextViewerConfiguration {
	
	private TextViewerConfigurationDelegate configurationDelegate;

	public JSPTextViewerConfiguration() {
		super();
		configurationDelegate = new TextViewerConfigurationDelegate(this);
	}


	/*
	 * JBIDE-4390:  
	 * The line style provider for partition type == "org.eclipse.jst.jsp.SCRIPT.JSP_EL2", 
	 * which is ommitted (forgotten ?) in superclass
	 */
	private LineStyleProvider fLineStyleProviderForJSPEL2;

	/*
	 * JBIDE-4390:  
	 * The method is overriden to provide a line style provider for partition type == "org.eclipse.jst.jsp.SCRIPT.JSP_EL2", 
	 * which is ommitted (forgotten ?) in superclass
	 */
	private LineStyleProvider getLineStyleProviderForJSPEL2() {
		if (fLineStyleProviderForJSPEL2 == null) {
			fLineStyleProviderForJSPEL2 = new LineStyleProviderForJSPEL();
		}
		return fLineStyleProviderForJSPEL2;
	}

	/*
	 * JBIDE-4390:  
	 * The method is overriden to provide a line style provider for partition type == "org.eclipse.jst.jsp.SCRIPT.JSP_EL2", 
	 * which is ommitted (forgotten ?) in superclass
	 * 
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.jst.jsp.ui.StructuredTextViewerConfigurationJSP#getLineStyleProviders(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
	public LineStyleProvider[] getLineStyleProviders(ISourceViewer sourceViewer, String partitionType) {
		LineStyleProvider[] providers = null;

		if (partitionType == IJSPPartitions.JSP_DEFAULT_EL2) {
			providers = new LineStyleProvider[]{getLineStyleProviderForJSPEL2()};
		} 
		else {
			providers = super.getLineStyleProviders(sourceViewer, partitionType);
		}

		return providers;
	}

	protected IContentAssistProcessor[] getContentAssistProcessors(ISourceViewer sourceViewer, String partitionType) {
		return configurationDelegate.getContentAssistProcessors(sourceViewer, partitionType);
	}
	
	/*
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getHyperlinkDetectors(org.eclipse.jface.text.source.ISourceViewer)
	 * @since 3.1
	 */
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
		return configurationDelegate.getHyperlinkDetectors(
				sourceViewer,
				fPreferenceStore.getBoolean(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_HYPERLINKS_ENABLED));		
	}

	public IContentAssistProcessor[] getContentAssistProcessorsForPartitionType(
			ISourceViewer sourceViewer, String partitionType) {
		// TODO Auto-generated method stub
		return super.getContentAssistProcessors(sourceViewer, partitionType);
	}
}