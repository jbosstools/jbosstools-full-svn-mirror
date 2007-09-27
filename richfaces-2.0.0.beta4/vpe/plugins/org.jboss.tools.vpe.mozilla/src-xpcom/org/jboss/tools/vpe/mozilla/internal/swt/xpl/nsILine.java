/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.vpe.mozilla.internal.swt.xpl;

import org.eclipse.swt.graphics.Rectangle;

public class nsILine {
	private nsIFrame firstFrame;
	private int numFrames;
	private Rectangle bounds;
	private int flags;
	
	nsILine(nsIFrame firstFrame, int numFrames, Rectangle bounds, int flags) {
		this.firstFrame = firstFrame;
		this.numFrames = numFrames;
		this.bounds = bounds;
		this.flags = flags;
	}

	public int Release() {
		return 0;
	}
}
