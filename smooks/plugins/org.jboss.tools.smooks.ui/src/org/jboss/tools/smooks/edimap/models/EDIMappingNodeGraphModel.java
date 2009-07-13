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
package org.jboss.tools.smooks.edimap.models;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.model.medi.Segment;

/**
 * @author Dart (dpeng@redhat.com)
 *
 */
public class EDIMappingNodeGraphModel extends TreeNodeModel {

	public EDIMappingNodeGraphModel(Object data, ITreeContentProvider contentProvider, ILabelProvider labelProvider) {
		super(data, contentProvider, labelProvider);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#createChildModel(java.lang.Object, org.eclipse.jface.viewers.ITreeContentProvider, org.eclipse.jface.viewers.ILabelProvider)
	 */
	@Override
	protected TreeNodeModel createChildModel(Object model, ITreeContentProvider contentProvider,
			ILabelProvider labelProvider) {
		return new EDIMappingNodeGraphModel(model, contentProvider, labelProvider);
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#canLinkWithSource(java.lang.Object)
	 */
	@Override
	public boolean canLinkWithSource(Object model) {
		return false;
	}
	
	

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#isLinkable()
	 */
	@Override
	public boolean isLinkable() {
		Object nodeModel = getData();
		if(!(nodeModel instanceof Segment)){
			return false;
		}
		return super.isLinkable();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#canLinkWithTarget(java.lang.Object)
	 */
	@Override
	public boolean canLinkWithTarget(Object model) {
		Object nodeModel = getData();
		if(!(nodeModel instanceof Segment)){
			return false;
		}
		if(model instanceof EDIDataContainerGraphModel){
			return true;
		}
		return false;
	}


}
