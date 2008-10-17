package org.jboss.tools.smooks.ui.gef.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.jboss.tools.smooks.ui.gef.util.figures.Interactor;


//--------------------------------------------
//| ExpandableGraphNodeContentFigure         |
//|                                          |
//|   ----------------------------------     |
//|   | verticalGroup                  |     |
//|   |                                |     |
//|   | -----------------------------  |     |
//|   | | horizontalGroup           |  |     |
//|   | |                           |  |     |
//|   | | ---------------------     |  |     |
//|   | | | outlinedArea      |     |  |     |
//|   | | | ----------------- |     |  |     |
//|   | | | |[+]iconArea    | |     |  |     |
//|   | | | ----------------- |     |  |     |
//|   | | | ----------------- |     |  |     |
//|   | | | | innerContent  | |     |  |     |
//|   | | | ----------------- |     |  |     |
//|   | | ---------------------     |  |     |
//|   | -----------------------------  |     |
//|   |                                |     |
//|   ----------------------------------     |
//--------------------------------------------

public class ExpandableGraphNodeContentFigure extends GraphNodeContentFigure {
	
	protected IFigure interactor;
	
	public ExpandableGraphNodeContentFigure() {
		super();
	}
	
	public Interactor getInteractor() {
		return (Interactor)interactor;
	}
	
	protected void createFigure() {
		createVerticalGroup(this);
		createOutlinedArea(verticalGroup);
//		createInteractor(iconArea);
		innerContentArea.setBorder(new MarginBorder(0, 10, 0, 0));
	}
	
	protected void createInteractor(IFigure parent) {
//		interactor = new Interactor();
//		interactor.setBorder(new MarginBorder(0, 0, 0, 5));
//		interactor.setForegroundColor(ColorConstants.black);
//		interactor.setBackgroundColor(ColorConstants.white);
//		parent.add(interactor);
	}
	
	protected void createPreceedingSpace(IFigure parent) {
		RectangleFigure space = new RectangleFigure();
		space.setVisible(false);
		space.setPreferredSize(new Dimension(10, 10));
		parent.add(space);
	}

	/**
	 */
	public void setInteractor(IFigure interactor) {
		this.interactor = interactor;
		iconArea.add(interactor);
	}
}