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
package org.jboss.tools.vpe.editor.toolbar;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

/*
 * @author Erick
 */
public class ArrowRight extends Canvas{

	private Image image;

	public ArrowRight(Composite parent, int style) {
		super(parent, style);

		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				ArrowRight.this.paintControl(e);
			}
		});
	}

	void paintControl(PaintEvent e) {
	     if (image != null) {
	         e.gc.drawImage(image, image.getBounds().width + 5, 1); 
	     }
	 }

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		redraw();
	}

	public Point computeSize(int wHint, int hHint, boolean changed) {
		int width = 0, height = 0;
		if (image != null) {
			Rectangle bounds = image.getBounds();
		    width = bounds.width;
		    height = bounds.height;
		}

		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;          
		return new Point(width+2, height); 
	}
}