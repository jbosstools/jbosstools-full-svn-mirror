package org.jboss.tools.smooks.ui.gef.util.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Interactor the +/- control commonly found in trees
 */
public class Interactor extends RectangleFigure {

	protected boolean isExpanded = true;

	public Interactor() {
		super();
		setPreferredSize(new Dimension(9, 9));
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
		repaint();
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	protected void fillShape(Graphics g) {
		super.fillShape(g);
		Rectangle r = getBounds();
		int mx = r.x + r.width / 2;
		int my = r.y + r.height / 2;
		int s = 2;
		g.drawLine(r.x + s, my, r.x + r.width - s - 1, my);
		
		if (!isExpanded) {
			g.drawLine(mx, r.y + s, mx, r.y + r.height - s - 1);
		}
	}
}