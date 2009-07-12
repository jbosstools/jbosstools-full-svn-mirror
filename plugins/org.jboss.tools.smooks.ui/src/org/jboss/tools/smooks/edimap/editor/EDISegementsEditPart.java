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
package org.jboss.tools.smooks.edimap.editor;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.editor.FormPage;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.tree.editparts.TreeContainerEditPart;
import org.jboss.tools.smooks.gef.tree.figures.IMoveableModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.model.graphics.ext.FigureType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.medi.Segments;

/**
 * @author Dart (dpeng@redhat.com)
 *
 */
public class EDISegementsEditPart extends TreeContainerEditPart {

	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if (IMoveableModel.PRO_BOUNDS_CHANGED.equals(evt.getPropertyName())) {
			DefaultEditDomain domain = (DefaultEditDomain) getViewer().getEditDomain();
			IEditorPart editor = domain.getEditorPart();
			if((editor instanceof FormPage)){
				editor = ((FormPage)editor).getEditor();
			}
			
			if(editor instanceof ISmooksModelProvider && getModel() instanceof IMoveableModel){
				SmooksGraphicsExtType graph = ((ISmooksModelProvider)editor).getSmooksGraphicsExt();
				Rectangle rect = ((IMoveableModel)getModel()).getBounds();
				recordBounds(graph,rect);
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart#recordFigureBounds(org.jboss.tools.smooks.model.graphics.ext.FigureType, org.eclipse.draw2d.geometry.Rectangle)
	 */
	@Override
	protected void recordFigureBounds(FigureType figureType, Rectangle bounds) {
		figureType.setX(String.valueOf(bounds.getLocation().x));
		figureType.setY(String.valueOf(bounds.getLocation().y));
	}
	
	public static String generateFigureId(Object data){
		if(data instanceof Segments){
//			String xml = ((MappingNode)data).getXmltag();
//			return "segments_" + xml;
			return "segments";
		}
		return null;
	}


	protected String generateFigureID(){
		TreeNodeModel model = (TreeNodeModel) getModel();
		Object data = model.getData();
		return generateFigureId(data);
	}
}
