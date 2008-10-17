package org.jboss.tools.smooks.ui.gef.figures;

import org.eclipse.draw2d.IFigure;

public interface IExpandable extends IFigure {
	boolean isExpanded();

	void setExpanded(boolean isExpanded);
}
