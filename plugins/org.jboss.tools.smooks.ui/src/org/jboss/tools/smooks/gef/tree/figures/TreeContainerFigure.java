/**
 * 
 */
package org.jboss.tools.smooks.gef.tree.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.jboss.tools.smooks.gef.tree.model.TreeContainerModel;

/**
 * @author DartPeng
 * 
 */
public class TreeContainerFigure extends Figure {

	private IFigure headerFigure;

	private IFigure contentFigure;
	
	private Label label;
	
	private TreeContainerModel model;
	
	private Color headerColor = ColorConstants.button;
	
	public TreeContainerFigure(TreeContainerModel model) {
		super();
		this.model = model;
		headerFigure = new Figure() {

			@Override
			protected void paintFigure(Graphics graphics) {
				super.paintFigure(graphics);
				graphics.pushState();
				graphics.setForegroundColor(headerColor);
				graphics.setBackgroundColor(ColorConstants.white);
				graphics.fillGradient(getBounds(), true);
				graphics.setForegroundColor(headerColor);
				graphics.drawLine(getBounds().getBottomLeft().translate(0, -1),
						getBounds().getBottomRight().translate(0, -1));
				graphics.popState();
			}

			@Override
			public Dimension getPreferredSize(int hint, int hint2) {
				Dimension size = super.getPreferredSize(hint, hint2);
				return new Dimension(size.width,25);
			}
		};
		label = new Label();
		headerFigure.add(label);
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		headerFigure.setLayoutManager(layout);
		headerFigure.setOpaque(true);
		headerFigure.setBackgroundColor(ColorConstants.blue);

		contentFigure = new Figure();
		contentFigure.setBackgroundColor(ColorConstants.red);
		contentFigure.setLayoutManager(new ToolbarLayout());
		this.add(headerFigure);
		this.add(contentFigure);
		this.setLayoutManager(new ToolbarLayout());
	}

	/**
	 * @return the label
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(Label label) {
		this.label = label;
	}

	public IFigure getContentFigure() {
		return contentFigure;
	}
	
	public Color getHeaderColor() {
		return headerColor;
	}

	public void setHeaderColor(Color headerColor) {
		this.headerColor = headerColor;
	}

	public Rectangle getBounds() {
		Rectangle rect = super.getBounds();
		if (getLayoutManager() != null) {
			Dimension d = getLayoutManager().getPreferredSize(this, -1, -1);
			rect.setSize(d);
		}
		return rect;
	}

	@Override
	protected void paintBorder(Graphics graphics) {
		graphics.setForegroundColor(ColorConstants.buttonDarker);
		Rectangle drawnRectangle = new Rectangle(getBounds().x, getBounds().y,
				getBounds().width - 1, getBounds().height - 1);
		graphics.drawRoundRectangle(drawnRectangle, 5, 5);
	}
	
	public void setText(String text){
		if(label != null){
			label.setText(text);
		}
	}
	
	public TreeContainerModel getModel() {
		return model;
	}

	public void setModel(TreeContainerModel model) {
		this.model = model;
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		graphics.pushState();
		graphics.setAlpha(190);
		graphics.setBackgroundColor(ColorConstants.white);
		graphics.fillRectangle(getBounds());
		graphics.popState();
	}
}
