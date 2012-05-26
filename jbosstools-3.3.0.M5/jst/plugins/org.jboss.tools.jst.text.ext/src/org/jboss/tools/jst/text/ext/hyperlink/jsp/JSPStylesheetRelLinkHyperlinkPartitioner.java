/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.text.ext.hyperlink.jsp;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlinkPartitioner;
import org.jboss.tools.common.text.ext.hyperlink.HyperlinkRegion;
import org.jboss.tools.common.text.ext.hyperlink.IExclusiblePartitionerRecognition;
import org.jboss.tools.common.text.ext.hyperlink.IHyperlinkPartitionRecognizer;
import org.jboss.tools.common.text.ext.hyperlink.IHyperlinkRegion;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.Utils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * @author Jeremy
 */
public class JSPStylesheetRelLinkHyperlinkPartitioner extends AbstractHyperlinkPartitioner implements IHyperlinkPartitionRecognizer, IExclusiblePartitionerRecognition {
	public static final String JSP_STYLESHEET_REL_LINK_PARTITION = "org.jboss.tools.common.text.ext.jsp.JSP_STYLESHEET_REL_LINK"; //$NON-NLS-1$
	
	private static final String LINK_TAGNAME = "link"; //$NON-NLS-1$
	private static final String REL_ATTRNAME = "rel"; //$NON-NLS-1$
	private static final String VALID_REL_ATTRVALUE = "stylesheet"; //$NON-NLS-1$
	private static final String HREF_ATTRNAME = "href"; //$NON-NLS-1$

	private static final String IMPORT_DIRECTIVENAME = "@import"; //$NON-NLS-1$
	private static final String URL_METHODSTART = "url("; //$NON-NLS-1$
	private static final String URL_METHODEND = ")"; //$NON-NLS-1$
	private static final String URL_METHODEND_2 = ";"; //$NON-NLS-1$
	
	/**
	 * @see com.ibm.sse.editor.hyperlink.AbstractHyperlinkPartitioner#parse(org.eclipse.jface.text.IDocument, com.ibm.sse.editor.extensions.hyperlink.IHyperlinkRegion)
	 */
	protected IHyperlinkRegion parse(IDocument document, IHyperlinkRegion superRegion) {
		if(superRegion == null) return null;
		StructuredModelWrapper smw = new StructuredModelWrapper();
		smw.init(document);
		try {
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return null;
			
			Utils.findNodeForOffset(xmlDocument, superRegion.getOffset());
//			if (!recognize(document, superRegion)) return null;
			IRegion r = getRegion(document, superRegion.getOffset());
			if (r == null) return null;
			
			String axis = getAxis(document, superRegion);
			String contentType = superRegion.getContentType();
			String type = JSP_STYLESHEET_REL_LINK_PARTITION;
			int length = r.getLength() - (superRegion.getOffset() - r.getOffset());
			int offset = superRegion.getOffset();

			IHyperlinkRegion region = new HyperlinkRegion(offset, length, axis, contentType, type);
			return region;
		} finally {
			smw.dispose();
		}
	}

	/**
	 * @see com.ibm.sse.editor.extensions.hyperlink.IHyperlinkPartitionRecognizer#recognize(org.eclipse.jface.text.IDocument, com.ibm.sse.editor.extensions.hyperlink.IHyperlinkRegion)
	 */
	public boolean recognize(IDocument document, IHyperlinkRegion region) {
		if(region == null) return false;
		StructuredModelWrapper smw = new StructuredModelWrapper();
		smw.init(document);
		try {
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return false;
			
			Node n = Utils.findNodeForOffset(xmlDocument, region.getOffset());

			Attr attr = null;
			Node tag = null;
			Text styleText = null;
			
			if (n instanceof Attr) {
				attr = (Attr)n;
				tag = ((Attr)n).getOwnerElement();
			} else if (n instanceof Element) {
				tag = n;
			} else if (n instanceof Text) {
				styleText = (Text)n;
			} else 
				return false;
			

			if (attr != null || tag != null) {
				// do recognize link tag
				if (!LINK_TAGNAME.equalsIgnoreCase(tag.getNodeName())) 
					return false;
	
				String linkRel = Utils.getAttributeValue(document, tag, REL_ATTRNAME);
				if (!VALID_REL_ATTRVALUE.equalsIgnoreCase(linkRel))
					return false;
	
				return true;
			}
			
			if (styleText == null) return false;

			// do recognize style text
			int start = Utils.getValueStart(styleText);
			int current = region.getOffset();
			
			String text = styleText.getData();
			if (text == null || text.length() == 0) return false;

			text = text.toLowerCase();
			
			int importStart = text.lastIndexOf(IMPORT_DIRECTIVENAME.toLowerCase(), current - start);
			if (importStart == -1)
				return false;
			
			int urlMethodStart = text.indexOf(URL_METHODSTART.toLowerCase(), importStart);
			if (urlMethodStart == -1)
				return false;
			
			int urlMethodEnd = text.indexOf(URL_METHODEND.toLowerCase(), urlMethodStart);
			if (urlMethodEnd == -1)
				return false;

			int urlMethodEnd2 = text.indexOf(URL_METHODEND_2.toLowerCase(), urlMethodEnd);
			if (urlMethodEnd2 == -1)
				return false;

			if (current < start + urlMethodStart + URL_METHODSTART.length() ||
					current >= start + urlMethodEnd)
				return false;
			
			return true;
		} finally {
			smw.dispose();
		}
	}

