/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Exadel, Inc.
 *     Red Hat, Inc. 
 *******************************************************************************/
package org.jboss.tools.vpe.editor.xpl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

public class SashForm extends Composite {
	private static final Color buttonDarker =
			Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	private static final Color buttonLightest = 
			Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);

	public int SASH_WIDTH = 7;

	private static final int DRAG_MINIMUM = 20;
	
	private int orientation = SWT.HORIZONTAL;
	private Sash[] sashes = new Sash[0];
	private Control[] controls = new Control[0];
	private Control maxControl = null;
	private Listener sashListener;
	private final static String LAYOUT_RATIO = "layout ratio"; //$NON-NLS-1$
	private final static String WEIGHTS = "weights"; //$NON-NLS-1$
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	public SashForm(Composite parent, int style) {
		super(parent, checkStyle(style));
		if ((style & SWT.VERTICAL) != 0){
			orientation = SWT.VERTICAL;
		}
		
		this.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				layout(true);
			}
		});
		
		sashListener = new Listener() {
			public void handleEvent(Event e) {
				onDragSash(e);
			}
		};
	}

	public void addWeightsChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	private void fireWeightsPropertyChange() {
		listeners.firePropertyChange(WEIGHTS, 1, 0);
	}

	public void removeWeightsChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	private static int checkStyle (int style) {
		int mask = SWT.BORDER;
		return style & mask;
	}
	
	public int getOrientation() {
		return orientation;
	}

	public Control getMaximizedControl(){
		return this.maxControl;
	}
	
	public int[] getWeights() {
		checkWidget();
		Control[] cArray = getControls(false);
		float[] ratios = new float[cArray.length];
		for (int i = 0; i < cArray.length; i++) {
			Float ratio = (Float)cArray[i].getData(LAYOUT_RATIO);
			if (ratio != null) {
				ratios[i] = ratio.floatValue();
			} else {
				ratios[i] = (float)0.2;
			}
		}
		
		int[] weights = new int[cArray.length];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = (int)(ratios[i] * 1000);
		}
		return weights;
	}

	private Control[] getControls(boolean onlyVisible) {
		Control[] children = getChildren();
		Control[] controls = new Control[0];
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof Sash) continue;
			if (onlyVisible && !children[i].getVisible()) continue;
	
			Control[] newControls = new Control[controls.length + 1];
			System.arraycopy(controls, 0, newControls, 0, controls.length);
			newControls[controls.length] = children[i];
			controls = newControls;
		}
		return controls;
	}

	public void layout(boolean changed) {
		checkWidget();
		Rectangle area = getClientArea();
		if (area.width == 0 || area.height == 0) return;
		
		Control[] newControls = getControls(true);
		if (controls.length == 0 && newControls.length == 0) return;
		controls = newControls;
		
		if (maxControl != null && !maxControl.isDisposed()) {
			for (int i= 0; i < controls.length; i++){
				if (controls[i] != maxControl) {
					controls[i].setBounds(-200, -200, 0, 0);
				} else {
					controls[i].setBounds(area);
				}
			}
			return;
		}
		
		// keep just the right number of sashes
		if (sashes.length < controls.length - 1) {
			Sash[] newSashes = new Sash[controls.length - 1];
			System.arraycopy(sashes, 0, newSashes, 0, sashes.length);
			int sashOrientation = (orientation == SWT.HORIZONTAL) ? SWT.VERTICAL : SWT.HORIZONTAL;
			for (int i = sashes.length; i < newSashes.length; i++) {
				newSashes[i] = new Sash(this, sashOrientation);
				newSashes[i].addListener(SWT.Paint, new SashPainter());
				newSashes[i].addListener(SWT.Selection, sashListener);
			}
			sashes = newSashes;
		}
		if (sashes.length > controls.length - 1) {
			if (controls.length == 0) {
				for (int i = 0; i < sashes.length; i++) {
					sashes[i].dispose();
				}
				sashes = new Sash[0];
			} else {
				Sash[] newSashes = new Sash[controls.length - 1];
				System.arraycopy(sashes, 0, newSashes, 0, newSashes.length);
				for (int i = controls.length - 1; i < sashes.length; i++) {
					sashes[i].dispose();
				}
				sashes = newSashes;
			}
		}
		
		if (controls.length == 0) return;
		
		// get the ratios
		float[] ratios = new float[controls.length];
		float total = 0;
		for (int i = 0; i < controls.length; i++) {
			Float ratio = (Float)controls[i].getData(LAYOUT_RATIO);
			if (ratio != null) {
				ratios[i] = ratio.floatValue();
			} else {
				ratios[i] = (float)0.2;
			}
			total += ratios[i];
		}
		
		if (orientation == SWT.HORIZONTAL) {
			total += (float)sashes.length * ((float)SASH_WIDTH / (float)area.width);
		} else {
			total += (float)sashes.length * ((float)SASH_WIDTH / (float)area.height);
		}
		
		if (orientation == SWT.HORIZONTAL) {
			int width = (int)((ratios[0] / total) * (float)area.width);
			int x = area.x;
			controls[0].setBounds(x, area.y, width, area.height);
			x += width;
			for (int i = 1; i < controls.length - 1; i++) {
				sashes[i - 1].setBounds(x, area.y, SASH_WIDTH, area.height);
				x += SASH_WIDTH;
				width = (int)((ratios[i] / total) * (float)area.width);
				controls[i].setBounds(x, area.y, width, area.height);
				x += width;
			}
			if (controls.length > 1) {
				sashes[sashes.length - 1].setBounds(x, area.y, SASH_WIDTH, area.height);
				x += SASH_WIDTH;
				width = area.width - x;
				controls[controls.length - 1].setBounds(x, area.y, width, area.height);
			}
		} else {
			int height = (int)((ratios[0] / total) * (float)area.height);
			int y = area.y;
			controls[0].setBounds(area.x, y, area.width, height);
			y += height;
			for (int i = 1; i < controls.length - 1; i++) {
				sashes[i - 1].setBounds(area.x, y, area.width, SASH_WIDTH);
				y += SASH_WIDTH;
				height = (int)((ratios[i] / total) * (float)area.height);
				controls[i].setBounds(area.x, y, area.width, height);
				y += height;
			}
			if (controls.length > 1) {
				sashes[sashes.length - 1].setBounds(area.x, y, area.width, SASH_WIDTH);
				y += SASH_WIDTH;
				height = area.height - y;
				controls[controls.length - 1].setBounds(area.x, y, area.width, height);
			}
	
		}
	}

	private void onDragSash(Event event) {
		if (event.detail == SWT.DRAG) {
			// constrain feedback
			Rectangle area = getClientArea();
			if (orientation == SWT.HORIZONTAL) {
				event.x = Math.min(Math.max(DRAG_MINIMUM, event.x), area.width - DRAG_MINIMUM - SASH_WIDTH);
			} else {
				event.y = Math.min(Math.max(DRAG_MINIMUM, event.y), area.height - DRAG_MINIMUM - SASH_WIDTH);
			}
			return;
		}
	
		Sash sash = (Sash)event.widget;
		int sashIndex = -1;
		for (int i= 0; i < sashes.length; i++) {
			if (sashes[i] == sash) {
				sashIndex = i;
				break;
			}
		}
		if (sashIndex == -1) return;
	
		Control c1 = controls[sashIndex];
		Control c2 = controls[sashIndex + 1];
		Rectangle b1 = c1.getBounds();
		Rectangle b2 = c2.getBounds();
		
		Rectangle sashBounds = sash.getBounds();
		Rectangle area = getClientArea();
		if (orientation == SWT.HORIZONTAL) {
			int shift = event.x - sashBounds.x;
			if (shift == 0) {
				return;
			}
			b1.width += shift;
			b2.x += shift;
			b2.width -= shift;
			if (b1.width < DRAG_MINIMUM || b2.width < DRAG_MINIMUM) {
				return;
			}
			c1.setData(LAYOUT_RATIO, new Float((float)b1.width / (float)area.width));
			c2.setData(LAYOUT_RATIO, new Float((float)b2.width / (float)area.width));
		} else {
			int shift = event.y - sashBounds.y;
			if (shift == 0) {
				return;
			}
			b1.height += shift;
			b2.y += shift;
			b2.height -= shift;
			if (b1.height < DRAG_MINIMUM || b2.height < DRAG_MINIMUM) {
				return;
			}
			c1.setData(LAYOUT_RATIO, new Float((float)b1.height / (float)area.height));
			c2.setData(LAYOUT_RATIO, new Float((float)b2.height / (float)area.height));
		}
		
		c1.setBounds(b1);
		sash.setBounds(event.x, event.y, event.width, event.height);
		c2.setBounds(b2);

		fireWeightsPropertyChange();
	}

	public void setOrientation(int orientation) {
		checkWidget();
		if (this.orientation == orientation) return;
		if (orientation != SWT.HORIZONTAL && orientation != SWT.VERTICAL) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.orientation = orientation;
		
		int sashOrientation = (orientation == SWT.HORIZONTAL) ? SWT.VERTICAL : SWT.HORIZONTAL;
		for (int i = 0; i < sashes.length; i++) {
			sashes[i].dispose();
			sashes[i] = new Sash(this, sashOrientation);
			sashes[i].addListener(SWT.Selection, sashListener);
		}
		layout();
	}
	public void setLayout (Layout layout) {
		checkWidget();
	}

	public void setMaximizedControl(Control control){
		checkWidget();
		if (control == null) {
			if (maxControl != null) {
				this.maxControl = null;
				layout();
				for (int i= 0; i < sashes.length; i++){
					sashes[i].setVisible(true);
				}
			}
			return;
		}
		
		for (int i= 0; i < sashes.length; i++){
			sashes[i].setVisible(false);
		}
		maxControl = control;
		layout();
	}
	
	public void setWeights(int[] weights) {
		checkWidget();
		Control[] cArray = getControls(false);
		if (weights == null || weights.length != cArray.length) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		
		int total = 0;
		for (int i = 0; i < weights.length; i++) {
			if (weights[i] < 0) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			total += weights[i];
		}
		if (total == 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		for (int i = 0; i < cArray.length; i++) {
			cArray[i].setData(LAYOUT_RATIO, new Float((float)weights[i] / (float)total));
		}
		
		layout();

		fireWeightsPropertyChange();
	}

	void paint(Sash sash, GC gc) {
		Point size = sash.getSize();
		if (getOrientation() == SWT.HORIZONTAL) {
			gc.setForeground(buttonDarker);
			gc.drawLine(0, 0, 0, size.y);
			gc.setForeground(buttonLightest);
			gc.drawLine(SASH_WIDTH - 1, 0, SASH_WIDTH - 1, size.y);		
		} else {
			gc.setForeground(buttonDarker);
			gc.drawLine(0, 2, size.x, 2);
			gc.setForeground(buttonLightest);
			gc.drawLine(0, SASH_WIDTH-2, size.x, SASH_WIDTH-2);			
		}
	}

	class SashPainter implements Listener {
		public void handleEvent(Event e) {
			paint((Sash)e.widget, e.gc);
		}
	}
}
