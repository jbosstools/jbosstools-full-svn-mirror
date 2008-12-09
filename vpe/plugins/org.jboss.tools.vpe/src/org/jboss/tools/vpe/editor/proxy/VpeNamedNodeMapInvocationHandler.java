/*******************************************************************************
 * Copyright (c) 2007-2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author mareshkau
 *
 */
public class VpeNamedNodeMapInvocationHandler implements InvocationHandler {

	private NamedNodeMap namedNodeMap;
	private VpePageContext pageContext;

	/**
	 * @param node
	 */
	public VpeNamedNodeMapInvocationHandler(VpePageContext pageContext,NamedNodeMap namedNodeMap) {
		this.namedNodeMap  = namedNodeMap;
		this.pageContext = pageContext;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = method.invoke(namedNodeMap, args);
		if(result instanceof Node) {
			return VpeProxyUtil.createProxyForELExpressionNode(pageContext, (Node)result);
		}
		return result;
	}
	

}
