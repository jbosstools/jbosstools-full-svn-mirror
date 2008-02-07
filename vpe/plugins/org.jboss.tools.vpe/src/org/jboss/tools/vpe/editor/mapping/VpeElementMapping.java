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
package org.jboss.tools.vpe.editor.mapping;

import java.util.Set;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.template.expression.VpeExpressionBuilder;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Element;

public class VpeElementMapping extends VpeNodeMapping {
	private VpeTemplate template;
	private Set ifDependencySet;
//	private Map xmlnsMap;
	private Object data;
	private nsIDOMElement border;
	
	public VpeElementMapping(Element sourceElement, nsIDOMElement visualElement, nsIDOMElement border, VpeTemplate template, Set ifDependencySet, Object data) {
		super(sourceElement, visualElement);
		this.template = template;
		if (ifDependencySet != null && ifDependencySet.size() > 0) {
			this.ifDependencySet = ifDependencySet;
		}
		this.data = data;
		this.border = border;
	}
	
	public nsIDOMElement getVisualElement() {
		return (nsIDOMElement) getVisualNode();
	}
	
	public VpeTemplate getTemplate() {
		return template;
	}
	
//	public Map getXmlnsMap() {
//		return xmlnsMap;
//	}
//	
//	public void setXmlnsMap(Map xmlnsMap) {
//		this.xmlnsMap = xmlnsMap;
//	}
	
	public Object getData() {
		return data;
	}
	
	public boolean isIfDependencyFromAttribute(String attrName) {
		if (ifDependencySet == null) {
			return false;
		}
		String signature = VpeExpressionBuilder.attrSignature(attrName, template.isCaseSensitive());
		if (ifDependencySet.contains(signature)) {
			return true;
		}
		return ifDependencySet.contains(VpeExpressionBuilder.SIGNATURE_ANY_ATTR);
	}
	
	public nsIDOMElement getBorder() {
		return border;
	}
	
	public boolean isBorder(nsIDOMNode border) {
		if (this.border == null) return false;
		if (this.border.equals(border)) return true;
		
		nsIDOMNode element = border;
		
		while(true){
			if(this.border.equals(element)) return true;
			if(getVisualNode().equals(element)) return false;
			
			element = element.getParentNode();
			if(element == null) break;
		}
		return false;
	}
}
