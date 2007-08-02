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
package org.jboss.tools.vpe.editor.mozilla;

import org.jboss.tools.vpe.mozilla.internal.swt.xpl.nsIDOMNode;
import org.w3c.dom.Node;


public class MozillaDropInfo {
	private boolean canDrop;
	private Node caretParent;
	private int caretOffset;
	
	public MozillaDropInfo(boolean canDrop, Node caretParent, int caretOffset) {
		this.canDrop = canDrop;
		this.caretParent = caretParent;
		this.caretOffset = caretOffset;
	}

	public boolean canDrop() {
		return canDrop;
	}

	public Node getCaretParent() {
		return caretParent;
	}

	public int getCaretOffset() {
		return caretOffset;
	}
}
