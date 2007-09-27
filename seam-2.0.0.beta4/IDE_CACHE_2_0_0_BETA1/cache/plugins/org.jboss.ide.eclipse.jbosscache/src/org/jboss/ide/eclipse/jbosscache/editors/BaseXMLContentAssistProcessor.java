/*******************************************************************************
 * Copyright (c) 2001, 2004, 2006 IBM Corporation, JBoss Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jens Lukowski/Innoopract - initial renaming/restructuring
 *     Max Rydahl Andersen, JBoss Inc. - extracted attribueValue proposal mechanism.
 *******************************************************************************/
package org.jboss.ide.eclipse.jbosscache.editors;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLContentAssistProcessor;

public abstract class BaseXMLContentAssistProcessor extends XMLContentAssistProcessor {
	
	protected void addTagNameProposals(ContentAssistRequest contentAssistRequest, int arg1){
		// TODO Auto-generated method stub
		
		//super.addTagNameProposals(contentAssistRequest, arg1);
		
		IDOMNode node = (IDOMNode) contentAssistRequest.getNode();
		IDOMNode parentNode = (IDOMNode) contentAssistRequest.getParent();
		
		String parentNodeName = parentNode.getNodeName();				
		
		// Find the attribute region and name for which this position should have a value proposed
		IStructuredDocumentRegion open = node.getFirstStructuredDocumentRegion();
		ITextRegionList openRegions = open.getRegions();
		int i = openRegions.indexOf(contentAssistRequest.getRegion());
		
		//No text
		if(i == -1){
			//Show All Elements with parent
			List attributeValueProposals = getTagValueProposals(parentNodeName,"", contentAssistRequest.getReplacementBeginPosition(), contentAssistRequest);
			if(attributeValueProposals!=null) {
				for (Iterator iter = attributeValueProposals.iterator(); iter.hasNext();) {
					ICompletionProposal element = (ICompletionProposal) iter.next();				
					contentAssistRequest.addProposal(element);					
				}
			}
		}
		
		else{
			
			ITextRegion nameRegion = null;
			while (i >= 0) {
				nameRegion = openRegions.get(i--);
				
				if (nameRegion.getType() == DOMRegionContext.XML_TAG_NAME)
					break;
			}
		
			String matchString = contentAssistRequest.getMatchString();				
			
			int offset = contentAssistRequest.getReplacementBeginPosition();
					
							
			List attributeValueProposals = getTagValueProposals(parentNodeName, matchString, offset, contentAssistRequest);
			if(attributeValueProposals!=null) {
				for (Iterator iter = attributeValueProposals.iterator(); iter.hasNext();) {
					ICompletionProposal element = (ICompletionProposal) iter.next();				
					contentAssistRequest.addProposal(element);					
				}
			}
				
			
		}				
		
	}
	
	
	
	abstract protected List getTagValueProposals(String parentName, String matchString, int offset, ContentAssistRequest contentAssistRequest);
	

	protected void addAttributeValueProposals(ContentAssistRequest contentAssistRequest) {
		super.addAttributeValueProposals(contentAssistRequest);
		
		IDOMNode node = (IDOMNode) contentAssistRequest.getNode();
	
		// Find the attribute region and name for which this position should have a value proposed
		IStructuredDocumentRegion open = node.getFirstStructuredDocumentRegion();
		ITextRegionList openRegions = open.getRegions();
		int i = openRegions.indexOf(contentAssistRequest.getRegion() );
		if (i < 0)
			return;
		ITextRegion nameRegion = null;
		while (i >= 0) {
			nameRegion = openRegions.get(i--);
			if (nameRegion.getType() == DOMRegionContext.XML_TAG_ATTRIBUTE_NAME)
				break;
		}
	
		String matchString = contentAssistRequest.getMatchString();
		int offset = contentAssistRequest.getReplacementBeginPosition();
		if (matchString == null) {
			matchString = ""; //$NON-NLS-1$			
		}
		
		if (matchString.length() > 0 && (matchString.startsWith("\"") || matchString.startsWith("'") ) ) {//$NON-NLS-2$//$NON-NLS-1$
			matchString = matchString.substring(1);
			offset = offset+1;
		}
		
	
		if (nameRegion != null) {
			String attributeName = open.getText(nameRegion);
			
			List attributeValueProposals = getAttributeValueProposals(attributeName, matchString, offset, contentAssistRequest);
			if(attributeValueProposals!=null) {
				for (Iterator iter = attributeValueProposals.iterator(); iter.hasNext();) {
					ICompletionProposal element = (ICompletionProposal) iter.next();				
					contentAssistRequest.addProposal(element);					
				}
			}
			
		}
		
	}

	abstract protected List getAttributeValueProposals(String attributeName, String matchString, int offset, ContentAssistRequest contentAssistRequest);
		
}
