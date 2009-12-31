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
package org.jboss.tools.smooks.graphical.editors.editparts.freemarker;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.jboss.tools.smooks.gef.common.RootModel;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart;
import org.jboss.tools.smooks.gef.tree.figures.TreeNodeFigure;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.IFreemarkerTemplateModel;

/**
 * @author Dart
 * 
 */
public class FreemarkerXMLNodeEditPart extends TreeNodeEditPart {
	private Font hiddenLabelFont;

	private Font oldFont;

	private Color oldColor;

	@Override
	public void deactivate() {
		super.deactivate();
		if (hiddenLabelFont != null) {
			hiddenLabelFont.dispose();
		}
	}

	/**
	 * @return the xslLabelFont
	 */
	public Font getHiddenLabelFont() {
		if (hiddenLabelFont == null) {
			IFigure figure = getFigure();

			FontData fd = new FontData();
			fd.setStyle(SWT.ITALIC);
			if (figure != null) {
				FontData[] fds = figure.getFont().getFontData();
				if (fds != null && fds.length > 0) {
					fd.setHeight(fds[0].getHeight());
					fd.setName(fds[0].getName());
					fd.setLocale(fds[0].getLocale());
				}
			}
			hiddenLabelFont = new Font(null, fd);
		}
		return hiddenLabelFont;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart#canDirectEdit
	 * ()
	 */
	@Override
	protected boolean canDirectEdit() {
		// TODO Auto-generated method stub
		return super.canDirectEdit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart#isDragLink()
	 */
	@Override
	protected boolean isDragLink() {
		// TODO Auto-generated method stub
		return super.isDragLink();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart#refreshVisuals
	 * ()
	 */
	@Override
	protected void refreshVisuals() {
		AbstractSmooksGraphicalModel model = (AbstractSmooksGraphicalModel) getModel();
		TreeNodeFigure figure = (TreeNodeFigure) getFigure();
		Object data = model.getData();
		AbstractSmooksGraphicalModel parent = model;
		while (parent != null && !(parent instanceof RootModel)) {
			parent = parent.getParent();
		}
		if (oldFont != null && oldColor != null) {
			figure.getLabel().setFont(oldFont);
			figure.setNodeLabelForegroundColor(oldColor);
		}
		Object rmodel = parent;
		super.refreshVisuals();
		if (data instanceof IFreemarkerTemplateModel && rmodel instanceof RootModel) {
			if (((IFreemarkerTemplateModel) data).isHidden((RootModel) rmodel)) {
				if (oldFont == null) {
					oldFont = figure.getLabel().getFont();
				}
				if (oldColor == null) {
					oldColor = figure.getLabel().getForegroundColor();
				}
				figure.getLabel().setFont(getHiddenLabelFont());
				figure.setNodeLabelForegroundColor(ColorConstants.lightGray);
				figure.getLabel().repaint();
			}
		}
	}

}
