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
package org.jboss.tools.smooks.xml.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dart Peng
 * @Date Jul 25, 2008
 */
public class AbstractXMLObject {
	
	protected AbstractXMLObject parent;
	
	protected String name;
	
	public String getNamespaceURL() {
		return namespaceURL;
	}

	public void setNamespaceURL(String namespaceURL) {
		this.namespaceURL = namespaceURL;
	}

	protected String namespaceURL = null;
	
	protected List<AbstractXMLObject> children = new ArrayList<AbstractXMLObject>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AbstractXMLObject> getChildren() {
		return children;
	}

	public void setChildren(List<AbstractXMLObject> children) {
		this.children = children;
	}

	/**
	 * @return the parent
	 */
	public AbstractXMLObject getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(AbstractXMLObject parent) {
		this.parent = parent;
	}
	
	
}
