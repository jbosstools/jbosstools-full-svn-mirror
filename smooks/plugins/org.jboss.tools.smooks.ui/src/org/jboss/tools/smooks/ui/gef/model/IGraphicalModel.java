package org.jboss.tools.smooks.ui.gef.model;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 
 * @author Dart Peng
 * 
 */
public interface IGraphicalModel {

	public int getX();

	public void setX(int x);

	public int getY();

	public void setY(int y);

	public int getWidth();

	public void setWidht(int widht);

	public int getHeight();

	public void setHeight(int height);

	public Rectangle getConstraint();

	/**
	 * Set new constraint to this model.If the new rectangle is equals old
	 * constraint , the method will do nothing
	 * 
	 * @param rect
	 *            Constraint bounds rectangle
	 */
	public void setConstraint(Rectangle rect);

}
