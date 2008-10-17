package org.jboss.tools.smooks.ui.gef.util.figures;

import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * @deprecated
 *
 */
public class Placeholder extends RectangleFigure {
	
	public Placeholder() {
		super();
		setPreferredSize(new Dimension(9, 9));
		this.setVisible(false);
	}
}
