/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class FieldMarkerComposite extends Canvas implements IFieldMarker, PaintListener {

	private Image errorImage = null;

	private Image waringImage = null;

//	private Image informationImage = null;

	private int type = TYPE_NONE;

	public FieldMarkerComposite(Composite parent, int style) {
		super(parent, style);
		errorImage = SmooksConfigurationActivator.getDefault().getImageRegistry().get(GraphicsConstants.IMAGE_OVR_ERROR);
		waringImage = SmooksConfigurationActivator.getDefault().getImageRegistry().get(GraphicsConstants.IMAGE_OVR_WARING);
		this.addPaintListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.IFieldMarker#setMarkerType()
	 */
	public void setMarkerType(int type) {
		this.type = type;
		this.redraw();
	}
	
	public int getMarkerType(){
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.IFieldMarker#setMessage()
	 */
	public void setMessage(String message) {
		this.setToolTipText(message);
	}
	
	public String getMessage(){
		return getToolTipText();
	}

	public void clean() {
		setMarkerType(TYPE_NONE);
		this.setToolTipText(null);
		this.redraw();
	}

	public void paintControl(PaintEvent e) {
		GC gc = e.gc;
		gc.fillRectangle(getBounds());
		if (type == IFieldMarker.TYPE_ERROR) {
			gc.drawImage(errorImage, 0, 0);
		}
		if (type == IFieldMarker.TYPE_WARINING) {
			gc.drawImage(waringImage, 0, 0);
		}
	}

}
