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
package org.jboss.tools.shale.text.ext.hyperlink;

import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Node;

import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlinkPartitioner;
import org.jboss.tools.common.text.ext.hyperlink.HyperlinkRegion;
import org.jboss.tools.common.text.ext.hyperlink.IHyperlinkRegion;
import org.jboss.tools.common.text.ext.util.Utils;
import org.jboss.tools.common.text.ext.hyperlink.jsp.JSPRootHyperlinkPartitioner;

public class JSPShaleComponentJSFIDHyperlinkPartitioner extends AbstractHyperlinkPartitioner {
	public static final String JSP_ComponentJSFID_PARTITION = "org.jboss.tools.jst.jsp.JSP_ComponentJSFID";
	
	protected String getDefaultPartitionType() {
		return JSP_ComponentJSFID_PARTITION;
	}

	protected IHyperlinkRegion parse(IDocument document, IHyperlinkRegion superRegion) {
		IStructuredModel model = null;
		try {	
			model = getModelManager().getExistingModelForRead(document);
			IDOMDocument xmlDocument = (model instanceof IDOMModel) ? ((IDOMModel) model).getDocument() : null;
			if (xmlDocument == null) return null;
			
//			Node n = 
				Utils.findNodeForOffset(xmlDocument, superRegion.getOffset());
//			if (!recognize(document, superRegion)) return null;
			IHyperlinkRegion r = getRegion(document, superRegion.getOffset());
			if (r == null) return null;
			
			String axis = getAxis(document, superRegion);
			String contentType = superRegion.getContentType();
			String type = getDefaultPartitionType();
			int length = r.getLength() - (superRegion.getOffset() - r.getOffset());
			int offset = superRegion.getOffset();
			
			IHyperlinkRegion region = new HyperlinkRegion(offset, length, axis, contentType, type);
			return region;
		} catch (Exception x) {
			return null;
		} finally {
			if (model != null)	model.releaseFromRead();
		}
	}

	protected String getAxis(IDocument document, IHyperlinkRegion superRegion) {
		if (superRegion.getAxis() == null || superRegion.getAxis().length() == 0) {
			return JSPRootHyperlinkPartitioner.computeAxis(document, superRegion.getOffset()) + "/";
		}
		return superRegion.getAxis();
	}
	
	public static IHyperlinkRegion getRegion(IDocument document, final int offset) {
		IStructuredModel model = null;
		try {	
			model = getModelManager().getExistingModelForRead(document);
			IDOMDocument xmlDocument = (model instanceof IDOMModel) ? ((IDOMModel) model).getDocument() : null;
			if (xmlDocument == null) return null;
			
			Node n = Utils.findNodeForOffset(xmlDocument, offset);

			if (n == null || !(n instanceof IDOMAttr)) return null;
			
			IDOMAttr attr = (IDOMAttr)n;
			
			if (attr.getValueRegionStartOffset() > offset) return null;

			String attrText = document.get(attr.getValueRegionStartOffset(), 
									attr.getEndOffset() - attr.getValueRegionStartOffset());
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

			int propStart = bStart + attr.getValueRegionStartOffset();
			int propLength = bEnd - bStart;
			
			if (propStart > offset || propStart + propLength < offset) return null;
	
			IHyperlinkRegion region = new HyperlinkRegion(propStart, propLength, null, null, null);
			return region;
		} catch (Exception x) {
			return null;
		} finally {
			if (model != null)	model.releaseFromRead();
		}
		
	}

}
