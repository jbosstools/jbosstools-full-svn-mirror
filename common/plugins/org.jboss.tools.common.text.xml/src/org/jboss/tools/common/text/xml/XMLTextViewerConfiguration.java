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
package org.jboss.tools.common.text.xml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML;
import org.jboss.tools.common.text.xml.contentassist.ContentAssistProcessorBuilder;
import org.jboss.tools.common.text.xml.contentassist.ContentAssistProcessorDefinition;

/**
 * @author Igels
 */
public class XMLTextViewerConfiguration extends StructuredTextViewerConfigurationXML {
	
	SourceViewerConfiguration initial = null;

	public XMLTextViewerConfiguration() {
		super();
	}
	
	public void setInitialConfiguration(SourceViewerConfiguration initial) {
		this.initial = initial;
	}

	protected IContentAssistProcessor[] getContentAssistProcessors(ISourceViewer sourceViewer, String partitionType) {

		// if we have our own processors we need 
		// to define them in plugin.xml file of their
		// plugins using extention point 
		// "org.jboss.tools.common.text.xml.contentAssistProcessor"
		
		ContentAssistProcessorDefinition[] defs = ContentAssistProcessorBuilder.getInstance().getContentAssistProcessorDefinitions(partitionType);

		if(defs==null) return null;

		List processors = new ArrayList();
		for(int i=0; i<defs.length; i++) {
		    IContentAssistProcessor processor = defs[i].createContentAssistProcessor();
		    if(!processors.contains(processor)) {
			    processors.add(processor);			        
		    }
		}

		IContentAssistProcessor[] in = getInitialProcessors(sourceViewer, partitionType);
		if(in != null && in.length > 0) {

			//we do not need super processors - make initial processors responcible for that 
			for(int i=0; i<in.length; i++) {
			    if(!processors.contains(in[i])) {
				    processors.add(in[i]);			        
			    }
			}
		} else {
			IContentAssistProcessor[] ps = super.getContentAssistProcessors(sourceViewer, partitionType);
			for(int i=0; ps != null && i<ps.length; i++) {
			    if(!processors.contains(ps[i])) {
				    processors.add(ps[i]);			        
			    }
			}
		}
		return (IContentAssistProcessor[])processors.toArray(new IContentAssistProcessor[0]);
	}

	/*
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getHyperlinkDetectors(org.eclipse.jface.text.source.ISourceViewer)
	 * @since 3.1
	 */
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
		if (fPreferenceStore == null)
			return null;
		if (sourceViewer == null || !fPreferenceStore.getBoolean(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_HYPERLINKS_ENABLED))
			return null;

		List allDetectors = new ArrayList(0);

		IHyperlinkDetector extHyperlinkDetector = getTextEditorsExtensionsHyperlinkDetector(); 

		if (extHyperlinkDetector != null) allDetectors.add(extHyperlinkDetector);
		
/*		
		IHyperlinkDetector[] superDetectors = super.getHyperlinkDetectors(sourceViewer);
		for (int m = 0; m < superDetectors.length; m++) {
			IHyperlinkDetector detector = superDetectors[m];
			if (!allDetectors.contains(detector)) {
				allDetectors.add(detector);
			}
		}
*/
		IHyperlinkDetector[] ts = (IHyperlinkDetector[]) allDetectors.toArray(new IHyperlinkDetector[0]);
		IHyperlinkDetector[] in = (initial != null) ? initial.getHyperlinkDetectors(sourceViewer) : null;
		if(in == null || in.length == 0) return ts;
		if(ts == null || ts.length == 0) return in;
		ArrayList<IHyperlinkDetector> total = new ArrayList<IHyperlinkDetector>();
		for (int i = 0; i < ts.length; i++) total.add(ts[i]);
		for (int i = 0; i < in.length; i++) if(!total.contains(in[i])) total.add(in[i]);
		return total.toArray(new IHyperlinkDetector[0]);
	}

	private IHyperlinkDetector getTextEditorsExtensionsHyperlinkDetector() {
		Plugin plugin = Platform.getPlugin("org.jboss.tools.common.text.ext");
		return (plugin != null && plugin instanceof IAdaptable ? (IHyperlinkDetector)((IAdaptable)plugin).getAdapter(IHyperlinkDetector.class):null);
	}

	IContentAssistProcessor[] getInitialProcessors(ISourceViewer sourceViewer, String partitionType) {
		if(initial == null) return null;
		try {
			Method m = initial.getClass().getDeclaredMethod("getContentAssistProcessors", new Class[]{ISourceViewer.class, String.class});
			m.setAccessible(true);
			return (IContentAssistProcessor[])m.invoke(initial, new Object[]{sourceViewer, partitionType});
		} catch (NoSuchMethodException e) {
			XmlEditorPlugin.getPluginLog().logError(e);
		} catch (IllegalArgumentException e) {
			XmlEditorPlugin.getPluginLog().logError(e);
		} catch (IllegalAccessException e) {
			XmlEditorPlugin.getPluginLog().logError(e);
		} catch (InvocationTargetException e) {
			XmlEditorPlugin.getPluginLog().logError(e);
		}
		
		return null;
	}

}
