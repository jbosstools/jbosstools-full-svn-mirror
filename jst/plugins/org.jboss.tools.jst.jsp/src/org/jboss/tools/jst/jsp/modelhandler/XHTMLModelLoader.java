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

import java.util.Iterator;
import java.util.List;

import org.eclipse.jst.jsp.core.internal.modelhandler.JSPModelLoader;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapterFactory;
import org.eclipse.wst.xml.core.internal.ssemodelquery.ModelQueryAdapter;
import org.jboss.tools.jst.jsp.modelquery.ModelQueryAdapterFactoryForXHTML;

/**
 * @author A. Yukhovich
 *
 */
public class XHTMLModelLoader extends JSPModelLoader {
	
	public XHTMLModelLoader() {
	}

	public List getAdapterFactories() {
		List factories = super.getAdapterFactories();
		/*
		 * Replace the default JSP model query by using our own factory
		 */

		Iterator i = factories.iterator();
		while (i.hasNext()) {
			if (((INodeAdapterFactory) i.next()).isFactoryForType(ModelQueryAdapter.class)) {
				i.remove();
			}
		}

		factories.add(new ModelQueryAdapterFactoryForXHTML());
		return factories;
	}
}
