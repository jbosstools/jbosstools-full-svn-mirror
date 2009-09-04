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

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Region;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.jboss.tools.common.el.core.model.ELExpression;
import org.jboss.tools.common.el.core.parser.ELParserFactory;
import org.jboss.tools.common.el.core.parser.ELParserUtil;
import org.jboss.tools.common.el.core.resolver.ELCompletionEngine;
import org.jboss.tools.common.el.core.resolver.ELContext;
import org.jboss.tools.common.el.core.resolver.ELContextImpl;
import org.jboss.tools.common.el.core.resolver.ELOperandResolveStatus;
import org.jboss.tools.common.el.core.resolver.ELResolver;
import org.jboss.tools.common.el.core.resolver.ElVarSearcher;
import org.jboss.tools.common.el.core.resolver.Var;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.KbQuery.Type;

/**
 * 
 * @author Jeremy
 *
 */
public class XmlContentAssistProcessor extends AbstractXMLContentAssistProcessor {

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.jsp.contentassist.AbstractXMLContentAssistProcessor#createContext()
	 */
	@Override
	protected ELContext createContext() {
		IFile file = getResource();
		ELResolver[] elResolvers = getELResolvers(file);

		ELContextImpl context = new ELContextImpl();
		context.setResource(getResource());
		context.setElResolvers(elResolvers);
		setVars(context, file);

		return context;
	}

	protected void setVars(ELContextImpl context, IFile file) {
		ELCompletionEngine fakeEngine = new ELCompletionEngine(){
		
			public ELOperandResolveStatus resolveELOperand(IFile file,
					ELExpression operand, boolean returnEqualedVariablesOnly,
					List<Var> vars, ElVarSearcher varSearcher)
					throws BadLocationException, StringIndexOutOfBoundsException {
				return null;
			}
		
			public ELParserFactory getParserFactory() {
				return ELParserUtil.getJbossFactory();
			}
		};
		ElVarSearcher varSearcher = new ElVarSearcher(file, fakeEngine);
		List<Var> vars = varSearcher.findAllVars(file, getOffset());

		if (vars != null) {
			for (Var var : vars) {
				context.addVar(new Region(getOffset(), 0), var);
			}
		}
	}

	@Override 
	protected KbQuery createKbQuery(Type type, String query, String stringQuery) {
		KbQuery kbQuery = new KbQuery();

		String prefix = getTagPrefix();
		String  uri = getTagUri();
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


	@Override
	protected void addTagNameProposals(
			ContentAssistRequest contentAssistRequest, int childPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addAttributeValueELProposals(ContentAssistRequest contentAssistRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addTextELProposals(ContentAssistRequest contentAssistRequest) {
		// TODO Auto-generated method stub
		
	}
	
	
}