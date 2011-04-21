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

/**
 * @author Dart Peng
 * @Date Jul 26, 2008
 */
public class DocumentObject extends AbstractXMLObject {
	TagObject rootTag;

	public TagObject getRootTag() {
		return rootTag;
	}

	public void setRootTag(TagObject rootTag) {
		this.rootTag = rootTag;
		if(rootTag != null) rootTag.setParent(this);
	}
	
	public String toString(){
		if(rootTag != null)
		return "Document : " + getName() + "\n" + getRootTag().toString();
		
		return "Docuement Object :  " +getName();
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.xml.model.AbstractXMLObject#getParent()
	 */
	@Override
	public AbstractXMLObject getParent() {
		return null;
	}
	
	
	
}
