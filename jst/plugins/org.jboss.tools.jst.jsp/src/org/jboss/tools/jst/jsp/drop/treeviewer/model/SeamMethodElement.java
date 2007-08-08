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
package org.jboss.tools.jst.jsp.drop.treeviewer.model;

/**
 * 
 * @author Viacheslav Kabanovich	
 */
public class SeamMethodElement extends SeamElement {

	public SeamMethodElement(String name, ModelElement parent) {
		super(name, parent);
	}
	
	public String getValue() {
		return "#{" + getFullName() + "}";
	}

	protected String getFullName() {
		return parent.getFullName() + "." + name + "()";
	}

	protected String getComparedValue() {
		return "#{" + getFullName();
	}

}
