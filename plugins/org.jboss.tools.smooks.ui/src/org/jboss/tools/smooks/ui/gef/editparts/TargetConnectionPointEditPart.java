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
package org.jboss.tools.smooks.ui.gef.editparts;

import org.eclipse.draw2d.IFigure;
import org.jboss.tools.smooks.ui.gef.figures.TargetConnectionPointFigure;
import org.jboss.tools.smooks.ui.gef.model.TreeItemRelationModel;

/**
 * @author Dart Peng
 * @Date Jul 30, 2008
 */
public class TargetConnectionPointEditPart extends ConnectionPointEditPart {
	@Override
	protected IFigure createFigure() {
		TargetConnectionPointFigure figure = new TargetConnectionPointFigure();
		figure.setSize(10,10);
		TreeItemRelationModel model = (TreeItemRelationModel)getModel();
		figure.setModel(model);
		return figure;
	}

}
