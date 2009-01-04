/**
 * 
 */
package org.jboss.tools.smooks.ui.gef.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;

/**
 * @author Dart
 * 
 */
public interface ILineFigurePaintListener {
	public void drawLineAdditions(Graphics graphics , IFigure hostFigure, LineConnectionModel model);
}
