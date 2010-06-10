/**
 * 
 */
package org.jboss.tools.smooks.graphical.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;

/**
 * @author Dart
 *
 */
public class ContainerBorder extends AbstractBorder {
	protected static Insets insets = new Insets(2,2,2,2);
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets(IFigure figure) {
		return insets;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Border#paint(org.eclipse.draw2d.IFigure, org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
	 */
	public void paint(IFigure figure, Graphics g, Insets insets) {
		Rectangle r = figure.getBounds().getCropped(insets);
		
		g.setForegroundColor(GraphicsConstants.TB_BG_CORLOR);
		g.setBackgroundColor(GraphicsConstants.TB_BG_CORLOR);
		
		//Draw the sides of the border
		g.fillRectangle(r.x, r.y + 2, r.width, 6);
		g.fillRectangle(r.x, r.bottom() - 8, r.width, 6);
		g.fillRectangle(r.x, r.y + 2, 6, r.height - 4);
		g.fillRectangle(r.right() - 6, r.y + 2, 6, r.height - 4);

//		//Outline the border
//		g.setForegroundColor(ColorConstants.red);
//		g.drawLine(r.x, r.y + 2, r.right() - 1, r.y + 2);
//		g.drawLine(r.x, r.bottom() - 3, r.right() - 1, r.bottom() - 3);
//		g.drawLine(r.x, r.y + 2, r.x, r.bottom() - 3);
//		g.drawLine(r.right() - 1, r.bottom() - 3, r.right() - 1, r.y + 2);
//		
//		r.crop(new Insets(1, 1, 0, 0));
//		r.expand(1, 1);
//		r.crop(getInsets(figure));
	}

}
