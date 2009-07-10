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
package org.jboss.tools.vpe.editor.template.resize;

public class TemplateResizeData {
	private String sizeAttribute = null;
	private String positionAttribute=null;
	private String tagXpath=null;
	private String expression=null;
	private boolean isPositionEnabled=true;
	
	private TemplateResizeData next=null;
	
	@SuppressWarnings("nls")
	public TemplateResizeData(String attribute, String position, String xPath, String test, String enabledString){
		sizeAttribute = attribute;
		if("".equals(sizeAttribute)) sizeAttribute = null;
		tagXpath = xPath;
		if("".equals(tagXpath)) tagXpath = null;
		expression = test;
		if("".equals(expression)) expression = null;
		positionAttribute = position;
		if("".equals(positionAttribute)) positionAttribute = null;
		if(enabledString != null && "yes".equalsIgnoreCase(enabledString))isPositionEnabled = false;
	}
	
	public String getSizeAttribute(){
		return sizeAttribute;
	}

	public String getPositionAttribute(){
		return positionAttribute;
	}

	public boolean isAbsolutePositionEnabled(){
		return isPositionEnabled;
	}
	
	public String getTagXpath(){
		return tagXpath;
	}
	
	public String getExpression(){
		return expression;
	}
	
	public void setNext(TemplateResizeData next){
		this.next = next;
	}
	
	public TemplateResizeData getNext(){
		return next;
	}
}
