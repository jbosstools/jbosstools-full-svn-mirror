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
import org.eclipse.core.resources.IFile;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.ElService;
import org.jboss.tools.vpe.editor.util.ResourceUtil;
import org.w3c.dom.Attr;
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
		Object result = method.invoke(node, args);
		if(result instanceof String) {
			String processedStr = (String) result;
			result = replaceEL(processedStr);
		} else if(result instanceof Attr) {
			result = VpeProxyUtil.createProxyForELExpressionNode(pageContext,
						(Node)result);
		}
 		return result;
	}
	
	protected String replaceEL(String toReplace) {
		String result = toReplace;
       
        //fix for JBIDE-3030
		result = ResourceUtil.getBundleValue(pageContext, toReplace);
        if(pageContext.getVisualBuilder().getCurrentIncludeInfo()==null) {
        	return result;
        }
        final IFile file = pageContext.getVisualBuilder().getCurrentIncludeInfo().getFile();
        return  ElService.getInstance().replaceEl(file, result);
	}

}
