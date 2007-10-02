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
package org.jboss.tools.jst.jsp.modelquery;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.jsp.core.internal.modelquery.JSPModelQueryAdapterImpl;
import org.eclipse.jst.jsp.core.internal.modelquery.ModelQueryAdapterFactoryForJSP;
import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolver;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelqueryimpl.ModelQueryImpl;
import org.eclipse.wst.xml.core.internal.contentmodel.modelqueryimpl.SimpleAssociationProvider;
import org.eclipse.wst.xml.core.internal.contentmodel.util.CMDocumentCache;
import org.eclipse.wst.xml.core.internal.modelquery.XMLCatalogIdResolver;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.jst.jsp.JspEditorPlugin;

/**
 * @author A. Yukhovich
 *
 */
public class ModelQueryAdapterFactoryForXHTML extends ModelQueryAdapterFactoryForJSP {
	public ModelQueryAdapterFactoryForXHTML() {
	}

	public ModelQueryAdapterFactoryForXHTML(Object key, boolean registerAdapters) {
		super(key, registerAdapters);
	}

	@SuppressWarnings("restriction")
	public INodeAdapterFactory copy() {
		return new ModelQueryAdapterFactoryForXHTML(getAdapterKey(), isShouldRegisterAdapter());
	}

	@SuppressWarnings("restriction")
	ModelQuery createModelQuery(IStructuredModel model, URIResolver resolver) {
		return new ModelQueryImpl(new SimpleAssociationProvider(new XHTMLModelQueryCMProvider()));
	}
	
	/**
	 * createAdapter method comment.
	 */
	protected INodeAdapter createAdapter(INodeNotifier target) {

		if (JspEditorPlugin.DEBIG_INFO) {
			System.out.println("-----------------------ModelQueryAdapterFactoryForXHTML.createAdapter" + target); //$NON-NLS-1$
		}
		
		if (modelQueryAdapterImpl == null) {
			if (target instanceof IDOMNode) {
				IDOMNode xmlNode = (IDOMNode) target;
				IStructuredModel model = stateNotifier = xmlNode.getModel();
				String baseLocation = model.getBaseLocation();
				// continue only if the location is known
				if (baseLocation != null) {
					stateNotifier.addModelStateListener(this);
					File file = new Path(model.getBaseLocation()).toFile();
					if (file.exists()) {
						baseLocation = file.getAbsolutePath();
					}
					else {
						IPath basePath = new Path(model.getBaseLocation());
						IPath derivedPath = null;
						if (basePath.segmentCount() > 1)
							derivedPath = ResourcesPlugin.getWorkspace().getRoot().getFile(basePath).getLocation();
						else
							derivedPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().append(basePath);
						if (derivedPath != null) {
							baseLocation = derivedPath.toString();
						}
					}
					URIResolver resolver = new XMLCatalogIdResolver(baseLocation, model.getResolver());

					ModelQuery modelQuery = createModelQuery(model, resolver);
					modelQuery.setEditMode(ModelQuery.EDIT_MODE_UNCONSTRAINED);
					modelQueryAdapterImpl = new JSPModelQueryAdapterImpl(new CMDocumentCache(), modelQuery, resolver);
				}
			}
		}
		return modelQueryAdapterImpl;
	}

}
