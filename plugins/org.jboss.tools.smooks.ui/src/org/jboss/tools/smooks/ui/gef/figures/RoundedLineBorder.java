package org.jboss.tools.smooks.ui.gef.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;

public class RoundedLineBorder extends LineBorder {
	protected int arcLength;

	protected int lineStyle = Graphics.LINE_SOLID;

	public RoundedLineBorder(Color c, int width, int arcLength) {
		super(c, width);
		this.arcLength = arcLength;
	}

	public RoundedLineBorder(int width, int arcLength) {
		super(width);
		this.arcLength = arcLength;
	}

	public RoundedLineBorder(Color c, int width, int arcLength, int lineStyle) {
		super(c, width);
		this.arcLength = arcLength;
		this.lineStyle = lineStyle;
	}

	public RoundedLineBorder(int width, int arcLength, int lineStyle) {
		super(width);
		this.arcLength = arcLength;
		this.lineStyle = lineStyle;
	}

	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		tempRect.setBounds(getPaintRectangle(figure, insets));
		int width = getWidth();
		Color color = getColor();
		if (width % 2 == 1) {
			tempRect.width--;
			tempRect.height--;
		}
		tempRect.shrink(width / 2, width / 2);
		graphics.setLineWidth(width);
		graphics.setLineStyle(lineStyle);
		if (color != null)
			graphics.setForegroundColor(color);
		graphics.drawRoundRectangle(tempRect, arcLength, arcLength);
	}
}
