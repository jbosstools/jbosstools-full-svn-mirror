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
package org.jboss.tools.common.text.ext.hyperlink.xml;

import org.eclipse.jface.text.IDocument;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlinkPartitioner;
import org.jboss.tools.common.text.ext.hyperlink.HyperlinkRegion;
import org.jboss.tools.common.text.ext.hyperlink.IExclusiblePartitionerRecognition;
import org.jboss.tools.common.text.ext.hyperlink.IHyperlinkPartitionRecognizer;
import org.jboss.tools.common.text.ext.hyperlink.IHyperlinkRegion;
import org.jboss.tools.common.text.ext.util.StructuredModelWrapper;
import org.jboss.tools.common.text.ext.util.Utils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * @author Jeremy
 */
public class XMLElementAttributeValueHyperlinkPartitioner extends AbstractHyperlinkPartitioner implements IHyperlinkPartitionRecognizer, IExclusiblePartitionerRecognition {
	public static final String XML_ATTRIBUTE_VALUE_PARTITION = "org.jboss.tools.common.text.ext.xml.XML_ATTRIBUTE_VALUE";

	protected String getPartitionType() {
		return XML_ATTRIBUTE_VALUE_PARTITION;
	}

	/**
	 * @see com.ibm.sse.editor.hyperlink.AbstractHyperlinkPartitioner#parse(org.eclipse.jface.text.IDocument, com.ibm.sse.editor.extensions.hyperlink.IHyperlinkRegion)
	 */
	protected IHyperlinkRegion parse(IDocument document, IHyperlinkRegion superRegion) {
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(document);
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return null;
			
			Node n = Utils.findNodeForOffset(xmlDocument, superRegion.getOffset());
			if (n == null || !(n instanceof Attr)) return null;
			int start = Utils.getValueStart(n);
			int end = Utils.getValueEnd(n);

			String axis = getAxis(document, superRegion);
			String contentType = superRegion.getContentType();
			String type = getPartitionType();
			int length = end - start - (superRegion.getOffset() - start);
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
		StructuredModelWrapper smw = new StructuredModelWrapper();
		try {
			smw.init(document);
			Document xmlDocument = smw.getDocument();
			if (xmlDocument == null) return false;
			
			Node n = Utils.findNodeForOffset(xmlDocument, region.getOffset());
			if (!(n instanceof Attr)) return false;
			int start = Utils.getValueStart(n);
			int end = Utils.getValueEnd(n);
			if (start < 0 || start > region.getOffset() || end < region.getOffset()) 
				return false;

			return true;
		} finally {
			smw.dispose();
		}
	}

	public boolean excludes(String partitionType, IDocument document, IHyperlinkRegion superRegion) {
		return false;
	}

	public String getExclusionPartitionType() {
		return getPartitionType();
	}


}