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
import org.jboss.tools.vpe.editor.util.ElService;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author mareshkau
 *
 */
public class VpeNodeInvocationHandler implements InvocationHandler {

	/**
	 * Node for which we process events
	 */
	private Node node;
	
	private VpePageContext pageContext; 
		
	/**
	 * @param node
	 */
	public VpeNodeInvocationHandler(VpePageContext pageContext,Node node) {
		this.node = node;
		this.pageContext = pageContext;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = method.invoke(this.node, args);
		if(result instanceof String) {
			String processedStr = (String) result;
			result = replaceEL(processedStr);
		} else if(result instanceof Attr) {
			result = VpeProxyUtil.createProxyForELExpressionNode(this.pageContext,
						(Node)result);
		} else if(result instanceof NamedNodeMap) {
			result = VpeProxyUtil.createProxyForNamedNodeMap(this.pageContext, (NamedNodeMap)result);
		}
 		return result;
	}
	/**
	 * Replaced string with el value
	 * @param toReplace
	 * @return
	 */
	private String replaceEL(String toReplace) {
		
        return  ElService.getInstance().replaceElAndResources(this.pageContext, toReplace);
	}

}
