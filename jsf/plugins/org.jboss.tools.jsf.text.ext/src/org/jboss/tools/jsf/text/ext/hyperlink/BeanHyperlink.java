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
package org.jboss.tools.jsf.text.ext.hyperlink;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink;
import org.jboss.tools.jsf.text.ext.JSFExtensionsPlugin;
import org.jboss.tools.jst.web.project.list.WebPromptingProvider;

/**
 * @author Jeremy
 */
public class BeanHyperlink extends AbstractHyperlink {

	/**
	 * @see com.ibm.sse.editor.AbstractHyperlink#doHyperlink(org.eclipse.jface.text.IRegion)
	 */
	protected void doHyperlink(IRegion region) {
		XModel xModel = getXModel();
		if (xModel == null || region == null) return;
		WebPromptingProvider provider = WebPromptingProvider.getInstance();
		region = JSPBeanHyperlinkPartitioner.getRegionPart(getDocument(), region.getOffset());
		try {	
			if(region == null) return;
			String beanName = getDocument().get(region.getOffset(), region.getLength());
			if(beanName == null) return;
			provider.getList(xModel, WebPromptingProvider.JSF_BEAN_OPEN, beanName, null);
		} catch (BadLocationException x) {
			JSFExtensionsPlugin.log("", x);
		}
	}

	/**
	 * @see com.ibm.sse.editor.AbstractHyperlink#doGetHyperlinkRegion(int)
	 */
	protected IRegion doGetHyperlinkRegion(int offset) {
		IRegion region = JSPBeanHyperlinkPartitioner.getWordRegion(getDocument(), offset);
		return region;
	}

}
