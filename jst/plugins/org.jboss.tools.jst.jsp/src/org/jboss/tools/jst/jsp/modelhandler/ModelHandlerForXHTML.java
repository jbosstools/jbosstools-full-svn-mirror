 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.jst.jsp.modelhandler;

import org.eclipse.jst.jsp.core.internal.modelhandler.ModelHandlerForJSP;
import org.eclipse.wst.sse.core.internal.provisional.IModelLoader;

/**
 * @author A. Yukhovich
 */
public class ModelHandlerForXHTML extends ModelHandlerForJSP {
	
	/**
	 * Needs to match what's in plugin registry. In fact, can be overwritten
	 * at run time with what's in registry! (so should never be 'final')
	 */
	static String AssociatedContentTypeID = "org.eclipse.wst.html.core.htmlsource"; //$NON-NLS-1$
	/**
	 * Needs to match what's in plugin registry. In fact, can be overwritten
	 * at run time with what's in registry! (so should never be 'final')
	 */
	private static String ModelHandlerID = "org.jboss.tools.jst.jsp.modelhandler.xhtml";


	public ModelHandlerForXHTML() {
		super();
		setId(ModelHandlerID);
		setAssociatedContentTypeId(AssociatedContentTypeID);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.sse.core.internal.ltk.modelhandler.IModelHandler#getModelLoader()
	 */
	public IModelLoader getModelLoader() {
		return new XHTMLModelLoader();
	}

}
