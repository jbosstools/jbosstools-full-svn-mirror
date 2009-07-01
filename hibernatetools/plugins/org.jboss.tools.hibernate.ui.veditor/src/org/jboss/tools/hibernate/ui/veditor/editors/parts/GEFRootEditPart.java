/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.hibernate.ui.veditor.editors.parts;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
//import org.eclipse.gef.LayerConstants; 

public class GEFRootEditPart extends ScalableFreeformRootEditPart {

	public GEFRootEditPart() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public void setToFront(AbstractConnectionEditPart editpart) {
		ConnectionLayer layer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
		IFigure fig = editpart.getFigure();
		layer.getChildren().remove(fig);
		layer.getChildren().add(fig);
	}
	

}
