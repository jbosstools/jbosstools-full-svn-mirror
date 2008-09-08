package org.jboss.tools.smooks.ui.gef.util.figures;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.smooks.ui.gef.figures.IExpandable;
import org.jboss.tools.smooks.ui.gef.figures.SpacingFigure;

public class ContainerLayout extends AbstractLayout {
	protected boolean isHorizontal;

	private int align = PositionConstants.LEFT;

	protected int spacing = 0;

	protected int border = 0;

	public ContainerLayout() {
		this(true, 0);
	}

	public int getAlign() {
		return align;
	}

	public void setAlign(int align) {
		this.align = align;
	}

	public ContainerLayout(boolean isHorizontal, int spacing) {
		this.isHorizontal = isHorizontal;
		this.spacing = spacing;
	}

	public void setHorizontal(boolean isHorizontal) {
		this.isHorizontal = isHorizontal;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	public void setBorder(int border) {
		this.border = border;
	}

	protected int alignFigure(IFigure parent, IFigure child) {
		return -1;
	}

	/**
	 * Calculates and returns the preferred size of the container given as
	 * input.
	 * 
	 * @param figure
	 *            Figure whose preferred size is required.
	 * @return The preferred size of the passed Figure.
	 */
	protected Dimension calculatePreferredSizeHelper(IFigure parent) {
		Dimension preferred = new Dimension();
		List children = parent.getChildren();

		for (int i = 0; i < children.size(); i++) {
			IFigure child = (IFigure) children.get(i);

			Dimension childSize = child.getPreferredSize();

			if (isHorizontal) {
				preferred.width += childSize.width;
				preferred.height = Math.max(preferred.height, childSize.height);
			} else {
				preferred.height += childSize.height;
				preferred.width = Math.max(preferred.width, childSize.width);
			}
		}

		int childrenSize = children.size();
		if (childrenSize > 1) {
			if (isHorizontal) {
				preferred.width += spacing * (childrenSize - 1);
			} else {
				preferred.height += spacing * (childrenSize - 1);
			}
		}

		preferred.width += border * 2;
		preferred.height += border * 2;
		preferred.width += parent.getInsets().getWidth();
		preferred.height += parent.getInsets().getHeight();

		return preferred;
	}

	protected Dimension calculatePreferredSize(IFigure parent, int width,
			int height) {
		Dimension preferred = null;

		// Here we ensure that an unexpanded container is given a size of (0,0)
		//
		if (parent instanceof IExpandable) {
			IExpandable expandableFigure = (IExpandable) parent;
			if (!expandableFigure.isExpanded()) {
				preferred = new Dimension();
			}
		}

		if (preferred == null) {
			preferred = calculatePreferredSizeHelper(parent);
		}

		return preferred;
	}

	protected void adjustLayoutLocation(IFigure parent, Dimension dimension) {
	}

	public void layout(IFigure parent) {
		List children = parent.getChildren();

		int rx = 0;

		Dimension dimension = new Dimension();

		for (int i = 0; i < children.size(); i++) {
			IFigure child = (IFigure) children.get(i);
			Dimension childSize = child.getPreferredSize();
			if (isHorizontal) {
				dimension.height = Math.max(dimension.height, childSize.height);
				rx += childSize.width;
			} else {
				dimension.width = Math.max(dimension.width, childSize.width);
			}
		}

		int tempwidth = rx;

		// dimension.width += parent.getInsets().left;
		// dimension.height += parent.getInsets().top;

		if (isHorizontal) {
			dimension.height += border * 2;
			dimension.width += border;
		} else {
			dimension.width += border * 2;
			dimension.height += border;
		}

		
		int maxChildWidth = 0;
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			IFigure childf = (IFigure) iterator.next();
			maxChildWidth = Math.max(childf.getPreferredSize().width, maxChildWidth);
		}
		adjustLayoutLocation(parent, dimension);
		// Add by DartPeng Jul 16 2008
		int myh = 0;
//		if (isHorizontal && align == PositionConstants.RIGHT) {
//			for (int i = children.size() - 1; i >= 0; i--) {
//				IFigure child = (IFigure) children.get(i);
//				Dimension childSize = child.getPreferredSize();
//
//				if (isHorizontal) {
//					int y = -1;
//
//					y = alignFigure(parent, child);
//
//					if (y == -1) {
//						y = (dimension.height - childSize.height) / 2;
//					}
//					myh += childSize.width;
//					myh += spacing;
//					Rectangle rectangle = new Rectangle(
//							parent.getClientArea().width - myh, y,
//							childSize.width, childSize.height);
//					rectangle.translate(parent.getClientArea().getLocation());
//
//					child.setBounds(rectangle);
//					dimension.width += childSize.width;
//					dimension.width += spacing;
//					if (child instanceof SpacingFigure) {
//						int availableHorizontalSpace = parent.getClientArea().width
//								- rx;
//						dimension.width += availableHorizontalSpace;
//						myh += availableHorizontalSpace;
//					}
//				}
//			}
//			return;
//		}

		for (int i = 0; i < children.size(); i++) {
			IFigure child = (IFigure) children.get(i);
			Dimension childSize = child.getPreferredSize();

			if (isHorizontal) {
				int y = -1;

				y = alignFigure(parent, child);

				if (y == -1) {
					y = (dimension.height - childSize.height) / 2;
				}

				int adjust = dimension.width;

				Rectangle rectangle = new Rectangle(adjust, y, childSize.width,
						childSize.height);
				rectangle.translate(parent.getClientArea().getLocation());

				child.setBounds(rectangle);
				dimension.width += childSize.width;
				dimension.width += spacing;
				tempwidth -= childSize.width;
				if (child instanceof SpacingFigure) {
					int availableHorizontalSpace = parent.getClientArea().width
							- rx;
					dimension.width += availableHorizontalSpace;
					tempwidth -= availableHorizontalSpace;
				}
			} else {
				int adjust = 0;
				// Modify by Dart 2008.7.17
//				switch (this.align) {
//				case PositionConstants.LEFT:
//					break;
//				case PositionConstants.RIGHT:
//					adjust = parent.getClientArea().width - dimension.width;
//				}
				
				Rectangle rectangle = new Rectangle(adjust, dimension.height,
						maxChildWidth, childSize.height);
				rectangle.translate(parent.getClientArea().getLocation());

				child.setBounds(rectangle);
				dimension.height += childSize.height;
				dimension.height += spacing;
			}
		}
	}
}