/**
 * 
 */
package org.jboss.tools.smooks.graphical.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Dart
 *
 */
public class ContainerFigure extends Figure {

	private FreeformLayer pane;
	
	

	public ContainerFigure() {
		super();
		this.setBorder(new ContainerBorder());
		ScrollPane scrollpane = new ScrollPane();
		pane = new FreeformLayer();
		pane.setLayoutManager(new FreeformLayout());
		setLayoutManager(new StackLayout());
		add(scrollpane);
		scrollpane.setViewport(new FreeformViewport());
		scrollpane.setContents(pane);

		setBackgroundColor(ColorConstants.listBackground);
		setOpaque(true);
	}
	
	public IFigure getContentsPane(){
		return pane;
	}

	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension(400,500);
	}

	protected void paintFigure(Graphics graphics) {
		Rectangle rect = getBounds().getCopy();
		rect.crop(new Insets(2,0,2,0));
		graphics.fillRectangle(rect);
	}

}
