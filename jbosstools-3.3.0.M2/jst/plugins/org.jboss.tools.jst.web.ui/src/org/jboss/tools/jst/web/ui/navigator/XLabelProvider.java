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
package org.jboss.tools.jst.web.ui.navigator;

import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.model.impl.ExtraRootImpl;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.model.pv.WebProjectNode;

/**
 * @author Viacheslav Kabanovich
 */
public class XLabelProvider extends WebProjectsLabelProvider {
	
	public static class RootWrapper  {
		public Object element;
	}
	
	public XLabelProvider() {}

	public String getText(Object element) {
		if(element instanceof RootWrapper) {
			element = ((RootWrapper)element).element;
		}
		if(element instanceof WebProjectNode && element instanceof ExtraRootImpl) {
			return WebUIMessages.WEB_RESOURCES;
		}
		return super.getText(element);
	}

	public Image getImage(Object element) {
		if(element instanceof RootWrapper) {
			element = ((RootWrapper)element).element;
		}
		return super.getImage(element); 
	}


}
