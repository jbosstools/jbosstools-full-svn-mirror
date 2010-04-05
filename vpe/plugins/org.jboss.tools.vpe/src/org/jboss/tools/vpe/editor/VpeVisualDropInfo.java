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
package org.jboss.tools.vpe.editor;

import org.mozilla.interfaces.nsIDOMNode;

public class VpeVisualDropInfo {
	private nsIDOMNode dropContainer;
	private long dropOffset;

	public VpeVisualDropInfo(nsIDOMNode dropContainer, long dropOffset) {
		this.dropContainer = dropContainer;
		this.dropOffset = dropOffset;
	}
	
	public nsIDOMNode getDropContainer() {
		return dropContainer;
	}
	
	public long getDropOffset() {
		return dropOffset;
	}
	public void setDropOffset(long dropOffset) {
		this.dropOffset = dropOffset;
	}

	public void release() {
			dropContainer = null;
	}
}
