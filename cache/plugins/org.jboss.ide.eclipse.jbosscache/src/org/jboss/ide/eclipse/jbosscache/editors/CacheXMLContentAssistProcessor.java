package org.jboss.ide.eclipse.jbosscache.editors;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.wst.sse.ui.internal.contentassist.CustomCompletionProposal;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.editor.CMImageUtil;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImageHelper;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImages;

public class CacheXMLContentAssistProcessor extends CacheContentAssistProcessor {

	CacheAttributeMatcher extractor;
	
	static String[] propertyNames;
	
	private static CacheXMLContentAssistProcessor processor = new CacheXMLContentAssistProcessor();
	
	
	public static CacheXMLContentAssistProcessor getContentAsist(){
		return processor;
	}
	
	
	public CacheXMLContentAssistProcessor() {
		super();
	
		extractor = new CacheAttributeMatcher();
		
	}

	
	protected List getAttributeValueProposals(String attributeName, String matchString, int offset, ContentAssistRequest contentAssistRequest) {
		String nodeName = contentAssistRequest.getNode().getNodeName();
		if("attribute".equals(nodeName) && "name".equals(attributeName)) {
			List types = this.extractor.findMatchingPropertyTypes(matchString);
			
			List proposals = new ArrayList(types.size() );		
			for (Iterator iter = types.iterator(); iter.hasNext();) {
				String element = (String) iter.next();
				proposals.add(new CompletionProposal(element, offset, matchString.length(), element.length(), null, null, null, null) );
			}
			return proposals;
		}
		
		
		if("mapping".equals(nodeName) && "resource".equals(attributeName)) {
			
		}
		
		return Collections.EMPTY_LIST;
	}


	@Override
	protected List getTagValueProposals(String parentName,String matchString, int offset, ContentAssistRequest contentAssistRequest) {
		
		if(parentName == null || parentName.equals(""))
			return null;
		
		List types = null;
		if(matchString.equals("")){
			//Add all possible tags
			
			types = this.extractor.findTagMatcher(parentName);			

		}else{
			//Get Match String
			types = this.extractor.findTagMatcher(parentName,matchString);
		}
			
			List proposals = new ArrayList(types.size() );
			for (int i=0;i<types.size();i++) {
				String element = getEndTag((String) types.get(i));
				proposals.add(new CustomCompletionProposal(element,offset,matchString.length(),element.length(),XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_OBJ_TAG_GENERIC),(String) types.get(i),null,null,0,false));
			}
			return proposals;			
						
	}
	
	private String getEndTag(String tag){
		return tag+">"+"</"+tag+">";
	}

	
}
