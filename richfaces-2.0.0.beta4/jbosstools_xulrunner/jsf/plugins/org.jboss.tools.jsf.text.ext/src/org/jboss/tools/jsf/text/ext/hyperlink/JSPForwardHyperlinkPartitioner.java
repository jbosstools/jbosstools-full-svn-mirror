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
package org.jboss.tools.jsf.text.ext.hyperlink;

import org.eclipse.jface.text.IDocument;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlinkPartitioner;
import org.jboss.tools.common.text.ext.hyperlink.HyperlinkRegion;
import org.jboss.tools.common.text.ext.hyperlink.IHyperlinkRegion;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.TaglibManagerWrapper;
import org.jboss.tools.common.text.ext.util.Utils;
import org.jboss.tools.common.text.ext.hyperlink.jsp.JSPRootHyperlinkPartitioner;
import org.jboss.tools.jsf.text.ext.JSFExtensionsPlugin;

/**
 * @author Jeremy
 */
public class JSPForwardHyperlinkPartitioner extends AbstractHyperlinkPartitioner /*implements IHyperlinkPartitionRecognizer */{
	public static final String JSP_FORWARD_PARTITION = "org.jboss.tools.common.text.ext.jsp.JSP_FORWARD";

	/**
	 * @see com.ibm.sse.editor.hyperlink.AbstractHyperlinkPartitioner#parse(org.eclipse.jface.text.IDocument, com.ibm.sse.editor.extensions.hyperlink.IHyperlinkRegion)
	 */
	protected IHyperlinkRegion parse(IDocument document, IHyperlinkRegion superRegion) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(document);
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return null;
			
			Utils.findNodeForOffset(xmlDocument, superRegion.getOffset());
			IHyperlinkRegion r = getRegion(document, superRegion.getOffset());
			if (r == null) return null;
			
			String axis = getAxis(document, superRegion);
			String contentType = superRegion.getContentType();
			String type = JSP_FORWARD_PARTITION;
			int length = r.getLength() - (superRegion.getOffset() - r.getOffset());
			int offset = superRegion.getOffset();
			
			IHyperlinkRegion region = new HyperlinkRegion(offset, length, axis, contentType, type);
			return region;
		} catch (Exception x) {
			JSFExtensionsPlugin.log("", x);
			return null;
		} finally {
			smw.dispose();
		}
	}

	protected String getAxis(IDocument document, IHyperlinkRegion superRegion) {
		if (superRegion.getAxis() == null || superRegion.getAxis().length() == 0) {
			return JSPRootHyperlinkPartitioner.computeAxis(document, superRegion.getOffset()) + "/";
		}
		return superRegion.getAxis();
	}
	
	public static IHyperlinkRegion getRegion(IDocument document, final int offset) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(document);
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return null;
			
			Node n = Utils.findNodeForOffset(xmlDocument, offset);

			if (n == null || !(n instanceof Attr)) return null;
			
			int start = Utils.getValueStart(n);
			int end = Utils.getValueEnd(n);
			
			if (start < 0 || start > offset) return null;

			String attrText = document.get(start, end - start);
			StringBuffer sb = new StringBuffer(attrText);

			//find start and end of path property
			int bStart = 0;
			int bEnd = attrText.length() - 1;

			while (bStart < bEnd && 
					(sb.charAt(bStart) == '\'' || sb.charAt(bStart) == '\"' ||
							Character.isWhitespace(sb.charAt(bStart)))) { 
				bStart++;
			}
			while (bEnd > bStart && 
					(sb.charAt(bEnd) == '\'' || sb.charAt(bEnd) == '\"' ||
							Character.isWhitespace(sb.charAt(bEnd)))) { 
				bEnd--;
			}
			bEnd++;

			int propStart = bStart + start;
			int propLength = bEnd - bStart;
			
			if (propStart > offset || propStart + propLength < offset) return null;
	
			IHyperlinkRegion region = new HyperlinkRegion(propStart, propLength, null, null, null);
			return region;
		} catch (Exception x) {
			JSFExtensionsPlugin.log("", x);
			return null;
		} finally {
			smw.dispose();
		}
		
	}

	/**
	 * @see com.ibm.sse.editor.extensions.hyperlink.IHyperlinkPartitionRecognizer#recognize(org.eclipse.jface.text.IDocument, com.ibm.sse.editor.extensions.hyperlink.IHyperlinkRegion)
	 */
	public boolean recognize(IDocument document, IHyperlinkRegion region) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(document);
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return false;
			
			Node n = Utils.findNodeForOffset(xmlDocument, region.getOffset());
			if (!(n instanceof Attr)) return false;

			IHyperlinkRegion r = getRegion(document, region.getOffset());
			if (r == null) return false;

			Attr attr = (Attr)n;
			String attrName = attr.getNodeName();
			if (!"var".equals(attrName) && !"basename".equals(attrName)) return false;
			
			Element lbTag = attr.getOwnerElement();
			String name = lbTag.getTagName();
			int column = name.indexOf(":");
			if (column == -1) return false;
			String prefix = name.substring(0, column);
			if (prefix == null || prefix.trim().length() == 0) return false;
			
			TaglibManagerWrapper tmw = new TaglibManagerWrapper();
			tmw.init(document, region.getOffset());
			if(!tmw.exists()) return false;

			if (!prefix.equals(tmw.getCorePrefix())) return false;

			Attr lbTagVar = lbTag.getAttributeNode("var");
			Attr lbTagBasename = lbTag.getAttributeNode("basename");

			if (lbTagVar == null || lbTagVar.getNodeValue() == null ||
					lbTagVar.getNodeValue().trim().length() == 0) return false;
			if (lbTagBasename == null || lbTagBasename.getNodeValue() == null ||
					lbTagBasename.getNodeValue().trim().length() == 0) return false;
			
			return true;
		} catch (Exception x) {
			JSFExtensionsPlugin.log("", x);
			return false;
		} finally {
			smw.dispose();
		}
	}

}
