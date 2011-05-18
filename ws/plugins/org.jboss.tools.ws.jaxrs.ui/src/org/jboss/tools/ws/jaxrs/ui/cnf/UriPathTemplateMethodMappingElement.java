/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Xavier Coulon - Initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.ws.jaxrs.ui.cnf;

import java.util.Stack;

import org.jboss.tools.ws.jaxrs.core.metamodel.ResourceMethod;

public class UriPathTemplateMethodMappingElement {

	private final Stack<ResourceMethod> resourceMethods;

	public UriPathTemplateMethodMappingElement(Stack<ResourceMethod> resourceMethods) {
		super();
		this.resourceMethods = resourceMethods;
	}


	public ResourceMethod getLastMethod() {
		return resourceMethods.lastElement();
	}
	

}
