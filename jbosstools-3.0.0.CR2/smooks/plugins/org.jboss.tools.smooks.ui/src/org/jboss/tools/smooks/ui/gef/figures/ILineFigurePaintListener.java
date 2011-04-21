/**
 * 
 */
package org.jboss.tools.smooks.ui.gef.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;

/**
 * @author Dart
 * 
 */
public interface ILineFigurePaintListener {
	public void drawLineAdditions(Graphics graphics , IFigure hostFigure, LineConnectionModel model);
	
	public void drawLineSourceLocator(Graphics graphics , IFigure hostFigure, LineConnectionModel model);
	
	public void drawLineTargetLocator(Graphics graphics , IFigure hostFigure, LineConnectionModel model);
	/**
	 * Return the line you wanted . If return figure is NOT null , the drawLineAdditions method will be disabled
	 * @param model
	 * @return
	 */
	public PolylineConnection createHostFigure(LineConnectionModel model);
	
	/**
	 * 
	 * @param sid
	 * @param tid
	 * @return
	 */
	public PolylineConnection createDummyFigure(CreateConnectionRequest req);
}
