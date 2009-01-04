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
import java.util.Iterator;
import java.util.List;

import org.jboss.tools.smooks.ui.editors.TransformDataTreeViewer;

/**
 * @author Dart Peng
 * @Date Jul 25, 2008
 */
public class TagObject extends AbstractXMLObject {
	protected List<TagPropertyObject> properties = new ArrayList<TagPropertyObject>();

	public List<TagPropertyObject> getProperties() {
		return properties;
	}

	public void setProperties(List<TagPropertyObject> properties) {
		this.properties = properties;
	}

	public void addProperty(TagPropertyObject pro) {
		this.getProperties().add(pro);
		if (pro != null)
			pro.setParent(this);
		support.firePropertyChange(TransformDataTreeViewer.ADD_CHILDREN_EVENT, null, pro);
	}

	public void removeProperty(TagPropertyObject pro) {
		this.getProperties().remove(pro);
		if (pro != null)
			pro.setParent(null);
		support.firePropertyChange(TransformDataTreeViewer.REMOVE_CHILDREN_EVENT, pro, null);
	}

	public void addChildTag(TagObject tag) {
		this.getChildren().add(tag);
		if (tag != null)
			tag.setParent(this);
		support.firePropertyChange(TransformDataTreeViewer.ADD_CHILDREN_EVENT, null, tag);
	}

	public void removeChildTag(TagObject tag) {
		this.getChildren().remove(tag);
		if (tag != null)
			tag.setParent(null);
		support.firePropertyChange(TransformDataTreeViewer.REMOVE_CHILDREN_EVENT, tag, null);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer(getName());
		buffer.append("\n");
		for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
			TagPropertyObject pro = (TagPropertyObject) iterator.next();
			buffer.append("\t");
			buffer.append(pro.getName());
			buffer.append("\n");
		}

		List l = getChildren();
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			TagObject tag = (TagObject) iterator.next();
			buffer.append("\t");
			buffer.append(tag.toString());
			buffer.append("\n");
		}
		return buffer.toString();
	}

}
