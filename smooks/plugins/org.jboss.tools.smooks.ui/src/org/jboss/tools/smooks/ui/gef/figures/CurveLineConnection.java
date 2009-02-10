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
package org.jboss.tools.smooks.ui.gef.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.smooks.ui.gef.editparts.StructuredDataConnectionEditPart;

/**
 * @author Dart Peng
 * @Date Aug 16, 2008
 */
public class CurveLineConnection extends PolylineConnection {
	private double[] fBasicCenterCurve;
	
	
	private StructuredDataConnectionEditPart hostEditPart;

	public StructuredDataConnectionEditPart getHostEditPart() {
		return hostEditPart;
	}

	public void setHostEditPart(StructuredDataConnectionEditPart hostEditPart) {
		this.hostEditPart = hostEditPart;
	}

	public CurveLineConnection(StructuredDataConnectionEditPart editPart) {
		super();
		this.setHostEditPart(editPart);
	}


	private int[] getCenterCurvePoints(int startx, int starty, int endx,
			int endy) {
		buildBaseCenterCurve(endx - startx);
		double height = endy - starty;
		height = height / 2;
		int width = Math.abs(startx - endx);

		
		if (width > fBasicCenterCurve.length) {
			width = fBasicCenterCurve.length;
//			return points;
		}
		int[] points = new int[width];
		for (int i = 0; i < width; i++) {
			points[i] = (int) (-height * fBasicCenterCurve[i] + height + starty);
		}
		return points;
	}

	private PointList getcenterCurvePointList() {
		if (this.getCenterWidth() < 0)
			return null;
		PointList list1 = this.getPoints();
		Point start = this.getStart();
		Point end = this.getEnd();
		if (start == null || end == null) {
			return null;
		}
		int[] points = getCenterCurvePoints(start.x + 8, start.y, end.x - 8, end.y);
		PointList list = new PointList();
		for (int i = 1; i < points.length; i++) {
			list.addPoint(start.x + 8 + i - 1, points[i - 1]);
		}
		list.addPoint(this.getPoints().getLastPoint());
		list.insertPoint(this.getStart(), 0);
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Polyline#outlineShape(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void outlineShape(Graphics g) {
		try {
			PointList displayPoints = getcenterCurvePointList();
			if (displayPoints != null)
				g.drawPolyline(displayPoints);
		} catch (Exception e) {
			e.printStackTrace();
			super.outlineShape(g);
		}
	}

	public Rectangle getBounds() {
		if (bounds == null) {
			if (this.getCenterWidth() <= 0)
				return super.getBounds();
			bounds = getcenterCurvePointList().getBounds();
			bounds.expand(lineWidth / 2, lineWidth / 2);

			for (int i = 0; i < getChildren().size(); i++) {
				IFigure child = (IFigure) getChildren().get(i);
				bounds.union(child.getBounds());
			}
		}
		return bounds;
	}

	private void buildBaseCenterCurve(int w) {
		double width = w;
		int count = getCenterWidth();
		if (count < 0)
			return;
		fBasicCenterCurve = new double[count];
		for (int i = 0; i < getCenterWidth(); i++) {
			double r = i / width;
			fBasicCenterCurve[i] = Math.cos(Math.PI * r);
		}
	}

	private int getCenterWidth() {
		if (this.getPoints().size() == 0)
			return -1;
		Point start = this.getStart();
		Point end = this.getEnd();
		int w = Math.abs(start.x - end.x);
		if (w <= 0)
			return -1;
		return Math.abs(w);
	}
}
