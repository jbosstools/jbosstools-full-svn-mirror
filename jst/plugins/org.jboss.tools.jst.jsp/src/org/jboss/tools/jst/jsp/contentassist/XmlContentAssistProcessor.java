/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.jsp.contentassist;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImageHelper;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImages;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.messages.JstUIMessages;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageContextFactory;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.taglib.INameSpace;

/**
 * 
 * @author Jeremy
 *
 */
@SuppressWarnings("restriction")
public class XmlContentAssistProcessor extends AbstractXMLContentAssistProcessor {
	protected static final Image JSF_EL_PROPOSAL_IMAGE = JspEditorPlugin.getDefault().getImage(JspEditorPlugin.CA_JSF_EL_IMAGE_PATH);

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.jsp.contentassist.AbstractXMLContentAssistProcessor#createContext()
	 */
	@Override
	protected ELContext createContext() {
		return PageContextFactory.createPageContext(getResource());
	}
	
	@Override 
	protected KbQuery createKbQuery(Type type, String query, String stringQuery) {
		return createKbQuery(type, query, stringQuery, getTagPrefix(), getTagUri());
	}

	@Override 
	protected KbQuery createKbQuery(Type type, String query, String stringQuery, String prefix, String uri) {
		KbQuery kbQuery = new KbQuery();

		String[] parentTags = getParentTags(type == Type.ATTRIBUTE_NAME || type == Type.ATTRIBUTE_VALUE);
		String	parent = getParent(type == Type.ATTRIBUTE_VALUE, type == Type.ATTRIBUTE_NAME);
		String queryValue = query;
		String queryStringValue = stringQuery;
		
		kbQuery.setPrefix(prefix);
		kbQuery.setUri(uri);
		kbQuery.setParentTags(parentTags);
		kbQuery.setParent(parent); 
		kbQuery.setMask(true); 
		kbQuery.setType(type);
		kbQuery.setOffset(getOffset());
		kbQuery.setValue(queryValue); 
		kbQuery.setStringQuery(queryStringValue);
		
		return kbQuery;
	}

	/**
	 * Returns URI string for the prefix specified using the namespaces collected for 
	 * the {@link IPageContext} context.
	 * 
	 * 	@Override org.jboss.tools.jst.jsp.contentassist.AbstractXMLContentAssistProcessor#getUri(String)
	 */
	protected String getUri(String prefix) {
		return null;
	}

	/**
	 * Calculates and adds the tag proposals to the Content Assist Request object
	 * 
	 * @param contentAssistRequest Content Assist Request object
	 * @param childPosition the 
	 */

	@Override
	protected void addTagInsertionProposals(
			ContentAssistRequest contentAssistRequest, int childPosition) {
		
		// Need to check if an EL Expression is opened here.
		// If it is true we don't need to start any new tag proposals
		TextRegion prefix = getELPrefix();
		if (prefix != null && prefix.isELStarted()) {
			return;
		}
		
		addTagNameProposals(contentAssistRequest, childPosition);
	}

	private void addTagNameProposalsForPrefix(
			ContentAssistRequest contentAssistRequest, 
			int childPosition, 
			String query,
			String prefix,
			String uri, 
			int defaultRelevance
			) {
		if (query == null)
			query = ""; //$NON-NLS-1$
		String stringQuery = "<" + query; //$NON-NLS-1$
				
		KbQuery kbQuery = createKbQuery(Type.TAG_NAME, query, stringQuery, prefix, uri);
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, getContext());
		
		for (int i = 0; proposals != null && i < proposals.length; i++) {
			TextProposal textProposal = proposals[i];
			
			String replacementString = textProposal.getReplacementString();
			String closingTag = textProposal.getLabel();
			if (closingTag != null && closingTag.startsWith("<")) { //$NON-NLS-1$
				closingTag = closingTag.substring(1);
			}
			
			if (replacementString.startsWith("<")) { //$NON-NLS-1$
				// Because the tag starting char is already in the text
				replacementString = replacementString.substring(1);
			}
			if (!replacementString.endsWith("/>")) { //$NON-NLS-1$
				replacementString += "</" + closingTag + ">"; //$NON-NLS-1$ //$NON-NLS-2$
			}

		
			int replacementOffset = contentAssistRequest.getReplacementBeginPosition();
			int replacementLength = contentAssistRequest.getReplacementLength();
			int cursorPosition = getCursorPositionForProposedText(replacementString);
			Image image = textProposal.getImage();
			if (image == null) {
				image = XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_OBJ_TAG_GENERIC);
			}

			String displayString = closingTag;
			IContextInformation contextInformation = null;
			String additionalProposalInfo = textProposal.getContextInfo();
			int relevance = textProposal.getRelevance();
			if (relevance == TextProposal.R_NONE) {
				relevance = defaultRelevance == TextProposal.R_NONE? TextProposal.R_TAG_INSERTION : defaultRelevance;
			}

			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(true, replacementString, 
					replacementOffset, replacementLength, cursorPosition, image, displayString, 
					contextInformation, additionalProposalInfo, relevance);

