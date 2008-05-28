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
package org.jboss.tools.seam.ui.pages.editor.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.gef.GEFGraphicalViewer;
import org.jboss.tools.seam.ui.pages.editor.ecore.pages.PgException;
import org.jboss.tools.seam.ui.pages.editor.edit.ExceptionEditPart;
import org.jboss.tools.seam.ui.pages.editor.figures.xpl.FixedConnectionAnchor;

public class ExceptionFigure extends NodeFigure implements HandleBounds {
	private static final Dimension SIZE = new Dimension(56, 100);

	private Image icon = null;

	public PgException exc;

	ExceptionEditPart editPart;

	public void setEditPart(ExceptionEditPart part) {
		editPart = part;
	}

	public void setConstraint(IFigure child, Object constraint) {
		super.setConstraint(child, constraint);
	}

	public void setIcon(Image i) {
		//icon = PrintIconHelper.getPrintImage(i);
	}


	public void init(int number) {
		FixedConnectionAnchor c;
		if (number == 0)
			number = 1;
		for (int i = 0; i < number; i++) {
			c = new FixedConnectionAnchor(this);
			c.offsetV = 32 + LINK_HEIGHT * i;
			c.leftToRight = false;
			connectionAnchors.put((i + 1) + "_OUT", c);
			outputConnectionAnchors.addElement(c);
		}
	}

	public void addConnectionAnchor(int number) {
		FixedConnectionAnchor c;
		if (number == 1)
			return;
		c = new FixedConnectionAnchor(this);
		c.offsetV = 32 + LINK_HEIGHT * (number - 1);
		// c.offsetH = -1;
		c.leftToRight = false;
		connectionAnchors.put(number + "_OUT", c);
		outputConnectionAnchors.addElement(c);
	}

	public void removeConnectionAnchor() {
		if (outputConnectionAnchors.size() == 1)
			return;
		outputConnectionAnchors.remove(outputConnectionAnchors.size() - 1);
	}

	public void removeAllConnectionAnchor() {
		outputConnectionAnchors.removeAllElements();
	}

	public ExceptionFigure(PgException group) {
		this.exc = group;

		setOpaque(false);
		setLayoutManager(new XYLayout());

		setBorder(new GroupBorder(blackColor));

		if (group != null) {
			FixedConnectionAnchor c;
			c = new FixedConnectionAnchor(this);
			c.offsetV = 10;
			//c.offsetH = -1;
			connectionAnchors.put("1_IN", c);
			inputConnectionAnchors.addElement(c);

			c = new FixedConnectionAnchor(this);
			c.offsetV = 10;
			c.leftToRight = false;
			connectionAnchors.put("1_OUT", c);
			outputConnectionAnchors.addElement(c);
		}
	}

	/**
	 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
	 */
	public Rectangle getHandleBounds() {
		return getBounds().getCropped(new Insets(0, 0, 0, 0));
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return SIZE;
	}

	int width, height;

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		g.translate(r.getLocation());

		int start = 0;
		

		g.setBackgroundColor(whiteColor);

		g.fillRectangle(start + 1, 1, 22, 19);

		if (icon != null)
			g.drawImage(icon, start + 4, 2);
		

		if (exc != null /*&& group.isConfirmed()*/) {
			g.setBackgroundColor(exceptionBackgroundColor);
			g.setForegroundColor(exceptionForegroundColor);
		} else {
			g.setBackgroundColor(lightGrayColor);
		}

	}

	class GroupBorder extends LineBorder {
		public GroupBorder(Color color) {
			super(color);
		}

		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			Rectangle r = getPaintRectangle(figure, insets).getCopy();
			graphics.translate(r.getLocation());
			int width = r.width - 1;
			int height = r.height - 1;
			
			if (exc != null)
				graphics.setForegroundColor(blackColor);
			else
				graphics.setForegroundColor(darkGrayColor);

			graphics.drawLine(1, 0, width-1, 0);
			graphics.drawLine(0, 1, 0, height - 1);
			graphics.drawLine(1, height, width-1, height);
			graphics.drawLine(width, 1, width, height - 1);
			graphics.drawLine(23 , 0, 23, height);
			

	}

	public void mouseDoubleClicked(MouseEvent me) {
	}

	public void mousePressed(MouseEvent me) {
		if (me.button == 3) {
			((GEFGraphicalViewer) editPart.getViewer()).setNoDeselect();
			editPart.getViewer().select(editPart);
		}
	}


}
}