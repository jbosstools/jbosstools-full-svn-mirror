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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jboss.tools.smooks.ui.gef.model.TreeItemRelationModel;

/**
 * @author Dart Peng
 * @Date Jul 31, 2008
 */
public class TreeItemRelationFigure extends RectangleFigure {
//	TreeItem treeItem = null;
//	int caculateY = 0;

//	public TreeItem getTreeItem() {
//		return treeItem;
//	}
//
//	public void setTreeItem(TreeItem treeItem) {
//		this.treeItem = treeItem;
//	}
	
	private TreeItemRelationModel model = null;

	public TreeItemRelationModel getModel() {
		return model;
	}

	public void setModel(TreeItemRelationModel model) {
		this.model = model;
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		graphics.setBackgroundColor(ColorConstants.black);
		graphics.fillRectangle(getBounds());
//		graphics.pushState();
//
//		graphics.setForegroundColor(ColorConstants.red);
//		if (!treeItem.isDisposed()) {
//			String text = treeItem.getText();
//			if (text != null)
//				graphics.drawText(text, getBounds().getTopLeft());
//		}
//
//		graphics.popState();
	}

	public void caculateLocationY() {
		TreeItem treeItem = this.getModel().getTreeItem();
		if (treeItem != null && !treeItem.isDisposed()) {
			int y = this.collapsedParentItemLocation(treeItem);
			Tree tree = treeItem.getParent();
			int height = tree.getItemHeight();
			this.setLocation(new Point(0, y + height / 2));
		}
	}

	protected int collapsedParentItemLocation(TreeItem item) {
		int y = 0;
		if (item != null && !item.isDisposed()) {
			y = item.getBounds().y;
			if (y == 0) {
				if (item.getParentItem() != null)
					return this.collapsedParentItemLocation(item
							.getParentItem());
			}
			return y;
		}
		return y;
	}

}
