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
import org.jboss.tools.smooks.ui.gef.figures.SourceConnectionPointFigure;
import org.jboss.tools.smooks.ui.gef.model.TreeItemRelationModel;

/**
 * @author Dart Peng
 * @Date Jul 30, 2008
 */
public class SourceConnectionPointEditPart extends ConnectionPointEditPart {

	@Override
	protected IFigure createFigure() {
		SourceConnectionPointFigure figure = new SourceConnectionPointFigure();
		figure.setSize(50,50);
		TreeItemRelationModel model = (TreeItemRelationModel)getModel();
		figure.setModel(model);//(model.getTreeItem());
		return figure;
	}

}
