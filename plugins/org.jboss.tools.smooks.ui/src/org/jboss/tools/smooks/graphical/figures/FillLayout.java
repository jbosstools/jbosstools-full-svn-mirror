/**
 * 
 */
package org.jboss.tools.smooks.graphical.figures;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * @author Dart
 * 
 */
public class FillLayout extends AbstractLayout {

	private Control canvas;

	public FillLayout(Control canvas) {
		this.canvas = canvas;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.
	 * draw2d.IFigure, int, int)
	 */
	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		return container.getPreferredSize(wHint, hHint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	public void layout(IFigure container) {
		List<?> children = container.getChildren();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			IFigure child = (IFigure) iterator.next();

			Rectangle rect = container.getBounds();// getClientArea(Rectangle.SINGLETON);
			if (canvas != null) {
				rect = new Rectangle(canvas.getBounds().x, canvas.getBounds().y, canvas.getBounds().width, canvas.getBounds().height);
			}
			int cw = rect.width / 3 ;
			int ch = rect.height - 60 ;
			cw = Math.min(cw, 800);
			ch = Math.min(ch, 800);
			int x = 30;
			int y = 30;
			if (child instanceof TargetContainerFigure) {
				x = cw*2 - 30;
			}
			child.setBounds(new Rectangle(x, y, cw, ch));
		}
	}

}
