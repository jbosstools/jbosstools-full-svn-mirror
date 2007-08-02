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
package org.jboss.tools.shale.ui.clay.editor.figures;

import org.eclipse.draw2d.*;

import org.jboss.tools.shale.ui.clay.editor.edit.ClayDiagramEditPart;

public class DiagramFigure extends FreeformLayer implements IFigure {
	//public static final Color lightGrayColor = new Color(null, 0xf1, 0xf1, 0xf1);
	private ClayDiagramEditPart editPart;
	
	public DiagramFigure(ClayDiagramEditPart editPart){
		super();
		this.editPart = editPart;
		setLayoutManager(new ClayDiagramLayout(editPart.getClayModel().getOptions()));
		setBorder(new MarginBorder(5));
		setBackgroundColor(ColorConstants.white);
		setOpaque(true);
	}
	
	public ClayDiagramEditPart getEditPart(){
		return editPart;
	}
}
