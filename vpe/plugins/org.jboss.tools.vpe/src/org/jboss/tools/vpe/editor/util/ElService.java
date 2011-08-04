/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.vpe.editor.util;

import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.w3c.dom.Node;

/**
 * 
 * @author Eugeny Stherbin
 * @author Vitali Yemialyanchyk
 */
public class ElService {

	protected ELResolver elResolver;
	
	public ElService(VpePageContext pageContext) {
		elResolver = new ELResolver(pageContext);
	}

	public String replaceEl(String resourceString) {
		return getElResolver().replaceEl(resourceString);
	}

	/**
	 * Checks if is cloneable node.
	 * 
	 * @param sourceNode
	 *            the source node
	 * 
	 * @return true, if is cloneable node
	 */
	public boolean isELNode(Node sourceNode) {
		return getElResolver().isELNode(sourceNode);
	}

	public ResourceReference[] getResourceReferences() {
		return getElResolver().getResourceReferences();
	}

	public String replaceElAndResources(String value) {
		return getElResolver().replaceElAndResources(value);
	}
	
	private ELResolver getElResolver() {
		return elResolver;
	}

	public void setElResolver(ELResolver elResolver) {
		this.elResolver = elResolver;
	}

}
