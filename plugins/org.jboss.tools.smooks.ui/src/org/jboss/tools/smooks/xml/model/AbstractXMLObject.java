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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.jboss.tools.smooks.ui.IXMLStructuredObject;
import org.jboss.tools.smooks.ui.editors.TransformDataTreeViewer;

/**
 * @author Dart Peng
 * @Date Jul 25, 2008
 */
public class AbstractXMLObject implements ITransformTreeNode , IXMLStructuredObject{
	
	protected PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	protected Element referenceElement = null;

	protected boolean canEdit = false;
		
	public boolean isCanEdit() {
		return canEdit;
	}
	
	public boolean isAttribute(){
		return false;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

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
	
	public PropertyChangeListener[] getPropertyChangeListeners(){
		return support.getPropertyChangeListeners();
	}

	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		Element element = this.getReferenceElement();
		if(element != null){
			element.setName(name);
		}
		support.firePropertyChange(TransformDataTreeViewer.NODE_PROPERTY_EVENT, oldName, this.name);
	}

	public List<AbstractXMLObject> getXMLNodeChildren() {
		return children;
	}

	public void setChildren(List<AbstractXMLObject> children) {
		this.children = children;
	}
	
	public Element getReferenceElement() {
		return referenceElement;
	}

	public void setReferenceElement(Element referenceElement) {
		this.referenceElement = referenceElement;
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

	public void addNodePropetyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removeNodePropetyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	public List<IXMLStructuredObject> getChildren() {
		List children = getXMLNodeChildren();
		return children;
	}

	public Object getID() {
		return getName();
	}

	public String getNodeName() {
		return getName();
	}

	public boolean isRootNode() {
		return false;
	}
	
	
}
