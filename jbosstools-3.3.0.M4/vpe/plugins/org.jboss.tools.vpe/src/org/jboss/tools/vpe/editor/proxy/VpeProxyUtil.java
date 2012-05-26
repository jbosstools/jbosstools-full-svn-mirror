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

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Utill Class created for creating proxis for vpe and manipulation them
 * 
 * @author mareshkau
 */
@SuppressWarnings("restriction")
public class VpeProxyUtil {

	/**
	 * Create proxy for source nodes. This function should be called for nodes,
	 * in which used el expressions which should be replaced
	 * 
	 * @param sourceNode
	 * @return proxy for node
	 */
	public static Node createProxyForELExpressionNode(
			VpePageContext pageContext, Node sourceNode) {

		Node proxy = null;
		List<Class<?>> interfaceses = new ArrayList<Class<?>>();
		interfaceses.add(Node.class);
		boolean isDOMImpl = false;
		if (sourceNode instanceof IDOMElement) {
			interfaceses.add(IDOMElement.class);
			isDOMImpl = true;
		} else if (sourceNode instanceof IDOMAttr) {
			interfaceses.add(IDOMAttr.class);
			isDOMImpl = true;
		}
		if (isDOMImpl) {
			proxy = (Node) Proxy.newProxyInstance(IDOMNode.class
					.getClassLoader(), interfaceses.toArray(new Class[0]),
					new VpeNodeInvocationHandler(pageContext, sourceNode));
		} else {
			if (sourceNode instanceof Element) {
				interfaceses.add(Element.class);
			} else if (sourceNode instanceof Attr) {
				interfaceses.add(Attr.class);
			}
			proxy = (Node) Proxy.newProxyInstance(Node.class.getClassLoader(),
					interfaceses.toArray(new Class[0]),
					new VpeNodeInvocationHandler(pageContext, sourceNode));
		}
		return proxy;
	}

	public static NamedNodeMap createProxyForNamedNodeMap(
			VpePageContext pageContext, NamedNodeMap namedNodeMap) {
		NamedNodeMap proxy = (NamedNodeMap) Proxy
				.newProxyInstance(NamedNodeMap.class.getClassLoader(),
						new Class[] { NamedNodeMap.class },
						new VpeNamedNodeMapInvocationHandler(pageContext,
								namedNodeMap));

		return proxy;
	}
}