	protected String getAxis(IDocument document, IHyperlinkRegion superRegion) {
		if (superRegion.getAxis() == null || superRegion.getAxis().length() == 0) {
			return JSPRootHyperlinkPartitioner.computeAxis(document, superRegion.getOffset()) + "/"; //$NON-NLS-1$
		}
		return superRegion.getAxis();
	}

	protected IRegion getRegion (IDocument document, int offset) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		smw.init(document);
		smw.init(document);
		try {
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return null;
			
			Node n = Utils.findNodeForOffset(xmlDocument, offset);
			
			if (n == null || !(n instanceof Text || n instanceof Attr)) return null;
			
			String text = null;
			int bStart = 0;
			int bEnd = 0;
			
			if (n instanceof Text) {
				int start = Utils.getValueStart(n);
				int end = Utils.getValueEnd(n);

				if (start < 0 || start > offset) return null;
	
				text = document.get(start, end - start);
				bStart = offset - start;
				bEnd = offset - start;
			} else if (n instanceof Attr) {
				Attr attr = (Attr)n;
				if (!HREF_ATTRNAME.equalsIgnoreCase(attr.getName())) return null;
				int start = Utils.getValueStart(n);
				int end = Utils.getValueEnd(n);
				if(start < 0) return null;
				
				text = document.get(start, end - start);
				bStart = offset - start;
				bEnd = offset - start;
			}
			StringBuffer sb = new StringBuffer(text);
			//find start of bean property
			while (bStart >= 0) { 
				if (!Character.isJavaIdentifierPart(sb.charAt(bStart)) &&
						sb.charAt(bStart) != '\\' && sb.charAt(bStart) != '/' &&
						sb.charAt(bStart) != ':' && sb.charAt(bStart) != '-' &&
						sb.charAt(bStart) != '.' && sb.charAt(bStart) != '_' &&
						sb.charAt(bStart) != '%' && sb.charAt(bStart) != '?' &&
						sb.charAt(bStart) != '&' && sb.charAt(bStart) != '=') {
					bStart++;
					break;
				}
			
				if (bStart == 0) break;
				bStart--;
			}
			// find end of bean property
			while (bEnd >= 0 && bEnd < sb.length()) { 
				if (!Character.isJavaIdentifierPart(sb.charAt(bEnd)) &&
						sb.charAt(bEnd) != '\\' && sb.charAt(bEnd) != '/' &&
						sb.charAt(bEnd) != ':' && sb.charAt(bEnd) != '-' &&
						sb.charAt(bEnd) != '.' && sb.charAt(bEnd) != '_' &&
						sb.charAt(bEnd) != '%' && sb.charAt(bEnd) != '?' &&
						sb.charAt(bEnd) != '&' && sb.charAt(bEnd) != '=') {
					break;
				}
				bEnd++;
			}
			
			final int propStart = bStart + Utils.getValueStart(n);
			final int propLength = bEnd - bStart;
			
			if (propStart > offset || propStart + propLength < offset) return null;
			return new Region(propStart,propLength);
		} catch (BadLocationException x) {
			//ignore
			return null;
		} finally {
			smw.dispose();
		}
	}

	public String getExclusionPartitionType() {
		return null;
	}

	public boolean excludes(String partitionType, IDocument document,
			IHyperlinkRegion superRegion) {
		return (JSP_STYLESHEET_REL_LINK_PARTITION.equals(partitionType) && recognize(document, superRegion));
	}

}
