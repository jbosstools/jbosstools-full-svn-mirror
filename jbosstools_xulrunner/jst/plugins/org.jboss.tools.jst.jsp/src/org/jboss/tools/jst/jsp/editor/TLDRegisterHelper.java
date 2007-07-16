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
package org.jboss.tools.jst.jsp.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;

import org.jboss.tools.common.kb.KbTldResource;
import org.jboss.tools.common.kb.wtp.JspWtpKbConnector;
import org.jboss.tools.common.kb.wtp.TLDVersionHelper;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.ui.editors.dnd.DropUtils;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.web.tld.TaglibData;

public class TLDRegisterHelper {

	public static void registerTld(TaglibData data, JspWtpKbConnector wtpKbConnector, IDocument document) {
		String version = TLDVersionHelper.getTldVersion(data.getUri(), data.getPrefix(), document);
		KbTldResource resource = new KbTldResource(data.getUri(), "", data.getPrefix(), version);
		if(data.isNs()) {
			resource.setJsfResource(true);
			boolean registrated = wtpKbConnector.registerResource(resource, true);
	        if(!registrated || resource.isCustomTld()) {
	        	try {
					resource = (KbTldResource)resource.clone();
				} catch (CloneNotSupportedException e) {
					JspEditorPlugin.getPluginLog().logError(e);
					return;
				}
	        	IEditorInput input = JspEditorPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput();
	        	String tldContent = DropUtils.getTldContent(input, data.getUri());
	        	if(tldContent!=null) {
	        		resource.setTldContent(tldContent);
	        		wtpKbConnector.registerResource(resource, true);
	        	}
	        }
		} else {
			wtpKbConnector.registerResource(resource);
		}
	}

}
