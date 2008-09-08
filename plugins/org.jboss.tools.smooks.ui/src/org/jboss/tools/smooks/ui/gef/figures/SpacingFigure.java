package org.jboss.tools.smooks.ui.gef.figures;

import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;

public class SpacingFigure extends RectangleFigure {
	public SpacingFigure() {
		setFill(false);
		setPreferredSize(new Dimension(0, 0));
	}
}
