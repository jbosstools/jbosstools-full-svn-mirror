package org.jboss.tools.smooks.ui.gef.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.jboss.tools.smooks.ui.gef.util.figures.ContainerLayout;


public class ContainerFigure extends RectangleFigure implements IExpandable {

	protected boolean isOutlined = false;

	protected boolean isExpanded = true;

	public ContainerFigure() {
		setLayoutManager(new ContainerLayout());
		setFill(false);
	}

	public void doLayout() {
		layout();
		setValid(true);
	}

	public ContainerLayout getContainerLayout() {
		return (ContainerLayout) getLayoutManager();
	}

	public void setOutlined(boolean isOutlined) {
		this.isOutlined = isOutlined;
	}

	protected void outlineShape(Graphics graphics) {
		if (isOutlined) {
			super.outlineShape(graphics);
		}
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.RectangleFigure#fillShape(org.eclipse.draw2d.Graphics)
	 */

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

}