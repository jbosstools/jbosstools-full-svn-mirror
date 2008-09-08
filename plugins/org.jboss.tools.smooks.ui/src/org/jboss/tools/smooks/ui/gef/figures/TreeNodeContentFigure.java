package org.jboss.tools.smooks.ui.gef.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.jboss.tools.smooks.ui.gef.util.GraphicsConstants;

/**
 * 
 * @author Dart Peng
 * 
 * @CreateTime Jul 21, 2008
 */
public class TreeNodeContentFigure extends ExpandableGraphNodeContentFigure
		implements ISelectableFigure {

	private boolean selected = false;

	/**
	 * @return the selected
	 */
	public boolean getSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		this.repaint();
	}

	/**
	 * @return the focus
	 */
	public boolean getFocus() {
		return focus;
	}

	/**
	 * @param focus
	 *            the focus to set
	 */
	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	private boolean focus = false;

	public IFigure getConnectionFigure() {
		return outlinedArea;
	}

	protected void createOutlinedArea(IFigure parent) {
		super.createOutlinedArea(parent);
		outlinedArea.setBorder(new MarginBorder(0, 2, 0, 2));
	}

	protected void fillShape(Graphics graphics) {
		super.fillShape(graphics);
	}
	
	public void paintFigure(Graphics graphics){
		super.paintFigure(graphics);
		ContainerFigure figure = this.getIconArea();
		if (this.selected) {
			figure.setFill(true);
			figure.setBackgroundColor(GraphicsConstants.groupHeaderColor);
		} else {
			figure.setFill(false);
			figure.setBackgroundColor(ColorConstants.white);
		}
	}
}
