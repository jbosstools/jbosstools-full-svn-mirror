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
package org.jboss.tools.jst.jsp.support.kb;

import java.io.InputStream;
import java.util.*;
import org.eclipse.ui.IEditorInput;
import org.jboss.tools.common.kb.*;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

public class WTPKbdJSFIDResource extends WTPKbAbstractModelResource {
	public static String SUPPORTED_ID = WebPromptingProvider.SHALE_COMPONENTS;

	public WTPKbdJSFIDResource(IEditorInput fEditorInput) {
		super(fEditorInput);
	}

	public void setConstraint(String name, String value) {
	}

	public void clearConstraints() {
	}

	public Collection<KbProposal> queryProposal(String query) {
		Collection<KbProposal> proposals = new ArrayList<KbProposal>();
		try {
			if (!isReadyToUse()) return proposals;
			String rQuery = getPassiveQueryPart(query);
			
			Set<String> sorted = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			List beanList = fProvider.getList(fXModel, SUPPORTED_ID, "", null);
			Iterator itr = beanList.iterator();
			while (itr.hasNext()) sorted.add(itr.next().toString());				
			if (sorted.isEmpty()) return proposals;
			Iterator it = sorted.iterator();
			while(it.hasNext()) {
				String text = (String)it.next();
				if(rQuery.length() == 0 || text.startsWith(rQuery)) {
					KbProposal proposal = new KbProposal();
					proposal.setLabel(text);
					proposal.setReplacementString(text);
					proposal.setIcon(KbIcon.ENUM_ITEM);
					proposals.add(proposal);
					proposal.setPosition(proposal.getReplacementString().length());
				}
			}
		} catch (Exception x) {
			JspEditorPlugin.getPluginLog().logError(x);
		}
		return proposals;
	}

	private String getPassiveQueryPart(String query) {
		if (query == null || query.trim().length() == 0) return "";
		int startIndex = query.length();
		return query.substring(0, startIndex);
	}

	public boolean isReadyToUse() {
		return (fProvider != null && fXModel != null);
	}
	
	public String getType() {
		return KbDinamicResource.JSF_ID;
	}

	public InputStream getInputStream() {
		return null;
	}

}
