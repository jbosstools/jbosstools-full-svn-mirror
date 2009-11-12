/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.graphical.editors.model.javamapping;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.model.AbstractResourceConfigGraphModel;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.CSVLinkConnection;

/**
 * @author Dart
 * 
 */
public class JavaBeanGraphModel extends AbstractResourceConfigGraphModel {
	

	public JavaBeanGraphModel(Object data, ITreeContentProvider contentProvider, ILabelProvider labelProvider,
			IEditingDomainProvider domainProvider) {
	    super(data,contentProvider,labelProvider,domainProvider);
	}

	@Override
	protected TreeNodeModel createChildModel(Object model, ITreeContentProvider contentProvider,
			ILabelProvider labelProvider) {
		Object m = AdapterFactoryEditingDomain.unwrap(model);
		if(m instanceof String) return null;
		return new JavaBeanChildGraphModel(model, contentProvider, labelProvider, this.domainProvider);
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#isLinkable(java.lang.Class)
	 */
	@Override
	public boolean isLinkable(Class<?> connectionType) {
		if(connectionType == CSVLinkConnection.class){
			return true;
		}
		return false;
//		return super.isLinkable(connectionType);
	}
}
