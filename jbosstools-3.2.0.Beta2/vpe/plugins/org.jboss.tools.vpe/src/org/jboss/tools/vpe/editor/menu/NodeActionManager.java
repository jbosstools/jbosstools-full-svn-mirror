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
package org.jboss.tools.vpe.editor.menu;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.wst.sse.core.internal.format.IStructuredFormatProcessor;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.w3c.dom.Node;

/**
 * 
 * @author eskimo(dgolovin@exadel.com)
 *
 */
public class NodeActionManager extends AbstractActionManager {
	/**
	 * @deprecated
	 * @param model
	 * @param modelQuery
	 * @param viewer
	 */
	public NodeActionManager(IStructuredModel model, ModelQuery modelQuery, Viewer viewer) {
		super(model, modelQuery, viewer);
	}

	public NodeActionManager(IStructuredModel model, Viewer viewer) {
		this(model, ModelQueryUtil.getModelQuery(model), viewer);
	}

	/**
	 * @param model
	 * @return
	 */
	public static ModelQuery getModelQuery(IStructuredModel model) {
		return ModelQueryUtil.getModelQuery(model);
	}

	public void reformat(Node newElement, boolean deep) {
		try {
			model.aboutToChangeModel();
			IStructuredFormatProcessor formatProcessor = new FormatProcessorXML();
			formatProcessor.formatNode(newElement);
		}
		finally {
			model.changedModel();
		}
	}

	public void setModel(IStructuredModel newModel) {
		model = newModel;
		setModelQuery(ModelQueryUtil.getModelQuery(newModel));
	}

	protected void setModelQuery(ModelQuery newModelQuery) {
		modelQuery = newModelQuery;
	}
}