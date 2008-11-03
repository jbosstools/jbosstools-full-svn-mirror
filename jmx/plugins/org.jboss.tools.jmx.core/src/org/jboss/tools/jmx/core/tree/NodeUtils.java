/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.jboss.tools.jmx.core.tree;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;


import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.jmx.core.IConnectionWrapper;
import org.jboss.tools.jmx.core.IJMXRunnable;
import org.jboss.tools.jmx.core.JMXException;

public class NodeUtils {

    public static ObjectNameNode findObjectNameNode(Node node,
            ObjectName objectName) {
        Assert.isNotNull(node);

        if (node instanceof ObjectNameNode) {
            ObjectNameNode onNode = (ObjectNameNode) node;
            if (onNode.getObjectName().equals(objectName)) {
                return onNode;
            }
        }
        Node[] children = node.getChildren();
        for (int i = 0; i < children.length; i++) {
            Node child = children[i];
            Node found = findObjectNameNode(child, objectName);
            if (found != null) {
                return (ObjectNameNode) found;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Root createObjectNameTree(final IConnectionWrapper connectionWrapper)
            throws CoreException {
    	final Root[] _root = new Root[1];
    	connectionWrapper.run(new IJMXRunnable() {
			public void run(MBeanServerConnection connection) throws JMXException {
				try {
			        Set beanInfo = connection.queryNames(new ObjectName("*:*"), null); //$NON-NLS-1$
			        _root[0] = NodeBuilder.createRoot(connectionWrapper);
			        Iterator iter = beanInfo.iterator();
			        while (iter.hasNext()) {
			            ObjectName on = (ObjectName) iter.next();
			            NodeBuilder.addToTree(_root[0], on);
			        }
				} catch( IOException ioe ) {
					// TODO throw coreexception
				} catch( MalformedObjectNameException mone) {
					// TODO throw coreexception
				}
			}
    	});
        return _root[0];
    }
}
