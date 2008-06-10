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

import org.eclipse.draw2d.AnchorListener;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.graphics.Color;
import org.jboss.tools.seam.ui.pages.editor.ecore.pages.Link;
import org.jboss.tools.seam.ui.pages.editor.edit.LinkEditPart;


public class ConnectionFigure extends PolylineConnection implements Connection, AnchorListener{
	private boolean manual = false;
	private LinkEditPart editPart = null;
	private Link link = null;
	private int oldStartPointX = 0, oldStartPointY = 0, oldEndPointX = 0,
			oldEndPointY = 0;

	public ConnectionFigure(LinkEditPart part) {
		super();
		editPart = part;
		link = part.getLinkModel();
	}

	public Link getLinkModel() {
		return link;
	}

	public ConnectionFigure() {
		super();
	}

	public void refreshFont() {
		if (getChildren().size() > 0 && getChildren().get(0) instanceof Label) {
			Label label = (Label) getChildren().get(0);
//			label.setFont(editPart.getLinkModel().getJSFModel().getOptions()
//					.getLinkPathFont());
			label.setSize(label.getPreferredSize());
		}
	}

	public void setOldPoints(Point start, Point end) {
		oldStartPointX = start.x;
		oldStartPointY = start.y;
		oldEndPointX = end.x;
		oldEndPointY = end.y;
	}

	public Point getOldStartPoint() {
		return new Point(oldStartPointX, oldStartPointY);
	}

	public Point getOldEndPoint() {
		return new Point(oldEndPointX, oldEndPointY);
	}

	public void setManual(boolean flag) {
		manual = flag;
	}

	public boolean isManual() {
		return manual;
	}

	public void save() {
		if (editPart != null)
			editPart.save();
	}

	public void clear() {
		if (editPart != null)
			editPart.clear();
	}

	PointList points;
	Point point;
	Point beg = new Point(0, 0), end = new Point(0, 0);
	Point corner = new Point(0, 0);
	boolean horiz;
	private boolean selected;

	@Override
	public void setForegroundColor(Color fg) {
		// TODO Auto-generated method stub
		super.setForegroundColor(fg);
	}

	protected void outlineShape(Graphics g) {

		if(selected) {
			g.setForegroundColor(FigureFactory.selectedColor);
		} else {
			g.setForegroundColor(getForegroundColor());
		}
		points = getPoints();
		point = points.getPoint(0);
		beg.x = point.x;
		beg.y = point.y;

		if (points.getFirstPoint().y == points.getLastPoint().y) {
			super.outlineShape(g);
			return;
		}

		for (int i = 1; i < points.size(); i++) {
			point = points.getPoint(i);
			end.x = point.x;
			end.y = point.y;

			if (beg.y == end.y)
				horiz = true;
			else
				horiz = false;

			if (i != 1) {
				if (horiz) {
					if (end.x > beg.x) {
						corner.x = beg.x + 1;
						beg.x += 2;
					} else {
						corner.x = beg.x - 1;
						beg.x -= 2;
					}
				} else {
					if (end.y > beg.y) {
						corner.y = beg.y + 1;
						beg.y += 2;
					} else {
						corner.y = beg.y - 1;
						beg.y -= 2;
					}
				}
			}
			if (corner.x != 0)
				g.drawLine(corner, corner);
			corner.x = 0;

			if (i != points.size() - 1) {
				if (horiz) {
					if (end.x > beg.x) {
						corner.x = end.x - 1;
						end.x -= 2;

					} else {
						corner.x = end.x + 1;
						end.x += 2;
					}
				} else {
					if (end.y > beg.y) {
						corner.y = end.y - 1;
						end.y -= 2;
					} else {
						corner.y = end.y + 1;
						end.y += 2;
					}
				}
			}

			g.drawLine(beg, end);
			point = points.getPoint(i);
			beg.x = point.x;
			beg.y = point.y;
		}
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}
}
