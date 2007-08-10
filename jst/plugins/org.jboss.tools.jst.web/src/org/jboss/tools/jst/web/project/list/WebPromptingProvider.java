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
package org.jboss.tools.jst.web.project.list;

import java.util.*;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.util.ModelFeatureFactory;
import org.jboss.tools.jst.web.WebModelPlugin;

public class WebPromptingProvider implements IWebPromptingProvider {
	
	static IWebPromptingProvider[] providers;
	
	static {
		String[] pns = new String[]{
			"org.jboss.tools.jsf.model.pv.JSFPromptingProvider",
			"org.jboss.tools.struts.model.pv.StrutsPromptingProvider",
			"org.jboss.tools.shale.model.pv.ShalePromptingProvider"
		};
		List<IWebPromptingProvider> l = new ArrayList<IWebPromptingProvider>();
		for (int i = 0; i < pns.length; i++) {
			try {
				IWebPromptingProvider p = (IWebPromptingProvider)ModelFeatureFactory.getInstance().createFeatureInstance(pns[i]);
				if(p != null) l.add(p);
			} catch (Exception e) {
				WebModelPlugin.getPluginLog().logError(e);
			}
		}
		providers = l.toArray(new IWebPromptingProvider[0]);
	}
	
	public static WebPromptingProvider getInstance() {
		return WebPromptingProviderHolder.provider;
	}
	
	public boolean isSupporting(String id) {
		for (int i = 0; i < providers.length; i++) {
			if(providers[i].isSupporting(id)) return true;
		}
		return false;
	}

	public List<Object> getList(XModel model, String id, String prefix, Properties properties) {
		for (int i = 0; i < providers.length; i++) {
			if(providers[i].isSupporting(id)) return providers[i].getList(model, id, prefix, properties);
		}
		return EMPTY_LIST;
	}
	
	static class WebPromptingProviderHolder {
		static WebPromptingProvider provider = new WebPromptingProvider();
	}

}
