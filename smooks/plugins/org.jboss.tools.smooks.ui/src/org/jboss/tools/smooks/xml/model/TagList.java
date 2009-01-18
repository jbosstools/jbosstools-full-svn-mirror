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

import org.jboss.tools.smooks.ui.IXMLStructuredObject;
import org.jboss.tools.smooks.ui.editors.TransformDataTreeViewer;

/**
 * @author Dart Peng
 * @Date Jul 26, 2008
 */
public class TagList extends AbstractXMLObject {
	List<TagObject> rootTagList = new ArrayList<TagObject>();
	
	private Object id = new Object();

//	public TagObject getRootTag() {
//		return rootTag;
//	}
	
	public List<TagObject> getRootTagList() {
		return rootTagList;
	}
	
	

	@Override
	public List<IXMLStructuredObject> getChildren() {
		List tags = getRootTagList();
		return tags;
	}



	@Override
	public Object getID() {
		return id;
	}



	@Override
	public String getName() {
		return "";
	}



	public void removeRootTag(TagObject rootTag){
		rootTagList.remove(rootTag);
		if(rootTag != null) rootTag.setParent(null);
		support.firePropertyChange(TransformDataTreeViewer.REMOVE_CHILDREN_EVENT, rootTag, null);
	}

	public void addRootTag(TagObject rootTag) {
		rootTagList.add(rootTag);
		if(rootTag != null) rootTag.setParent(this);
		support.firePropertyChange(TransformDataTreeViewer.ADD_CHILDREN_EVENT, null, rootTag);
	}
	
	public String toString(){
//		if(rootTag != null)
//		return "Document : " + getName() + "\n" + getRootTag().toString();
		
		return "Docuement Object :  " +getName();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.xml.model.AbstractXMLObject#getParent()
	 */
	@Override
	public AbstractXMLObject getParent() {
		return null;
	}



	@Override
	public boolean isRootNode() {
		return true;
	}
	
	
	
	
	
}
