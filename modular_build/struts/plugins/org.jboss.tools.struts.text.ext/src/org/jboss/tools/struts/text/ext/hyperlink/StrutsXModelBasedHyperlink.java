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
package org.jboss.tools.struts.text.ext.hyperlink;

import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.Utils;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;
import org.jboss.tools.struts.text.ext.StrutsExtensionsPlugin;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * @author Jeremy
 */
public abstract class StrutsXModelBasedHyperlink extends AbstractHyperlink {

	/** 
	 * @see com.ibm.sse.editor.AbstractHyperlink#doHyperlink(org.eclipse.jface.text.IRegion)
	 */
	protected void doHyperlink(IRegion region) {
		IFile documentFile = getFile();
		XModel xModel = getXModel(documentFile);
		if (xModel == null) {
			openFileFailed();
			return;
		}

		WebPromptingProvider provider = WebPromptingProvider.getInstance();

		Properties p = getRequestProperties(region);
		p.put(WebPromptingProvider.FILE, documentFile);

		List<Object> list = provider.getList(xModel, getRequestMethod(), p.getProperty("prefix"), p);
		if (list != null && list.size() >= 1) {
			openFileInEditor((String)list.get(0));
			return;
		}
		String error = p.getProperty(WebPromptingProvider.ERROR); 
		if ( error != null && error.length() > 0) {
			openFileFailed();
		}
	}
	
	protected abstract String getRequestMethod();

	protected abstract Properties getRequestProperties(IRegion region);
	
	protected IRegion fLastRegion = null;
	/** 
	 * @see com.ibm.sse.editor.AbstractHyperlink#doGetHyperlinkRegion(int)
	 */
	protected IRegion doGetHyperlinkRegion(int offset) {
		fLastRegion = getRegion(offset);
		return fLastRegion;
	}
	
	protected IRegion getRegion (int offset) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		smw.init(getDocument());
		try {
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return null;
			
			Node n = Utils.findNodeForOffset(xmlDocument, offset);

			if (n == null || !(n instanceof Attr || n instanceof Text)) return null;
			
			int start = Utils.getValueStart(n);
			int end = Utils.getValueEnd(n);

			if (start > offset || end < offset) return null;

			String text = getDocument().get(start, end - start);
			StringBuffer sb = new StringBuffer(text);

			//find start and end of class property
			int bStart = 0;
			int bEnd = text.length() - 1;

			while (bStart < bEnd && (Character.isWhitespace(sb.charAt(bStart)) 
					|| sb.charAt(bStart) == '\"' || sb.charAt(bStart) == '\"')) { 
				bStart++;
			}
			while (bEnd > bStart && (Character.isWhitespace(sb.charAt(bEnd)) 
					|| sb.charAt(bEnd) == '\"' || sb.charAt(bEnd) == '\"')) { 
				bEnd--;
			}
			bEnd++;

			final int propStart = bStart + start;
			final int propLength = bEnd - bStart;
			
			if (propStart > offset || propStart + propLength < offset) return null;
			
			IRegion region = new IRegion () {
				public int getLength() {
					return propLength;
				}

				public int getOffset() {
					return propStart;
				}
				
				public boolean equals(Object arg) {
					if (!(arg instanceof IRegion)) return false;
					IRegion region = (IRegion)arg;
					
					if (getOffset() != region.getOffset()) return false;
					if (getLength() != region.getLength()) return false;
					return true;
				}
				
				public String toString() {
					return "IRegion [" + getOffset() +", " + getLength()+ "]";
				}
				
			};
			
			return region;
		} catch (BadLocationException x) {
			StrutsExtensionsPlugin.getPluginLog().logError(x);
			return null;
		} finally {
			smw.dispose();
		}
	}
	
	protected String getAttributeValue (IDocument document, Node node, String attrName) {
		if(document == null || node == null || attrName == null) return null;
		try {
			Attr attr = (Attr)node.getAttributes().getNamedItem(attrName);
			return Utils.getTrimmedValue(document, attr);
		} catch (BadLocationException x) {
			StrutsExtensionsPlugin.getPluginLog().logError(x);
			return null;
		}
	}

}
