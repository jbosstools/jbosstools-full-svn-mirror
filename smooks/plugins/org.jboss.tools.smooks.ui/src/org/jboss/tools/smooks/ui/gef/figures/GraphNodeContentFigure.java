package org.jboss.tools.smooks.ui.gef.figures;

import org.eclipse.draw2d.IFigure;
import org.jboss.tools.smooks.ui.gef.util.figures.IConnectedFigure;


//------------------------------
//| GraphNodeContentFigure     |
//|                            |
//| -------------------------  |
//| | vertical group        |  |
//| | --------------------- |  |
//| | | outlined area     | |  |
//| | | ----------------- | |  |
//| | | | icon area     | | |  |
//| | | ----------------- | |  |
//| | | ----------------- | |  |
//| | | | inner content | | |  |
//| | | ----------------- | |  |
//| | --------------------- |  |
//| -------------------------  |
//------------------------------

public class GraphNodeContentFigure extends ContainerFigure implements IConnectedFigure {
	
	protected ContainerFigure verticalGroup;
	protected ContainerFigure outlinedArea;
	protected ContainerFigure iconArea;
	protected ContainerFigure innerContentArea; 
	
	public GraphNodeContentFigure() {
		createFigure();
	}

	public IFigure getConnectionFigure() {
		return outlinedArea;
	}
	
	public ContainerFigure getIconArea() {
		return iconArea;
	}
	
	public ContainerFigure getOutlinedArea() {
		return outlinedArea;
	}
	
	public ContainerFigure getInnerContentArea() {
		return innerContentArea;
	}
	
	protected void createFigure() {
		createVerticalGroup(this);
		createOutlinedArea(verticalGroup);
	}
	
	protected void createVerticalGroup(IFigure parent) {
		verticalGroup = new ContainerFigure();
		verticalGroup.getContainerLayout().setHorizontal(false);
		parent.add(verticalGroup);
	}
	
	protected void createOutlinedArea(IFigure parent) {
		outlinedArea = new ContainerFigure();
		outlinedArea.getContainerLayout().setHorizontal(false);
//		outlinedArea.setBackgroundColor(ColorConstants.red);
//		outlinedArea.setFill(true);
		parent.add(outlinedArea);    
		
		iconArea = new IconAreaFigure();
		iconArea.getContainerLayout().setHorizontal(true);
		outlinedArea.add(iconArea);
		
		innerContentArea = new ContainerFigure();
		innerContentArea.getContainerLayout().setHorizontal(false);
//		innerContentArea.setBackgroundColor(ColorConstants.red);
//		innerContentArea.setFill(true);
		outlinedArea.add(innerContentArea);
	}
}