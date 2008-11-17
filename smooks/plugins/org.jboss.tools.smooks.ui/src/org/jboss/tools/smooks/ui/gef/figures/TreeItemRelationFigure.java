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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author Dart Peng
 * @Date Jul 31, 2008
 */
public class TreeItemRelationFigure extends RectangleFigure {
	TreeItem treeItem = null;
	int caculateY = 0;

	public TreeItem getTreeItem() {
		return treeItem;
	}

	public void setTreeItem(TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		graphics.pushState();

		graphics.setForegroundColor(ColorConstants.red);
		graphics.drawText(treeItem.getText(), getBounds().getTopLeft());

		graphics.popState();
	}

	public void caculateLocationY() {
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