			contentAssistRequest.addProposal(proposal);
		}
	}

	/**
	 * Calculates and adds the tag name proposals to the Content Assist Request object
	 * 
	 * @param contentAssistRequest Content Assist Request object
	 * @param childPosition the 
	 */
	@Override
	protected void addTagNameProposals(
			ContentAssistRequest contentAssistRequest, int childPosition) {

		String mainPrefix = getTagPrefix();
		String mainURI = getTagUri();
		
		String query = contentAssistRequest.getMatchString();
		addTagNameProposalsForPrefix(contentAssistRequest, childPosition, query, mainPrefix, mainURI, TextProposal.R_TAG_INSERTION);

		if (query == null || query.length() == 0 || query.contains(":")) //$NON-NLS-1$
			return;
		
		// Make an additional proposals to allow prefixed tags to be entered with no prefix typed
		ELContext elContext = getContext();
		if (elContext instanceof IPageContext) {
			IPageContext pageContext = (IPageContext)elContext;
			Map<String, List<INameSpace>> nsMap = pageContext.getNameSpaces(contentAssistRequest.getReplacementBeginPosition());
			if (nsMap == null) return;
			
			for (List<INameSpace> namespaces : nsMap.values()) {
				if (namespaces == null) continue;
				
				for (INameSpace namespace : namespaces) {
					String possiblePrefix = namespace.getPrefix(); 
					String possibleURI = namespace.getURI();
					String possibleQuery = namespace.getPrefix() + ":" + query; //$NON-NLS-1$
					addTagNameProposalsForPrefix(contentAssistRequest, childPosition, 
							possibleQuery, possiblePrefix, possibleURI, 
							TextProposal.R_TAG_INSERTION - 1); // TODO: Make relevance to be lower here
				}
			}
		}
	}

	/**
	 * Calculates and adds the attribute value proposals to the Content Assist Request object
	 */
	protected void addAttributeValueProposals(ContentAssistRequest contentAssistRequest) {
		// Need to check if an EL Expression is opened here.
		// If it is true we don't need to start any new tag proposals
		TextRegion prefix = getELPrefix();
		if (prefix != null && prefix.isELStarted()) {
			return;
		}
		
		String matchString = contentAssistRequest.getMatchString();
		String query = matchString;
		if (query == null)
			query = ""; //$NON-NLS-1$
		String stringQuery = matchString;

		KbQuery kbQuery = createKbQuery(Type.ATTRIBUTE_VALUE, query, stringQuery);
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, getContext());

		for (int i = 0; proposals != null && i < proposals.length; i++) {
			TextProposal textProposal = proposals[i];
			int replacementOffset = contentAssistRequest.getReplacementBeginPosition();
			int replacementLength = contentAssistRequest.getReplacementLength();
			if(textProposal.getStart() >= 0 && textProposal.getEnd() >= 0) {
				replacementOffset += textProposal.getStart() + 1;
				replacementLength = textProposal.getEnd() - textProposal.getStart();
			}
			String replacementString = "\"" + textProposal.getReplacementString() + "\""; //$NON-NLS-1$ //$NON-NLS-2$
			if(textProposal.getStart() >= 0 && textProposal.getEnd() >= 0) {
				replacementString = textProposal.getReplacementString();
			}
			int cursorPosition = getCursorPositionForProposedText(replacementString);
			Image image = textProposal.getImage();
			String displayString = textProposal.getLabel() == null ? 
					replacementString : 
						textProposal.getLabel();
			IContextInformation contextInformation = null;
			String additionalProposalInfo = textProposal.getContextInfo();
			int relevance = textProposal.getRelevance();
			if (relevance == TextProposal.R_NONE) {
				relevance = TextProposal.R_JSP_ATTRIBUTE_VALUE;
			}

			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(replacementString, 
					replacementOffset, replacementLength, cursorPosition, image, displayString, 
					contextInformation, additionalProposalInfo, relevance);

			contentAssistRequest.addProposal(proposal);
		}
	}

	@Override
	protected void addAttributeValueELProposals(ContentAssistRequest contentAssistRequest) {
		TextRegion prefix = getELPrefix();
		if (prefix == null) {
			return;
		}

		if(!prefix.isELStarted()) {
			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(true, "#{}",  //$NON-NLS-1$
					getOffset(), 0, 2, JSF_EL_PROPOSAL_IMAGE, JstUIMessages.JspContentAssistProcessor_NewELExpression, 
					null, JstUIMessages.JspContentAssistProcessor_NewELExpressionAttrInfo, TextProposal.R_XML_ATTRIBUTE_VALUE_TEMPLATE);

			contentAssistRequest.addProposal(proposal);
			return;
		}
		String matchString = "#{" + prefix.getText(); //$NON-NLS-1$
		String query = matchString;
		if (query == null)
			query = ""; //$NON-NLS-1$
		String stringQuery = matchString;
		
		int beginChangeOffset = prefix.getStartOffset() + prefix.getOffset();
				
		KbQuery kbQuery = createKbQuery(Type.ATTRIBUTE_VALUE, query, stringQuery);
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, getContext());
		
		for (int i = 0; proposals != null && i < proposals.length; i++) {
			TextProposal textProposal = proposals[i];
			
			int replacementOffset = beginChangeOffset;
			int replacementLength = prefix.getLength();
			String replacementString = prefix.getText().substring(0, replacementLength) + textProposal.getReplacementString();
			int cursorPosition = replacementString.length();
			Image image = textProposal.getImage();
			
			// JBIDE-512, JBIDE-2541 related changes ===>>>
//				String displayString = prefix.getText().substring(0, replacementLength) + textProposal.getReplacementString();
			String displayString = textProposal.getLabel();
			if (displayString == null)
				displayString = textProposal.getReplacementString() == null ? replacementString : textProposal.getReplacementString();
			// <<<=== JBIDE-512, JBIDE-2541 related changes

			IContextInformation contextInformation = null;
			String additionalProposalInfo = (textProposal.getContextInfo() == null ? "" : textProposal.getContextInfo()); //$NON-NLS-1$
			int relevance = textProposal.getRelevance();
			if (relevance == TextProposal.R_NONE) {
				relevance = TextProposal.R_JSP_JSF_EL_VARIABLE_ATTRIBUTE_VALUE;
			}

			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(replacementString, 
					replacementOffset, replacementLength, cursorPosition, image, displayString, 
					contextInformation, additionalProposalInfo, relevance);

			contentAssistRequest.addProposal(proposal);
		}

		if (prefix.isELStarted() && !prefix.isELClosed()) {
			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal("}", //$NON-NLS-1$
					getOffset(), 0, 1, JSF_EL_PROPOSAL_IMAGE, JstUIMessages.JspContentAssistProcessor_CloseELExpression, 
					null, JstUIMessages.JspContentAssistProcessor_CloseELExpressionInfo, TextProposal.R_XML_ATTRIBUTE_VALUE_TEMPLATE);

			contentAssistRequest.addProposal(proposal);
		}
	}

	@Override
	protected void addTextELProposals(ContentAssistRequest contentAssistRequest) {
		TextRegion prefix = getELPrefix();
		if (prefix == null || !prefix.isELStarted()) {
			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(true, "#{}", //$NON-NLS-1$ 
					contentAssistRequest.getReplacementBeginPosition(), 
					0, 2, JSF_EL_PROPOSAL_IMAGE, JstUIMessages.JspContentAssistProcessor_NewELExpression, null, 
					JstUIMessages.FaceletPageContectAssistProcessor_NewELExpressionTextInfo, TextProposal.R_XML_ATTRIBUTE_VALUE_TEMPLATE);
			
			contentAssistRequest.addProposal(proposal);
			return;
		}
		String matchString = "#{" + prefix.getText(); //$NON-NLS-1$
		String query = matchString;
		if (query == null)
			query = ""; //$NON-NLS-1$
		String stringQuery = matchString;

		int beginChangeOffset = prefix.getStartOffset() + prefix.getOffset();

		KbQuery kbQuery = createKbQuery(Type.TEXT, query, stringQuery);
		TextProposal[] proposals = PageProcessor.getInstance().getProposals(kbQuery, getContext());

		for (int i = 0; proposals != null && i < proposals.length; i++) {
			TextProposal textProposal = proposals[i];

			int replacementOffset = beginChangeOffset;
			int replacementLength = prefix.getLength();
			String replacementString = prefix.getText().substring(0, replacementLength) + textProposal.getReplacementString();
			int cursorPosition = replacementString.length();
			Image image = textProposal.getImage();

			// JBIDE-512, JBIDE-2541 related changes ===>>>
//			String displayString = prefix.getText().substring(0, replacementLength) + textProposal.getReplacementString();
			String displayString = textProposal.getLabel();
			if (displayString == null)
				displayString = textProposal.getReplacementString() == null ? replacementString : textProposal.getReplacementString();

			// <<<=== JBIDE-512, JBIDE-2541 related changes
			IContextInformation contextInformation = null;
			String additionalProposalInfo = textProposal.getContextInfo();
			int relevance = textProposal.getRelevance();
			if (relevance == TextProposal.R_NONE) {
				relevance = TextProposal.R_JSP_JSF_EL_VARIABLE_ATTRIBUTE_VALUE;
			}

			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal(replacementString, 
					replacementOffset, replacementLength, cursorPosition, image, displayString, 
					contextInformation, additionalProposalInfo, relevance);

			contentAssistRequest.addProposal(proposal);
		}

		if (prefix.isELStarted() && !prefix.isELClosed()) {
			AutoContentAssistantProposal proposal = new AutoContentAssistantProposal("}", //$NON-NLS-1$
					getOffset(), 0, 1, JSF_EL_PROPOSAL_IMAGE, JstUIMessages.JspContentAssistProcessor_CloseELExpression, 
					null, JstUIMessages.JspContentAssistProcessor_CloseELExpressionInfo, TextProposal.R_XML_ATTRIBUTE_VALUE_TEMPLATE);

			contentAssistRequest.addProposal(proposal);
		}
	}
}