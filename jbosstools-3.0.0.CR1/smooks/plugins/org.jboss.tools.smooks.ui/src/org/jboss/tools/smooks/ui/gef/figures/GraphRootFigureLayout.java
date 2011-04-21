/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.gef.figures;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author Dart Peng
 * @Date Jul 30, 2008
 */
public class GraphRootFigureLayout extends XYLayout {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	public void layout(IFigure container) {
		super.layout(container);
		List list = container.getChildren();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			IFigure figure = (IFigure) iterator.next();
			if (figure instanceof SourceConnectionPointFigure) {
				((SourceConnectionPointFigure)figure).caculateLocationY();
				figure.setLocation(new Point(0,figure.getBounds().y));
				figure.setSize(0, 0);
			}
			if (figure instanceof TargetConnectionPointFigure) {
				((TargetConnectionPointFigure)figure).caculateLocationY();
				int x = container.getClientArea().width - 2;
				figure.setLocation(new Point(x,figure.getBounds().y));
				figure.setSize(0, 0);
			}
		}
	}

}
