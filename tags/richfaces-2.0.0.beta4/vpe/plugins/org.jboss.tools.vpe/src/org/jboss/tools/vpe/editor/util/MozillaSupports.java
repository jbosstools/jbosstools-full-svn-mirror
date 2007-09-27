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
package org.jboss.tools.vpe.editor.util;

import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsID;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMNode;
import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsISupports;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class MozillaSupports {

	public static void addRef(Object obj) {
		if (obj instanceof nsISupports) {
			((nsISupports) obj).AddRef();
		}
	}

	public static void release(Object obj) {
		if (obj instanceof nsISupports) {
			((nsISupports) obj).Release();
		}
	}

	public static void release(Object obj0, Object obj1) {
		release(obj0);
		release(obj1);
	}

	public static void release(Object obj0, Object obj1, Object obj2) {
		release(obj0);
		release(obj1);
		release(obj2);
	}
	
	public static int getRefCount(Object obj) {
		if (obj instanceof nsISupports) {
			return ((nsISupports) obj).getRefCount();
		}
		return 0;
	}

	public static int getAddress(Object obj) {
		if (obj instanceof nsISupports) {
			return ((nsISupports) obj).getAddress();
		}
		return 0;
	}

	public static int getChildCount(Node node) {
		int count = 0;
		NodeList children = node.getChildNodes();
		if (children != null) {
			count = children.getLength();
			release(children);
		}
		return count;
	}

	public static Node getChildNode(Node node, int index) {
		Node child = null;
		NodeList children = node.getChildNodes();
		if (children != null && index >= 0 && index < children.getLength()) {
			child = children.item(index);
			release(children);
		}
		return child;
	}
	
	public static int getOffset(Node node) {
		return ((nsIDOMNode)node).getOffset();
	}

	public static int queryInterface(Object obj, nsID uuid) {
		if (obj instanceof nsISupports) {
			return ((nsISupports) obj).queryInterface(uuid);
		}
		return 0;
	}
}
